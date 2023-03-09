package com.example.study_app.qiita.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun QiitaListScreen(
    viewModel: QiitaListViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
//    val isLoading = remember { uiState.value.isLoading }
//    val isError = remember { uiState.value.isError }

    Column {
        Text(text = if (uiState.value.isLoading) "Loading..." else "LoadFinish")
        if (!uiState.value.isLoading && !uiState.value.isError) {
            Text(text = uiState.value.qiitaList.size.toString())
        }
    }
    
}