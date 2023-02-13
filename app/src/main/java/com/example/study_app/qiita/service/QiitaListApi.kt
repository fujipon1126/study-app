package com.example.study_app.qiita.service

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Query

internal interface QiitaListApi {

    //    @HTTP(hasBody = true, method = "GET", path = "items")
    @GET("items")
    suspend fun fetchQiitaList(
        @Query("page") page: Int,
        @Query("par_page") parPage: Int,
//        @Query("query") query: String?
    ): List<QiitaListResponseDataItem>
}