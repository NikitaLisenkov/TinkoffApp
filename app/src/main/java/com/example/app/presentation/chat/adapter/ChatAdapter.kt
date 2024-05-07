package com.example.app.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.utils.addReactions
import com.example.app.presentation.chat.model.ChatItemUi
import com.example.app.presentation.chat.model.DateItemUi
import com.example.app.presentation.chat.model.MessageIncomingUi
import com.example.app.presentation.chat.model.MessageOutgoingUi
import com.example.app.presentation.chat.view.FlexboxLayout
import com.example.app.presentation.chat.view.MessageViewGroup

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
            is DateViewHolder -> holder.bind(getItem(position) as DateItemUi)
            is MessageIncomingViewHolder -> holder.bind(getItem(position) as MessageIncomingUi)
            is MessageOutgoingViewHolder -> holder.bind(getItem(position) as MessageOutgoingUi)
        }
    }

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txDate: TextView = itemView.findViewById(R.id.txDate)

        fun bind(dateItem: DateItemUi) {
            txDate.text = dateItem.date
        }
    }

    class MessageIncomingViewHolder(
        itemView: View,
        private val onEmojiClick: (emojiName: String, msgId: Long) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        var item: MessageIncomingUi? = null

        private val messageView = itemView as MessageViewGroup
        private val avatarImageView = messageView.avatarImageView
        private val nameTextView = messageView.nameTextView
        private val messageTextView = messageView.messageTextView

        val reactionsLayout = messageView.reactionsLayout

        fun bind(msgIn: MessageIncomingUi) {
            item = msgIn
            avatarImageView.setImageResource(R.drawable.ic_darrel)
            nameTextView.text = msgIn.text
            messageTextView.text = msgIn.text
            reactionsLayout.removeAllViews()
            reactionsLayout.addReactions(
                reactions = msgIn.reactions,
                onEmojiClick = { emojiName ->
                    onEmojiClick.invoke(emojiName, msgIn.id)
                }
            )
        }
    }

    class MessageOutgoingViewHolder(
        itemView: View,
        private val onEmojiClick: (emojiName: String, msgId: Long) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        var item: MessageOutgoingUi? = null

        private val txMessageOutgoing: TextView = itemView.findViewById(R.id.txMessageOutgoing)

        val reactFlexbox: FlexboxLayout = itemView.findViewById(R.id.reactFlexbox)

        fun bind(msgOut: MessageOutgoingUi) {
            item = msgOut
            txMessageOutgoing.text = msgOut.text
            reactFlexbox.removeAllViews()
            reactFlexbox.addReactions(
                reactions = msgOut.reactions,
                onEmojiClick = { emojiName ->
                    onEmojiClick.invoke(emojiName, msgOut.id)
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