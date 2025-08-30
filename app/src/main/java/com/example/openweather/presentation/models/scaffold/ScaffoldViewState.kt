package com.example.openweather.presentation.models.scaffold

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.openweather.presentation.models.scaffold.topappbar.TopAppBarState

class ScaffoldViewState {

    var topAppBarState by mutableStateOf<TopAppBarState?>(null)

    fun reset() {
        topAppBarState = null
    }
}

val LocalScaffoldViewState = compositionLocalOf<ScaffoldViewState> {
    error("No ScaffoldViewState provided")
}
