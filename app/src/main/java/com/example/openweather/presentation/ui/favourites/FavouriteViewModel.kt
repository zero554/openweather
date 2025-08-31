package com.example.openweather.presentation.ui.favourites

import com.example.openweather.BaseViewModel
import com.example.openweather.domain.repository.OpenWeatherRepository
import com.example.openweather.presentation.models.FavouriteUiModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FavouriteViewModel(
    private val openWeatherRepository: OpenWeatherRepository
): BaseViewModel() {

    private val _favouriteUiState: MutableStateFlow<FavouriteUiState?> = MutableStateFlow(null)
    val favouriteUiState: StateFlow<FavouriteUiState?> by ::_favouriteUiState

    init {
        getLocations()
    }

    private fun getLocations() = runCoroutine {
        openWeatherRepository
            .getFavouriteLocations()
            .map { it.map { weather ->
                FavouriteUiModel(
                    latitude = weather.latitude,
                    longitude = weather.longitude,
                    name = openWeatherRepository.getLocationName(latitude = weather.latitude, longitude = weather.longitude),
                    weatherCondition = weather.weatherCondition,
                    current = weather.current
                )
            } }
            .collect { locations ->
            _favouriteUiState.update {
                FavouriteUiState(locations = locations.toPersistentList())
            }
        }
    }
}