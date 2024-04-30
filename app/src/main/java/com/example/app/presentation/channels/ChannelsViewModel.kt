package com.example.app.presentation.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.repo.ChannelsRepository
import com.example.app.presentation.channels.model.ChannelsItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelsViewModel @Inject constructor(private val repo: ChannelsRepository) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: Flow<State> = _state

    init {
        sendAction(
            Action.LoadData(SelectedTab.SUBSCRIBED)
        )
    }

    fun sendAction(action: Action) {
        when (action) {
            is Action.LoadData -> loadData(action.tab)
            is Action.onSubscribedClick -> loadData(SelectedTab.SUBSCRIBED)
            is Action.onAllStreamsClick -> loadData(SelectedTab.ALL_STREAMS)
            is Action.OnSearchClick -> emitSearch(action.text)
            is Action.OnChannelClick -> toggleChannel(action.item)
        }
    }

    private fun loadData(tab: SelectedTab) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { State.Loading }
            try {
                val response = when (tab) {
                    SelectedTab.SUBSCRIBED -> repo.getStreamsSubscriptions()
                    SelectedTab.ALL_STREAMS -> repo.getAllStreams()
                }

                val streams = mutableListOf<ChannelsItem.Stream>()

                response.map {
                    launch {
                        val topics = loadTopics(it.streamId, it.name)
                        streams.add(
                            ChannelsItem.Stream(
                                id = it.streamId,
                                text = it.name,
                                isExpanded = false,
                                topics = topics
                            )
                        )
                    }
                }.joinAll()

                _state.update {
                    State.Content(
                        items = streams,
                        visibleItems = streams,
                        selectedTab = tab
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                State.Error
            }
        }
    }

    private val searchFlow = MutableSharedFlow<String>(extraBufferCapacity = 100)

    init {
        searchFlow
            .distinctUntilChanged()
            .debounce(500)
            .mapLatest { search(it) }
            .flowOn(Dispatchers.IO)
            .onEach { _state.value = it }
            .launchIn(viewModelScope)
    }

    private fun emitSearch(text: String) {
        viewModelScope.launch {
            searchFlow.emit(text)
        }
    }

    private suspend fun search(query: String): State {
        return try {
            val currentState = _state.value as? State.Content ?: return _state.value
            _state.value = State.Loading

            val newItems = if (query.isBlank()) {
                currentState.items
            } else {
                currentState.items.filter { channel ->
                    channel.text.contains(query, ignoreCase = true)
                }
            }

            currentState.copy(visibleItems = newItems)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            State.Error
        }
    }

    private fun toggleChannel(item: ChannelsItem.Stream) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _state.value as? State.Content ?: return@launch

            var topicsToRemove: List<ChannelsItem.Topic> = emptyList()

            val updatedItems = currentState.visibleItems.flatMap { currentItem ->
                if (currentItem.id == item.id && currentItem is ChannelsItem.Stream) {
                    if (currentItem.isExpanded) {
                        topicsToRemove = currentItem.topics
                        listOf(currentItem.copy(isExpanded = false))
                    } else {
                        listOf(currentItem.copy(isExpanded = true)) + currentItem.topics
                    }
                } else {
                    listOf(currentItem)
                }
            }

            _state.value = currentState.copy(visibleItems = updatedItems - topicsToRemove.toSet())
        }
    }

    private suspend fun loadTopics(streamId: Int, streamName: String): List<ChannelsItem.Topic> {
        val response = try {
            repo.getTopics(streamId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            emptyList()
        }
        return response.map {
            ChannelsItem.Topic(
                id = it.maxId,
                text = it.name,
                streamName = streamName,
                backgroundColorRes = 0
            )
        }
    }

    sealed interface State {
        data object Loading : State

        data class Content(
            val visibleItems: List<ChannelsItem>,
            val items: List<ChannelsItem>,
            val selectedTab: SelectedTab
        ) : State

        data object Error : State
    }

    sealed interface Action {
        data class LoadData(val tab: SelectedTab) : Action
        data object onSubscribedClick : Action
        data object onAllStreamsClick : Action
        data class OnSearchClick(val text: String) : Action
        data class OnChannelClick(val item: ChannelsItem.Stream) : Action
    }

    enum class SelectedTab {
        SUBSCRIBED,
        ALL_STREAMS
    }

}