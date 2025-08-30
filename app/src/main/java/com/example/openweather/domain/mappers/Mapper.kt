package com.example.openweather.domain.mappers

import com.example.openweather.data.database.entities.ForecastEntity
import com.example.openweather.data.database.entities.WeatherEntity
import com.example.openweather.data.remote.dto.CurrentWeatherDto
import com.example.openweather.data.remote.dto.WeatherItem
import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.WeatherCondition
import com.example.openweather.presentation.models.WeatherUiModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun WeatherItem.toForeCastUiModel(): ForecastUiModel {
    val timestamp = dateTime.toLong()

    val instant = Instant.fromEpochSeconds(timestamp)
    val datetime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val dayOfWeek = datetime.dayOfWeek.name

    return ForecastUiModel(
        day = dayOfWeek,
        current = main.temp.toInt().toString()
    )
}

fun WeatherItem.toForeCastEntity(
    latitude: Double,
    longitude: Double,
    lastUpdated: Long = System.currentTimeMillis()
): ForecastEntity {
    val timestamp = dateTime.toLong()

    val instant = Instant.fromEpochSeconds(timestamp)
    val datetime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val dayOfWeek = datetime.dayOfWeek.name

    return ForecastEntity(
        weatherLatitude = latitude,
        weatherLongitude = longitude,
        lastUpdated = lastUpdated,
        day = dayOfWeek,
        current = main.temp.toInt().toString()
    )
}

fun CurrentWeatherDto.toWeatherUiModel(): WeatherUiModel {
    val weatherCondition = weather[0].main

    return WeatherUiModel(
        current = main.temp.toInt().toString(),
        min = main.tempMin.toInt().toString(),
        max = main.tempMax.toInt().toString(),
        weatherCondition = getWeatherCondition(weatherCondition)
    )
}

fun CurrentWeatherDto.toWeatherEntity(
    latitude: Double,
    longitude: Double,
    lastUpdated: Long = System.currentTimeMillis()
): WeatherEntity {
    val weatherCondition = weather[0].main

    return WeatherEntity(
        latitude = latitude,
        longitude = longitude,
        lastUpdated = lastUpdated,
        current = main.temp.toInt().toString(),
        min = main.tempMin.toInt().toString(),
        max = main.tempMax.toInt().toString(),
        weatherCondition = weatherCondition
    )
}

fun WeatherEntity.toWeatherUiModel(): WeatherUiModel {
    return WeatherUiModel(
        latitude = latitude,
        longitude = longitude,
        lastUpdated = lastUpdated,
        isFavourite = isFavourite,
        current = current,
        min = min,
        max = max,
        weatherCondition = getWeatherCondition(weatherCondition),
    )
}

fun ForecastEntity.toForecastUiModel(): ForecastUiModel {
    return ForecastUiModel(
        day = day,
        current = current
    )
}

fun WeatherUiModel.toWeatherEntity(): WeatherEntity {
    return WeatherEntity(
        latitude = latitude,
        longitude = longitude,
        lastUpdated = lastUpdated,
        isFavourite = isFavourite,
        current = current,
        min = min,
        max = max,
        weatherCondition = getWeatherConditionStringFromEnum(weatherCondition)
    )
}

private fun getWeatherCondition(value: String) = when(value) {
    "Rain" -> WeatherCondition.RAINY
    "Clouds" -> WeatherCondition.CLOUDY
    else -> WeatherCondition.SUNNY
}

private fun getWeatherConditionStringFromEnum(weatherCondition: WeatherCondition) = when(weatherCondition) {
    WeatherCondition.RAINY -> "Rain"
    WeatherCondition.CLOUDY -> "Clouds"
    else -> "Sun"
}

fun List<WeatherItem>.onePerDay(): List<WeatherItem> {
    val seenDays = mutableSetOf<LocalDate>()
    return this.filter { item ->
        val date = Instant.fromEpochSeconds(item.dateTime.toLong())
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        if (date in seenDays) {
            false // already added a forecast for this day
        } else {
            seenDays.add(date)
            true
        }
    }.toPersistentList()
}