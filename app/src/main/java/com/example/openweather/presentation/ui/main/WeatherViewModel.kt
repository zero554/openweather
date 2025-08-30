package com.example.openweather.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.example.openweather.BaseViewModel
import com.example.openweather.common.Resource
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
    private val getWeatherUseCase: GetWeatherUseCase
) : BaseViewModel() {

    private val _weatherUiState: MutableStateFlow<WeatherUiState?> = MutableStateFlow(null)
    val weatherUiState: StateFlow<WeatherUiState?> by ::_weatherUiState

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