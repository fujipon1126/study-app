package com.example.study_app.qiita.usecase

import com.example.study_app.qiita.domain.QiitaListDomainData
import com.example.study_app.qiita.repository.QiitaListRepository
import kotlinx.coroutines.flow.Flow

class GetQiitaListUseCase(
    private val repository: QiitaListRepository
) {

    operator fun invoke(
        page: Int,
        query: String?
    ): Flow<List<QiitaListDomainData>> {
        return repository.fetchQiitaList(page = page, query = query)
    }

}