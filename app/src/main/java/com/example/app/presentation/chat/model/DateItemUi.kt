package com.example.app.presentation.chat.model

data class DateItemUi(
    val date: String
) : ChatItemUi {
    override val id: Long = date.hashCode().toLong()
}
