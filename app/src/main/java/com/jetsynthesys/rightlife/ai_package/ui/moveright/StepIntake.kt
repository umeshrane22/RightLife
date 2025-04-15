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
import kotlin.math.pow
import kotlin.math.sqrt

class StepIntake: View {
    private var thumbPaint: Paint? = null
    private var fillPaint: Paint? = null
    private var backgroundPaint: Paint? = null
    private var capillaryRect: RectF? = null
    private var fillPercentage = 0f
    private var intervalColors = intArrayOf(
        Color.RED,  // 0-2k
        Color.RED,  // 2-4k
        Color.RED,  // 4-6k
        Color.RED,  // 6-8k
        Color.RED,  // 8-10k
        Color.RED,  // 10-12k
    )
    private var backgroundColor = Color.LTGRAY

    private var minSteps = 0
    private var maxSteps = 12000
    private val stepInterval = 2000
    private var listener: OnStepCountChangeListener? = null

    private var thumbRadius = 0f
    private var thumbY = 0f

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
        fillPaint = Paint()
        fillPaint!!.style = Paint.Style.FILL

        backgroundPaint = Paint()
        backgroundPaint!!.color = backgroundColor
        backgroundPaint!!.style = Paint.Style.FILL

        capillaryRect = RectF()

        thumbPaint = Paint()
        thumbPaint!!.color = Color.WHITE // Always white thumb
        thumbPaint!!.style = Paint.Style.FILL
        thumbPaint!!.isAntiAlias = true // Smooth edges
        thumbRadius = resources.displayMetrics.density * 15 // Adjust thumb size
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = w / 10
        capillaryRect!![padding.toFloat(), padding.toFloat(), (w - padding).toFloat()] =
            (h - padding).toFloat()
        thumbY = calculateYFromSteps(0) // Initial thumb position at 2k steps
        createGradientShader()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw rounded slider
        canvas.drawRoundRect(
            capillaryRect!!, capillaryRect!!.width() / 4, capillaryRect!!.width() / 4,
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
            fillRect, capillaryRect!!.width() / 4, capillaryRect!!.width() / 4,
            fillPaint!!
        )

        // Draw thumb
        canvas.drawCircle(capillaryRect!!.centerX(), thumbY, thumbRadius, thumbPaint!!)
    }

    private fun createGradientShader() {
        val positions = FloatArray(intervalColors.size + 1)
        for (i in positions.indices) {
            positions[i] = i.toFloat() / intervalColors.size
        }
        val extendedColors = IntArray(intervalColors.size + 1)
        System.arraycopy(intervalColors, 0, extendedColors, 0, intervalColors.size)
        extendedColors[intervalColors.size] =
            intervalColors[intervalColors.size - 1] // Repeat last color
        val gradient = LinearGradient(
            0f, capillaryRect!!.top,
            0f, capillaryRect!!.bottom,
            extendedColors,
            positions,
            Shader.TileMode.CLAMP
        )
        fillPaint!!.setShader(gradient)
    }

    private fun calculateYFromSteps(steps: Int): Float {
        val percentage = (steps - minSteps).toFloat() / (maxSteps - minSteps)
        return capillaryRect!!.bottom - percentage * capillaryRect!!.height()
    }

    private fun calculateStepsFromY(y: Float): Int {
        val percentage = (capillaryRect!!.bottom - y) / capillaryRect!!.height()
        val steps = (minSteps + percentage * (maxSteps - minSteps)).toInt()
        return Math.round(steps.toFloat() / stepInterval) * stepInterval
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> if (isThumbTouched(
                    event.x,
                    event.y
                ) || event.action == MotionEvent.ACTION_MOVE
            ) {
                // Ensure the thumb stays within the vertical limits of the view
                val newY = max(
                    (capillaryRect!!.top + thumbRadius).toDouble(),
                    min((capillaryRect!!.bottom - thumbRadius).toDouble(), event.y.toDouble())
                ).toFloat()

                val steps = calculateStepsFromY(newY)
                val oldThumbY = thumbY // Store the old thumbY value
                thumbY = newY // Assign constrained Y value
                fillPercentage = (steps - minSteps).toFloat() / (maxSteps - minSteps)
                invalidate()

                // Log the new thumb position only if it has changed
                if (oldThumbY != thumbY) {
                    Log.d(
                        "CapillaryView",
                        "Thumb moved from Y: $oldThumbY to Y: $thumbY"
                    )
                    updateStepCount(steps)
                }

                return true
            }

            MotionEvent.ACTION_UP -> return isThumbTouched(event.x, event.y)
        }
        return super.onTouchEvent(event)
    }

    /*   @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (isThumbTouched(event.getX(), event.getY()) || event.getAction() == MotionEvent.ACTION_MOVE) {
                    float newY = Math.max(capillaryRect.top, Math.min(capillaryRect.bottom, event.getY()));
                    int steps = calculateStepsFromY(newY);
                    float oldThumbY = thumbY; // Store the old thumbY value
                    thumbY = calculateYFromSteps(steps);
                    fillPercentage = (float) (steps - minSteps) / (maxSteps - minSteps);
                    invalidate();
                    // Log the new thumb position only if it has changed
                    if (oldThumbY != thumbY) {
                        Log.d("CapillaryView", "Thumb moved from Y: " + oldThumbY + " to Y: " + thumbY);
                        updateStepCount(steps);
                    }


                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                return isThumbTouched(event.getX(), event.getY());
        }
        return super.onTouchEvent(event);
    }*/
    private fun isThumbTouched(x: Float, y: Float): Boolean {
        return sqrt(
            (x - capillaryRect!!.centerX()).toDouble().pow(2.0) + (y - thumbY).toDouble().pow(2.0)
        ) <= thumbRadius
    }

    fun setIntervalColors(color: Int) {
        intervalColors = intArrayOf(color, color, color, color, color, color)
        createGradientShader()
        invalidate()
        /*if (colors.length == 6) {
            intervalColors = colors;
            createGradientShader();
            invalidate();
        } else {
            throw new IllegalArgumentException("Exactly 6 colors are required.");
        }*/
    }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        backgroundPaint!!.color = backgroundColor
        invalidate()
    }

    fun setFillPercentage(percentage: Float) {
        fillPercentage = max(0.0, min(1.0, percentage.toDouble())).toFloat()
        val steps = (minSteps + (maxSteps - minSteps) * fillPercentage).toInt()
        thumbY = calculateYFromSteps(steps)
        invalidate()
        //updateStepCount(steps);
    }

    fun setMinSteps(minSteps: Int) {
        this.minSteps = minSteps
        thumbY = calculateYFromSteps(minSteps)
        invalidate()
        //updateStepCount(minSteps);
    }

    fun setMaxSteps(maxSteps: Int) {
        this.maxSteps = maxSteps
        createGradientShader()
        invalidate()
    }

    fun setOnStepCountChangeListener(listener: OnStepCountChangeListener?) {
        this.listener = listener
    }

    private fun updateStepCount(stepCount: Int) {
        if (listener != null) {
            listener!!.onStepCountChanged(stepCount)
        }
    }

    interface OnStepCountChangeListener {
        fun onStepCountChanged(stepCount: Int)
    }
}