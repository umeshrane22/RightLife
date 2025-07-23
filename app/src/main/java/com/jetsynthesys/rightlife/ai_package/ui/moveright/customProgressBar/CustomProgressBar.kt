package com.jetsynthesys.rightlife.ai_package.ui.moveright.customProgressBar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.roundToInt

class CustomProgressBar(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val progressBarBackgroundPaint = Paint().apply {
        color = Color.parseColor("#F5F5F5")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val progressPaint = Paint().apply {
        color = Color.parseColor("#FF5C5C")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 25f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }

    private val indicatorPaint = Paint().apply {
        color = Color.parseColor("#FF5C5C")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    var progress: Float = 0.0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            invalidate()
            onProgressChanged?.invoke(field)
        }

    private var onProgressChanged: ((Float) -> Unit)? = null

    private val intensityLabels = arrayOf("Low", "Moderate", "High", "Very High")
    private val intensitySteps = intensityLabels.size - 1

    init {
        isClickable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = 40f
        val startX = padding
        val endX = width - padding
        val centerY = height / 2f + 20f

        // Heights
        val startHeight = 30f
        val endHeight = 60f

        // Rounded corner radius
        val radiusLeft = startHeight / 2
        val radiusRight = endHeight / 2

        // Top and bottom positions
        val topLeft = centerY - startHeight / 2
        val bottomLeft = centerY + startHeight / 2
        val topRight = centerY - endHeight / 2
        val bottomRight = centerY + endHeight / 2

        // Gray trapezoid background bar with rounded ends
        val bgPath = Path().apply {
            moveTo(startX + radiusLeft, bottomLeft)
            lineTo(endX - radiusRight, bottomRight)
            arcTo(RectF(endX - 2 * radiusRight, topRight, endX, bottomRight), 90f, -180f)
            lineTo(startX + radiusLeft, topLeft)
            arcTo(RectF(startX, topLeft, startX + 2 * radiusLeft, bottomLeft), 270f, -180f)
            close()
        }

        canvas.drawPath(bgPath, progressBarBackgroundPaint)

        // Red circle indicator (fixed size, same as Moderate)
        val progressX = startX + (endX - startX) * progress
        val fixedIndicatorRadius = 28.33f // Fixed radius for Moderate (calculated at progress = 0.333)
        canvas.drawCircle(progressX, centerY, fixedIndicatorRadius, indicatorPaint)

        // Step dots & labels
        val textOffsetX = 10f
        val stepDotPaint = Paint().apply {
            color = Color.parseColor("#FF5C5C")
            style = Paint.Style.FILL
            alpha = 100
            isAntiAlias = true
        }

        val effectiveStartX = startX + radiusLeft
        val effectiveEndX = endX - radiusRight

        for (i in intensityLabels.indices) {
            val indicatorX = effectiveStartX + i * (effectiveEndX - effectiveStartX) / intensitySteps
            val textX = when (i) {
                0 -> indicatorX + textOffsetX
                intensityLabels.lastIndex -> indicatorX - textOffsetX
                else -> indicatorX
            }

            canvas.drawCircle(indicatorX, centerY, 8f, stepDotPaint)
            canvas.drawText(intensityLabels[i], textX, centerY - 40f, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val startX = 40f
                val endX = width - 40f
                val touchX = event.x.coerceIn(startX, endX)
                val newProgress = (touchX - startX) / (endX - startX)

                // Snap to nearest intensity step
                progress = snapToIntensity(newProgress)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun snapToIntensity(newProgress: Float): Float {
        val stepSize = 1.0f / intensitySteps
        val snappedStep = (newProgress / stepSize).roundToInt().coerceIn(0, intensitySteps)
        return snappedStep * stepSize
    }

    fun setOnProgressChangedListener(listener: (Float) -> Unit) {
        onProgressChanged = listener
    }

    fun getCurrentIntensity(): String {
        val stepSize = 1.0f / intensitySteps
        val currentStep = (progress / stepSize).toInt().coerceIn(0, intensitySteps)
        return intensityLabels[currentStep]
    }
}