package com.example.app.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.chat.model.ChatItem
import com.example.app.chat.model.DateItem
import com.example.app.chat.model.MessageIncoming
import com.example.app.chat.model.MessageOutgoing
import com.example.app.chat.model.Reaction
import com.example.app.data.ChatRepository
import com.example.app.data.Constants
import com.example.app.data.model.Message
import com.example.app.data.model.MessageEvent
import com.example.app.data.model.ReactionResponse
import com.example.app.utils.DateUtils
import com.example.app.utils.EmojiUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messageList: MutableStateFlow<List<ChatItem>> = MutableStateFlow(emptyList())
    val messageList: StateFlow<List<ChatItem>> = _messageList.asStateFlow()

    private val repository = ChatRepository()

    private var topicName: String = ""
    private var streamName: String = ""

    init {
        viewModelScope.launch {
            repository.handleEvents(
                onNewMessage = ::onNewMessage,
                onUpdateReaction = {
                    if (it.messageId != null && it.emojiCode != null) {
                        updateReactions(
                            msgId = it.messageId,
                            emojiCode = EmojiUtils.unicodeSequenceToString(it.emojiCode)
                        )
                    }
                }
            )
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            try {
                repository.sendMessage(
                    streamName = streamName,
                    topicName = topicName,
                    text = text
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                // TODO: handle error
            }
        }
    }

    fun sendReaction(msgId: Long, emojiCode: String) {
        viewModelScope.launch {
            try {
                // TODO: add emoji name to Reaction
                repository.sendReaction(
                    messageId = msgId,
                    emojiCode = EmojiUtils.stringToUnicodeSequence(emojiCode),
                    emojiName = ""
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Log.e("qwe", null, e)
                // TODO: handle error
            }
        }
    }

    fun loadMessages(streamName: String, topicName: String) {
        this.streamName = streamName
        this.topicName = topicName

        fun List<ReactionResponse>.toReactionItems(): List<Reaction> = filter { it.reactionType == "unicode_emoji" }
            .groupBy { it.emojiCode }
            .map { (unicode, reactions) ->
                Reaction(
                    code = EmojiUtils.unicodeSequenceToString(unicode),
                    counter = reactions.size
                )
            }

        fun List<Message>.toChatItems(): List<ChatItem> = map {
            if (it.senderId == Constants.MY_ID) {
                MessageOutgoing(
                    id = it.msgId,
                    text = it.content,
                    time = it.time,
                    reactions = it.reactions.toReactionItems()
                )
            } else {
                MessageIncoming(
                    id = it.msgId,
                    text = it.content,
                    time = it.time,
                    reactions = it.reactions.toReactionItems(),
                    user = it.senderName
                )
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val items = repository.getMessages(streamName = streamName, topicName = topicName)
                    .groupBy { DateUtils.convertMillisToDateStr(it.time) }
                    .flatMap { (date, messages) ->
                        listOf(DateItem(date)) + messages.toChatItems()
                    }
                _messageList.value = items
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Log.e("qwe", null, e)
                //TODO: handle error
            }
        }
    }

    private fun onNewMessage(event: MessageEvent) {
        if (event.topicName == topicName && event.streamName == streamName) {
            val message = if (event.senderId == Constants.MY_ID) {
                MessageOutgoing(
                    id = event.id,
                    text = event.content,
                    time = System.currentTimeMillis(),
                    reactions = emptyList()
                )
            } else {
                MessageIncoming(
                    id = event.id,
                    text = event.content,
                    time = System.currentTimeMillis(),
                    reactions = emptyList(),
                    user = event.senderFullName
                )
            }
            _messageList.value = _messageList.value + listOf(message)
        }
    }

    private fun updateReactions(msgId: Long, emojiCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newList = _messageList.value.map { item ->
                if (item.id == msgId) {
                    when (item) {
                        is DateItem -> item
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