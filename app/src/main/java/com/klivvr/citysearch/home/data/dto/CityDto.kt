package com.klivvr.citysearch.home.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    @SerialName("coord")
    val coord: Coord? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("_id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null
) {
    @Serializable
    data class Coord(
        @SerialName("lat")
        val lat: Double? = null,
        @SerialName("lon")
        val lon: Double? = null
    )
}