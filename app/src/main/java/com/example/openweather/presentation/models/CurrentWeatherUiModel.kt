package com.example.openweather.presentation.models

data class CurrentWeatherUiModel(
    val current: String = "",
    val min: String = "",
    val max: String = "",
    val weatherCondition: WeatherCondition = WeatherCondition.RAINY,
)