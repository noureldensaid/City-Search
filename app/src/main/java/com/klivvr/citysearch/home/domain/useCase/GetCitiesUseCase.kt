package com.klivvr.citysearch.home.domain.useCase

import com.klivvr.citysearch.core.utils.DispatcherProvider
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.repository.CityRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for retrieving a list of cities.
 *
 * This class fetches all cities from the repository, sorts them, and returns them.
 * The sorting is done first by the city's normalized name and then by its country,
 * ensuring a consistent and user-friendly order. The operation is performed on a
 * background thread provided by [DispatcherProvider].
 *
 * @property repo The [CityRepository] for accessing city data.
 * @property dispatcher The [DispatcherProvider] for managing coroutine contexts.
 */
@Singleton
class GetCitiesUseCase @Inject constructor(
    private val repo: CityRepository,
    private val dispatcher: DispatcherProvider
) {
    suspend operator fun invoke(): List<CityModel> = withContext(dispatcher.default) {
        repo.loadAll().sortedWith(compareBy(CityModel::normalizedName, CityModel::country))
    }
}