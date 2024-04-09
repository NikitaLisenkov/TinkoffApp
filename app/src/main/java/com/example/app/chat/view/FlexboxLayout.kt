package com.example.app.chat.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.contains
import com.example.app.R
import com.example.app.utils.dp
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import kotlin.math.roundToInt

class FlexboxLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    private var buttonPlusClickListener: OnClickListener? = null

    private val plusButton by lazy {
        val button = ShapeableImageView(context)
        val roundSize = 10f.dp(context)
        val paddingVertical = 2f.dp(context).roundToInt()
        val paddingHorizontal = 2f.dp(context).roundToInt()
        button.layoutParams = generateDefaultLayoutParams()
        button.setContentPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

        button.setBackgroundColor(Color.parseColor("#1C1C1C"))

        button.shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, roundSize)
            .build()

        button.setImageResource(R.drawable.add_ic)
        button.setOnClickListener {
            buttonPlusClickListener?.onClick(it)
        }
        super.addView(button)
        button
    }

    fun setButtonPlusClickListener(listener: OnClickListener?) {
        buttonPlusClickListener = listener
    }

    fun addButtonPlus() {
        if (!contains(plusButton)) {
            addView(plusButton)
        }
    }

    fun addViewAndRelayout(view: View) {
        addView(view, childCount - 1)
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        var width = 0
        var height = 0
        var lineWidth = 0
        var maxHeightInLine = 0

        children.forEach { child ->
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, height)

            if (lineWidth + child.getFullWidth() <= widthSize - paddingLeft - paddingRight) {
                lineWidth += child.getFullWidth()
                maxHeightInLine = maxHeightInLine.coerceAtLeast(child.getFullHeight())
            } else {
                width = width.coerceAtLeast(lineWidth)
                height += maxHeightInLine
                lineWidth = child.getFullWidth()
                maxHeightInLine = child.getFullHeight()
            }
        }

        width = width.coerceAtLeast(lineWidth)
        height += maxHeightInLine

        width += paddingLeft + paddingRight
        height += paddingTop + paddingBottom

        setMeasuredDimension(
            resolveSizeAndState(width, widthMeasureSpec, 0),
            resolveSizeAndState(height, heightMeasureSpec, 0)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = paddingLeft
        var currentTop = paddingTop
        var maxHeightInLine = 0

        children.forEach { child ->
            val layoutParams = child.layoutParams as MarginLayoutParams

            if (currentLeft + child.getFullWidth() > width - paddingRight) {
                currentLeft = paddingLeft
                currentTop += maxHeightInLine
                maxHeightInLine = 0
            }

            val left = currentLeft + layoutParams.leftMargin
            val top = currentTop + layoutParams.topMargin
            val right = left + child.measuredWidth
            val bottom = top + child.measuredHeight

            child.layout(left, top, right, bottom)

            currentLeft += child.getFullWidth()
            maxHeightInLine = maxHeightInLine.coerceAtLeast(child.getFullHeight())
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return layoutParams is MarginLayoutParams
    }

    private fun View.getFullWidth(): Int {
        val layoutParams = this.layoutParams as MarginLayoutParams
        return this.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
    }

    private fun View.getFullHeight(): Int {
        val layoutParams = this.layoutParams as MarginLayoutParams
        return this.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
    }

}