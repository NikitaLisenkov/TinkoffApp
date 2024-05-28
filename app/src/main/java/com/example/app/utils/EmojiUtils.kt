package com.example.app.utils

import android.view.ViewGroup
import com.example.app.presentation.chat.model.ReactionUi
import com.example.app.presentation.chat.view.EmojiView

object EmojiUtils {

    fun unicodeSequenceToString(unicodeSequence: String): String {
        return unicodeSequence.split('-')
            .map { it.toInt(16) }
            .flatMap { Character.toChars(it).toList() }
            .joinToString("")
    }

    fun stringToUnicodeSequence(text: String): String {
        val result = StringBuilder()
        var i = 0
        while (i < text.length) {
            val codePoint = text.codePointAt(i)
            if (i > 0) result.append('-')
            result.append(Integer.toHexString(codePoint))
            i += Character.charCount(codePoint)
        }
        return result.toString()
    }
}

fun ViewGroup.setEmojis(
    items: List<ReactionUi>,
    onEmojiClick: (((emojiName: String, emojiCode: String) -> Unit)),
    onAddClick: (() -> Unit)
) {
    removeAllViews()
    items.forEach { emoji ->
        val emojiView = EmojiView(context).apply {
            emojiName = emoji.name
            emojiUnicode = emoji.code
            emojiCounter = emoji.counter
            flagIsSelected = emoji.isSelected
            setOnClickListener { onEmojiClick.invoke(emoji.name, emoji.code) }
        }
        addView(emojiView)
    }
    if (items.isNotEmpty()) {
        addView(
            EmojiView(context).apply {
                emojiUnicode = "+"
                setOnClickListener { onAddClick.invoke() }
            }
        )
    }
}