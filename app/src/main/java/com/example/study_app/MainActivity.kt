package com.example.study_app

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.study_app.background.OneTimeWorker
import com.example.study_app.composable.MyNavHost
import com.example.study_app.ui.theme.StudyappTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    @ApplicationContext
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Android13以降はPOST_NOTIFICATIONSの権限許諾を求める(Activityの場合)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (granted) {
                    // 権限を取得できた
                    Toast.makeText(context, "権限取得した", Toast.LENGTH_SHORT).show()
                } else {
                    // 権限を取得できなかった
                    Toast.makeText(context, "権限取得できなかった", Toast.LENGTH_SHORT).show()
                }
            }
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // ショートカット起動のチェック
        var shortcutExtra = "main"
        if (intent.extras?.containsKey("shortcut") == true) {
            shortcutExtra = intent.getStringExtra("shortcut").toString()
        }

        // WorkManager実行確認
        val oneTimeWorkerRequest = OneTimeWorkRequestBuilder<OneTimeWorker>().build()
        WorkManager.getInstance(context).enqueue(oneTimeWorkerRequest)

        setContent {
            StudyappTheme {
                MyNavHost(startDestination = shortcutExtra)
            }
        }
    }
}

@Composable
fun MainComposable(
    modifier: Modifier = Modifier,
    onNavigateToRequest: () -> Unit,
    onNavigateToPhotoPicker: () -> Unit,
    onNavigateToZoomImage: () -> Unit,
    onQiitaApi: () -> Unit,
    onForceCrash: () -> Unit
) {
    Column(modifier = modifier) {
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
            onForceCrash = {}
        )
    }
}