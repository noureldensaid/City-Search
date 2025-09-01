package com.klivvr.citysearch.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klivvr.citysearch.core.presentation.ui.theme.HintText
import com.klivvr.citysearch.core.presentation.ui.theme.PrimaryText
import com.klivvr.citysearch.core.presentation.ui.theme.SecondaryBackground
import com.klivvr.citysearch.core.utils.skipInteraction
import com.klivvr.citysearch.core.utils.toFlagEmoji
import com.klivvr.citysearch.home.domain.model.CityModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityCard(
    modifier: Modifier = Modifier,
    city: CityModel,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(112.dp),
        onClick = onClick,
        interactionSource = skipInteraction(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .background(color = SecondaryBackground, shape = CircleShape)
            ) {
                Text(city.flagEmoji, fontSize = 36.sp)
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${city.name}, ${city.country}",
                    fontSize = 14.sp,
                    maxLines = 1,
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${city.longitude}, ${city.latitude}",
                    color = HintText,
                    fontSize = 12.sp
                )
            }
        }
    }
}


@Preview
@Composable
private fun CityCardPreview() {
    CityCard(
        city = CityModel(
            id = 1,
            country = "UA",
            name = "New York",
            latitude = 40.7128,
            longitude = -74.0060,
            flagEmoji = "Us".toFlagEmoji()
        ),
    ) {}
}