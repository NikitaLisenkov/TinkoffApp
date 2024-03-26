package com.example.app.chat.model

data class MessageOutgoing(
    override val id: String,
    val text: String,
    val time: Long,
    val reactions: List<Reaction>
) : ChatItem
