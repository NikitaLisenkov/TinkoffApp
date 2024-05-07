package com.example.app.presentation.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.app.presentation.chat.model.ChatItemUi

class ChatDiffCallback : DiffUtil.ItemCallback<ChatItemUi>() {

    override fun areItemsTheSame(oldItem: ChatItemUi, newItem: ChatItemUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatItemUi, newItem: ChatItemUi): Boolean {
        return oldItem == newItem
    }
}

