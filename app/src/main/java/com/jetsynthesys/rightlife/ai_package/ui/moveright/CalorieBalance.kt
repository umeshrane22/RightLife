package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
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
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentCalorieBalanceBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalorieBalance : BaseFragment<FragmentCalorieBalanceBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCalorieBalanceBinding
        get() = FragmentCalorieBalanceBinding::inflate

    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart
    private lateinit var layoutLineChart: FrameLayout
    private lateinit var stripsContainer: FrameLayout
    private lateinit var backButton: ImageView
    private lateinit var leftArrow: ImageView
    private lateinit var rightArrow: ImageView
    private lateinit var dateRangeLabel: TextView
    private lateinit var averageText: TextView
    private lateinit var tabGroup: RadioGroup
    private val viewModel: StepsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set background
        view.setBackgroundResource(R.drawable.gradient_color_background_workout)

        // Initialize views
        barChart = view.findViewById(R.id.heartRateChart)
        lineChart = view.findViewById(R.id.heartLineChart)
        layoutLineChart = view.findViewById(R.id.lyt_line_chart)
        stripsContainer = view.findViewById(R.id.stripsContainer)
        backButton = view.findViewById(R.id.back_button)
        leftArrow = view.findViewById(R.id.backward_image_heart_rate)
        rightArrow = view.findViewById(R.id.forward_image_heart_rate)
        dateRangeLabel = view.findViewById(R.id.date_range_label)
        averageText = view.findViewById(R.id.average_number)
        tabGroup = view.findViewById(R.id.tabGroup)

        // Setup charts
        setupCharts()

        // Set default tab
        tabGroup.check(R.id.rbWeek)

        // Back button click
        backButton.setOnClickListener { navigateToFragment(HomeBottomTabFragment(), "landingFragment") }

        // Arrow navigation
        leftArrow.setOnClickListener {
            viewModel.goLeft()
            updateChart()
            updateDateRangeLabel()
        }
        rightArrow.setOnClickListener {
            viewModel.goRight()
            updateChart()
            updateDateRangeLabel()
        }

        // Tab selection
        tabGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.currentRange = when (checkedId) {
                R.id.rbWeek -> RangeTypeChart.WEEK
                R.id.rbMonth -> RangeTypeChart.MONTH
                R.id.rbSixMonths -> RangeTypeChart.SIX_MONTHS
                else -> RangeTypeChart.WEEK
            }
            viewModel.setInitialRange(viewModel.currentRange)
            updateChart()
            updateDateRangeLabel()
        }

        // Back press handling
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateToFragment(HomeBottomTabFragment(), "landingFragment")
        }

        // Initial updates
        updateChart()
        updateDateRangeLabel()
        updateAverageStats()
    }

    private fun navigateToFragment(fragment: Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    private fun setupCharts() {
        setupBarChart()
        setupLineChart()
    }

    private fun setupBarChart() {
        barChart.apply {
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textSize = 12f
                granularity = 1f
                setDrawGridLines(false)
                textColor = Color.BLACK
                yOffset = 15f
            }
            axisLeft.apply {
                textSize = 12f
                textColor = Color.BLACK
                setDrawGridLines(true)
            }
            axisRight.isEnabled = false
            description.isEnabled = false
            animateY(1000)
            setNoDataText("Loading data...")
        }
    }

    private fun setupLineChart() {
        lineChart.apply {
            axisLeft.apply {
                axisMaximum = viewModel.yMax.toFloat() * 1.2f
                axisMinimum = 0f
                setDrawGridLines(true)
                textColor = Color.BLACK
                textSize = 10f
                setLabelCount(6, true)
                valueFormatter = object : IndexAxisValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return if (value == 0f) "0" else "${(value / 1000).toInt()}k"
                    }
                }
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

    private fun updateChart() {
        when (viewModel.currentRange) {
            RangeTypeChart.WEEK, RangeTypeChart.MONTH -> {
                barChart.visibility = View.VISIBLE
                layoutLineChart.visibility = View.GONE
                updateBarChart()
            }
            RangeTypeChart.SIX_MONTHS -> {
                barChart.visibility = View.GONE
                layoutLineChart.visibility = View.VISIBLE
                lineChartForSixMonths()
            }
        }
        updateAverageStats()
    }

    private fun updateBarChart() {
        val entries = if (viewModel.filteredData.isNotEmpty()) {
            viewModel.filteredData.mapIndexed { index, item ->
                BarEntry(index.toFloat(), item.steps.toFloat())
            }
        } else {
            listOf(BarEntry(0f, 0f), BarEntry(1f, 0f))
        }
        val labels = if (viewModel.filteredData.isNotEmpty()) {
            viewModel.filteredData.map { formatDate(it.date, "d\nMMM") }
        } else {
            listOf("No Data", "No Data")
        }
        Log.d("CalorieBalance", "updateBarChart: entries size = ${entries.size}, labels size = ${labels.size}")

        val dataSet = BarDataSet(entries, "Steps").apply {
            colors = listOf(Color.rgb(255, 102, 128))
            valueTextColor = Color.BLACK
            valueTextSize = 12f
            setDrawValues(false) // Hide values on bars
            barBorderWidth = 1f
           // cornerRadius = 4f // Rounded corners
        }
        val barData = BarData(dataSet).apply {
            barWidth = if (viewModel.currentRange == RangeTypeChart.WEEK) 0.4f else 0.2f
        }
        barChart.data = barData
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.axisLeft.axisMaximum = viewModel.yMax.toFloat() * 1.2f
        barChart.axisLeft.axisMinimum = 0f
        barChart.xAxis.setDrawGridLines(true) // Add grid lines
        barChart.setFitBars(true)
        barChart.invalidate()

        // Add goal and average lines
        val goalLine = com.github.mikephil.charting.data.Entry(0f, viewModel.goalSteps.toFloat())
        val limitLine = com.github.mikephil.charting.components.LimitLine(goalLine.y, "Goal")
        limitLine.lineWidth = 1f
        //limitLine.lineDashLengths = floatArrayOf(5f, 5f)
        limitLine.textSize = 10f
        //limitLine.enableDashedLine = true
        barChart.axisLeft.addLimitLine(limitLine)

        val avgSteps = viewModel.filteredData.averageSteps().toFloat().takeIf { it > 0 } ?: 0f
        val avgLine = com.github.mikephil.charting.data.Entry(0f, avgSteps)
        val avgLimitLine = com.github.mikephil.charting.components.LimitLine(avgLine.y, "Avg")
        avgLimitLine.lineWidth = 1f
        //avgLimitLine.lineDashLengths = floatArrayOf(3f, 3f)
        avgLimitLine.textSize = 10f
        //avgLimitLine.enableDashedLine = true
        barChart.axisLeft.addLimitLine(avgLimitLine)

        // Weekly labels
        if (viewModel.currentRange == RangeTypeChart.WEEK) {
            barChart.post {
                stripsContainer.removeAllViews()
                viewModel.filteredData.forEachIndexed { index, item ->
                    val transformer = barChart.getTransformer(YAxis.AxisDependency.LEFT)
                    val hMargin = 40f
                    val vMargin = 20f
                    val xFraction = index.toFloat() / viewModel.filteredData.size
                    val yFraction = item.steps.toFloat() / viewModel.yMax.toFloat()
                    val pixel = transformer.getPixelForValues(
                        (xFraction * (barChart.width - 2 * hMargin)) + hMargin,
                        (1 - yFraction) * (barChart.height - 2 * vMargin) + vMargin
                    )
                    val label = TextView(requireContext()).apply {
                        text = item.steps.toString()
                        setTextColor(Color.BLACK)
                        textSize = 12f
                        setPadding(4, 2, 4, 2)
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        x = (pixel.x - 15f).toFloat()
                        y = (pixel.y - 25f).toFloat()
                    }
                    stripsContainer.addView(label)
                }
            }
        }
    }

    private fun lineChartForSixMonths() {
        val entries = if (viewModel.filteredData.isNotEmpty()) {
            viewModel.filteredData.mapIndexed { index, item ->
                Entry(index.toFloat(), item.steps.toFloat())
            }
        } else {
            listOf(Entry(0f, 0f), Entry(1f, 0f))
        }
        Log.d("CalorieBalance", "lineChartForSixMonths: entries size = ${entries.size}")

        val dataSet = LineDataSet(entries, "Steps").apply {
            color = Color.rgb(255, 102, 128)
            lineWidth = 1f
            setDrawCircles(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.LINEAR
            fillAlpha = 20 // Slight fill for iOS-like effect
            fillColor = Color.rgb(255, 102, 128)
            setDrawFilled(true)
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(
            viewModel.monthlyGroups.map { formatDate(it.startDate, "LLL\nyyyy") }.ifEmpty { listOf("No Data", "No Data") }
        )
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.BLACK
            textSize = 10f
            setDrawGridLines(true)
            setDrawLabels(true)
            granularity = 1f
            labelCount = viewModel.monthlyGroups.size.coerceAtLeast(2)
            setAvoidFirstLastClipping(true)
        }

        lineChart.axisLeft.apply {
            axisMaximum = viewModel.yMax.toFloat() * 1.2f
            axisMinimum = 0f
            setDrawGridLines(true)
            textColor = Color.BLACK
            textSize = 10f
            setLabelCount(6, true)
            valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if (value == 0f) "0" else "${(value / 1000).toInt()}k"
                }
            }
            setDrawAxisLine(true)
        }
        lineChart.axisRight.isEnabled = false

        lineChart.animateX(1000)
        lineChart.animateY(1000)
        lineChart.invalidate()

        lineChart.post {
            val transformer = lineChart.getTransformer(YAxis.AxisDependency.LEFT)
            val hMargin = 40f
            val vMargin = 20f
            if (viewModel.currentRange == RangeTypeChart.SIX_MONTHS) {
                val overallStart = viewModel.filteredData.firstOrNull()?.date ?: viewModel.startDate
                val overallEnd = viewModel.filteredData.lastOrNull()?.date ?: viewModel.endDate
                val totalInterval = if (overallEnd.time > overallStart.time) overallEnd.time - overallStart.time.toDouble() else 1.0

                Log.d("CalorieBalance", "filteredData size: ${viewModel.filteredData.size}, overallStart: $overallStart, overallEnd: $overallEnd")

                stripsContainer.removeAllViews()
                viewModel.monthlyGroups.forEach { group ->
                    val monthEntries = viewModel.filteredData.filter { it.date >= group.startDate && it.date <= group.endDate }
                    if (monthEntries.isNotEmpty()) {
                        val startIndex = viewModel.filteredData.indexOfFirst { it.date >= group.startDate }
                        val endIndex = viewModel.filteredData.indexOfLast { it.date <= group.endDate }
                        if (startIndex >= 0 && endIndex >= 0) {
                            val xStartFraction = startIndex.toDouble() / viewModel.filteredData.size
                            val xEndFraction = endIndex.toDouble() / viewModel.filteredData.size
                            val yFraction = group.avgSteps.toDouble() / viewModel.yMax
                            val pixelStart = transformer.getPixelForValues(
                                (xStartFraction * (lineChart.width - 2 * hMargin)).toFloat() + hMargin,
                                ((1 - yFraction) * (lineChart.height - 2 * vMargin) + vMargin).toFloat()
                            )
                            val pixelEnd = transformer.getPixelForValues(
                                (xEndFraction * (lineChart.width - 2 * hMargin)).toFloat() + hMargin,
                                ((1 - yFraction) * (lineChart.height - 2 * vMargin) + vMargin).toFloat()
                            )

                            // Draw red capsule
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
                                text = "${group.avgSteps / 1000}k"
                                setTextColor(Color.BLACK)
                                textSize = 12f
                                setPadding(6, 4, 6, 4)
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                x = ((pixelStart.x + pixelEnd.x) / 2 - 20).toFloat()
                                y = (pixelStart.y - 20).toFloat()
                            }
                            stripsContainer.addView(avgText)

                            // Difference label
                            val diffText = TextView(requireContext()).apply {
                                text = group.monthDiffString
                                setTextColor(if (group.isPositiveChange) Color.GREEN else Color.RED)
                                textSize = 12f
                                setPadding(6, 4, 6, 4)
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                x = ((pixelStart.x + pixelEnd.x) / 2 - 20).toFloat()
                                y = (pixelStart.y + 20).toFloat()
                            }
                            stripsContainer.addView(diffText)
                        }
                    }
                }
            }
        }
    }

    private fun updateDateRangeLabel() {
        val sdf = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        dateRangeLabel.text = "${sdf.format(viewModel.startDate)} - ${sdf.format(viewModel.endDate)}"
    }

    private fun updateAverageStats() {
        val avg = viewModel.filteredData.averageSteps().toInt().takeIf { it > 0 } ?: 0
        averageText.text = "$avg Steps"
    }

    private fun formatDate(date: Date, format: String): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }
}

