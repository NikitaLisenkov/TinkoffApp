package com.example.app.presentation.chat.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import com.example.app.utils.dp

open class FlexboxLayoutRight @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    init {
        setWillNotDraw(true)
    }

    private var childWidth: Int = 0
    private var childHeight: Int = 0
    private val childRightMargin: Int = 7.dp(context)
    private val childBottomMargin: Int = 10.dp(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth: Int = MeasureSpec.getSize(widthMeasureSpec)
        var currentWidth = parentWidth
        var currentHeight = 0

        children.forEach { child ->
            val childLP = child.layoutParams as MarginLayoutParams

            measureChildWithMargins(
                child,
                widthMeasureSpec,
                currentWidth,
                heightMeasureSpec,
                currentHeight
            )

            childLP.apply {
                rightMargin = childRightMargin
                bottomMargin = childBottomMargin
            }

            childWidth = child.measuredWidth + childLP.run { leftMargin + rightMargin }
            childHeight = child.measuredHeight + childLP.run { topMargin + bottomMargin }

            if (currentWidth == parentWidth && currentHeight == 0) currentHeight += childHeight

            if (currentWidth - childWidth >= 0) {
                currentWidth -= childWidth
            } else {
                currentHeight += childHeight
                currentWidth = parentWidth - childWidth
            }
        }

        setMeasuredDimension(
            resolveSize(parentWidth, widthMeasureSpec),
            resolveSize(currentHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val parentWidth: Int = measuredWidth
        var top = 0
        var right = parentWidth

        children.forEach { child ->
            val childLP = child.layoutParams as MarginLayoutParams
            childWidth = child.measuredWidth + childLP.run { leftMargin + rightMargin }
            childHeight = child.measuredHeight + childLP.run { topMargin + bottomMargin }
            childLP.apply {
                rightMargin = childRightMargin
                bottomMargin = childBottomMargin
            }

            if (right - childWidth >= 0) {
                with(childLP) {
                    child.layout(
                        right - childWidth + leftMargin,
                        top + topMargin,
                        right - rightMargin,
                        top + childHeight - bottomMargin
                    )
                }
                right -= childWidth
            } else {
                right = parentWidth
                top += childHeight
                with(childLP) {
                    child.layout(
                        right - childWidth + leftMargin,
                        top + topMargin,
                        right - rightMargin,
                        top + childHeight - bottomMargin
                    )
                }
                right -= childWidth
            }
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)
}