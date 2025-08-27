package com.example.openweather.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.openweather.R
import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.WeatherCondition
import com.example.openweather.presentation.ui.theme.OpenWeatherTheme
import com.example.openweather.presentation.ui.theme.Rainy

@Composable
fun WeatherRow(
    forecastUiModel: ForecastUiModel,
    weatherCondition: WeatherCondition,
    modifier: Modifier = Modifier
) = with(forecastUiModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = day,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )

        Image(
            painter = painterResource(getWeatherIcon(weatherCondition)),
            contentDescription = weatherCondition.toString(),
            modifier = Modifier
                .size(24.dp)
        )


        Text(
            text = "${current}\u00B0",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}

@Composable
fun getWeatherIcon(
    weatherCondition: WeatherCondition
): Int {
    val resource by remember(weatherCondition) {
        derivedStateOf {
            when (weatherCondition) {
                WeatherCondition.RAINY -> R.drawable.rain_3x
                WeatherCondition.SUNNY -> R.drawable.clear_3x
                WeatherCondition.CLOUDY -> R.drawable.partlysunny_3x
            }
        }
    }

    return resource
}

@Preview(showBackground = true)
@Composable
private fun WeatherRowPreview() {
    val forecastUiModel = ForecastUiModel(
        day = "Monday",
        current = "15"
    )

    OpenWeatherTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Rainy)
                .fillMaxSize()
        ) {
            WeatherRow(
                forecastUiModel = forecastUiModel,
                weatherCondition = WeatherCondition.RAINY
            )
        }
    }
}