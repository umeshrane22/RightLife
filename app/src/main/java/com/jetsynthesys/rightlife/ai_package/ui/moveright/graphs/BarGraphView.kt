package com.jetsynthesys.rightlife.ai_package.ui.moveright.graphs

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class  BarGraphView(context: Context, attrs: android.util.AttributeSet? = null) : android.view.View(context, attrs) {
    private val barPaint = android.graphics.Paint().apply {
        color = 0xFFB11414.toInt()
        style = android.graphics.Paint.Style.FILL
        isAntiAlias = true
    }
    private var dataPoints: FloatArray = FloatArray(7) { 0f }
    private var spacing = 10f

    fun setDataPoints(points: FloatArray) {
        dataPoints = points
        invalidate()
    }

    fun setSpacing(spacing: Float) {
        this.spacing = spacing
        invalidate()
    }

    override fun onDraw(canvas: android.graphics.Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        val minValue = 0f
        val maxValue = dataPoints.maxOrNull() ?: 100f
        val scaleY = height / maxValue
        val totalSpacing = spacing * (dataPoints.size - 1)
        val availableWidth = width - totalSpacing
        val barWidth = availableWidth / dataPoints.size

        dataPoints.forEachIndexed { index, value ->
            val barHeight = value * scaleY
            val left = index * (barWidth + spacing)
            val top = height - barHeight
            val right = left + barWidth
            val bottom = height
            canvas.drawRect(left, top, right, bottom, barPaint)
        }
    }
}