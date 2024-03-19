package com.example.app

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMessage()
    }

    private fun initMessage() {
        findViewById<MessageViewGroup>(R.id.messageView).apply {
            setBackgroundResource(R.drawable.emoji_unselected_bg)
            setPadding(48)
            avatarImageView.setImageResource(R.drawable.ic_darrel)

            nameTextView.text = "Darrell Steward"
            nameTextView.setTextColor(ContextCompat.getColor(context, com.google.android.material.R.color.material_deep_teal_200))
            nameTextView.setTypeface(null, Typeface.BOLD)
            nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

            messageTextView.text = "Привет! lorem ipsum test message dalshe zabil"
            messageTextView.setTextColor(ContextCompat.getColor(context, R.color.white))

            reactionsLayout.addButtonPlus()

            reactionsLayout.setButtonPlusClickListener {
                val newEmojiView = EmojiView(this@MainActivity)
                newEmojiView.emojiCode = emojis.random()
                newEmojiView.setPadding(16)
                newEmojiView.background = AppCompatResources.getDrawable(this@MainActivity, R.drawable.emoji_bg)
                newEmojiView.counter++

                val layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(4, 4, 4, 4)
                newEmojiView.layoutParams = layoutParams

                newEmojiView.setOnClickListener { emojiView ->
                    (emojiView as EmojiView).counter++
                    emojiView.isSelected = !emojiView.isSelected
                }
                reactionsLayout.addViewAndRelayout(newEmojiView)
            }
        }
    }

    companion object {
        val emojis = listOf(
            "\uD83D\uDE00", // 😀
            "\uD83D\uDE01", // 😁
            "\uD83D\uDE02", // 😂
            "\uD83D\uDE03", // 😃
            "\uD83D\uDE04", // 😄
            "\uD83D\uDE05", // 😅
            "\uD83D\uDE06", // 😆
            "\uD83D\uDE07", // 😇
            "\uD83D\uDE08", // 😈
            "\uD83D\uDE09", // 😉
            "\uD83D\uDE0A", // 😊
            "\uD83D\uDE0B", // 😋
            "\uD83D\uDE0C", // 😌
            "\uD83D\uDE0D", // 😍
            "\uD83D\uDE0E", // 😎
        )
    }
}
