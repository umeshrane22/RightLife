package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.addCallback
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentHeartRateVariabilityBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeartRateVariabilityFragment : BaseFragment<FragmentHeartRateVariabilityBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHeartRateVariabilityBinding
        get() = FragmentHeartRateVariabilityBinding::inflate

    private lateinit var lineChart: LineChart
    private lateinit var radioGroup: RadioGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.gradient_color_background_workout)

        lineChart = view.findViewById(R.id.heartRateChart)
        radioGroup = view.findViewById(R.id.tabGroup)

        // Show Week data by default
        updateChart(getWeekData(), getWeekLabels())
        //fetchHeartRateVariability("last_weekly")
        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek ->{
                    updateChart(getWeekData(), getWeekLabels())
                   // fetchHeartRateVariability("last_weekly")
                }
                R.id.rbMonth -> {
                    updateChart(getMonthData(), getMonthLabels())
                   // fetchHeartRateVariability("last_monthly")
                }
                R.id.rbSixMonths ->{
                    updateChart(getSixMonthData(), getSixMonthLabels())
                   // fetchHeartRateVariability("last_six_months")

                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToFragment(HomeBottomTabFragment(),"landingFragment")
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
        val dataSet = LineDataSet(entries, "Heart Rate")
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
            Entry(1f, 65f),
            Entry(2f, 75f),
            Entry(3f, 70f),
            Entry(4f, 85f),
            Entry(5f, 80f),
            Entry(6f, 60f),
            Entry(7f, 90f)
        )
    }

    /** X-Axis Labels for Week */
    private fun getWeekLabels(): List<String> {
        return listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    /** Sample Data for Month (4 weeks) */
    private fun getMonthData(): List<Entry> {
        return listOf(
            Entry(0f, 40f), // 1-7 Jan
            Entry(1f, 55f), // 8-14 Jan
            Entry(2f, 35f), // 15-21 Jan
            Entry(3f, 50f), // 22-28 Jan
            Entry(4f, 30f)  // 29-31 Jan
        )
    }

    /** X-Axis Labels for Month */
    private fun getMonthLabels(): List<String> {
        return listOf("1-7 Jan", "8-14 Jan", "15-21 Jan", "22-28 Jan", "29-31 Jan")
    }

    /** Sample Data for 6 Months */
    private fun getSixMonthData(): List<Entry> {
        return listOf(
            Entry(0f, 45f), // Jan
            Entry(1f, 55f), // Feb
            Entry(2f, 50f), // Mar
            Entry(3f, 65f), // Apr
            Entry(4f, 70f), // May
            Entry(5f, 60f)  // Jun
        )
    }

    /** X-Axis Labels for 6 Months */
    private fun getSixMonthLabels(): List<String> {
        return listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
    }
    private fun fetchHeartRateVariability(period: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiServiceFastApi.getHeartRateVariability(
                    userId = "64763fe2fa0e40d9c0bc8264",
                    period = period,
                    date = "2025-03-24"
                )

                if (response.isSuccessful) {
                    val heartRateVariability = response.body()
                    heartRateVariability?.let { data ->

                        val totalCalories = data.heartRateVariability.sumOf { it.value.toDoubleOrNull() ?: 0.0 }
                        withContext(Dispatchers.Main) {
                           // updateChart(entries, labels)
                            /* Toast.makeText(
                                 requireContext(),
                                 "Total Active Calories: $totalCalories kcal",
                                 Toast.LENGTH_LONG
                             ).show()*/
                        }
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
}
