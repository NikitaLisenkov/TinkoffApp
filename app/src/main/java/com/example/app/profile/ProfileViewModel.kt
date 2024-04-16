package com.example.app.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.di.GlobalDI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class ProfileViewModel : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: Flow<State> = _state

    private val api = GlobalDI.zulipApi

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { State.Loading }
            try {
                val response = api.getOwnUserProfile()
                _state.update {
                    State.Content(
                        avatarUrl = response.avatarUrl,
                        name = response.fullName,
                        isOnline = response.isActive
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                State.Error
            }
        }
    }

    sealed interface State {
        data object Loading : State
        data class Content(
            val avatarUrl: String,
            val name: String,
            val isOnline: Boolean
        ) : State

        data object Error : State
    }
}