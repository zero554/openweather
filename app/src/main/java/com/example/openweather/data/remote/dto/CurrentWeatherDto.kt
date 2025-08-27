package com.example.openweather.data.remote.dto

data class CurrentWeatherDto(
    val main: Main,
    val weather: List<Weather>
)