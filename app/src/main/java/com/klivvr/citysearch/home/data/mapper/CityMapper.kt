package com.klivvr.citysearch.home.data.mapper

import com.klivvr.citysearch.core.base.BaseMapper
import com.klivvr.citysearch.core.utils.toFlagEmoji
import com.klivvr.citysearch.home.data.dto.CityDto
import com.klivvr.citysearch.home.domain.model.CityModel
import javax.inject.Inject

/**
 * Maps a list of [CityDto] (Data Transfer Objects) from the data layer
 * to a list of [CityModel] entities for the domain layer.
 *
 * This mapper handles potential null values from the DTO and transforms the data
 * into a clean, non-nullable model suitable for use in the application's business logic
 * and UI. It also enriches the model with derived data, such as a normalized name for
 * searching and a flag emoji based on the country code.
 */
class CityMapper @Inject constructor() : BaseMapper<List<CityDto>, List<CityModel>> {
    override fun map(from: List<CityDto>) = from.map { cityDto ->
        CityModel(
            id = cityDto.id ?: -1,
            name = cityDto.name.orEmpty(),
            country = cityDto.country.orEmpty(),
            longitude = cityDto.coord?.lon ?: 0.0,
            latitude = cityDto.coord?.lat ?: 0.0,
            normalizedName = cityDto.name?.lowercase()?.trim().orEmpty(),
            flagEmoji = cityDto.country.orEmpty().toFlagEmoji()
        )
    }
}