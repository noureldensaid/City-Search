package com.klivvr.citysearch.home.presentation.model

import androidx.compose.runtime.Stable
import com.klivvr.citysearch.home.domain.model.CityModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents the state of the home screen.
 *
 * This data class holds all the necessary information to render the UI of the home screen.
 * It is marked as `@Stable` to help the Compose compiler make smart recomposition decisions.
 *
 * @property isLoading Indicates whether the screen is currently in a loading state (e.g., fetching initial data).
 * @property data The persistent list of [CityModel] items to be displayed. Using [PersistentList] for performance gains in recomposition.
 * @property searchQuery The current text entered by the user in the search bar.
 * @property citiesCount The total number of cities available or matching the search criteria.
 */
@Stable
data class HomeScreenState(
    val isLoading: Boolean = true,
    val data: PersistentList<CityModel> = persistentListOf(),
    val searchQuery: String = "",
    val citiesCount: Int = 0,
)
