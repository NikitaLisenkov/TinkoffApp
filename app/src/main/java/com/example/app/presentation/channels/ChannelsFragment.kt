package com.example.app.presentation.channels

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.app.R
import com.example.app.databinding.FragmentChannelsBinding
import com.example.app.presentation.channels.ChannelsViewModel.Action
import com.example.app.presentation.channels.ChannelsViewModel.SelectedTab
import com.example.app.presentation.channels.adapter.ChannelsAdapter
import com.example.app.presentation.channels.model.ChannelsItemUi
import com.example.app.presentation.chat.ChatFragment
import com.example.app.utils.getApp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ChannelsFragment : Fragment(R.layout.fragment_channels) {

    private val binding by viewBinding(FragmentChannelsBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: ChannelsViewModel by viewModels { factory }

    private val adapter = ChannelsAdapter(
        onChannelClick = {
            viewModel.sendAction(
                Action.OnChannelClick(it)
            )
        },
        onTopicClick = {
            openChat(it)
        }
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.getApp().createChannelsComponent()?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubscribed.tvText.text = "Subscribed"
        binding.btnAllStreams.tvText.text = "All streams"

        binding.etSearch.addTextChangedListener { text ->
            viewModel.sendAction(
                Action.OnSearchClick(text?.toString().orEmpty())
            )
        }

        binding.btnSubscribed.root.setOnClickListener {
            viewModel.sendAction(Action.OnSubscribedClick)
        }

        binding.btnAllStreams.root.setOnClickListener {
            viewModel.sendAction(Action.OnAllStreamsClick)
        }

        binding.layoutError.btnRetry.setOnClickListener {
            viewModel.sendAction(
                Action.LoadData(SelectedTab.SUBSCRIBED)
            )
        }

        binding.rvChannels.adapter = adapter

        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { render(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDetach() {
        super.onDetach()
        requireContext().getApp().clearChannelsComponent()
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

                    adapter.submitList(state.visibleItems)
                }

                is ChannelsViewModel.State.Error -> {
                    progress.isGone = true
                    rvChannels.isGone = true
                    layoutError.root.isVisible = true
                }
            }
        }
    }

    private fun openChat(item: ChannelsItemUi.TopicUi) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainerView,
                ChatFragment.newInstance(
                    streamName = item.streamName,
                    topicName = item.text
                ),
                ChatFragment.TAG
            )
            .addToBackStack(null)
            .commit()
    }

}