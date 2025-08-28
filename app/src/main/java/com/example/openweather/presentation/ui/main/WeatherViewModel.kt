package com.example.openweather.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.example.openweather.BaseViewModel
import com.example.openweather.common.Resource
import com.example.openweather.domain.usecase.GetCurrentWeatherUseCase
import com.example.openweather.domain.usecase.GetFiveDayForecastUseCase
import com.example.openweather.presentation.models.WeatherUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update


class WeatherViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getFiveDayForecastUseCase: GetFiveDayForecastUseCase
) : BaseViewModel() {

    private val _weatherUiState: MutableStateFlow<WeatherUiState?> = MutableStateFlow(null)
    val weatherUiState: StateFlow<WeatherUiState?> by ::_weatherUiState

    init {
        getWeatherData()
    }

    private fun getWeatherData() {
        combine(
            getCurrentWeatherUseCase(),
            getFiveDayForecastUseCase()
        ) { currentDay, fiveDayForecast ->
            val isLoading = currentDay is Resource.Loading || fiveDayForecast is Resource.Loading

            val errorMsg = listOfNotNull(
                (currentDay as? Resource.Error)?.message,
                (fiveDayForecast as? Resource.Error)?.message
            ).joinToString("\n").ifBlank { null }

            return@combine when {
                isLoading -> {
                    WeatherUiState(
                        weatherUiModel = WeatherUiModel(),
                        fiveDayForecast = persistentListOf(),
                        isLoading = true
                    )
                }

                errorMsg != null -> {
                    WeatherUiState(
                        errorMessage = errorMsg,
                        weatherUiModel = WeatherUiModel(),
                        fiveDayForecast = persistentListOf(),
                    )
                }

                else ->  WeatherUiState(
                    weatherUiModel = (currentDay as? Resource.Success)?.data ?: WeatherUiModel(),
                    fiveDayForecast = (fiveDayForecast as? Resource.Success)?.data
                        ?: persistentListOf()
                )
            }
        }.onEach { uiState ->
            _weatherUiState.update { uiState }
        }.launchIn(viewModelScope)
    }
}