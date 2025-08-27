package com.example.openweather.data.remote.dto

data class City(
    val name: String,
    val coord: Coord,
    val country: String,
    val timezone: Double,
    val sunrise: Double,
    val sunset: Double
)