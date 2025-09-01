package com.klivvr.citysearch.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.klivvr.citysearch.core.presentation.ui.theme.PrimaryBackground

@Composable
fun DefaultLoadingComponent(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    strokeWidth: Dp = 4.dp,
    spinnerColor: Color = Color.Blue,
    trackColor: Color = Color.LightGray.copy(alpha = 0.35f),
) {
    Box(
        modifier
            .fillMaxSize()
            .background(PrimaryBackground)
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size),
            color = spinnerColor,
            strokeWidth = strokeWidth,
            trackColor = trackColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultLoadingComponentPreview() {
    DefaultLoadingComponent()
}