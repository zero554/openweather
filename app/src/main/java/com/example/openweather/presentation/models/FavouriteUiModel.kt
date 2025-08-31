package com.example.openweather.presentation.models

data class FavouriteUiModel(
    val latitude: Double,
    val longitude: Double,
    val current: String,
    val name: String?,
    val weatherCondition: WeatherCondition
)
