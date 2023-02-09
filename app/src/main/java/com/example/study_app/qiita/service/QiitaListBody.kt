package com.example.study_app.qiita.service

data class QiitaListBody(
    val page: Int,
    val per_page: Int = 20,
    val query: String?
)