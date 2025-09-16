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
 * and handles leading/trailing whitespace in the search prefix. If the search prefix is empty, it returns the
 * full cached list of cities.
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

    private suspend fun getTrie(): Trie = withContext(dispatcherProvider.default) {
        trie ?: mutex.withLock { trie ?: buildTrieLocked() }
    }

    private suspend fun buildTrieLocked(): Trie {
        return try {
            when (val response = repo.loadAll()) {
                is ResponseState.Success -> {
                    val cities = response.data
                    cachedCities = cities
                    Trie(cities).also { t ->
                        cities.forEachIndexed { idx, city ->
                            t.insert(city.normalizedName, idx)
                        }
                        trie = t
                    }
                }
                is ResponseState.Error -> {
                    Trie(emptyList()).also { t -> trie = t; cachedCities = emptyList() }
                }
            }
        } catch (ce: CancellationException) {
            throw ce
        }
    }

    suspend operator fun invoke(prefix: String): List<CityModel> =
        withContext(dispatcherProvider.default) {
            val q = prefix.trim()
            if (q.isEmpty()) {
                // ensure cache is initialized at least once
                if (cachedCities == null) getTrie()
                return@withContext cachedCities ?: emptyList()
            }
            getTrie().search(q.lowercase())
        }
}
