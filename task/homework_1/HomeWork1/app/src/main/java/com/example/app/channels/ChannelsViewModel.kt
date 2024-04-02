package com.example.app.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.channels.model.ChannelsItem
import com.example.app.chat.FakeData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChannelsViewModel : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: Flow<State> = _state

    init {
        loadData(SelectedTab.SUBSCRIBED)
    }

    fun loadData(tab: SelectedTab) {
        viewModelScope.launch {
            _state.update { State.Loading }
            delay(1000)

            val channels = when (tab) {
                SelectedTab.SUBSCRIBED -> FakeData.channels.take(3)
                SelectedTab.ALL_STREAMS -> FakeData.channels
            }

            _state.update {
                State.Content(
                    items = channels,
                    selectedTab = tab
                )
            }
        }
    }

    fun onSubscribedClick() {
        loadData(SelectedTab.SUBSCRIBED)
    }

    fun onAllStreamsClick() {
        loadData(SelectedTab.ALL_STREAMS)
    }

    fun onSearchClick(text: String) {
        // TODO: implement search
    }

    fun onChannelClick(item: ChannelsItem.Channel) {
        val currentState = _state.value as? State.Content ?: return

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

        _state.update {
            currentState.copy(items = updatedItems - topicsToRemove.toSet())
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