package com.example.app.chat.model

data class Date(
    val date: String
) : ChatItem {
    override val id: String = date
}
