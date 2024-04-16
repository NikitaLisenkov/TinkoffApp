package com.example.app.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.di.GlobalDI
import com.example.app.people.model.People
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PeopleViewModel : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)

    val state: Flow<State> = _state

    private val api = GlobalDI.zulipApi

    init {
        loadPeople()
    }

    fun loadPeople() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { State.Loading }
            try {
                val response = api.getAllUsers()
                val users = response.members.map {
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

}