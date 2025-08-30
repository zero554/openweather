package com.example.openweather.presentation.ui.favourites

import com.example.openweather.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavouriteViewModel: BaseViewModel() {

    private val _favouriteUiState: MutableStateFlow<FavouriteUiState?> = MutableStateFlow(null)
    val favouriteUiState: StateFlow<FavouriteUiState?> by ::_favouriteUiState


}