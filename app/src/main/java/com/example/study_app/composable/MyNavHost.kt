package com.example.study_app.composable

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
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("main") {
            MainComposable(
                modifier = modifier,
                onNavigateToRequest = { navController.navigate("request_permission") },
                onNavigateToPhotoPicker = { navController.navigate("photo_picker") }
            )
        }
        composable("request_permission") {
            RequestPermissionComposable()
        }
        composable("photo_picker") {
            PhotoPickerComposable()
        }
    }

}