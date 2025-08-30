package com.example.openweather.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "forecasts",
    foreignKeys = [ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["id"],
        childColumns = ["weatherId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("weatherId")]
)
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val weatherId: Long,
    val lastUpdated: Long,
    val day: String,
    val current: String
)
