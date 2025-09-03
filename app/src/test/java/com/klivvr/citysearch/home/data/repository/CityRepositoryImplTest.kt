package com.klivvr.citysearch.home.data.repository

import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.repository.CityRepository

class CityRepositoryImplTest(private val items: List<CityModel>) : CityRepository {
    var calls: Int = 0
        private set
    override suspend fun loadAll(): List<CityModel> {
        calls++
        return items
    }
}