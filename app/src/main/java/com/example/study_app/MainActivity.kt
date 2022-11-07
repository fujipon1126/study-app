package com.example.study_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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