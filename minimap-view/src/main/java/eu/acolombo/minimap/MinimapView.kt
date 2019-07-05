package eu.acolombo.minimap

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView

class MinimapView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private val a: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MinimapView)

    var maxSize = a.getDimension(R.styleable.MinimapView_minimapMaxSize, 0f)
        set(value) {
            field = value
            currentScrollableView?.requestLayout()
        }
    var cornerRadius = a.getDimension(R.styleable.MinimapView_minimapCornerRadius, 0f)
        set(value) {
            field = value
            invalidate()
        }
    var mapBackgroundColor = a.getColor(R.styleable.MinimapView_minimapBackgroundColor, Color.GRAY)
        set(value) {
            field = value
            backgroundPaint.color = value
            invalidate()
        }
    var indicatorColor = a.getColor(R.styleable.MinimapView_minimapIndicatorColor, Color.WHITE)
        set(value) {
            field = value
            indicatorPaint.color = value
            invalidate()
        }
    var borderWidth = a.getDimension(R.styleable.MinimapView_minimapBorderWidth, 0f)
        set(value) {
            field = value
            indicatorPaint.strokeWidth = value
            invalidate()
        }

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

    private var currentScrollableView : View ? = null

    fun setRecyclerView(recyclerView: RecyclerView) {
        // Wait for recyclerView to be measured before doing anything with the minimap
        recyclerView.addLayoutChangeListener { recyclerView.doOnPreDraw { updateScaleFactor(recyclerView) } }

        recyclerView.addScrollListener { dx, dy -> if (visibility == VISIBLE) moveIndicator(dx, dy) }

        currentScrollableView = recyclerView
    }

    private fun updateScaleFactor(rv: RecyclerView) {
        scrollWidth = rv.computeHorizontalScrollRange().toFloat()
        scrollHeight = rv.computeVerticalScrollRange().toFloat()

        if (updateMapVisibility(rv)) {

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

    private fun updateMapVisibility(scrollableView: View): Boolean {
        if (!scrollableView.isBigger()) visibility = GONE
        return visibility == VISIBLE
    }

    private fun View?.isBigger() = this != null && (scrollWidth > this.width || scrollHeight > this.height)

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

var RecyclerView.minimap: MinimapView
    get() = MinimapView(context).apply { setRecyclerView(this@minimap) }
    set(value) = value.setRecyclerView(this@minimap)