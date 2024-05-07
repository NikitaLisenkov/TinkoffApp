package com.example.app.presentation.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.model.UserModel
import com.example.app.domain.repo.PeopleRepository
import com.example.app.presentation.people.model.PeopleUi
import com.example.app.utils.runSuspendCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class PeopleViewModel @Inject constructor(
    private val repo: PeopleRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)

    val state: Flow<State> = _state

    init {
        repo.getAllUsersFlow()
            .onEach { sendAction(Action.UpdateUsers(it)) }
            .launchIn(viewModelScope)

        sendAction(Action.LoadData)
    }

    fun sendAction(action: Action) {
        when (action) {
            is Action.LoadData -> loadPeople()
            is Action.UpdateUsers -> updateUsers(action.users)
        }
    }

    private fun updateUsers(users: List<UserModel>) {
        val people = users.map {
            PeopleUi(
                id = it.userId,
                avatarUrl = it.avatarUrl,
                fullName = it.fullName,
                email = it.email,
                isOnline = it.isActive
            )
        }
        _state.update { State.Content(people) }
    }

    private fun loadPeople() {
        viewModelScope.launch {
            _state.update { State.Loading }
            runSuspendCatching(
                action = { repo.fetchAllUsers() },
                onSuccess = {},
                onError = { _state.value = State.Error }
            )
        }
    }

    sealed interface State {
        data object Loading : State
        data class Content(val people: List<PeopleUi>) : State
        data object Error : State
    }

    sealed interface Action {
        data object LoadData : Action
        data class UpdateUsers(val users: List<UserModel>) : Action
    }

}