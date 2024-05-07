package com.example.app.presentation.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.model.StreamModel
import com.example.app.domain.repo.ChannelsRepository
import com.example.app.presentation.channels.mapper.toUi
import com.example.app.presentation.channels.model.ChannelsItemUi
import com.example.app.utils.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ChannelsViewModel @Inject constructor(
    private val repo: ChannelsRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State.Loading(SelectedTab.SUBSCRIBED)
    )
    val state: Flow<State> = _state

    init {
        _state.flatMapLatest {
            when (it.selectedTab) {
                SelectedTab.SUBSCRIBED -> repo.getAllStreamsWithTopicsFlow(onlySubscribed = true)
                SelectedTab.ALL_STREAMS -> repo.getAllStreamsWithTopicsFlow(onlySubscribed = false)
            }
        }.onEach { streams ->
            sendAction(
                Action.UpdateStreams(streams)
            )
        }.launchIn(viewModelScope)

        sendAction(
            Action.LoadData(SelectedTab.SUBSCRIBED)
        )
    }

    fun sendAction(action: Action) {
        when (action) {
            is Action.LoadData -> loadData(action.tab)
            is Action.OnSubscribedClick -> loadData(SelectedTab.SUBSCRIBED)
            is Action.OnAllStreamsClick -> loadData(SelectedTab.ALL_STREAMS)
            is Action.UpdateStreams -> updateStreams(action.streams)
            is Action.OnSearchClick -> emitSearch(action.text)
            is Action.OnChannelClick -> toggleChannel(action.item)
        }
    }

    private fun loadData(tab: SelectedTab) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { State.Loading(tab) }
            runSuspendCatching(
                action = {
                    when (tab) {
                        SelectedTab.SUBSCRIBED -> repo.fetchStreamsWithTopics(onlySubscribed = true)
                        SelectedTab.ALL_STREAMS -> repo.fetchStreamsWithTopics(onlySubscribed = false)
                    }
                },
                onSuccess = {},
                onError = { _state.value = State.Error(tab) }
            )
        }
    }

    private fun updateStreams(streams: List<StreamModel>) {
        val items = streams.toUi()
        _state.update {
            State.Content(
                visibleItems = items,
                items = items,
                selectedTab = it.selectedTab
            )
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

    private fun search(query: String): State {
        val currentState = _state.value as? State.Content ?: return _state.value
        _state.update { State.Loading(it.selectedTab) }

        val newItems = if (query.isBlank()) {
            currentState.items
        } else {
            currentState.items.filter { channel ->
                channel.text.contains(query, ignoreCase = true)
            }
        }

        return currentState.copy(visibleItems = newItems)
    }

    private fun toggleChannel(item: ChannelsItemUi.StreamUi) {
        viewModelScope.launch {
            repo.updateStream(
                streamId = item.id,
                isExpanded = !item.isExpanded
            )
        }
    }

    sealed interface State {
        val selectedTab: SelectedTab

        data class Loading(
            override val selectedTab: SelectedTab
        ) : State

        data class Content(
            val visibleItems: List<ChannelsItemUi>,
            val items: List<ChannelsItemUi>,
            override val selectedTab: SelectedTab
        ) : State

        data class Error(
            override val selectedTab: SelectedTab
        ) : State
    }

    sealed interface Action {
        data class LoadData(val tab: SelectedTab) : Action
        data class UpdateStreams(val streams: List<StreamModel>) : Action
        data object OnSubscribedClick : Action
        data object OnAllStreamsClick : Action
        data class OnSearchClick(val text: String) : Action
        data class OnChannelClick(val item: ChannelsItemUi.StreamUi) : Action
    }

    enum class SelectedTab {
        SUBSCRIBED,
        ALL_STREAMS
    }

}