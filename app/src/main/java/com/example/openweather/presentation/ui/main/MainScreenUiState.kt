package com.example.openweather.presentation.ui.main

import com.example.openweather.presentation.models.navigation.NavigationDrawerItem
import kotlinx.collections.immutable.ImmutableList

data class MainUiState(
    val navigationDrawerItems: ImmutableList<NavigationDrawerItem>,
    val selectedNavDrawerItemIndex: Int = 0
)
