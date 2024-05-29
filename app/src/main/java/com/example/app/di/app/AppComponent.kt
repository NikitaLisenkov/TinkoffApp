package com.example.app.di.app

import android.content.Context
import com.example.app.data.database.dao.ChatDao
import com.example.app.data.database.dao.StreamDao
import com.example.app.data.database.dao.UserDao
import com.example.app.data.network.ZulipApi
import com.example.app.di.app.modules.AppModule
import com.example.app.di.app.modules.DataSourceModule
import com.example.app.di.app.modules.DatabaseModule
import com.example.app.di.app.modules.NetworkModule
import com.example.app.di.app.modules.ViewModelModule
import com.example.app.presentation.MainActivity
import com.google.gson.Gson
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        ViewModelModule::class,
        DataSourceModule::class
    ]
)
interface AppComponent {
    fun context(): Context
    fun getZulipApiService(): ZulipApi
    fun getUserDao(): UserDao
    fun getStreamDao(): StreamDao
    fun getChatDao(): ChatDao
    fun gson(): Gson
    fun inject(activity: MainActivity)
}