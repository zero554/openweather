package com.example.openweather.presentation.ui.weather

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
    val scaffoldViewState = LocalScaffoldViewState.current
    val color by animateColorAsState(
        targetValue = if (weatherUiModel.isFavourite) Color.White else Color.Black,
        animationSpec = tween(durationMillis = 500)
    )

    val topAppBarState = remember(weatherUiModel.isFavourite) {
        TopAppBarState(
            icon = Icons.Default.Menu,
            actions = persistentListOf(
                TopBarAction(
                    icon = Icons.Default.Favorite,
                    tint = color,
                    onClick = { events(WeatherUiEvent.TopAppBarAction.FavouriteClick(weatherUiModel)) }
                )
            )
        )
    }

    DisposableEffect(topAppBarState) {
        scaffoldViewState.topAppBarState = topAppBarState
        onDispose {
            // Only clear if no one else has replaced it
            if (scaffoldViewState.topAppBarState === topAppBarState) {
                scaffoldViewState.topAppBarState = null
            }
        }
    }

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) { CircularProgressIndicator() }
    } else ScreenContent(weatherUiState = this, modifier = modifier)

}

@Composable
fun ScreenContent(
    weatherUiState: WeatherUiState,
    modifier: Modifier = Modifier
) = with (weatherUiState){
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

fun averageColor(bitmap: Bitmap): Color {
    var r = 0
    var g = 0
    var b = 0
    var count = 0

    for (x in 0 until bitmap.width step 10) { // step to speed up
        for (y in 0 until bitmap.height step 10) {
            val pixel = bitmap.getPixel(x, y)
            r += (pixel shr 16 and 0xFF)
            g += (pixel shr 8 and 0xFF)
            b += (pixel and 0xFF)
            count++
        }
    }

    return Color(r / count, g / count, b / count)
}

//@Composable
//fun extractColorFromDrawable(
//    drawableRes: Int,
//    onColorReady: (Color) -> Unit
//) {
//    val context = LocalContext.current
//
//    LaunchedEffect(drawableRes) {
//        val drawable = ContextCompat.getDrawable(context, drawableRes)
//        val bitmap = (drawable as BitmapDrawable).bitmap
//
//        // Use Palette to get dominant color
//        Palette.from(bitmap).generate { palette ->
//            palette?.dominantSwatch?.rgb?.let { rgb ->
//                onColorReady(Color(rgb))
//            }
//        }
//    }
//}

@Preview
@Composable
private fun WeatherScreenPreview() {
    val weatherUiModel = WeatherUiModel(
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