package com.example.app.presentation.chat.mapper

import com.example.app.Constants
import com.example.app.domain.model.MessageModel
import com.example.app.domain.model.ReactionModel
import com.example.app.presentation.chat.model.ChatItem
import com.example.app.presentation.chat.model.MessageIncoming
import com.example.app.presentation.chat.model.MessageOutgoing
import com.example.app.presentation.chat.model.Reaction
import com.example.app.utils.EmojiUtils

fun List<MessageModel>.toChatItems(): List<ChatItem> = map {
    if (it.senderId == Constants.MY_ID) {
        MessageOutgoing(
            id = it.msgId,
            text = it.content,
            time = it.time,
            reactions = it.reactions.toReactionItems()
        )
    } else {
        MessageIncoming(
            id = it.msgId,
            text = it.content,
            time = it.time,
            reactions = it.reactions.toReactionItems(),
            user = it.senderName
        )
    }
}

fun List<ReactionModel>.toReactionItems(): List<Reaction> =
    filter { it.reactionType == "unicode_emoji" }
        .groupBy { it.emojiCode }
        .map { (unicode, reactions) ->
            Reaction(
                code = EmojiUtils.unicodeSequenceToString(unicode),
                counter = reactions.size
            )
        }