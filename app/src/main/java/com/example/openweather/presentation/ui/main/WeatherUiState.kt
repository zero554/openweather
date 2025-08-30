package com.example.openweather.presentation.ui.main

import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.CurrentWeatherUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class WeatherUiState(
    val currentWeatherUiModel: CurrentWeatherUiModel = CurrentWeatherUiModel(),
    val fiveDayForecast: ImmutableList<ForecastUiModel> = persistentListOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
