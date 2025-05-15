package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.databinding.FragmentRestorativeSleepBinding
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.android.material.snackbar.Snackbar
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.RestorativeSleepAllData
import com.jetsynthesys.rightlife.ai_package.model.RestorativeSleepData
import com.jetsynthesys.rightlife.ai_package.model.RestorativeSleepResponse
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.math.pow
import kotlin.math.round
import kotlin.time.Duration

class RestorativeSleepFragment(): BaseFragment<FragmentRestorativeSleepBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRestorativeSleepBinding
        get() = FragmentRestorativeSleepBinding::inflate
    var snackbar: Snackbar? = null

    private lateinit var barChart: BarChart
    private lateinit var radioGroup: RadioGroup
    private lateinit var progressDialog: ProgressDialog
    private lateinit var btnPrevious: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var dateRangeText: TextView
    private lateinit var tvAveragePercentage: TextView
    private lateinit var tvAverageSleep: TextView
    private lateinit var tvRemSleep: TextView
    private lateinit var tvDeepSleep: TextView
    private lateinit var tvRestoDate: TextView
    private lateinit var restorativeSleepResponse: RestorativeSleepResponse
    private var currentTab = 0 // 0 = Week, 1 = Month, 2 = 6 Months
    private var currentDateWeek: LocalDate = LocalDate.now() // today
    private var currentDateMonth: LocalDate = LocalDate.now() // today
    private var mStartDate = ""
    private var mEndDate = ""
    private lateinit var stageColorMap: Map<String, Int>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.sleep_stages_bg)
        stageColorMap = mapOf(
            "REM Sleep" to ContextCompat.getColor(requireContext(), R.color.light_blue_bar),
            "Deep Sleep" to ContextCompat.getColor(requireContext(), R.color.purple_bar)
        )

        barChart = view.findViewById(R.id.restorativeBarChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        btnPrevious = view.findViewById(R.id.btn_prev)
        btnNext = view.findViewById(R.id.btn_next)
        tvRemSleep = view.findViewById(R.id.tv_rem_time)
        tvDeepSleep = view.findViewById(R.id.tv_deep_time)
        tvRestoDate = view.findViewById(R.id.tv_restorative_date)
        tvAveragePercentage = view.findViewById(R.id.tv_average_sleep_percentage)
        tvAverageSleep = view.findViewById(R.id.tv_average_sleep_duration)
        dateRangeText = view.findViewById(R.id.tv_selected_date)
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
      //  val weekData = getDummyWeekSleepData()
     //   val monthData = getDummyMonthSleepData()
    //    renderStackedChart(weekData)

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

    //    updateChart(entries, listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))

    }

    private fun loadMonthData() {
        val endOfMonth = currentDateMonth
        val startOfMonth = endOfMonth.minusMonths(1)
        val formatter = DateTimeFormatter.ofPattern("d MMM")
        dateRangeText.text = "${startOfMonth.format(formatter)} - ${endOfMonth.format(formatter)}, ${currentDateMonth.year}"

        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fetchSleepData(endOfMonth.format(formatter1), "monthly")

        val weekRanges = listOf("1", "2", "3", "4", "5","6", "7", "8", "9", "10","11", "12", "13", "14", "15","16", "17", "18", "19", "20","21", "22", "23", "24", "25","26", "27", "28", "29", "30")

     //   updateChart(entries, weekRanges)
    }

    private fun loadSixMonthsData() {
        val startDate = getSixMonthsEarlierDate()
        val endDate = getTodayDate()

        val formatter = DateTimeFormatter.ofPattern("MMM yyyy")
        dateRangeText.text = "${startDate.format(formatter)} - ${endDate.format(formatter)}"

        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fetchSleepData(endDate.format(formatter1), "monthly")
       // updateChart(entries, months)
    }

    fun mapToMonthlySleepChartData(startDate: String, endDate: String, restorativeSleepDetails: List<RestorativeSleepData>): List<Pair<String, SleepStageDurations>> {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startCal = Calendar.getInstance()
        val endCal = Calendar.getInstance()
        startCal.time = inputFormat.parse(startDate) ?: return emptyList()
        endCal.time = inputFormat.parse(endDate) ?: return emptyList()

        val sleepDataMap = restorativeSleepDetails.associateBy { it.date }
        val chartData = mutableListOf<Pair<String, SleepStageDurations>>()

        var dayCounter = 1

        while (!startCal.after(endCal)) {
            val dateKey = inputFormat.format(startCal.time)
            val detail = sleepDataMap[dateKey]
            val stages = detail?.sleepStages
            val rem = stages?.remSleep?.toFloat() ?: 0f
            val deep = stages?.deepSleep?.toFloat() ?: 0f

            chartData.add(dayCounter.toString() to SleepStageDurations(rem,deep))

            startCal.add(Calendar.DATE, 1)
            dayCounter++
        }

        return chartData
    }

    private fun fetchSleepData(endDate: String,period: String) {
        progressDialog.show()
        val userid = SharedPreferenceManager.getInstance(requireActivity()).userId ?: "68010b615a508d0cfd6ac9ca"
        val source = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
        val call = ApiClient.apiServiceFastApi.fetchSleepRestorativeDetail(userid, source,period, endDate)
        call.enqueue(object : Callback<RestorativeSleepResponse> {
            override fun onResponse(call: Call<RestorativeSleepResponse>, response: Response<RestorativeSleepResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    if (response?.body()!=null) {
                        restorativeSleepResponse = response.body()!!
                        setRestorativeSleepData(restorativeSleepResponse?.data)
                    }

                }else if(response.code() == 400){
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Record Not Found", Toast.LENGTH_SHORT).show()
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

    private fun setRestorativeSleepData(restorativeSleepResponse: RestorativeSleepAllData?) {
        var totalRemDuration = 0.0
        var totalDeepDuration = 0.0
        val formatters = DateTimeFormatter.ISO_DATE_TIME
        if (restorativeSleepResponse?.restorativeSleepDetails!=null) {
            if (restorativeSleepResponse.restorativeSleepDetails.getOrNull(0)?.sleepStages!=null) {

                var dataSize = restorativeSleepResponse.restorativeSleepDetails.size -1
                for (i in 0 until restorativeSleepResponse.restorativeSleepDetails.size) {
                    var startDateTime = LocalDateTime.now()
                    var endDateTime = LocalDateTime.now()
                    if(restorativeSleepResponse.restorativeSleepDetails[dataSize].sleepStartTime != null) {
                        startDateTime = LocalDateTime.parse(restorativeSleepResponse.restorativeSleepDetails[dataSize].sleepStartTime, formatters)
                    }
                    if (restorativeSleepResponse.restorativeSleepDetails[dataSize].sleepEndTime != null) {
                         endDateTime = LocalDateTime.parse(restorativeSleepResponse.restorativeSleepDetails[dataSize].sleepStartTime, formatters)
                    }
                    val duration =
                        java.time.Duration.between(startDateTime, endDateTime).toMinutes()
                            .toFloat() / 60f // Convert to hours

                    if(restorativeSleepResponse.restorativeSleepDetails[dataSize].sleepStages?.remSleep != null)
                         {
                            totalRemDuration += duration
                        }else if (restorativeSleepResponse.restorativeSleepDetails[dataSize].sleepStages?.deepSleep != null)
                        {
                            totalDeepDuration += duration
                        }
                    }
                }
            }
            tvRemSleep.setText(convertDecimalHoursToHrMinFormat(totalRemDuration))
            tvDeepSleep.setText(convertDecimalHoursToHrMinFormat(totalDeepDuration))
            tvRestoDate.setText(convertDateToNormalDate(restorativeSleepResponse?.restorativeSleepDetails?.getOrNull(restorativeSleepResponse.restorativeSleepDetails.size.minus(1))?.date!!))
            tvAverageSleep.setText(convertDecimalHoursToHrMinFormat(restorativeSleepResponse.averageSleepDuration!!))
            tvAveragePercentage.setText(""+restorativeSleepResponse?.averageRestorativeSleepPercentage?.roundToDecimals(2)+"%")
            if (restorativeSleepResponse.restorativeSleepDetails.size > 8){
                val formattedData = mapToMonthlySleepChartData(restorativeSleepResponse.startDate!!,restorativeSleepResponse.endDate!!,restorativeSleepResponse.restorativeSleepDetails!!)
                renderStackedChart(formattedData)
            }else {
                val formattedData = mapToSleepChartData(restorativeSleepResponse.startDate!!,restorativeSleepResponse.endDate!!,restorativeSleepResponse.restorativeSleepDetails!!)
                renderStackedChart(formattedData)
            }
        }

    fun convertDateToNormalDate(dateStr: String): String{
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("EEEE dd MMM, yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(dateStr, inputFormatter)
        val formatted = date.format(outputFormatter)
        return formatted
    }

    fun Double.roundToDecimals(decimals: Int): Double {
        val factor = 10.0.pow(decimals)
        return round(this * factor) / factor
    }

    private fun convertDecimalHoursToHrMinFormat(hoursDecimal: Double): String {
        val totalMinutes = (hoursDecimal * 60).toInt()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return String.format("%02dhr %02dmins", hours, minutes)
    }

    fun mapToSleepChartData(startDate: String, endDate: String, restorativeSleepDetails: List<RestorativeSleepData>): List<Pair<String, SleepStageDurations>> {

        val outputFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

        return restorativeSleepDetails.mapNotNull { detail ->
            try {
                val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(detail.date)
                val formattedDate = parsedDate?.let { outputFormat.format(it) } ?: return@mapNotNull null

                val stages = detail.sleepStages

                val rem = stages?.remSleep?.toFloat() ?: 0f
                val deep = stages?.deepSleep?.toFloat() ?: 0f

                formattedDate to SleepStageDurations(rem,deep)

            } catch (e: Exception) {
                null // skip this entry on parse failure or nulls
            }
        }
    }

    private fun renderStackedChart(mappedData: List<Pair<String, SleepStageDurations>>) {
        val entries = mutableListOf<BarEntry>()
        val xLabels = mutableListOf<String>()

        mappedData.forEachIndexed { index, (label, durations) ->
            val stackedValues = floatArrayOf(
                durations.rem / 60f,
                durations.deep / 60f
            )
            entries.add(BarEntry(index.toFloat(), stackedValues))
            xLabels.add(label)
        }

        val dataSet = BarDataSet(entries, "Sleep Stages").apply {
            setColors(
                stageColorMap["REM Sleep"]!!,
                stageColorMap["Deep Sleep"]!!
            )
            stackLabels = arrayOf("REM Sleep", "Deep Sleep")
            valueTextColor = Color.TRANSPARENT
            valueTextSize = 10f
        }

        barChart.setRenderer(
            RestorativeBarChartRenderer(barChart, barChart.animator, barChart.viewPortHandler, stageColorMap)
        )

        barChart.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            description.isEnabled = false
            isHighlightPerTapEnabled = false
            isHighlightPerDragEnabled = false
            legend.isEnabled = false
            setScaleEnabled(false)
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xLabels)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
            }
            legend.isEnabled = false
            animateY(1000)
            invalidate()
        }
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
}

class RestorativeBarChartRenderer(private val chart: BarDataProvider, animator: ChartAnimator, viewPortHandler: ViewPortHandler,
    private val stageColorMap: Map<String, Int>
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val radius = 20f
    private val stageNames = arrayOf("REM Sleep", "Deep Sleep")

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

data class SleepStageDurations(
    val rem: Float,
    val deep: Float

)



