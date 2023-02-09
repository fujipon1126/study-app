package com.example.study_app.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter())
        val moshiConverterFactory = MoshiConverterFactory.create(moshi.build())
        return Retrofit.Builder()
            .baseUrl("https://qiita.com/api/v2")
            .addConverterFactory(moshiConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
//            addInterceptor(networkConnectivityInterceptor)
//            addInterceptor(requestTokenInterceptor)
//            addInterceptor(basicAuthInterceptor)
//            addInterceptor(apiErrorInterceptor)
//            addNetworkInterceptor(loggingInterceptor)
//            cookieJar(userSessionComponentManager.currentCookieJar)
        }.build()
    }
}