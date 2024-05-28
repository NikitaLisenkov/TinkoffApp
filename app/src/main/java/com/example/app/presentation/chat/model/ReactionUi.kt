package com.example.app.presentation.chat.model

data class ReactionUi(
    val code: String,
    val name: String,
    val counter: Int,
    val isSelected: Boolean
)