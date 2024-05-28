package com.example.app.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.R
import com.example.app.databinding.ItemMessageOutgoingBinding
import com.example.app.presentation.chat.model.ChatItemUi
import com.example.app.presentation.chat.model.DateItemUi
import com.example.app.presentation.chat.model.MessageIncomingUi
import com.example.app.presentation.chat.model.MessageOutgoingUi
import com.example.app.presentation.chat.view.MessageViewGroup
import com.example.app.utils.setEmojis

class ChatAdapter(
    private val onAddReactionClick: (ChatItemUi) -> Unit,
    private val onEmojiClick: (emojiName: String, msgId: Long) -> Unit
) : ListAdapter<ChatItemUi, RecyclerView.ViewHolder>(ChatDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MessageIncomingUi -> MESSAGE_INCOMING_VIEW_TYPE
            is MessageOutgoingUi -> MESSAGE_OUTGOING_VIEW_TYPE
            is DateItemUi -> DATE_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MESSAGE_INCOMING_VIEW_TYPE -> {
                val viewMessageIncoming = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_incoming, parent, false)
                return MessageIncomingViewHolder(viewMessageIncoming, onEmojiClick, onAddReactionClick).apply {
                    itemView.setOnLongClickListener {
                        onAddReactionClick.invoke(
                            requireNotNull(item)
                        )
                        true
                    }
                }
            }

            MESSAGE_OUTGOING_VIEW_TYPE -> {
                val viewMessageOutgoing = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_outgoing, parent, false)
                return MessageOutgoingViewHolder(viewMessageOutgoing, onEmojiClick, onAddReactionClick).apply {
                    itemView.setOnLongClickListener {
                        onAddReactionClick.invoke(
                            requireNotNull(item)
                        )
                        true
                    }
                }
            }

            DATE_VIEW_TYPE -> {
                val viewDate = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_date, parent, false)
                DateViewHolder(viewDate)
            }

            else -> throw Exception(INVALID_VIEW_TYPE)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DateViewHolder -> holder.bind(getItem(position) as DateItemUi)
            is MessageIncomingViewHolder -> holder.bind(getItem(position) as MessageIncomingUi)
            is MessageOutgoingViewHolder -> holder.bind(getItem(position) as MessageOutgoingUi)
        }
    }

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txDate: TextView = itemView.findViewById(R.id.tv_date)

        fun bind(dateItem: DateItemUi) {
            txDate.text = dateItem.date
        }
    }

    class MessageIncomingViewHolder(
        itemView: View,
        private val onEmojiClick: (emojiName: String, msgId: Long) -> Unit,
        private val onAddReactionClick: (ChatItemUi) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {

        var item: MessageIncomingUi? = null

        private val messageView = itemView as MessageViewGroup

        fun bind(msgIn: MessageIncomingUi) {
            item = msgIn
            with(messageView.binding) {
                Glide.with(itemView)
                    .load(msgIn.avatarUrl)
                    .centerCrop()
                    .into(ivAvatar)
                tvUsername.text = msgIn.userName
                tvMessage.text = msgIn.text
                tvTimestamp.text = msgIn.time
            }
            if (msgIn.reactions.isNotEmpty()) {
                messageView.setEmojis(
                    items = msgIn.reactions,
                    onEmojiClick = { name, _ -> onEmojiClick.invoke(name, msgIn.id) },
                    onAddClick = { onAddReactionClick.invoke(msgIn) }
                )
            } else {
                messageView.binding.flexbox.removeAllViews()
            }
        }
    }

    class MessageOutgoingViewHolder(
        itemView: View,
        private val onEmojiClick: (emojiName: String, msgId: Long) -> Unit,
        private val onAddReactionClick: (ChatItemUi) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {

        var item: MessageOutgoingUi? = null

        private val binding: ItemMessageOutgoingBinding = ItemMessageOutgoingBinding.bind(itemView)

        fun bind(msgOut: MessageOutgoingUi) {
            item = msgOut
            with(binding) {
                tvMessage.text = msgOut.text
                tvTimestamp.text = msgOut.time
                if (msgOut.reactions.isNotEmpty()) {
                    flexbox.setEmojis(
                        items = msgOut.reactions,
                        onEmojiClick = { name, _ -> onEmojiClick.invoke(name, msgOut.id) },
                        onAddClick = { onAddReactionClick.invoke(msgOut) }
                    )
                } else {
                    flexbox.removeAllViews()
                }
            }
        }
    }

    companion object {
        const val DATE_VIEW_TYPE = 1
        const val MESSAGE_INCOMING_VIEW_TYPE = 2
        const val MESSAGE_OUTGOING_VIEW_TYPE = 3
        const val INVALID_VIEW_TYPE = "Error view type"
    }
}