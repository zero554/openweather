package com.example.openweather.presentation.ui.favourites

import com.example.openweather.BaseViewModelTest
import com.example.openweather.domain.repository.OpenWeatherRepository
import com.example.openweather.presentation.models.FavouriteUiModel
import com.example.openweather.presentation.models.WeatherCondition
import com.example.openweather.presentation.models.WeatherUiModel
import com.google.common.truth.Truth.assertThat
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

class FavouriteViewModelTest: BaseViewModelTest() {

    private lateinit var viewModel: FavouriteViewModel
    private val openWeatherRepository = Mockito.mock<OpenWeatherRepository>()

    val weatherUiModel = WeatherUiModel()
    val weatherModels = listOf(weatherUiModel)
    val locationsFlow = flowOf(weatherModels)

    @Before
    fun setUp() = runBlockingTest {
        whenever(openWeatherRepository.getFavouriteLocations()).thenReturn(locationsFlow)
        whenever(openWeatherRepository.getLocationName(weatherUiModel.latitude, weatherUiModel.longitude))
            .thenReturn("name")

        viewModel = getViewModel()
    }

    @Test
    fun `SHOULD populated state with locations WHEN viewModel initialises`() {
        val state = viewModel.favouriteUiState.value

        val locations = persistentListOf(
            FavouriteUiModel(
                latitude = 0.0,
                longitude = 0.0,
                current = "",
                name = "name",
                weatherCondition = WeatherCondition.RAINY
            )
        )

        assertThat(state?.locations).isEqualTo(locations)
    }

    private fun getViewModel(): FavouriteViewModel {
        return FavouriteViewModel(openWeatherRepository = openWeatherRepository)
    }

}