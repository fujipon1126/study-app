package com.example.study_app.qiita.usecase

import com.example.study_app.qiita.repository.QiitaListRepository
import com.example.study_app.qiita.service.QiitaListResponseDataItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQiitaListUseCase @Inject constructor(
    private val repository: QiitaListRepository
) {

    operator fun invoke(
        page: Int,
        query: String?
    ): Flow<List<QiitaListResponseDataItem>> {
        return repository.fetchQiitaList(page = page, query = query)
    }

}