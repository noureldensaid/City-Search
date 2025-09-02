package com.klivvr.citysearch.home.presentation.model

import com.klivvr.citysearch.home.domain.model.CityModel

sealed interface HomeScreenEvent {
    data class OnQueryChange(val query: String) : HomeScreenEvent
    data object LoadCities : HomeScreenEvent
    data class OnCityClick(val city: CityModel) : HomeScreenEvent
}
