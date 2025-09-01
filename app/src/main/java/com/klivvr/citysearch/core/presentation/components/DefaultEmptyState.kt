package com.klivvr.citysearch.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klivvr.citysearch.R
import com.klivvr.citysearch.core.presentation.ui.theme.HintText
import com.klivvr.citysearch.core.presentation.ui.theme.PrimaryBackground


@Composable
fun DefaultEmptyState(
    modifier: Modifier = Modifier,
    body: String = "No matching results were found",
    painter: Painter = painterResource(id = R.mipmap.ic_launcher_foreground),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.padding(14.dp),
            painter = painter,
            contentDescription = null
        )
        Text(
            text = body,
            fontSize = 14.sp,
            color = HintText,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEmptyState() {
    DefaultEmptyState()
}
