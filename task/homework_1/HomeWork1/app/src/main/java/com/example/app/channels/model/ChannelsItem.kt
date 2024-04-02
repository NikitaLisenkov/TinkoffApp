package com.example.app.channels.model

import androidx.annotation.DrawableRes

sealed interface ChannelsItem {
    val id: String
    val text: String

    data class Channel(
        override val id: String,
        override val text: String,
        val isExpanded: Boolean,
        val topics: List<Topic>
    ) : ChannelsItem

    data class Topic(
        override val id: String,
        override val text: String,
        val channelName: String,
        @DrawableRes val backgroundColorRes: Int
    ) : ChannelsItem
}