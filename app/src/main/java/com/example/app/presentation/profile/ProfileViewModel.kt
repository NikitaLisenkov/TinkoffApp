package com.example.app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.repo.ProfileRepository
import com.example.app.utils.runSuspendCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: Flow<State> = _state

    init {
        sendAction(Action.LoadData)
    }

    fun sendAction(action: Action) {
        when (action) {
            Action.LoadData -> loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { State.Loading }
            runSuspendCatching(
                action = { repo.getOwnUserProfile() },
                onSuccess = { response ->
                    _state.update {
                        State.Content(
                            avatarUrl = response.avatarUrl,
                            name = response.fullName,
                            isOnline = response.isActive
                        )
                    }
                },
                onError = {
                    _state.update {
                        State.Error
                    }
                }
            )
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

    sealed interface Action {
        data object LoadData : Action
    }
}