package com.klivvr.citysearch.home.data.repository

import android.content.Context
import com.klivvr.citysearch.core.utils.DispatcherProvider
import com.klivvr.citysearch.home.data.dto.CityDto
import com.klivvr.citysearch.home.data.mapper.CityMapper
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.repository.CityRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
    private val cityMapper: CityMapper,
    private val dispatcher: DispatcherProvider
) : CityRepository {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun loadAll(): List<CityModel> = withContext(dispatcher.io) {
        context.assets.open("cities.json").use { input ->
            val dto = json.decodeFromStream(ListSerializer(CityDto.serializer()), input)
            cityMapper.map(dto)
        }
    }
}