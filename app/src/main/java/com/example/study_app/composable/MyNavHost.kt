package com.example.study_app.composable

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Operation
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.study_app.MainComposable
import com.example.study_app.background.PeriodicWorker
import com.example.study_app.qiita.list.QiitaListScreen
import java.util.concurrent.TimeUnit

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    lifecycleOwner: LifecycleOwner,
    startDestination: String = "main"
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        Log.d("MyNavHost", "pickerから取得したUri $uri")
    }
    val multiPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(3)
    ) { uris ->
        Log.d("MyNavHost", "pickerから取得したUri数 ${uris.size}")
    }
    val pickMultipleMediaLauncher: ActivityResultLauncher<Intent> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode != Activity.RESULT_OK) {
                Log.d("MyNavHost", "キャンセル")
            } else {

            }
        }

    val ctx = LocalContext.current

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("main") {
            MainComposable(
                modifier = modifier,
                onNavigateToRequest = { navController.navigate("request_permission") },
                onNavigateToPhotoPicker = { navController.navigate("photo_picker") },
                onNavigateToZoomImage = { navController.navigate("zoom_image") },
                onQiitaApi = { navController.navigate("qiita_api") },
                onWorkManager = { navController.navigate("workmanager") },
                onForceCrash = {
                    throw RuntimeException("Test Crash")
                }
            )
        }
        composable("request_permission") {
            RequestPermissionComposable()
        }
        composable("photo_picker") {
            PhotoPickerComposable(
                onLaunchSinglePickerImageOnly = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                },
                onLaunchSinglePickerImageAndVideo = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageAndVideo
                            )
                        )
                    }
                },
                onLaunchSinglePickerVideoOnly = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.VideoOnly
                            )
                        )
                    }
                },
                onLaunchMultiPickerMimeType = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        multiPhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.SingleMimeType("*/*")
                            )
                        )
                    }
                },
                onLaunchOtherPicker = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        pickMultipleMediaLauncher.launch(
                            Intent(MediaStore.ACTION_PICK_IMAGES).apply {
                                type = "*/*"
                                putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, 3)
                            })
                    }
                }
            )
        }
        composable("zoom_image") {
            ZoomImageComposablePager()
        }
        composable("qiita_api") {
            QiitaListScreen()
        }
        composable("workmanager") {
            WorkManagerComposable(
                onPeriodicWorkRequestStart = {
                    // 15分間隔で実行
                    val periodicRequest = PeriodicWorkRequestBuilder<PeriodicWorker>(
                        15,
                        TimeUnit.MINUTES
                    ).addTag("PeriodicWorker").build()
                    WorkManager.getInstance(ctx).enqueue(periodicRequest)
                },
                onCancelRequest = {
                    WorkManager.getInstance(ctx).cancelAllWorkByTag("PeriodicWorker")
                }
            )
        }
    }
}