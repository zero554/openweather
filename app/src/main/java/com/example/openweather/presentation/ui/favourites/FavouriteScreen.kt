package com.example.openweather.presentation.ui.favourites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        modifier = modifier
            .padding(16.dp)
    ) {

    }

}