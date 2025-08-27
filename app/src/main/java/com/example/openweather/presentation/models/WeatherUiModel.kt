package com.example.openweather.presentation.models

import kotlinx.collections.immutable.ImmutableList

data class WeatherUiModel(
    val current: String,
    val min: String,
    val max: String,
    val weatherCondition: WeatherCondition,
)