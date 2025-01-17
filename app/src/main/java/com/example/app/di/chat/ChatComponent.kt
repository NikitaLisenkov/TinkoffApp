package com.example.app.di.chat

import com.example.app.di.app.AppComponent
import com.example.app.di.app.modules.DataSourceModule
import com.example.app.di.app.modules.ViewModelModule
import com.example.app.presentation.chat.ChatFragment
import dagger.Component

@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        ChatModule::class,
        ViewModelModule::class,
        DataSourceModule::class
    ]
)

@ChatScope
interface ChatComponent {

    fun inject(chatFragment: ChatFragment)
}