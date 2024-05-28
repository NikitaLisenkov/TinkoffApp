package com.example.app.di.app.modules

import android.content.Context
import androidx.room.Room
import com.example.app.data.database.AppDatabase
import com.example.app.data.database.dao.ChatDao
import com.example.app.data.database.dao.StreamDao
import com.example.app.data.database.dao.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun createDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context = context,
        AppDatabase::class.java,
        "zulip_app_db"
    ).build()

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideStreamDao(appDatabase: AppDatabase): StreamDao = appDatabase.streamDao()

    @Provides
    @Singleton
    fun provideChatDao(appDatabase: AppDatabase): ChatDao = appDatabase.chatDao()
}