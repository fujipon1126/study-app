package com.example.study_app.pushnotification

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.Date

interface PushClient {
    @POST("/v1/projects/study-app-7227b/messages:send")
    fun sendPush(@Body message: PushDto): Call<ResponseBody>

    companion object {
        fun create(context: Context): PushClient {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(Date::class.java, Rfc3339DateJsonAdapter())
            val moshiConverterFactory = MoshiConverterFactory.create(moshi.build())
            return Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .client(pushClient(context))
                .addConverterFactory(moshiConverterFactory)
                .build()
                .create(PushClient::class.java)
        }

        private fun pushClient(context: Context) = OkHttpClient.Builder()
            .addInterceptor(AddAuthorizationHeaderInterceptor(context))
            .build()
    }

    private class AddAuthorizationHeaderInterceptor(
        private val context: Context,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${getAccessToken(context = context)}")
                .addHeader("Content-Type", "application/json; UTF-8")
                .build()

            return chain.proceed(newRequest)
        }

        private fun getAccessToken(context: Context): String {
            val credentials = GoogleCredentials
                .fromStream(context.assets.open("serviceaccount.json"))
                .createScoped("https://www.googleapis.com/auth/firebase.messaging")
            credentials.refresh()
            return credentials.accessToken.tokenValue
        }
    }
}