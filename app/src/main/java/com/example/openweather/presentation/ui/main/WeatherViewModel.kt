package com.example.openweather.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.example.openweather.BaseViewModel
import com.example.openweather.common.Resource
import com.example.openweather.domain.usecase.GetCurrentLocationUseCase
import com.example.openweather.domain.usecase.GetCurrentWeatherUseCase
import com.example.openweather.domain.usecase.GetFiveDayForecastUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update


class WeatherViewModel(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getFiveDayForecastUseCase: GetFiveDayForecastUseCase
) : BaseViewModel() {

    private val _weatherUiState: MutableStateFlow<WeatherUiState?> = MutableStateFlow(null)
    val weatherUiState: StateFlow<WeatherUiState?> by ::_weatherUiState

    private fun getWeatherData(latitude: String, longitude: String) {
        combine(
            getCurrentWeatherUseCase(latitude = latitude, longitude = longitude),
            getFiveDayForecastUseCase(latitude = latitude, longitude = longitude),
        ) { currentDay, fiveDayForecast ->
            val isLoading = currentDay is Resource.Loading || fiveDayForecast is Resource.Loading
            val errorMsg = listOfNotNull(
                (currentDay as? Resource.Error)?.message,
                (fiveDayForecast as? Resource.Error)?.message
            ).joinToString("\n").ifBlank { null }
            val weatherUiModel = (currentDay as? Resource.Success)?.data
            weatherUiModel?.let {
                WeatherUiState(
                    isLoading = isLoading,
                    errorMessage = errorMsg,
                    weatherUiModel = it,
                    fiveDayForecast = (fiveDayForecast as? Resource.Success)?.data
                        ?: persistentListOf()
                )
            }
        }.onEach { uiState ->
            _weatherUiState.update { uiState }
        }.launchIn(viewModelScope)
    }

    fun fetchLocation() = runCoroutine {
        getCurrentLocationUseCase().collect {
            getWeatherData(latitude = it.latitude, longitude = it.longitude)
        }
    }
}