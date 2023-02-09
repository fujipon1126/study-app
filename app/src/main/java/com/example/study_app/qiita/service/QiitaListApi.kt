package com.example.study_app.qiita.service

import retrofit2.http.Body
import retrofit2.http.GET

internal interface QiitaListApi {

    @GET("/api/v2/items")
    fun fetchQiitaList(@Body body: QiitaListBody): QiitaListResponse
}