package com.example.openweather.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.openweather.presentation.models.navigation.WeatherRoute
import com.example.openweather.presentation.ui.weather.weatherNavigationGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun RootNavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = WeatherRoute,
        modifier = modifier
    ) {
        weatherNavigationGraph(navController)
    }
}

@Composable
fun <T> observeUiEvents(uiEvents: Flow<T>, onUiEvent: (T) -> Unit ) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEvents, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                uiEvents.collect { onUiEvent(it) }
            }
        }
    }
}