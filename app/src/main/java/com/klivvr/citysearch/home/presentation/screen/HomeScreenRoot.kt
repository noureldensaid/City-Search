package com.klivvr.citysearch.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.klivvr.citysearch.R
import com.klivvr.citysearch.core.presentation.components.DefaultEmptyState
import com.klivvr.citysearch.core.presentation.components.DefaultLoadingComponent
import com.klivvr.citysearch.core.presentation.components.DefaultSearchTextField
import com.klivvr.citysearch.core.presentation.ui.theme.GreyBorder
import com.klivvr.citysearch.core.presentation.ui.theme.PrimaryBackground
import com.klivvr.citysearch.core.presentation.ui.theme.PrimaryText
import com.klivvr.citysearch.core.utils.toFlagEmoji
import com.klivvr.citysearch.core.utils.toReadableString
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.presentation.components.CityListRail
import com.klivvr.citysearch.home.presentation.model.HomeScreenEvent
import com.klivvr.citysearch.home.presentation.model.HomeScreenState
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit
) {
    Scaffold(
        modifier = modifier,
        containerColor = PrimaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryBackground,
                        titleContentColor = PrimaryText
                    )
                )
                if (state.citiesCount > 0) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, bottom = 16.dp),
                        text = stringResource(
                            R.string.cities,
                            state.citiesCount.toReadableString()
                        ),
                        textAlign = TextAlign.Center,
                        color = PrimaryText
                    )
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White)
                    .border(width = 1.dp, color = GreyBorder),
                contentAlignment = Alignment.Center
            ) {
                DefaultSearchTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    initialSearchText = state.searchQuery,
                    debounceTime = 0L,
                    onSearch = {
                        onEvent(HomeScreenEvent.OnQueryChange(it))
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading && state.data.isEmpty() -> DefaultLoadingComponent()

                !state.isLoading && state.data.isEmpty() -> DefaultEmptyState()

                else -> CityListRail(
                    data = state.data,
                    onClick = { onEvent(HomeScreenEvent.OnCityClick(it)) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(device = "spec:parent=pixel_8,orientation=portrait")
@Preview(device = "spec:parent=pixel_8,orientation=landscape")
@Composable
fun HomeScreenComponentPreview() {
    HomeScreenRoot(
        snackbarHostState = SnackbarHostState(),
        state = HomeScreenState(
            isLoading = false,
            data = persistentListOf(
                CityModel(
                    id = 1,
                    country = "UA",
                    name = "New York",
                    latitude = 46.7128,
                    longitude = -73.0060,
                    flagEmoji = "Us".toFlagEmoji()
                ),
                CityModel(
                    id = 2,
                    country = "UA",
                    name = "New York",
                    latitude = 40.7128,
                    longitude = -74.0060,
                    flagEmoji = "Us".toFlagEmoji()
                ),
                CityModel(
                    id = 5,
                    country = "UA",
                    name = "California",
                    latitude = 41.7128,
                    longitude = -75.0060,
                    flagEmoji = "Us".toFlagEmoji()
                ),
            )
        )
    ) {}
}