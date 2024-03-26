package com.example.app.chat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.addReactions
import com.example.app.chat.model.ChatItem
import com.example.app.chat.model.Date
import com.example.app.chat.model.MessageIncoming
import com.example.app.chat.model.MessageOutgoing
import com.example.app.chat.view.FlexboxLayout
import com.example.app.chat.view.MessageViewGroup

class ChatAdapter(
    private val onAddReactionClick: (ChatItem) -> Unit,
    private val onEmojiClick: (emojiCode: String, msgId: String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var chatItems = listOf<ChatItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when (chatItems[position]) {
            is MessageIncoming -> MESSAGE_INCOMING_VIEW_TYPE
            is MessageOutgoing -> MESSAGE_OUTGOING_VIEW_TYPE
            is Date -> DATE_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MESSAGE_INCOMING_VIEW_TYPE -> {
                val viewMessageIncoming = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_incoming, parent, false)
                return MessageIncomingViewHolder(viewMessageIncoming, onEmojiClick).apply {
                    itemView.setOnLongClickListener {
                        onAddReactionClick.invoke(
                            requireNotNull(item)
                        )
                        true
                    }
                    reactionsLayout.setButtonPlusClickListener {
                        onAddReactionClick.invoke(
                            requireNotNull(item)
                        )
                    }
                }
            }

            MESSAGE_OUTGOING_VIEW_TYPE -> {
                val viewMessageOutgoing = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_outgoing, parent, false)
                return MessageOutgoingViewHolder(viewMessageOutgoing, onEmojiClick).apply {
                    itemView.setOnLongClickListener {
                        onAddReactionClick.invoke(
                            requireNotNull(item)
                        )
                        true
                    }
                    reactFlexbox.setButtonPlusClickListener {
                        onAddReactionClick.invoke(
                            requireNotNull(item)
                        )
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
            is DateViewHolder -> holder.bind(chatItems[position] as Date)
            is MessageIncomingViewHolder -> holder.bind(chatItems[position] as MessageIncoming)
            is MessageOutgoingViewHolder -> holder.bind(chatItems[position] as MessageOutgoing)
        }
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txDate: TextView = itemView.findViewById(R.id.txDate)

        fun bind(dateChat: Date) {
            txDate.text = dateChat.date
        }
    }

    class MessageIncomingViewHolder(
        itemView: View,
        private val onEmojiClick: (emojiCode: String, msgId: String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        var item: MessageIncoming? = null

        private val messageView = itemView as MessageViewGroup
        private val avatarImageView = messageView.avatarImageView
        private val nameTextView = messageView.nameTextView
        private val messageTextView = messageView.messageTextView

        val reactionsLayout = messageView.reactionsLayout

        fun bind(msgIn: MessageIncoming) {
            item = msgIn
            avatarImageView.setImageResource(R.drawable.ic_darrel)
            nameTextView.text = msgIn.text
            messageTextView.text = msgIn.text
            reactionsLayout.removeAllViews()
            reactionsLayout.addReactions(
                reactions = msgIn.reactions,
                onEmojiClick = { emojiCode ->
                    onEmojiClick.invoke(emojiCode, msgIn.id)
                }
            )
        }
    }

    class MessageOutgoingViewHolder(
        itemView: View,
        private val onEmojiClick: (emojiCode: String, msgId: String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        var item: MessageOutgoing? = null

        private val txMessageOutgoing: TextView = itemView.findViewById(R.id.txMessageOutgoing)

        val reactFlexbox: FlexboxLayout = itemView.findViewById(R.id.reactFlexbox)

        fun bind(msgOut: MessageOutgoing) {
            item = msgOut
            txMessageOutgoing.text = msgOut.text
            reactFlexbox.removeAllViews()
            reactFlexbox.addReactions(
                reactions = msgOut.reactions,
                onEmojiClick = { emojiCode ->
                    onEmojiClick.invoke(emojiCode, msgOut.id)
                }
            )
        }
    }

    companion object {
        const val DATE_VIEW_TYPE = 1
        const val MESSAGE_INCOMING_VIEW_TYPE = 2
        const val MESSAGE_OUTGOING_VIEW_TYPE = 3
        const val INVALID_VIEW_TYPE = "Error view type"
    }
}