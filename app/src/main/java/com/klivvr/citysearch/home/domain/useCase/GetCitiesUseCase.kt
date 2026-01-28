package com.klivvr.citysearch.home.domain.useCase

import com.klivvr.citysearch.core.base.ResponseState
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.repository.CityRepository
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Use case responsible for retrieving a list of all available cities.
 *
 * This class acts as an intermediary between the presentation layer (e.g., a ViewModel)
 * and the data layer (the [CityRepository]). It encapsulates the business logic for
 * fetching the full list of cities from the repository.
 *
 * The repository already handles dispatching to the appropriate thread (IO), so this
 * use case simply delegates the call without additional context switching to avoid
 * unnecessary dispatcher hopping.
 *
 * The result is wrapped in a [ResponseState] to handle both success and error states gracefully.
 *
 * @property repo The [CityRepository] instance used to access the city data.
 */
@Singleton
class GetCitiesUseCase @Inject constructor(
    private val repo: CityRepository
) {
    /**
     * Retrieves all cities from the repository.
     *
     * @return A [ResponseState] containing either the list of cities on success,
     *         or an exception on failure.
     */
    suspend operator fun invoke(): ResponseState<List<CityModel>> {
        return repo.loadAll()
    }
}