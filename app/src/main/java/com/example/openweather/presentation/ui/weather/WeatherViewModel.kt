package com.example.openweather.presentation.ui.weather

import androidx.lifecycle.viewModelScope
import com.example.openweather.BaseViewModel
import com.example.openweather.common.Resource
import com.example.openweather.domain.usecase.FavouriteWeatherUseCase
import com.example.openweather.domain.usecase.GetCurrentLocationUseCase
import com.example.openweather.domain.usecase.GetWeatherUseCase
import com.example.openweather.presentation.models.Location
import com.example.openweather.presentation.models.WeatherUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update


class WeatherViewModel(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val favouriteWeatherUseCase: FavouriteWeatherUseCase
) : BaseViewModel() {

    private val _weatherUiState: MutableStateFlow<WeatherUiState?> = MutableStateFlow(null)
    val weatherUiState: StateFlow<WeatherUiState?> by ::_weatherUiState

    fun handleUiEvents(event: WeatherUiEvent) {
        if (event is WeatherUiEvent.TopAppBarAction.FavouriteClick) {
            setWeatherAsFavourite(event.weatherUiModel)
        }
    }

    private fun setWeatherAsFavourite(weatherUiModel: WeatherUiModel) = runCoroutine {
        val updated = weatherUiModel.favourite()
        _weatherUiState.update { it?.copy(weatherUiModel = updated) }

        favouriteWeatherUseCase(updated)
    }

    private fun WeatherUiModel.favourite(): WeatherUiModel = this.copy(isFavourite = !this.isFavourite)

    private fun getWeatherData(location: Location) = runCoroutine {
        getWeatherUseCase(
            location = location
        ).onEach { resource ->
            when(resource) {
                is Resource.Loading -> {
                    _weatherUiState.update { WeatherUiState(isLoading = true) }
                }
                is Resource.Error -> {
                    _weatherUiState.update { WeatherUiState(errorMessage = resource.message) }
                }
                is Resource.Success -> _weatherUiState.update {
                    WeatherUiState(
                        weatherUiModel = resource.data?.current ?: WeatherUiModel(),
                        fiveDayForecast = resource.data?.fiveDayForecast ?: persistentListOf()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun fetchLocation() = runCoroutine {
        getCurrentLocationUseCase().collect {
            getWeatherData(it)
        }
    }
}