package com.example.app.utils

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import androidx.core.view.setPadding
import com.example.app.TinkoffApp
import com.example.app.presentation.chat.model.ReactionUi
import com.example.app.presentation.chat.view.EmojiView
import com.example.app.presentation.chat.view.FlexboxLayout
import kotlinx.coroutines.CancellationException
import kotlin.math.roundToInt

fun Float.sp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
)

fun Float.dp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
)

fun FlexboxLayout.addReactions(reactions: List<ReactionUi>, onEmojiClick: (String) -> Unit) {
    val flexBox = this
    reactions.forEach { reaction ->
        val emojiView = EmojiView(flexBox.context)
        emojiView.setOnClickListener {
            onEmojiClick.invoke(reaction.name)
        }
        emojiView.setPadding(4f.dp(context).roundToInt())
        emojiView.emojiCode = reaction.code
        emojiView.counter = reaction.counter
        emojiView.isEmojiSelected = reaction.isSelected
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

fun Context.getApp(): TinkoffApp = this.applicationContext as TinkoffApp

suspend fun <T> runSuspendCatching(
    action: suspend () -> T,
    onSuccess: suspend (T) -> Unit,
    onError: () -> Unit
) {
    try {
        val result = action()
        onSuccess.invoke(result)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        onError.invoke()
    }
}