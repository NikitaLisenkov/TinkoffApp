package com.example.app.data.repository

import android.util.Log
import com.example.app.Constants
import com.example.app.data.database.dao.ChatDao
import com.example.app.data.network.ZulipApi
import com.example.app.data.network.model.EventResponse
import com.example.app.data.network.model.EventType
import com.example.app.data.network.model.MessageResponse
import com.example.app.data.network.model.NarrowRequest
import com.example.app.data.network.model.NarrowType
import com.example.app.data.network.model.ReactionOpType
import com.example.app.data.repository.mappers.domain.toDomain
import com.example.app.data.repository.mappers.entity.toEntity
import com.example.app.data.repository.mappers.entity.toReactionEntity
import com.example.app.domain.model.MessageModel
import com.example.app.domain.repo.ChatRepository
import com.example.app.domain.repo.ChatRepository.Companion.PAGE_SIZE
import com.example.app.utils.runSuspendCatching
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ZulipApi,
    private val chatDao: ChatDao,
    private val gson: Gson
) : ChatRepository {

    override fun getMessagesFlow(streamName: String, topicName: String): Flow<List<MessageModel>> {
        return chatDao.getAllMessagesWithReactionsFlow(streamName, topicName).map {
            it.toDomain()
        }
    }

    override suspend fun fetchMessages(streamName: String, topicName: String) {
        val response = api.getMessages(
            narrow = buildNarrow(streamName = streamName, topicName = topicName),
            numBefore = PAGE_SIZE
        )

        insertMessages(
            messages = response.messages,
            streamName = streamName,
            topicName = topicName
        )
    }

    override suspend fun loadNextPage(firstMessageId: Long, streamName: String, topicName: String): Boolean {
        val response = api.getMessages(
            anchor = firstMessageId.toString(),
            narrow = buildNarrow(streamName = streamName, topicName = topicName),
            numAfter = 0,
            numBefore = PAGE_SIZE
        )

        insertMessages(
            messages = response.messages,
            streamName = streamName,
            topicName = topicName
        )

        return response.messages.singleOrNull()?.msgId == firstMessageId
    }

    private suspend fun insertMessages(
        messages: List<MessageResponse>,
        streamName: String,
        topicName: String
    ) {
        val messageEntities = messages.toEntity(streamName, topicName)
        chatDao.insertMessages(messageEntities)

        val reactionEntities = messages.flatMap { it.reactions.toEntity(it.msgId) }
        chatDao.insertReactions(reactionEntities)
    }

    override suspend fun sendMessage(streamName: String, topicName: String, text: String) {
        api.sendMessage(
            streamName = streamName,
            topicName = topicName,
            content = text
        )
    }

    override suspend fun sendReaction(messageId: Long, emojiName: String) {
        val message = chatDao.getMessageWithReaction(messageId) ?: return
        val isMyReaction = message.reactionsList.any {
            it.emojiName == emojiName && it.userId == Constants.MY_ID
        }
        if (isMyReaction) {
            api.deleteEmoji(messageId = messageId, emojiName = emojiName)
        } else {
            api.addEmoji(messageId = messageId, emojiName = emojiName)
        }
    }

    override suspend fun clearStorage(streamName: String, topicName: String, keepMessagesCount: Int) {
        val messages = chatDao.getMessages(streamName = streamName, topicName = topicName)
        val size = messages.size
        if (keepMessagesCount > size) return
        val messagesToRemove = messages.subList(keepMessagesCount, size).map { it.messageId }
        chatDao.deleteMessages(messagesToRemove)
    }

    override suspend fun handleEvents(
        streamName: String,
        topicName: String
    ) {
        coroutineScope {
            try {
                val eventRs = api.registerEvent()
                var eventId: Long = -1

                while (this.isActive) {
                    runSuspendCatching(
                        action = {
                            api.getEventsFromQueue(
                                queueId = eventRs.queueId,
                                lastEventId = eventId
                            )
                        },
                        onSuccess = { response ->
                            eventId = response.events.maxOf { it.id }
                            handleEvent(
                                response = response,
                                streamName = streamName,
                                topicName = topicName
                            )
                        },
                        onError = {
                            return@runSuspendCatching
                        }
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Throwable) {
                delay(3000)
                handleEvents(streamName, topicName)
            }
        }
    }

    private suspend fun handleEvent(response: EventResponse, streamName: String, topicName: String) {
        response.events.forEach { event ->
            Log.d("qwe", "NEW EVENT: $event")

            when (event.type) {
                EventType.MESSAGE -> {
                    event.message?.let {
                        insertMessages(
                            messages = listOf(
                                MessageResponse(
                                    msgId = it.id,
                                    senderName = it.senderFullName,
                                    content = it.content,
                                    senderId = it.senderId,
                                    time = it.timestamp,
                                    avatarUrl = it.avatarUrl,
                                    reactions = emptyList()
                                )
                            ),
                            streamName = streamName,
                            topicName = topicName
                        )
                    }
                }

                EventType.REACTION -> {
                    when (event.operationType) {
                        ReactionOpType.ADD -> {
                            chatDao.insertReaction(
                                reaction = event.toReactionEntity()
                            )
                        }

                        ReactionOpType.REMOVE -> {
                            chatDao.deleteReaction(
                                reaction = event.toReactionEntity()
                            )
                        }

                        null -> Unit
                    }
                }
            }
        }
    }

    private fun buildNarrow(streamName: String, topicName: String): String = gson.toJson(
        listOf(
            NarrowRequest(
                operator = NarrowType.STREAM_OPERATOR.stringValue,
                operand = streamName
            ),
            NarrowRequest(
                operator = NarrowType.TOPIC_OPERATOR.stringValue,
                operand = topicName
            )
        )
    )
}