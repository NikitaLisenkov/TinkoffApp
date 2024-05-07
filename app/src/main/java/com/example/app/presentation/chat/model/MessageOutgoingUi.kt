package com.example.app.presentation.chat.model

data class MessageOutgoingUi(
    override val id: Long,
    val text: String,
    val time: Long,
    val reactions: List<ReactionUi>
) : ChatItemUi
