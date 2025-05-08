package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

class StepIntake : View {
    private var thumbPaint: Paint? = null
    private var fillPaint: Paint? = null
    private var backgroundPaint: Paint? = null
    private var capillaryRect: RectF? = null
    private var fillPercentage = 0f
    private var intervalColors = IntArray(10) { Color.RED } // 10 intervals for 0-20k (0-2k, 2-4k, ..., 18-20k)
    private var backgroundColor = Color.LTGRAY

    private var minSteps = 0
    private var maxSteps = 20000
    private val stepInterval = 2000
    private var listener: OnStepCountChangeListener? = null

    private var lineThickness = 0f
    private var lineGap = 0f
    private var touchAreaHeight = 0f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        fillPaint = Paint().apply {
            style = Paint.Style.FILL
        }

        backgroundPaint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }

        capillaryRect = RectF()

        thumbPaint = Paint().apply {
            color = Color.WHITE // White lines
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        // Define line properties
        val density = resources.displayMetrics.density
        lineThickness = 2 * density // 2dp thick
        lineGap = 4 * density // 4dp gap between lines
        touchAreaHeight = (lineThickness * 2 + lineGap) * 1.5f // Reduced multiplier for better touch sensitivity
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = w / 10
        capillaryRect!![padding.toFloat(), padding.toFloat(), (w - padding).toFloat()] =
            (h - padding).toFloat()
        createGradientShader()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw rounded slider background
        canvas.drawRoundRect(
            capillaryRect!!,
            capillaryRect!!.width() / 4,
            capillaryRect!!.width() / 4,
            backgroundPaint!!
        )

        // Draw filled region
        val fillHeight = fillPercentage * capillaryRect!!.height()
        val fillRect = RectF(
            capillaryRect!!.left,
            capillaryRect!!.bottom - fillHeight,
            capillaryRect!!.right,
            capillaryRect!!.bottom
        )
        canvas.drawRoundRect(
            fillRect,
            capillaryRect!!.width() / 4,
            capillaryRect!!.width() / 4,
            fillPaint!!
        )

        // Draw double line indicator at the top of the green fill
        thumbPaint!!.strokeWidth = lineThickness
        val lineY1 = fillRect.top - lineGap / 2 - lineThickness / 2
        val lineY2 = fillRect.top + lineGap / 2 + lineThickness / 2
        // Top line
        canvas.drawLine(
            capillaryRect!!.left,
            lineY1,
            capillaryRect!!.right,
            lineY1,
            thumbPaint!!
        )
        // Bottom line
        canvas.drawLine(
            capillaryRect!!.left,
            lineY2,
            capillaryRect!!.right,
            lineY2,
            thumbPaint!!
        )
    }

    private fun createGradientShader() {
        val positions = FloatArray(intervalColors.size + 1)
        for (i in positions.indices) {
            positions[i] = i.toFloat() / intervalColors.size
        }
        val extendedColors = IntArray(intervalColors.size + 1)
        System.arraycopy(intervalColors, 0, extendedColors, 0, intervalColors.size)
        extendedColors[intervalColors.size] = intervalColors[intervalColors.size - 1]
        val gradient = LinearGradient(
            0f,
            capillaryRect!!.top,
            0f,
            capillaryRect!!.bottom,
            extendedColors,
            positions,
            Shader.TileMode.CLAMP
        )
        fillPaint!!.shader = gradient
    }

    private fun calculateYFromSteps(steps: Int): Float {
        val percentage = (steps - minSteps).toFloat() / (maxSteps - minSteps)
        return capillaryRect!!.bottom - percentage * capillaryRect!!.height()
    }

    private fun calculateStepsFromY(y: Float): Int {
        val percentage = (capillaryRect!!.bottom - y) / capillaryRect!!.height()
        val steps = (minSteps + percentage * (maxSteps - minSteps)).toInt()
        return (steps.toFloat() / stepInterval).toInt() * stepInterval
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // Allow dragging anywhere on the slider, not just the thumb
                val newY = max(
                    capillaryRect!!.top.toDouble(),
                    min(capillaryRect!!.bottom.toDouble(), event.y.toDouble())
                ).toFloat()
                val steps = calculateStepsFromY(newY)
                fillPercentage = (steps - minSteps).toFloat() / (maxSteps - minSteps)
                invalidate()
                updateStepCount(steps)
                return true
            }
            MotionEvent.ACTION_UP -> return true
        }
        return super.onTouchEvent(event)
    }

    private fun isThumbTouched(x: Float, y: Float): Boolean {
        val fillHeight = fillPercentage * capillaryRect!!.height()
        val fillTop = capillaryRect!!.bottom - fillHeight
        val touchTop = fillTop - (lineThickness + lineGap / 2)
        val touchBottom = fillTop + (lineThickness + lineGap / 2)
        return x >= capillaryRect!!.left &&
                x <= capillaryRect!!.right &&
                y >= touchTop - touchAreaHeight / 2 &&
                y <= touchBottom + touchAreaHeight / 2
    }

    fun setIntervalColors(color: Int) {
        intervalColors = IntArray((maxSteps - minSteps) / stepInterval) { color }
        createGradientShader()
        invalidate()
    }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        backgroundPaint!!.color = backgroundColor
        invalidate()
    }

    fun setFillPercentage(percentage: Float) {
        fillPercentage = max(0.0, min(1.0, percentage.toDouble())).toFloat()
        invalidate()
    }

    fun setMinSteps(minSteps: Int) {
        this.minSteps = minSteps
        intervalColors = IntArray((maxSteps - minSteps) / stepInterval) { Color.RED }
        createGradientShader()
        invalidate()
    }

    fun setMaxSteps(maxSteps: Int) {
        this.maxSteps = maxSteps
        intervalColors = IntArray((maxSteps - minSteps) / stepInterval) { Color.RED }
        createGradientShader()
        invalidate()
    }

    fun setOnStepCountChangeListener(listener: OnStepCountChangeListener?) {
        this.listener = listener
    }

    private fun updateStepCount(stepCount: Int) {
        Log.d("StepIntake", "Step count updated: $stepCount")
        listener?.onStepCountChanged(stepCount)
    }

    interface OnStepCountChangeListener {
        fun onStepCountChanged(stepCount: Int)
    }
}