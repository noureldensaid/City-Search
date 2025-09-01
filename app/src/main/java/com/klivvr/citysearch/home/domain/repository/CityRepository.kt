package com.klivvr.citysearch.home.domain.repository

import com.klivvr.citysearch.home.domain.model.CityModel

interface CityRepository {
    suspend fun loadAll(): List<CityModel>
}