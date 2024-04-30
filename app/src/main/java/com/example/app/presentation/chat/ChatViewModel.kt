package com.example.app.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.Constants
import com.example.app.data.network.model.MessageEvent
import com.example.app.domain.repo.ChatRepository
import com.example.app.presentation.chat.mapper.toChatItems
import com.example.app.presentation.chat.model.ChatItem
import com.example.app.presentation.chat.model.DateItem
import com.example.app.presentation.chat.model.MessageIncoming
import com.example.app.presentation.chat.model.MessageOutgoing
import com.example.app.presentation.chat.model.Reaction
import com.example.app.utils.DateUtils
import com.example.app.utils.EmojiUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(private val repo: ChatRepository) : ViewModel() {

    private val _messageList: MutableStateFlow<List<ChatItem>> = MutableStateFlow(emptyList())
    val messageList: StateFlow<List<ChatItem>> = _messageList.asStateFlow()

    private var topicName: String = ""
    private var streamName: String = ""

    init {
        viewModelScope.launch {
            repo.handleEvents(
                onNewMessage = {
                    sendAction(
                        Action.OnNewMessage(it)
                    )
                },
                onUpdateReaction = {
                    if (it.messageId != null && it.emojiCode != null) {
                        sendAction(
                            Action.OnNewReaction(msgId = it.messageId, emojiCode = it.emojiCode)
                        )
                    }
                }
            )
        }
    }

    fun sendAction(action: Action) {
        when (action) {
            is Action.LoadData -> loadMessages(
                streamName = action.streamName,
                topicName = action.topicName
            )

            is Action.SendMessage -> sendMessage(action.text)
            is Action.SendReaction -> sendReaction(action.msgId, action.emojiCode)
            is Action.OnNewMessage -> updateMessages(action.event)
            is Action.OnNewReaction -> updateReactions(action.msgId, action.emojiCode)
        }
    }

    private fun sendMessage(text: String) {
        viewModelScope.launch {
            try {
                repo.sendMessage(
                    streamName = streamName,
                    topicName = topicName,
                    text = text
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                State.Error
            }
        }
    }

    private fun sendReaction(msgId: Long, emojiCode: String) {
        viewModelScope.launch {
            try {
                // TODO: add emoji name to Reaction
                repo.sendReaction(
                    messageId = msgId,
                    emojiCode = EmojiUtils.stringToUnicodeSequence(emojiCode),
                    emojiName = ""
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                State.Error
            }
        }
    }

    private fun loadMessages(streamName: String, topicName: String) {
        this.streamName = streamName
        this.topicName = topicName

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val items = repo.getMessages(streamName = streamName, topicName = topicName)
                    .groupBy { DateUtils.convertMillisToDateStr(it.time) }
                    .flatMap { (date, messages) ->
                        listOf(DateItem(date)) + messages.toChatItems()
                    }
                _messageList.value = items
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                State.Error
            }
        }
    }

    private fun updateMessages(event: MessageEvent) {
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
            _messageList.value += listOf(message)
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

    sealed interface State {
        data object Loading : State

        data class Content(val items: List<ChatItem>) : State

        data object Error : State
    }

    sealed interface Action {
        data class LoadData(val streamName: String, val topicName: String) : Action
        data class SendMessage(val text: String) : Action
        data class SendReaction(val msgId: Long, val emojiCode: String) : Action
        data class OnNewMessage(val event: MessageEvent) : Action
        data class OnNewReaction(val msgId: Long, val emojiCode: String) : Action
    }

}