package com.example.app.presentation.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.di.ServiceLocator
import com.example.app.presentation.people.model.People
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PeopleViewModel : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)

    val state: Flow<State> = _state

    private val repo = ServiceLocator.peopleRepo

    init {
        sendAction(Action.LoadData)
    }

    fun sendAction(action: Action) {
        when (action) {
            Action.LoadData -> loadPeople()
        }
    }

    private fun loadPeople() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { State.Loading }
            try {
                val response = repo.getAllUsers()
                val users = response.map {
                    People(
                        id = it.userId,
                        avatarUrl = it.avatarUrl,
                        fullName = it.fullName,
                        email = it.email,
                        isOnline = it.isActive
                    )
                }
                _state.update { State.Content(users) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                State.Error
            }
        }
    }

    sealed interface State {
        data object Loading : State
        data class Content(val people: List<People>) : State
        data object Error : State
    }

    sealed interface Action {
        data object LoadData : Action
    }

}