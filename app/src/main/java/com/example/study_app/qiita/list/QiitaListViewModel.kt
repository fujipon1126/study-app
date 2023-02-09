package com.example.study_app.qiita.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.study_app.qiita.usecase.GetQiitaListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class QiitaListViewModel constructor(
    private val getQiitaListUseCase: GetQiitaListUseCase
) : ViewModel() {

    init {
        fetchQiitaList()
    }

    private fun fetchQiitaList() {
        viewModelScope.launch {
            getQiitaListUseCase(page = 1, query = "").collect { result ->
                Log.d("getQiitaListUseCase", "result = ${result.size}")
            }
        }
    }

}