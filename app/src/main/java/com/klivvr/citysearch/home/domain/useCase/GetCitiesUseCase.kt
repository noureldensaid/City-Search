package com.klivvr.citysearch.home.domain.useCase

import com.klivvr.citysearch.core.utils.DispatcherProvider
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.repository.CityRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCitiesUseCase @Inject constructor(
    private val repo: CityRepository,
    private val dispatcher: DispatcherProvider
) {
    suspend operator fun invoke(): List<CityModel> = withContext(dispatcher.default) {
        repo.loadAll().sortedWith(compareBy(CityModel::normalizedName, CityModel::country))
    }
}