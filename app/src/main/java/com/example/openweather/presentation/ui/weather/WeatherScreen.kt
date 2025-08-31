package com.example.openweather.presentation.ui.weather

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.example.openweather.R
import com.example.openweather.presentation.components.WeatherHeader
import com.example.openweather.presentation.components.WeatherRow
import com.example.openweather.presentation.components.WeatherTitle
import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.WeatherCondition
import com.example.openweather.presentation.models.WeatherUiModel
import com.example.openweather.presentation.models.scaffold.LocalScaffoldViewState
import com.example.openweather.presentation.ui.theme.Cloudy
import com.example.openweather.presentation.ui.theme.OpenWeatherTheme
import com.example.openweather.presentation.ui.theme.Rainy
import com.example.openweather.presentation.ui.theme.Sunny
import com.example.openweather.presentation.models.scaffold.topappbar.TopAppBarState
import com.example.openweather.presentation.models.scaffold.topappbar.TopBarAction
import kotlinx.collections.immutable.persistentListOf

@Composable
fun WeatherScreen(
    weatherUiState: WeatherUiState,
    modifier: Modifier = Modifier,
    events: (WeatherUiEvent) -> Unit

) = with(weatherUiState) {
    val color = if (weatherUiModel != null) getBackgroundColor(weatherUiModel.weatherCondition) else Color.White

    val scaffoldViewState = LocalScaffoldViewState.current

    val topAppBarState = remember(weatherUiModel?.isFavourite) {
        TopAppBarState(
            icon = Icons.Default.Menu,
            containerColour =  color,
            actions = persistentListOf(
                TopBarAction(
                    icon = Icons.Default.Favorite,
                    tint = if (weatherUiModel?.isFavourite == true) Color.Black else Color.White,
                    onClick = { events(WeatherUiEvent.TopAppBarAction.FavouriteClick) }
                )
            )
        )
    }

    DisposableEffect(topAppBarState) {
        scaffoldViewState.topAppBarState = topAppBarState
        onDispose {
            // Only clear if no screen else has replaced it
            if (scaffoldViewState.topAppBarState === topAppBarState) {
                scaffoldViewState.topAppBarState = null
            }
        }
    }

    when {
        isLoading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) { CircularProgressIndicator() }
        }
        errorMessage != null -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) { Text(
                text = errorMessage,
                modifier = Modifier.padding(16.dp)
            ) }
        }
        else -> ScreenContent(weatherUiState = this, modifier = modifier)
    }
}

@Composable
fun ScreenContent(
    weatherUiState: WeatherUiState,
    modifier: Modifier = Modifier
) = with (weatherUiState){
    weatherUiModel?.let {
        Column(
            modifier = modifier
                .background(color = getBackgroundColor(weatherUiModel.weatherCondition))
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            WeatherHeaderBackground(weatherUiModel)

            WeatherTitle(weatherUiModel)

            HorizontalDivider(color = MaterialTheme.colorScheme.onBackground)

            fiveDayForecast.forEach {
                WeatherRow(
                    forecastUiModel = it,
                    weatherCondition = weatherUiModel.weatherCondition,
                )
            }
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
                WeatherCondition.SUNNY -> Sunny
                WeatherCondition.CLOUDY -> Cloudy
                else -> Rainy
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
                WeatherCondition.UNKNOWN -> R.drawable.unknown_med_24px
            }
        }
    }

    return resource
}

@Composable
fun extractColorFromDrawable(drawableRes: Int, defaultColor: Color = Color.Unspecified): Color {
    val context = LocalContext.current

    // produceState runs a suspend block and updates the value
    val color by produceState(initialValue = defaultColor, key1 = drawableRes) {
        val drawable = ContextCompat.getDrawable(context, drawableRes)
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val palette = Palette.from(bitmap).generate()
            palette.dominantSwatch?.rgb?.let { rgb ->
                value = Color(rgb)
            }
        }
    }

    return color
}

@Preview
@Composable
private fun WeatherScreenPreview() {
    val weatherUiModel = WeatherUiModel(
        latitude = 0.0,
        longitude = 0.0,
        lastUpdated = 0L,
        min = "10",
        current = "25",
        max = "17",
        weatherCondition = WeatherCondition.SUNNY,
    )

    val weatherUiState = WeatherUiState(
        weatherUiModel = weatherUiModel,
        fiveDayForecast = persistentListOf(
            ForecastUiModel(
                day = "Tuesday",
                current = "20"
            ),ForecastUiModel(
                day = "Tuesday",
                current = "20"
            ),ForecastUiModel(
                day = "Tuesday",
                current = "20"
            ),ForecastUiModel(
                day = "Tuesday",
                current = "20"
            ),ForecastUiModel(
                day = "Tuesday",
                current = "20"
            )
        )
    )

    OpenWeatherTheme {
        WeatherScreen(weatherUiState = weatherUiState) {}
    }
}