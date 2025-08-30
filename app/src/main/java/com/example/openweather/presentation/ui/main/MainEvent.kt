package com.example.openweather.presentation.ui.main

import com.example.openweather.presentation.models.navigation.NavigationDrawerItem

sealed interface MainEvent {
    data class NavigationDrawerItemClick(val item: NavigationDrawerItem): MainEvent
}
