package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.macros

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.ChartAnimator
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
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.tab.FrequentlyLoggedListAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.frequentlylogged.LoggedBottomSheet
import com.google.android.flexbox.FlexboxLayout
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.ActiveCaloriesResponse
import com.jetsynthesys.rightlife.ai_package.model.MindfullResponse
import com.jetsynthesys.rightlife.ai_package.model.RestingHeartRateResponse
import com.jetsynthesys.rightlife.ai_package.model.response.ConsumedCaloriesResponse
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.DailyCalorieData
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.WeeklyCalorieData
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentCalorieBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalorieFragment : BaseFragment<FragmentCalorieBinding>() {

    private lateinit var barChart: BarChart
    private lateinit var radioGroup: RadioGroup
    private lateinit var backwardImage: ImageView
    private lateinit var forwardImage: ImageView
    private var selectedWeekDate: String = ""
    private var selectedMonthDate: String = ""
    private var selectedHalfYearlyDate: String = ""
    private lateinit var selectedDate: TextView
    private lateinit var selectedItemDate: TextView
    private lateinit var selectHeartRateLayout: CardView
    private lateinit var selectedCalorieTv: TextView
    private lateinit var averageBurnCalorie: TextView
    private lateinit var averageHeading: TextView
    private lateinit var percentageTv: TextView
    private lateinit var percentageIc: TextView
    private lateinit var layoutLineChart: FrameLayout
    private lateinit var stripsContainer: FrameLayout
    private lateinit var lineChart: LineChart

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCalorieBinding
        get() = FragmentCalorieBinding::inflate

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.meal_log_background))

        barChart = view.findViewById(R.id.heartRateChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        backwardImage = view.findViewById(R.id.backward_image_calorie)
        forwardImage = view.findViewById(R.id.forward_image_calorie)
        selectedDate = view.findViewById(R.id.selectedDateCalorie)
        selectedItemDate = view.findViewById(R.id.selectedDate)
        selectHeartRateLayout = view.findViewById(R.id.selectCalorieLayout)
        selectedCalorieTv = view.findViewById(R.id.selectedCalorieTv)
        percentageTv = view.findViewById(R.id.percentage_text)
        averageBurnCalorie = view.findViewById(R.id.average_number)
        averageHeading = view.findViewById(R.id.averageHeading)
        percentageIc = view.findViewById(R.id.percentageIc)
        layoutLineChart = view.findViewById(R.id.lyt_line_chart)
        stripsContainer = view.findViewById(R.id.stripsContainer)
        lineChart = view.findViewById(R.id.heartLineChart)

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)
        fetchActiveCalories("last_weekly")
       /* fetchActiveCalories("last_weekly")
        setupLineChart()*/

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek -> fetchActiveCalories("last_weekly")
                R.id.rbMonth -> fetchActiveCalories("last_monthly")
                R.id.rbSixMonths -> {Toast.makeText(requireContext(),"Coming Soon",Toast.LENGTH_SHORT).show()}
            }
        }

        backwardImage.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            var selectedTab: String = "Week"
            if (selectedId != -1) {
                val selectedRadioButton = view.findViewById<RadioButton>(selectedId)
                selectedTab = selectedRadioButton.text.toString()
            }

            if (selectedTab == "Week") {
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
                fetchActiveCalories("last_weekly")
            } else if (selectedTab == "Month") {
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
                fetchActiveCalories("last_monthly")
            } else {
                Toast.makeText(requireContext(),"Coming Soon",Toast.LENGTH_SHORT).show()
               /* selectedHalfYearlyDate = ""
                fetchActiveCalories("last_six_months")*/
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

            if (selectedTab == "Week") {
                if (selectedWeekDate != currentDate) {
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
                    fetchActiveCalories("last_weekly")
                } else {
                    Toast.makeText(context, "Not selected future date", Toast.LENGTH_SHORT).show()
                }
            } else if (selectedTab == "Month") {
                if (selectedMonthDate != currentDate) {
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
                    fetchActiveCalories("last_monthly")
                } else {
                    Toast.makeText(context, "Not selected future date", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(),"Coming Soon",Toast.LENGTH_SHORT).show()
               /* if (selectedHalfYearlyDate != currentDate) {
                    selectedHalfYearlyDate = ""
                    fetchActiveCalories("last_six_months")
                } else {
                    Toast.makeText(context, "Not selected future date", Toast.LENGTH_SHORT).show()
                }*/
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
    private fun updateChart(entries: List<BarEntry>, labels: List<String>, labelsDate: List<String>) {
        val dataSet = BarDataSet(entries, "Calories Burned")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.light_green)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black_no_meals)
        dataSet.valueTextSize = 12f
        if (entries.size > 7) {
            dataSet.setDrawValues(false)
        } else {
            dataSet.setDrawValues(true)
        }
        dataSet.barShadowColor = Color.TRANSPARENT
        dataSet.highLightColor = ContextCompat.getColor(requireContext(), R.color.light_green)
        val barData = BarData(dataSet)
        barData.barWidth = 0.4f
        barChart.data = barData
        barChart.setFitBars(true)
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels) // Set custom labels
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        /*xAxis.textSize = 10 Validated that no issues are introduced by the changes.f
                xAxis.granularity = 1f*/
        xAxis.labelCount = labels.size
        xAxis.setDrawLabels(true)
        xAxis.labelRotationAngle = 0f // optional, for vertical display
        xAxis.setDrawGridLines(false)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.black_no_meals)
        xAxis.yOffset = 15f // Move labels down
        val leftYAxis: YAxis = barChart.axisLeft
        leftYAxis.textSize = 12f
        leftYAxis.textColor = ContextCompat.getColor(requireContext(), R.color.black_no_meals)
        leftYAxis.setDrawGridLines(true)
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
                    Log.d("ChartClick", "Clicked X: $x, Y: $y")
                    selectedItemDate.text = labelsDate.get(x)
                    selectedCalorieTv.text = y.toInt().toString()
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

    private fun getWeekLabelsDate(): List<String> {
        return listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    /** Fetch and update chart with API data */
    private fun fetchActiveCalories(period: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                var selectedDate = currentDateTime.format(formatter)

                // Run date selection + UI-safe calls on the Main thread
                withContext(Dispatchers.Main) {
                    when (period) {
                        "last_weekly" -> {
                            if (selectedWeekDate.isEmpty()) {
                                selectedWeekDate = selectedDate
                            } else {
                                selectedDate = selectedWeekDate
                            }
                            setSelectedDate(selectedWeekDate)
                        }

                        "last_monthly" -> {
                            if (selectedMonthDate.isEmpty()) {
                                val firstDate = getFirstDateOfMonth(selectedDate, 1)
                                selectedMonthDate = firstDate
                                selectedDate = firstDate
                            } else {
                                selectedDate = getFirstDateOfMonth(selectedMonthDate, 1)
                            }
                            setSelectedDateMonth(selectedMonthDate, "Month")
                        }

                        else -> { // last_six_months or default
                            if (selectedHalfYearlyDate.isEmpty()) {
                                val firstDate = getFirstDateOfMonth(selectedDate, 1)
                                selectedHalfYearlyDate = firstDate
                                selectedDate = firstDate
                            } else {
                                selectedDate = getFirstDateOfMonth(selectedHalfYearlyDate, 1)
                            }
                            setSelectedDateMonth(selectedHalfYearlyDate, "Year")
                        }
                    }
                }

                // Proceed with API call
                val response = ApiClient.apiServiceFastApi.getConsumedCalories(
                    userId = userId,
                    period = period,
                    date = selectedDate
                )

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.statusCode == 200) {
                        val (entries, labels, labelsDate) = when (period) {
                            "last_weekly" -> processWeeklyData(data, selectedDate)
                            "last_monthly" -> processMonthlyData(data, selectedDate)
                            "last_six_months" -> processSixMonthsData(data, selectedDate)
                            else -> Triple(getWeekData(), getWeekLabels(), getWeekLabelsDate())
                        }

                        // Ensure all UI updates are on the main thread
                        withContext(Dispatchers.Main) {
                            if (data.consumedCalorieTotals.size > 31) {
                                barChart.visibility = View.GONE
                                layoutLineChart.visibility = View.VISIBLE
                            } else {
                                barChart.visibility = View.VISIBLE
                                layoutLineChart.visibility = View.GONE
                                updateChart(entries, labels, labelsDate) // Must be thread-safe (UI updates on main thread)
                            }
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error: ${response.code()} - ${response.message()}", Toast.LENGTH_SHORT).show()
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
    private fun processWeeklyData(
        activeCaloriesResponse: ConsumedCaloriesResponse,
        currentDate: String
    ): Triple<List<BarEntry>, List<String>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dateString = currentDate
        val date = dateFormat.parse(dateString)
        calendar.time = date!!
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(year, month, day)
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        val calorieMap = mutableMapOf<String, Float>()
        val labels = mutableListOf<String>()
        val labelsDate = mutableListOf<String>()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())

        // Initialize 7 days with 0 calories
        repeat(7) {
            val dateStr = dateFormat.format(calendar.time)
            calorieMap[dateStr] = 0f
            val dateLabel = "${convertDate(dateStr)}, $year"
            val dayString = dayFormat.format(calendar.time)
            labels.add(dayString)
            labelsDate.add(dateLabel)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        // Aggregate calories by day
        if (activeCaloriesResponse.consumedCalorieTotals.isNotEmpty()) {
            activeCaloriesResponse.consumedCalorieTotals.forEach { calorie ->
                val startDate = dateFormat.parse(calorie.date)?.let { Date(it.time) }
                if (startDate != null) {
                    val dayKey = dateFormat.format(startDate)
                    calorieMap[dayKey] = calorieMap[dayKey]!! + (calorie.caloriesConsumed.toFloat() ?: 0f)
                }
            }
        }
        setLastAverageValue(activeCaloriesResponse, "% Past Week")
        val entries = calorieMap.values.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
        return Triple(entries, labels, labelsDate)
    }

    /** Process API data for last_monthly (5 weeks) */
    private fun processMonthlyData(
        activeCaloriesResponse: ConsumedCaloriesResponse,
        currentDate: String
    ): Triple<List<BarEntry>, List<String>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dateString = currentDate
        val date = dateFormat.parse(dateString)
        calendar.time = date!!
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(year, month, day)
        val calorieMap = mutableMapOf<String, Float>()
        val weeklyLabels = mutableListOf<String>()
        val labelsDate = mutableListOf<String>()

        val days = getDaysInMonth(month + 1, year)
        repeat(days) {
            val dateStr = dateFormat.format(calendar.time)
            calorieMap[dateStr] = 0f
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        for (i in 0 until days) {
            weeklyLabels.add(
                when (i) {
                    2 -> "1-7"
                    9 -> "8-14"
                    15 -> "15-21"
                    22 -> "22-28"
                    29 -> "29-31"
                    else -> "" // empty string hides the label
                }
            )
            val dateLabel = "${convertMonth(dateString)}, $year"
            if (i < 7) {
                labelsDate.add("1-7 $dateLabel")
            } else if (i < 14) {
                labelsDate.add("8-14 $dateLabel")
            } else if (i < 21) {
                labelsDate.add("15-21 $dateLabel")
            } else if (i < 28) {
                labelsDate.add("22-28 $dateLabel")
            } else {
                labelsDate.add("29-31 $dateLabel")
            }
        }
        // Aggregate calories by week
        if (activeCaloriesResponse.consumedCalorieTotals.isNotEmpty()) {
            activeCaloriesResponse.consumedCalorieTotals.forEach { calorie ->
                val startDate = dateFormat.parse(calorie.date)?.let { Date(it.time) }
                if (startDate != null) {
                    val dayKey = dateFormat.format(startDate)
                    calorieMap[dayKey] = calorieMap[dayKey]!! + (calorie.caloriesConsumed.toFloat() ?: 0f)
                }
            }
        }
        setLastAverageValue(activeCaloriesResponse, "% Past Month")
        val entries = calorieMap.values.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
        return Triple(entries, weeklyLabels, labelsDate)
    }

    /** Process API data for last_six_months (6 months) */
    private fun processSixMonthsData(
        activeCaloriesResponse: ConsumedCaloriesResponse,
        currentDate: String
    ): Triple<List<BarEntry>, List<String>, List<String>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.MARCH, 24)
        calendar.add(Calendar.MONTH, -5) // Start 6 months back

        val calorieMap = mutableMapOf<Int, Float>()
        val labels = mutableListOf<String>()
        val labelsDate = mutableListOf<String>()

        // Initialize 6 months
        repeat(6) { month ->
            calorieMap[month] = 0f
            labels.add(monthFormat.format(calendar.time))
            calendar.add(Calendar.MONTH, 1)
        }

        if (activeCaloriesResponse.consumedCalorieTotals.isNotEmpty()) {
            // Aggregate calories by month
            activeCaloriesResponse.consumedCalorieTotals.forEach { calorie ->
                val startDate = dateFormat.parse(calorie.date)?.let { Date(it.time) }
                if (startDate != null) {
                    calendar.time = startDate
                    val monthDiff = ((2025 - 1900) * 12 + Calendar.MARCH) - ((calendar.get(Calendar.YEAR) - 1900) * 12 + calendar.get(Calendar.MONTH))
                    val monthIndex = 5 - monthDiff // Reverse to align with labels (0 = earliest month)
                    if (monthIndex in 0..5) {
                        calorieMap[monthIndex] = calorieMap[monthIndex]!! + (calorie.caloriesConsumed.toFloat() ?: 0f)
                    }
                }
            }
        }
        setLastAverageValue(activeCaloriesResponse, "% Past 6 Month")
        val entries = calorieMap.values.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
        return Triple(entries, labels, labelsDate)
    }

    private fun setSelectedDate(selectedWeekDate: String) {
        // Called on main thread via fetchActiveCalories
        activity?.runOnUiThread {
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
            val dateView: String = "${convertDate(dateStr)}-${convertDate(selectedWeekDate)}, $year"
            selectedDate.text = dateView
            selectedDate.gravity = Gravity.CENTER
        }
    }

    private fun setSelectedDateMonth(selectedMonthDate: String, dateViewType: String) {
        // Called on main thread via fetchActiveCalories
        activity?.runOnUiThread {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            val dateString = selectedMonthDate
            val date = dateFormat.parse(dateString)
            calendar.time = date!!
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            if (dateViewType == "Month") {
                val lastDayOfMonth = getDaysInMonth(month + 1, year)
                val lastDateOfMonth = getFirstDateOfMonth(selectedMonthDate, lastDayOfMonth)
                val dateView: String = "${convertDate(selectedMonthDate)}-${convertDate(lastDateOfMonth)}, $year"
                selectedDate.text = dateView
            } else {
                selectedDate.text = year.toString()
            }
            selectedDate.gravity = Gravity.CENTER
        }
    }

    private fun setLastAverageValue(activeCaloriesResponse: ConsumedCaloriesResponse, type: String) {
        activity?.runOnUiThread {
            averageBurnCalorie.text = activeCaloriesResponse.currentAvgCalories.toInt().toString()
            if (activeCaloriesResponse.progressSign == "plus") {
                percentageTv.text = "${activeCaloriesResponse.progressPercentage.toInt()} $type"
                // percentageIc.setImageResource(R.drawable.ic_up)
            } else if (activeCaloriesResponse.progressSign == "minus") {
                percentageTv.text = "${activeCaloriesResponse.progressPercentage.toInt()} $type"
                // percentageIc.setImageResource(R.drawable.ic_down)
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

    private fun formatDate(date: Date, format: String): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }

    private fun updateDateRangeLabel() {
        val sdf = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        // dateRangeLabel.text = "${sdf.format(viewModel.startDate)} - ${sdf.format(viewModel.endDate)}"
    }

}