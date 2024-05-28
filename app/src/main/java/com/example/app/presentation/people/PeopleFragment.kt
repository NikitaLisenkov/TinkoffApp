package com.example.app.presentation.people

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.app.R
import com.example.app.databinding.FragmentPeopleBinding
import com.example.app.presentation.base.BaseFragment
import com.example.app.presentation.people.PeopleViewModel.Action
import com.example.app.presentation.people.PeopleViewModel.State
import com.example.app.presentation.people.adapter.PeopleAdapter
import com.example.app.presentation.people.model.PeopleUi
import com.example.app.presentation.profile.user_details.UserDetailsInfoFragment
import com.example.app.utils.getApp

class PeopleFragment : BaseFragment<State, Action, PeopleViewModel>(R.layout.fragment_people) {

    override val viewModel: PeopleViewModel by viewModels { factory }

    private val adapter = PeopleAdapter(
        onUserClick = { openUserInfo(it) }
    )

    private val binding: FragmentPeopleBinding by viewBinding(FragmentPeopleBinding::bind)

    override fun inject() {
        requireContext().getApp().createPeopleComponent()?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPeopleList.adapter = adapter

        binding.layoutError.btnRetry.setOnClickListener {
            viewModel.sendAction(Action.LoadData)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.sendAction(Action.LoadData)
            binding.swipeRefresh.postDelayed(
                { binding.swipeRefresh.isRefreshing = false },
                1000
            )
        }

        binding.usersSearch.etUsersSearchField.addTextChangedListener {
            viewModel.sendAction(
                Action.OnSearchText(
                    it?.toString().orEmpty()
                )
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireContext().getApp().clearPeopleComponent()
    }

    private fun openUserInfo(user: PeopleUi) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container_view,
                UserDetailsInfoFragment.newInstance(
                    userAvatarUrl = user.avatarUrl,
                    userFullName = user.fullName,
                    userIsOnline = user.isOnline
                ),
                UserDetailsInfoFragment.TAG
            )
            .addToBackStack(null)
            .commit()
    }

    override fun render(state: State) {
        with(binding) {
            when (state) {
                is State.Loading -> {
                    swipeRefresh.isRefreshing = true
                    rvPeopleList.isGone = true
                    layoutError.root.isGone = true
                }

                is State.Content -> {
                    adapter.submitList(state.people)
                    rvPeopleList.isVisible = true
                    swipeRefresh.isRefreshing = false
                    layoutError.root.isGone = true
                }

                is State.Error -> {
                    layoutError.root.isVisible = true
                    rvPeopleList.isGone = true
                    swipeRefresh.isRefreshing = false
                }
            }
        }
    }
}