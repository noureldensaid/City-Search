package com.klivvr.citysearch.home.presentation.model

import android.net.Uri
import androidx.compose.runtime.Stable
import com.klivvr.citysearch.home.domain.model.CityModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class HomeScreenState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val data: PersistentList<CityModel> = persistentListOf(),
    val googleMapsUri: Uri? = null,
    val searchQuery: String = "",
    val citiesCount: Int = 0,
)
