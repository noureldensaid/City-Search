package com.klivvr.citysearch.home.presentation.viewModel

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.useCase.GetCitiesUseCase
import com.klivvr.citysearch.home.domain.useCase.SearchCitiesUseCase
import com.klivvr.citysearch.home.presentation.model.HomeScreenEvent
import com.klivvr.citysearch.home.presentation.model.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state = MutableStateFlow(HomeScreenState())
        private set

    private val _eventChannel =
        Channel<HomeScreenEvent.HomeScreenUiEvent>(capacity = Channel.BUFFERED)
    val eventChannel = _eventChannel.receiveAsFlow()


    init {
        state.update { it.copy(searchQuery = savedStateHandle[SEARCH_QUERY_KEY] ?: "") }
        onEvent(HomeScreenEvent.LoadCities)
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.LoadCities -> loadCities()
            is HomeScreenEvent.OnQueryChange -> searchCities(event.query)
            is HomeScreenEvent.OnCityClick -> navigateToGoogleMaps(event.city)
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            state.update { it.copy(isLoading = true) }
            try {
                val cities = getCitiesUseCase()
                state.update {
                    it.copy(
                        citiesCount = cities.size,
                        data = cities.toPersistentList()
                    )
                }
            } catch (e: Exception) {
                _eventChannel.send(HomeScreenEvent.HomeScreenUiEvent.ShowError(e.message))
            } finally {
                state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun searchCities(query: String) {
        viewModelScope.launch {
            savedStateHandle[SEARCH_QUERY_KEY] = query
            state.update { it.copy(searchQuery = query) }
            val cities = searchCitiesUseCase(query)
            state.update { it.copy(data = cities.toPersistentList(), citiesCount = cities.size) }
        }
    }

    private fun navigateToGoogleMaps(city: CityModel) {
        viewModelScope.launch {
            _eventChannel.send(HomeScreenEvent.HomeScreenUiEvent.OpenMap("geo:${city.latitude},${city.longitude}".toUri()))
        }
    }

    companion object {
        private const val SEARCH_QUERY_KEY = "search_query"
    }
}