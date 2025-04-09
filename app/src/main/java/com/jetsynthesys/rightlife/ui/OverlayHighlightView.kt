package com.jetsynthesys.rightlife.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.view.View



class OverlayHighlightView(
    context: Context,
    private val targetView: View
) : View(context) {

    private val overlayPaint = Paint().apply {
        color = Color.parseColor("#80000000") // semi-transparent dim
    }

    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Fill the screen with dim overlay
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)

        // Get position of targetView relative to this view
        val anchorLocation = IntArray(2)
        val overlayLocation = IntArray(2)

        targetView.getLocationOnScreen(anchorLocation)
        this.getLocationOnScreen(overlayLocation)

        val relativeX = anchorLocation[0] - overlayLocation[0]
        val relativeY = anchorLocation[1] - overlayLocation[1]

        // Add padding around the highlight
        val padding = 3f
        val left = relativeX - padding
        val top = relativeY - padding
        val right = relativeX + targetView.width + padding
        val bottom = relativeY + targetView.height + padding
        val cornerRadius = 32f // change this to adjust roundness

        // Draw transparent rounded rectangle
        val rect = RectF(left, top, right, bottom)
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, clearPaint)
    }

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }
}



