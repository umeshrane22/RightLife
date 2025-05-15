package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.graphics.Canvas
import android.graphics.Color
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
import com.jetsynthesys.rightlife.ai_package.model.FormattedData
import com.jetsynthesys.rightlife.ai_package.model.SleepPerformanceAllData
import com.jetsynthesys.rightlife.ai_package.model.SleepPerformanceList
import com.jetsynthesys.rightlife.ai_package.model.SleepPerformanceResponse
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.ai_package.ui.thinkright.fragment.MindfullChartRenderer
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.pow
import kotlin.math.round

class SleepPerformanceFragment : BaseFragment<FragmentSleepPerformanceBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSleepPerformanceBinding
        get() = FragmentSleepPerformanceBinding::inflate
    var snackbar: Snackbar? = null

    private lateinit var btnPrevious: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var dateRangeText: TextView
    private lateinit var tvSleepAverage: TextView
    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart
    private lateinit var radioGroup: RadioGroup
    private lateinit var layoutLineChart: FrameLayout
    private lateinit var stripsContainer: FrameLayout
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sleepPerformanceResponse: SleepPerformanceResponse
    private var currentTab = 0 // 0 = Week, 1 = Month, 2 = 6 Months
    private var currentDateWeek: LocalDate = LocalDate.now() // today
    private var currentDateMonth: LocalDate = LocalDate.now() // today
    private var mStartDate = ""
    private var mEndDate = ""

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
        tvSleepAverage = view.findViewById(R.id.tv_sleep_perform_average)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        radioGroup.check(R.id.rbWeek)
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        mStartDate = getOneWeekEarlierDate().format(dateFormatter)
        mEndDate = getTodayDate().format(dateFormatter)
        val endOfWeek = currentDateWeek
        val startOfWeek = endOfWeek.minusDays(6)

        val formatter = DateTimeFormatter.ofPattern("d MMM")
        dateRangeText.text = "${startOfWeek.format(formatter)} - ${endOfWeek.format(formatter)}, ${currentDateWeek.year}"
        setupListeners()
        fetchSleepData(mEndDate,"weekly")

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(HomeBottomTabFragment(), "HomeBottomTabFragment")

            }
        })

        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(HomeBottomTabFragment(), "HomeBottomTabFragment")
        }

    //    setupChart()

    }

    fun getTodayDate(): LocalDate {
        return LocalDate.now()
    }

    fun getOneWeekEarlierDate(): LocalDate {
        return LocalDate.now().minusWeeks(1)
    }
    fun getOneMonthEarlierDate(): LocalDate {
        return LocalDate.now().minusMonths(1)
    }
    fun getSixMonthsEarlierDate(): LocalDate {
        return LocalDate.now().minusMonths(6)
    }

    private fun setupListeners() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek -> {
                    currentTab = 0
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val startDate = getOneWeekEarlierDate().format(dateFormatter)
                    val endDate = getTodayDate().format(dateFormatter)
                    val formatter = DateTimeFormatter.ofPattern("d MMM")
                    dateRangeText.text = "${getOneWeekEarlierDate().format(formatter)} - ${getTodayDate().format(formatter)}, ${currentDateWeek.year}"
                    fetchSleepData(endDate, "weekly")
                }
                R.id.rbMonth -> {
                    currentTab = 1
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val startDate = getOneMonthEarlierDate().format(dateFormatter)
                    val endDate = getTodayDate().format(dateFormatter)
                    val formatter = DateTimeFormatter.ofPattern("d MMM")
                    dateRangeText.text = "${getOneMonthEarlierDate().format(formatter)} - ${getTodayDate().format(formatter)}, ${currentDateMonth.year}"
                    fetchSleepData(endDate, "monthly")
                }
                R.id.rbSixMonths -> {
                    currentTab = 2
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val startDate = getSixMonthsEarlierDate().format(dateFormatter)
                    val endDate = getTodayDate().format(dateFormatter)
                    val formatter = DateTimeFormatter.ofPattern("d MMM")
                    dateRangeText.text = "${getSixMonthsEarlierDate().format(formatter)} - ${getTodayDate().format(formatter)}, ${currentDateMonth.year}"
                    fetchSleepData(endDate, "monthly")
                }
            }
        }

        btnPrevious.setOnClickListener {
            when (currentTab) {
                0 -> {
                    currentDateWeek = currentDateWeek.minusWeeks(1)
                    loadWeekData()
                }
                1 -> {
                    currentDateMonth = currentDateMonth.minusMonths(1)
                    loadMonthData()
                }
                2 -> {
                    currentDateMonth = currentDateMonth.minusMonths(6)
                    loadSixMonthsData()
                }
            }
        }

        btnNext.setOnClickListener {
            when (currentTab) {
                0 -> {
                    currentDateWeek = currentDateWeek.plusWeeks(1)
                    loadWeekData()
                }
                1 -> {
                    currentDateMonth = currentDateMonth.plusMonths(1)
                    loadMonthData()
                }
                2 -> {
                    currentDateMonth = currentDateMonth.plusMonths(6)
                    loadSixMonthsData()
                }
            }
        }
    }

    private fun loadWeekData() {
        val endOfWeek = currentDateWeek
        val startOfWeek = endOfWeek.minusDays(6)

        val formatter = DateTimeFormatter.ofPattern("d MMM")
        dateRangeText.text = "${startOfWeek.format(formatter)} - ${endOfWeek.format(formatter)}, ${currentDateWeek.year}"

        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fetchSleepData(endOfWeek.format(formatter1), "weekly")

    }

    private fun loadMonthData() {
        val endOfMonth = currentDateMonth
        val startOfMonth = endOfMonth.minusMonths(1)
        val formatter = DateTimeFormatter.ofPattern("d MMM")
        dateRangeText.text = "${startOfMonth.format(formatter)} - ${endOfMonth.format(formatter)}, ${currentDateMonth.year}"

        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fetchSleepData(endOfMonth.format(formatter1), "monthly")

      //  val weekRanges = listOf("1", "2", "3", "4", "5","6", "7", "8", "9", "10","11", "12", "13", "14", "15","16", "17", "18", "19", "20","21", "22", "23", "24", "25","26", "27", "28", "29", "30")

     //   updateChart(entries, weekRanges)
    }

    private fun loadSixMonthsData() {
        val startDate = getSixMonthsEarlierDate()
        val endDate = getTodayDate()

        val formatter = DateTimeFormatter.ofPattern("MMM yyyy")
        dateRangeText.text = "${startDate.format(formatter)} - ${endDate.format(formatter)}"

        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fetchSleepData(endDate.format(formatter1), "monthly")
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

    private fun fetchSleepData(endDate: String, period: String) {
        progressDialog.show()
        val userid = SharedPreferenceManager.getInstance(requireActivity()).userId ?: "68010b615a508d0cfd6ac9ca"
        val source = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
        val call = ApiClient.apiServiceFastApi.fetchSleepPerformance(userid, source, period,endDate)
        call.enqueue(object : Callback<SleepPerformanceResponse> {
            override fun onResponse(call: Call<SleepPerformanceResponse>, response: Response<SleepPerformanceResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    sleepPerformanceResponse = response.body()!!
                    setSleepRightPerformanceData(sleepPerformanceResponse.sleepPerformanceAllData)
                }else if(response.code() == 400){
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Record Not Found", Toast.LENGTH_SHORT).show()
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

    private fun setSleepRightPerformanceData(sleepPerformanceResponse: SleepPerformanceAllData?) {
        tvSleepAverage.setText(""+sleepPerformanceResponse?.sleepPerformanceAverage?.roundToDecimals(2)+"%")
      //  updateChart()
        if (sleepPerformanceResponse?.sleepPerformanceList?.isNotEmpty() == true) {
            if (sleepPerformanceResponse.sleepPerformanceList.size < 9 ) {
                setupWeeklyBarChart(barChart, sleepPerformanceResponse.sleepPerformanceList, sleepPerformanceResponse.endDatetime!!)
            }else{
                setupMonthlyBarChart(barChart, sleepPerformanceResponse.sleepPerformanceList, sleepPerformanceResponse.startDatetime!!,sleepPerformanceResponse.endDatetime!!)
            }
        }
    }
    fun Double.roundToDecimals(decimals: Int): Double {
        val factor = 10.0.pow(decimals)
        return round(this * factor) / factor
    }

    fun setupMonthlyBarChart(chart: BarChart, data: List<SleepPerformanceList>?, startDateStr:String, endDateStr:String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val trimmedEndDate = endDateStr.substring(0, 10)  // "2025-05-01"
        val trimmedStartDate = startDateStr.substring(0, 10)  // "2025-05-01"
        val startDate = LocalDate.parse(trimmedStartDate, formatter)
        val endDate = LocalDate.parse(trimmedEndDate, formatter)

        val daysBetween = ChronoUnit.DAYS.between(startDate, endDate).toInt()
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        for (i in 0..daysBetween) {
            val currentDate = startDate.plusDays(i.toLong())
            val dayOfMonth = currentDate.dayOfMonth
            val lastDay = endDate.lengthOfMonth()

            val labelGroup = when (dayOfMonth) {
                in 1..7 -> "1–7"
                in 8..14 -> "8–14"
                in 15..21 -> "15–21"
                in 22..28 -> "22–28"
                in 29..lastDay -> "29–$lastDay"
                else -> ""
            }

            // Only label the first day in each fixed group
            val shouldLabel = dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 15 || dayOfMonth == 22 || dayOfMonth == 29
            labels.add(if (shouldLabel) labelGroup else "")
        }
        data?.forEachIndexed { index, item ->
            entries.add(BarEntry(index.toFloat(), item.sleepPerformanceData?.sleepPerformance?.toFloat() ?: 0f))
        }

        val dataSet = BarDataSet(entries, "Sleep Performance")
        dataSet.setColors(Color.parseColor("#4593FB"))
        dataSet.valueTextSize = 9f

        val barData = BarData(dataSet)
        barData.barWidth = 0.4f

        chart.data = barData
        val customRenderer = MindfullChartRenderer(chart, chart.animator, chart.viewPortHandler)
        customRenderer.initBuffers()
        chart.renderer = customRenderer

        chart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            labelCount = labels.size
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            labelRotationAngle = -30f
            textSize = 8f
        }

        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisMaximum = 100f
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.isHighlightPerTapEnabled = false
        chart.isHighlightPerDragEnabled = false
        chart.legend.isEnabled = false

        chart.setVisibleXRangeMaximum(labels.size.toFloat()) // Show all bars
        chart.setFitBars(true)
        chart.invalidate()
    }

    fun getWeekDayNames(endOfWeek: LocalDate): List<String> {
        val startOfWeek = endOfWeek.minusDays(6)
        val formatter = DateTimeFormatter.ofPattern("EEE") // "Mon", "Tue", etc.

        return (0..6).map { dayOffset ->
            startOfWeek.plusDays(dayOffset.toLong()).format(formatter)
        }
    }

    fun setupWeeklyBarChart(chart: BarChart, data: List<SleepPerformanceList>?, endDate: String) {
        val entries = ArrayList<BarEntry>()
        val trimmedDate = endDate.substring(0, 10)  // "2025-05-01"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(trimmedDate, formatter)
        val labels = getWeekDayNames(localDate)

        data?.forEachIndexed { index, item ->
            entries.add(BarEntry(index.toFloat(), item.sleepPerformanceData?.sleepPerformance?.toFloat() ?: 0f))
        }

        val dataSet = BarDataSet(entries, "SleepPerformance")
        dataSet.setColors(Color.parseColor("#4593FB"))
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        chart.data = barData
        val customRenderer = RoundedBarChartRenderer(barChart, barChart.animator, barChart.viewPortHandler)
        customRenderer.initBuffers()
        chart.renderer = customRenderer

        chart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            labelRotationAngle = -30f
            textSize = 10f
        }

        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisMaximum = 100f
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.isHighlightPerTapEnabled = false
        chart.isHighlightPerDragEnabled = false
        chart.legend.isEnabled = false
        chart.setScaleEnabled(false)
        chart.invalidate()
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
