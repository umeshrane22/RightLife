package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.databinding.FragmentRestorativeSleepBinding
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.android.material.snackbar.Snackbar
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.RestorativeSleepDetail
import com.jetsynthesys.rightlife.ai_package.model.RestorativeSleepResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepStageResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepStages
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class RestorativeSleepFragment: BaseFragment<FragmentRestorativeSleepBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRestorativeSleepBinding
        get() = FragmentRestorativeSleepBinding::inflate
    var snackbar: Snackbar? = null

    private lateinit var barChart: BarChart
    private lateinit var radioGroup: RadioGroup
    private lateinit var progressDialog: ProgressDialog
    private lateinit var btnPrevious: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var dateRangeText: TextView
    private var currentTab = 0 // 0 = Week, 1 = Month, 2 = 6 Months
    private var currentDate: LocalDate = LocalDate.now() // today

    private val stageColorMap = mapOf(
        "Light" to Color.parseColor("#AEE2FF"),
        "Deep" to Color.parseColor("#BDB2FF"),
        "REM" to Color.parseColor("#8ECAE6"),
        "Awake" to Color.parseColor("#6A4C93")
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundResource(R.drawable.sleep_stages_bg)

        barChart = view.findViewById(R.id.restorativeBarChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        btnPrevious = view.findViewById(R.id.btn_prev)
        btnNext = view.findViewById(R.id.btn_next)
        dateRangeText = view.findViewById(R.id.tv_selected_date)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        // Show Week data by default
      //  updateChart(getWeekData(), getWeekLabels())
        fetchSleepData()
        setupListeners()

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)
        val weekData = getDummyWeekSleepData()
        val monthData = getDummyMonthSleepData()
        renderStackedChart(weekData)

        // Handle Radio Button Selection

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(HomeBottomTabFragment(), "HomeBottomTabFragment")

            }
        })

        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(HomeBottomTabFragment(), "HomeBottomTabFragment")
        }

        /*barChart.renderer = RoundedBarChartRenderer(
            barChart,
            barChart.animator,
            barChart.viewPortHandler
        )*/


    }

    private fun setupListeners() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek -> {
                    currentTab = 0
                    loadWeekData()
                    renderStackedChart(getDummyWeekSleepData())
                }
                R.id.rbMonth -> {
                    currentTab = 1
                    loadMonthData()
                    renderMonthChart(barChart,getDummyMonthSleepData())
                }
                R.id.rbSixMonths -> {
                    currentTab = 2
                    loadSixMonthsData()
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

    //    updateChart(entries, listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))

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

     //   updateChart(entries, weekRanges)
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
       // updateChart(entries, months)
    }

    fun renderMonthChart(chart: BarChart, data: List<Pair<String, RestorativeSleepDetail>>) {
        val entries = ArrayList<BarEntry>()
        val xLabels = ArrayList<String>()

        data.forEachIndexed { index, (dateLabel, detail) ->
            val stageDurations = mutableMapOf(
                "Light" to 0f,
                "Deep" to 0f,
                "REM" to 0f,
                "Awake" to 0f
            )

            detail.sleepStages.forEach {
                val start = LocalTime.parse(it.startDatetime)
                val end = LocalTime.parse(it.endDatetime)
                val duration = Duration.between(start, end).toMinutes().toFloat() / 60f
                stageDurations[it.stage] = stageDurations.getOrDefault(it.stage, 0f) + duration
            }

            val stackedValues = arrayOf(
                stageDurations["Light"] ?: 0f,
                stageDurations["Deep"] ?: 0f,
                stageDurations["REM"] ?: 0f,
                stageDurations["Awake"] ?: 0f
            )

            entries.add(BarEntry(index.toFloat(), stackedValues.toFloatArray()))
            xLabels.add(dateLabel) // Use "1", "2", ..., or full dates if needed
        }

        val dataSet = BarDataSet(entries, "Sleep Stages").apply {
            setDrawValues(false)
            colors = listOf(
                Color.parseColor("#B2E0FE"), // Light
                Color.parseColor("#A3B4F8"), // Deep
                Color.parseColor("#9287F9"), // REM
                Color.parseColor("#4D3DF8")  // Awake
            )
            stackLabels = arrayOf("Light", "Deep", "REM", "Awake")
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.4f // ✅ Thin
            chart.setVisibleXRangeMaximum(31f)
            chart.setRenderer(RestorativeBarChartRenderer(barChart, barChart.animator, barChart.viewPortHandler,stageColorMap))
        }

        chart.apply {
            this.data = barData
            description.isEnabled = false
            legend.isEnabled = false
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawGridBackground(false)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(xLabels)
                granularity = 1f
                labelCount = 6
                setDrawGridLines(false)
            }

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 5f
                granularity = 1f
            }

            axisRight.isEnabled = false

            invalidate()
        }
    }

    private fun fetchSleepData() {
        progressDialog.show()
         val userId = "67f6698fa213d14e22a47c2a"
        val date = "2025-03-18"
        val source = "apple"
        val period = "weekly"
        val call = ApiClient.apiServiceFastApi.fetchSleepRestorativeDetail(userId, source,period, date)
        call.enqueue(object : Callback<RestorativeSleepResponse> {
            override fun onResponse(call: Call<RestorativeSleepResponse>, response: Response<RestorativeSleepResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                 //   sleepStageResponse = response.body()!!
              //      setSleepRightStageData(sleepStageResponse)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
            override fun onFailure(call: Call<RestorativeSleepResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }

    private fun renderStackedChart(weekData: List<Pair<String, RestorativeSleepDetail>>) {
        val entries = mutableListOf<BarEntry>()
        val xLabels = mutableListOf<String>()

        weekData.forEachIndexed { index, (label, detail) ->
            val stageDurations = mutableMapOf(
                "Light" to 0f,
                "Deep" to 0f,
                "REM" to 0f,
                "Awake" to 0f
            )

            detail.sleepStages.forEach {
                val start = LocalTime.parse(it.startDatetime)
                val end = LocalTime.parse(it.endDatetime)
                val duration = Duration.between(start, end).toMinutes().toFloat() / 60f
                stageDurations[it.stage] = stageDurations.getOrDefault(it.stage, 0f) + duration
            }

            val stackedValues = arrayOf(
                stageDurations["Light"] ?: 0f,
                stageDurations["Deep"] ?: 0f,
                stageDurations["REM"] ?: 0f,
                stageDurations["Awake"] ?: 0f
            )

            entries.add(BarEntry(index.toFloat(), stackedValues.toFloatArray()))
            xLabels.add(label)
        }

        val dataSet = BarDataSet(entries, "Sleep Stages").apply {
            setColors(
                stageColorMap["Light"]!!,
                stageColorMap["Deep"]!!,
                stageColorMap["REM"]!!,
                stageColorMap["Awake"]!!
            )
            stackLabels = arrayOf("Light", "Deep", "REM", "Awake")
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }
        barChart.setRenderer(RestorativeBarChartRenderer(barChart, barChart.animator, barChart.viewPortHandler,stageColorMap))
        barChart.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xLabels)
                granularity = 1f
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                labelRotationAngle = -25f
            }

            legend.isEnabled = false
            animateY(1000)
            invalidate()
        }

    }

    private fun getDummyWeekSleepData(): List<Pair<String, RestorativeSleepDetail>> {
        return listOf(
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f)
        )
    }
    private fun getDummyMonthSleepData(): List<Pair<String, RestorativeSleepDetail>> {
        return listOf(
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
            "Mon\n3 Feb" to mockDetail(3f, 1f, 0.5f, 0.5f),
            "Tue\n4 Feb" to mockDetail(2.5f, 1f, 0.5f, 0.2f),
            "Wed\n5 Feb" to mockDetail(3f, 1f, 0.7f, 0.3f),
            "Thu\n6 Feb" to mockDetail(3.2f, 1.2f, 0.4f, 0.2f),
            "Fri\n7 Feb" to mockDetail(1.5f, 0.7f, 0.3f, 0.1f),
            "Sat\n8 Feb" to mockDetail(3.5f, 0.8f, 0.4f, 0.3f),
        )
    }

    private fun mockDetail(light: Float, deep: Float, rem: Float, awake: Float): RestorativeSleepDetail {
        fun genStage(stage: String, hours: Float, offset: Float): SleepStages {
            val start = LocalTime.of(22, 0).plusMinutes((offset * 60).toLong())
            val end = start.plusMinutes((hours * 60).toLong())
            return SleepStages(stage, start.toString(), end.toString(),45)
        }

        return RestorativeSleepDetail(
            totalSleepDurationMinutes = ((light + deep + rem + awake) * 60).toDouble(),
            sleepStartTime = "22:00",
            sleepEndTime = "06:00",
            sleepStages = arrayListOf(
                genStage("Light", light, 0f),
                genStage("Deep", deep, light),
                genStage("REM", rem, light + deep),
                genStage("Awake", awake, light + deep + rem)
            ),
            calculatedRestorativeSleep = null
        )
    }


    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
}

