package com.example.app.data.repository.mappers

import com.example.app.data.network.model.MessagesResponse
import com.example.app.domain.model.MessageModel
import com.example.app.domain.model.ReactionModel

fun MessagesResponse.toDomain(): List<MessageModel> = this.messages.map {
    MessageModel(
        msgId = it.msgId,
        senderName = it.senderName,
        content = it.content,
        senderId = it.senderId,
        time = it.time,
        avatarUrl = it.avatarUrl,
        reactions = it.reactions.map {
            ReactionModel(
                emojiName = it.emojiCode,
                emojiCode = it.emojiCode,
                reactionType = it.reactionType,
                userId = it.userId
            )
        },
        subject = it.subject
    )
}