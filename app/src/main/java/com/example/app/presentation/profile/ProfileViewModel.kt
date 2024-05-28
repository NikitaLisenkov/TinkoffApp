package com.example.app.presentation.profile

import com.example.app.presentation.base.BaseViewModel
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    override val reducer: ProfileReducer
) : BaseViewModel<ProfileViewModel.State, ProfileViewModel.Action>(
    initState = State.Loading
) {

    init {
        sendAction(Action.LoadData)
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