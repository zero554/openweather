package com.example.openweather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherItem(
    @SerializedName("dt") val dateTime: Double,
    val main: Main,
    val weather: List<Weather>,
    @SerializedName("dt_txt") val dateTimeText: String
)
