package com.example.study_app.qiita.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.study_app.qiita.list.ui.QiitaListUiState
import com.example.study_app.qiita.usecase.GetQiitaListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QiitaListViewModel @Inject constructor(
    private val getQiitaListUseCase: GetQiitaListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QiitaListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchQiitaList()
    }

    private fun fetchQiitaList() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            getQiitaListUseCase(page = 1, query = "").collect { result ->
                Log.d("getQiitaListUseCase", "result = ${result.size}")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        qiitaList = result
                    )
                }
            }
        }
    }

}