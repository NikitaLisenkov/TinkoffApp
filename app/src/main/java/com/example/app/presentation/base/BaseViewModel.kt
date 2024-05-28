package com.example.app.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE, ACTION>(
    initState: STATE
) : ViewModel() {

    private val _state: MutableStateFlow<STATE> = MutableStateFlow(initState)
    val state: Flow<STATE> = _state

    protected abstract val reducer: BaseReducer<STATE, ACTION>

    protected val currentState: STATE
        get() = _state.value

    fun sendAction(action: ACTION) {
        viewModelScope.launch {
            reducer.reduce(currentState, action)?.let {
                _state.value = it
            }
        }
    }

}