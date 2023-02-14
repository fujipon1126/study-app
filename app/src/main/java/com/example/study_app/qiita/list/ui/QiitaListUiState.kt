package com.example.study_app.qiita.list.ui

import com.example.study_app.qiita.domain.QiitaListDomainData

data class QiitaListUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val qiitaList: List<QiitaListDomainData> = emptyList()
)
