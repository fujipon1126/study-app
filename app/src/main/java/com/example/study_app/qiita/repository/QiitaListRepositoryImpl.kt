package com.example.study_app.qiita.repository

import com.example.study_app.qiita.datasource.QiitaListRemoteDataSource
import com.example.study_app.qiita.domain.QiitaListDomainData
import com.example.study_app.qiita.service.toDomainData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QiitaListRepositoryImpl @Inject constructor(
    private val remoteDataSource: QiitaListRemoteDataSource
): QiitaListRepository {

    override fun fetchQiitaList(
        page: Int,
        query: String?
    ): Flow<List<QiitaListDomainData>> {
        return remoteDataSource.fetchQiitaList(
            page = page,
            query = query
        ).map { response ->
            response.toDomainData()
        }
    }

}