class StepsViewModel : ViewModel() {
    val allDailySteps = mutableListOf<DailySteps>()
    val goalSteps = 10000
    var currentRange = RangeTypeChart.WEEK
    var startDate = Date()
    var endDate = Date()

    init {
        generateMockData()
        setInitialRange(RangeTypeChart.WEEK)
    }

    private fun generateMockData() {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val start = sdf.parse("2025/01/01") ?: Date()
        val end = sdf.parse("2025/07/01") ?: Date()

        var current = start
        while (current <= end) {
            val randomSteps = (2000..15000).random()
            allDailySteps.add(DailySteps(current, randomSteps))
            calendar.time = current
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            current = calendar.time
        }
        Log.d("StepsViewModel", "Generated ${allDailySteps.size} days of data from ${formatDate(start, "d MMM yyyy")} to ${formatDate(end, "d MMM yyyy")}")
    }

    val filteredData: List<DailySteps>
        get() = allDailySteps.filter { it.date >= startDate && it.date <= endDate }

    val monthlyGroups: List<MonthGroup>
        get() = if (currentRange != RangeTypeChart.SIX_MONTHS || filteredData.isEmpty()) emptyList() else {
            val calendar = Calendar.getInstance()
            val grouped = filteredData.groupBy {
                calendar.time = it.date
                Pair(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
            }
            // Sort by year and month using a custom comparison
            val sorted = grouped.toList().sortedWith(compareBy({ it.first.first }, { it.first.second }))
            var results = mutableListOf<MonthGroup>()
            var previousAvg: Int? = null
            for ((_, days) in sorted) {
                val sortedDays = days.sortedBy { it.date }
                val totalSteps = sortedDays.sumOf { it.steps }
                val avg = if (sortedDays.isNotEmpty()) totalSteps / sortedDays.size else 0
                val diffString = previousAvg?.let {
                    val delta = avg - it
                    val pct = (delta.toDouble() / it * 100).toInt()
                    if (pct >= 0) "+$pct%" else "$pct%"
                } ?: ""
                val isPositive = diffString.startsWith('+')
                previousAvg = avg

                val monthStart = calendar.apply {
                    time = sortedDays.first().date
                    set(Calendar.DAY_OF_MONTH, 1)
                }.time
                val monthEnd = calendar.apply {
                    time = sortedDays.last().date
                    set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                }.time
                val clampedStart = maxOf(monthStart, startDate)
                val clampedEnd = minOf(monthEnd, endDate)
                results.add(MonthGroup(sortedDays, avg, diffString, isPositive, clampedStart, clampedEnd))
            }
            results
        }

    fun setInitialRange(range: RangeTypeChart) {
        currentRange = range
        val calendar = Calendar.getInstance()
        val now = Date()
        when (range) {
            RangeTypeChart.WEEK -> {
                val interval = calendar.getActualMinimum(Calendar.DAY_OF_WEEK)
                calendar.time = now
                calendar.set(Calendar.DAY_OF_WEEK, interval)
                startDate = calendar.time
                calendar.add(Calendar.DAY_OF_YEAR, 6)
                endDate = calendar.time
            }
            RangeTypeChart.MONTH -> {
                calendar.time = now
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                startDate = calendar.time
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                endDate = calendar.time
            }
            RangeTypeChart.SIX_MONTHS -> {
                calendar.time = now
                calendar.add(Calendar.MONTH, -6)
                startDate = calendar.time
                endDate = now
            }
        }
        val minDate = allDailySteps.minByOrNull { it.date }?.date ?: startDate
        val maxDate = allDailySteps.maxByOrNull { it.date }?.date ?: endDate
        startDate = maxOf(startDate, minDate)
        endDate = minOf(endDate, maxDate)
        Log.d("StepsViewModel", "Set range: $currentRange, startDate: ${formatDate(startDate, "d MMM yyyy")}, endDate: ${formatDate(endDate, "d MMM yyyy")}")
    }

    fun goLeft() {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        when (currentRange) {
            RangeTypeChart.WEEK -> calendar.add(Calendar.DAY_OF_YEAR, -7)
            RangeTypeChart.MONTH -> calendar.add(Calendar.MONTH, -1)
            RangeTypeChart.SIX_MONTHS -> calendar.add(Calendar.MONTH, -6)
        }
        startDate = calendar.time
        calendar.time = endDate
        when (currentRange) {
            RangeTypeChart.WEEK -> calendar.add(Calendar.DAY_OF_YEAR, -7)
            RangeTypeChart.MONTH -> calendar.add(Calendar.MONTH, -1)
            RangeTypeChart.SIX_MONTHS -> calendar.add(Calendar.MONTH, -6)
        }
        endDate = calendar.time
        val minDate = allDailySteps.minByOrNull { it.date }?.date ?: startDate
        val maxDate = allDailySteps.maxByOrNull { it.date }?.date ?: endDate
        startDate = maxOf(startDate, minDate)
        endDate = minOf(endDate, maxDate)
        Log.d("StepsViewModel", "Go left: startDate: ${formatDate(startDate, "d MMM yyyy")}, endDate: ${formatDate(endDate, "d MMM yyyy")}")
    }

    fun goRight() {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        when (currentRange) {
            RangeTypeChart.WEEK -> calendar.add(Calendar.DAY_OF_YEAR, 7)
            RangeTypeChart.MONTH -> calendar.add(Calendar.MONTH, 1)
            RangeTypeChart.SIX_MONTHS -> calendar.add(Calendar.MONTH, 6)
        }
        startDate = calendar.time
        calendar.time = endDate
        when (currentRange) {
            RangeTypeChart.WEEK -> calendar.add(Calendar.DAY_OF_YEAR, 7)
            RangeTypeChart.MONTH -> calendar.add(Calendar.MONTH, 1)
            RangeTypeChart.SIX_MONTHS -> calendar.add(Calendar.MONTH, 6)
        }
        endDate = calendar.time
        val minDate = allDailySteps.minByOrNull { it.date }?.date ?: startDate
        val maxDate = allDailySteps.maxByOrNull { it.date }?.date ?: endDate
        startDate = maxOf(startDate, minDate)
        endDate = minOf(endDate, maxDate)
        Log.d("StepsViewModel", "Go right: startDate: ${formatDate(startDate, "d MMM yyyy")}, endDate: ${formatDate(endDate, "d MMM yyyy")}")
    }

    val yMax: Double
        get() = filteredData.maxOfOrNull { it.steps.toDouble() }?.times(1.15) ?: 15000.0

    private fun formatDate(date: Date, format: String): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }
}

data class DailySteps(val date: Date, val steps: Int)
enum class RangeTypeChart { WEEK, MONTH, SIX_MONTHS }
data class MonthGroup(
    val days: List<DailySteps>,
    val avgSteps: Int,
    val monthDiffString: String,
    val isPositiveChange: Boolean,
    val startDate: Date,
    val endDate: Date
)

private fun List<DailySteps>.averageSteps(): Number {
    return if (isEmpty()) 0.0 else sumOf { it.steps } / size
}
