package com.example.app.profile

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.app.R
import com.example.app.databinding.FragmentProfileBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { render(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun render(state: ProfileViewModel.State) {
        with(binding) {
            when (state) {
                is ProfileViewModel.State.Loading -> {
                    progress.isVisible = true
                    layoutProfile.root.isGone = true
                }

                is ProfileViewModel.State.Content -> {
                    progress.isGone = true
                    layoutProfile.root.isVisible = true
                    layoutProfile.tvFullUserName.text = state.name

                    if (state.isOnline) {
                        layoutProfile.tvUserStatus.text = getString(R.string.online)
                        layoutProfile.tvUserStatus.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.color_online)
                        )
                    } else {
                        layoutProfile.tvUserStatus.text = getString(R.string.offline)
                        layoutProfile.tvUserStatus.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.orange)
                        )
                    }

                    Glide.with(root)
                        .load(state.avatarUrl)
                        .into(binding.layoutProfile.ivAvatar)
                }

                is ProfileViewModel.State.Error -> {
                    // TODO: add error state
                }
            }
        }
    }
}