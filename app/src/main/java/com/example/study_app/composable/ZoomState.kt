package com.example.study_app.composable

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset

@Stable
class ZoomState {
    private var _scale = mutableStateOf(1f)
    val scale: Float
        get() = _scale.value

    private var _offsetX = mutableStateOf(0f)
    val offsetX: Float
        get() = _offsetX.value

    private var _offsetY = mutableStateOf(0f)
    val offsetY: Float
        get() = _offsetY.value

    fun applyGesture(pan: Offset, zoom: Float) {
        _scale.value *= zoom
        _offsetX.value += pan.x
        _offsetY.value += pan.y
    }
}