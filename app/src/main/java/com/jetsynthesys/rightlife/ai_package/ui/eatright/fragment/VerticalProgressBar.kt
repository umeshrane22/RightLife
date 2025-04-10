package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class VerticalProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
    ) : View(context, attrs) {

        private var progress: Float = 0f // 0.0 to 1.0

        fun setProgress(value: Float) {
            progress = value.coerceIn(0f, 1f)
            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            canvas?.let {
                val paint = Paint().apply {
                    color = Color.parseColor("#00C853") // Green fill
                    isAntiAlias = true
                }

                val fillHeight = height * progress
                canvas.drawRect(
                    0f,
                    height - fillHeight,
                    width.toFloat(),
                    height.toFloat(),
                    paint
                )
            }
        }
    }