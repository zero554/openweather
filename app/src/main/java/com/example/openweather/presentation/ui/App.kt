package com.example.openweather.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.openweather.onUiEvent
import com.example.openweather.presentation.RootNavGraph
import com.example.openweather.presentation.observeUiEvents
import com.example.openweather.presentation.ui.main.MainScreen
import com.example.openweather.presentation.ui.theme.OpenWeatherTheme
import com.example.openweather.presentation.ui.main.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    OpenWeatherTheme {
        val viewModel = koinViewModel<MainViewModel>()
        val mainUiState by viewModel.mainUiState.collectAsStateWithLifecycle()

        observeUiEvents(viewModel.uiEvents) { onUiEvent(navController, it) }

        mainUiState?.let {
            MainScreen(
                mainUiState = it,
                navController = navController,
                modifier = modifier,
                events = viewModel::handleMainEvents,
            ) { modifier ->
                RootNavGraph(
                    navController = navController,
                    modifier = modifier
                )
            }
        }
    }
}