package com.example.openweather.presentation.ui.weather

import app.cash.turbine.test
import com.example.openweather.BaseViewModelTest
import com.example.openweather.common.Resource
import com.example.openweather.domain.usecase.FavouriteWeatherUseCase
import com.example.openweather.domain.usecase.GetCurrentLocationUseCase
import com.example.openweather.domain.usecase.GetWeatherUseCase
import com.example.openweather.presentation.models.ForecastUiModel
import com.example.openweather.presentation.models.Location
import com.example.openweather.presentation.models.Weather
import com.example.openweather.presentation.models.WeatherUiModel
import com.google.common.truth.Truth.assertThat
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class WeatherViewModelTest: BaseViewModelTest() {

    private lateinit var viewModel: WeatherViewModel

    private val getCurrentLocationUseCase = mock<GetCurrentLocationUseCase>()
    private val getWeatherUseCase = mock<GetWeatherUseCase>()
    private val favouriteWeatherUseCase = mock<FavouriteWeatherUseCase>()

    private val location = Location(10.0, 15.0)

    @Before
    fun setUp() {
        viewModel = getViewModel()
    }

    @Test
    fun `SHOULD set isLoading to true WHEN data is still being loaded`() = runBlockingTest {
        whenever(getCurrentLocationUseCase.invoke()).thenReturn(flowOf(location))
        whenever(getWeatherUseCase.invoke(location)).thenReturn(flowOf(Resource.Loading()))

        viewModel.fetchLocation()

        viewModel.weatherUiState.test {
            val weatherUiState = awaitItem()
            assertThat(weatherUiState?.isLoading).isEqualTo(true)
        }
    }

    @Test
    fun `SHOULD set error WHEN an error is returned from the domain layer`() = runBlockingTest {
        whenever(getCurrentLocationUseCase.invoke()).thenReturn(flowOf(location))
        whenever(getWeatherUseCase.invoke(location)).thenReturn(flowOf(Resource.Error("error")))

        viewModel.fetchLocation()

        viewModel.weatherUiState.test {
            val weatherUiState = awaitItem()
            assertThat(weatherUiState?.errorMessage).isEqualTo("error")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `SHOULD set state WHEN an date is returned from the domain layer`() = runBlockingTest {
        val weatherUiModel = WeatherUiModel()
        val fiveDayForecast = persistentListOf(
            ForecastUiModel(
                day = "Monday",
                "22"
            )
        )

        val weather = Weather(
            current = weatherUiModel,
            fiveDayForecast = fiveDayForecast
        )

        val resource = Resource.Success<Weather>(data = weather)

        whenever(getCurrentLocationUseCase.invoke()).thenReturn(flowOf(location))
        whenever(getWeatherUseCase.invoke(location)).thenReturn(flowOf(resource))

        viewModel.fetchLocation()

        viewModel.weatherUiState.test {
            val weatherUiState = awaitItem()
            val expectedState = WeatherUiState(
                weatherUiModel = weatherUiModel,
                fiveDayForecast = fiveDayForecast
            )
            assertThat(weatherUiState).isEqualTo(expectedState)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `SHOULD favourite the weather`() = runBlockingTest {
        val weatherUiModel = WeatherUiModel()
        val fiveDayForecast = persistentListOf(
            ForecastUiModel(
                day = "Monday",
                "22"
            )
        )

        val weather = Weather(
            current = weatherUiModel,
            fiveDayForecast = fiveDayForecast
        )

        val resource = Resource.Success<Weather>(data = weather)

        whenever(getCurrentLocationUseCase.invoke()).thenReturn(flowOf(location))
        whenever(getWeatherUseCase.invoke(location)).thenReturn(flowOf(resource))

        viewModel.fetchLocation()
        viewModel.handleUiEvents(WeatherUiEvent.TopAppBarAction.FavouriteClick)

        val state = viewModel.weatherUiState.value
        assertThat(state?.weatherUiModel?.isFavourite).isEqualTo(true)
    }

    private fun getViewModel(): WeatherViewModel {
        return WeatherViewModel(
            getCurrentLocationUseCase = getCurrentLocationUseCase,
            getWeatherUseCase = getWeatherUseCase,
            favouriteWeatherUseCase = favouriteWeatherUseCase
        )
    }
}