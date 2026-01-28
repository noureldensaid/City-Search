package com.klivvr.citysearch.home.domain.useCase

import com.klivvr.citysearch.core.base.ResponseState
import com.klivvr.citysearch.core.utils.DispatcherProvider
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.model.Trie
import com.klivvr.citysearch.home.domain.repository.CityRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


/**
 * A use case responsible for searching cities based on a given prefix.
 *
 * This class utilizes a [Trie] data structure for efficient prefix-based searching of city names.
 * The list of cities is loaded from the [CityRepository] and used to build the Trie.
 * To optimize performance, the Trie is built lazily upon the first search request and then cached in memory
 * for subsequent searches. The construction of the Trie and caching of the city list are handled in a thread-safe
 * manner using a [Mutex].
 *
 * The search operation is performed on a background thread provided by [DispatcherProvider]. It is case-insensitive
 * and handles leading/trailing whitespace in the search prefix.
 *
 * **Empty Query Behavior:**
 * When the search prefix is empty (after trimming), this use case returns the **full list of cities**.
 * This provides better UX by showing all available cities when the user clears the search field,
 * allowing them to browse the complete list or start a new search.
 *
 * The [invalidate] method can be called to clear the cached Trie and city list, forcing them to be rebuilt
 * on the next search request.
 *
 * @property repo The [CityRepository] responsible for providing the raw city data.
 * @property dispatcherProvider Provides coroutine dispatchers to run operations on appropriate threads.
 */
@Singleton
class SearchCitiesUseCase @Inject constructor(
    private val repo: CityRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    private val mutex = Mutex()
    @Volatile private var trie: Trie? = null
    @Volatile private var cachedCities: List<CityModel>? = null

    /**
     * Gets the cached Trie instance, building it if necessary.
     * Thread-safe and lazy - only builds once.
     */
    private suspend fun getTrie(): Trie = withContext(dispatcherProvider.default) {
        trie ?: mutex.withLock { trie ?: buildTrieLocked() }
    }

    /**
     * Builds the Trie from repository data.
     * Must be called within mutex lock.
     */
    private suspend fun buildTrieLocked(): Trie {
        return try {
            when (val response = repo.loadAll()) {
                is ResponseState.Success -> {
                    val cities = response.data
                    cachedCities = cities
                    Trie(cities).also { t ->
                        cities.forEachIndexed { idx, city ->
                            // Insert with validation - will throw if data is invalid
                            t.insert(city.normalizedName, idx)
                        }
                        trie = t
                    }
                }
                is ResponseState.Error -> {
                    // On error, create empty Trie to avoid repeated failed attempts
                    Trie(emptyList()).also { t ->
                        trie = t
                        cachedCities = emptyList()
                    }
                }
            }
        } catch (ce: CancellationException) {
            // Don't catch cancellation - propagate it
            throw ce
        } catch (e: Exception) {
            // If Trie construction fails (e.g., validation error), create empty Trie
            Trie(emptyList()).also { t ->
                trie = t
                cachedCities = emptyList()
            }
        }
    }

    /**
     * Searches for cities matching the given prefix.
     *
     * @param prefix The search query. Leading/trailing whitespace is automatically trimmed.
     *               Case-insensitive - will be converted to lowercase before searching.
     * @return A list of [CityModel] objects matching the prefix, sorted by name and country.
     *         Returns the **full list of cities** if the prefix is empty (after trimming).
     *
     * @example
     * ```
     * searchCities("par")  // Returns: [Paris, Parma, ...]
     * searchCities("  ")   // Returns: [All cities] (full list)
     * searchCities("")     // Returns: [All cities] (full list)
     * ```
     */
    suspend operator fun invoke(prefix: String): List<CityModel> =
        withContext(dispatcherProvider.default) {
            val query = prefix.trim()

            // Empty query = return all cities
            // This provides better UX: clearing search shows the full list
            if (query.isEmpty()) {
                // Ensure cache is initialized
                if (cachedCities == null) getTrie()
                return@withContext cachedCities ?: emptyList()
            }

            // Perform prefix search
            getTrie().search(query.lowercase())
        }

    /**
     * Invalidates the cached Trie and city list.
     * The next search will trigger a fresh load from the repository.
     *
     * Call this when:
     * - City data has been updated
     * - You want to free memory
     * - Testing different data sets
     */
    fun invalidate() {
        trie = null
        cachedCities = null
    }
}