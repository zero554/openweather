package com.example.openweather.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.openweather.presentation.models.WeatherCondition
import com.example.openweather.presentation.models.WeatherUiModel
import com.example.openweather.presentation.ui.theme.OpenWeatherTheme
import com.example.openweather.presentation.ui.theme.Rainy

@Composable
fun WeatherHeader(
    weatherUiModel: WeatherUiModel,
    modifier: Modifier = Modifier
) = with(weatherUiModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "${current}\u00B0",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "$weatherCondition",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherHeaderPreview() {
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
            WeatherHeader(weatherUiModel = weatherUiModel)
        }
    }
}