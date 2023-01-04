package com.example.study_app.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.study_app.R

@Composable
fun ZoomImageComposable() {
//    var scale by remember { mutableStateOf(1f) }
//    var offset by remember { mutableStateOf(Offset.Zero) }
    val zoomState = rememberZoomState()

//    Image(
//        painter = painterResource(id = R.drawable.lucci),
//        contentDescription = "ルッチ",
//        contentScale = ContentScale.Fit,
//        modifier = Modifier
//            .fillMaxSize()
//            .pointerInput(Unit) {
//                detectTransformGestures { _, pan, zoom, _ ->
//                    scale *= zoom
//                    offset += pan
//                }
//            }
//            .graphicsLayer {
//                scaleX = scale
//                scaleY = scale
//                translationX = offset.x
//                translationY = offset.y
//            }
//    )
    AsyncImage(
        model = "https://developer.android.com/images/brand/Android_Robot.png",
        contentDescription = "kintone",
        modifier = Modifier
            .onSizeChanged { size ->
                zoomState.setLayoutSize(size.toSize())
            }
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    zoomState.applyGesture(pan, zoom)
                }
            }
            .graphicsLayer {
                scaleX = zoomState.scale
                scaleY = zoomState.scale
                translationX = zoomState.offsetX
                translationY = zoomState.offsetY
            },
        onSuccess = { success ->
            zoomState.setImageSize(success.painter.intrinsicSize)
        }
    )
}

@Preview
@Composable
fun ZoomImageComposablePreview() {
    ZoomImageComposable()
}