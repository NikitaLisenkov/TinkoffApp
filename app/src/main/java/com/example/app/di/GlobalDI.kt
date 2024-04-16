package com.example.app.di

import com.example.app.data.ZulipApi
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GlobalDI {

    private const val BASE_URL = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
    private const val AUTH_HEADER = "Authorization"
    private const val AUTH_TYPE = "Basic"
    private const val API_KEY = "ChcFE2prTvClktoEmrrzvPsaN6sVQtUt"
    private const val EMAIL = "nelisenkov@gmail.com"

    private val authInterceptor = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .addHeader(AUTH_HEADER, Credentials.basic(EMAIL, API_KEY))
                .build()
        )
    }

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .addInterceptor(authInterceptor)
        .addNetworkInterceptor(httpLoggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val zulipApi: ZulipApi = retrofit.create(ZulipApi::class.java)
}