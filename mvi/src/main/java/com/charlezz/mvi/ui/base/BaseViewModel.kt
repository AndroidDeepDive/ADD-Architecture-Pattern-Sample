package com.charlezz.mvi.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<A : Action, S : State>(
    state: S
) : ViewModel() {

    private val _uiStateFlow: MutableStateFlow<S> = MutableStateFlow(state)

    private val currentUiState: S
        get() = _uiStateFlow.value

    val uiStateFlow: Flow<S> = _uiStateFlow.asStateFlow()

    private val _actionFlow : MutableSharedFlow<A> = MutableSharedFlow()
    val actionFlow = _actionFlow.asSharedFlow()

    init { subscribeAction() }

    fun setAction(action: A) = viewModelScope.launch {
        _actionFlow.emit(action)
    }

    private fun subscribeAction() = viewModelScope.launch {
        _actionFlow.collect { handleAction(it) }
    }

    abstract fun handleAction(action: A)

    protected fun setState(reduce: S.() -> S) = viewModelScope.launch {
        val newState = currentUiState.reduce()
        _uiStateFlow.value = newState
    }

    protected fun withState(state: (S) -> Unit) {
        return state(currentUiState)
    }

}
