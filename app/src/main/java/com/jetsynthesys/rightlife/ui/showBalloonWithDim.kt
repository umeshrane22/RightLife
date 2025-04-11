package com.jetsynthesys.rightlife.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation

fun Context.showBalloonWithDim(
    anchorView: View,
    message: String,
    prefName: String,
    alignment: BalloonAlignment = BalloonAlignment.BOTTOM,
    arrowPosition: Float = 0.9f,
    xOff: Int = 0,
    yOff: Int = 0,
    onDismiss: (() -> Unit)? = null
) {
    val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
    if (sharedPreferenceManager.isTooltipShowed(prefName))
        return
    else
        sharedPreferenceManager.saveTooltip(prefName, true)

    val activity = this as? Activity ?: return

    val rootView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)

    // Build the Balloon
    val balloon = Balloon.Builder(this)
        .setText(message)
        .setArrowSize(15)
        .setArrowPosition(arrowPosition)
        .setWidthRatio(0.5f)
        .setCornerRadius(8f)
        .setTextSize(12f)
        .setPadding(12)
        .setTextColor(Color.BLACK)
        .setBackgroundColor(Color.WHITE)
        .setBalloonAnimation(BalloonAnimation.FADE)
        .build()

    // Make sure anchor is laid out before showing
    anchorView.post {
        when (alignment) {
            BalloonAlignment.TOP -> balloon.showAlignTop(anchorView, xOff, yOff)
            BalloonAlignment.LEFT -> balloon.showAlignLeft(anchorView, xOff, yOff)
            BalloonAlignment.RIGHT -> balloon.showAlignRight(anchorView, xOff, yOff)
            BalloonAlignment.BOTTOM -> balloon.showAlignBottom(anchorView, xOff, yOff)
        }

    }

    // Create and add dim background overlay
    val dimOverlay = OverlayHighlightView(this, anchorView).apply {
        alpha = 0f
        isClickable = true
        isFocusable = true
    }


    rootView.addView(
        dimOverlay,
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    dimOverlay.animate().alpha(1f).setDuration(200).start()

    // Tap outside to dismiss
    dimOverlay.setOnClickListener {
        balloon.dismiss()
    }

    // On Dismiss: Remove overlay and callback
    balloon.setOnBalloonDismissListener {
        dimOverlay.animate().alpha(0f).setDuration(200).withEndAction {
            rootView.removeView(dimOverlay)
            onDismiss?.invoke()
        }.start()
    }
}

fun Context.showBalloon(
    anchorView: View,
    message: String,
    alignment: BalloonAlignment = BalloonAlignment.TOP,
    arrowPosition: Float = 0.9f,
    xOff: Int = 0,
    yOff: Int = 0,
) {
    // Build the Balloon
    val balloon = Balloon.Builder(this)
        .setText(message)
        .setArrowSize(15)
        .setArrowPosition(arrowPosition)
        .setWidthRatio(0.5f)
        .setCornerRadius(8f)
        .setTextSize(12f)
        .setPadding(12)
        .setTextColor(Color.BLACK)
        .setBackgroundColor(Color.WHITE)
        .setBalloonAnimation(BalloonAnimation.FADE)
        .build()

    // Make sure anchor is laid out before showing
    anchorView.post {
        when (alignment) {
            BalloonAlignment.TOP -> balloon.showAlignTop(anchorView, xOff, yOff)
            BalloonAlignment.LEFT -> balloon.showAlignLeft(anchorView, xOff, yOff)
            BalloonAlignment.RIGHT -> balloon.showAlignRight(anchorView, xOff, yOff)
            BalloonAlignment.BOTTOM -> balloon.showAlignBottom(anchorView, xOff, yOff)
        }
    }

    anchorView.postDelayed({
        balloon.dismiss()
    }, 3000)
}

