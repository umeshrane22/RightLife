package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.ChartAnimator
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.databinding.FragmentSleepPerformanceBinding
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
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.android.material.snackbar.Snackbar
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.SleepPerformanceResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepStageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class SleepPerformanceFragment : BaseFragment<FragmentSleepPerformanceBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSleepPerformanceBinding
        get() = FragmentSleepPerformanceBinding::inflate
    var snackbar: Snackbar? = null

    private lateinit var btnPrevious: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var dateRangeText: TextView
    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart
    private lateinit var radioGroup: RadioGroup
    private lateinit var layoutLineChart: FrameLayout
    private lateinit var stripsContainer: FrameLayout
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sleepPerformanceResponse: SleepPerformanceResponse
    private var currentTab = 0 // 0 = Week, 1 = Month, 2 = 6 Months
    private var currentDate: LocalDate = LocalDate.now() // today

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundResource(R.drawable.sleep_stages_bg)

        barChart = view.findViewById(R.id.heartRateChart)
        layoutLineChart = view.findViewById(R.id.lyt_line_chart)
        stripsContainer = view.findViewById(R.id.stripsContainer)
        lineChart = view.findViewById(R.id.heartLineChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        btnPrevious = view.findViewById(R.id.btn_prev)
        btnNext = view.findViewById(R.id.btn_next)
        dateRangeText = view.findViewById(R.id.tv_selected_date)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)

        // Show Week data by default
     //   updateChart(getWeekData(), getWeekLabels())
        setupListeners()
        setupChart()
        loadWeekData()
        fetchSleepData()

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")

            }
        })

        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")
        }

    }

    private fun setupListeners() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek -> {
                    currentTab = 0
                    barChart.visibility = View.VISIBLE
                    layoutLineChart.visibility = View.GONE
                    loadWeekData()
                }
                R.id.rbMonth -> {
                    currentTab = 1
                    barChart.visibility = View.VISIBLE
                    layoutLineChart.visibility = View.GONE
                    loadMonthData()
                }
                R.id.rbSixMonths -> {
                    currentTab = 2
                    barChart.visibility = View.GONE
                    layoutLineChart.visibility = View.VISIBLE
                    lineChartForSixMonths()
                }
            }
        }

        btnPrevious.setOnClickListener {
            when (currentTab) {
                0 -> {
                    currentDate = currentDate.minusWeeks(1)
                    loadWeekData()
                }
                1 -> {
                    currentDate = currentDate.minusMonths(1)
                    loadMonthData()
                }
                2 -> {
                    currentDate = currentDate.minusMonths(6)
                    loadSixMonthsData()
                }
            }
        }

        btnNext.setOnClickListener {
            when (currentTab) {
                0 -> {
                    currentDate = currentDate.plusWeeks(1)
                    loadWeekData()
                }
                1 -> {
                    currentDate = currentDate.plusMonths(1)
                    loadMonthData()
                }
                2 -> {
                    currentDate = currentDate.plusMonths(6)
                    loadSixMonthsData()
                }
            }
        }
    }

    private fun loadWeekData() {
        val startOfWeek = currentDate.with(java.time.DayOfWeek.MONDAY)
        val endOfWeek = startOfWeek.plusDays(6)

        val formatter = DateTimeFormatter.ofPattern("d MMM")
        dateRangeText.text = "${startOfWeek.format(formatter)} - ${endOfWeek.format(formatter)}, ${currentDate.year}"

        val entries = mutableListOf<BarEntry>()
        for (i in 0..6) {
            entries.add(BarEntry(i.toFloat(), (50..100).random().toFloat()))
        }

        updateChart(entries, listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))

    }

    private fun loadMonthData() {
        val startOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth())
        val endOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth())

        val formatter = DateTimeFormatter.ofPattern("d MMM")
        dateRangeText.text = "${startOfMonth.format(formatter)} - ${endOfMonth.format(formatter)}, ${currentDate.year}"

        val entries = mutableListOf<BarEntry>()
        val daysInMonth = endOfMonth.dayOfMonth
        for (i in 0 until daysInMonth) {
            entries.add(BarEntry(i.toFloat(), (50..100).random().toFloat()))
        }

        val weekRanges = listOf("1", "2", "3", "4", "5","6", "7", "8", "9", "10","11", "12", "13", "14", "15","16", "17", "18", "19", "20","21", "22", "23", "24", "25","26", "27", "28", "29", "30")

        updateChart(entries, weekRanges)
    }

    private fun loadSixMonthsData() {
        val startOfPeriod = currentDate.minusMonths(5).with(TemporalAdjusters.firstDayOfMonth())
        val endOfPeriod = currentDate.with(TemporalAdjusters.lastDayOfMonth())

        val formatter = DateTimeFormatter.ofPattern("MMM yyyy")
        dateRangeText.text = "${startOfPeriod.format(formatter)} - ${endOfPeriod.format(formatter)}"

        val entries = mutableListOf<BarEntry>()
        for (i in 0..5) {
            entries.add(BarEntry(i.toFloat(), (50..100).random().toFloat()))
        }

        val months = listOf(
            startOfPeriod.month.name.take(3),
            startOfPeriod.plusMonths(1).month.name.take(3),
            startOfPeriod.plusMonths(2).month.name.take(3),
            startOfPeriod.plusMonths(3).month.name.take(3),
            startOfPeriod.plusMonths(4).month.name.take(3),
            startOfPeriod.plusMonths(5).month.name.take(3)
        )
        updateChart(entries, months)
    }

    private fun updateChart(entries: List<BarEntry>, labels: List<String>) {
        val dataSet = BarDataSet(entries, "Performance")
        dataSet.color = Color.parseColor("#87CEFA")

        barChart.data = BarData(dataSet)
        barChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value.toInt() in labels.indices) labels[value.toInt()] else ""
            }
        }

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.axisMaximum = 100f
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.invalidate()
    }

    private fun fetchSleepData() {
        progressDialog.show()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val userId = "67f6698fa213d14e22a47c2a"
        val period = "weekly"
        val source = "apple"
        val call = ApiClient.apiServiceFastApi.fetchSleepPerformance(userId, source, period)
        call.enqueue(object : Callback<SleepPerformanceResponse> {
            override fun onResponse(call: Call<SleepPerformanceResponse>, response: Response<SleepPerformanceResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    sleepPerformanceResponse = response.body()!!
                    setSleepRightPerformanceData(sleepPerformanceResponse)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
            override fun onFailure(call: Call<SleepPerformanceResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }

    private fun setSleepRightPerformanceData(sleepPerformanceResponse: SleepPerformanceResponse) {

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

    private fun setupChart() {
        barChart.setRenderer(RoundedBarChartRenderer(barChart, barChart.animator, barChart.viewPortHandler))
        barChart.description.isEnabled = false
        barChart.setDrawValueAboveBar(true)
        barChart.setDrawBarShadow(false)
        barChart.setPinchZoom(false)
        barChart.setDrawGridBackground(false)
        barChart.setScaleEnabled(false) // disables pinch and double-tap zoom
        barChart.isDoubleTapToZoomEnabled = false // disables zoom on double tap

        barChart.isDragEnabled = false
        barChart.isHighlightPerTapEnabled = false

        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelCount = 7

        val leftAxis = barChart.axisLeft
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 100f
        leftAxis.setDrawGridLines(true)
        leftAxis.granularity = 20f
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
}


class RoundedBarChartRenderer(
    chart: BarChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val mBarRect = RectF()
    private val radius = 20f  // change this for more or less curve

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)

        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = dataSet.barBorderWidth

        val drawBorder = dataSet.barBorderWidth > 0f

        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        // initialize the buffer
        mBarBuffers[index].setPhases(phaseX, phaseY)
        mBarBuffers[index].setDataSet(index)
        mBarBuffers[index].setInverted(mChart.isInverted(dataSet.axisDependency))
        mBarBuffers[index].setBarWidth(0.55f)

        mBarBuffers[index].feed(dataSet)

        trans.pointValuesToPixel(mBarBuffers[index].buffer)

        val isSingleColor = dataSet.colors.size == 1

        if (isSingleColor) {
            mRenderPaint.color = dataSet.color
        }

        val buffer = mBarBuffers[index].buffer

        for (j in buffer.indices step 4) {
            if (!mViewPortHandler.isInBoundsLeft(buffer[j + 2])) continue

            if (!mViewPortHandler.isInBoundsRight(buffer[j])) break

            if (!isSingleColor) {
                mRenderPaint.color = dataSet.getColor(j / 4)
            }

            mBarRect.set(buffer[j], buffer[j + 1], buffer[j + 2], buffer[j + 3])

            val path = Path()
            path.addRoundRect(
                mBarRect,
                floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f),
                Path.Direction.CW
            )
            c.drawPath(path, mRenderPaint)

            if (drawBorder) {
                c.drawPath(path, mBarBorderPaint)
            }
        }
    }
}
