package com.example.app.domain.model

data class MessageModel(
    val msgId: Long,
    val senderName: String,
    val content: String,
    val senderId: Long,
    val time: Long,
    val avatarUrl: String?,
    val reactions: List<ReactionModel>,
)