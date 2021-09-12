package com.charlezz.mvi.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect

abstract class BaseActivity<A: Action, S: State, VM: BaseViewModel<A, S>>: AppCompatActivity() {

    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeState()
    }

    private fun observeState() = lifecycleScope.launchWhenStarted {
        viewModel.actionAndStateFlow.collect { (_, state) ->
            render(state)
        }
    }

    abstract fun render(state: S)

}
