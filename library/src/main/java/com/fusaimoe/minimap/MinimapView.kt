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

    // Custom paints
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


    fun setRecyclerView(recyclerView: RecyclerView) {
        updateScaleFactor(recyclerView)

        invalidate()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isVisible) moveIndicator(dx, dy)
            }
        })

        recyclerView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateScaleFactor(recyclerView)
        }
    }

    private fun updateScaleFactor(recyclerView: RecyclerView) {
        scrollWidth = recyclerView.computeHorizontalScrollRange().toFloat()
        scrollHeight = recyclerView.computeVerticalScrollRange().toFloat()

        updateVisibility(recyclerView)
        if (isVisible) {

            // The height of the recyclerView might be bigger than the scrollable width of the recyclerView
            val biggerWidth = maxOf(scrollWidth, recyclerView.width.toFloat())
            val biggerHeight = recyclerView.height.toFloat()

            scaleFactor = when {

                biggerWidth > biggerHeight -> biggerWidth / (if (totalWidth != 0f) minOf(
                    maxSize,
                    totalWidth
                ) else maxSize)
                biggerWidth < biggerHeight -> biggerHeight / (if (totalHeight != 0f) minOf(
                    maxSize,
                    totalHeight
                ) else maxSize)
                else -> scrollWidth / maxSize
            }

            totalWidth = maxOf(scrollWidth, recyclerView.width.toFloat()) / scaleFactor
            totalHeight = maxOf(scrollHeight, recyclerView.height.toFloat()) / scaleFactor

            if (scaleFactor != 0f) {
                indicatorWidth = recyclerView.width / scaleFactor
                indicatorHeight = recyclerView.height / scaleFactor
            }

            requestLayout()
        }
    }

    private fun updateVisibility(recyclerView: RecyclerView): Boolean {
        return if (shouldBeVisible(recyclerView)) {
            visibility = recyclerView.visibility
            isVisible = recyclerView.visibility == View.VISIBLE
            true
        } else {
            visibility = View.GONE
            isVisible = false
            false
        }
    }

    private fun shouldBeVisible(recyclerView: RecyclerView?) =
        recyclerView != null && (scrollWidth > recyclerView.width || scrollHeight > recyclerView.height)

    private fun moveIndicator(dx: Int, dy: Int): Boolean {
        return if (scaleFactor != 0f) {
            indicatorX += dx / scaleFactor
            indicatorY += dy / scaleFactor
            invalidate()
            true
        } else {
            false
        }
    }

    private fun drawIndicator(canvas: Canvas?) {
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

        val width = if (widthMode == View.MeasureSpec.EXACTLY) widthSize.toFloat() else minOf(maxSize, totalWidth)
        val height = if (heightMode == View.MeasureSpec.EXACTLY) heightSize.toFloat() else minOf(maxSize, totalHeight)

        setMeasuredDimension(width.toInt() + borderWidth.toInt(), height.toInt() + borderWidth.toInt())
    }

}