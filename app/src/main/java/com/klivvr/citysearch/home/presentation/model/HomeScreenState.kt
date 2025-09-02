package com.klivvr.citysearch.home.presentation.model

import androidx.compose.runtime.Stable
import com.klivvr.citysearch.home.domain.model.CityModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class HomeScreenState(
    val isLoading: Boolean = true,
    val data: PersistentList<CityModel> = persistentListOf(),
    val searchQuery: String = "",
    val citiesCount: Int = 0,
)
