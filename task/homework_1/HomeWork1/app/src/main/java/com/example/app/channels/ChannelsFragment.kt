package com.example.app.channels

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
import com.example.app.channels.ChannelsViewModel.SelectedTab
import com.example.app.channels.adapter.ChannelsAdapter
import com.example.app.channels.model.ChannelsItem
import com.example.app.chat.ChatFragment
import com.example.app.databinding.FragmentChannelsBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ChannelsFragment : Fragment(R.layout.fragment_channels) {

    private val binding by viewBinding(FragmentChannelsBinding::bind)

    private val viewModel: ChannelsViewModel by viewModels()

    private val adapter = ChannelsAdapter(
        onChannelClick = { viewModel.onChannelClick(it) },
        onTopicClick = { openChat(it) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubscribed.tvText.text = "Subscribed"
        binding.btnAllStreams.tvText.text = "All streams"

        binding.ivSearch.setOnClickListener {
            viewModel.onSearchClick(binding.etSearch.text.toString())
        }

        binding.btnSubscribed.root.setOnClickListener {
            viewModel.onSubscribedClick()
        }

        binding.btnAllStreams.root.setOnClickListener {
            viewModel.onAllStreamsClick()
        }

        binding.layoutError.btnRetry.setOnClickListener {
            viewModel.loadData(SelectedTab.SUBSCRIBED)
        }

        binding.rvChannels.adapter = adapter

        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { render(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun render(state: ChannelsViewModel.State) {
        with(binding) {
            when (state) {
                is ChannelsViewModel.State.Loading -> {
                    progress.isVisible = true
                    rvChannels.isGone = true
                    layoutError.root.isGone = true
                }

                is ChannelsViewModel.State.Content -> {
                    progress.isGone = true
                    rvChannels.isVisible = true
                    layoutError.root.isGone = true

                    when (state.selectedTab) {
                        SelectedTab.SUBSCRIBED -> {
                            btnSubscribed.divider.isVisible = true
                            btnAllStreams.divider.isGone = true
                        }

                        SelectedTab.ALL_STREAMS -> {
                            btnSubscribed.divider.isGone = true
                            btnAllStreams.divider.isVisible = true
                        }
                    }

                    adapter.channelsItems = state.items
                }

                is ChannelsViewModel.State.Error -> {
                    progress.isGone = true
                    rvChannels.isGone = true
                    layoutError.root.isVisible = true
                }
            }
        }
    }

    private fun openChat(item: ChannelsItem.Topic) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainerView,
                ChatFragment.newInstance(
                    channelName = item.channelName,
                    topicName = item.text
                )
            )
            .addToBackStack(null)
            .commit()
    }

}