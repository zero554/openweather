package com.example.openweather.presentation.models.scaffold.topappbar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarAction(
    val icon: ImageVector,
    val tint: Color = Color.Unspecified,
    val contentDescription: String? = null,
    val onClick: () -> Unit
)
