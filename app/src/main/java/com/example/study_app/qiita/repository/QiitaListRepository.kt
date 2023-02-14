package com.example.study_app.qiita.repository

import com.example.study_app.qiita.domain.QiitaListDomainData
import com.example.study_app.qiita.service.QiitaListResponseDataItem
import kotlinx.coroutines.flow.Flow

interface QiitaListRepository {

    fun fetchQiitaList(
        page: Int,
        query: String?
    ): Flow<List<QiitaListDomainData>>

}