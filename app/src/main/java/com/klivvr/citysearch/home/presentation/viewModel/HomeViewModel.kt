package com.klivvr.citysearch.home.presentation.viewModel

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klivvr.citysearch.core.base.ResponseState
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.useCase.GetCitiesUseCase
import com.klivvr.citysearch.home.domain.useCase.GroupCitiesUseCase
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


/**
 * ViewModel for the Home screen, responsible for managing the UI state and handling business logic.
 *
 * This ViewModel orchestrates the data flow for the home screen, which includes fetching,
 * searching, and grouping a list of cities. It exposes the screen state via a [MutableStateFlow]
 * and communicates one-time UI events (like navigation or showing errors) through a [Channel].
 *
 * It utilizes a [SavedStateHandle] to persist the user's search query across process death,
 * ensuring a seamless user experience.
 *
 * @param getCitiesUseCase Use case for fetching the complete list of cities from the data source.
 * @param searchCitiesUseCase Use case for performing a search operation on the cities list based on a query.
 * @param groupCitiesUseCase Use case for grouping a list of cities into sections based on their starting letter.
 * @param savedStateHandle A handle to the saved state of the ViewModel, used here to persist and restore the search query.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val groupCitiesUseCase: GroupCitiesUseCase,
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
            val cities = getCitiesUseCase()
            when (cities) {
                is ResponseState.Error -> _eventChannel.send(
                    HomeScreenEvent.HomeScreenUiEvent.ShowError(
                        cities.exception.message
                    )
                )

                is ResponseState.Success -> {
                    state.update {
                        it.copy(
                            citiesCount = cities.data.size,
                            sections = groupCitiesUseCase(cities.data).toPersistentList()
                        )
                    }
                }
            }
            state.update { it.copy(isLoading = false) }
        }
    }

    private fun searchCities(query: String) {
        viewModelScope.launch {
            savedStateHandle[SEARCH_QUERY_KEY] = query
            state.update { it.copy(searchQuery = query) }
            val cities = searchCitiesUseCase(query)
            state.update {
                it.copy(
                    citiesCount = cities.size,
                    sections = groupCitiesUseCase(cities).toPersistentList()
                )
            }
        }
    }

    private fun navigateToGoogleMaps(city: CityModel) {
        viewModelScope.launch {
            _eventChannel.send(
                HomeScreenEvent.HomeScreenUiEvent.OpenMap("geo:${city.latitude},${city.longitude}".toUri())
            )
        }
    }

    companion object {
        private const val SEARCH_QUERY_KEY = "search_query"
    }
}