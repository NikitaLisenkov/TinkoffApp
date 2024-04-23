package com.example.app.presentation.chat

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.app.R
import com.example.app.databinding.FragmentChatBinding
import com.example.app.presentation.chat.ChatViewModel.Action
import com.example.app.presentation.chat.adapter.ChatAdapter
import com.example.app.presentation.chat.model.ChatItem
import com.example.app.presentation.chat.reactions.ReactionsDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val binding: FragmentChatBinding by viewBinding(FragmentChatBinding::bind)

    private val viewModel: ChatViewModel by viewModels()

    private val adapter: ChatAdapter = ChatAdapter(
        onAddReactionClick = {
            onAddReactionsClick(it)
        },
        onEmojiClick = { emojiCode, msgId ->
            viewModel.sendAction(
                Action.SendReaction(
                    msgId = msgId,
                    emojiCode = emojiCode
                )
            )
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val streamName = it.getString(ARG_STREAM_NAME).orEmpty()
            val topicName = it.getString(ARG_TOPIC_NAME).orEmpty()
            binding.toolbar.title = streamName
            binding.tvTopic.text = topicName

            val loadAction = Action.LoadData(streamName = streamName, topicName = topicName)

            viewModel.sendAction(loadAction)

            binding.layoutError.btnRetry.setOnClickListener {
                viewModel.sendAction(loadAction)
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupRecyclerView()
        setupInput()
        setupResultListener()

        viewModel.messageList
            .map { state -> ChatViewModel.State.Content(items = state) }
            .flowWithLifecycle(lifecycle)
            .onEach { render(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupRecyclerView() {
        binding.rvMessageList.adapter = adapter
    }

    private fun setupInput() {
        with(binding) {
            etText.addTextChangedListener {
                val text = it?.toString().orEmpty()
                if (text.isBlank()) {
                    ivSend.setImageResource(R.drawable.ic_add)
                } else {
                    ivSend.setImageResource(R.drawable.ic_send)
                }
            }
            ivSend.setOnClickListener {
                val text = etText.text.toString()
                if (text.isBlank()) {
                    Snackbar.make(requireView(), "Enter message!", Snackbar.LENGTH_SHORT).show()
                } else {
                    viewModel.sendAction(Action.SendMessage(text))
                    etText.text.clear()
                }
            }
        }
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
            val msgId = result.getLong(ReactionsDialog.RESULT_KEY_MSG_ID, -1)
            val emojiCode = result.getString(ReactionsDialog.RESULT_KEY_EMOJI)
            if (msgId != -1L && emojiCode != null) {
                viewModel.sendAction(
                    Action.SendReaction(msgId, emojiCode)
                )
            }
        }
    }

    private fun render(state: ChatViewModel.State) {
        with(binding) {
            when (state) {
                is ChatViewModel.State.Loading -> {
                    progress.isVisible = true
                    rvMessageList.isGone = true
                    layoutError.root.isGone = true
                }

                is ChatViewModel.State.Content -> {
                    layoutError.root.isGone = true
                    progress.isGone = true
                    rvMessageList.isVisible = true

                    val previousSize = adapter.currentList.size
                    adapter.submitList(state.items) {
                        val position = adapter.currentList.size - 1
                        when {
                            previousSize == 0 -> {
                                rvMessageList.scrollToPosition(position)
                            }

                            previousSize != adapter.currentList.size -> {
                                rvMessageList.smoothScrollToPosition(position)
                            }
                        }
                    }
                }

                is ChatViewModel.State.Error -> {
                    layoutError.root.isVisible = true
                    rvMessageList.isGone = true
                    progress.isGone = true
                }
            }

        }
    }

    companion object {
        const val TAG: String = "TAG_CHAT_FRAGMENT"

        private const val ARG_STREAM_NAME: String = "ARG_STREAM_NAME"
        private const val ARG_TOPIC_NAME: String = "ARG_TOPIC_NAME"

        fun newInstance(streamName: String, topicName: String): ChatFragment =
            ChatFragment().apply {
                arguments = bundleOf(
                    ARG_STREAM_NAME to streamName,
                    ARG_TOPIC_NAME to topicName
                )
            }

    }
}