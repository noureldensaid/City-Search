package com.klivvr.citysearch.home.domain.useCase
import com.google.common.truth.Truth.assertThat
import com.klivvr.citysearch.core.utils.DefaultDispatcherProviderTest
import com.klivvr.citysearch.home.data.repository.CityRepositoryImplTest
import com.klivvr.citysearch.home.domain.model.CityModel
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchCitiesUseCaseTest {


    private fun seed() = listOf(
        CityModel(id = 1, name = "Cairo",      country = "EG", latitude = 30.0444, longitude = 31.2357, flagEmoji = "EG"),
        CityModel(id = 2, name = "Cairopolis", country = "EG", latitude = 29.9000, longitude = 31.2000, flagEmoji = "EG"),
        CityModel(id = 3, name = "Alexandria", country = "EG", latitude = 31.2001, longitude = 29.9187, flagEmoji = "EG"),
        CityModel(id = 4, name = "Calgary",    country = "CA", latitude = 51.0447, longitude = -114.0719, flagEmoji = "CA"),
        CityModel(id = 5, name = "cAi",        country = "US", latitude  = 40.7128,longitude = -74.0060, flagEmoji = "US"),
        )

    private fun seed2() = listOf(
        CityModel(id = 6,  name = "Alabama",     country = "US", latitude = 0.0, longitude = 0.0, flagEmoji = "US"),
        CityModel(id = 7,  name = "Albuquerque", country = "US", latitude = 0.0, longitude = 0.0, flagEmoji = "US"),
        CityModel(id = 8,  name = "Anaheim",     country = "US", latitude = 0.0, longitude = 0.0, flagEmoji = "US"),
        CityModel(id = 9,  name = "Arizona",     country = "US", latitude = 0.0, longitude = 0.0, flagEmoji = "US"),
        CityModel(id = 10, name = "Sydney",      country = "AU", latitude = 0.0, longitude = 0.0, flagEmoji = "AU"),
        )

    @Test
    fun `search is case-insensitive and trims`() = runTest {
        val dispatcher = DefaultDispatcherProviderTest(testScheduler)
        val repo = CityRepositoryImplTest(seed())
        val useCase = SearchCitiesUseCase(repo, dispatcher)

        val result = useCase("  CaI ")

        assertThat(result.map { it.name })
            .containsAtLeast("Cairo", "Cairopolis", "cAi")
        // ensure trie built once for first call
        assertThat(repo.calls).isEqualTo(1)
    }

    @Test
    fun `trie builds once and is reused across concurrent calls`() = runTest {
        val dispatcher = DefaultDispatcherProviderTest(testScheduler)
        val repo = CityRepositoryImplTest(seed())
        val useCase = SearchCitiesUseCase(repo, dispatcher)

        // Fire multiple concurrent searches; lazy init must run only once
        val jobs = List(6) { i ->
            async { useCase(if (i % 2 == 0) "ca" else "al") }
        }
        jobs.forEach { it.await() }

        // verify repo.loadAll() was called exactly once
        assertThat(repo.calls).isEqualTo(1)
    }

    @Test
    fun `prefix A returns all except Sydney`() = runTest {
        val dispatcher = DefaultDispatcherProviderTest(testScheduler)
        val repo = CityRepositoryImplTest(seed2())
        val useCase = SearchCitiesUseCase(repo, dispatcher)
        val result = useCase("A")
        assertThat(result.map { it.name }).containsExactly(
            "Alabama", "Albuquerque", "Anaheim", "Arizona"
        )
    }

    @Test
    fun `prefix s returns Sydney only`() = runTest {
        val dispatcher = DefaultDispatcherProviderTest(testScheduler)
        val repo = CityRepositoryImplTest(seed2())
        val useCase = SearchCitiesUseCase(repo, dispatcher)
        val result = useCase("s")
        assertThat(result.map { it.name }).containsExactly("Sydney")
    }

    @Test
    fun `prefix Al returns Alabama and Albuquerque`() = runTest {
        val dispatcher = DefaultDispatcherProviderTest(testScheduler)
        val repo = CityRepositoryImplTest(seed2())
        val useCase = SearchCitiesUseCase(repo, dispatcher)
        val result = useCase("Al")
        assertThat(result.map { it.name }).containsExactly(
            "Alabama", "Albuquerque"
        )
    }

    @Test
    fun `prefix Alb returns Albuquerque only`() = runTest {
        val dispatcher = DefaultDispatcherProviderTest(testScheduler)
        val repo = CityRepositoryImplTest(seed2())
        val useCase = SearchCitiesUseCase(repo, dispatcher)
        val result = useCase("Alb")
        assertThat(result.map { it.name }).containsExactly("Albuquerque")
    }

    @Test
    fun `no matches returns empty list`() = runTest {
        val dispatcher = DefaultDispatcherProviderTest(testScheduler)
        val repo = CityRepositoryImplTest(seed())
        val useCase = SearchCitiesUseCase(repo, dispatcher)

        val result = useCase("zzz")

        assertThat(result).isEmpty()
        // First call still builds the trie
        assertThat(repo.calls).isEqualTo(1)
    }
}