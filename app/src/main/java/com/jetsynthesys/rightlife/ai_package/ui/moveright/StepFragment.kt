package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.response.StepTrackerData
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.ai_package.ui.steps.SetYourStepGoalFragment
import com.jetsynthesys.rightlife.databinding.FragmentStepBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class StepFragment : BaseFragment<FragmentStepBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStepBinding
        get() = FragmentStepBinding::inflate

    private lateinit var barChart: BarChart
    private lateinit var radioGroup: RadioGroup
    private lateinit var backwardImage: ImageView
    private lateinit var forwardImage: ImageView
    private var selectedWeekDate: String = ""
    private var selectedMonthDate: String = ""
    private var selectedHalfYearlyDate: String = ""
    private lateinit var selectedDate: TextView
    private lateinit var selectedItemDate: TextView
    private lateinit var heart_rate_description_heading: TextView
    private lateinit var step_discreption: TextView
    private lateinit var selectHeartRateLayout: CardView
    private lateinit var layout_btn_log_meal: LinearLayoutCompat
    private lateinit var selectedCalorieTv: TextView
    private lateinit var averageBurnCalorie: TextView
    private lateinit var averageHeading: TextView
    private lateinit var percentageTv: TextView
    private lateinit var percentageIc: ImageView
    private lateinit var back_button_calorie_balance: ImageView
    private lateinit var layoutLineChart: FrameLayout
    private lateinit var stripsContainer: FrameLayout
    private lateinit var lineChart: LineChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set background
        view.setBackgroundResource(R.drawable.gradient_color_background_workout)

        // Initialize views
        barChart = view.findViewById(R.id.heartRateChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        backwardImage = view.findViewById(R.id.backwardImage)
        forwardImage = view.findViewById(R.id.forwardImage)
        selectedDate = view.findViewById(R.id.selectedDate)
        selectedItemDate = view.findViewById(R.id.selectedItemDate)
        selectHeartRateLayout = view.findViewById(R.id.selectHeartRateLayout)
        selectedCalorieTv = view.findViewById(R.id.selectedCalorieTv)
        percentageTv = view.findViewById(R.id.percentageTv)
        heart_rate_description_heading = view.findViewById(R.id.heart_rate_description_heading)
        step_discreption = view.findViewById(R.id.step_discreption)
        layout_btn_log_meal = view.findViewById(R.id.layout_btn_log_meal)
        averageBurnCalorie = view.findViewById(R.id.averageBurnCalorie)
        averageHeading = view.findViewById(R.id.averageHeading)
        percentageIc = view.findViewById(R.id.percentageIc)
        layoutLineChart = view.findViewById(R.id.lyt_line_chart)
        stripsContainer = view.findViewById(R.id.stripsContainer)
        lineChart = view.findViewById(R.id.heartLineChart)
        layout_btn_log_meal.setOnClickListener {
            navigateToFragment(SetYourStepGoalFragment(), "HomeBottomTabFragment")
        }
        back_button_calorie_balance = view.findViewById(R.id.step_back)
        back_button_calorie_balance.setOnClickListener {
            navigateToFragment(HomeBottomTabFragment(), "HomeBottomTabFragment")
        }

        // Update heading to reflect steps
        averageHeading.text = "Average Steps"

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)
        fetchStepDetails("last_weekly")
        setupLineChart()

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek -> fetchStepDetails("last_weekly")
                R.id.rbMonth -> fetchStepDetails("last_monthly")
                R.id.rbSixMonths -> fetchStepDetails("last_six_months")
            }
        }

        backwardImage.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            var selectedTab: String = "Week"
            if (selectedId != -1) {
                val selectedRadioButton = view.findViewById<RadioButton>(selectedId)
                selectedTab = selectedRadioButton.text.toString()
            }

            if (selectedTab.contentEquals("Week")) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val calendar = Calendar.getInstance()
                val dateString = selectedWeekDate
                val date = dateFormat.parse(dateString)
                calendar.time = date!!
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                calendar.set(year, month, day)
                calendar.add(Calendar.DAY_OF_YEAR, -7)
                val dateStr = dateFormat.format(calendar.time)
                selectedWeekDate = dateStr
                fetchStepDetails("last_weekly")
            } else if (selectedTab.contentEquals("Month")) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val calendar = Calendar.getInstance()
                val dateString = selectedMonthDate
                val date = dateFormat.parse(dateString)
                calendar.time = date!!
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                calendar.set(year, month - 1, day)
                val dateStr = dateFormat.format(calendar.time)
                val firstDateOfMonth = getFirstDateOfMonth(dateStr, 1)
                selectedMonthDate = firstDateOfMonth
                fetchStepDetails("last_monthly")
            } else {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val calendar = Calendar.getInstance()
                val dateString = selectedHalfYearlyDate
                val date = dateFormat.parse(dateString)
                calendar.time = date!!
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                calendar.set(year, month - 6, day)
                val dateStr = dateFormat.format(calendar.time)
                selectedHalfYearlyDate = dateStr
                fetchStepDetails("last_six_months")
            }
        }

        forwardImage.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            var selectedTab: String = "Week"
            if (selectedId != -1) {
                val selectedRadioButton = view.findViewById<RadioButton>(selectedId)
                selectedTab = selectedRadioButton.text.toString()
            }
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val currentDate: String = currentDateTime.format(formatter)

            if (selectedTab.contentEquals("Week")) {
                if (!selectedWeekDate.contentEquals(currentDate)) {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val calendar = Calendar.getInstance()
                    val dateString = selectedWeekDate
                    val date = dateFormat.parse(dateString)
                    calendar.time = date!!
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    calendar.set(year, month, day)
                    calendar.add(Calendar.DAY_OF_YEAR, +7)
                    val dateStr = dateFormat.format(calendar.time)
                    selectedWeekDate = dateStr
                    fetchStepDetails("last_weekly")
                } else {
                    Toast.makeText(context, "Not selected future date", Toast.LENGTH_SHORT).show()
                }
            } else if (selectedTab.contentEquals("Month")) {
                if (!selectedMonthDate.contentEquals(currentDate)) {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val calendar = Calendar.getInstance()
                    val dateString = selectedMonthDate
                    val date = dateFormat.parse(dateString)
                    calendar.time = date!!
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    calendar.set(year, month + 1, day)
                    val dateStr = dateFormat.format(calendar.time)
                    val firstDateOfMonth = getFirstDateOfMonth(dateStr, 1)
                    selectedMonthDate = firstDateOfMonth
                    fetchStepDetails("last_monthly")
                } else {
                    Toast.makeText(context, "Not selected future date", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (!selectedHalfYearlyDate.contentEquals(currentDate)) {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val calendar = Calendar.getInstance()
                    val dateString = selectedHalfYearlyDate
                    val date = dateFormat.parse(dateString)
                    calendar.time = date!!
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    calendar.set(year, month + 6, day)
                    val dateStr = dateFormat.format(calendar.time)
                    selectedHalfYearlyDate = dateStr
                    fetchStepDetails("last_six_months")
                } else {
                    Toast.makeText(context, "Not selected future date", Toast.LENGTH_SHORT).show()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToFragment(HomeBottomTabFragment(), "landingFragment")
        }
    }

    private fun navigateToFragment(fragment: Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    /** Update BarChart with new data and add dotted lines for total_steps_count and total_steps_avg */
    private fun updateChart(entries: List<BarEntry>, labels: List<String>, labelsDate: List<String>, stepData: StepTrackerData) {
        // Log the data for debugging
        Log.d("StepFragment", "Entries size: ${entries.size}, Labels size: ${labels.size}, LabelsDate size: ${labelsDate.size}")
        Log.d("StepFragment", "Entries: $entries")
        Log.d("StepFragment", "Labels: $labels")
        Log.d("StepFragment", "LabelsDate: $labelsDate")

        val dataSet = BarDataSet(entries, "Steps")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.moveright)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black_no_meals)
        dataSet.valueTextSize = 12f
        if (entries.size > 7) {
            dataSet.setDrawValues(false)
        } else {
            dataSet.setDrawValues(true)
        }
        dataSet.barShadowColor = Color.TRANSPARENT
        dataSet.highLightColor = ContextCompat.getColor(requireContext(), R.color.light_orange)
        val barData = BarData(dataSet)
        barData.barWidth = 0.4f
        barChart.data = barData
        barChart.setFitBars(true)

        // Dynamically set Y-axis range to handle step counts
        val leftYAxis: YAxis = barChart.axisLeft
        leftYAxis.textSize = 12f
        leftYAxis.textColor = ContextCompat.getColor(requireContext(), R.color.black_no_meals)
        leftYAxis.setDrawGridLines(true)

        // Calculate min and max values from entries, including totalStepsCount and totalStepsAvg
        val minValue = minOf(entries.minOfOrNull { it.y } ?: 0f, stepData.totalStepsCount.toFloat(), stepData.totalStepsAvg.toFloat())
        val maxValue = maxOf(entries.maxOfOrNull { it.y } ?: 0f, stepData.totalStepsCount.toFloat(), stepData.totalStepsAvg.toFloat())
        // Set axis range with some padding
        leftYAxis.axisMinimum = if (minValue < 0) minValue * 1.2f else 0f
        leftYAxis.axisMaximum = if (maxValue > 0) maxValue * 1.2f else 10000f // Default max for steps
        leftYAxis.setDrawZeroLine(true) // Show zero line for clarity
        leftYAxis.zeroLineColor = Color.BLACK
        leftYAxis.zeroLineWidth = 1f

        // Add dotted line for total_steps_count
        val totalStepsLine = LimitLine(stepData.totalStepsCount.toFloat(), "Total Steps: ${stepData.totalStepsCount.toInt()}")
        totalStepsLine.lineColor = Color.BLUE
        totalStepsLine.lineWidth = 1f
        totalStepsLine.enableDashedLine(10f, 10f, 0f) // Dotted line: 10f dash length, 10f gap length
        totalStepsLine.textColor = Color.BLUE
        totalStepsLine.textSize = 10f
        totalStepsLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP

        // Add dotted line for total_steps_avg
        val avgStepsLine = LimitLine(stepData.stepsGoal.toFloat(), "Goal Steps: ${stepData.stepsGoal.toInt()}")
        avgStepsLine.lineColor = Color.GREEN
        avgStepsLine.lineWidth = 1f
        avgStepsLine.enableDashedLine(10f, 10f, 0f) // Dotted line: 10f dash length, 10f gap length
        avgStepsLine.textColor = Color.GREEN
        avgStepsLine.textSize = 10f
        avgStepsLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP

        // Clear existing limit lines and add new ones
        leftYAxis.removeAllLimitLines()
        leftYAxis.addLimitLine(totalStepsLine)
        leftYAxis.addLimitLine(avgStepsLine)

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.granularity = 1f
        xAxis.labelCount = labels.size
        xAxis.setDrawLabels(true)
        xAxis.labelRotationAngle = 0f
        xAxis.setDrawGridLines(false)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.black_no_meals)
        xAxis.yOffset = 15f

        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setExtraOffsets(0f, 0f, 0f, 0f)
        val legend = barChart.legend
        legend.setDrawInside(false)
        barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                selectHeartRateLayout.visibility = View.VISIBLE
                if (e != null) {
                    val x = e.x.toInt()
                    val y = e.y
                    Log.d("ChartClick", "Clicked X: $x, Steps: $y, LabelsDate size: ${labelsDate.size}")
                    // Check if x is within bounds of labelsDate
                    if (x in 0 until labelsDate.size) {
                        selectedItemDate.text = labelsDate[x]
                        selectedCalorieTv.text = y.toInt().toString() // Display steps
                    } else {
                        Log.e("ChartClick", "Index $x out of bounds for labelsDate size ${labelsDate.size}")
                        selectHeartRateLayout.visibility = View.INVISIBLE
                    }
                }
            }
            override fun onNothingSelected() {
                Log.d("ChartClick", "Nothing selected")
                selectHeartRateLayout.visibility = View.INVISIBLE
            }
        })
        barChart.animateY(1000)
        barChart.invalidate()
    }

    /** Fetch and update chart with API data */
    private fun fetchStepDetails(period: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                var selectedDate: String

                if (period.contentEquals("last_weekly")) {
                    if (selectedWeekDate.contentEquals("")) {
                        selectedDate = currentDateTime.format(formatter)
                        selectedWeekDate = selectedDate
                    } else {
                        selectedDate = selectedWeekDate
                    }
                    setSelectedDate(selectedWeekDate)
                } else if (period.contentEquals("last_monthly")) {
                    if (selectedMonthDate.contentEquals("")) {
                        selectedDate = currentDateTime.format(formatter)
                        val firstDateOfMonth = getFirstDateOfMonth(selectedDate, 1)
                        selectedDate = firstDateOfMonth
                        selectedMonthDate = firstDateOfMonth
                    } else {
                        val firstDateOfMonth = getFirstDateOfMonth(selectedMonthDate, 1)
                        selectedDate = firstDateOfMonth
                    }
                    setSelectedDateMonth(selectedMonthDate, "Month")
                } else {
                    if (selectedHalfYearlyDate.contentEquals("")) {
                        selectedDate = currentDateTime.format(formatter)
                        val firstDateOfMonth = getFirstDateOfMonth(selectedDate, 1)
                        selectedDate = firstDateOfMonth
                        selectedHalfYearlyDate = firstDateOfMonth
                    } else {
                        val firstDateOfMonth = getFirstDateOfMonth(selectedHalfYearlyDate, 1)
                        selectedDate = firstDateOfMonth
                    }
                    setSelectedDateMonth(selectedHalfYearlyDate, "Year")
                }

                val response = ApiClient.apiServiceFastApi.getStepsDetail(
                    userId = userId,
                    period = period,
                    date = selectedDate
                )
                if (response.isSuccessful) {
                    val stepTrackerResponse = response.body()
                    if (stepTrackerResponse?.statusCode == 200 && stepTrackerResponse.data.isNotEmpty()) {
                        val stepData = stepTrackerResponse.data[0] // Assuming single data entry
                        withContext(Dispatchers.Main) {
                            if (period == "last_six_months") {
                                barChart.visibility = View.GONE
                                layoutLineChart.visibility = View.VISIBLE
                                lineChartForSixMonths(stepData)
                            } else {
                                barChart.visibility = View.VISIBLE
                                layoutLineChart.visibility = View.GONE
                                val (entries, labels, labelsDate) = when (period) {
                                    "last_weekly" -> processWeeklyData(stepData, selectedDate)
                                    "last_monthly" -> processMonthlyData(stepData, selectedDate)
                                    else -> processWeeklyData(stepData, selectedDate) // Fallback
                                }
                                updateChart(entries, labels, labelsDate, stepData)
                            }
                            // Update average and comparison UI
                            setStepStats(stepData, period)
                            heart_rate_description_heading.text = stepTrackerResponse.data.get(0).heading.toString()
                            step_discreption.text = stepTrackerResponse.data.get(0).description.toString()

                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "No step data available", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Step data  ${response.message()}", Toast.LENGTH_SHORT).show()
                        barChart.visibility = View.GONE
                        averageBurnCalorie.text = "0"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /** Process API data for last_weekly period (7 days) */
    private fun processWeeklyData(stepData: StepTrackerData, currentDate: String): Triple<List<BarEntry>, List<String>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dateString = currentDate
        val date = dateFormat.parse(dateString)
        calendar.time = date!!
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(year, month, day)
        calendar.add(Calendar.DAY_OF_YEAR, -6) // Start of the week
        val stepMap = mutableMapOf<String, Float>()
        val labels = mutableListOf<String>()
        val labelsDate = mutableListOf<String>()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())

        // Initialize 7 days with 0 steps
        repeat(7) {
            val dateStr = dateFormat.format(calendar.time)
            stepMap[dateStr] = 0f
            val dateLabel = (convertDate(dateStr) + "," + year)
            val dayString = dayFormat.format(calendar.time)
            labels.add(dayString)
            labelsDate.add(dateLabel)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Aggregate steps by day using record_details
        stepData.recordDetails.forEach { record ->
            val dayKey = record.date
            stepMap[dayKey] = record.totalStepsCountPerDay.toFloat()
        }

        val entries = stepMap.entries.mapIndexed { index, entry -> BarEntry(index.toFloat(), entry.value) }
        return Triple(entries, labels, labelsDate)
    }

    /** Process API data for monthly period (5 weeks) */
    private fun processMonthlyData(stepData: StepTrackerData, currentDate: String): Triple<List<BarEntry>, List<String>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dateString = currentDate
        val date = dateFormat.parse(dateString)
        calendar.time = date!!
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(year, month, day)
        val stepMap = mutableMapOf<String, Float>()
        val weeklyLabels = mutableListOf<String>()
        val labelsDate = mutableListOf<String>()

        val days = getDaysInMonth(month + 1, year)
        calendar.set(year, month, 1) // Start from the first day of the month
        repeat(days) {
            val dateStr = dateFormat.format(calendar.time)
            stepMap[dateStr] = 0f
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Reset calendar to the start of the month
        calendar.set(year, month, 1)
        val dateLabel = "${convertMonth(dateString)},$year"
        repeat(5) { week ->
            val weekLabel = when (week) {
                0 -> "1-7"
                1 -> "8-14"
                2 -> "15-21"
                3 -> "22-28"
                4 -> "29-${days}"
                else -> ""
            }
            if (weekLabel.isNotEmpty()) {
                weeklyLabels.add(weekLabel)
                labelsDate.add("$weekLabel $dateLabel")
            }
        }

        // Aggregate steps by day using record_details
        stepData.recordDetails.forEach { record ->
            val dayKey = record.date
            stepMap[dayKey] = stepMap[dayKey]?.plus(record.totalStepsCountPerDay.toFloat()) ?: record.totalStepsCountPerDay.toFloat()
        }

        // Aggregate into weekly buckets
        val weeklySteps = mutableListOf<Float>()
        var currentWeekSum = 0f
        var dayCount = 0
        var weekIndex = 0
        stepMap.entries.sortedBy { it.key }.forEachIndexed { index, entry ->
            currentWeekSum += entry.value
            dayCount++
            val isLastDayOfWeek = dayCount == 7 || (weekIndex == 4 && index == stepMap.size - 1)
            if (isLastDayOfWeek) {
                weeklySteps.add(currentWeekSum)
                currentWeekSum = 0f
                dayCount = 0
                weekIndex++
            }
        }

        // If the last week has fewer than 7 days, add it
        if (currentWeekSum > 0) {
            weeklySteps.add(currentWeekSum)
        }

        val entries = weeklySteps.mapIndexed { index, steps -> BarEntry(index.toFloat(), steps) }
        return Triple(entries, weeklyLabels.take(entries.size), labelsDate.take(entries.size))
    }

    /** Process API data for six_monthly period (6 months) */
    private fun processSixMonthsData(stepData: StepTrackerData, currentDate: String): Triple<List<BarEntry>, List<String>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val yearMonthFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dateString = currentDate
        val date = dateFormat.parse(dateString)
        calendar.time = date!!
        calendar.add(Calendar.MONTH, -5) // Start 6 months back

        val stepMap = mutableMapOf<Int, Float>()
        val labels = mutableListOf<String>()
        val labelsDate = mutableListOf<String>()

        // Initialize 6 months
        repeat(6) { month ->
            stepMap[month] = 0f
            labels.add(monthFormat.format(calendar.time))
            labelsDate.add(convertMonth(dateFormat.format(calendar.time)) + "," + calendar.get(Calendar.YEAR))
            calendar.add(Calendar.MONTH, 1)
        }

        // Aggregate steps by month using record_details
        stepData.recordDetails.forEach { record ->
            val recordDate = dateFormat.parse(record.date)
            if (recordDate != null) {
                calendar.time = recordDate
                val recordYearMonth = yearMonthFormat.format(recordDate)
                calendar.time = dateFormat.parse(currentDate)
                calendar.add(Calendar.MONTH, -5) // Reset to start of 6-month period
                for (monthIndex in 0..5) {
                    val periodYearMonth = yearMonthFormat.format(calendar.time)
                    if (recordYearMonth == periodYearMonth) {
                        stepMap[monthIndex] = stepMap[monthIndex]?.plus(record.totalStepsCountPerDay.toFloat()) ?: record.totalStepsCountPerDay.toFloat()
                    }
                    calendar.add(Calendar.MONTH, 1)
                }
            }
        }

        val entries = stepMap.entries.mapIndexed { index, entry -> BarEntry(index.toFloat(), entry.value) }
        return Triple(entries, labels, labelsDate)
    }

    /** Update UI with step stats (average and comparison) */
    private fun setStepStats(stepData: StepTrackerData, period: String) {
        activity?.runOnUiThread {
            // Set average steps
            averageBurnCalorie.visibility = View.VISIBLE
            averageBurnCalorie.text = stepData.totalStepsAvg.toInt().toString()

            // Set comparison percentage
            val currentAverage = stepData.comparison.currentAverageStepsPerDay
            val previousAverage = stepData.comparison.previousAverageStepsPerDay
            val percentage = if (previousAverage != 0.0) ((currentAverage - previousAverage) / previousAverage * 100).toInt() else 0
            val periodLabel = when (period) {
                "last_weekly" -> "% Past Week"
                "last_monthly" -> "% Past Month"
                "last_six_months" -> "% Past 6 Months"
                else -> "%"
            }
            if (percentage > 0) {
                percentageTv.text = "+$percentage$periodLabel"
                percentageIc.setImageResource(R.drawable.ic_up)
            } else if (percentage < 0) {
                percentageTv.text = "$percentage$periodLabel"
                percentageIc.setImageResource(R.drawable.ic_down)
            } else {
                percentageTv.text = "0$periodLabel"
                percentageIc.setImageResource(R.drawable.ic_up)
            }
        }
    }

    private fun setSelectedDate(selectedWeekDate: String) {
        requireActivity().runOnUiThread {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            val dateString = selectedWeekDate
            val date = dateFormat.parse(dateString)
            calendar.time = date!!
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            calendar.set(year, month, day)
            calendar.add(Calendar.DAY_OF_YEAR, -6)
            val dateStr = dateFormat.format(calendar.time)
            val dateView: String = convertDate(dateStr) + "-" + convertDate(selectedWeekDate) + "," + year
            selectedDate.text = dateView
            selectedDate.gravity = Gravity.CENTER
        }
    }

    private fun setSelectedDateMonth(selectedMonthDate: String, dateViewType: String) {
        activity?.runOnUiThread {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            val dateString = selectedMonthDate
            val date = dateFormat.parse(dateString)
            calendar.time = date!!
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            if (dateViewType.contentEquals("Month")) {
                val lastDayOfMonth = getDaysInMonth(month + 1, year)
                val lastDateOfMonth = getFirstDateOfMonth(selectedMonthDate, lastDayOfMonth)
                val dateView: String = convertDate(selectedMonthDate) + "-" + convertDate(lastDateOfMonth) + "," + year
                selectedDate.text = dateView
                selectedDate.gravity = Gravity.CENTER
            } else {
                selectedDate.text = year.toString()
                selectedDate.gravity = Gravity.CENTER
            }
        }
    }

    private fun convertDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMM", Locale.getDefault())
        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }

    private fun convertMonth(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM", Locale.getDefault())
        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }

    private fun getDaysInMonth(month: Int, year: Int): Int {
        val yearMonth = YearMonth.of(year, month)
        return yearMonth.lengthOfMonth()
    }

    private fun getFirstDateOfMonth(inputDate: String, value: Int): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val parsedDate = LocalDate.parse(inputDate, formatter)
        val firstDayOfMonth = parsedDate.withDayOfMonth(value)
        return firstDayOfMonth.format(formatter)
    }

    private fun setupLineChart() {
        lineChart.apply {
            axisLeft.apply {
                axisMinimum = 0f
                setDrawGridLines(true)
                textColor = Color.BLACK
                textSize = 10f
                setLabelCount(6, true)
                setDrawAxisLine(true)
            }
            axisRight.isEnabled = false
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.BLACK
                textSize = 10f
                setDrawGridLines(true)
                setDrawLabels(true)
                granularity = 1f
                setAvoidFirstLastClipping(true)
            }
            description.isEnabled = false
            setTouchEnabled(true)
            setDrawGridBackground(false)
            setDrawBorders(false)
            setNoDataText("Loading data...")
        }
    }

    private fun lineChartForSixMonths(stepData: StepTrackerData) {
        // Aggregate steps by month
        val monthFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val monthLabelFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val stepMap = mutableMapOf<String, Float>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Initialize 6 months
        calendar.time = dateFormat.parse(stepData.startDate)!!
        repeat(6) {
            val monthKey = monthFormat.format(calendar.time)
            stepMap[monthKey] = 0f
            calendar.add(Calendar.MONTH, 1)
        }

        // Aggregate steps by month
        stepData.recordDetails.forEach { record ->
            val recordDate = dateFormat.parse(record.date)
            if (recordDate != null) {
                val monthKey = monthFormat.format(recordDate)
                stepMap[monthKey] = stepMap[monthKey]?.plus(record.totalStepsCountPerDay.toFloat()) ?: record.totalStepsCountPerDay.toFloat()
            }
        }

        val entries = stepMap.entries.sortedBy { it.key }.mapIndexed { index, entry ->
            Entry(index.toFloat(), entry.value)
        }.ifEmpty { listOf(Entry(0f, 0f), Entry(1f, 0f)) }

        val dataSet = LineDataSet(entries, "Steps").apply {
            color = Color.rgb(255, 102, 128)
            lineWidth = 1f
            setDrawCircles(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.LINEAR
            fillAlpha = 20
            fillColor = Color.rgb(255, 102, 128)
            setDrawFilled(true)
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        val labels = stepMap.keys.sorted().map { monthLabelFormat.format(monthFormat.parse(it)!!) }
            .ifEmpty { listOf("No Data", "No Data") }
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        lineChart.xAxis.apply {
            labelCount = labels.size.coerceAtLeast(2)
        }

        lineChart.axisLeft.apply {
            axisMaximum = (entries.maxOfOrNull { it.y }?.times(1.2f) ?: 10000f) // Default max for steps
            valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }
        }

        lineChart.animateX(1000)
        lineChart.animateY(1000)
        lineChart.invalidate()

        lineChart.post {
            val transformer = lineChart.getTransformer(YAxis.AxisDependency.LEFT)
            val hMargin = 40f
            val vMargin = 20f
            stripsContainer.removeAllViews()
            stepMap.entries.sortedBy { it.key }.forEachIndexed { index, entry ->
                val monthSteps = entry.value
                val monthLabel = labels[index]
                val xStartFraction = index.toFloat() / (stepMap.size - 1)
                val xEndFraction = index.toFloat() / (stepMap.size - 1)
                val yFraction = monthSteps / (entries.maxOfOrNull { it.y }?.toFloat() ?: 1f)
                val pixelStart = transformer.getPixelForValues(
                    (xStartFraction * (lineChart.width - 2 * hMargin)) + hMargin,
                    ((1 - yFraction) * (lineChart.height - 2 * vMargin) + vMargin).toFloat()
                )
                val pixelEnd = transformer.getPixelForValues(
                    (xEndFraction * (lineChart.width - 2 * hMargin)) + hMargin,
                    ((1 - yFraction) * (lineChart.height - 2 * vMargin) + vMargin).toFloat()
                )

                // Draw capsule
                val capsuleView = View(requireContext()).apply {
                    val width = (pixelEnd.x - pixelStart.x).toInt().coerceAtLeast(20)
                    layoutParams = FrameLayout.LayoutParams(width, 8)
                    background = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 4f
                        setColor(Color.RED)
                    }
                    x = pixelStart.x.toFloat()
                    y = (pixelStart.y - 3).toFloat()
                }
                stripsContainer.addView(capsuleView)

                // Average label
                val avgText = TextView(requireContext()).apply {
                    text = "${monthSteps.toInt()}"
                    setTextColor(Color.BLACK)
                    textSize = 12f
                    setPadding(6, 4, 6, 4)
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    x = (((pixelStart.x + pixelEnd.x) / 2 - 20).toFloat())
                    y = (pixelStart.y - 20).toFloat()
                }
                stripsContainer.addView(avgText)
            }
        }
    }
}