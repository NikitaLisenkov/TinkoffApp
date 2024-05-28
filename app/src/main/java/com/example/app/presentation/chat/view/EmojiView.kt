package com.example.app.presentation.chat.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.example.app.R
import com.example.app.utils.EmojiUtils
import com.example.app.utils.dp
import com.example.app.utils.sp

class EmojiView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : View(context, attributeSet, defStyle, defTheme) {

    var emojiName: String = ""
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var emojiUnicode: String = ""
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var emojiCounter: Int = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var flagIsSelected: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    private var counterColor: Int = 0

    @ColorInt
    private var colorNotSelected: Int = 0

    @ColorInt
    private var colorSelected: Int = 0

    private var emojiWidth: Float = 0F
    private var emojiHeight: Float = 0F

    private var cornerRadius: Float = 10f.dp(context)
    private val textMargin: Float = 6f.sp(context)
    private val bgMargin: Float = 10f.dp(context)

    private val emojiPoint = PointF()
    private val counterPoint = PointF()
    private val paddingPaint = Paint()

    private val bgPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val emojiPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    private var emojiSize: Float
        get() = emojiPaint.textSize
        set(value) {
            if (emojiPaint.textSize != value) {
                emojiPaint.textSize = value
                requestLayout()
            }
        }

    init {
        context.withStyledAttributes(attributeSet, R.styleable.EmojiView) {
            emojiUnicode = DEFAULT_EMOJI_UNICODE
            emojiSize = DEFAULT_FONT_SIZE_SP.sp(context)
            emojiCounter = getInt(R.styleable.EmojiView_count, 0)
            counterColor = ResourcesCompat.getColor(resources, R.color.white_primary, null)
            colorSelected = ResourcesCompat.getColor(resources, R.color.grey_selected, null)
            colorNotSelected =
                ResourcesCompat.getColor(resources, R.color.grey_dark, null)
        }
    }

    private val counterPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
        textSize = emojiSize
        color = counterColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val counterString = emojiCounter.toString()
        emojiWidth = if (emojiUnicode == "+") {
            val defEmoji = EmojiUtils.unicodeSequenceToString(DEFAULT_EMOJI_UNICODE)
            emojiPaint.measureText(defEmoji, 0, defEmoji.length)
        } else {
            emojiPaint.measureText(emojiUnicode, 0, emojiUnicode.length)
        }
        val textWidth =
            emojiWidth + counterPaint.measureText(counterString, 0, counterString.length)
        emojiHeight = emojiPaint.fontMetrics.run { bottom - ascent }
        val contentWidth = textWidth + (paddingStart + paddingEnd + bgMargin) * 2f + textMargin
        val contentHeight = emojiHeight + paddingTop + paddingBottom + bgMargin * 2f
        setMeasuredDimension(contentWidth.toInt(), contentHeight.toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (emojiUnicode == "+") {
            emojiPoint.set(width / 2f, height / 2f)
        } else {
            val padding = paddingPaint.measureText("0") * (emojiCounter.toString().length - 1)
            val baseline = height / 1.6f

            emojiPoint.set(
                width / 2f - textMargin - padding,
                baseline
            )
            counterPoint.set(
                width / 2f + textMargin - padding,
                baseline
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        val canvasCount = canvas.save()
        setViewBackground(canvas)
        drawViewContent(canvas)
        canvas.restoreToCount(canvasCount)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isSelected) mergeDrawableStates(drawableState, DRAWABLE_STATE)
        return drawableState
    }

    private fun drawViewContent(canvas: Canvas) {
        with(canvas) {
            if (emojiUnicode == "+") {
                emojiPaint.apply {
                    color = counterColor
                    strokeWidth = 1f.dp(context)
                }
                drawLine(
                    (width - emojiWidth) / 2,
                    height / 2f,
                    (width + emojiWidth) / 2,
                    height / 2f,
                    emojiPaint
                )
                drawLine(
                    width / 2f,
                    (height + emojiHeight) / 2,
                    width / 2f,
                    (height - emojiHeight) / 2f,
                    emojiPaint
                )
            } else {
                drawText(emojiUnicode, emojiPoint.x, emojiPoint.y, emojiPaint)
                drawText(emojiCounter.toString(), counterPoint.x, counterPoint.y, counterPaint)
            }
        }
    }

    private fun setViewBackground(canvas: Canvas) {
        bgPaint.apply {
            color = if (flagIsSelected) colorSelected
            else colorNotSelected
        }
        canvas.drawRoundRect(
            0f,
            0f,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            cornerRadius,
            cornerRadius,
            bgPaint
        )
    }

    companion object {
        private const val DEFAULT_FONT_SIZE_SP = 14f
        private const val DEFAULT_EMOJI_UNICODE = "1f600"
        private val DRAWABLE_STATE = IntArray(1) { android.R.attr.state_selected }
    }

}