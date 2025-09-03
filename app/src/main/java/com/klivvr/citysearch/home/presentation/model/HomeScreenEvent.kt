package com.klivvr.citysearch.home.presentation.model

import android.net.Uri
import com.klivvr.citysearch.home.domain.model.CityModel

/**
 * Defines the events that can be triggered from the Home screen.
 * These events are sent from the UI to the ViewModel to process user interactions
 * or other screen-level actions.
 */
sealed interface HomeScreenEvent {
    data class OnQueryChange(val query: String) : HomeScreenEvent
    data object LoadCities : HomeScreenEvent
    data class OnCityClick(val city: CityModel) : HomeScreenEvent
    sealed interface HomeScreenUiEvent {
        data class OpenMap(val uri: Uri) : HomeScreenUiEvent
        data class ShowError(val message: String?) : HomeScreenUiEvent
    }
}
