package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Toast
import com.jetsynthesys.rightlife.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.WakeupTimeResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class WakeUpTimeDialogFragment(private val context: Context, private val wakeupTime: String, private val recordId: String, private val listener: OnWakeUpTimeSelectedListener
) : BottomSheetDialogFragment() {

    private val handler = Handler(Looper.getMainLooper())

    private val mContext = context
    private val mWakeupTime = wakeupTime
    private val mRecordId = recordId
    private var mHour = 0
    private var mMinute = 0
    private var mAmPm = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.wakeup_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        val hourPicker = view.findViewById<NumberPicker>(R.id.hourPickerWake)
        val minutePicker = view.findViewById<NumberPicker>(R.id.minutePickerWake)
        val amPmPicker = view.findViewById<NumberPicker>(R.id.amPmPickerWake)

        mHour = getHourFromIso(mWakeupTime)
        mMinute = getMinuteFromIso(mWakeupTime)
        mAmPm = getAmPmFromIso(mWakeupTime)


        hourPicker.minValue = 1
        hourPicker.maxValue = 12
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        amPmPicker.minValue = 0
        amPmPicker.maxValue = 1
        amPmPicker.displayedValues = arrayOf("AM", "PM")
        hourPicker.value = mHour
        minutePicker.value = mMinute
        if (mAmPm == "AM"){
            amPmPicker.value = 0
        }else{
            amPmPicker.value = 1
        }

        val btnSendData = view.findViewById<LinearLayout>(R.id.btn_confirm)
        btnSendData.setOnClickListener {
            updateWakeupTime()
            listener.onWakeUpTimeSelected(mWakeupTime)
            dismiss()
        }

        fun updateNumberPickerText(picker: NumberPicker) {
            try {
                val field = NumberPicker::class.java.getDeclaredField("mInputText")
                field.isAccessible = true
                val editText = field.get(picker) as EditText
                editText.setTextColor( resources.getColor(R.color.sleep_duration_blue))
                editText.textSize = 24f
                editText.typeface = Typeface.DEFAULT_BOLD
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        fun refreshPickers() {
            handler.post {
                updateNumberPickerText(hourPicker)
                updateNumberPickerText(minutePicker)
                updateNumberPickerText(amPmPicker)
            }
        }


        val listener = NumberPicker.OnValueChangeListener { _, _, _ -> refreshPickers() }
        hourPicker.setOnValueChangedListener(listener)
        minutePicker.setOnValueChangedListener(listener)
        amPmPicker.setOnValueChangedListener(listener)
        refreshPickers()
    }

    fun parseIsoDateTime(isoDateTime: String): LocalDateTime {
        return LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    // 1. Return the hour in 12-hour format
    fun getHourFromIso(isoDateTime: String): Int {
        val dateTime = parseIsoDateTime(isoDateTime)
        val hour = dateTime.hour % 12
        return if (hour == 0) 12 else hour
    }

    // 2. Return the minutes
    fun getMinuteFromIso(isoDateTime: String): Int {
        val dateTime = parseIsoDateTime(isoDateTime)
        return dateTime.minute
    }

    // 3. Return AM or PM
    fun getAmPmFromIso(isoDateTime: String): String {
        val dateTime = parseIsoDateTime(isoDateTime)
        return if (dateTime.hour < 12) "AM" else "PM"
    }

    private fun updateWakeupTime() {
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId ?: "68010b615a508d0cfd6ac9ca"
        val source = "apple"
        val call = ApiClient.apiServiceFastApi.updateWakeupTime(userId, source, record_id =  mRecordId, timer_value = mWakeupTime )
        call.enqueue(object : Callback<WakeupTimeResponse> {
            override fun onResponse(call: Call<WakeupTimeResponse>, response: Response<WakeupTimeResponse>) {
                if (response.isSuccessful) {

                      Toast.makeText(mContext, "Log Saved Successfully!", Toast.LENGTH_SHORT).show()

                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                     Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<WakeupTimeResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                  Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }
}