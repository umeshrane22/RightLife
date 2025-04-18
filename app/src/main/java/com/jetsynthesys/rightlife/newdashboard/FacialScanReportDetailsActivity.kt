package com.jetsynthesys.rightlife.newdashboard

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityFacialScanReportDetailsBinding
import com.jetsynthesys.rightlife.newdashboard.model.FacialScanReport
import com.jetsynthesys.rightlife.newdashboard.model.FacialScanReportData
import com.jetsynthesys.rightlife.newdashboard.model.FacialScanReportResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.jetsynthesys.rightlife.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class FacialScanReportDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacialScanReportDetailsBinding
    private lateinit var dateRange: String
    private lateinit var startDateAPI: String
    private lateinit var endDateAPI: String
    private var selectedRange: String? = "week"
    private var monthOffset = 0
    private var sixMonthOffset = 0
    private var weekOffsetDays = 0
    private lateinit var graphData: FacialScanReportData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacialScanReportDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        //updateChart(getWeekData(), getWeekLabels())

        binding.backwardImageHeartRate.setOnClickListener {
            if (selectedRange == "week") {
                weekOffsetDays -= 7
                dateRange = getWeekRange()
                binding.tvDateRange.text = dateRange
                fetchPastFacialScanReport(
                    convertVitalNameToKey(binding.tvWitale.text.toString()),
                    startDateAPI,
                    endDateAPI

                )
            } else {
                Toast.makeText(this, "Only week can be selected", Toast.LENGTH_SHORT).show()
            }
        }
        binding.forwardImageHeartRate.setOnClickListener {
            if (selectedRange == "week") {
                weekOffsetDays += 7
                dateRange = getWeekRange()
                binding.tvDateRange.text = dateRange
                fetchPastFacialScanReport(
                    convertVitalNameToKey(binding.tvWitale.text.toString()),
                    startDateAPI,
                    endDateAPI

                )
            } else {
                Toast.makeText(this, "Only week can be selected", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Radio Button Selection
        binding.tabGroup.setOnCheckedChangeListener { _, checkedId ->
            val date = dateRange.split("-")
            when (checkedId) {
                R.id.rbWeek -> {
                    selectedRange = "week"
                    dateRange = getWeekRange()
                    binding.tvDateRange.text = dateRange
                    fetchPastFacialScanReport(
                        convertVitalNameToKey(binding.tvWitale.text.toString()),
                        startDateAPI,
                        endDateAPI

                    )

                }

                R.id.rbMonth -> {
                    selectedRange = "month"
                    dateRange = getMonthRange()
                    binding.tvDateRange.text = dateRange
                    fetchPastFacialScanReport(
                        convertVitalNameToKey(binding.tvWitale.text.toString()),
                        startDateAPI,
                        endDateAPI
                    )

                }

                R.id.rbSixMonths -> {
                    selectedRange = "6months"
                    dateRange = getSixMonthRange()
                    binding.tvDateRange.text = dateRange
                    fetchPastFacialScanReport(
                        convertVitalNameToKey(binding.tvWitale.text.toString()),
                        startDateAPI,
                        endDateAPI
                    )

                }
            }
        }


        dateRange = getWeekRange()
        binding.tvDateRange.text = dateRange

        val date = dateRange.split("-")


        //fetchPastFacialScanReport("PULSE_RATE", startDateAPI, endDateAPI)

        selectedRange = "week"
        dateRange = getWeekRange()
        binding.tvDateRange.text = dateRange
        fetchPastFacialScanReport(
            convertVitalNameToKey(binding.tvWitale.text.toString()),
            startDateAPI,
            endDateAPI

        )

        binding.rlWitelsSelection.setOnClickListener {
            openPopup()
        }

        binding.heartRateChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    val date = graphData.data?.get(0)?.createdAt?.let { it1 ->
                        formatIsoDateToReadable(
                            it1
                        )
                    }
                    Log.d("AAAA","Date : = "+date)
                    val value = it.y

                    binding.reportDate.text = date.toString()
                    binding.reportNameAverageValue.text = value.toString()

                    showToast("date = $date  and value = $value")

                }
            }

            override fun onNothingSelected() {

            }

        })

    }

    private fun fetchPastFacialScanReport(key: String, startDate: String, endDate: String) {

        //Dummy data
        val entries = mutableListOf<Entry>()

        for (i in 1..7) {
            val entry = (60..120).random() // Random heart rate
            // Add entry to the list
            entries.add(Entry(i.toFloat(), entry.toFloat()))
        }
        //updateChart(entries, getWeekLabels())

        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getPastReport(
            SharedPreferenceManager.getInstance(this).accessToken,
            startDate,
            endDate,
            key
        )

        call.enqueue(object : Callback<FacialScanReportResponse> {
            override fun onResponse(
                call: Call<FacialScanReportResponse>,
                response: Response<FacialScanReportResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    graphData = response.body()?.data!!

                    if (graphData.data.isNullOrEmpty()) {
                        return
                    }
                    //binding.reportNameAverage.text = "Average :"//binding.tvWitale.text.toString()
                    graphData.data?.get(0)?.unit
                    //binding.reportNameAverageValue.text = graphData?.avgValue.toString()+" "+graphData?.data?.get(0)?.unit

                    binding.tvAverage.text = graphData.avgValue.toString()
                    binding.tvMaximum.text = graphData.maxValue.toString()
                    binding.tvMinimum.text = graphData.minValue.toString()


                    // val entries: ArrayList<Entry> = ArrayList()

                    /*for ((index, item) in graphData?.data?.withIndex()!!) {
                        binding.averageNumber.text = item.unit
                        binding.tvDescription.text = item.implication
                        item.value?.let {
                            val a: Float = it.toFloat()
                            entries.add(Entry(index.toFloat(), a))
                        }
                    }*/

                    val entries = mutableListOf<Entry>()
                    //val yAxisEntries = mutableListOf<Entry>()
                    graphData.yAxisValue?.forEach { y ->
                        // Add a dummy X value like -1f to show as reference or skip plotting if not needed
                        entries.add(Entry(-1f, y.toFloat()))
                    }

                    //updateChart(entries, getWeekLabels())
                    selectedRange?.let { plotGraph2(graphData, binding, it) }

                    // set bottom card data
                    binding.indicator.text = graphData.data?.get(0)?.indicator
                    binding.tvIndicatorExplain.text = graphData.data?.get(0)?.implication
                    binding.tvIndicatorValue.text =
                        graphData.data?.get(0)?.value.toString() + " " + graphData?.data?.get(0)?.unit
                    binding.tvIndicatorValueBg.text =
                        graphData.data?.get(0)?.lowerRange.toString() + "-" + graphData?.data?.get(
                            0
                        )?.upperRange.toString() + " " + graphData?.data?.get(0)?.unit
                    //binding.tvIndicatorValueBg.setBackgroundColor(ColorStateList.valueOf(Utils.getColorFromColorCode(graphData?.data?.get(0)?.colour)))
                    binding.tvIndicatorValueBg.setBackgroundColor(
                        Color.parseColor(
                            "#" + graphData?.data?.get(
                                0
                            )?.colour
                        )
                    )
                } else {
                    showToast("Server Error: " + response.code())
                }
            }

            override fun onFailure(call: Call<FacialScanReportResponse>, t: Throwable) {
                showToast("Network Error: " + t.message)
            }

        })
    }

    private fun generateMockFacialScanReportData(): FacialScanReportData {
        val simulatedReports = ArrayList<FacialScanReport>()

        val baseImplication =
            "Supports efficient cardiovascular function, ensuring optimal oxygen and nutrient delivery."
        val dates = listOf(
            "2025-01-03T15:16:15.405Z",
            "2025-01-10T11:00:00.000Z",
            "2025-01-17T09:30:00.000Z",
            "2025-01-24T13:45:00.000Z",
            "2025-01-31T08:20:00.000Z",
            "2025-02-07T17:10:00.000Z",
            "2025-02-14T10:00:00.000Z",
            "2025-02-21T19:30:00.000Z"
        )

        for (i in dates.indices) {
            val report = FacialScanReport().apply {
                createdAt = dates[i]
                if (i % 2 == 0) {
                    value = 2 + (i * 2.12)
                } else {
                    value = 0.55 + (i * 0.12)
                }

                lowerRange = 0.0
                upperRange = 6.0
                unit = "%"
                indicator = if (i % 2 == 0) "Normal" else "Elevated"
                colour = if (i % 2 == 0) "05D75F" else "FF0000"
                implication = baseImplication
                homeScreen = ""
            }
            simulatedReports.add(report)
        }

        return FacialScanReportData().apply {
            data = simulatedReports
            avgValue = simulatedReports.mapNotNull { it.value }.average()
            minValue = simulatedReports.minOf { it.value ?: 0.0 }
            maxValue = simulatedReports.maxOf { it.value ?: 0.0 }
            yAxisValue = simulatedReports.mapNotNull { it.value?.toInt() }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun openPopup() {
        val popupMenu = PopupMenu(this, binding.rlWitelsSelection)
        popupMenu.menuInflater.inflate(R.menu.witals_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            binding.tvWitale.text = menuItem.title
            val date = dateRange.split("-")
            fetchPastFacialScanReport(
                convertVitalNameToKey(menuItem.title.toString()),
                startDateAPI,
                endDateAPI
            )
            //2025-04-09&endDate=2025-04-02&key=PULSE_RATE
            when (menuItem.itemId) {
                R.id.navPulseRate -> {

                }

                R.id.navBreathingRate -> {

                }

                R.id.nav_BloodPressure -> {

                }

                R.id.nav_HeartRateVariability -> {

                }

                R.id.nav_CardiacWorkload -> {

                }

                R.id.nav_StressIndex -> {

                }

                R.id.nav_BodyMassIndex -> {

                }

                R.id.navCardiovascularDiseaseRisks -> {

                }

                R.id.navOverallWellnessScore -> {

                }
            }
            true
        }
        popupMenu.show()
    }

    private fun convertVitalNameToKey(vitalName: String): String {
        return when (vitalName) {
            "Blood Pressure" -> "BP_RPP"
            "Breathing Rate" -> "BR_BPM"
            "Heart Rate Variability" -> "HRV_SDNN"
            "Cardiac Workload" -> "MSI"
            "Cardiovascular Disease Risks" -> "BP_CVD"
            "Pulse Rate" -> "PULSE_RATE"
            "Stress Index" -> "STRESS_INDEX"
            "Body Mass Index" -> "BMI"
            else -> ""
        }
    }

    private fun capToToday(inputDate: Date): Date {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        return if (inputDate.after(today.time)) today.time else inputDate
    }

    private fun getWeekRange(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Apply offset in days
        calendar.add(Calendar.DAY_OF_YEAR, weekOffsetDays)
        calendar.time = capToToday(calendar.time)

        val startDate = dateFormat.format(calendar.time)
        endDateAPI = startDate

        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val endDate = dateFormat.format(calendar.time)
        startDateAPI = endDate

        return "$endDate - $startDate"
    }

    private fun getMonthRange(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Current date with offset applied
        calendar.add(Calendar.MONTH, monthOffset)
        val endDate = dateFormat.format(calendar.time)
        endDateAPI = endDate

        // Move back 1 month from that point
        calendar.add(Calendar.MONTH, -1)
        val startDate = dateFormat.format(calendar.time)
        startDateAPI = startDate

        return "$startDate - $endDate"
    }

    private fun getSixMonthRange(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Current date with 6-month offset applied
        calendar.add(Calendar.MONTH, sixMonthOffset * 6)
        val endDate = dateFormat.format(calendar.time)
        endDateAPI = endDate

        // Go back 6 months from that point
        calendar.add(Calendar.MONTH, -6)
        val startDate = dateFormat.format(calendar.time)
        startDateAPI = startDate

        return "$startDate - $endDate"
    }


    fun plotGraph2(
        graphData: FacialScanReportData,
        binding: ActivityFacialScanReportDetailsBinding,
        duration: String
    ) {
        val lineChart = binding.heartRateChart
        val entries = mutableListOf<Entry>()
        val xAxisLabels = mutableListOf<String>()

        val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdfInput.timeZone = TimeZone.getTimeZone("UTC")

        val dateList = mutableListOf<Date>()
        val parsedData = graphData.data?.mapNotNull {
            val date = it.createdAt?.let { sdfInput.parse(it) }
            if (date != null && it.value != null) {
                dateList.add(date)
                Pair(date, it.value!!.toFloat())
            } else null
        } ?: emptyList()

        when (duration.lowercase(Locale.ROOT)) {

            // --- Week: Show 7 consecutive days ---
            "week" -> {
                val calendar = Calendar.getInstance()
                calendar.time = dateList.minOrNull() ?: Date()
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

                val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
                repeat(7) { i ->
                    val day = calendar.time
                    val value =
                        parsedData.find { sdfInput.format(it.first) == sdfInput.format(day) }?.second
                            ?: 0f
                    entries.add(Entry(i.toFloat(), value))
                    xAxisLabels.add(dayFormat.format(day))
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            // --- Month: Group data in weekly buckets and average values ---
            "month" -> {
                val calendar = Calendar.getInstance()
                calendar.time = dateList.minOrNull() ?: Date()
                calendar.set(Calendar.DAY_OF_MONTH, 1)

                val month = calendar.get(Calendar.MONTH)
                val monthFormat = SimpleDateFormat("d MMM", Locale.getDefault())

                var weekIndex = 0
                while (calendar.get(Calendar.MONTH) == month) {
                    val weekStart = calendar.time
                    val weekEnd = Calendar.getInstance().apply {
                        time = weekStart
                        add(Calendar.DAY_OF_MONTH, 6)
                    }.time

                    val weeklyValues = parsedData.filter {
                        it.first >= weekStart && it.first <= weekEnd
                    }.map { it.second }

                    val weekValue =
                        if (weeklyValues.isNotEmpty()) weeklyValues.average().toFloat() else 0f
                    entries.add(Entry(weekIndex.toFloat(), weekValue))

                    val startDay = calendar.get(Calendar.DAY_OF_MONTH)
                    val endDay =
                        (startDay + 6).coerceAtMost(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                    val monthLabel = monthFormat.format(calendar.time).split(" ")[1]
                    xAxisLabels.add("$startDay-$endDay $monthLabel")

                    calendar.add(Calendar.DAY_OF_MONTH, 7)
                    weekIndex++
                }
            }

            // --- 6 Months: Group by month and average values ---
            "6months" -> {
                val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())

                val monthGrouped = parsedData.groupBy {
                    monthFormat.format(it.first)
                }

                val sortedMonths = monthGrouped.keys.sortedBy {
                    monthFormat.parse(it)
                }

                sortedMonths.forEachIndexed { index, month ->
                    val values = monthGrouped[month]?.map { it.second } ?: emptyList()
                    val avg = if (values.isNotEmpty()) values.average().toFloat() else 0f
                    entries.add(Entry(index.toFloat(), avg))
                    xAxisLabels.add(month)
                }
            }
        }

        // Chart setup
        val dataSet = LineDataSet(entries, "Data Set").apply {
            color = Color.parseColor("#05AB26")
            setCircleColor(Color.parseColor("#05AB26"))
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(false)
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // X-Axis config
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            valueFormatter = IndexAxisValueFormatter(xAxisLabels)
            setDrawGridLines(false)
            textColor = Color.BLACK
            labelRotationAngle = 0f
        }

        // Y-Axis config
        graphData.yAxisValue?.let { yValues ->
            lineChart.axisLeft.apply {
                axisMinimum = yValues.minOrNull()?.toFloat() ?: 0f
                axisMaximum = yValues.maxOrNull()?.toFloat() ?: 100f
                setLabelCount(5, true)
                textColor = Color.BLACK
            }
        }

        lineChart.axisRight.isEnabled = false

        // Final chart styling
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            animateX(1000)
            legend.textColor = Color.BLACK
            invalidate()
        }
    }

    fun formatIsoDateToReadable(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
        val date = inputFormat.parse(input)
        return outputFormat.format(date!!)
    }

}