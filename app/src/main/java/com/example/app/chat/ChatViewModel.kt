package com.example.app.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.FakeData
import com.example.app.chat.model.ChatItem
import com.example.app.chat.model.MessageIncoming
import com.example.app.chat.model.MessageOutgoing
import com.example.app.chat.model.Reaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel : ViewModel() {

    private val _messageList: MutableStateFlow<List<ChatItem>> = MutableStateFlow(FakeData.messages)

    val messageList: StateFlow<List<ChatItem>> = _messageList.asStateFlow()

    fun sendMessage(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newList = _messageList.value.toMutableList()
            newList.add(
                MessageOutgoing(
                    id = UUID.randomUUID().toString(),
                    text = text,
                    time = System.currentTimeMillis(),
                    reactions = emptyList()
                )
            )
            _messageList.value = newList
        }
    }

    fun updateReactions(msgId: String, emojiCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newList = _messageList.value.map { item ->
                if (item.id == msgId) {
                    when (item) {
                        is com.example.app.chat.model.Date -> item
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
            _messageList.value = newList
        }
    }

    private fun updateReaction(
        emojiCode: String,
        currentReactions: List<Reaction>
    ): List<Reaction> {
        return if (currentReactions.find { it.code == emojiCode } == null) {
            currentReactions + listOf(
                Reaction(
                    code = emojiCode,
                    counter = 1
                )
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