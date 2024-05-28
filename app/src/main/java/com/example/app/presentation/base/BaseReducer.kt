package com.example.app.presentation.base

fun interface BaseReducer<STATE, ACTION> {
    suspend fun reduce(currentState: STATE, action: ACTION): STATE?
}