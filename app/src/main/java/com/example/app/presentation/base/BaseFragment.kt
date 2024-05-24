package com.example.app.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

abstract class BaseFragment<
        STATE,
        ACTION,
        VM : BaseViewModel<STATE, ACTION>>(@LayoutRes layoutId: Int) :
    Fragment(layoutId) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    protected abstract val viewModel: VM

    protected abstract fun inject()

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { render(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    protected abstract fun render(state: STATE)

}