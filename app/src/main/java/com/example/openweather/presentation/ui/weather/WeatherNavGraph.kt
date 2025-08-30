
package com.example.openweather.presentation.ui.weather
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.openweather.onUiEvent
import com.example.openweather.presentation.models.navigation.Screen
import com.example.openweather.presentation.models.navigation.WeatherRoute
import com.example.openweather.presentation.observeUiEvents
import com.example.openweather.presentation.ui.favourites.FavouriteScreen
import com.example.openweather.presentation.ui.favourites.FavouriteUiState
import com.example.openweather.presentation.ui.favourites.FavouriteViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
fun NavGraphBuilder.weatherNavigationGraph(navController: NavHostController) {
    navigation<WeatherRoute>(startDestination = Screen.Weather) {
        composable<Screen.Weather> {
            val viewModel = koinViewModel<WeatherViewModel>()

            val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

            LaunchedEffect(permissionState.status) {
                if (permissionState.status.isGranted) {
                    viewModel.fetchLocation()
                } else {
                    permissionState.launchPermissionRequest()
                }
            }

            val weatherUiState by viewModel.weatherUiState.collectAsStateWithLifecycle()

            weatherUiState?.let {
                WeatherScreen(
                    weatherUiState = it,
                    events = viewModel::handleUiEvents
                )
            }
        }
        composable<Screen.Favourites> {
            val viewModel = koinViewModel<FavouriteViewModel>()
            val favouriteUiState by viewModel.favouriteUiState.collectAsStateWithLifecycle()

            observeUiEvents(viewModel.uiEvents) { onUiEvent(navController, it) }

            FavouriteScreen(
                favouriteUiState = FavouriteUiState(
                    locations = persistentListOf()
                )
            )
//            favouriteUiState?.let {
//                FavouriteScreen(
//                    favouriteUiState = it
//                )
//            }
        }
    }
}