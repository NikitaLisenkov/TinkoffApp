package com.example.app.chat.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.app.R
import com.example.app.utils.dp
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import kotlin.math.roundToInt

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : ViewGroup(context, attrs, defStyle, defTheme) {

    val avatarImageView: ShapeableImageView
    val nameTextView: TextView
    val messageTextView: TextView
    val reactionsLayout: FlexboxLayout

    init {
        avatarImageView = ShapeableImageView(context).apply {
            setImageResource(R.mipmap.ic_launcher)
            val imageSize = 38f.dp(context).roundToInt()
            layoutParams = MarginLayoutParams(imageSize, imageSize)

            shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCorners(CornerFamily.ROUNDED, imageSize / 2f)
                .build()
            addView(this)
        }

        nameTextView = TextView(context).apply {
            text = "Username"
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.google.android.material.R.color.material_deep_teal_200
                )
            )
            setTypeface(null, Typeface.BOLD)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            layoutParams = generateDefaultLayoutParams()
            addView(this)
        }

        messageTextView = TextView(context).apply {
            text = "Message text"
            setTextColor(ContextCompat.getColor(context, R.color.white))
            layoutParams = generateDefaultLayoutParams()
            addView(this)
        }

        reactionsLayout = FlexboxLayout(context).apply {
            layoutParams = generateDefaultLayoutParams()
            this@MessageViewGroup.addView(this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val children = listOf(avatarImageView, nameTextView, messageTextView, reactionsLayout)
        children.forEach { measureChildWithMargins(it, widthMeasureSpec, 0, heightMeasureSpec, 0) }

        val availableWidth = widthSize - paddingLeft - paddingRight - avatarImageView.measuredWidth - MARGIN_GAP
        val availableHeight = heightSize - paddingTop - paddingBottom

        nameTextView.measure(
            MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.UNSPECIFIED)
        )

        messageTextView.measure(
            MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.UNSPECIFIED)
        )

        val totalHeight = paddingTop + paddingBottom + 2 * MARGIN_GAP + maxOf(
            avatarImageView.measuredHeight,
            nameTextView.measuredHeight + messageTextView.measuredHeight
        ) + reactionsLayout.measuredHeight

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> {
                (paddingLeft + paddingRight
                        + MARGIN_GAP
                        + avatarImageView.measuredWidth
                        + nameTextView.measuredWidth
                        + messageTextView.measuredWidth
                        + reactionsLayout.measuredWidth
                        )
                    .coerceAtMost(widthSize)
            }

            else -> {
                paddingLeft + paddingRight + MARGIN_GAP +
                        maxOf(
                            avatarImageView.measuredWidth,
                            nameTextView.measuredWidth,
                            messageTextView.measuredWidth,
                            reactionsLayout.measuredWidth
                        )
            }
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> totalHeight.coerceAtMost(heightSize)
            else -> totalHeight
        }

        setMeasuredDimension(
            resolveSizeAndState(width, widthMeasureSpec, 0),
            resolveSizeAndState(height, heightMeasureSpec, 0)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val avatarTop = paddingTop
        val avatarLeft = paddingLeft

        avatarImageView.layout(
            avatarLeft,
            avatarTop,
            avatarLeft + avatarImageView.measuredWidth,
            avatarTop + avatarImageView.measuredHeight
        )

        val nameTop = paddingTop
        val nameLeft = avatarLeft + avatarImageView.measuredWidth + MARGIN_GAP
        nameTextView.layout(
            nameLeft,
            nameTop,
            nameLeft + nameTextView.measuredWidth,
            nameTop + nameTextView.measuredHeight
        )

        val messageTop = nameTop + nameTextView.measuredHeight + MARGIN_GAP
        messageTextView.layout(
            nameLeft,
            messageTop,
            nameLeft + messageTextView.measuredWidth,
            messageTop + messageTextView.measuredHeight
        )

        val reactionsTop = maxOf(
            avatarTop + avatarImageView.measuredHeight,
            messageTop + messageTextView.measuredHeight
        ) + MARGIN_GAP

        reactionsLayout.layout(
            paddingLeft,
            reactionsTop,
            paddingLeft + reactionsLayout.measuredWidth,
            reactionsTop + reactionsLayout.measuredHeight
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun checkLayoutParams(layoutParams: LayoutParams?): Boolean {
        return layoutParams is MarginLayoutParams
    }

    override fun generateLayoutParams(layoutParams: LayoutParams?): LayoutParams {
        return MarginLayoutParams(layoutParams)
    }

    private companion object {
        const val MARGIN_GAP = 32
    }
}
