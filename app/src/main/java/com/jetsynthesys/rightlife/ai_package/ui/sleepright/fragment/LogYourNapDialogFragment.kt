package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.BaseResponse
import com.jetsynthesys.rightlife.ai_package.model.LogNapRequest
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

class LogYourNapDialogFragment(private val requireContext: Context, private val listener: OnLogYourNapSelectedListener) : BottomSheetDialogFragment() {
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var tvDuration: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvRemindTime: TextView
    private val mContext = requireContext

    private var startTime: LocalTime = LocalTime.of(0, 0)
    private var endTime: LocalTime = LocalTime.of(7, 30)
    private var selectedDate: LocalDate = LocalDate.now().minusDays(1)
    var startHour = 0
    var startMinute = 0
    var endHour = 7
    var endMinute = 30

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_log_last_night_sleep, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        tvStartTime = view.findViewById(R.id.tvStartTime)
        tvEndTime = view.findViewById(R.id.tvEndTime)
        tvDuration = view.findViewById(R.id.tvDuration)
        tvDate = view.findViewById(R.id.tvDate)
        tvRemindTime = view.findViewById(R.id.tvRemindTime)

        updateDuration()

        view.findViewById<View>(R.id.btnDatePicker).setOnClickListener {
            showDatePicker()
        }

        view.findViewById<View>(R.id.startTimeContainer).setOnClickListener {
            TimePickerDialogFragment(requireContext,startHour, startMinute,selectedDate,0) { hour, minute ->
                // Handle selected time
                val formatted = LocalTime.of(hour, minute).format(DateTimeFormatter.ofPattern("hh:mm a"))
                startHour = hour
                startMinute = minute
                tvStartTime.text = formatted
                startTime = LocalTime.of(hour, minute)
                updateDuration()
            }.show(childFragmentManager, "TimePickerDialog")
           // showTimePicker(isStart = true)
        }

        view.findViewById<View>(R.id.endTimeContainer).setOnClickListener {
            TimePickerDialogFragment(requireContext,endHour, endMinute,selectedDate,1) { hour, minute ->
                // Handle selected time
                val formatted = LocalTime.of(hour, minute)
                    .format(DateTimeFormatter.ofPattern("hh:mm a"))
                endHour = hour
                endMinute = minute
                tvEndTime.text = formatted
                endTime = LocalTime.of(hour, minute)
                updateDuration()
            }.show(childFragmentManager, "TimePickerDialog")
           // showTimePicker(isStart = false)
        }

        view.findViewById<View>(R.id.btnSaveLog).setOnClickListener {
            val sleepTime = tvStartTime.text.toString()
            val inputFmt  = DateTimeFormatter.ofPattern("hh:mm a")
            val outputFmt = DateTimeFormatter.ofPattern("HH:mm:ss")
            val time24h = LocalTime.parse(sleepTime, inputFmt).format(outputFmt)
            val formatStartDate = "$selectedDate"+"T"+"$time24h"
            val deviceZone  = ZoneId.systemDefault()          // phone / emulator zone
            val ldt         = LocalDateTime.parse(formatStartDate)      // parses without zone
            val instantStart     = ldt.atZone(deviceZone).toInstant().toString()
            val wakeTime =  tvEndTime.text.toString()
            val inputFmt1  = DateTimeFormatter.ofPattern("hh:mm a")
            val outputFmt1 = DateTimeFormatter.ofPattern("HH:mm:ss")
            val time24h1 = LocalTime.parse(wakeTime, inputFmt1).format(outputFmt1)
            val formatWakeDate = "$selectedDate"+"T"+"$time24h1"
            val ldt1         = LocalDateTime.parse(formatWakeDate)      // parses without zone
            val instantEnd     = ldt1.atZone(deviceZone).toInstant().toString()
            val utcOdt   = OffsetDateTime.parse(instantStart)    // parse as an OffsetDateTime (UTC)
            val systemZone1: ZoneId = ZoneId.systemDefault()       // or ZoneId.systemDefault()
            val istOdt   = utcOdt.atZoneSameInstant(systemZone1)   // same instant, new zone
            val startSleepTime  = istOdt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            val utcOdt1   = OffsetDateTime.parse(instantEnd)    // parse as an OffsetDateTime (UTC)
            val systemZone: ZoneId = ZoneId.systemDefault()     // or ZoneId.systemDefault()
            val istOdt1   = utcOdt1.atZoneSameInstant(systemZone)   // same instant, new zone
            val endSleepTime  = istOdt1.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            logNap(startSleepTime,endSleepTime)
        }
        view.findViewById<View>(R.id.btnClose).setOnClickListener {
            dismiss()
        }
    }

    private fun logNap(sleepTime: String, wakeTime: String) {
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val source = "android"
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = selectedDate.format(dateFormatter)
          val call = ApiClient.apiServiceFastApi.logNap(userId, source, date, LogNapRequest(sleepTime, wakeTime, set_reminder = 0, reminder_value = tvRemindTime.text.toString()))
        call.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(mContext, "Log Saved Successfully!", Toast.LENGTH_SHORT).show()
                    listener.onLogTimeSelected("OK")
                    dismiss()

                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getTodayDate(): LocalDate {
        return LocalDate.now()
    }

    private fun updateDuration() {
        val start = startTime.atDate(selectedDate)
        val end = if (endTime < startTime) endTime.atDate(selectedDate.plusDays(1)) else endTime.atDate(selectedDate)
        val duration = Duration.between(start, end)
        val hours = duration.toHours()
        val minutes = duration.minusHours(hours).toMinutes()

        if (hours <= 15) {
            tvStartTime.text = startTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
            tvEndTime.text = endTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
            tvDuration.text = "$hours hr $minutes mins"
            tvDate.text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        }else{
            Toast.makeText(requireContext,"Sleep time cannot be more than 15 hours", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTimePicker(isStart: Boolean) {
        val initial = if (isStart) startTime else endTime
        val timePicker = TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                if (isStart) startTime = LocalTime.of(hour, minute)
                else endTime = LocalTime.of(hour, minute)
                updateDuration()
            },
            initial.hour,
            initial.minute,
            false
        )
        timePicker.show()
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                selectedDate = LocalDate.of(year, month + 1, day)
                updateDuration()
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
        val today = System.currentTimeMillis()
        val sevenDaysAgo = today - 6 * 24 * 60 * 60 * 1000L // 6 days ago in milliseconds
        datePicker.datePicker.maxDate = today
        datePicker.datePicker.minDate = sevenDaysAgo
        datePicker.show()
    }
}