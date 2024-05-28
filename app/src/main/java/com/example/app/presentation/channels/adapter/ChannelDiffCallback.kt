package com.example.app.presentation.channels.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.app.presentation.channels.model.ChannelsItemUi

class ChannelDiffCallback : DiffUtil.ItemCallback<ChannelsItemUi>() {

    override fun areItemsTheSame(oldItem: ChannelsItemUi, newItem: ChannelsItemUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChannelsItemUi, newItem: ChannelsItemUi): Boolean {
        return oldItem == newItem
    }
}