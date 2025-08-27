package com.example.openweather.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.openweather.presentation.models.WeatherCondition
import com.example.openweather.presentation.models.WeatherUiModel
import com.example.openweather.presentation.ui.theme.OpenWeatherTheme
import com.example.openweather.presentation.ui.theme.Rainy

@Composable
fun WeatherTitle(
    weatherUiModel: WeatherUiModel,
    modifier: Modifier = Modifier
) = with(weatherUiModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Content(
            value = min,
            description = "min",
        )

        Content(
            value = current,
            description = "current"
        )

        Content(
            value = max,
            description = "max"
        )
    }
}

@Composable
fun RowScope.Content(
    value: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "${value}\u00B0",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherTitlePreview() {
    val weatherUiModel = WeatherUiModel(
        min = "10",
        current = "15",
        max = "17",
        weatherCondition = WeatherCondition.RAINY
    )

    OpenWeatherTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Rainy)
                .fillMaxSize()
        ) {
            WeatherTitle(weatherUiModel)
        }
    }
}