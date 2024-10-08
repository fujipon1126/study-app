package com.example.study_app.composable

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.study_app.background.PeriodicWorker
import com.example.study_app.qiita.list.QiitaListScreen
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "main",
    onPinnedShortcut: () -> Unit,
    onSendNotification: () -> Unit,
    onGlanceUpdate: () -> Unit,
    onAddGlance: () -> Unit
) {
    val context = LocalContext.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        Log.d("MyNavHost", "pickerから取得したUri $uri")
    }
    val multiPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(3)
    ) { uris ->
        Log.d("MyNavHost", "pickerから取得したUri数 ${uris.size}")
        Toast.makeText(context, "選択可能最大数:" + MediaStore.getPickImagesMaxLimit(), Toast.LENGTH_LONG).show()
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
                onPinnedShortcut = onPinnedShortcut,
                onSendNotification = onSendNotification,
                onGlanceUpdate = { onGlanceUpdate() },
                onAddGlance = { onAddGlance() },
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
                    // 5分間隔で実行
                    val periodicRequest = PeriodicWorkRequestBuilder<PeriodicWorker>(
                        5,
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