package com.klivvr.citysearch.home.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klivvr.citysearch.core.presentation.ui.theme.GreyBorder
import com.klivvr.citysearch.home.domain.model.CityModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CityListRail(
    modifier: Modifier = Modifier,
    data: List<CityModel>,
    onClick: (CityModel) -> Unit,
) {
    val grouped = remember(data) {
        val map = linkedMapOf<Char, MutableList<CityModel>>()
        data.forEach { city ->
            val key = city.name.first()
            map.getOrPut(key) { mutableListOf() }.add(city)
        }
        map
    }
    val firstLetter = grouped.keys.firstOrNull()
    val lastLetter = grouped.keys.lastOrNull()

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        grouped.forEach { (letter, cities) ->

            stickyHeader(key = letter) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(48.dp)
                        .border(1.dp, GreyBorder, CircleShape)
                        .background(color = Color.White, shape = CircleShape)
                ) { Text(text = letter.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold) }
            }

            itemsIndexed(
                items = cities,
                key = { _, city -> city.id }
            ) { idxInSection, city ->

                val isFirstInSection = idxInSection == 0
                val isLastInSection = idxInSection == cities.lastIndex
                val isFirstOverall = (letter == firstLetter && isFirstInSection)
                val isLastOverall = (letter == lastLetter && isLastInSection)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(start = 8.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TimelineRail(
                        drawConnectorAbove = !isFirstOverall || isLastOverall,
                        drawConnectorBelow = !isLastOverall
                    )
                    CityCard(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 16.dp),
                        city = city,
                        onClick = { onClick(city) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun CityListRailPreview() {
    val data = listOf(
        CityModel(
            id = 1,
            name = "Amsterdam",
            country = "Netherlands",
            latitude = 52.37,
            longitude = 4.89,
            flagEmoji = "🇳🇱"
        ),
        CityModel(
            id = 2,
            name = "Ankara",
            country = "Turkey",
            latitude = 39.92,
            longitude = 32.85,
            flagEmoji = "🇹🇷"
        ),
        CityModel(
            id = 3,
            name = "Berlin",
            country = "Germany",
            latitude = 52.52,
            longitude = 13.40,
            flagEmoji = "🇩🇪"
        ),
    )
    CityListRail(
        data = data,
        onClick = {}
    )
}