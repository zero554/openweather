package com.example.openweather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.openweather.data.database.OpenWeatherDatabase.Companion.LATEST_VERSION
import com.example.openweather.data.database.dao.ForecastDao
import com.example.openweather.data.database.dao.WeatherDao
import com.example.openweather.data.database.dao.WeatherWithForecastDao
import com.example.openweather.data.database.entities.ForecastEntity
import com.example.openweather.data.database.entities.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        ForecastEntity::class
    ],
    version = LATEST_VERSION,
    exportSchema = true
)

abstract class OpenWeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun forecastDao(): ForecastDao
    abstract fun weatherWithForecastDao(): WeatherWithForecastDao

    companion object {
        const val DATABASE_NAME = "open_weather_db"
        const val LATEST_VERSION = 2
    }
}
