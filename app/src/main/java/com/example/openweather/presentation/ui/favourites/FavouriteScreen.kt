package com.example.openweather.presentation.ui.favourites

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.openweather.presentation.components.getWeatherIcon
import com.example.openweather.presentation.models.FavouriteUiModel
import com.example.openweather.presentation.models.scaffold.LocalScaffoldViewState
import com.example.openweather.presentation.models.scaffold.topappbar.TopAppBarState

@Composable
fun FavouriteScreen(
    favouriteUiState: FavouriteUiState,
    modifier: Modifier = Modifier
) = with(favouriteUiState) {
    val scaffoldViewState = LocalScaffoldViewState.current
    val topAppBarState = remember {
        TopAppBarState(
            title = "Favourites",
            showNavIcon = true
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

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
    ) {
        locations.forEach {
            Location(favouriteUiModel = it)
        }
    }

}

@Composable
fun Location(
    favouriteUiModel: FavouriteUiModel,
    modifier: Modifier = Modifier
) = with(favouriteUiModel) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column (modifier = Modifier.padding(16.dp)) {
            name?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(getWeatherIcon(weatherCondition)), contentDescription = null)

                Text(
                    text = "${current}\u00B0",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lat: $latitude,",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )

                Text(
                    text = "Lon: $longitude",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
        }
    }
}