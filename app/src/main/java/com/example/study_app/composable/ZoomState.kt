package com.example.study_app.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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

    private var _offsetX = Animatable(0f)
    val offsetX: Float
        get() = _offsetX.value

    private var _offsetY = Animatable(0f)
    val offsetY: Float
        get() = _offsetY.value

    private val velocityTracker = VelocityTracker()
    private val velocityDecay = exponentialDecay<Float>()
    private var shouldFling = true

    suspend fun applyGesture(
        pan: Offset,
        zoom: Float,
        position: Offset,
        timeMillis: Long
    ) = coroutineScope {
        _scale.value = (_scale.value * zoom).coerceIn(minScale, maxScale)

        val boundX = max((fitImageSize.width * _scale.value - layoutSize.width), 0f) / 2f
        _offsetX.updateBounds(-boundX, boundX)
        launch {
            _offsetX.snapTo(_offsetX.value + pan.x)
        }

        val boundY = max((fitImageSize.height * _scale.value - layoutSize.height), 0f) / 2f
        _offsetY.updateBounds(-boundY, boundY)
        launch {
            _offsetY.snapTo(_offsetY.value + pan.y)
        }

        velocityTracker.addPosition(timeMillis, position)

        if (zoom != 1f) {
            shouldFling = false
        }
    }

    suspend fun endGesture() = coroutineScope {
        if (shouldFling) {
            val velocity = velocityTracker.calculateVelocity()
            launch {
                _offsetX.animateDecay(velocity.x, velocityDecay)
            }
            launch {
                _offsetY.animateDecay(velocity.y, velocityDecay)
            }
        }
        shouldFling = true
    }
}