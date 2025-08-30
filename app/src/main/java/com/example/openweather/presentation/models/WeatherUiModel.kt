package com.example.openweather.presentation.models

data class WeatherUiModel(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val current: String = "",
    val min: String = "",
    val max: String = "",
    val weatherCondition: WeatherCondition = WeatherCondition.RAINY,
)