package com.jetsynthesys.rightlife.ai_package.ui.moveright.graphs

import android.content.Context
import android.graphics.*
import android.icu.text.SimpleDateFormat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.math.min

class ActivityFactorGraph : View {

    private val linePaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 2f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val fillPaint = Paint().apply {
        color = Color.argb(100, 255, 105, 180)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val markerPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 2f
        isAntiAlias = true
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    private val dotPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 24f
        isAntiAlias = true
    }

    private val valuePaint = Paint().apply {
        color = Color.WHITE
        textSize = 20f
        isAntiAlias = true
    }

    private val trendPaint = Paint().apply {
        textSize = 16f
        isAntiAlias = true
    }

    private val gridPaint = Paint().apply {
        color = Color.argb(50, 255, 255, 255)
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val avgPaint = Paint().apply {
        color = Color.RED
        textSize = 24f
        isAntiAlias = true
    }

    private var dataPoints = mutableListOf<Float>()
    private var capillaryRect: RectF? = null
    private var selectedIndex = -1
    private val minValue = 0f
    private val maxValue = 2.5f
    private val days = 180

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var dateRange: String? = null
    private var average: Float = 0f

    companion object {
        private val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
        private val highlightedIndices = listOf(30, 60, 90, 120, 150)
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        generateDataPoints()
        calculateDateRange()
    }

    private fun generateDataPoints() {
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.MAY, 1) // Start from May 1, 2025
        startDate = calendar.clone() as Calendar
        for (i in 0 until days) {
            val value = minValue + (maxValue - minValue) * Math.random().toFloat()
            dataPoints.add(value)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        endDate = calendar.clone() as Calendar
        // Match screenshot data points with trends, adjusted for May start
        dataPoints[0] = 1.71f // May 1
        dataPoints[30] = 1.51f // May 31
        dataPoints[60] = 1.38f // Jun 30
        dataPoints[90] = 1.44f // Jul 30
        dataPoints[120] = 1.48f // Aug 29
    }

    private fun calculateDateRange() {
        startDate?.let { start ->
            endDate?.let { end ->
                val sdf = SimpleDateFormat("d MMM", Locale.getDefault())
                dateRange = "1 May - 28 May, 2025" // Fixed to match screenshot
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = w / 10
        capillaryRect = RectF(
            padding.toFloat(),
            padding.toFloat() + 50f,
            (w - padding).toFloat(),
            (h - padding).toFloat()
        )
        average = if (dataPoints.isNotEmpty()) dataPoints.average().toFloat() else 0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        capillaryRect?.let { rect ->
            // Date range & average
            dateRange?.let {
                canvas.drawText(it, rect.left, rect.top - 20f, textPaint)
                canvas.drawText("Avg ${String.format("%.2f", average)}", rect.right - 100f, rect.top - 20f, avgPaint)
            }

            // Draw horizontal grid lines
            val gridStep = rect.height() / 5
            for (i in 0..5) {
                val y = rect.top + i * gridStep
                canvas.drawLine(rect.left, y, rect.right, y, gridPaint)
                val value = minValue + (5 - i) * (maxValue - minValue) / 5
                canvas.drawText(String.format("%.1f", value), rect.left - 30f, y + 5f, textPaint)
            }

            // X-axis months
            val xStep = rect.width() / 5
            for (i in months.indices) {
                val x = rect.left + i * xStep
                canvas.drawText(months[i], x, rect.bottom + 20f, textPaint)
                // Draw year under each month
                canvas.drawText("2025", x, rect.bottom + 40f, textPaint.apply { textSize = 16f })
            }

            // Line graph
            val path = Path()
            val stepX = rect.width() / (days - 1)
            if (dataPoints.isNotEmpty()) {
                path.moveTo(rect.left, rect.bottom - (dataPoints[0] - minValue) * (rect.height() / (maxValue - minValue)))
                for (i in 1 until dataPoints.size) {
                    val x = rect.left + i * stepX
                    val y = rect.bottom - (dataPoints[i] - minValue) * (rect.height() / (maxValue - minValue))
                    path.lineTo(x, y)

                    if (i in highlightedIndices) {
                        canvas.drawText(String.format("%.2f", dataPoints[i]), x - 20f, y - 20f, valuePaint)
                        val prev = dataPoints.getOrElse(i - 1) { dataPoints[i] }
                        val change = ((dataPoints[i] - prev) / prev * 100).toFloat()
                        trendPaint.color = if (change >= 0) Color.GREEN else Color.RED
                        canvas.drawText(String.format("%+.2f%%", change), x - 20f, y - 40f, trendPaint)
                    }
                }

                // Draw line and fill
                canvas.drawPath(path, linePaint)
                val fillPath = Path(path).apply {
                    lineTo(rect.right, rect.bottom)
                    lineTo(rect.left, rect.bottom)
                    close()
                }
                canvas.drawPath(fillPath, fillPaint)
            }

            // Marker on selected index
            if (selectedIndex in 0 until dataPoints.size) {
                val markerX = rect.left + selectedIndex * stepX
                val y = rect.bottom - (dataPoints[selectedIndex] - minValue) * (rect.height() / (maxValue - minValue))
                canvas.drawLine(markerX, rect.top, markerX, rect.bottom, markerPaint)
                canvas.drawCircle(markerX, y, 5f, dotPaint)
                canvas.drawText(String.format("%.2f", dataPoints[selectedIndex]), markerX - 20f, y - 15f, valuePaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        capillaryRect?.let { rect ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val touchX = event.x
                    val index = ((touchX - rect.left) / (rect.width() / (days - 1))).toInt().coerceIn(0, days - 1)
                    if (index != selectedIndex) {
                        selectedIndex = index
                        invalidate()
                    }
                    return true
                }
                MotionEvent.ACTION_UP -> return true
                else -> {}
            }
        }
        return super.onTouchEvent(event)
    }

    fun setDataPoints(points: List<Float>) {
        dataPoints.clear()
        dataPoints.addAll(points)
        invalidate()
    }

    fun getAverage(): Float {
        return if (dataPoints.isNotEmpty()) dataPoints.average().toFloat() else 0f
    }
}