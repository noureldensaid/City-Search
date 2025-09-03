package com.klivvr.citysearch.home.domain.repository

import com.klivvr.citysearch.home.domain.model.CityModel

/**
 * Repository interface for handling city data operations.
 * This acts as an abstraction layer between the domain and data layers,
 * defining the contract for fetching city information.
 */
interface CityRepository {
    suspend fun loadAll(): List<CityModel>
}