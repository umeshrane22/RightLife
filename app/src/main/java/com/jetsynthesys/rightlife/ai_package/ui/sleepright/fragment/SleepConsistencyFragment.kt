package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.DataPoints
import com.jetsynthesys.rightlife.ai_package.model.SleepConsistencyResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepDetails
import com.jetsynthesys.rightlife.ai_package.model.SleepDurationData
import com.jetsynthesys.rightlife.ai_package.model.SleepJson
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentSleepConsistencyBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class SleepConsistencyFragment : BaseFragment<FragmentSleepConsistencyBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSleepConsistencyBinding
        get() = FragmentSleepConsistencyBinding::inflate
    var snackbar: Snackbar? = null

    private lateinit var barChart: SleepGraphView
    private lateinit var radioGroup: RadioGroup
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sleepConsistencyResponse: SleepConsistencyResponse
    private lateinit var btnPrevious: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var dateRangeText: TextView
    private var currentTab = 0 // 0 = Week, 1 = Month, 2 = 6 Months
    private var currentDate: LocalDate = LocalDate.now() // today

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundResource(R.drawable.sleep_stages_bg)

        barChart = view.findViewById(R.id.sleepConsistencyChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        btnPrevious = view.findViewById(R.id.btn_prev)
        btnNext = view.findViewById(R.id.btn_next)
        dateRangeText = view.findViewById(R.id.tv_selected_date)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)
        setupListeners()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(HomeBottomTabFragment(), "HomeBottomTabFragment")

            }
        })
        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(HomeBottomTabFragment(), "HomeBottomTabFragment")
        }

        val jsonData : SleepJson = loadStepData()
        val fullList = jsonData.dataPoints
        val firstSeven = ArrayList(fullList.take(7))
        setJsonData(firstSeven)

        fetchSleepData()
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun setupListeners() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek -> {
                    currentTab = 0
                    val jsonData : SleepJson = loadStepData()
                    val fullList = jsonData.dataPoints
                    val firstSeven = ArrayList(fullList.take(7))
                    setJsonData(firstSeven)
                    loadWeekData()
                }
                R.id.rbMonth -> {
                    currentTab = 1
                    val jsonData : SleepJson = loadStepData()
                    val fullList = jsonData.dataPoints
                    val firstSeven = ArrayList(fullList.take(30))
                    setJsonData(firstSeven)
                    loadMonthData()
                }
                R.id.rbSixMonths -> {
                    currentTab = 2
                    val jsonData : SleepJson = loadStepData()
                    val fullList = jsonData.dataPoints
                    setJsonData(fullList)
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

     //   updateChart(entries, listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))

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

      //  updateChart(entries, weekRanges)
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
      //  updateChart(entries, months)
    }

    private fun loadStepData(): SleepJson {
        //D:\Client Project\RightLifeAiApp28March\RightLife\app\src\main\assets\fit\alldata\derived_com.google.heart_rate.bpm_com.google.a(1).json
        val json = context?.assets?.open("assets/fit/alldata/derived_com.google.sleep.segment_com.google.an.json")?.bufferedReader().use { it?.readText() }
        return Gson().fromJson(json, object : TypeToken<SleepJson>() {}.type)
//        val json = context?.assets.open("derived_com.google.active_minutes_com.google.a.json").readText()
//

//        val jsonString = json/* load JSON file as string */
//        val activeMinutesData = gson.fromJson(jsonString, HeartRateFitData::class.java)
//
//     //   D:\Client Project\RightLifeAiApp28March\RightLife\app\src\main\assets\Fit\All data\derived_com.google.active_minutes_com.google.a(4).json

//
    }

    private fun fetchSleepData() {
        progressDialog.show()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val userId = "67f6698fa213d14e22a47c2a"
        val period = "weekly"
        val source = "apple"
        val call = ApiClient.apiServiceFastApi.fetchSleepConsistencyDetail(userId, source, period)
        call.enqueue(object : Callback<SleepConsistencyResponse> {
            override fun onResponse(call: Call<SleepConsistencyResponse>, response: Response<SleepConsistencyResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    sleepConsistencyResponse = response.body()!!
                    sleepConsistencyResponse.sleepConsistencyEntry?.sleepDetails?.let {
                       // setData(it)
                    }

                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<SleepConsistencyResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun setJsonData(parseSleepData: ArrayList<DataPoints>) = runBlocking {
        val result = async {
            parseSleepJsonData(parseSleepData)
        }.await()
        barChart.setSleepData(result)
    }

    fun setData(parseSleepData: ArrayList<SleepDetails>) = runBlocking {
        val result = async {
            parseSleepData(parseSleepData)
        }.await()
        barChart.setSleepData(result)
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun parseSleepJsonData(sleepDetails: List<DataPoints>): List<SleepEntry> {
        val sleepSegments = mutableListOf<SleepEntry>()
        for (sleepEntry in sleepDetails) {
            val startTime = sleepEntry.startTimeNanos!!
            val endTime = sleepEntry.endTimeNanos!!
            val duration = sleepEntry.fitValue.getOrNull(0)?.value?.intVal?.toFloat()!!
            sleepSegments.add(SleepEntry(convertNanoToFormattedDate(startTime), convertNanoToFormattedDate(endTime), duration))
        }
        return sleepSegments
    }

    fun convertNanoToFormattedDate(nanoTime: Long): String {
        val millis = nanoTime / 1_000_000
        val date = Date(millis)

        // Format the date
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(date)
    }

    private fun parseSleepData(sleepDetails: List<SleepDetails>): List<SleepEntry> {
        val sleepSegments = mutableListOf<SleepEntry>()
        for (sleepEntry in sleepDetails) {
            val startTime = sleepEntry.startDatetime ?: ""
            val endTime = sleepEntry.endDatetime ?: ""
            val duration = sleepEntry.value?.toFloat()!!
            sleepSegments.add(SleepEntry(startTime, endTime, duration))
        }
        return sleepSegments
    }

/*
private fun updateChart(entries: List<BarEntry>, labels: List<String>) {
        val dataSet = BarDataSet(entries, "Calories Burned")
        dataSet.color = resources.getColor(R.color.sleep_duration_blue)
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
*/

    private fun getWeekData(): List<BarEntry> {
        return listOf(
            BarEntry(0f, 200f),
            BarEntry(1f, 350f),
            BarEntry(2f, 270f),
            BarEntry(3f, 400f),
            BarEntry(4f, 320f),
            BarEntry(5f, 500f),
            BarEntry(6f, 450f)
        )
    }

    /** X-Axis Labels for Week */
    private fun getWeekLabels(): List<String> {
        return listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    /** Sample Data for Month (4 weeks) */
    private fun getMonthData(): List<BarEntry> {
        return listOf(
            BarEntry(0f, 1500f), // 1-7 Jan
            BarEntry(1f, 1700f), // 8-14 Jan
            BarEntry(2f, 1400f), // 15-21 Jan
            BarEntry(3f, 1800f), // 22-28 Jan
            BarEntry(4f, 1200f)  // 29-31 Jan
        )
    }

    /** X-Axis Labels for Month */
    private fun getMonthLabels(): List<String> {
        return listOf("1-7 Jan", "8-14 Jan", "15-21 Jan", "22-28 Jan", "29-31 Jan")
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

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

}

class SleepGraphView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val sleepData = mutableListOf<SleepEntry>()

    private val paintGrid = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 2f
    }

    private val paintSleep = Paint().apply {
        color = Color.parseColor("#70A1FF") // Light blue for sleep bars
        style = Paint.Style.FILL
    }

    private val paintSleepHighlight = Paint().apply {
        color = Color.parseColor("#007BFF") // Dark blue for the last sleep bar
        style = Paint.Style.FILL
    }

    private val paintText = Paint().apply {
        color = Color.BLACK
        textSize = 36f
    }

    fun setSleepData(data: List<SleepEntry>) {
        sleepData.clear()
        sleepData.addAll(data)
        invalidate()
    }

    val markerView1 = SleepMarkerView1(context, sleepData)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = 100f
        val widthPerDay = (width - 2 * padding) / sleepData.size
        val heightPerHour = (height - 2 * padding) / 12f // Assuming 12-hour range (from 6 PM to 6 AM)

        // Draw grid lines for hours
        val hours = listOf("8", "9", "10", "11", "12", "1", "2", "3", "4", "5", "6", "7", "8")
        for (i in hours.indices) {
            val y = height - padding - (i * heightPerHour)
            canvas.drawText(hours[i], 10f, y, paintText)
            canvas.drawLine(padding, y, width - padding, y, paintGrid)
        }

        // Draw sleep bars
        sleepData.forEachIndexed { index, entry ->
            val x = padding + index * widthPerDay
            val startHour = entry.getStartLocalDateTime().hour + entry.getStartLocalDateTime().minute / 60f
            val duration = entry.value

            // Convert start hour to Y position (adjusting for 24-hour format)
            val startY = height - padding - ((startHour - 18).coerceIn(0f, 12f) * heightPerHour)
            val endY = (startY - (duration * heightPerHour)).coerceAtLeast(padding)

            val paint = if (index == sleepData.size - 1) paintSleepHighlight else paintSleep
            canvas.drawRoundRect(x, endY, x + widthPerDay * 0.8f, startY, 20f, 20f, paint)

            // Draw date labels
            val dateLabel = entry.getStartLocalDateTime().format(DateTimeFormatter.ofPattern("d MMM"))
            canvas.drawText(dateLabel, x, height - 30f, paintText)
        }
    }
}


data class SleepEntry(
    val startDatetime: String,
    val endDatetime: String,
    val value: Float
) {
    fun getStartLocalDateTime(): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return LocalDateTime.parse(startDatetime, formatter)
    }

    fun getEndLocalDateTime(): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return LocalDateTime.parse(endDatetime, formatter)
    }

    companion object {
        fun fromJson(json: String): List<SleepEntry> {
            val entries = mutableListOf<SleepEntry>()
            val jsonArray = org.json.JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                val startDateTime = obj.getString("start_datetime")
                val endDateTime = obj.getString("end_datetime")
                val value = obj.getDouble("value").toFloat()

                entries.add(SleepEntry(startDateTime, endDateTime, value))
            }
            return entries
        }
    }
}
