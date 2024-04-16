package com.example.app.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.app.chat.model.ChatItem

class ChatDiffCallback : DiffUtil.ItemCallback<ChatItem>() {

    override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return oldItem == newItem
    }
}

