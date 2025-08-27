package com.example.openweather.data.remote.dto

data class WeatherDto(
    val list: List<WeatherItem>, val city: City
)
