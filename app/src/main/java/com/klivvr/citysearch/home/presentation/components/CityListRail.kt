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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klivvr.citysearch.core.presentation.ui.theme.GreyBorder
import com.klivvr.citysearch.home.domain.model.CityModel
import com.klivvr.citysearch.home.domain.model.CitySection

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CityListRail(
    modifier: Modifier = Modifier,
    sections: List<CitySection>,
    onClick: (CityModel) -> Unit,
) {

    val firstLetter = sections.firstOrNull()?.letter
    val lastLetter = sections.lastOrNull()?.letter

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        sections.forEach { section ->
            val letter = section.letter
            val cities = section.items

            stickyHeader(key = "hdr_$letter") {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(48.dp)
                        .border(1.dp, GreyBorder, CircleShape)
                        .background(color = Color.White, shape = CircleShape)
                ) {
                    Text(
                        text = letter.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            itemsIndexed(
                items = cities,
                key = { _, city -> city.id },                 // stable keys
                contentType = { _, _ -> "city_row" }          // helps recycling
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
        CitySection(
            letter = 'A',
            items = listOf(
                CityModel(
                    id = 1,
                    name = "Amsterdam",
                    country = "Netherlands",
                    latitude = 52.3676,
                    longitude = 4.9041,
                    flagEmoji = "🇳🇱",
                    normalizedName = "amsterdam",
                ),
                CityModel(
                    id = 2,
                    name = "Athens",
                    country = "Greece",
                    latitude = 37.9838,
                    longitude = 23.7275,
                    flagEmoji = "🇬🇷",
                    normalizedName = "athens",
                )
            )
        )
    )
    CityListRail(
        sections = data,
        onClick = {}
    )
}