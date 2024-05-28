package com.example.app.di.channels

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.ChannelsRepositoryImpl
import com.example.app.di.ViewModelKey
import com.example.app.domain.repo.ChannelsRepository
import com.example.app.presentation.channels.ChannelsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ChannelsModule {
    @Binds
    @ChannelsScope
    fun channelsRepository(channelsRepositoryImpl: ChannelsRepositoryImpl): ChannelsRepository

    @ChannelsScope
    @Binds
    @IntoMap
    @ViewModelKey(ChannelsViewModel::class)
    fun bindChannelsViewModel(channelsViewModel: ChannelsViewModel): ViewModel

}