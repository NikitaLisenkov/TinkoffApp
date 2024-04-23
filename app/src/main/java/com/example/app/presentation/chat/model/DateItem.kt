package com.example.app.presentation.chat.model

data class DateItem(
    val date: String
) : ChatItem {
    override val id: Long = date.hashCode().toLong()
}
