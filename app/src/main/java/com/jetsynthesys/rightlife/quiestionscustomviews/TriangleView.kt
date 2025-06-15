package com.jetsynthesys.rightlife.quiestionscustomviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class TriangleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val baseTrianglePaint = Paint().apply {
        color = 0xFFE1FFCB.toInt()  // Light Green (Base Triangle)
        style = Paint.Style.FILL
    }

    private val progressTrianglePaint = Paint().apply {
        color = 0xFF66B12D.toInt()  // Green (Progress Triangle)
        style = Paint.Style.FILL
    }

    private val dotPaint = Paint().apply {
        color = Color.BLACK  // Dots Color
        style = Paint.Style.FILL
    }

    private var thumbPaint: Paint

    private val verticalLinePaint = Paint().apply {
        color = Color.BLACK  // Vertical line color
        strokeWidth = 6f
    }

    private var progressRatio: Float = 0f // Will be set dynamically
    private var topDotPosition: Pair<Float, Float>? = null // Single top dot
    private var bottomDotPositions: List<Pair<Float, Float>> = emptyList() // Bottom dots
    private val dotRadius = 10f // Dot size

    init {
        thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        thumbPaint.color = Color.WHITE
        thumbPaint.style = Paint.Style.FILL
        thumbPaint.setShadowLayer(10f, 0f, 0f, Color.GRAY)
        setLayerType(LAYER_TYPE_SOFTWARE, thumbPaint)
    }

    fun setProgress(progress: Int, maxProgress: Int) {
        progressRatio = progress.toFloat() / maxProgress

        // Change base triangle color dynamically based on progress
        baseTrianglePaint.color = when (progress) {
            0 -> 0xFFE1FFCB.toInt()  // Light Green
            1 -> 0xFFE1FFCB.toInt()  // Light Green
            2 -> 0xFFFFFFC4.toInt()  // Light Yellow
            3 -> 0xFFFFEED5.toInt()  // Light Orange
            4 -> 0xFFFC2611.toInt()  // Light Orange
            else -> 0xFFFC2611.toInt() // Red (Same for last case)
        }

        // Change progress triangle color dynamically based on progress
        progressTrianglePaint.color = when (progress) {
            0 -> 0xFF66B12D.toInt()  // Green
            1 -> 0xFF66B12D.toInt()  // Green
            2 -> 0xFFFFFE31.toInt()  // Yellow
            3 -> 0xFFFB9900.toInt()  // Orange
            4 -> 0xFFFC2611.toInt()  // Orange
            else -> 0xFFFC2611.toInt() // Red (Same for last case)
        }

        // Precompute dot positions
        calculateDotPositions()

        //invalidate()  // Redraw the view with updated progress
        postInvalidate()
    }

    private fun calculateDotPositions() {
        val width = width.toFloat()
        val height = height.toFloat()

        // Guard against unmeasured (zero) view size
        if (width == 0f || height == 0f) return

        val padding = dotRadius

        // Ensure valid bounds for coerceIn
        val minX = padding
        val maxX = width - padding
        val minY = padding
        val maxY = height - padding

        val rawTopDotX = width * progressRatio
        val rawTopDotY = height - (height * progressRatio)

        val topDotX = rawTopDotX.coerceIn(minX, maxX)
        val topDotY = rawTopDotY.coerceIn(minY, maxY)

        topDotPosition = Pair(topDotX, topDotY)

        // Bottom dots: ensure safe clamping
        bottomDotPositions = (1..4).map { i ->
            val ratio = i / 4f
            val rawX = width * ratio
            val dotX = rawX.coerceIn(minX, maxX)
            val dotY = height - dotRadius - 2f
            Pair(dotX, dotY)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // **Draw Base Triangle (Full Background)**
        val baseTrianglePath = Path().apply {
            moveTo(0f, height)   // Bottom-left corner
            lineTo(width, height)  // Bottom-right corner
            lineTo(width, 0f)  // Top-right corner
            close()
        }
        canvas.drawPath(baseTrianglePath, baseTrianglePaint)

        // **Draw Dynamic Progress Triangle**
        val progressTrianglePath = Path().apply {
            moveTo(0f, height) // Bottom-left corner
            lineTo(width * progressRatio, height) // Adjusted bottom-right corner
            lineTo(width * progressRatio, height - (height * progressRatio)) // Top-right corner based on progress
            close()
        }
        canvas.drawPath(progressTrianglePath, progressTrianglePaint)

        // **Draw fixed vertical line at 100% height**
        canvas.drawLine(0f, height, 0f, 0f, verticalLinePaint)

        // **Draw the single top dot inside the progress triangle**
        topDotPosition?.let { (x, y) ->
            canvas.drawCircle(x, y, dotRadius, thumbPaint)
        }

        // **Draw bottom dots along the base (Fixed Half-Cut Issue)**
        bottomDotPositions.forEach { (x, y) ->
            canvas.drawCircle(x, y, dotRadius, dotPaint)
        }
    }
}