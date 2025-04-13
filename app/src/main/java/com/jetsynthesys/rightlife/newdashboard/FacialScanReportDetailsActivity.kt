package com.jetsynthesys.rightlife.newdashboard

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
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
            }else{
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
            }else{
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


    }


    private fun updateChart(entries: List<Entry>, labels: List<String>) {
        val dataSet = LineDataSet(entries, "Heart Rate")
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.BLACK
        dataSet.setCircleColor(Color.RED)
        dataSet.circleRadius = 5f
        dataSet.lineWidth = 2f
        dataSet.setDrawValues(false) // Hide values on points

        val lineData = LineData(dataSet)
        binding.heartRateChart.data = lineData

        // Customize X-Axis
        val xAxis = binding.heartRateChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels) // Set custom labels
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.BLACK
        xAxis.yOffset = 15f // Move labels down

        // Customize Y-Axis
        val leftYAxis: YAxis = binding.heartRateChart.axisLeft
        leftYAxis.textSize = 12f
        leftYAxis.textColor = Color.BLACK
        leftYAxis.setDrawGridLines(true)

        // Disable right Y-axis
        binding.heartRateChart.axisRight.isEnabled = false
        binding.heartRateChart.description.isEnabled = false

        // Refresh chart
        binding.heartRateChart.invalidate()
    }


    /** Sample Data for Week */
    private fun getWeekData(): List<Entry> {
        return listOf(
            Entry(1f, 65f),
            Entry(2f, 75f),
            Entry(3f, 70f),
            Entry(4f, 85f),
            Entry(5f, 80f),
            Entry(6f, 60f),
            Entry(7f, 90f)
        )
    }

    /** X-Axis Labels for Week */
    private fun getWeekLabels(): List<String> {
        return listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    /** Sample Data for Month (4 weeks) */
    private fun getMonthData(): List<Entry> {
        return listOf(
            Entry(0f, 40f), // 1-7 Jan
            Entry(1f, 55f), // 8-14 Jan
            Entry(2f, 35f), // 15-21 Jan
            Entry(3f, 50f), // 22-28 Jan
            Entry(4f, 30f)  // 29-31 Jan
        )
    }

    /** X-Axis Labels for Month */
    private fun getMonthLabels(): List<String> {
        return listOf("1-7 Jan", "8-14 Jan", "15-21 Jan", "22-28 Jan", "29-31 Jan")
    }

    /** Sample Data for 6 Months */
    private fun getSixMonthData(): List<Entry> {
        return listOf(
            Entry(0f, 45f), // Jan
            Entry(1f, 55f), // Feb
            Entry(2f, 50f), // Mar
            Entry(3f, 65f), // Apr
            Entry(4f, 70f), // May
            Entry(5f, 60f)  // Jun
        )
    }

    /** X-Axis Labels for 6 Months */
    private fun getSixMonthLabels(): List<String> {
        return listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
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
                    val graphData = response.body()?.data

                    if (graphData?.data.isNullOrEmpty()) {
                        return
                    }
                    binding.reportNameAverage.text = "Average :"//binding.tvWitale.text.toString()
                    graphData?.data?.get(0)?.unit
                    binding.reportNameAverageValue.text = graphData?.avgValue.toString()+" "+graphData?.data?.get(0)?.unit

                    binding.tvAverage.text = graphData?.avgValue.toString()
                    binding.tvMaximum.text = graphData?.maxValue.toString()
                    binding.tvMinimum.text = graphData?.minValue.toString()


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
                    graphData?.yAxisValue?.forEach { y ->
                        // Add a dummy X value like -1f to show as reference or skip plotting if not needed
                        entries.add(Entry(-1f, y.toFloat()))
                    }

                    //updateChart(entries, getWeekLabels())
                    if (graphData != null) {
                        selectedRange?.let { plotGraph2(graphData,binding, it) }
                    };

                // set bottom card data
                    binding.indicator.text = graphData?.data?.get(0)?.indicator
                    binding.tvIndicatorExplain.text = graphData?.data?.get(0)?.implication
                    binding.tvIndicatorValue.text = graphData?.data?.get(0)?.value.toString()+" "+graphData?.data?.get(0)?.unit
                    binding.tvIndicatorValueBg.text = graphData?.data?.get(0)?.lowerRange.toString()+"-"+graphData?.data?.get(0)?.upperRange.toString()+" "+graphData?.data?.get(0)?.unit
                    //binding.tvIndicatorValueBg.setBackgroundColor(ColorStateList.valueOf(Utils.getColorFromColorCode(graphData?.data?.get(0)?.colour)))
                    binding.tvIndicatorValueBg.setBackgroundColor(Color.parseColor("#"+graphData?.data?.get(0)?.colour));
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

        val baseImplication = "Supports efficient cardiovascular function, ensuring optimal oxygen and nutrient delivery."
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
                if (i%2==0) {
                    value = 2 + (i * 2.12)
                }else{
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

  /*  private fun getWeekRange(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val calendar = Calendar.getInstance()
        val startDate = dateFormat.format(calendar.time) // Current date
        startDateAPI = startDate;

        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val endDate = dateFormat.format(calendar.time) // 7 days earlier
            endDateAPI = endDate
        return "$endDate - $startDate"
    }*/

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

    private fun formatDateRange(startDateStr: String?, endDateStr: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-d", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMM - d MMM, yyyy", Locale.getDefault())

        try {
            val startDate: Date = inputFormat.parse(startDateStr)
            val endDate: Date = inputFormat.parse(endDateStr)

            return outputFormat.format(startDate) + " - " + outputFormat.format(endDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }



/*    fun plotGraph(
        graphData: FacialScanReportData,
        binding: ActivityFacialScanReportDetailsBinding,
        rangeType: String // "week", "month", or "6months"
    ) {
        val lineChart = binding.heartRateChart
        val entries = mutableListOf<Entry>()
        val xAxisLabels = mutableListOf<String>()

        val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdfInput.timeZone = TimeZone.getTimeZone("UTC")

        val calendar = Calendar.getInstance()
        val sdfWeekday = SimpleDateFormat("EEE", Locale.getDefault()) // Mon, Tue...
        val sdfMonth = SimpleDateFormat("MMM", Locale.getDefault())   // Jan, Feb...

        // Use LinkedHashMap to preserve order
        val weekMap = linkedMapOf<String, MutableList<Float>>()   // For month
        val monthMap = linkedMapOf<String, MutableList<Float>>()  // For 6 months

        // Sort by date first
       *//* val sortedData = graphData.data?.sortedBy {
            it.createdAt?.let { dateStr -> sdfInput.parse(dateStr) }
        }*//*

        val mockData = generateMockFacialScanReportData()

        val sortedData = mockData.data?.sortedBy {
            it.createdAt?.let { dateStr -> sdfInput.parse(dateStr) }
        }

        sortedData?.forEachIndexed { index, item ->
            val value = item.value
            val createdAt = item.createdAt
            if (value != null && createdAt != null) {
                val date = sdfInput.parse(createdAt)
                if (date != null) {
                    calendar.time = date

                    when (rangeType) {
                        "week" -> {
                            val dayOfWeek = sdfWeekday.format(date)
                            xAxisLabels.add(dayOfWeek)
                            entries.add(Entry(index.toFloat(), value.toFloat()))
                        }

                        "month" -> {
                            val weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH)
                            val label = "Week $weekOfMonth"
                            weekMap.getOrPut(label) { mutableListOf() }.add(value.toFloat())
                        }

                        "6months" -> {
                            val label = sdfMonth.format(date)
                            monthMap.getOrPut(label) { mutableListOf() }.add(value.toFloat())
                        }
                    }
                }
            }
        }

        // Average and add entries for month/week/6months
        when (rangeType) {
            "month" -> {
                weekMap.entries.forEachIndexed { index, entry ->
                    val avg = entry.value.average().toFloat()
                    entries.add(Entry(index.toFloat(), avg))
                    xAxisLabels.add(entry.key)
                }
            }

            "6months" -> {
                monthMap.entries.forEachIndexed { index, entry ->
                    val avg = entry.value.average().toFloat()
                    entries.add(Entry(index.toFloat(), avg))
                    xAxisLabels.add(entry.key)
                }
            }
        }

        // Create dataset
        val dataSet = LineDataSet(entries, "Heart Rate (bpm)").apply {
            color = Color.parseColor("#05AB26")
            setCircleColor(Color.parseColor("#05AB26"))
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
            mode = LineDataSet.Mode.LINEAR
        }

        lineChart.data = LineData(dataSet)

        // Setup X Axis
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            valueFormatter = IndexAxisValueFormatter(xAxisLabels)
            setDrawGridLines(false)
            textColor = Color.BLACK
            labelRotationAngle = 0f
        }

        // Setup Y Axis
        graphData.yAxisValue?.let { yValues ->
            lineChart.axisLeft.apply {
                axisMinimum = yValues.minOrNull()?.toFloat() ?: 0f
                axisMaximum = yValues.maxOrNull()?.toFloat() ?: 100f
                setLabelCount(yValues.size, true)
                textColor = Color.BLACK
            }
        }

        lineChart.axisRight.isEnabled = false

        // Beautify chart
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            animateX(1000)
            legend.textColor = Color.BLACK
            invalidate()
        }
    }*/



    /*    fun plotGraph(
            graphData: FacialScanReportData,
            binding: ActivityFacialScanReportDetailsBinding,
            rangeType: String // "week", "month", or "6months"
        ) {
            val lineChart = binding.heartRateChart
            val entries = mutableListOf<Entry>()
            val xAxisLabels = mutableListOf<String>()

            val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdfInput.timeZone = TimeZone.getTimeZone("UTC")

            val calendar = Calendar.getInstance()

            // Used for month/week formatting
            val sdfWeekday = SimpleDateFormat("EEE", Locale.getDefault()) // Mon, Tue...
            val sdfMonth = SimpleDateFormat("MMM", Locale.getDefault())   // Jan, Feb...
            val sdfDay = SimpleDateFormat("dd", Locale.getDefault())      // 01, 02...

            val weekMap = mutableMapOf<String, MutableList<Float>>() // For month range
            val monthMap = mutableMapOf<String, MutableList<Float>>() // For 6 months

            graphData.data?.forEachIndexed { index, item ->
                val value = item.value
                val createdAt = item.createdAt
                if (value != null && createdAt != null) {
                    val date = sdfInput.parse(createdAt)
                    if (date != null) {
                        calendar.time = date

                        when (rangeType) {
                            "week" -> {
                                val dayOfWeek = sdfWeekday.format(date)
                                xAxisLabels.add(dayOfWeek)
                                entries.add(Entry(index.toFloat(), value.toFloat()))
                            }

                            "month" -> {
                                val weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH)
                                val label = "Week $weekOfMonth"
                                if (!weekMap.containsKey(label)) {
                                    weekMap[label] = mutableListOf()
                                }
                                weekMap[label]?.add(value.toFloat())
                            }

                            "6months" -> {
                                val monthName = sdfMonth.format(date)
                                if (!monthMap.containsKey(monthName)) {
                                    monthMap[monthName] = mutableListOf()
                                }
                                monthMap[monthName]?.add(value.toFloat())
                            }
                        }
                    }
                }
            }

            // Process for 'month' and '6months' range types
            if (rangeType == "month") {
                weekMap.entries.forEachIndexed { index, entry ->
                    val avg = entry.value.average().toFloat()
                    entries.add(Entry(index.toFloat(), avg))
                    xAxisLabels.add(entry.key)
                }
            }

            if (rangeType == "6months") {
                monthMap.entries.forEachIndexed { index, entry ->
                    val avg = entry.value.average().toFloat()
                    entries.add(Entry(index.toFloat(), avg))
                    xAxisLabels.add(entry.key)
                }
            }

            // Create dataset
            val dataSet = LineDataSet(entries, "Heart Rate (bpm)").apply {
                color = Color.parseColor("#05AB26")
                setCircleColor(Color.parseColor("#05AB26"))
                lineWidth = 2f
                circleRadius = 4f
                valueTextSize = 10f
                mode = LineDataSet.Mode.LINEAR
            }

            lineChart.data = LineData(dataSet)

            // Setup X Axis
            lineChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                valueFormatter = IndexAxisValueFormatter(xAxisLabels)
                setDrawGridLines(false)
                textColor = Color.BLACK
                labelRotationAngle = 0f
            }

            // Setup Y Axis using yAxisValue from response
            graphData.yAxisValue?.let { yValues ->
                lineChart.axisLeft.apply {
                    axisMinimum = yValues.minOrNull()?.toFloat() ?: 0f
                    axisMaximum = yValues.maxOrNull()?.toFloat() ?: 100f
                    setLabelCount(yValues.size, true)
                    textColor = Color.BLACK
                }
            }

            lineChart.axisRight.isEnabled = false

            // Beautify chart
            lineChart.apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                animateX(1000)
                legend.textColor = Color.BLACK
                invalidate()
            }
        }*/


        fun plotGraph(
            graphData: FacialScanReportData,
            binding: ActivityFacialScanReportDetailsBinding,
            duration: String
        ) {
            val lineChart = binding.heartRateChart
            val entries = mutableListOf<Entry>()
            val xAxisLabels = mutableListOf<String>()

            val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdfInput.timeZone = TimeZone.getTimeZone("UTC")

            // Grouping data by date for quick lookup
            val valueMap = mutableMapOf<String, Float>()
            val dateList = mutableListOf<Date>()

            graphData.data?.forEach {
                val date = it.createdAt?.let { sdfInput.parse(it) }
                if (date != null && it.value != null) {
                    dateList.add(date)
                    valueMap[sdfInput.format(date)] = it.value!!.toFloat()
                }
            }

            when (duration.lowercase(Locale.ROOT)) {

                // --- Week: Show all 7 days ---
                "week" -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = dateList.minOrNull() ?: Date()
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

                    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
                    repeat(7) { i ->
                        val dateKey = sdfInput.format(calendar.time)
                        val value = valueMap[dateKey] ?: 0f
                        entries.add(Entry(i.toFloat(), value))
                        xAxisLabels.add(dayFormat.format(calendar.time))
                        calendar.add(Calendar.DAY_OF_MONTH, 1)
                    }
                }

                // --- Month: Weekly ranges like 1-7 Apr, etc. ---
                "month" -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = dateList.minOrNull() ?: Date()
                    val month = calendar.get(Calendar.MONTH)

                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    val monthFormat = SimpleDateFormat("d MMM", Locale.getDefault())

                    var weekIndex = 0
                    while (calendar.get(Calendar.MONTH) == month) {
                        val start = calendar.get(Calendar.DAY_OF_MONTH)
                        val startDateKey = sdfInput.format(calendar.time)
                        val value = valueMap[startDateKey] ?: 0f
                        entries.add(Entry(weekIndex.toFloat(), value))

                        val end = (start + 6).coerceAtMost(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                        xAxisLabels.add("$start-$end ${monthFormat.format(calendar.time).split(" ")[1]}")
                        calendar.add(Calendar.DAY_OF_MONTH, 7)
                        weekIndex++
                    }
                }

                // --- 6 Months: Show each unique month name ---
                "6months" -> {
                    val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
                    val months = sortedSetOf<String>()
                    val monthToValue = mutableMapOf<String, Float>()

                    dateList.forEach { date ->
                        val month = monthFormat.format(date)
                        if (!monthToValue.containsKey(month)) {
                            monthToValue[month] = valueMap[sdfInput.format(date)] ?: 0f
                        }
                        months.add(month)
                    }

                    months.forEachIndexed { index, month ->
                        entries.add(Entry(index.toFloat(), monthToValue[month] ?: 0f))
                        xAxisLabels.add(month)
                    }
                }
            }

            // Chart setup
            val dataSet = LineDataSet(entries, "").apply {
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

            // Chart styling
            lineChart.apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                animateX(1000)
                legend.textColor = Color.BLACK
                invalidate()
            }
        }




    /*
        fun plotGraph(
            graphData: FacialScanReportData,
            binding: ActivityFacialScanReportDetailsBinding,
            duration: String // Add this parameter to switch x-axis format
        ) {
            val lineChart = binding.heartRateChart
            val entries = mutableListOf<Entry>()
            val xAxisLabels = mutableListOf<String>()

            val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdfInput.timeZone = TimeZone.getTimeZone("UTC")

            // Select date format based on duration
            val sdfOutput = when (duration.lowercase(Locale.ROOT)) {
                "week" -> SimpleDateFormat("EEE", Locale.getDefault())      // Mon, Tue...
                "month" -> SimpleDateFormat("d MMM", Locale.getDefault())   // 5 Apr
                "6months" -> SimpleDateFormat("MMM", Locale.getDefault())   // Apr
                else -> SimpleDateFormat("d MMM", Locale.getDefault())      // Default fallback
            }

            // Prepare entries and X-axis labels
            graphData.data?.forEachIndexed { index, item ->
                item.value?.let { value ->
                    entries.add(Entry(index.toFloat(), value.toFloat()))

                    val date = item.createdAt?.let { sdfInput.parse(it) }
                    val label = date?.let { sdfOutput.format(it) } ?: "N/A"
                    xAxisLabels.add(label)
                }
            }

            // Create dataset
            val dataSet = LineDataSet(entries, "Heart Rate (bpm)").apply {
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

            // Setup X-Axis
            lineChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                valueFormatter = IndexAxisValueFormatter(xAxisLabels)
                setDrawGridLines(false)
                textColor = Color.BLACK
                labelRotationAngle = if (duration == "month" || duration == "6months") 0f else 0f
            }

            // Setup Y-Axis
            graphData.yAxisValue?.let { yValues ->
                lineChart.axisLeft.apply {
                    axisMinimum = yValues.minOrNull()?.toFloat() ?: 0f
                    axisMaximum = yValues.maxOrNull()?.toFloat() ?: 100f
                    setLabelCount(5, true)
                    textColor = Color.BLACK
                }
            }

            lineChart.axisRight.isEnabled = false

            // Beautify chart
            lineChart.apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                animateX(1000)
                legend.textColor = Color.BLACK
                invalidate()
            }
        }*/


    /// new graph plot
    /*fun plotGraph(graphData: FacialScanReportData, binding: ActivityFacialScanReportDetailsBinding) {
        var lineChart = binding.heartRateChart
        val entries = mutableListOf<Entry>()
        val xAxisLabels = mutableListOf<String>()

        val sdfInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdfInput.timeZone = TimeZone.getTimeZone("UTC")
        val sdfOutput = SimpleDateFormat("EEE", Locale.getDefault()) // Weekday format

        // Prepare entries and X-axis labels
        graphData.data?.forEachIndexed { index, item ->
            item.value?.let { value ->
                entries.add(Entry(index.toFloat(), value.toFloat()))

                val date = sdfInput.parse(item.createdAt)
                val dayOfWeek = date?.let { sdfOutput.format(it) } ?: "Day"
                xAxisLabels.add(dayOfWeek)
            }
        }

        // Create dataset
        val dataSet = LineDataSet(entries, "Heart Rate (bpm)").apply {
            color = Color.parseColor("#05AB26") // Custom color from your response
            setCircleColor(Color.parseColor("#05AB26"))
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
            mode = LineDataSet.Mode.LINEAR
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Setup X-Axis
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            valueFormatter = IndexAxisValueFormatter(xAxisLabels)
            setDrawGridLines(false)
            textColor = Color.BLACK
        }

        // Setup Y-Axis
        graphData.yAxisValue?.let { yValues ->
            lineChart.axisLeft.apply {
                axisMinimum = yValues.minOrNull()?.toFloat() ?: 0f
                axisMaximum = yValues.maxOrNull()?.toFloat() ?: 100f
                setLabelCount(yValues.size, true)
                textColor = Color.BLACK
            }
        }

        lineChart.axisRight.isEnabled = false

        // Beautify chart
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            animateX(1000)
            legend.textColor = Color.BLACK
            invalidate()
        }
    }*/


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
                    val value = parsedData.find { sdfInput.format(it.first) == sdfInput.format(day) }?.second ?: 0f
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

                    val weekValue = if (weeklyValues.isNotEmpty()) weeklyValues.average().toFloat() else 0f
                    entries.add(Entry(weekIndex.toFloat(), weekValue))

                    val startDay = calendar.get(Calendar.DAY_OF_MONTH)
                    val endDay = (startDay + 6).coerceAtMost(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
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

}