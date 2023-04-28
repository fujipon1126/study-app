package com.example.study_app.di

import com.example.study_app.qiita.DefaultDispatcher
import com.example.study_app.qiita.IODispatcher
import com.example.study_app.qiita.MainDispatcher
import com.example.study_app.qiita.UnconfinedDispatcher
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
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
        val rxJava3CallAdapterFactory = RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io())
        return Retrofit.Builder()
            .baseUrl("https://qiita.com/api/v2/")
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(rxJava3CallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
//            addInterceptor(networkConnectivityInterceptor)
//            addInterceptor(requestTokenInterceptor)
//            addInterceptor(basicAuthInterceptor)
//            addInterceptor(apiErrorInterceptor)
//            addNetworkInterceptor(loggingInterceptor)
//            cookieJar(userSessionComponentManager.currentCookieJar)
        }.build()
    }

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @IODispatcher
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        Dispatchers.Unconfined
        return Dispatchers.Main
    }

    @UnconfinedDispatcher
    @Provides
    fun provideUnconfinedDispatcher(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}