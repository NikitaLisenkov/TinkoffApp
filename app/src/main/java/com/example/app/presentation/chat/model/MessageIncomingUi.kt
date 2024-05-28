package com.example.app.presentation.chat.model

data class MessageIncomingUi(
    override val id: Long,
    val text: String,
    val userName: String,
    val time: String,
    val avatarUrl: String?,
    val reactions: List<ReactionUi>
) : ChatItemUi
