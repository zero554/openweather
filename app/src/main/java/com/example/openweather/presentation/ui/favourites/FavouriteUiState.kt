package com.example.openweather.presentation.ui.favourites

import com.example.openweather.presentation.models.FavouriteUiModel
import kotlinx.collections.immutable.ImmutableList

data class FavouriteUiState(
    val locations: ImmutableList<FavouriteUiModel>
)
