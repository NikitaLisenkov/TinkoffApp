package com.example.app.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.app.R
import com.example.app.databinding.FragmentProfileBinding
import com.example.app.presentation.base.BaseFragment
import com.example.app.utils.getApp

class ProfileFragment : BaseFragment<
        ProfileViewModel.State,
        ProfileViewModel.Action,
        ProfileViewModel>(R.layout.fragment_profile) {

    override val viewModel: ProfileViewModel by viewModels { factory }

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    override fun inject() {
        requireContext().getApp().createProfileComponent()?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutError.btnRetry.setOnClickListener {
            viewModel.sendAction(ProfileViewModel.Action.LoadData)
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireContext().getApp().clearProfileComponent()
    }

    override fun render(state: ProfileViewModel.State) {
        with(binding) {
            when (state) {
                is ProfileViewModel.State.Loading -> {
                    profileShimmer.root.isVisible = true
                    layoutError.root.isGone = true
                    layoutProfile.root.isGone = true
                }

                is ProfileViewModel.State.Content -> {
                    profileShimmer.root.isGone = true
                    layoutProfile.root.isVisible = true
                    layoutError.root.isGone = true
                    layoutProfile.tvFullUserName.text = state.name

                    if (state.isOnline) {
                        layoutProfile.tvUserStatus.text = getString(com.example.app.R.string.online)
                        layoutProfile.tvUserStatus.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green_light
                            )
                        )
                    } else {
                        layoutProfile.tvUserStatus.text =
                            getString(R.string.offline)
                        layoutProfile.tvUserStatus.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.orange
                            )
                        )
                    }

                    Glide.with(root)
                        .load(state.avatarUrl)
                        .into(layoutProfile.ivAvatar)
                }

                is ProfileViewModel.State.Error -> {
                    profileShimmer.root.isGone = true
                    layoutProfile.root.isGone = true
                    layoutError.root.isVisible = true
                }
            }
        }
    }
}