package com.example.app.presentation.channels.model

import androidx.annotation.DrawableRes

sealed interface ChannelsItemUi {
    val id: Long
    val text: String

    data class StreamUi(
        override val id: Long,
        override val text: String,
        val isExpanded: Boolean,
    ) : ChannelsItemUi

    data class TopicUi(
        override val id: Long,
        override val text: String,
        val streamName: String,
        @DrawableRes val backgroundColorRes: Int
    ) : ChannelsItemUi
}