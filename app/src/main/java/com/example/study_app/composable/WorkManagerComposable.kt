package com.example.study_app.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun WorkManagerComposable(
    onPeriodicWorkRequestStart: () -> Unit,
    onCancelRequest: () -> Unit
) {
    Column {
        Button(onClick = onPeriodicWorkRequestStart) {
            Text(text = "定期的な処理スケジュール開始")
        }
        Button(onClick = onCancelRequest) {
            Text(text = "定期的な処理スケジュールキャンセル")
        }
    }

}