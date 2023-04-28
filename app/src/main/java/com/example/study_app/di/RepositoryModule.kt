package com.example.study_app.di

import com.example.study_app.qiita.repository.QiitaListRepository
import com.example.study_app.qiita.repository.QiitaListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindQiitaListRepository(impl: QiitaListRepositoryImpl): QiitaListRepository
}