package com.example.app.chat.reactions

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.app.FakeData
import com.example.app.R
import com.example.app.chat.adapter.ReactionsAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReactionsDialog : BottomSheetDialogFragment(R.layout.bottom_sheet) {

    private val adapter = ReactionsAdapter(
        onReactionClick = {
            onReactionClick(it)
        }
    )

    private val messageId: String
        get() = arguments?.getString(ARG_MSG_ID, "").orEmpty()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.reactions = FakeData.emojis
        val rvEmojis = view.findViewById<RecyclerView>(R.id.rvEmojis)
        rvEmojis.adapter = adapter
    }

    private fun onReactionClick(emoji: String) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(
                RESULT_KEY_EMOJI to emoji,
                RESULT_KEY_MSG_ID to messageId,
            )
        )
        dismiss()
    }

    companion object {
        const val ARG_MSG_ID: String = "ARG_MSG_ID"
        const val REQUEST_KEY: String = "REQUEST_KEY"
        const val RESULT_KEY_EMOJI: String = "RESULT_KEY_EMOJI"
        const val RESULT_KEY_MSG_ID: String = "RESULT_KEY_MSG_ID"
    }
}