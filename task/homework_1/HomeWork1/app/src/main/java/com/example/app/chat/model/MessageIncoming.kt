package com.example.app.chat.model

data class MessageIncoming(
    override val id: String,
    val text: String,
    val user: String,
    val time: Long,
    val reactions: List<Reaction>
) : ChatItem
