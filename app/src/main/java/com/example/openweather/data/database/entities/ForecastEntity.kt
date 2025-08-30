package com.example.openweather.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "forecasts",
    foreignKeys = [ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["latitude", "longitude"],
        childColumns = ["weatherLatitude", "weatherLongitude"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("weatherLatitude", "weatherLongitude")]
)
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weatherLatitude: Double,
    val weatherLongitude: Double,
    val lastUpdated: Long,
    val day: String,
    val current: String
)
