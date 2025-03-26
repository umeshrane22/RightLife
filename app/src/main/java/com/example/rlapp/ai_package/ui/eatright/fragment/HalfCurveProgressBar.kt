package com.example.rlapp.ai_package.ui.eatright.fragment

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.rlapp.R

class HalfCurveProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = Color.LTGRAY // Background arc color
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = resources.getColor(R.color.border_green) // Green progress arc color
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaintMain = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = 70f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val textPaintSecondary = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = 30f
        color = Color.LTGRAY
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = 18f
        color = resources.getColor(R.color.border_green)
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val rectF = RectF()

    // Progress values
    private var currentValue: Int = 2285
    private var maxValue: Int = 2000
    private var progress: Float = 0f
    private var animatedProgress: Float = 0f

    // Arc dimensions
    private val startAngle = 150f
    private val sweepAngleMax = 240f

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.HalfCurveProgressBar, defStyleAttr, 0)
        currentValue = typedArray.getInt(R.styleable.HalfCurveProgressBar_currentValue, 2285)
        maxValue = typedArray.getInt(R.styleable.HalfCurveProgressBar_maxValue, 2000)
        backgroundPaint.color =
            typedArray.getColor(R.styleable.HalfCurveProgressBar_backgroundColor, Color.LTGRAY)
        progressPaint.color = typedArray.getColor(
            R.styleable.HalfCurveProgressBar_progressColor,
            resources.getColor(R.color.border_green)
        )
        backgroundPaint.strokeWidth =
            typedArray.getDimension(R.styleable.HalfCurveProgressBar_strokeWidth, 20f)
        progressPaint.strokeWidth =
            typedArray.getDimension(R.styleable.HalfCurveProgressBar_strokeWidth, 20f)
        textPaintMain.textSize =
            typedArray.getDimension(R.styleable.HalfCurveProgressBar_textSizeMain, 70f)
        typedArray.recycle()

        updateProgress()
    }

    fun setValues(current: Int, max: Int) {
        currentValue = current
        maxValue = max
        updateProgress()
        invalidate()
    }

    fun setProgress(progress: Float) {
        animatedProgress = progress.coerceIn(0f, this.progress)
        invalidate()
    }

    private fun updateProgress() {
        progress = if (maxValue > 0) {
            (currentValue.toFloat() / maxValue.toFloat() * 100f).coerceIn(0f, 100f)
        } else {
            0f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val density = resources.displayMetrics.density
        val desiredWidth = (300 * density).toInt() // Default width
        val desiredHeight = (150 * density).toInt() // Fixed height of 150dp

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2f
        val paddingTop = 20f // Adjust this value to set padding from the top
        val centerY = (height / 2f) + paddingTop // Move the progress bar down

        val radius = (width.coerceAtMost(height) / 2f) - 40f

        rectF.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        canvas.drawArc(rectF, startAngle, sweepAngleMax, false, backgroundPaint)

        val sweepAngle = (animatedProgress / 100f) * sweepAngleMax
        canvas.drawArc(rectF, startAngle, sweepAngle, false, progressPaint)

        drawMainText(canvas, centerX, centerY)
        drawLabel(canvas, centerX, centerY + 40f + 30f)
    }

    private fun drawMainText(canvas: Canvas, centerX: Float, centerY: Float) {
        val textY = centerY - 20f
        canvas.drawText("$currentValue", centerX, textY, textPaintMain)
        canvas.drawText("/ $maxValue KCal", centerX, textY + 40f, textPaintSecondary)
    }

    private fun drawLabel(canvas: Canvas, centerX: Float, yPosition: Float) {
        canvas.drawText("Calories", centerX, yPosition, labelPaint)
    }
}
