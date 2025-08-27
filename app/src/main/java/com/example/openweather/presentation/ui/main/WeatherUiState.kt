package com.example.openweather.presentation.ui.main

import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.WeatherUiModel
import kotlinx.collections.immutable.ImmutableList

data class WeatherUiState(
    val weatherUiModel: WeatherUiModel,
    val fiveDayForecast: ImmutableList<ForecastUiModel>,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
