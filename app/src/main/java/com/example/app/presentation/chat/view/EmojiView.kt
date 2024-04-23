package com.example.app.presentation.chat.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.example.app.R
import com.example.app.utils.dp
import com.example.app.utils.sp

class EmojiView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : View(context, attributeSet, defStyle, defTheme) {

    var emojiCode: String = "\uD83E\uDD74"
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var counter: Int = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private var marginLeft = 0
    private var marginRight = 0
    private var marginTop = 0
    private var marginBottom = 0

    private val roundRadius = 10f.dp(context)

    init {
        context.withStyledAttributes(attributeSet, R.styleable.EmojiView) {
            counter = getInt(R.styleable.EmojiView_count, 0)
        }
    }

    private val textToDraw: String
        get() {
            return if (counter != 0) "$emojiCode $counter"
            else emojiCode
        }

    private val bgPaint = Paint().apply {
        color = Color.parseColor("#3A3A3A")
    }

    private val textPaint = TextPaint().apply {
        color = Color.WHITE
        textSize = 20f.sp(context)
    }

    private val bgRectF = RectF()
    private val textRect = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textRect)

        val actualWidth = resolveSize(
            paddingRight + paddingLeft + textRect.width() + marginRight + marginLeft,
            widthMeasureSpec
        )

        val actualHeight = resolveSize(
            paddingTop + paddingBottom + textRect.height() + marginTop + marginBottom,
            heightMeasureSpec
        )

        bgRectF.set(
            marginLeft.toFloat(),
            marginTop.toFloat(),
            actualWidth.toFloat(),
            actualHeight.toFloat()
        )

        setMeasuredDimension(
            actualWidth,
            actualHeight
        )
    }

    override fun onDraw(canvas: Canvas) {
        val topOffset = height / 2 - textRect.exactCenterY()

        canvas.drawRoundRect(bgRectF, roundRadius, roundRadius, bgPaint)
        canvas.drawText(textToDraw, paddingLeft.toFloat(), topOffset, textPaint)
    }

}