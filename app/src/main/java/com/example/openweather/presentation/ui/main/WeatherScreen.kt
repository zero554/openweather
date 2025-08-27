package com.example.openweather.presentation.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.openweather.R
import com.example.openweather.presentation.components.WeatherHeader
import com.example.openweather.presentation.components.WeatherRow
import com.example.openweather.presentation.components.WeatherTitle
import com.example.openweather.presentation.models.UpcomingWeatherItemUiModel
import com.example.openweather.presentation.models.WeatherCondition
import com.example.openweather.presentation.models.WeatherUiModel
import com.example.openweather.presentation.ui.theme.Cloudy
import com.example.openweather.presentation.ui.theme.OpenWeatherTheme
import com.example.openweather.presentation.ui.theme.Rainy
import com.example.openweather.presentation.ui.theme.Sunny
import kotlinx.collections.immutable.persistentListOf

@Composable
fun WeatherScreen(
    weatherScreenUiState: WeatherScreenUiState,
    modifier: Modifier = Modifier
) = with(weatherScreenUiState) {
    Column(
        modifier = modifier
            .background(color = getBackgroundColor(weatherUiModel.weatherCondition))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WeatherHeaderBackground(weatherUiModel)

        WeatherTitle(weatherUiModel)

        HorizontalDivider(color = MaterialTheme.colorScheme.onBackground)

        upcomingDays.forEach {
            WeatherRow(
                upcomingWeatherItemUiModel = it,
                weatherCondition = weatherUiModel.weatherCondition,
            )
        }
    }
}

@Composable
fun WeatherHeaderBackground(
    weatherUiModel: WeatherUiModel,
    modifier: Modifier = Modifier
) = with(weatherUiModel) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(getBackgroundImage(weatherCondition)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
        )

        WeatherHeader(weatherUiModel)
    }
}

@Composable
fun getBackgroundColor(weatherCondition: WeatherCondition): Color {
    val color by remember(weatherCondition) {
        derivedStateOf {
            when (weatherCondition) {
                WeatherCondition.RAINY -> Rainy
                WeatherCondition.SUNNY -> Sunny
                WeatherCondition.CLOUDY -> Cloudy
            }
        }
    }

    return color
}

@Composable
fun getBackgroundImage(
    weatherCondition: WeatherCondition
): Int {
    val resource by remember(weatherCondition) {
        derivedStateOf {
            when (weatherCondition) {
                WeatherCondition.RAINY -> R.drawable.forest_rainy
                WeatherCondition.SUNNY -> R.drawable.forest_sunny
                WeatherCondition.CLOUDY -> R.drawable.forest_cloudy
            }
        }
    }

    return resource
}

@Preview
@Composable
private fun WeatherScreenPreview() {
    val weatherUiModel = WeatherUiModel(
        min = "10",
        current = "25",
        max = "17",
        weatherCondition = WeatherCondition.SUNNY,
    )

    val weatherScreenUiState = WeatherScreenUiState(
        weatherUiModel = weatherUiModel,
        upcomingDays = persistentListOf(
            UpcomingWeatherItemUiModel(
                day = "Tuesday",
                current = "20"
            ),UpcomingWeatherItemUiModel(
                day = "Tuesday",
                current = "20"
            ),UpcomingWeatherItemUiModel(
                day = "Tuesday",
                current = "20"
            ),UpcomingWeatherItemUiModel(
                day = "Tuesday",
                current = "20"
            ),UpcomingWeatherItemUiModel(
                day = "Tuesday",
                current = "20"
            )
        )
    )

    OpenWeatherTheme {
        WeatherScreen(weatherScreenUiState = weatherScreenUiState)
    }
}