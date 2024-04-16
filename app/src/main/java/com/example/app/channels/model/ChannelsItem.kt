package com.example.app.channels.model

import androidx.annotation.DrawableRes

sealed interface ChannelsItem {
    val id: Int
    val text: String

    data class Stream(
        override val id: Int,
        override val text: String,
        val isExpanded: Boolean,
        val topics: List<Topic>
    ) : ChannelsItem

    data class Topic(
        override val id: Int,
        override val text: String,
        val streamName: String,
        @DrawableRes val backgroundColorRes: Int
    ) : ChannelsItem
}