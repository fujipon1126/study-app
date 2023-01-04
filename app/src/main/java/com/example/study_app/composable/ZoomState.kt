package com.example.study_app.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset

@Composable
fun rememberZoomState(
    minScale: Float = 1f,
    maxScale: Float = 3f
) = remember { ZoomState(minScale, maxScale) }

@Stable
class ZoomState(
    private val minScale: Float,
    private val maxScale: Float
) {
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
        _scale.value = (_scale.value * zoom).coerceIn(minScale, maxScale)
        _offsetX.value += pan.x
        _offsetY.value += pan.y
    }
}