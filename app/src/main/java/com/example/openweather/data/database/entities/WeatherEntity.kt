package com.example.openweather.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val lastUpdated: Long,
    val current: String,
    val min: String,
    val max: String,
    val weatherCondition: String
)
