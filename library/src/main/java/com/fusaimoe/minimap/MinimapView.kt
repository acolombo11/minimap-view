package com.fusaimoe.minimap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MinimapView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    companion object {
        var RecyclerView.minimap: MinimapView
            get() = MinimapView(context).apply { setRecyclerView(this@minimap) }
            set(value) = value.setRecyclerView(this@minimap)
    }

    private var isVisible = false

    // Default Sizes
    // TODO Make these values customizable by xml
    private var maxSize = resources.getDimension(R.dimen.minimap_size)
    private var borderWidth = resources.getDimension(R.dimen.minimap_border_width)
    private var cornerRadius = resources.getDimension(R.dimen.minimap_corner_radius)

    // Custom Sizes
    private var scaleFactor = 0f
    private var scrollWidth = 0f
    private var scrollHeight = 0f
    private var totalWidth = 0f
    private var totalHeight = 0f
    private var indicatorWidth = 0f
    private var indicatorHeight = 0f
    private var indicatorX = 0f
    private var indicatorY = 0f

    // Custom default paints
    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorDefaultBackground)
        isAntiAlias = true
    }
    private val indicatorPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.colorDefaultIndicator)
        strokeCap = Paint.Cap.ROUND
        strokeWidth = borderWidth
    }

    // TODO Do the same for ScrollView
    fun setRecyclerView(recyclerView: RecyclerView) {
        // Wait for recyclerView to be measured before doing anything with the minimap
        recyclerView.afterMeasured {
            updateScaleFactor(this)
            addScrollListener { dx, dy -> if (isVisible) moveIndicator(dx, dy) }
            addLayoutChangeListener { updateScaleFactor(this) }
        }
    }

    private fun updateScaleFactor(rv: RecyclerView) {
        scrollWidth = rv.computeHorizontalScrollRange().toFloat()
        scrollHeight = rv.computeVerticalScrollRange().toFloat()

        rv.updateVisibility()
        if (isVisible) {

            // Scrollable height might be < than the scrollable width while scrollable width being < than total height of the RecyclerView
            val biggerWidth = maxOf(scrollWidth, rv.width.toFloat()) + rv.paddingRight + rv.paddingLeft
            val biggerHeight = maxOf(scrollHeight, rv.height.toFloat()) + rv.paddingTop + rv.paddingBottom
            val smallerWidth = if (maxSize > totalWidth && totalWidth != 0f) totalWidth else maxSize
            val smallerHeight = if (maxSize > totalHeight && totalHeight != 0f) totalHeight else maxSize

            // So, when calculating scaleFactor, we need the bigger size to fit into the maxSize of the view
            scaleFactor = when {
                biggerWidth > biggerHeight -> biggerWidth / smallerWidth
                biggerWidth < biggerHeight -> biggerHeight / smallerHeight
                else  -> biggerWidth / smallerWidth
            }

            totalWidth = biggerWidth / scaleFactor
            totalHeight = biggerHeight / scaleFactor
            
            // TODO Make it possible to choose a maxWidth or maxHeight, instead of just one mazSize to stay inside of

            if (scaleFactor != 0f) {
                indicatorWidth = rv.width / scaleFactor
                indicatorHeight = rv.height / scaleFactor
            }

            requestLayout()
        }
    }

    private fun View.updateVisibility() = if (this.shouldBeVisible()) {
        visibility = this.visibility
        isVisible = this.visibility == View.VISIBLE
        true
    } else {
        visibility = View.GONE
        isVisible = false
        false
    }

    private fun View?.shouldBeVisible() = this != null && (scrollWidth > this.width || scrollHeight > this.height)

    private fun moveIndicator(dx: Int, dy: Int) = if (scaleFactor != 0f) {
        indicatorX += dx / scaleFactor
        indicatorY += dy / scaleFactor
        fixIndicatorPosition()
        invalidate()
        true
    } else {
        false
    }

    private fun drawIndicator(canvas: Canvas?) {
        fixIndicatorPosition()
        canvas?.drawRoundRect(
            indicatorX + borderWidth / 2,
            indicatorY + borderWidth / 2,
            indicatorX + indicatorWidth + borderWidth / 2,
            indicatorY + indicatorHeight + borderWidth / 2,
            cornerRadius,
            cornerRadius,
            indicatorPaint
        )
    }

    private fun fixIndicatorPosition() {
        when {
            indicatorX < 0f -> indicatorX = 0f
            indicatorX > totalWidth - indicatorWidth -> indicatorX = totalWidth - indicatorWidth
        }
        when {
            indicatorY < 0f -> indicatorY = 0f
            indicatorY > totalHeight - indicatorHeight -> indicatorY = totalHeight - indicatorHeight
        }
    }

    private fun drawBackground(canvas: Canvas?) {
        canvas?.drawRoundRect(
            0f + borderWidth / 2,
            0f + borderWidth / 2,
            totalWidth + borderWidth / 2,
            totalHeight + borderWidth / 2,
            cornerRadius,
            cornerRadius,
            backgroundPaint
        )
    }

    override fun onDraw(canvas: Canvas?) {
        drawBackground(canvas)
        drawIndicator(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        // TODO Check how everything is working with exact size
        val width =
            if (widthMode == View.MeasureSpec.EXACTLY) widthSize.toFloat() else if (totalWidth > maxSize) maxSize else totalWidth
        val height =
            if (heightMode == View.MeasureSpec.EXACTLY) heightSize.toFloat() else if (totalHeight > maxSize) maxSize else totalHeight

        setMeasuredDimension(width.toInt() + borderWidth.toInt(), height.toInt() + borderWidth.toInt())
    }

}