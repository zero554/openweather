package com.example.openweather.domain.usecase

import com.example.openweather.common.Resource
import com.example.openweather.domain.mappers.onePerDay
import com.example.openweather.domain.mappers.toForeCastUiModel
import com.example.openweather.domain.repository.OpenWeatherRepository
import com.example.openweather.presentation.models.ForecastUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

interface GetFiveDayForecastUseCase {
    operator fun invoke(): Flow<Resource<ImmutableList<ForecastUiModel>>>
}

class GetFiveDayForecaseUseCaseImpl(
    private val openWeatherRepository: OpenWeatherRepository
): GetFiveDayForecastUseCase {
    override fun invoke(): Flow<Resource<ImmutableList<ForecastUiModel>>> = flow {
        try {
            emit(Resource.Loading())

            val fiveDayForecast = openWeatherRepository
                .getFiveDayForecast()
                .list
                .onePerDay()
                .map { it.toForeCastUiModel() }
                .toPersistentList()

            emit(Resource.Success(data = fiveDayForecast))
        } catch (exception: HttpException) {
            emit(Resource.Error(exception.localizedMessage ?: "An unexpected error occurred") )
        } catch (exception: IOException) {
            emit(Resource.Error("Could not reach server. Check your internet connection ${exception.localizedMessage}"))
        }
    }

}