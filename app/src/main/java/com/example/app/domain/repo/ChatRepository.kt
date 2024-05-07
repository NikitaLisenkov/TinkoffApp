package com.example.app.domain.repo

import com.example.app.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(streamName: String, topicName: String, text: String)
    suspend fun sendReaction(messageId: Long, emojiName: String)
    fun getMessagesFlow(streamName: String, topicName: String): Flow<List<MessageModel>>
    suspend fun fetchMessages(streamName: String, topicName: String)
    suspend fun handleEvents(streamName: String, topicName: String)
    suspend fun loadNextPage(firstMessageId: Long, streamName: String, topicName: String): Boolean
    suspend fun clearStorage(streamName: String, topicName: String, keepMessagesCount: Int = MAX_MESSAGES_COUNT)

    companion object {
        const val PAGE_SIZE: Int = 20
        const val MAX_MESSAGES_COUNT: Int = 50
    }
}