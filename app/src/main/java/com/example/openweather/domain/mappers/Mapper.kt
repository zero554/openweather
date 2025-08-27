package com.example.openweather.domain.mappers

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

fun CurrentWeatherDto.toWeatherUiModel(): WeatherUiModel {
    val weatherCondition = weather[0].main

    return WeatherUiModel(
        current = main.temp.toInt().toString(),
        min = main.tempMin.toInt().toString(),
        max = main.tempMax.toInt().toString(),
        weatherCondition = getWeatherCondition(weatherCondition)
    )
}

private fun getWeatherCondition(value: String) = when(value) {
    "Rain" -> WeatherCondition.RAINY
    "Clouds" -> WeatherCondition.CLOUDY
    else -> WeatherCondition.SUNNY
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