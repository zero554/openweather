package com.example.openweather.presentation.ui.main

import com.example.openweather.presentation.models.UpcomingWeatherItemUiModel
import com.example.openweather.presentation.models.WeatherUiModel
import kotlinx.collections.immutable.ImmutableList

data class WeatherScreenUiState(
    val weatherUiModel: WeatherUiModel,
    val upcomingDays: ImmutableList<UpcomingWeatherItemUiModel>
)
