package com.klivvr.citysearch.home.presentation.screen

import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.klivvr.citysearch.core.utils.ObserveAsEvents
import com.klivvr.citysearch.home.presentation.model.HomeScreenEvent
import com.klivvr.citysearch.home.presentation.viewModel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.eventChannel) { uiEvent ->
        when (uiEvent) {
            is HomeScreenEvent.HomeScreenUiEvent.OpenMap -> context.startActivity(
                Intent(Intent.ACTION_VIEW, uiEvent.uri)
            )

            is HomeScreenEvent.HomeScreenUiEvent.ShowError -> scope.launch {
                uiEvent.message?.let { snackbarHostState.showSnackbar(it) }
            }
        }
    }

    HomeScreenRoot(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::onEvent,
    )

}


@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreenComponentPreview()
}