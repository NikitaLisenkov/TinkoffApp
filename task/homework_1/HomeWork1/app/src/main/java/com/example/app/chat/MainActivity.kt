package com.example.app.chat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.chat.adapter.ChatAdapter
import com.example.app.chat.model.ChatItem
import com.example.app.chat.model.Date
import com.example.app.chat.model.MessageIncoming
import com.example.app.chat.model.MessageOutgoing
import com.example.app.chat.model.Reaction
import java.util.UUID

class MainActivity : AppCompatActivity() {

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

    private lateinit var rvMessageList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        setupInput()
        setupResultListener()
        fakeListMessage()
    }

    private fun setupRecyclerView() {
        rvMessageList = findViewById(R.id.rvMessageList)
        rvMessageList.adapter = chatAdapter
    }

    private fun setupInput() {
        val edText = findViewById<EditText>(R.id.et_text)
        val ivSend = findViewById<ImageView>(R.id.iv_send)

        edText.addTextChangedListener {
            val text = it?.toString().orEmpty()
            if (text.isBlank()) {
                ivSend.setImageResource(R.drawable.ic_add)
            } else {
                ivSend.setImageResource(R.drawable.ic_send)
            }
        }

        ivSend.setOnClickListener {
            val text = edText.text.toString()
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
                rvMessageList.smoothScrollToPosition(chatAdapter.chatItems.size - 1)
                edText.text.clear()
            }
        }
    }

    private fun fakeListMessage() {
        chatAdapter.chatItems = FakeData.messages
    }

    private fun onAddReactionsClick(item: ChatItem) {
        ReactionsDialog()
            .apply { arguments = bundleOf(ReactionsDialog.ARG_MSG_ID to item.id) }
            .show(supportFragmentManager, null)
    }

    private fun setupResultListener() {
        supportFragmentManager.setFragmentResultListener(ReactionsDialog.REQUEST_KEY, this) { requestKey, result ->
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

    private fun updateReaction(emojiCode: String, currentReactions: List<Reaction>): List<Reaction> {
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

}