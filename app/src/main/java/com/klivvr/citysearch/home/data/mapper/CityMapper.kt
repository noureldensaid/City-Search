package com.klivvr.citysearch.home.data.mapper

import com.klivvr.citysearch.core.base.BaseMapper
import com.klivvr.citysearch.core.utils.toFlagEmoji
import com.klivvr.citysearch.home.data.dto.CityDto
import com.klivvr.citysearch.home.domain.model.CityModel
import javax.inject.Inject


class CityMapper @Inject constructor() : BaseMapper<List<CityDto>, List<CityModel>> {
    override fun map(from: List<CityDto>) = from.map { cityDto ->
        CityModel(
            id = cityDto.id ?: -1,
            name = cityDto.name.orEmpty(),
            country = cityDto.country.orEmpty(),
            longitude = cityDto.coord?.lon ?: 0.0,
            latitude = cityDto.coord?.lat ?: 0.0,
            flagEmoji = cityDto.country.orEmpty().toFlagEmoji()
        )
    }
}