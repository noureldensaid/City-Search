package com.klivvr.citysearch.home.domain.useCase

import com.klivvr.citysearch.core.base.ResponseState
import com.klivvr.citysearch.core.utils.DispatcherProvider
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.repository.CityRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Use case responsible for retrieving a list of all available cities.
 *
 * This class acts as an intermediary between the presentation layer (e.g., a ViewModel)
 * and the data layer (the [CityRepository]). It encapsulates the business logic for
 * fetching the full list of cities from the repository. The operation is executed
 * on a background thread managed by the provided [DispatcherProvider] to avoid
 * blocking the main thread.
 *
 * The result is wrapped in a [ResponseState] to handle both success and error states gracefully.
 *
 * @property repo The [CityRepository] instance used to access the city data.
 * @property dispatcher The [DispatcherProvider] used to switch coroutine contexts, ensuring
 *           the data fetching occurs on an appropriate background thread.
 */
@Singleton
class GetCitiesUseCase @Inject constructor(
    private val repo: CityRepository,
    private val dispatcher: DispatcherProvider
) {
    suspend operator fun invoke(): ResponseState<List<CityModel>> =
        withContext(dispatcher.default) {
            val response = repo.loadAll()
            when (response) {
                is ResponseState.Error -> ResponseState.Error(response.exception)
                is ResponseState.Success -> {
                    val cities = response.data
                     ResponseState.Success(cities)
                }
            }
        }
}