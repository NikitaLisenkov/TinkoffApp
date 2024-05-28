package com.example.app.presentation.channels.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.databinding.ItemChannelBinding
import com.example.app.databinding.ItemTopicBinding
import com.example.app.presentation.channels.model.ChannelsItemUi

class ChannelsAdapter(
    private val onChannelClick: (ChannelsItemUi.StreamUi) -> Unit,
    private val onTopicClick: (ChannelsItemUi.TopicUi) -> Unit
) : ListAdapter<ChannelsItemUi, RecyclerView.ViewHolder>(ChannelDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ChannelsItemUi.StreamUi -> CHANNEL_VIEW_TYPE
            is ChannelsItemUi.TopicUi -> TOPIC_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CHANNEL_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
                ChannelViewHolder(view).apply {
                    itemView.setOnClickListener {
                        onChannelClick.invoke(
                            getItem(adapterPosition) as ChannelsItemUi.StreamUi
                        )
                    }
                }
            }

            TOPIC_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
                TopicViewHolder(view).apply {
                    itemView.setOnClickListener {
                        onTopicClick.invoke(
                            getItem(adapterPosition) as ChannelsItemUi.TopicUi
                        )
                    }
                }
            }

            else -> throw Exception(INVALID_VIEW_TYPE)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChannelViewHolder -> holder.bind(getItem(position) as ChannelsItemUi.StreamUi)
            is TopicViewHolder -> holder.bind(getItem(position) as ChannelsItemUi.TopicUi)
        }
    }

    class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemChannelBinding.bind(itemView)

        fun bind(item: ChannelsItemUi.StreamUi) {
            binding.tvText.text = itemView.context.getString(R.string.channel_name_placeholder, item.text)
            binding.ivArrow.setImageResource(
                if (item.isExpanded) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
            )
        }
    }

    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemTopicBinding.bind(itemView)

        fun bind(item: ChannelsItemUi.TopicUi) {
            binding.tvText.text = item.text
            binding.tvText.setBackgroundResource(item.backgroundColorRes)
            val isEven = adapterPosition % 2 == 0
            val bgColor = if (isEven) R.color.cyan_light else R.color.yellow
            itemView.setBackgroundResource(bgColor)
        }
    }

    private companion object {
        const val CHANNEL_VIEW_TYPE = 0
        const val TOPIC_VIEW_TYPE = 1
        const val INVALID_VIEW_TYPE = "Error view type"
    }
}