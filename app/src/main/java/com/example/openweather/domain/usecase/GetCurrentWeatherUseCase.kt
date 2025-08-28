package com.example.openweather.domain.usecase

import com.example.openweather.common.Resource
import com.example.openweather.domain.mappers.toWeatherUiModel
import com.example.openweather.domain.repository.OpenWeatherRepository
import com.example.openweather.presentation.models.WeatherUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

interface GetCurrentWeatherUseCase {
    operator fun invoke(latitude: String, longitude: String): Flow<Resource<WeatherUiModel>>
}

class GetCurrentWeatherUseCaseImpl(
    private val openWeatherRepository: OpenWeatherRepository
): GetCurrentWeatherUseCase {
    override fun invoke(latitude: String, longitude: String): Flow<Resource<WeatherUiModel>> = flow {
        try {
            emit(Resource.Loading())
            val weather = openWeatherRepository
                .getCurrentWeather(
                    latitude = latitude,
                    longitude = longitude
                )
                .toWeatherUiModel()
            emit(Resource.Success(data = weather))
        } catch (exception: HttpException) {
            emit(Resource.Error(exception.localizedMessage ?: "An unexpected error occurred") )
        } catch (exception: IOException) {
            emit(Resource.Error("Could not reach server. Check your internet connection"))
        }
    }
}