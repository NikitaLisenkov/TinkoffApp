package com.example.app.data.repository.mappers.entity

import com.example.app.data.database.entity.MessageEntity
import com.example.app.data.database.entity.ReactionEntity
import com.example.app.data.network.model.Event
import com.example.app.data.network.model.MessageResponse
import com.example.app.data.network.model.ReactionResponse

fun Event.toReactionEntity() = ReactionEntity(
    messageId = this.messageId ?: 0L,
    userId = this.reactionUserId ?: 0L,
    emojiCode = this.emojiCode.orEmpty(),
    emojiName = this.emojiName,
    reactionType = "unicode_emoji"
)

fun List<MessageResponse>.toEntity(streamName: String, topicName: String): List<MessageEntity> = map {
    it.toEntity(streamName = streamName, topicName = topicName)
}

fun MessageResponse.toEntity(streamName: String, topicName: String): MessageEntity = MessageEntity(
    messageId = this.msgId,
    senderFullName = this.senderName,
    content = this.content,
    senderId = this.senderId,
    time = this.time,
    avatarUrl = this.avatarUrl.orEmpty(),
    streamName = streamName,
    topicName = topicName
)

fun List<ReactionResponse>.toEntity(messageId: Long): List<ReactionEntity> = this.map {
    ReactionEntity(
        messageId = messageId,
        userId = it.userId,
        emojiCode = it.emojiCode,
        emojiName = it.emojiName,
        reactionType = it.reactionType
    )
}