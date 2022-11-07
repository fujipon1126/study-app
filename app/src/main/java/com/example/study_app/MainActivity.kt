package com.example.study_app

import android.Manifest
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
import com.example.study_app.composable.MyNavHost
import com.example.study_app.ui.theme.StudyappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Android13以降はPOST_NOTIFICATIONSの権限許諾を求める(Activityの場合)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (granted) {
                    // 権限を取得できた
                    Toast.makeText(applicationContext, "権限取得した", Toast.LENGTH_SHORT).show()
                } else {
                    // 権限を取得できなかった
                    Toast.makeText(applicationContext, "権限取得できなかった", Toast.LENGTH_SHORT).show()
                }
            }
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        setContent {
            StudyappTheme {
                MyNavHost()
            }
        }
    }
}

@Composable
fun MainComposable(
    modifier: Modifier = Modifier,
    onNavigateToRequest: () -> Unit,
    onNavigateToPhotoPicker: () -> Unit
) {
    Column(modifier = modifier) {
        Button(onClick = onNavigateToRequest) {
            Text(text = "Navigate RequestPermissionComposable")
        }

        Button(onClick = onNavigateToPhotoPicker) {
            Text(text = "Navigate PhotoPickerComposable")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StudyappTheme {
        MainComposable(
            onNavigateToRequest = {},
            onNavigateToPhotoPicker = {}
        )
    }
}