package com.example.app.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.FakeData
import com.example.app.channels.model.ChannelsItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.random.Random

class ChannelsViewModel : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: Flow<State> = _state

    init {
        loadData(SelectedTab.SUBSCRIBED)
    }

    fun loadData(tab: SelectedTab) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = State.Loading
            delay(1000)

            val channels = when (tab) {
                SelectedTab.SUBSCRIBED -> FakeData.channels.take(3)
                SelectedTab.ALL_STREAMS -> FakeData.channels
            }

            _state.value = State.Content(
                items = channels,
                selectedTab = tab
            )
        }
    }

    fun onSubscribedClick() {
        loadData(SelectedTab.SUBSCRIBED)
    }

    fun onAllStreamsClick() {
        loadData(SelectedTab.ALL_STREAMS)
    }


    private val searchFlow = MutableSharedFlow<String>(extraBufferCapacity = 100)

    init {
        searchFlow
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .debounce(500)
            .mapLatest {
                _state.value = State.Loading
                search(it)
            }
            .flowOn(Dispatchers.IO)
            .onEach { _state.value = it }
            .launchIn(viewModelScope)
    }

    fun onSearchClick(text: String) {
        viewModelScope.launch {
            searchFlow.emit(text)
        }
    }

    private suspend fun search(query: String): State {
        return try {
            delay(1000)

            if (Random.nextBoolean()) {
                throw Throwable("Random error")
            }

            val result = FakeData.channels.filter { channel ->
                channel.text.contains(query, ignoreCase = true)
            }
            val selectedTab = (_state.value as? State.Content)?.selectedTab ?: SelectedTab.SUBSCRIBED
            State.Content(items = result, selectedTab = selectedTab)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            State.Error
        }
    }

    fun onChannelClick(item: ChannelsItem.Channel) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _state.value as? State.Content ?: return@launch

            var topicsToRemove: List<ChannelsItem.Topic> = emptyList()

            val updatedItems = currentState.items.flatMap { currentItem ->
                if (currentItem.id == item.id && currentItem is ChannelsItem.Channel) {
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

            _state.value = currentState.copy(items = updatedItems - topicsToRemove.toSet())
        }
    }

    sealed interface State {
        data object Loading : State

        data class Content(
            val items: List<ChannelsItem>,
            val selectedTab: SelectedTab
        ) : State

        data object Error : State
    }

    enum class SelectedTab {
        SUBSCRIBED,
        ALL_STREAMS
    }

}