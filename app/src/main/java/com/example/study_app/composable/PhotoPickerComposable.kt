package com.example.study_app.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PhotoPickerComposable(
    onLaunchSinglePickerImageOnly: () -> Unit,
    onLaunchSinglePickerImageAndVideo: () -> Unit,
    onLaunchSinglePickerVideoOnly: () -> Unit,
    onLaunchMultiPickerMimeType: () -> Unit
) {
    Column {
        Button(onClick = onLaunchSinglePickerImageOnly) {
            Text(text = "launch single photo picker Image Only!")
        }
        Button(onClick = onLaunchSinglePickerImageAndVideo) {
            Text(text = "launch single photo picker Image And Video!")
        }
        Button(onClick = onLaunchSinglePickerVideoOnly) {
            Text(text = "launch single photo picker Video Only!")
        }
        Button(onClick = onLaunchMultiPickerMimeType) {
            Text(text = "launch multi photo picker MimeType = */* !")
        }
    }
}