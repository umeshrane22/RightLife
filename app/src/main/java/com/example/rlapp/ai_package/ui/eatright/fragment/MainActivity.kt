package com.example.rlapp.ai_package.ui.eatright.fragment

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ai_package.ui.eatright.adapter.WeekAdapter
import com.example.rlapp.ai_package.ui.eatright.model.WeekData
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class Main2Activity : AppCompatActivity() {

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var weekRecyclerView: RecyclerView
    private lateinit var btnClose: ImageView
    private lateinit var txtSelectedDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aadefdnkfdlk)

        calendarView = findViewById(R.id.calendarView)
        weekRecyclerView = findViewById(R.id.weekRecyclerView)
        btnClose = findViewById(R.id.btnClose)
        txtSelectedDate = findViewById(R.id.txtSelectedDate)

        // Close Dialog (Finish Activity)
        btnClose.setOnClickListener { finish() }

        // Setup Calendar
        calendarView.setOnDateChangedListener { _, date, _ ->
            txtSelectedDate.text = "Selected: ${date.date} ${date.month + 1}, ${date.year}"
        }

        // Dummy Data for Week Calories
        val weekList = listOf(
            WeekData(2140),  // Deficit
            WeekData(12),    // Surplus
            WeekData(0),
            WeekData(0),
            WeekData(0)
        )

        weekRecyclerView.layoutManager = LinearLayoutManager(this)
        weekRecyclerView.adapter = WeekAdapter(weekList)
    }
}
