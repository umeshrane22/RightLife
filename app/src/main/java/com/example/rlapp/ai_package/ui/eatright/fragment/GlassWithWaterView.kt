package com.example.rlapp.ai_package.ui.eatright.fragment

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class GlassWithWaterView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val glassPaint: Paint = Paint()
    private val waterPaint: Paint = Paint()
    private var waterHeight = 0f // This will be animated
    private val glassHeight = 500 // Height of the glass
    private val glassTopWidth = 350  // Width at the top of the glass
    private val glassBottomWidth = 200  // Width at the bottom of the glass

    init {
        // Initialize paint for glass
        glassPaint.color = Color.GRAY
        glassPaint.style = Paint.Style.STROKE
        glassPaint.strokeWidth = 10f

        // Initialize paint for water
        waterPaint.color = Color.BLUE
        waterPaint.style = Paint.Style.FILL

        // Start the water animation when the view is created
        startWaterAnimation()
    }

    // Method to start the water animation
     fun startWaterAnimation() {
        val animator = ValueAnimator.ofFloat(0f, glassHeight.toFloat())
        animator.duration = 3000 // Duration of the animation in milliseconds
        animator.addUpdateListener { animation ->
            waterHeight = animation.animatedValue as Float
            invalidate() // Redraw the view as the water height changes
        }
        animator.start()
    }

    // Override the onDraw method to draw the glass and water
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the glass (trapezoidal shape)
        val glassPath = Path()
        // Top-left
        glassPath.moveTo(50f, 100f)
        // Top-right
        glassPath.lineTo((50 + glassTopWidth).toFloat(), 100f)
        // Bottom-right
        glassPath.lineTo((50 + glassBottomWidth).toFloat(), (100 + glassHeight).toFloat())
        // Bottom-left
        glassPath.lineTo(50f, (100 + glassHeight).toFloat())
        glassPath.close()

        // Draw the glass outline
        canvas.drawPath(glassPath, glassPaint)

        // Calculate the width of the water at the current height
        val waterWidthAtTop = glassTopWidth - ((glassTopWidth - glassBottomWidth) * (waterHeight / glassHeight))
        val waterWidthAtBottom = glassBottomWidth - ((glassTopWidth - glassBottomWidth) * (waterHeight / glassHeight))

        // Draw the water inside the glass (adjusting width based on water height)
        val waterPath = Path()
        // Top-left of the water
        waterPath.moveTo(50f + (glassTopWidth - waterWidthAtTop) / 2, (100 + (glassHeight - waterHeight)))
        // Top-right of the water
        waterPath.lineTo(50f + (glassTopWidth + waterWidthAtTop) / 2, (100 + (glassHeight - waterHeight)))
        // Bottom-right of the water
        waterPath.lineTo(50f + (glassBottomWidth + waterWidthAtBottom) / 2, (100.0f + glassHeight))
        // Bottom-left of the water
        waterPath.lineTo(50f + (glassBottomWidth - waterWidthAtBottom) / 2, (100.0f + glassHeight))
        waterPath.close()

        // Draw the water
        canvas.drawPath(waterPath, waterPaint)
    }
}
