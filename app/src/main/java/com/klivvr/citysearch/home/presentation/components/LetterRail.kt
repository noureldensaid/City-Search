package com.klivvr.citysearch.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.klivvr.citysearch.core.presentation.ui.theme.GreyBorder


@Composable
fun TimelineRail(
    drawConnectorAbove: Boolean,
    drawConnectorBelow: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(48.dp)
            .fillMaxHeight()
    ) {
        if (drawConnectorAbove) {
            Box(
                Modifier
                    .width(1.dp)
                    .weight(1f)
                    .background(GreyBorder)
            )
        }
        if (drawConnectorBelow) {
            Box(
                Modifier
                    .width(1.dp)
                    .weight(1f)
                    .background(GreyBorder)
            )
        } else {
            Box(
                Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(GreyBorder)
            )
        }
    }
}

@Preview
@Composable
private fun TimelineRailPreview() {
    TimelineRail(
        drawConnectorAbove = true,
        drawConnectorBelow = false,
    )
}