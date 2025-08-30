package com.example.openweather.domain.usecase

import com.example.openweather.domain.mappers.toWeatherEntity
import com.example.openweather.domain.repository.OpenWeatherRepository
import com.example.openweather.presentation.models.WeatherUiModel

interface FavouriteWeatherUseCase {
    suspend operator fun invoke(weatherUiModel: WeatherUiModel)
}

class FavouriteWeatherUseCaseImpl(
    private val openWeatherRepository: OpenWeatherRepository
): FavouriteWeatherUseCase {

    override suspend fun invoke(weatherUiModel: WeatherUiModel) {
        openWeatherRepository.insertWeather(weatherUiModel.toWeatherEntity())
    }
}