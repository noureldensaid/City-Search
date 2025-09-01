package com.klivvr.citysearch.home.domain.model

data class CityModel(
    val id: Int,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val flagEmoji: String,
    val normalizedName: String? = null,
    var nextCity: CityModel? = null,
)