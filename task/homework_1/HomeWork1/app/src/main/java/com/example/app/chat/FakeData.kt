package com.example.app.chat

import com.example.app.chat.model.Date
import com.example.app.chat.model.MessageIncoming
import com.example.app.chat.model.MessageOutgoing
import com.example.app.chat.model.Reaction

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
    )

    val messages = listOf(
        Date("31 Jan"),
        MessageIncoming(
            id = "1",
            user = "John",
            text = "Hello, how are you?",
            time = System.currentTimeMillis(),
            reactions = emptyList()
        ),
        Date("1 Feb"),
        MessageOutgoing(
            id = "2",
            text = "I'm fine, thank you!",
            time = System.currentTimeMillis(),
            reactions = emptyList()
        ),
        Date("2 Feb"),
        MessageIncoming(
            id = "3",
            user = "John",
            text = "What are you doing?",
            time = System.currentTimeMillis(),
            reactions = emptyList()
        ),
        Date("3 Feb"),
        MessageOutgoing(
            id = "4",
            text = "Hello, how are you?",
            time = System.currentTimeMillis(),
            reactions = emptyList()
        ),
        Date("4 Feb"),
        MessageIncoming(
            id = "5",
            user = "Alice",
            text = "I'm fine, thank you!",
            time = System.currentTimeMillis(),
            reactions = emojis.map {
                Reaction(
                    code = it,
                    counter = 1
                )
            }
        ),
        Date("5 Feb"),
        MessageOutgoing(
            id = "6",
            text = "What are you doing?",
            time = System.currentTimeMillis(),
            reactions = emojis.map {
                Reaction(
                    code = it,
                    counter = 1
                )
            }
        )
    )
}