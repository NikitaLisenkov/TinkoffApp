package com.example.app.di.channels

import com.example.app.di.app.AppComponent
import com.example.app.di.app.ViewModelModule
import com.example.app.presentation.channels.ChannelsFragment
import dagger.Component

@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        ChannelsModule::class,
        ViewModelModule::class
    ]
)

@ChannelsScope
interface ChannelsComponent {

    fun inject(channelsFragment: ChannelsFragment)
}