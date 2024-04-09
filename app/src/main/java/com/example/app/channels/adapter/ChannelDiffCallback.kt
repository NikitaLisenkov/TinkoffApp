package com.example.app.channels.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.app.channels.model.ChannelsItem

class ChannelDiffCallback : DiffUtil.ItemCallback<ChannelsItem>() {

    override fun areItemsTheSame(oldItem: ChannelsItem, newItem: ChannelsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChannelsItem, newItem: ChannelsItem): Boolean {
        return oldItem == newItem
    }
}