package com.example.app.presentation.people

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.app.R
import com.example.app.databinding.FragmentPeopleBinding
import com.example.app.presentation.people.PeopleViewModel.Action
import com.example.app.presentation.people.adapter.PeopleAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PeopleFragment : Fragment(R.layout.fragment_people) {

    private val binding: FragmentPeopleBinding by viewBinding(FragmentPeopleBinding::bind)

    private val viewModel: PeopleViewModel by viewModels()

    private val adapter = PeopleAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPeopleList.adapter = adapter
        binding.layoutError.btnRetry.setOnClickListener {
            viewModel.sendAction(Action.LoadData)
        }

        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { render(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun render(state: PeopleViewModel.State) {
        with(binding) {
            when (state) {
                is PeopleViewModel.State.Loading -> {
                    progress.isVisible = true
                    rvPeopleList.isGone = true
                    layoutError.root.isGone = true
                }

                is PeopleViewModel.State.Content -> {
                    adapter.submitList(state.people)
                    rvPeopleList.isVisible = true
                    progress.isGone = true
                    layoutError.root.isGone = true
                }

                is PeopleViewModel.State.Error -> {
                    layoutError.root.isVisible = true
                    rvPeopleList.isGone = true
                    progress.isGone = true
                }
            }
        }
    }
}