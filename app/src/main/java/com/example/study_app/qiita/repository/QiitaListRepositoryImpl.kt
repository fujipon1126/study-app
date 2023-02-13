package com.example.study_app.qiita.repository

import com.example.study_app.qiita.datasource.QiitaListRemoteDataSource
import com.example.study_app.qiita.service.QiitaListResponseDataItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QiitaListRepositoryImpl @Inject constructor(
    private val remoteDataSource: QiitaListRemoteDataSource
): QiitaListRepository {

    override fun fetchQiitaList(
        page: Int,
        query: String?
    ): Flow<List<QiitaListResponseDataItem>> {
        return remoteDataSource.fetchQiitaList(
            page = page,
            query = query
        )
    }

}