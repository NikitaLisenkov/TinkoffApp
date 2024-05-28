package com.example.app.domain.model

data class ReactionModel(
    val emojiName: String,
    val emojiCode: String,
    val reactionType: String,
    val userId: Long,
)