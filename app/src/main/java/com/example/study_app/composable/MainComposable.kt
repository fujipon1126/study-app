package com.example.study_app.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.study_app.ui.theme.StudyappTheme

@Composable
fun MainComposable(
    modifier: Modifier = Modifier,
    onNavigateToRequest: () -> Unit = {},
    onNavigateToPhotoPicker: () -> Unit = {},
    onNavigateToZoomImage: () -> Unit = {},
    onQiitaApi: () -> Unit = {},
    onWorkManager: () -> Unit = {},
    onPinnedShortcut: () -> Unit = {},
    onSendNotification: () -> Unit = {},
    onGlanceUpdate: () -> Unit = {},
    onAddGlance: () -> Unit = {},
    onForceCrash: () -> Unit = {}
) {
    Column(modifier = modifier
        .safeDrawingPadding()
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = Color.Cyan)) {
        Spacer(
            modifier = Modifier.windowInsetsTopHeight(
                WindowInsets(top = 200, bottom = 200)
            )
        )
        Button(onClick = onNavigateToRequest) {
            Text(text = "Navigate RequestPermissionComposable")
        }

        Button(onClick = onNavigateToPhotoPicker) {
            Text(text = "Navigate PhotoPickerComposable")
        }

        Button(onClick = onNavigateToZoomImage) {
            Text(text = "Navigate ZoomImageComposable")
        }

        Button(onClick = onQiitaApi) {
            Text(text = "Qiita Api Test Composable")
        }

        Button(onClick = onWorkManager) {
            Text(text = "WorkManager Composable")
        }

        Button(onClick = onPinnedShortcut) {
            Text(text = "Add Pinned short cut")
        }

        Button(onClick = onSendNotification) {
            Text(text = "Send Push Notification")
        }

        Button(onClick = onGlanceUpdate) {
            Text(text = "Glance Update")
        }

        Button(onClick = onAddGlance) {
            Text(text = "Glance Add")
        }

        Button(onClick = onForceCrash) {
            Text(text = "ForceCrash")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StudyappTheme {
        MainComposable(
            onNavigateToRequest = {},
            onNavigateToPhotoPicker = {},
            onNavigateToZoomImage = {},
            onQiitaApi = {},
            onWorkManager = {},
            onPinnedShortcut = {},
            onSendNotification = {},
            onGlanceUpdate = {},
            onAddGlance = {},
            onForceCrash = {}
        )
    }
}