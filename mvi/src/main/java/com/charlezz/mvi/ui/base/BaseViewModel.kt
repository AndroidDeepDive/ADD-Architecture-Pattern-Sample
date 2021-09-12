package com.charlezz.mvi.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseViewModel<A : Action, S : State>(
    state: S
) : ViewModel() {

    private val _actionAndStateFlow: MutableStateFlow<Pair<A?, S>> = MutableStateFlow(Pair(null, state))

    val actionAndStateFlow: Flow<Pair<A?, S>> = _actionAndStateFlow

    abstract fun handleAction(action: A)

    fun setState(action: A, state: S.() -> S) = viewModelScope.launch {
        val (currentAction, currentState) = _actionAndStateFlow.value
        val applyAction = if (currentAction == action) {
            currentAction
        } else {
            action
        }
        val applyState = if (currentState == state) {
            currentState
        } else {
            state(currentState)
        }
        _actionAndStateFlow.emit(
            Pair(applyAction, applyState)
        )
    }

}
