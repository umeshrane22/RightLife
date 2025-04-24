package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.HeartRateResponse
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentAverageHeartRateBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AverageHeartRateFragment : BaseFragment<FragmentAverageHeartRateBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAverageHeartRateBinding
        get() = FragmentAverageHeartRateBinding::inflate

    private lateinit var lineChart: LineChart
    private lateinit var radioGroup: RadioGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.gradient_color_background_workout)

        lineChart = view.findViewById(R.id.heartRateChart)
        radioGroup = view.findViewById(R.id.tabGroup)

        // Show Week data by default and fetch API data
        updateChart(getWeekData(), getWeekLabels())
        fetchHeartRate("last_weekly")

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek -> fetchHeartRate("last_weekly")
                R.id.rbMonth -> fetchHeartRate("last_monthly")
                R.id.rbSixMonths -> fetchHeartRate("last_six_months")
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
        val dataSet = LineDataSet(entries, "Average Heart Rate (bpm)")
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
    private fun fetchHeartRate(period: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiServiceFastApi.getHeartRate(
                    userId = "64763fe2fa0e40d9c0bc8264",
                    period = period,
                    date = "2025-03-24"
                )

                if (response.isSuccessful) {
                    val hrResponse = response.body()
                    hrResponse?.let { data ->
                        val (entries, labels) = when (period) {
                            "last_weekly" -> processWeeklyData(data)
                            "last_monthly" -> processMonthlyData(data)
                            "last_six_months" -> processSixMonthsData(data)
                            else -> Pair(getWeekData(), getWeekLabels()) // Fallback
                        }
                        // Safely handle null heartRate by providing a fallback empty list
                        val heartRates = data.activeHeartRateTotals ?: emptyList()
                        val averageHr = if (heartRates.isNotEmpty()) {
                            heartRates
                                .mapNotNull { it.heartRate }
                                .average()
                        } else {
                            0.0 // Default value if heartRate list is empty or null
                        }
                        withContext(Dispatchers.Main) {
                            updateChart(entries, labels)
                            if (averageHr > 0) {
                                Toast.makeText(
                                    requireContext(),
                                    "Average Heart Rate: ${String.format("%.2f", averageHr)} bpm",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "No valid heart rate data available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } ?: withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "No heart rate data received", Toast.LENGTH_SHORT).show()
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
    private fun processWeeklyData(data: HeartRateResponse): Pair<List<Entry>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val startDateStr = data.startDate?.substring(0, 10) ?: "2025-03-24" // Extract yyyy-MM-dd
        val startDate = dateFormat.parse(startDateStr) ?: return Pair(getWeekData(), getWeekLabels())
        calendar.time = startDate
        calendar.add(Calendar.DAY_OF_YEAR, -6) // Start of the week

        val hrMap = mutableMapOf<String, Float>()
        val labels = mutableListOf<String>()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault()) // e.g., "Mon"

        // Initialize 7 days
        repeat(7) {
            val dateStr = dateFormat.format(calendar.time)
            hrMap[dateStr] = 0f
            labels.add(dayFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Aggregate HR by day
        data.activeHeartRateTotals?.forEach { hr ->
            val dayKey = hr.date
            if (hrMap.containsKey(dayKey)) {
                hrMap[dayKey] = hr.heartRate?.toFloat() ?: 0f
            }
        }

        // Create entries
        val entries = hrMap.values.mapIndexed { index, value ->
            Entry(index.toFloat(), value)
        }
        return Pair(entries, labels)
    }

    /** Process API data for last_monthly (5 weeks) */
    private fun processMonthlyData(data: HeartRateResponse): Pair<List<Entry>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val startDateStr = data.startDate?.substring(0, 10) ?: "2025-03-24"
        val startDate = dateFormat.parse(startDateStr) ?: return Pair(getMonthData(), getMonthLabels())
        calendar.time = startDate
        calendar.set(Calendar.DAY_OF_MONTH, 1) // Start of the month

        val hrMap = mutableMapOf<Int, Float>()
        val labels = mutableListOf<String>()
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Initialize 5 weeks
        repeat(5) { week ->
            val startDay = week * 7 + 1
            val endDay = if (week == 4) daysInMonth else minOf(startDay + 6, daysInMonth)
            hrMap[week] = 0f
            labels.add("$startDay-$endDay")
        }

        // Aggregate HR by week
        data.activeHeartRateTotals?.forEach { hr ->
            val hrDate = dateFormat.parse(hr.date) ?: return Pair(getMonthData(), getMonthLabels())
            calendar.time = hrDate
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val weekIndex = (dayOfMonth - 1) / 7 // 0-based week index
            if (weekIndex in 0..4) {
                hrMap[weekIndex] = hr.heartRate?.toFloat() ?: 0f
            }
        }

        // Create entries
        val entries = hrMap.values.mapIndexed { index, value ->
            Entry(index.toFloat(), value)
        }
        return Pair(entries, labels)
    }

    /** Process API data for last_six_months (6 months) */
    private fun processSixMonthsData(data: HeartRateResponse): Pair<List<Entry>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val startDateStr = data.startDate?.substring(0, 10) ?: "2025-03-24"
        val startDate = dateFormat.parse(startDateStr) ?: return Pair(getSixMonthData(), getSixMonthLabels())
        calendar.time = startDate
        calendar.add(Calendar.MONTH, -5) // Start 6 months back

        val hrMap = mutableMapOf<Int, Float>()
        val labels = mutableListOf<String>()

        // Initialize 6 months
        repeat(6) { month ->
            hrMap[month] = 0f
            labels.add(monthFormat.format(calendar.time))
            calendar.add(Calendar.MONTH, 1)
        }

        // Aggregate HR by month
        data.activeHeartRateTotals?.forEach { hr ->
            val hrDate = dateFormat.parse(hr.date)
            calendar.time = hrDate
            val monthDiff = ((2025 - 1900) * 12 + Calendar.MARCH) - ((calendar.get(Calendar.YEAR) - 1900) * 12 + calendar.get(Calendar.MONTH))
            val monthIndex = 5 - monthDiff // Reverse to align with labels
            if (monthIndex in 0..5) {
                hrMap[monthIndex] = hr.heartRate?.toFloat() ?: 0f
            }
        }

        // Create entries
        val entries = hrMap.values.mapIndexed { index, value ->
            Entry(index.toFloat(), value)
        }
        return Pair(entries, labels)
    }
}