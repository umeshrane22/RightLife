package com.example.rlapp.ai_package.ui.thinkright.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PhqProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }

    private val scoreIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        color = Color.parseColor("#8B4513") // Brown color for the score indicator
        style = Paint.Style.FILL
    }

    // Define the segments and their properties
    private val segments = listOf(
        Segment(0f, 4f, Color.parseColor("#00FF00"), "Minimal"),  // Green
        Segment(4f, 9f, Color.parseColor("#FFFF00"), "Mild"),     // Yellow
        Segment(9f, 14f, Color.parseColor("#FFA500"), "Moderate"), // Orange
        Segment(14f, 19f, Color.parseColor("#FF0000"), "Severe"),  // Red
        Segment(19f, 27f, Color.parseColor("#8B0000"), "Ext. Severe") // Dark Red
    )

    private val scoreMarkers = listOf(0f, 4f, 9f, 14f, 19f, 27f)
    private var currentScore: Float = 7f // Default score (can be set dynamically)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val barHeight = 30f // Height of the progress bar
        val barTop = height * 0.4f // Position the bar vertically
        val barBottom = barTop + barHeight
        val maxScore = 27f // Maximum PHQ-9 score

        // Draw the segmented progress bar
        for (segment in segments) {
            val left = (segment.start / maxScore) * width
            val right = (segment.end / maxScore) * width
            paint.color = segment.color
            canvas.drawRect(left, barTop, right, barBottom, paint)
        }

        // Draw score markers and labels
        for (marker in scoreMarkers) {
            val x = (marker / maxScore) * width
            canvas.drawText(marker.toInt().toString(), x, barTop - 20f, textPaint)

            // Draw a small line for the marker
            paint.color = Color.BLACK
            canvas.drawRect(x - 2f, barTop, x + 2f, barBottom, paint)
        }

        // Draw segment labels
        for (segment in segments) {
            val centerX = ((segment.start + segment.end) / 2 / maxScore) * width
            canvas.drawText(segment.label, centerX, barBottom + 40f, textPaint)
        }

        // Draw the "Your Score" indicator
        val scoreX = (currentScore / maxScore) * width
        val indicatorRadius = 20f
        val indicatorTop = barTop - 60f
        val indicatorBottom = barTop

        // Draw the brown circle with the score
        scoreIndicatorPaint.color = Color.parseColor("#8B4513")
        canvas.drawCircle(scoreX, indicatorTop + indicatorRadius, indicatorRadius, scoreIndicatorPaint)

        // Draw the score text inside the circle
        textPaint.color = Color.WHITE
        textPaint.textSize = 24f
        canvas.drawText(currentScore.toInt().toString(), scoreX, indicatorTop + indicatorRadius + 8f, textPaint)

        // Draw the triangle pointer below the circle
        scoreIndicatorPaint.color = Color.parseColor("#8B4513")
        val path = android.graphics.Path().apply {
            moveTo(scoreX - 15f, indicatorTop + indicatorRadius * 2)
            lineTo(scoreX + 15f, indicatorTop + indicatorRadius * 2)
            lineTo(scoreX, indicatorBottom)
            close()
        }
        canvas.drawPath(path, scoreIndicatorPaint)
    }

    // Method to set the current score dynamically
    fun setScore(score: Float) {
        currentScore = score.coerceIn(0f, 27f) // Ensure score is between 0 and 27
        invalidate() // Redraw the view
    }

    private data class Segment(val start: Float, val end: Float, val color: Int, val label: String)
}