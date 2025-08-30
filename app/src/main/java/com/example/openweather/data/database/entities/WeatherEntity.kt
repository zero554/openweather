package com.example.openweather.data.database.entities

import androidx.room.Entity

@Entity(
    tableName = "weather",
    primaryKeys = ["latitude", "longitude"]
)
data class WeatherEntity(
    val latitude: Double,
    val longitude: Double,
    val lastUpdated: Long,
    val current: String,
    val min: String,
    val max: String,
    val weatherCondition: String,
    val isFavourite: Boolean = false
)
