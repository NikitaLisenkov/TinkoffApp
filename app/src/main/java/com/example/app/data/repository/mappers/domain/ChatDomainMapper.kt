package com.example.app.data.repository.mappers.domain

import com.example.app.data.database.entity.MessagesWithReactions
import com.example.app.domain.model.MessageModel
import com.example.app.domain.model.ReactionModel

fun List<MessagesWithReactions>.toDomain() = this.map {
    val message = it.messageEntity
    MessageModel(
        msgId = message.messageId,
        senderName = message.senderFullName,
        content = message.content,
        senderId = message.senderId,
        time = message.time,
        avatarUrl = message.avatarUrl,
        reactions = it.reactionsList.map { reaction ->
            ReactionModel(
                emojiName = reaction.emojiName.orEmpty(),
                emojiCode = reaction.emojiCode,
                reactionType = reaction.reactionType,
                userId = reaction.userId
            )
        }
    )
}