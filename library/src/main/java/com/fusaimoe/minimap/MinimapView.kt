package com.fusaimoe.minimap

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MinimapView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    companion object {
        var RecyclerView.minimap: MinimapView
            get() = MinimapView(context).apply { setRecyclerView(this@minimap) }
            set(value) = value.setRecyclerView(this@minimap)
    }

    private val a: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MinimapView)

    var maxSize = a.getDimension(R.styleable.MinimapView_minimapMaxSize, 0f)
    var borderWidth = a.getDimension(R.styleable.MinimapView_minimapBorderWidth, 0f)
    var cornerRadius = a.getDimension(R.styleable.MinimapView_minimapCornerRadius, 0f)
    var mapBackgroundColor = a.getColor(R.styleable.MinimapView_minimapBackgroundColor, Color.GRAY)
    var indicatorColor = a.getColor(R.styleable.MinimapView_minimapIndicatorColor, Color.WHITE)

    init {
        a.recycle()
    }

    // Calculated sizes
    private var scaleFactor = 0f
    private var scrollWidth = 0f
    private var scrollHeight = 0f
    private var calculatedWidth = 0f
    private var calculatedHeight = 0f
    private var indicatorWidth = 0f
    private var indicatorHeight = 0f

    // Calculated positions
    private var indicatorX = 0f
    private var indicatorY = 0f

    // Custom default paints
    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = mapBackgroundColor
        isAntiAlias = true
    }
    private val indicatorPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = indicatorColor
        strokeCap = Paint.Cap.ROUND
        strokeWidth = borderWidth
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        // Wait for recyclerView to be measured before doing anything with the minimap
        recyclerView.addLayoutChangeListenerHandler {
            updateScaleFactor(this)
        }

        recyclerView.addScrollListener { dx, dy, isSettlingByItself ->
            if (this@MinimapView.visibility == View.VISIBLE) moveIndicator(dx, dy, isSettlingByItself)
        }
    }

    private fun updateScaleFactor(rv: RecyclerView) {
        scrollWidth = rv.computeHorizontalScrollRange().toFloat()
        scrollHeight = rv.computeVerticalScrollRange().toFloat()

        if (rv.updateVisibility()) {

            // Scrollable height might be < than the scrollable width while scrollable width being < than total height of the RecyclerView
            val biggerWidth = maxOf(scrollWidth, rv.width.toFloat()) // + rv.paddingRight + rv.paddingLeft
            val biggerHeight = maxOf(scrollHeight, rv.height.toFloat()) //+ rv.paddingTop + rv.paddingBottom

            // So, when calculating scaleFactor, we need the bigger size to fit into the maxSize of the view
            when {
                biggerWidth >= biggerHeight -> {
                    scaleFactor = biggerWidth / maxSize
                    calculatedWidth = maxSize
                    calculatedHeight = biggerHeight / scaleFactor
                }
                biggerWidth < biggerHeight -> {
                    scaleFactor = biggerHeight / maxSize
                    calculatedHeight = maxSize
                    calculatedWidth = biggerWidth / scaleFactor
                }
            }

            if (scaleFactor != 0f) {
                indicatorWidth = rv.width / scaleFactor
                indicatorHeight = rv.height / scaleFactor
            }

            requestLayout()
        }
    }

    private fun View.updateVisibility() = if (this.shouldBeVisible()) {
        visibility = this.visibility
        this.visibility == View.VISIBLE
    } else {
        visibility = View.GONE
        false
    }

    private fun View?.shouldBeVisible() = this != null && (scrollWidth > this.width || scrollHeight > this.height)

    private var settlingDx = 0.0
    private var settlingDy = 0.0
    private fun moveIndicator(dx: Int, dy: Int, isSettling: Boolean) = if (scaleFactor != 0f) {
        // TODO make this avoidBouncing feature default value as true, don't check this stopMoving if it's false
        // If the scrolling is settling, and the d sign is different than the previous value settlingD, the scrolling changed direction out of the user control, so don't move
        val stopMovingX = isSettling && (settlingDx<0 == dx<0 || settlingDx>0 == dx>0)
        val stopMovingY = isSettling && (settlingDy<0 == dy<0 || settlingDy>0 == dy>0)
        if (!stopMovingX) indicatorX += dx / scaleFactor
        if (!stopMovingY) indicatorY += dy / scaleFactor
        if (isSettling) { // Updating settlingX and settlingY signs
            settlingDx = dx.toDouble()
            settlingDy = dy.toDouble()
        }
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
        val maxHorizontalPosition = calculatedWidth - indicatorWidth
        when {
            indicatorX < 0f -> indicatorX = 0f
            indicatorX > maxHorizontalPosition -> indicatorX = maxHorizontalPosition
        }

        val maxVerticalPosition = calculatedHeight - indicatorHeight
        when {
            indicatorY < 0f -> indicatorY = 0f
            indicatorY > maxVerticalPosition -> indicatorY = maxVerticalPosition
        }
    }

    private fun drawBackground(canvas: Canvas?) {
        canvas?.drawRoundRect(
            0f + borderWidth / 2,
            0f + borderWidth / 2,
            calculatedWidth + borderWidth / 2,
            calculatedHeight + borderWidth / 2,
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
        setMeasuredDimension(
            calculatedWidth.toInt() + borderWidth.toInt(),
            calculatedHeight.toInt() + borderWidth.toInt()
        )
    }

}