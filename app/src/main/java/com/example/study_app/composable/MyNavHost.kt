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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.study_app.MainComposable

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
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
    val pickMultipleMediaLauncher: ActivityResultLauncher<Intent> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode != Activity.RESULT_OK) {
            Log.d("MyNavHost", "キャンセル")
        } else {

        }
    }

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
    }

}