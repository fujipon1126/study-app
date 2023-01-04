package com.example.study_app.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import java.lang.Float.max

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
    private var imageSize = Size.Zero
    fun setImageSize(size: Size) {
        imageSize = size
        updateFitImageSize()
    }

    private var layoutSize = Size.Zero
    fun setLayoutSize(size: Size) {
        layoutSize = size
        updateFitImageSize()
    }

    private var fitImageSize = Size.Zero
    private fun updateFitImageSize() {
        if ((imageSize == Size.Zero) || (layoutSize == Size.Zero)) {
            fitImageSize = Size.Zero
            return
        }

        val imageAspectRatio = imageSize.width / imageSize.height
        val layoutAspectRatio = layoutSize.width / layoutSize.height

        fitImageSize = if (imageAspectRatio > layoutAspectRatio) {
            imageSize * (layoutSize.width / imageSize.width)
        } else {
            imageSize * (layoutSize.height / imageSize.height)
        }
    }

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

        val boundX = max((fitImageSize.width * _scale.value - layoutSize.width), 0f) / 2f
        _offsetX.value = (_offsetX.value + pan.x).coerceIn(-boundX, boundX)

        val boundY = max((fitImageSize.height * _scale.value - layoutSize.height), 0f) / 2f
        _offsetY.value += (_offsetY.value + pan.y).coerceIn(-boundY, boundY)
    }
}