package com.example.openweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.openweather.presentation.models.navigation.NavigationEvent
import com.example.openweather.presentation.models.navigation.Screen
import com.example.openweather.presentation.models.scaffold.LocalScaffoldViewState
import com.example.openweather.presentation.models.scaffold.ScaffoldViewState
import com.example.openweather.presentation.ui.App
import com.example.openweather.presentation.ui.weather.WeatherScreen
import com.example.openweather.presentation.ui.weather.WeatherViewModel
import com.example.openweather.presentation.ui.theme.OpenWeatherTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scaffoldViewState = remember { ScaffoldViewState() }

            CompositionLocalProvider(LocalScaffoldViewState provides scaffoldViewState) {
                App()
            }
        }
    }
}

fun <T> onUiEvent(navController: NavHostController, event: T) {
    when(event) {
        is NavigationEvent.NavigationDrawerEvent.Favourites -> {
            navController.navigate(Screen.Favourites)
        }
        is NavigationEvent.LocationClick -> {
            navController.navigate(Screen.Weather)
        }
    }
}