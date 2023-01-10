package com.example.study_app.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Float.max

private const val MIN_SCALE = 1.0f
private const val MID_SCALE = 1.75f
private const val MAX_SCALE = 3.0f

@Composable
fun rememberZoomState(
    maxScale: Float = MAX_SCALE
) = remember { ZoomState(maxScale) }

@Stable
class ZoomState(
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

    private var _scale = Animatable(MIN_SCALE).apply {
        // 一旦MIN_SCALEの半分まで縮小を許可するけど、endGesture()でMIN_SCALEに戻す
        updateBounds(MIN_SCALE / 2, maxScale)
    }

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
        launch {
            _scale.snapTo(_scale.value * zoom)
        }

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
            // ある一定速度を超えたら、速度は上限値とする
            val x = if (velocity.x > 0) {
                if (velocity.x > 10000) {
                    10000f
                } else {
                    velocity.x
                }
            } else {
                if (velocity.x < -10000) {
                    -10000f
                } else {
                    velocity.x
                }
            }
            launch {
                _offsetX.animateDecay(x, velocityDecay)
            }
            launch {
                _offsetY.animateDecay(velocity.y, velocityDecay)
            }
        }

        if (_scale.value < MIN_SCALE) {
            launch {
                _scale.animateTo(MIN_SCALE)
            }
        }

        shouldFling = true
    }

    suspend fun applyDoubleTap() = coroutineScope {
        if (_scale.value < MID_SCALE) {
            launch {
                _scale.animateTo(MID_SCALE)
            }
        } else if (_scale.value >= MID_SCALE && _scale.value < MAX_SCALE) {
            launch {
                _scale.animateTo(MAX_SCALE)
            }
        } else {
            launch {
                _scale.animateTo(MIN_SCALE)
            }
        }
    }
}