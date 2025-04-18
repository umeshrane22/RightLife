package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.addCallback
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.ActiveCaloriesResponse
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentBurnBinding
import com.jetsynthesys.rightlife.databinding.FragmentStepBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class StepFragment : BaseFragment<FragmentStepBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStepBinding
        get() = FragmentStepBinding::inflate

    private lateinit var barChart: BarChart
    private lateinit var radioGroup: RadioGroup
    private lateinit var lineChart: LineChart
    private lateinit var layoutLineChart: FrameLayout
    private lateinit var stripsContainer: FrameLayout



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.gradient_color_background_workout)

        barChart = view.findViewById(R.id.heartRateChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        layoutLineChart = view.findViewById(R.id.lyt_line_chart)
        stripsContainer = view.findViewById(R.id.stripsContainer)
        lineChart = view.findViewById(R.id.heartLineChart)

        // Initial chart setup with sample data
        updateChart(getWeekData(), getWeekLabels())
        fetchActiveCalories("last_weekly")

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)

        // Handle Radio Button Selection
        fetchActiveCalories("last_weekly")
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek -> {
                    barChart.visibility = View.VISIBLE
                    layoutLineChart.visibility = View.GONE
                    fetchActiveCalories("last_weekly")
                }
                R.id.rbMonth -> {
                    barChart.visibility = View.VISIBLE
                    layoutLineChart.visibility = View.GONE
                    fetchActiveCalories("monthly")
                }
                R.id.rbSixMonths -> {
                    barChart.visibility = View.GONE
                    layoutLineChart.visibility = View.VISIBLE
                    lineChartForSixMonths()
                    fetchActiveCalories("six_months")
                }
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

    /** Update BarChart with new data */
    private fun updateChart(entries: List<BarEntry>, labels: List<String>) {
        val dataSet = BarDataSet(entries, "Calories Burned")
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        barData.barWidth = 0.4f

        barChart.data = barData
        barChart.setFitBars(true)
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels) // Set custom labels
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.BLACK
        xAxis.yOffset = 15f // Move labels down
        val leftYAxis: YAxis = barChart.axisLeft
        leftYAxis.textSize = 12f
        leftYAxis.textColor = Color.BLACK
        leftYAxis.setDrawGridLines(true)
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.animateY(1000) // Smooth animation
        barChart.invalidate()
    }

    /** Sample Data for Week */
    private fun getWeekData(): List<BarEntry> {
        return listOf(
            BarEntry(0f, 200f), BarEntry(1f, 350f), BarEntry(2f, 270f),
            BarEntry(3f, 400f), BarEntry(4f, 320f), BarEntry(5f, 500f), BarEntry(6f, 450f)
        )
    }

    private fun getWeekLabels(): List<String> {
        return listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    /** Sample Data for Month */
    private fun getMonthData(): List<BarEntry> {
        return listOf(
            BarEntry(0f, 1500f), BarEntry(1f, 1700f), BarEntry(2f, 1400f),
            BarEntry(3f, 1800f), BarEntry(4f, 1200f)
        )
    }

    private fun getMonthLabels(): List<String> {
        return listOf("1-7", "8-14", "15-21", "22-28", "29-31")
    }

    /** Sample Data for 6 Months */
    private fun getSixMonthData(): List<BarEntry> {
        return listOf(
            BarEntry(0f, 9000f), BarEntry(1f, 8500f), BarEntry(2f, 8700f),
            BarEntry(3f, 9100f), BarEntry(4f, 9400f), BarEntry(5f, 8800f)
        )
    }

    private fun getSixMonthLabels(): List<String> {
        return listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
    }

    /** Fetch and update chart with API data */
    private fun fetchActiveCalories(period: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiServiceFastApi.getActiveCalories(
                    userId = "64763fe2fa0e40d9c0bc8264",
                    period = period,
                    date = "2025-03-24"
                )

                if (response.isSuccessful) {
                    val activeCaloriesResponse = response.body()
                    activeCaloriesResponse?.let { data ->
                        val (entries, labels) = when (period) {
                            "last_weekly" -> processWeeklyData(data)
                            "last_monthly" -> processMonthlyData(data)
                            "last_six_months" -> processSixMonthsData(data)
                            else -> Pair(getWeekData(), getWeekLabels()) // Fallback
                        }
                        val totalCalories = data.activeCaloriesTotals.sumOf { it.caloriesBurned ?: 0.0 }
                        withContext(Dispatchers.Main) {
                            updateChart(entries, labels)
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

    /** Process API data for last_weekly (7 days) */
    private fun processWeeklyData(data: ActiveCaloriesResponse): Pair<List<BarEntry>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.MARCH, 24) // Reference date
        calendar.add(Calendar.DAY_OF_YEAR, -6) // Start of the week (7 days back)

        val calorieMap = mutableMapOf<String, Float>()
        val labels = mutableListOf<String>()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault()) // e.g., "Mon"

        // Initialize 7 days with 0 calories
        repeat(7) {
            val dateStr = dateFormat.format(calendar.time)
            calorieMap[dateStr] = 0f
            labels.add(dayFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Aggregate calories by day
        data.activeCaloriesTotals.forEach { calorie ->
            val startDate = dateFormat.parse(calorie.date)?.let { Date(it.time) }
            if (startDate != null) {
                val dayKey = dateFormat.format(startDate)
                calorieMap[dayKey] = calorieMap[dayKey]!! + (calorie.caloriesBurned.toFloat() ?: 0f)
            }
        }

        val entries = calorieMap.values.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
        return Pair(entries, labels)
    }

    /** Process API data for last_monthly (5 weeks) */
    private fun processMonthlyData(data: ActiveCaloriesResponse): Pair<List<BarEntry>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.MARCH, 1) // Start of March

        val calorieMap = mutableMapOf<Int, Float>()
        val labels = mutableListOf<String>()

        // Initialize 5 weeks
        repeat(5) { week ->
            val startDay = week * 7 + 1
            val endDay = if (week == 4) 31 else startDay + 6
            calorieMap[week] = 0f
            labels.add("$startDay-$endDay")
        }

        // Aggregate calories by week
        data.activeCaloriesTotals.forEach { calorie ->
            val startDate = dateFormat.parse(calorie.date)?.let { Date(it.time) }
            if (startDate != null) {
                calendar.time = startDate
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val weekIndex = (dayOfMonth - 1) / 7 // 0-based week index
                calorieMap[weekIndex] = calorieMap[weekIndex]!! + (calorie.caloriesBurned.toFloat() ?: 0f)
            }
        }

        val entries = calorieMap.values.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
        return Pair(entries, labels)
    }

    /** Process API data for last_six_months (6 months) */
    private fun processSixMonthsData(data: ActiveCaloriesResponse): Pair<List<BarEntry>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.MARCH, 24)
        calendar.add(Calendar.MONTH, -5) // Start 6 months back

        val calorieMap = mutableMapOf<Int, Float>()
        val labels = mutableListOf<String>()

        // Initialize 6 months
        repeat(6) { month ->
            calorieMap[month] = 0f
            labels.add(monthFormat.format(calendar.time))
            calendar.add(Calendar.MONTH, 1)
        }

        // Aggregate calories by month
        data.activeCaloriesTotals.forEach { calorie ->
            val startDate = dateFormat.parse(calorie.date)?.let { Date(it.time) }
            if (startDate != null) {
                calendar.time = startDate
                val monthDiff = ((2025 - 1900) * 12 + Calendar.MARCH) - ((calendar.get(Calendar.YEAR) - 1900) * 12 + calendar.get(
                    Calendar.MONTH))
                val monthIndex = 5 - monthDiff // Reverse to align with labels (0 = earliest month)
                if (monthIndex in 0..5) {
                    calorieMap[monthIndex] = calorieMap[monthIndex]!! + (calorie.caloriesBurned.toFloat() ?: 0f)
                }
            }
        }

        val entries = calorieMap.values.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
        return Pair(entries, labels)
    }
    private fun lineChartForSixMonths(){
        val entries = listOf(
            Entry(0f, 72f), // Jan
            Entry(1f, 58f), // Feb
            Entry(2f, 68f), // Mar
            Entry(3f, 86f), // Apr
            Entry(4f, 72f), // May
            Entry(5f, 0f)   // Jun (Dummy data for axis alignment)
        )

        val dataSet = LineDataSet(entries, "Performance").apply {
            color = Color.BLUE
            valueTextSize = 12f
            setCircleColor(Color.BLUE)
            setDrawCircleHole(false)
            setDrawValues(false)
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Define months from Jan to Jun
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")

        // Chart configurations
        lineChart.apply {
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(months)
                textSize = 12f
                granularity = 1f // Ensures each month is evenly spaced
                setDrawGridLines(false)
            }
            axisRight.isEnabled = false
            invalidate()
        }

        // Wait until the chart is drawn to get correct positions
        lineChart.post {
            val viewPortHandler: ViewPortHandler = lineChart.viewPortHandler
            val transformer: Transformer = lineChart.getTransformer(YAxis.AxisDependency.LEFT)

            for (entry in entries) {
                // Ignore dummy entry for June (y=0)
                if (entry.x >= 5) continue

                // Convert chart values (data points) into pixel coordinates
                val pixelValues = transformer.getPixelForValues(entry.x, entry.y)

                val xPosition = pixelValues.x // X coordinate on screen
                val yPosition = pixelValues.y // Y coordinate on screen

                // Create a rounded strip dynamically
                val stripView = View(requireContext()).apply {
                    layoutParams = FrameLayout.LayoutParams(100, 12) // Wider height for smooth curves

                    // Create a rounded background for the strip
                    background = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 6f // Round all corners
                        setColor(if (entry.y >= 70) Color.GREEN else Color.RED)
                    }

                    x = (xPosition).toFloat()  // Adjust X to center the strip
                    y = (yPosition - 6).toFloat()   // Adjust Y so it overlaps correctly
                }

                // Add the strip overlay dynamically
                stripsContainer.addView(stripView)
            }
        }
    }
}