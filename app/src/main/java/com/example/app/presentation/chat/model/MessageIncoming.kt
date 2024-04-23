package com.example.app.presentation.chat.model

data class MessageIncoming(
    override val id: Long,
    val text: String,
    val user: String,
    val time: Long,
    val reactions: List<Reaction>
) : ChatItem
