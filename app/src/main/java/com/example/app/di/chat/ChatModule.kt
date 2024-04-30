package com.example.app.di.chat

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.ChatRepositoryImpl
import com.example.app.di.ViewModelKey
import com.example.app.domain.repo.ChatRepository
import com.example.app.presentation.chat.ChatViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ChatModule.Bindings::class])
class ChatModule {

    @Module
    interface Bindings {

        @Binds
        @ChatScope
        fun chatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

        @ChatScope
        @Binds
        @IntoMap
        @ViewModelKey(ChatViewModel::class)
        fun bindChatViewModel(chatViewModel: ChatViewModel): ViewModel
    }
}