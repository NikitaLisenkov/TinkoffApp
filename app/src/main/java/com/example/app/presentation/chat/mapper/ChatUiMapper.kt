package com.example.app.presentation.chat.mapper

import com.example.app.Constants
import com.example.app.domain.model.MessageModel
import com.example.app.domain.model.ReactionModel
import com.example.app.presentation.chat.model.ChatItemUi
import com.example.app.presentation.chat.model.DateItemUi
import com.example.app.presentation.chat.model.MessageIncomingUi
import com.example.app.presentation.chat.model.MessageOutgoingUi
import com.example.app.presentation.chat.model.ReactionUi
import com.example.app.utils.DateUtils
import com.example.app.utils.EmojiUtils

fun List<MessageModel>.toChatItems(myId: Long): List<ChatItemUi> =
    groupBy { DateUtils.convertSecondsToDateStr(it.time) }
        .flatMap { (date, messages) ->
            listOf(DateItemUi(date)) + messages.map {
                val formattedTime = DateUtils.convertSecondsToDateStr(it.time, DateUtils.sdfHourMinute)
                if (it.senderId == Constants.MY_ID) {
                    MessageOutgoingUi(
                        id = it.msgId,
                        text = it.content,
                        time = formattedTime,
                        reactions = it.reactions.toReactionItems(myId)
                    )
                } else {
                    MessageIncomingUi(
                        id = it.msgId,
                        text = it.content,
                        time = formattedTime,
                        reactions = it.reactions.toReactionItems(myId),
                        avatarUrl = it.avatarUrl,
                        userName = it.senderName
                    )
                }
            }
        }

fun List<ReactionModel>.toReactionItems(myId: Long): List<ReactionUi> =
    filter { it.reactionType == "unicode_emoji" }
        .groupBy { it.emojiCode }
        .map { (unicode, reactions) ->
            ReactionUi(
                code = EmojiUtils.unicodeSequenceToString(unicode),
                name = reactions.firstOrNull()?.emojiName.orEmpty(),
                counter = reactions.size,
                isSelected = reactions.any { it.userId == myId }
            )
        }
        .sortedBy { it.name }