package com.example.app.data

import android.util.Log
import com.example.app.data.model.Event
import com.example.app.data.model.EventType
import com.example.app.data.model.Message
import com.example.app.data.model.MessageEvent
import com.example.app.data.model.NarrowRequest
import com.example.app.data.model.NarrowType
import com.example.app.data.model.ReactionOpType
import com.example.app.di.GlobalDI
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import java.net.SocketTimeoutException

class ChatRepository {

    private val api = GlobalDI.zulipApi

    suspend fun getMessages(streamName: String, topicName: String): List<Message> {
        return api.getMessages(
            narrow = Gson().toJson(
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
        ).messages
    }

    suspend fun sendMessage(streamName: String, topicName: String, text: String) {
        api.sendMessage(
            streamName = streamName,
            topicName = topicName,
            content = text
        )
    }

    suspend fun sendReaction(messageId: Long, emojiCode: String, emojiName: String) {
        api.addEmoji(
            messageId = messageId,
            emojiCode = emojiCode,
            emojiName = emojiName
        )
    }

    suspend fun handleEvents(
        onNewMessage: (MessageEvent) -> Unit,
        onUpdateReaction: (Event) -> Unit
    ) {
        coroutineScope {
            val eventRs = api.registerEvent()
            var eventId: Long = -1

            while (this.isActive) {
                runCatching {
                    val response = api.getEventsFromQueue(
                        queueId = eventRs.queueId,
                        lastEventId = eventId
                    )

                    eventId = response.events.maxOf { it.id }
                    response
                }
                    .onFailure { exception ->
                        if (exception is SocketTimeoutException) {
                            return@onFailure
                        }
                        throw exception
                    }
                    .onSuccess {
                        it.events.forEach { event ->
                            Log.d("qwe", "NEW EVENT: $event")

                            when (event.type) {
                                EventType.MESSAGE -> {
                                    event.message?.let(onNewMessage)
                                }

                                EventType.REACTION -> {
                                    when (event.operationType) {
                                        ReactionOpType.ADD, ReactionOpType.REMOVE -> onUpdateReaction.invoke(event)
                                        else -> Unit
                                    }
                                }
                            }
                        }
                        return@onSuccess
                    }
            }
        }
    }

}