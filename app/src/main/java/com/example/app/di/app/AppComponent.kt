package com.example.app.di.app

import com.example.app.data.network.ZulipApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class])
interface AppComponent {
    fun getZulipApiService(): ZulipApi
}