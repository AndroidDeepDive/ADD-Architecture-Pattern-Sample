package com.charlezz.mvi.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseActivity<A : Action, S : State, VM : BaseViewModel<A, S>> : AppCompatActivity() {

    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeState()
    }

    private fun observeState() =
        viewModel.uiStateFlow.onEach { state ->
            render(state)
        }.launchIn(lifecycleScope)

    abstract fun render(state: S)

}
