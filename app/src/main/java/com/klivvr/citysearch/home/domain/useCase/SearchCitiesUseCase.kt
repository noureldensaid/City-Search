package com.klivvr.citysearch.home.domain.useCase

import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.model.Trie
import com.klivvr.citysearch.home.domain.repository.CityRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchCitiesUseCase @Inject constructor(
    private val repo: CityRepository
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

    suspend operator fun invoke(prefix: String): List<CityModel> =
        getTrie().search(prefix.trim().lowercase())

}
