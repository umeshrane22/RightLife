package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.macros

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var calories: Int = 0
    var maxCalories: Int = 3000 // you can set this dynamically
    var barColor: Int = Color.parseColor("#1AB65C") // Green
    var label: String = ""

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = barColor
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        textSize = 32f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val barHeight = (calories / maxCalories.toFloat()) * height

        canvas.drawRect(
            width / 4f,
            height - barHeight,
            width * 3 / 4f,
            height.toFloat(),
            barPaint
        )

        canvas.drawText(label, width / 2f, height + 35f, textPaint)
    }
}
