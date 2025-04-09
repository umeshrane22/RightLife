package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.R.color.transparent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jetsynthesys.rightlife.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WaterIntakeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var waterAmount: TextView
    private var selectedCups = 2
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedValueText: TextView
    private lateinit var ivCupIcon : ImageView
    private lateinit var progressFill: View
    private lateinit var progressBarContainer: FrameLayout
    private var dY = 0f
    private var minY = 0f
    private var maxY = 0f
    private var waterIntake = 0
    private val maxIntake = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_water_intake_selection, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = BottomSheetDialog(requireContext(), R.style.LoggedBottomSheetDialogTheme)
        dialog.setContentView(R.layout.fragment_eat_right_landing)
        dialog.window?.setBackgroundDrawableResource(transparent)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.isDraggable = false
        val closeIV = view.findViewById<ImageView>(R.id.closeIV)
        progressFill = view.findViewById(R.id.progressFill)
        progressBarContainer = view.findViewById(R.id.progressBarContainer)
        selectedValueText = view.findViewById(R.id.selectedValueText)
        ivCupIcon = view.findViewById(R.id.ivCupIcon)

        progressBarContainer.viewTreeObserver.addOnGlobalLayoutListener {
            minY = progressBarContainer.top.toFloat()
            maxY = (progressBarContainer.bottom - ivCupIcon.height).toFloat()
        }

        updateUI()

        ivCupIcon.setOnTouchListener { v, event ->
            val parent = v.parent as View
            val maxY = (parent.height - v.height).toFloat()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dY = v.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val newY = (event.rawY + dY).coerceIn(0f, maxY)
                    v.y = newY
                    updateValueBasedOnPosition(newY)
                }
                MotionEvent.ACTION_UP -> {
                    updateValueBasedOnPosition(v.y)
                }
            }
            true
        }

        closeIV.setOnClickListener {
            dismiss()
        }
    }

    private fun updateUI() {
        selectedValueText.text = "${waterIntake}"
        progressBarContainer.post {
            val containerHeight = progressBarContainer.height
            val progressRatio = waterIntake.toFloat() / maxIntake.toFloat()
            val newHeight = (containerHeight * progressRatio).toInt()
            val params = progressFill.layoutParams
            params.height = newHeight
            progressFill.layoutParams = params
        }
    }

    private fun updateValueBasedOnPosition(positionY: Float) {
        val containerHeight = progressBarContainer.height
        val maxSteps = maxIntake / 250  // assuming maxIntake is a multiple of 250
        val stepHeight = containerHeight / maxSteps

        val stepsFromBottom = ((containerHeight - positionY) / stepHeight).toInt()
        val clampedSteps = stepsFromBottom.coerceIn(0, maxSteps)

        waterIntake = clampedSteps * 250
        updateUI()
    }


//    private fun updateValueBasedOnPosition(y: Float) {
//        val containerHeight = progressBarContainer.height - ivCupIcon.height
//        val clampedY = y.coerceIn(0f, containerHeight.toFloat())
//        val progressRatio = 1 - (clampedY / containerHeight)
//        waterIntake = (progressRatio * maxIntake).toInt()
//        updateUI()
//    }

    companion object {
        const val TAG = "LoggedBottomSheet"
        @JvmStatic
        fun newInstance() = WaterIntakeBottomSheet().apply {
            arguments = Bundle().apply {
            }
        }
    }
}

