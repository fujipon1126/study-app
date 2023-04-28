package com.example.study_app.qiita.service

data class QiitaListResponseDataItem(
    val body: String,
    val coediting: Boolean,
    val comments_count: Int,
    val created_at: String,
    val group: String?,
    val id: String,
    val likes_count: Int,
    val page_views_count: Int?,
    val `private`: Boolean,
    val reactions_count: Int,
    val rendered_body: String,
    val stocks_count: Int,
    val tags: List<Tag>,
    val team_membership: String?,
    val title: String,
    val updated_at: String,
    val url: String,
    val user: User
)