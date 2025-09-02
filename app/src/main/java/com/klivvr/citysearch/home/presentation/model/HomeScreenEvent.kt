package com.klivvr.citysearch.home.presentation.model

import android.net.Uri
import com.klivvr.citysearch.home.domain.model.CityModel

sealed interface HomeScreenEvent {
    data class OnQueryChange(val query: String) : HomeScreenEvent
    data object LoadCities : HomeScreenEvent
    data class OnCityClick(val city: CityModel) : HomeScreenEvent
    sealed interface HomeScreenUiEvent {
        data class OpenMap(val uri: Uri) : HomeScreenUiEvent
        data class ShowError(val message: String?) : HomeScreenUiEvent
    }
}
