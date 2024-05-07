package com.example.app

import android.app.Application
import com.example.app.di.app.AppComponent
import com.example.app.di.app.DaggerAppComponent
import com.example.app.di.app.modules.AppModule
import com.example.app.di.channels.ChannelsComponent
import com.example.app.di.channels.DaggerChannelsComponent
import com.example.app.di.chat.ChatComponent
import com.example.app.di.chat.DaggerChatComponent
import com.example.app.di.people.DaggerPeopleComponent
import com.example.app.di.people.PeopleComponent
import com.example.app.di.profile.DaggerProfileComponent
import com.example.app.di.profile.ProfileComponent

class TinkoffApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    private var profileComponent: ProfileComponent? = null
    private var channelsComponent: ChannelsComponent? = null
    private var chatComponent: ChatComponent? = null
    private var peopleComponent: PeopleComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    fun createProfileComponent(): ProfileComponent? {
        if (profileComponent == null) {
            profileComponent = DaggerProfileComponent.builder()
                .appComponent(appComponent)
                .build()
        }
        return profileComponent
    }

    fun clearProfileComponent() {
        profileComponent = null
    }

    fun createChannelsComponent(): ChannelsComponent? {
        if (channelsComponent == null) {
            channelsComponent = DaggerChannelsComponent.builder()
                .appComponent(appComponent)
                .build()
        }
        return channelsComponent
    }

    fun clearChannelsComponent() {
        channelsComponent = null
    }

    fun createChatComponent(): ChatComponent? {
        if (chatComponent == null) {
            chatComponent = DaggerChatComponent.builder()
                .appComponent(appComponent)
                .build()
        }
        return chatComponent
    }

    fun clearChatComponent() {
        chatComponent = null
    }

    fun createPeopleComponent(): PeopleComponent? {
        if (peopleComponent == null) {
            peopleComponent = DaggerPeopleComponent.builder()
                .appComponent(appComponent)
                .build()
        }
        return peopleComponent
    }

    fun clearPeopleComponent() {
        peopleComponent = null
    }
}