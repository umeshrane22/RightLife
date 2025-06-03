package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.DataPoints
import com.jetsynthesys.rightlife.ai_package.model.SleepConsistencyDetail
import com.jetsynthesys.rightlife.ai_package.model.SleepConsistencyResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepDetails
import com.jetsynthesys.rightlife.ai_package.model.SleepJson
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentSleepConsistencyBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
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
    private lateinit var averageBedTime: TextView
    private lateinit var averageSleepTime: TextView
    private lateinit var averageWakeupTime: TextView
    private lateinit var consistencyTitle: TextView
    private lateinit var consistencyMessage: TextView
    private lateinit var percentageIcon: ImageView
    private lateinit var percentageText: TextView
    private var currentTab = 0 // 0 = Week, 1 = Month, 2 = 6 Months
    private var currentDateWeek: LocalDate = LocalDate.now() // today
    private var currentDateMonth: LocalDate = LocalDate.now() // today
    private var mStartDate = ""
    private var mEndDate = ""

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundResource(R.drawable.sleep_stages_bg)

        barChart = view.findViewById(R.id.sleepConsistencyChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        btnPrevious = view.findViewById(R.id.btn_prev)
        btnNext = view.findViewById(R.id.btn_next)
        dateRangeText = view.findViewById(R.id.tv_selected_date)
        averageBedTime = view.findViewById(R.id.tv_average_bed_time)
        averageSleepTime = view.findViewById(R.id.tv_average_sleep_time)
        averageWakeupTime = view.findViewById(R.id.tv_average_wakeup_time)
        consistencyTitle = view.findViewById(R.id.consistency_title)
        consistencyMessage = view.findViewById(R.id.consistency_message)
        percentageIcon = view.findViewById(R.id.percentage_icon)
        percentageText = view.findViewById(R.id.percentage_text)
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
                val fragment = HomeBottomTabFragment()
                val args = Bundle().apply {
                    putString("ModuleName", "SleepRight")
                }
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment, "SearchWorkoutFragment")
                    addToBackStack(null)
                    commit()
                }

            }
        })
        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            val fragment = HomeBottomTabFragment()
            val args = Bundle().apply {
                putString("ModuleName", "SleepRight")
            }
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "SearchWorkoutFragment")
                addToBackStack(null)
                commit()
            }        }
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

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
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
                    fetchSleepData(endDate,"weekly")
                }
                R.id.rbMonth -> {
                    currentTab = 1
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val startDate = getOneMonthEarlierDate().format(dateFormatter)
                    val endDate = getTodayDate().format(dateFormatter)
                    val formatter = DateTimeFormatter.ofPattern("d MMM")
                    dateRangeText.text = "${getOneMonthEarlierDate().format(formatter)} - ${getTodayDate().format(formatter)}, ${currentDateMonth.year}"
                    fetchSleepData(endDate,"monthly")
                }
                R.id.rbSixMonths -> {
                    currentTab = 2
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val startDate = getSixMonthsEarlierDate().format(dateFormatter)
                    val endDate = getTodayDate().format(dateFormatter)
                    val formatter = DateTimeFormatter.ofPattern("d MMM")
                    dateRangeText.text = "${getSixMonthsEarlierDate().format(formatter)} - ${getTodayDate().format(formatter)}, ${currentDateMonth.year}"
                    fetchSleepData(endDate,"monthly")
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

        fetchSleepData(endOfWeek.format(formatter1),"weekly")

    }

    private fun loadMonthData() {
        val endOfMonth = currentDateMonth
        val startOfMonth = endOfMonth.minusMonths(1)
        val formatter = DateTimeFormatter.ofPattern("d MMM")
        dateRangeText.text = "${startOfMonth.format(formatter)} - ${endOfMonth.format(formatter)}, ${currentDateMonth.year}"

        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fetchSleepData(endOfMonth.format(formatter1),"monthly")
    }

    private fun loadSixMonthsData() {
        val startDate = getSixMonthsEarlierDate()
        val endDate = getTodayDate()

        val formatter = DateTimeFormatter.ofPattern("MMM yyyy")
        dateRangeText.text = "${startDate.format(formatter)} - ${endDate.format(formatter)}"

        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fetchSleepData(endDate.format(formatter1),"monthly")
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

    private fun fetchSleepData(mEndDate: String,period: String) {
        progressDialog.show()
        val userid = SharedPreferenceManager.getInstance(requireActivity()).userId ?: ""
        val source = "android"
        val date = mEndDate
        val call = ApiClient.apiServiceFastApi.fetchSleepConsistencyDetail(userid, source, period,mEndDate)
        call.enqueue(object : Callback<SleepConsistencyResponse> {
            override fun onResponse(call: Call<SleepConsistencyResponse>, response: Response<SleepConsistencyResponse>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    sleepConsistencyResponse = response.body()!!
                    if (sleepConsistencyResponse.statusCode == 200) {
                        if (sleepConsistencyResponse.data?.sleepDetails?.isNotEmpty() == true) {
                            sleepConsistencyResponse.data?.sleepDetails?.let {
                                setData(it)
                            }
                        }
                        setSleepAverageData(sleepConsistencyResponse.data?.sleepConsistencyDetail)
                        consistencyTitle.setText(sleepConsistencyResponse.data?.sleepInsightDetail?.title)
                        consistencyMessage.setText(sleepConsistencyResponse.data?.sleepInsightDetail?.message)
                        if (sleepConsistencyResponse.data?.progress_detail?.progress_sign == "plus"){
                            percentageIcon.visibility = View.VISIBLE
                            percentageIcon.setImageResource(R.drawable.ic_up)
                            percentageText.visibility = View.VISIBLE
                            percentageText.text = " "+ sleepConsistencyResponse.data?.progress_detail?.progress_percentage + " past week"
                        }else{
                            percentageIcon.visibility = View.VISIBLE
                            percentageText.visibility = View.VISIBLE
                            percentageIcon.setImageResource(R.drawable.ic_down)
                            percentageIcon.setBackgroundColor(resources.getColor(R.color.red))
                            percentageText.text = " "+ sleepConsistencyResponse.data?.progress_detail?.progress_percentage + " past week"
                        }
                    } else {
                        Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }else if(response.code() == 400){
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Record Not Found", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<SleepConsistencyResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }

    private fun setSleepAverageData(detail: SleepConsistencyDetail?) {
        if (detail?.averageSleepDurationHours != 0.0 && detail?.averageSleepDurationHours != null) {
            averageBedTime.setText(convertDecimalHoursToHrMinFormat(detail.averageSleepDurationHours!!))
        }
        if (detail?.averageSleepStartTime?.isNotEmpty() == true && detail.averageWakeTime?.isNotEmpty() == true) {
            averageSleepTime.setText(convertTo12HourFormat(detail.averageSleepStartTime!!))
            averageWakeupTime.setText(convertTo12HourFormat(detail.averageWakeTime!!))
        }
    }
    fun convertTo12HourFormat(datetimeStr: String): String {
        val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("h:mm a")
        val dateTime = LocalDateTime.parse(datetimeStr, inputFormatter)
        return dateTime.format(outputFormatter)
    }

    private fun convertDecimalHoursToHrMinFormat(hoursDecimal: Double): String {
        val totalMinutes = (hoursDecimal * 60).toInt()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return String.format("%02dhr %02dmins", hours, minutes)
    }

    fun setData(parseSleepData: ArrayList<SleepDetails>) = runBlocking {
        val result = async {
            parseSleepData(parseSleepData)
        }.await()
        barChart.setSleepData(result)
    }

    private fun parseSleepData(sleepDetails: List<SleepDetails>): List<SleepEntry> {
        val sleepSegments = mutableListOf<SleepEntry>()
        for (sleepEntry in sleepDetails) {
            val startTime = sleepEntry.sleepStartTime ?: ""
            val endTime = sleepEntry.sleepEndTime ?: ""
            val duration = sleepEntry.sleepDurationHours?.toFloat()!!
            sleepSegments.add(SleepEntry(startTime, endTime, duration))
        }
        return sleepSegments
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
        textSize = 24f
    }

    private val paintLabelText = Paint().apply {
        color = Color.BLACK
        textSize = 18f
    }

    fun setSleepData(data: List<SleepEntry>) {
        sleepData.clear()
        sleepData.addAll(data)
        invalidate()
    }

    val markerView1 = SleepMarkerView1(context, sleepData)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = 50f
        val widthPerDay = (width - 2 * padding) / sleepData.size
        val heightPerHour = (height - 2 * padding) / 7f // Assuming 12-hour range (from 6 PM to 6 AM)

        // Draw grid lines for hours
        val hours = listOf("8","6","4","2","12","10","8")
        for (i in hours.indices) {
            val y = height - padding - (i * heightPerHour)
            canvas.drawText(hours[i], 10f, y, paintText)
            canvas.drawLine(padding, y, width - padding, y, paintGrid)
        }

        // Draw sleep bars
        try {
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
                val dateLabel = entry.getStartLocalDateTime().format(DateTimeFormatter.ofPattern("d"))
                canvas.drawText(dateLabel, x, height - 30f, paintLabelText)
            }
        }catch (e: DateTimeParseException) {
            Log.e("SleepParse", "Failed to parse", e)
        }

    }
}

data class SleepEntry(val startDatetime: String, val endDatetime: String, val value: Float) {
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
