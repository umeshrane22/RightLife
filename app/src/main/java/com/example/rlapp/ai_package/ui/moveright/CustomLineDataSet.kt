package com.example.rlapp.ai_package.ui.moveright

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import java.text.SimpleDateFormat
import java.util.Locale


class CustomHeartRateGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val dataPoints = mutableListOf<HRDataPoint>()
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }
    private val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 36f
    }
    private val path = Path()
    private val minBpm = 70f
    private val maxBpm = 200f
    private val bpmRange = maxBpm - minBpm
    private val labelCountY = 5
    private val granularityX = 10f
    private val labelCountX = 6
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    fun setData(points: List<HRDataPoint>) {
        dataPoints.clear()
        dataPoints.addAll(points)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (dataPoints.isEmpty()) return

        val paddingLeft = 120f
        val paddingBottom = 80f
        val paddingTop = 20f
        val paddingRight = 20f

        val graphWidth = width - paddingLeft - paddingRight
        val graphHeight = height - paddingTop - paddingBottom
        canvas.drawLine(paddingLeft, paddingTop, paddingLeft, height - paddingBottom, axisPaint)
        canvas.drawLine(paddingLeft, height - paddingBottom, width - paddingRight, height - paddingBottom, axisPaint)
        val bpmStep = bpmRange / (labelCountY - 1)
        for (i in 0 until labelCountY) {
            val bpm = minBpm + i * bpmStep
            val y = height - paddingBottom - (bpm - minBpm) / bpmRange * graphHeight
            canvas.drawLine(paddingLeft, y, width - paddingRight, y, gridPaint)
            val label = bpm.toInt().toString()
            val labelWidth = textPaint.measureText(label)
            canvas.drawText(label, paddingLeft - labelWidth - 10f, y + textPaint.textSize / 3, textPaint)
        }
        val totalPoints = dataPoints.size
        val xStep = totalPoints / (labelCountX - 1)
        for (i in 0 until labelCountX) {
            val index = (i * xStep).coerceAtMost(totalPoints - 1)
            if (index % granularityX.toInt() != 0) continue
            val x = paddingLeft + (index.toFloat() / (totalPoints - 1)) * graphWidth
            val time = timeFormat.format(dataPoints[index].time)
            val labelWidth = textPaint.measureText(time)
            canvas.drawText(time, x - labelWidth / 2, height - paddingBottom + textPaint.textSize + 10f, textPaint)
        }
        drawHeartRateLine(canvas, paddingLeft, paddingTop, graphWidth, graphHeight)
    }

    private fun drawHeartRateLine(canvas: Canvas, paddingLeft: Float, paddingTop: Float, graphWidth: Float, graphHeight: Float) {
        val totalPoints = dataPoints.size
        if (totalPoints < 2) return
        for (i in 0 until totalPoints - 1) {
            val startPoint = dataPoints[i]
            val endPoint = dataPoints[i + 1]
            val startX = paddingLeft + (i.toFloat() / (totalPoints - 1)) * graphWidth
            val startY = height - paddingTop - ((startPoint.bpm - minBpm) / bpmRange) * graphHeight
            val endX = paddingLeft + ((i + 1).toFloat() / (totalPoints - 1)) * graphWidth
            val endY = height - paddingTop - ((endPoint.bpm - minBpm) / bpmRange) * graphHeight
            val avgBpm = (startPoint.bpm + endPoint.bpm) / 2f
            val color = when {
                avgBpm <= 118 -> Color.BLUE
                avgBpm in 121.0..134.0 -> Color.GREEN
                avgBpm in 134.1..155.0 -> Color.YELLOW
                avgBpm > 155 -> Color.RED
                else -> Color.BLUE
            }
            linePaint.color = color
            path.reset()
            path.moveTo(startX, startY)
            path.lineTo(endX, endY)
            canvas.drawPath(path, linePaint)
        }
    }
}