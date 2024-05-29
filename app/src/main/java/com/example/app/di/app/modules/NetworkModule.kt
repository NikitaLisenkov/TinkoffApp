package com.example.app.di.app.modules

import android.content.Context
import android.net.ConnectivityManager
import com.example.app.data.network.ZulipApi
import com.example.app.presentation.network_listener.NetworkListener
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val BASE_URL = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
        private const val AUTH_HEADER = "Authorization"
        private const val API_KEY = "ChcFE2prTvClktoEmrrzvPsaN6sVQtUt"
        private const val EMAIL = "nelisenkov@gmail.com"
    }

    @Provides
    @Singleton
    fun authInterceptor(): Interceptor = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .addHeader(AUTH_HEADER, Credentials.basic(EMAIL, API_KEY))
                .build()
        )
    }

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun okHttp(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .addInterceptor(authInterceptor)
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun zulipApi(retrofit: Retrofit): ZulipApi = retrofit.create(ZulipApi::class.java)

    @Provides
    @Singleton
    fun gson(): Gson = Gson()

    @Provides
    @Singleton
    fun networkListener(context: Context): NetworkListener = NetworkListener(
        manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    )

}