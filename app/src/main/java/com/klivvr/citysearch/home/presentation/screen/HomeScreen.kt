package com.klivvr.citysearch.home.presentation.screen

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.klivvr.citysearch.core.utils.ObserveAsEvents
import com.klivvr.citysearch.home.presentation.model.HomeScreenState
import com.klivvr.citysearch.home.presentation.viewModel.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.mapChannel) {
        context.startActivity(Intent(Intent.ACTION_VIEW, it))
    }

    HomeScreenRoot(
        onEvent = viewModel::onEvent,
        modifier = modifier,
        state = state
    )

}


@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreenRoot(
        state = HomeScreenState(),
    ) {}
}