package com.example.study_app.qiita.datasource

import com.example.study_app.qiita.IODispatcher
import com.example.study_app.qiita.service.QiitaListApi
import com.example.study_app.qiita.service.QiitaListBody
import com.example.study_app.qiita.service.QiitaListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class QiitaListRemoteDataSource @Inject constructor(
    retrofit: Retrofit,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {
    private val qiitaListApi: QiitaListApi = retrofit.create(QiitaListApi::class.java)

    fun fetchQiitaList(
        page: Int,
        query: String?
    ): Flow<QiitaListResponse> {
        return flow {
            val body = QiitaListBody(page = page, query = query)
            withContext(ioDispatcher) {
                qiitaListApi.fetchQiitaList(body)
            }
        }
    }
}