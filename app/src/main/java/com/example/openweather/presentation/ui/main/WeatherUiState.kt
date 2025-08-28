package com.example.openweather.presentation.ui.main

import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.WeatherUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class WeatherUiState(
    val weatherUiModel: WeatherUiModel = WeatherUiModel(),
    val fiveDayForecast: ImmutableList<ForecastUiModel> = persistentListOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
