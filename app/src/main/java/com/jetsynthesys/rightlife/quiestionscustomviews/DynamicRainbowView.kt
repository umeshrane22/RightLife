package com.jetsynthesys.rightlife.quiestionscustomviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class DynamicRainbowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private var colors: IntArray = intArrayOf(
        0xFFFF0000.toInt(), 0xFFFFC107.toInt(),
        0xFF2196F3.toInt(), 0xFF4CAF50.toInt()
    )

    var startAngle = 180f
    var sweepAngle = 180f

    private var centerX = 0f
    private var centerY = 0f
    private var minInnerRadius = 100f // ✅ innermost arc's fixed radius

    private var arcSpacing = 12f
    private var strokeWidth = 30f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (colors.isEmpty()) return

        val rectF = RectF()

        for (i in colors.indices) {
            paint.color = colors[i]
            paint.strokeWidth = strokeWidth

            val radius = minInnerRadius + i * (strokeWidth + arcSpacing)
            rectF.set(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius
            )

            canvas.drawArc(rectF, startAngle, sweepAngle, false, paint)
        }
    }

    fun setRainbowColors(newColors: IntArray) {
        if (newColors.isNotEmpty()) {
            colors = newColors
            invalidate()
        }
    }

    fun setStrokeWidth(width: Float) {
        strokeWidth = width
        invalidate()
    }

    fun setArcSpacing(spacing: Float) {
        arcSpacing = spacing
        invalidate()
    }

    fun setMinInnerRadius(radius: Float) {
        minInnerRadius = radius
        invalidate()
    }
}






/*class DynamicRainbowView : View {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 30f
        isAntiAlias = true
    }

    private var colors =
        intArrayOf(0xFFFF0000.toInt(), 0xFFFFC107.toInt(), 0xFF2196F3.toInt(), 0xFF4CAF50.toInt())
    private val fractions = floatArrayOf(0.25f, 0.5f, 0.75f, 1.0f)
    private val startAngle = 180f
    private val sweepAngle = 180f

    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var maxRadius: Float = 0f

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // You can extract custom attributes here if needed
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h.toFloat()  // Center at the bottom
        maxRadius = Math.min(w, h).toFloat() / 2f
        Log.d("DynamicRainbowView", "onSizeChanged: centerX=$centerX, centerY=$centerY, maxRadius=$maxRadius, width=$w, height=$h") //Added
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width
        val height = height

        // Calculate the bounding rectangle for the arcs
        val rectF = RectF()

        // Draw each arc with its corresponding color
        for (i in colors.indices) {
            paint.color = colors[i]
            val radius = maxRadius * fractions[i]
            rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

            canvas.drawArc(rectF, startAngle, sweepAngle, false, paint)
            Log.d("DynamicRainbowView", "onDraw: i=$i, radius=$radius, rectF=$rectF") // Added
        }
    }

    fun setRainbowColors(newColors: IntArray) {
        if (newColors.isNotEmpty() && newColors.size == colors.size) {
            colors = newColors
            invalidate()
        }
    }
}*/
