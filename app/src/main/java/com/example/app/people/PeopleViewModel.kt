package com.example.app.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.FakeData
import com.example.app.people.model.People
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PeopleViewModel : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)

    val state: Flow<State> = _state

    init {
        loadPeople()
    }

    fun loadPeople() {
        viewModelScope.launch {
            _state.update { State.Loading }
            delay(1000)
            _state.update { State.Content(FakeData.people) }
        }
    }

    sealed interface State {
        data object Loading : State
        data class Content(val people: List<People>) : State
        data object Error : State
    }

}