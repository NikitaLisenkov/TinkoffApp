package com.example.app

import com.example.app.channels.model.ChannelsItem
import com.example.app.chat.model.Reaction
import com.example.app.people.model.People

object FakeData {

    val emojis = listOf(
        "\uD83D\uDE00", // 😀
        "\uD83D\uDE01", // 😁
        "\uD83D\uDE02", // 😂
        "\uD83D\uDE03", // 😃
        "\uD83D\uDE04", // 😄
        "\uD83D\uDE05", // 😅
        "\uD83D\uDE06", // 😆
        "\uD83D\uDE07", // 😇
        "\uD83D\uDE08", // 😈
        "\uD83D\uDE09", // 😉
        "\uD83D\uDE0A", // 😊
        "\uD83D\uDE0B", // 😋
        "\uD83D\uDE0C", // 😌
        "\uD83D\uDE0D", // 😍
        "\uD83D\uDE0E", // 😎
    ).map {
        Reaction(
            code = it,
            counter = 1
        )
    }

    val people = List(30) {
        People(
            id = 1,
            avatarUrl = "",
            fullName = "Darrel Steward $it",
            email = "darrel@company.com $it",
            isOnline = it % 2 == 0
        )
    }

    val streams = List(10) { channelIndex ->
        val channelName = "Channel #$channelIndex"
        ChannelsItem.Stream(
            id = channelIndex,
            text = channelName,
            isExpanded = false,
            topics = List(channelIndex) { topicIndex ->
                val topicId = channelIndex * topicIndex
                ChannelsItem.Topic(
                    id = topicId,
                    text = "Topic #$topicId",
                    streamName = channelName,
                    backgroundColorRes = if (topicIndex % 2 == 0) R.color.color_cyan else R.color.color_yellow
                )
            }
        )
    }.filter { it.topics.isNotEmpty() }
}