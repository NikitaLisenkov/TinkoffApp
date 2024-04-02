package com.example.app.chat

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.app.R
import com.example.app.chat.adapter.ChatAdapter
import com.example.app.chat.model.ChatItem
import com.example.app.chat.model.Date
import com.example.app.chat.model.MessageIncoming
import com.example.app.chat.model.MessageOutgoing
import com.example.app.chat.model.Reaction
import com.example.app.databinding.FragmentChatBinding
import java.util.UUID

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val binding by viewBinding(FragmentChatBinding::bind)

    private val chatAdapter: ChatAdapter = ChatAdapter(
        onAddReactionClick = {
            onAddReactionsClick(it)
        },
        onEmojiClick = { emojiCode, msgId ->
            updateReactions(
                msgId = msgId,
                emojiCode = emojiCode
            )
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val channelName = it.getString(ARG_CHANNEL_NAME).orEmpty()
            val topicName = it.getString(ARG_TOPIC_NAME).orEmpty()
            binding.toolbar.title = channelName
            binding.tvTopic.text = topicName
        }

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupRecyclerView()
        setupInput(view)
        setupResultListener()
        fakeListMessage()
    }

    private fun setupRecyclerView() {
        binding.rvMessageList.adapter = chatAdapter
    }

    private fun setupInput(view: View) {
        binding.etText.addTextChangedListener {
            val text = it?.toString().orEmpty()
            if (text.isBlank()) {
                binding.ivSend.setImageResource(R.drawable.ic_add)
            } else {
                binding.ivSend.setImageResource(R.drawable.ic_send)
            }
        }

        binding.ivSend.setOnClickListener {
            val text = binding.etText.text.toString()
            if (text.isBlank()) {
                // TODO
            } else {
                chatAdapter.chatItems = chatAdapter.chatItems + listOf(
                    MessageOutgoing(
                        id = UUID.randomUUID().toString(),
                        text = text,
                        time = System.currentTimeMillis(),
                        reactions = emptyList()
                    )
                )
                binding.rvMessageList.smoothScrollToPosition(chatAdapter.chatItems.size - 1)
                binding.etText.text.clear()
            }
        }
    }

    private fun fakeListMessage() {
        chatAdapter.chatItems = FakeData.messages
    }

    private fun onAddReactionsClick(item: ChatItem) {
        ReactionsDialog()
            .apply { arguments = bundleOf(ReactionsDialog.ARG_MSG_ID to item.id) }
            .show(parentFragmentManager, null)
    }

    private fun setupResultListener() {
        parentFragmentManager.setFragmentResultListener(
            ReactionsDialog.REQUEST_KEY,
            this
        ) { requestKey, result ->
            val msgId = result.getString(ReactionsDialog.RESULT_KEY_MSG_ID)
            val emojiCode = result.getString(ReactionsDialog.RESULT_KEY_EMOJI)
            if (msgId != null && emojiCode != null) {
                updateReactions(
                    msgId = msgId,
                    emojiCode = emojiCode
                )
            }
        }
    }

    private fun updateReactions(msgId: String, emojiCode: String) {
        chatAdapter.chatItems = chatAdapter.chatItems.map { item ->
            if (item.id == msgId) {
                when (item) {
                    is Date -> item

                    is MessageIncoming -> item.copy(
                        reactions = updateReaction(emojiCode, item.reactions)
                    )

                    is MessageOutgoing -> item.copy(
                        reactions = updateReaction(emojiCode, item.reactions)
                    )
                }
            } else {
                item
            }
        }
    }

    private fun updateReaction(
        emojiCode: String,
        currentReactions: List<Reaction>
    ): List<Reaction> {
        return if (currentReactions.find { it.code == emojiCode } == null) {
            currentReactions + listOf(
                Reaction(code = emojiCode, counter = 1)
            )
        } else {
            val updatedReactions = currentReactions.map {
                if (it.code == emojiCode) {
                    it.copy(counter = it.counter - 1)
                } else {
                    it
                }
            }.toMutableList()
            updatedReactions.removeIf { it.counter <= 0 }
            updatedReactions
        }
    }

    companion object {
        private const val ARG_CHANNEL_NAME: String = "ARG_CHANNEL_NAME"
        private const val ARG_TOPIC_NAME: String = "ARG_TOPIC_NAME"

        fun newInstance(channelName: String, topicName: String): ChatFragment = ChatFragment().apply {
            arguments = bundleOf(
                ARG_CHANNEL_NAME to channelName,
                ARG_TOPIC_NAME to topicName
            )
        }

    }
}