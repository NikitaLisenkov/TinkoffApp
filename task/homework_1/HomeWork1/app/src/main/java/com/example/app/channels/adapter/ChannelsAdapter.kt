package com.example.app.channels.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.channels.model.ChannelsItem
import com.example.app.databinding.ItemChannelBinding
import com.example.app.databinding.ItemTopicBinding

class ChannelsAdapter(
    private val onChannelClick: (ChannelsItem.Channel) -> Unit,
    private val onTopicClick: (ChannelsItem.Topic) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var channelsItems = listOf<ChannelsItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when (channelsItems[position]) {
            is ChannelsItem.Channel -> CHANNEL_VIEW_TYPE
            is ChannelsItem.Topic -> TOPIC_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CHANNEL_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
                ChannelViewHolder(view).apply {
                    itemView.setOnClickListener {
                        onChannelClick.invoke(
                            channelsItems[adapterPosition] as ChannelsItem.Channel
                        )
                    }
                }
            }

            TOPIC_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
                TopicViewHolder(view).apply {
                    itemView.setOnClickListener {
                        onTopicClick.invoke(
                            channelsItems[adapterPosition] as ChannelsItem.Topic
                        )
                    }
                }
            }

            else -> throw Exception(INVALID_VIEW_TYPE)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChannelViewHolder -> holder.bind(channelsItems[position] as ChannelsItem.Channel)
            is TopicViewHolder -> holder.bind(channelsItems[position] as ChannelsItem.Topic)
        }
    }

    override fun getItemCount(): Int {
        return channelsItems.size
    }

    class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemChannelBinding.bind(itemView)

        fun bind(item: ChannelsItem.Channel) {
            binding.tvText.text = item.text
            binding.ivArrow.setImageResource(
                if (item.isExpanded) R.drawable.ic_arrow_down_24 else R.drawable.ic_arrow_up_24
            )
        }
    }

    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemTopicBinding.bind(itemView)

        fun bind(item: ChannelsItem.Topic) {
            binding.tvText.text = item.text
            binding.tvText.setBackgroundResource(item.backgroundColorRes)
        }
    }

    private companion object {
        const val CHANNEL_VIEW_TYPE = 0
        const val TOPIC_VIEW_TYPE = 1
        const val INVALID_VIEW_TYPE = "Error view type"
    }
}