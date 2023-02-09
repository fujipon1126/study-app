package com.example.study_app.qiita.service

import com.example.study_app.qiita.domain.QiitaListDomainData

internal fun QiitaListResponse.toDomainData(): List<QiitaListDomainData> {
    return rows.map { article ->
        QiitaListDomainData(
            title = article.title
        )
    }
}

data class QiitaListResponse(
    val rows: List<Article>
) {
    data class Article(
        val title: String
    )
}
