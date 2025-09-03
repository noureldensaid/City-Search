package com.klivvr.citysearch.home.domain.useCase

import com.klivvr.citysearch.core.utils.DispatcherProvider
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.model.Trie
import com.klivvr.citysearch.home.domain.repository.CityRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A use case responsible for searching cities based on a given prefix.
 *
 * This class utilizes a Trie data structure for efficient prefix-based searching.
 * The Trie is built lazily on the first search request and then cached in memory
 * for subsequent searches, ensuring optimal performance. The construction of the Trie
 * is thread-safe.
 *
 * The search operation is case-insensitive and trims whitespace from the input prefix.
 * It is executed on a background thread provided by [DispatcherProvider].
 *
 * @property repo The repository to load the city data from.
 * @property dispatcherProvider Provides coroutine dispatchers for background operations.
 */
@Singleton
class SearchCitiesUseCase @Inject constructor(
    private val repo: CityRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    @Volatile
    private var trie: Trie? = null
    private val mutex = Mutex()

    private suspend fun getTrie(): Trie {
        trie?.let { return it }
        return mutex.withLock {
            trie ?: Trie().also { t ->
                repo.loadAll().forEach { t.insert(it.name.lowercase(), it) }
                trie = t
            }
        }
    }

    suspend operator fun invoke(prefix: String): List<CityModel> = withContext(dispatcherProvider.default){
        getTrie().search(prefix.trim().lowercase())
    }


}
