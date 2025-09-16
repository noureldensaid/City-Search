package com.klivvr.citysearch.home.data.repository

import com.klivvr.citysearch.core.base.ResponseState
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.repository.CityRepository

class CityRepositoryImplTest(private val items: List<CityModel>) : CityRepository {
    var calls: Int = 0
        private set

    override suspend fun loadAll(): ResponseState<List<CityModel>> {
        calls++
        return ResponseState.Success(
            items
                .sortedWith(compareBy(CityModel::normalizedName, CityModel::country))
                .distinctBy { it.normalizedName to it.country }
        )
    }
}