package com.example.study_app.composable

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestPermissionComposable() {
    Text(text = "RequestPermissionComposable")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val requestPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                // 権限を取得できた
                Log.d("RequestPermissionComposable", "権限取得した")
            } else {
                // 権限を取得できなかった
                Log.d("RequestPermissionComposable", "権限取得できなかった")
            }
        }
        val context = LocalContext.current
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                // Some works that require permission
                Log.d("RequestPermissionComposable", "許可済み")
            }

            else -> {
                // Asking for permission
                SideEffect {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
//        val ctx = LocalContext.current
//
//        // check initial state of permission, it may be already granted
//        var grantState by remember {
//            mutableStateOf(
//                ContextCompat.checkSelfPermission(
//                    ctx,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) == PackageManager.PERMISSION_GRANTED
//            )
//        }
//        if (grantState) {
//            Log.d("RequestPermissionComposable", "許可済み")
//        } else {
//            val launcher: ManagedActivityResultLauncher<String, Boolean> =
//                rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
//                    grantState = it
//                }
//            SideEffect {
//                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
//            }
//        }
    }
}