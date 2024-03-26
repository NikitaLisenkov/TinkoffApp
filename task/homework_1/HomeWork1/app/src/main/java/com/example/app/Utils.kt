package com.example.app

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setPadding
import com.example.app.chat.model.Reaction
import com.example.app.chat.view.EmojiView
import com.example.app.chat.view.FlexboxLayout
import kotlin.math.roundToInt

fun Float.sp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
)

fun Float.dp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
)

fun FlexboxLayout.addReactions(reactions: List<Reaction>, onEmojiClick: (String) -> Unit) {
    val flexBox = this
    reactions.forEach { reaction ->
        val emojiView = EmojiView(flexBox.context)
        emojiView.setOnClickListener {
            onEmojiClick.invoke(reaction.code)
        }
        emojiView.setPadding(4f.dp(context).roundToInt())
        emojiView.emojiCode = reaction.code
        emojiView.counter = reaction.counter
        emojiView.background = AppCompatResources.getDrawable(flexBox.context, R.drawable.emoji_bg)
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(4, 4, 4, 4)
        emojiView.layoutParams = layoutParams
        flexBox.addViewAndRelayout(emojiView)
    }
    if (reactions.isNotEmpty()) {
        addButtonPlus()
    }
}