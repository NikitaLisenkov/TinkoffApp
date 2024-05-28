package com.example.app.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.Constants
import com.example.app.domain.repo.ChatRepository
import com.example.app.presentation.chat.mapper.toChatItems
import com.example.app.presentation.chat.model.ChatItemUi
import com.example.app.presentation.chat.model.MessageIncomingUi
import com.example.app.presentation.chat.model.MessageOutgoingUi
import com.example.app.utils.runSuspendCatching
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _chatItemsFlow: MutableStateFlow<List<ChatItemUi>> = MutableStateFlow(emptyList())
    val chatItemsFlow: StateFlow<List<ChatItemUi>> = _chatItemsFlow.asStateFlow()

    private var topicNameFlow: MutableStateFlow<String> = MutableStateFlow("")
    private var streamNameFlow: MutableStateFlow<String> = MutableStateFlow("")

    private var isLastPage: Boolean = false

    private var pagingJob: Job? = null

    init {
        combine(
            streamNameFlow,
            topicNameFlow,
        ) { stream, topic -> chatRepository.getMessagesFlow(streamName = stream, topicName = topic) }
            .flatMapLatest { it }
            .map { it.toChatItems(Constants.MY_ID) }
            .onEach { _chatItemsFlow.value = it }
            .launchIn(viewModelScope)

        var job: Job? = null

        combine(
            streamNameFlow,
            topicNameFlow,
        ) { stream, topic ->
            job?.cancel()
            job = viewModelScope.launch {
                chatRepository.handleEvents(
                    streamName = stream,
                    topicName = topic
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        runBlocking {
            chatRepository.clearStorage(
                streamName = streamNameFlow.value,
                topicName = topicNameFlow.value
            )
        }
        super.onCleared()
    }

    fun sendAction(action: Action) {
        when (action) {
            is Action.LoadData -> loadMessages(streamName = action.streamName, topicName = action.topicName)
            is Action.LoadNextPage -> loadNextPage()
            is Action.SendMessage -> sendMessage(action.text)
            is Action.SendReaction -> sendReaction(msgId = action.msgId, emojiName = action.emojiName)
        }
    }

    private fun sendMessage(text: String) {
        viewModelScope.launch {
            runSuspendCatching(
                action = {
                    chatRepository.sendMessage(
                        streamName = streamNameFlow.value,
                        topicName = topicNameFlow.value,
                        text = text
                    )
                },
                onSuccess = {},
                onError = {}
            )
        }
    }

    private fun sendReaction(msgId: Long, emojiName: String) {
        viewModelScope.launch {
            runSuspendCatching(
                action = {
                    chatRepository.sendReaction(
                        messageId = msgId,
                        emojiName = emojiName
                    )
                },
                onSuccess = {},
                onError = {}
            )
        }
    }

    private fun loadMessages(streamName: String, topicName: String) {
        this.streamNameFlow.value = streamName
        this.topicNameFlow.value = topicName

        viewModelScope.launch {
            runSuspendCatching(
                action = {
                    chatRepository.fetchMessages(
                        streamName = streamName,
                        topicName = topicName
                    )
                },
                onSuccess = {},
                onError = {}
            )
        }
    }

    private fun loadNextPage() {
        if (pagingJob?.isActive == true || isLastPage) return
        pagingJob = viewModelScope.launch {
            runSuspendCatching(
                action = {
                    val messageId = chatItemsFlow.value.firstOrNull { it is MessageIncomingUi || it is MessageOutgoingUi }?.id
                    isLastPage = chatRepository.loadNextPage(
                        firstMessageId = messageId ?: return@runSuspendCatching,
                        streamName = streamNameFlow.value,
                        topicName = topicNameFlow.value
                    )
                },
                onSuccess = {},
                onError = {}
            )
        }
    }

    sealed interface State {
        data object Loading : State

        data class Content(val items: List<ChatItemUi>) : State

        data object Error : State
    }

    sealed interface Action {
        data class LoadData(val streamName: String, val topicName: String) : Action
        data object LoadNextPage : Action
        data class SendMessage(val text: String) : Action
        data class SendReaction(val msgId: Long, val emojiName: String) : Action
    }

}