class RestorativeBarChartRenderer(
    private val chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val stageColorMap: Map<String, Int>
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val radius = 20f
    private val stageNames = arrayOf("Light", "Deep", "REM", "Awake")

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = chart.getTransformer(dataSet.axisDependency)
        val drawBorder = dataSet.barBorderWidth > 0f

        if (mBarBuffers.size < index + 1) return

        val buffer = mBarBuffers[index]
        buffer.setPhases(mAnimator.phaseX, mAnimator.phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(chart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(chart.barData.barWidth)

        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        if (!dataSet.isStacked) {
            for (j in buffer.buffer.indices step 4) {
                val left = buffer.buffer[j]
                val top = buffer.buffer[j + 1]
                val right = buffer.buffer[j + 2]
                val bottom = buffer.buffer[j + 3]

                val rect = RectF(left, top, right, bottom)

                val path = Path().apply {
                    addRoundRect(
                        rect,
                        floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f),
                        Path.Direction.CW
                    )
                }
                c.drawPath(path, mRenderPaint)
                if (drawBorder) c.drawPath(path, mBarBorderPaint)
            }
        } else {
            val barWidth = chart.barData.barWidth

            for (i in 0 until dataSet.entryCount) {
                val entry = dataSet.getEntryForIndex(i) as? BarEntry ?: continue
                val vals = entry.yVals ?: continue
                val topIndex = vals.indexOfLast { it != 0f }

                var posY = 0f
                var negY = -entry.negativeSum

                for (k in vals.indices) {
                    val value = vals[k]
                    val stageName = stageNames.getOrNull(k) ?: "Light"
                    mRenderPaint.color = stageColorMap[stageName] ?: Color.GRAY

                    val yStart: Float
                    val yEnd: Float

                    if (value >= 0f) {
                        yStart = posY
                        posY += value
                        yEnd = posY
                    } else {
                        yStart = negY
                        negY += value
                        yEnd = negY
                    }

                    val x = entry.x
                    val rect = RectF(
                        x - barWidth / 2f,
                        yEnd,
                        x + barWidth / 2f,
                        yStart
                    )
                    trans.rectToPixelPhase(rect, mAnimator.phaseY)

                    val drawRounded = (k == topIndex)

                    if (drawRounded) {
                        val path = Path().apply {
                            addRoundRect(
                                rect,
                                floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f),
                                Path.Direction.CW
                            )
                        }
                        c.drawPath(path, mRenderPaint)
                        if (drawBorder) c.drawPath(path, mBarBorderPaint)
                    } else {
                        c.drawRect(rect, mRenderPaint)
                        if (drawBorder) c.drawRect(rect, mBarBorderPaint)
                    }
                }
            }
        }
    }
}



