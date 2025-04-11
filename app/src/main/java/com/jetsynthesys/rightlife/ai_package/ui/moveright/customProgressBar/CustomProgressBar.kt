package com.jetsynthesys.rightlife.ai_package.ui.moveright.customProgressBar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CustomProgressBar(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val progressBarBackgroundPaint = Paint().apply {
        color = Color.parseColor("#F5F5F5") // Light Gray Background
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val progressPaint = Paint().apply {
        color = Color.parseColor("#FF5C5C") // Progress color
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 20f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }

    private val indicatorPaint = Paint().apply {
        color = Color.parseColor("#FF5C5C") // Red indicator
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
        val barHeight = 30f
        val startX = padding
        val endX = width - padding
        val centerY = height / 2f + 20f

        // Draw Background Bar
        val backgroundRect = RectF(startX, centerY - barHeight / 2, endX, centerY + barHeight / 2)
        canvas.drawRoundRect(backgroundRect, barHeight / 2, barHeight / 2, progressBarBackgroundPaint)

        // Draw Progress Indicator (Big Red Circle)
        val progressX = startX + (endX - startX) * progress
        canvas.drawCircle(progressX, centerY, 25f, indicatorPaint)

        // Labels and Indicator Dots
        val textOffsetX = 10f

        for (i in intensityLabels.indices) {
            val indicatorX = startX + i * (endX - startX) / intensitySteps
            val textX = when (i) {
                0 -> indicatorX + textOffsetX
                intensityLabels.lastIndex -> indicatorX - textOffsetX
                else -> indicatorX
            }

            canvas.drawCircle(indicatorX, centerY, 8f, indicatorPaint)
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
                progress = snapToIntensity(newProgress)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun snapToIntensity(newProgress: Float): Float {
        val stepSize = 1.0f / intensitySteps
        val snappedStep = (newProgress / stepSize).toInt().coerceIn(0, intensitySteps)
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