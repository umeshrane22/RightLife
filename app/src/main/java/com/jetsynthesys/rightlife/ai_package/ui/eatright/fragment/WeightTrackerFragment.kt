package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.R.*
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.frequentlylogged.LoggedBottomSheet
import com.google.android.flexbox.FlexboxLayout
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.RestingHeartRateResponse
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentWeightTrackerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WeightTrackerFragment : BaseFragment<FragmentWeightTrackerBinding>() {

    private lateinit var lineChart: LineChart
    private lateinit var radioGroup: RadioGroup


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWeightTrackerBinding
        get() = FragmentWeightTrackerBinding::inflate


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundColor(ContextCompat.getColor(requireContext(), color.meal_log_background))

        lineChart = view.findViewById(R.id.heartRateChart)
        radioGroup = view.findViewById(R.id.tabGroup)

        // Show Week data by default
        updateChart(getWeekData(), getWeekLabels())
        //fetchRestingHeartRate("last_weekly")

        // Set default selection to Week
        radioGroup.check(R.id.rbMonth)

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
//                R.id.rbWeek -> fetchRestingHeartRate("last_weekly")
//                R.id.rbMonth -> fetchRestingHeartRate("last_monthly")
//                R.id.rbSixMonths -> fetchRestingHeartRate("last_six_months")
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToFragment(HomeBottomTabFragment(), "landingFragment")
        }
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    /** Update chart with new data and X-axis labels */
    private fun updateChart(entries: List<Entry>, labels: List<String>) {
        val dataSet = LineDataSet(entries, "Resting Heart Rate (bpm)")
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.BLACK
        dataSet.setCircleColor(Color.RED)
        dataSet.circleRadius = 5f
        dataSet.lineWidth = 2f
        dataSet.setDrawValues(false) // Hide values on points

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Customize X-Axis
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels) // Set custom labels
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.BLACK
        xAxis.yOffset = 15f // Move labels down

        // Customize Y-Axis
        val leftYAxis: YAxis = lineChart.axisLeft
        leftYAxis.textSize = 12f
        leftYAxis.textColor = Color.BLACK
        leftYAxis.setDrawGridLines(true)

        // Disable right Y-axis
        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false

        // Refresh chart
        lineChart.invalidate()
    }

    /** Sample Data for Week */
    private fun getWeekData(): List<Entry> {
        return listOf(
            Entry(0f, 65f), Entry(1f, 75f), Entry(2f, 70f),
            Entry(3f, 85f), Entry(4f, 80f), Entry(5f, 60f), Entry(6f, 90f)
        )
    }

    private fun getWeekLabels(): List<String> {
        return listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    /** Sample Data for Month */
    private fun getMonthData(): List<Entry> {
        return listOf(
            Entry(0f, 40f), Entry(1f, 55f), Entry(2f, 35f),
            Entry(3f, 50f), Entry(4f, 30f)
        )
    }

    private fun getMonthLabels(): List<String> {
        return listOf("1-7", "8-14", "15-21", "22-28", "29-31")
    }

    /** Sample Data for 6 Months */
    private fun getSixMonthData(): List<Entry> {
        return listOf(
            Entry(0f, 45f), Entry(1f, 55f), Entry(2f, 50f),
            Entry(3f, 65f), Entry(4f, 70f), Entry(5f, 60f)
        )
    }

    private fun getSixMonthLabels(): List<String> {
        return listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
    }

    /** Fetch and update chart with API data */
    private fun fetchRestingHeartRate(period: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiServiceFastApi.getRestingHeartRate(
                    userId = "64763fe2fa0e40d9c0bc8264",
                    period = period,
                    date = "2025-03-24"
                )

                if (response.isSuccessful) {
                    val restingHeartRate = response.body()
                    restingHeartRate?.let { data ->
                        val (entries, labels) = when (period) {
                            "last_weekly" -> processWeeklyData(data)
                            "last_monthly" -> processMonthlyData(data)
                            "last_six_months" -> processSixMonthsData(data)
                            else -> Pair(getWeekData(), getWeekLabels()) // Fallback
                        }
                        val averageRhr = data.restingHeartRate
                            .mapNotNull { it.value.toDoubleOrNull() }
                            .average()
                        withContext(Dispatchers.Main) {
                            updateChart(entries, labels)

                        }
                    } ?: withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "No resting heart rate data received", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /** Process API data for last_weekly (7 days) */
    private fun processWeeklyData(data: RestingHeartRateResponse): Pair<List<Entry>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.MARCH, 24) // Reference date
        calendar.add(Calendar.DAY_OF_YEAR, -6) // Start of the week (March 18)

        val rhrMap = mutableMapOf<String, MutableList<Float>>()
        val labels = mutableListOf<String>()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault()) // e.g., "Mon"

        // Initialize 7 days
        repeat(7) {
            val dateStr = dateFormat.format(calendar.time)
            rhrMap[dateStr] = mutableListOf()
            labels.add(dayFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Aggregate RHR by day
        data.restingHeartRate.forEach { rhr ->
            val startDate = dateFormat.parse(rhr.startDatetime)?.let { Date(it.time) }
            if (startDate != null) {
                val dayKey = dateFormat.format(startDate)
                rhrMap[dayKey]?.add(rhr.value.toFloatOrNull() ?: 0f)
            }
        }

        // Average RHR per day
        val entries = rhrMap.values.mapIndexed { index, values ->
            val average = if (values.isNotEmpty()) values.average().toFloat() else 0f
            Entry(index.toFloat(), average)
        }
        return Pair(entries, labels)
    }

    /** Process API data for last_monthly (5 weeks) */
    private fun processMonthlyData(data: RestingHeartRateResponse): Pair<List<Entry>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.MARCH, 1) // Start of March

        val rhrMap = mutableMapOf<Int, MutableList<Float>>()
        val labels = mutableListOf<String>()

        // Initialize 5 weeks
        repeat(5) { week ->
            val startDay = week * 7 + 1
            val endDay = if (week == 4) 31 else startDay + 6
            rhrMap[week] = mutableListOf()
            labels.add("$startDay-$endDay")
        }

        // Aggregate RHR by week
        data.restingHeartRate.forEach { rhr ->
            val startDate = dateFormat.parse(rhr.startDatetime)?.let { Date(it.time) }
            if (startDate != null) {
                calendar.time = startDate
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val weekIndex = (dayOfMonth - 1) / 7 // 0-based week index
                rhrMap[weekIndex]?.add(rhr.value.toFloatOrNull() ?: 0f)
            }
        }

        // Average RHR per week
        val entries = rhrMap.values.mapIndexed { index, values ->
            val average = if (values.isNotEmpty()) values.average().toFloat() else 0f
            Entry(index.toFloat(), average)
        }
        return Pair(entries, labels)
    }

    /** Process API data for last_six_months (6 months) */
    private fun processSixMonthsData(data: RestingHeartRateResponse): Pair<List<Entry>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.MARCH, 24)
        calendar.add(Calendar.MONTH, -5) // Start 6 months back (Oct 2024)

        val rhrMap = mutableMapOf<Int, MutableList<Float>>()
        val labels = mutableListOf<String>()

        // Initialize 6 months
        repeat(6) { month ->
            rhrMap[month] = mutableListOf()
            labels.add(monthFormat.format(calendar.time))
            calendar.add(Calendar.MONTH, 1)
        }

        // Aggregate RHR by month
        data.restingHeartRate.forEach { rhr ->
            val startDate = dateFormat.parse(rhr.startDatetime)?.let { Date(it.time) }
            if (startDate != null) {
                calendar.time = startDate
                val monthDiff = ((2025 - 1900) * 12 + Calendar.MARCH) - ((calendar.get(Calendar.YEAR) - 1900) * 12 + calendar.get(
                    Calendar.MONTH))
                val monthIndex = 5 - monthDiff // Reverse to align with labels
                if (monthIndex in 0..5) {
                    rhrMap[monthIndex]?.add(rhr.value.toFloatOrNull() ?: 0f)
                }
            }
        }

        // Average RHR per month
        val entries = rhrMap.values.mapIndexed { index, values ->
            val average = if (values.isNotEmpty()) values.average().toFloat() else 0f
            Entry(index.toFloat(), average)
        }
        return Pair(entries, labels)
    }
}