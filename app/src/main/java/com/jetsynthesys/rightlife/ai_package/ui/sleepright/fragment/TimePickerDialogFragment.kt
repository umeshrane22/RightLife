package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.TimePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsynthesys.rightlife.R

class TimePickerDialogFragment(private val initialHour: Int,
                               private val initialMinute: Int,
                               private val onTimeSelected: (hour: Int, minute: Int) -> Unit)  : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.time_picker_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)

        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val btnLeft = view.findViewById<ImageView>(R.id.btnArrowLeft)
        val btnRight = view.findViewById<ImageView>(R.id.btnArrowRight)
        timePicker.setIs24HourView(false)
        timePicker.hour = initialHour
        timePicker.minute = initialMinute

        setTimePickerTextColor(timePicker,R.color.blue_bar)

        view.findViewById<TextView>(R.id.btnConfirm).setOnClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            onTimeSelected(selectedHour, selectedMinute)
            dismiss()
        }

        btnLeft.setOnClickListener {
            val currentHour = timePicker.hour
            timePicker.hour = (currentHour + 11) % 12  // Decrement hour with wrap-around
        }

        btnRight.setOnClickListener {
            val currentHour = timePicker.hour
            timePicker.hour = (currentHour + 1) % 12   // Increment hour with wrap-around
        }

        view.findViewById<TextView>(R.id.btnConfirm).setOnClickListener {
            onTimeSelected(timePicker.hour, timePicker.minute)
            dismiss()
        }

        view.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
            dismiss()
        }
    }

    @SuppressLint("PrivateApi")
    private fun setTimePickerTextColor(timePicker: TimePicker, color: Int) {
        try {
            val classForid = Class.forName("com.android.internal.R\$id")
            val hourNumberPicker = timePicker.findViewById<NumberPicker>(
                classForid.getField("hour").getInt(null)
            )
            val minuteNumberPicker = timePicker.findViewById<NumberPicker>(
                classForid.getField("minute").getInt(null)
            )
            val amPmNumberPicker = timePicker.findViewById<NumberPicker>(
                classForid.getField("amPm").getInt(null)
            )

            setNumberPickerTextColor(hourNumberPicker, color)
            setNumberPickerTextColor(minuteNumberPicker, color)
            setNumberPickerTextColor(amPmNumberPicker, color)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    private fun setNumberPickerTextColor(numberPicker: NumberPicker?, color: Int) {
        if (numberPicker == null) return
        try {
            val selectorWheelPaintField = NumberPicker::class.java.getDeclaredField("mSelectorWheelPaint")
            selectorWheelPaintField.isAccessible = true
            val paint = selectorWheelPaintField.get(numberPicker) as Paint
            paint.color = color

            // force redrawing
            numberPicker.invalidate()

            // loop through children to set text color
            for (i in 0 until numberPicker.childCount) {
                val child = numberPicker.getChildAt(i)
                if (child is EditText) {
                    child.setTextColor(color)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}