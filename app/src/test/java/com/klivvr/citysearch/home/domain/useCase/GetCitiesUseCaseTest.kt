package com.klivvr.citysearch.home.domain.useCase

import com.google.common.truth.Truth.assertThat
import com.klivvr.citysearch.core.utils.DefaultDispatcherProviderTest
import com.klivvr.citysearch.home.data.repository.CityRepositoryImplTest
import com.klivvr.citysearch.home.domain.model.CityModel
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetCitiesUseCaseTest {

    @Test
    fun `invoke sorts by normalizedName then country`() = runTest {
        val repo = CityRepositoryImplTest(
            listOf(
                CityModel(1, "Cairo", "EG", 0.0, 0.0, "EG" ,  "cairo"),
                CityModel(2, "Alexandria", "US", 0.0, 0.0, "US" , "alexandria"),
                CityModel(3, "Alexandria", "EG", 0.0, 0.0, "EG" , "alexandria")
            )
        )
        val dispatcher = DefaultDispatcherProviderTest(testScheduler)
        val useCase = GetCitiesUseCase(repo, dispatcher)

        val result = useCase()

        assertThat(result.map { it.name to it.country })
            .containsExactly(
                "Alexandria" to "EG",
                "Alexandria" to "US",
                "Cairo" to "EG"
            )
            .inOrder()
    }
}