package com.example.app.presentation.chat.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.R
import com.example.app.databinding.MessageViewGroupBinding
import com.example.app.presentation.chat.model.ReactionUi
import com.example.app.utils.setEmojis

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : ViewGroup(context, attrs, defStyle, defTheme) {

    val binding: MessageViewGroupBinding

    private val avatarRect = Rect()
    private val usernameRect = Rect()
    private val messageRect = Rect()
    private val flexboxRect = Rect()
    private val backgroundRect = Rect()
    private val timeStampRect = Rect()

    init {
        LayoutInflater.from(context).inflate(R.layout.message_view_group, this, true)
        binding = MessageViewGroupBinding.bind(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var usedWidth = 0
        var usedHeight = 0
        with(binding) {
            measureChildWithMargins(
                ivAvatar,
                widthMeasureSpec,
                usedWidth,
                heightMeasureSpec,
                usedHeight
            )
            usedWidth += getWidthWithMargins(ivAvatar)

            measureChildWithMargins(
                cvBackground,
                widthMeasureSpec,
                usedWidth,
                heightMeasureSpec,
                usedHeight
            )

            measureChildWithMargins(
                tvUsername,
                widthMeasureSpec,
                usedWidth,
                heightMeasureSpec,
                usedHeight
            )
            usedHeight += getHeightWithMargins(tvUsername)

            measureChildWithMargins(
                tvMessage,
                widthMeasureSpec,
                usedWidth,
                heightMeasureSpec,
                usedHeight
            )
            usedHeight += getHeightWithMargins(tvMessage)

            measureChildWithMargins(
                tvTimestamp,
                widthMeasureSpec,
                0,
                heightMeasureSpec,
                usedHeight
            )
            usedHeight += getHeightWithMargins(tvTimestamp)

            measureChildWithMargins(
                flexbox,
                widthMeasureSpec,
                usedWidth,
                heightMeasureSpec,
                usedHeight
            )
            usedHeight += getHeightWithMargins(flexbox)
            usedWidth += getWidthWithMargins(flexbox)
        }

        setMeasuredDimension(
            resolveSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec),
            resolveSize(usedHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        with(binding) {
            ivAvatar.layout(avatarRect, 0, 0)
            cvBackground.layout(backgroundRect, avatarRect.right, 0)
            tvUsername.layout(usernameRect, 0, 0)
            tvMessage.layout(messageRect, 0, usernameRect.bottom)
            tvTimestamp.layoutFromRightBorder(
                timeStampRect,
                backgroundRect.right,
                messageRect.bottom
            )
            flexbox.layout(flexboxRect, avatarRect.right, messageRect.bottom)
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)

    fun setEmojis(
        items: List<ReactionUi>,
        onEmojiClick: (emojiName: String, emojiCode: String) -> Unit,
        onAddClick: () -> Unit
    ) {
        binding.flexbox.setEmojis(items, onEmojiClick, onAddClick)
    }

}

private fun getWidthWithMargins(view: View): Int {
    val viewLP = view.layoutParams as ViewGroup.MarginLayoutParams
    return view.measuredWidth + viewLP.run { leftMargin + rightMargin }
}

private fun getHeightWithMargins(view: View): Int {
    val viewLP = view.layoutParams as ViewGroup.MarginLayoutParams
    return view.measuredHeight + viewLP.run { topMargin + bottomMargin }
}

private fun View.layout(rect: Rect) {
    with(rect) {
        layout(left, top, right, bottom)
    }
}

private fun View.layout(
    rect: Rect,
    leftBorder: Int,
    topBorder: Int
) {
    val viewLP = this.layoutParams as ViewGroup.MarginLayoutParams
    rect.left = leftBorder + viewLP.leftMargin
    rect.top = topBorder + viewLP.topMargin
    rect.right = rect.left + this.measuredWidth + viewLP.rightMargin
    rect.bottom = rect.top + this.measuredHeight + viewLP.bottomMargin
    this.layout(rect)
}

private fun View.layoutFromRightBorder(
    rect: Rect,
    rightBorder: Int,
    topBorder: Int
) {
    val viewLP = this.layoutParams as ViewGroup.MarginLayoutParams
    rect.right = rightBorder - viewLP.rightMargin
    rect.top = topBorder + viewLP.topMargin
    rect.left = rect.right - this.measuredWidth + viewLP.leftMargin
    rect.bottom = rect.top + this.measuredHeight + viewLP.bottomMargin
    this.layout(rect)
}

