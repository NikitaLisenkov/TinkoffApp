package com.example.app.domain.repo

import com.example.app.data.network.model.Event
import com.example.app.data.network.model.MessageEvent
import com.example.app.domain.model.MessageModel

interface ChatRepository {
    suspend fun getMessages(streamName: String, topicName: String): List<MessageModel>
    suspend fun sendMessage(streamName: String, topicName: String, text: String)
    suspend fun sendReaction(messageId: Long, emojiCode: String, emojiName: String)

    //TODO: move to usecase
    suspend fun handleEvents(
        onNewMessage: (MessageEvent) -> Unit,
        onUpdateReaction: (Event) -> Unit
    )
}