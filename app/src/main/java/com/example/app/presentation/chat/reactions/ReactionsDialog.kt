package com.example.app.presentation.chat.reactions

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.presentation.chat.adapter.ReactionsAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReactionsDialog : BottomSheetDialogFragment(R.layout.bottom_sheet) {

    private val adapter = ReactionsAdapter(
        onReactionClick = {
            onReactionClick(it)
        }
    )

    private val messageId: Long
        get() = arguments?.getLong(ARG_MSG_ID, -1) ?: -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.reactions = Reactions.emojiSet
        val rvEmojis = view.findViewById<RecyclerView>(R.id.rv_emojis)
        rvEmojis.adapter = adapter
    }

    private fun onReactionClick(emojiName: String) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(
                RESULT_KEY_EMOJI_NAME to emojiName,
                RESULT_KEY_MSG_ID to messageId,
            )
        )
        dismiss()
    }

    companion object {
        const val ARG_MSG_ID: String = "ARG_MSG_ID"
        const val REQUEST_KEY: String = "REQUEST_KEY"
        const val RESULT_KEY_EMOJI_NAME: String = "RESULT_KEY_EMOJI_NAME"
        const val RESULT_KEY_MSG_ID: String = "RESULT_KEY_MSG_ID"
    }
}