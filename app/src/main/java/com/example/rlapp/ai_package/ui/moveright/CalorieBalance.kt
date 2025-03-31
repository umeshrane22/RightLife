package com.example.rlapp.ai_package.ui.moveright

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import com.example.rlapp.R
import com.example.rlapp.ai_package.base.BaseFragment
import com.example.rlapp.ai_package.data.repository.ApiClient
import com.example.rlapp.ai_package.ui.home.HomeBottomTabFragment
import com.example.rlapp.databinding.FragmentCalorieBalanceBinding
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
import kotlinx.coroutines.launch


class CalorieBalance : BaseFragment<FragmentCalorieBalanceBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCalorieBalanceBinding
        get() = FragmentCalorieBalanceBinding::inflate

    private lateinit var barChart: BarChart
    private lateinit var calorieBalanceBackButton: ImageView
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
        calorieBalanceBackButton = view.findViewById(R.id.back_button)
        calorieBalanceBackButton.setOnClickListener {
            navigateToFragment(HomeBottomTabFragment(), "landingFragment")
        }

        //fetchCalorieAnalysis("weekly")

        // Show Week data by default
        updateChart(getWeekData(), getWeekLabels())

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek -> {
                    barChart.visibility = View.VISIBLE
                    layoutLineChart.visibility = View.GONE
                    fetchCalorieAnalysis("weekly")
                }
                R.id.rbMonth -> {
                    barChart.visibility = View.VISIBLE
                    layoutLineChart.visibility = View.GONE
                    fetchCalorieAnalysis("monthly")
                }
                R.id.rbSixMonths -> {
                    barChart.visibility = View.GONE
                    layoutLineChart.visibility = View.VISIBLE
                    lineChartForSixMonths()
                    fetchCalorieAnalysis("six_months")
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

        // Dynamically assign colors based on values
        val colors = entries.map { entry ->
            if (entry.y >= 0) Color.GREEN else Color.RED
        }

        dataSet.colors = colors // Set colors dynamically
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        barData.barWidth = 0.4f // Set bar width

        barChart.data = barData
        barChart.setFitBars(true)

        // Customize X-Axis
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels) // Set custom labels
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.BLACK
        xAxis.yOffset = 15f // Move labels down

        // Customize Y-Axis
        val leftYAxis: YAxis = barChart.axisLeft
        leftYAxis.textSize = 12f
        leftYAxis.textColor = Color.BLACK
        leftYAxis.setDrawGridLines(true)

        // Disable right Y-axis
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.animateY(1000) // Smooth animation
        barChart.invalidate()
    }


    /** Sample Data for Week */
    private fun getWeekData(): List<BarEntry> {
        return listOf(
            BarEntry(0f, 200f),
            BarEntry(1f, 350f),
            BarEntry(2f, -270f),
            BarEntry(3f, 400f),
            BarEntry(4f, -320f),
            BarEntry(5f, 500f),
            BarEntry(6f, -450f)
        )
    }

    /** X-Axis Labels for Week */
    private fun getWeekLabels(): List<String> {
        return listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    /** Sample Data for Month (4 weeks) */
    private fun getMonthData(): List<BarEntry> {
        return listOf(
            BarEntry(0f, -1500f), // 1-7 Jan
            BarEntry(1f, 1700f), // 8-14 Jan
            BarEntry(2f, -1400f), // 15-21 Jan
            BarEntry(3f, 1800f), // 22-28 Jan
            BarEntry(4f, -1200f)  // 29-31 Jan
        )
    }

    /** X-Axis Labels for Month */
    private fun getMonthLabels(): List<String> {
        return listOf("1-7 Jan", "8-14 Jan", "15-21 Jan", "22-28 Jan", "29-31 Jan")
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

    /** Sample Data for 6 Months */
    private fun getSixMonthData(): List<BarEntry> {
        return listOf(
            BarEntry(0f, 9000f), // Jan
            BarEntry(1f, 8500f), // Feb
            BarEntry(2f, 8700f), // Mar
            BarEntry(3f, 9100f), // Apr
            BarEntry(4f, 9400f), // May
            BarEntry(5f, 8800f)  // Jun
        )
    }

    /** X-Axis Labels for 6 Months */
    private fun getSixMonthLabels(): List<String> {
        return listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
    }
    private fun fetchCalorieAnalysis(period: String) {
        val userId = "64763fe2fa0e40d9c0bc8264" // Replace with actual user ID
        val source = "apple" // Replace with actual source

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiServiceFastApi.getFetchCalorieAnalysis(userId, source, period)
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        data.end_date
                        data.basal_energy_burned
                        data.active_energy_burned
                        data.period

                        /*val entries = processCalorieData(data)
                        val labels = getLabelsForPeriod(period)
                        updateChart(entries, labels)*/
                    } ?: showError("No data available")
                } else {
                    showError("Failed to fetch data")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }
    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}
