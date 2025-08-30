package com.example.openweather.presentation.models

import kotlinx.collections.immutable.ImmutableList

data class Weather(
    val current: CurrentWeatherUiModel,
    val fiveDayForecast: ImmutableList<ForecastUiModel>
)
