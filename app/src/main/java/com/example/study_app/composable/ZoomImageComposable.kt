package com.example.study_app.composable

import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.study_app.R
import com.example.study_app.extension.detectTransformGestures
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ZoomImageComposable(
    imageUrl: String,
    isVisible: Boolean
) {
//    var scale by remember { mutableStateOf(1f) }
//    var offset by remember { mutableStateOf(Offset.Zero) }

    val zoomState = rememberZoomState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isVisible) {
        zoomState.reset()
    }

//    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
//        scale *= zoomChange
//        offset += offsetChange
//    }

//    Image(
//        painter = painterResource(id = R.drawable.lucci),
//        contentDescription = "ルッチ",
//        contentScale = ContentScale.Fit,
//        modifier = Modifier
//            .fillMaxSize()
//            .graphicsLayer {
//                scaleX = scale
//                scaleY = scale
//                translationX = offset.x
//                translationY = offset.y
//            }
//            .transformable(state = state)
//    )

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
        model = imageUrl,
        contentDescription = "kintone",
        modifier = Modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
                onDoubleClick = {
                    scope.launch {
                        zoomState.applyDoubleTap()
                    }
                }
            )
            .clipToBounds()
            .onSizeChanged { size ->
                zoomState.setLayoutSize(size.toSize())
            }
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGestureStart = {
                        zoomState.startGesture()
                    },
                    onGesture = { centroid, pan, zoom, _, timeMillis ->
                        val canConsume = zoomState.canConsumeGesture(pan = pan, zoom = zoom)
                        if (canConsume) {
                            scope.launch {
                                zoomState.applyGesture(pan, zoom, centroid, timeMillis)
                            }
                        }
                        canConsume
                    },
                    onGestureEnd = {
                        scope.launch {
                            zoomState.endGesture()
                        }
                    }
                )
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

    // ローカルファイル読み込み
//    Image(
//        painter = rememberAsyncImagePainter(model = BitmapFactory.decodeFile(File("/data/user/0/com.example.study_app/cache/image_cache/mobile.png").absolutePath)),
//        contentDescription = null)
}

@Preview
@Composable
fun ZoomImageComposablePreview() {
    ZoomImageComposable(
        imageUrl = "",
        isVisible = true
    )
}