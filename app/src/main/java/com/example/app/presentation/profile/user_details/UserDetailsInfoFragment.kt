package com.example.app.presentation.profile.user_details

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.app.R
import com.example.app.databinding.FragmentUserDetailsInfoBinding

class UserDetailsInfoFragment : Fragment(R.layout.fragment_user_details_info) {

    private val binding by viewBinding(FragmentUserDetailsInfoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userAvatarUrl = requireArguments().getString(ARG_USER_AVATAR_URL)
        val userFullName = requireArguments().getString(ARG_USER_FULL_NAME)
        val userIsOnline = requireArguments().getBoolean(ARG_USER_IS_ONLINE)

        binding.toolbarUserProfile.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.tvFullUserName.text = userFullName
        if (userIsOnline) {
            binding.tvUserStatus.text = getString(R.string.online)
            binding.tvUserStatus.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_online
                )
            )
        } else {
            binding.tvUserStatus.text = getString(R.string.offline)
            binding.tvUserStatus.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_offline
                )
            )
        }

        Glide.with(this)
            .load(userAvatarUrl)
            .into(binding.ivAvatar)
    }

    companion object {
        const val TAG: String = "USER_DETAILS_INFO_FRAGMENT"

        private const val ARG_USER_AVATAR_URL = "ARG_USER_AVATAR_URL"
        private const val ARG_USER_FULL_NAME = "ARG_USER_FULL_NAME"
        private const val ARG_USER_IS_ONLINE = "ARG_USER_IS_ONLINE"

        fun newInstance(
            userAvatarUrl: String?,
            userFullName: String,
            userIsOnline: Boolean
        ): UserDetailsInfoFragment = UserDetailsInfoFragment().apply {
            arguments = bundleOf(
                ARG_USER_AVATAR_URL to userAvatarUrl,
                ARG_USER_FULL_NAME to userFullName,
                ARG_USER_IS_ONLINE to userIsOnline
            )
        }
    }
}
