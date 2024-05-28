package com.example.app.di.app.modules

import com.example.app.data.datasource.OwnUserLocalDataSourceImpl
import com.example.app.domain.local.OwnUserLocalDataSource
import dagger.Binds
import dagger.Module

@Module
interface DataSourceModule {
    @Binds
    fun bindOwnUserLocalDataSource(
        ownUserLocalDataSourceImpl: OwnUserLocalDataSourceImpl
    ): OwnUserLocalDataSource
}