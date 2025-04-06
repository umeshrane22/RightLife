package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.snackbar.Snackbar
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.SleepConsistencyResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepDurationData
import com.jetsynthesys.rightlife.databinding.FragmentSleepConsistencyBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SleepConsistencyFragment : BaseFragment<FragmentSleepConsistencyBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSleepConsistencyBinding
        get() = FragmentSleepConsistencyBinding::inflate
    var snackbar: Snackbar? = null

    private lateinit var barChart: SleepGraphView
    private lateinit var radioGroup: RadioGroup
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sleepConsistencyResponse: SleepConsistencyResponse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundResource(R.drawable.sleep_stages_bg)

        barChart = view.findViewById(R.id.sleepConsistencyChart)
        radioGroup = view.findViewById(R.id.tabGroup)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)

        // Set default selection to Week
        radioGroup.check(R.id.rbWeek)

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbWeek ->{
                  //  updateChart(getWeekData(), getWeekLabels())
                }
                R.id.rbMonth ->{
                 //   updateChart(getMonthData(), getMonthLabels())
                }
                R.id.rbSixMonths ->{
                 //   updateChart(getSixMonthData(), getSixMonthLabels())
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")

            }
        })
        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")
        }

        fetchSleepData()
    }

    private fun fetchSleepData() {
        progressDialog.show()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val userId = "user_test_1"
        val period = "weekly"
        val source = "apple"
        val call = ApiClient.apiServiceFastApi.fetchSleepConsistencyDetail(userId, source, period)
        call.enqueue(object : Callback<SleepConsistencyResponse> {
            override fun onResponse(call: Call<SleepConsistencyResponse>, response: Response<SleepConsistencyResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    sleepConsistencyResponse = response.body()!!
                    sleepConsistencyResponse.sleepConsistencyData?.sleepDetails?.let {
                        setData(it)
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

    fun setData(parseSleepData: ArrayList<SleepDurationData>) = runBlocking {
        val result = async {
            parseSleepData(parseSleepData)
        }.await()
        barChart.setSleepData(result)
    }

    private fun parseSleepData(sleepDetails: List<SleepDurationData>): List<SleepEntry> {
        val sleepSegments = mutableListOf<SleepEntry>()
        for (sleepEntry in sleepDetails) {
            val startTime = sleepEntry.startDatetime ?: ""
            val endTime = sleepEntry.endDatetime ?: ""
            val duration = sleepEntry.value?.toFloat()!!
            sleepSegments.add(SleepEntry(startTime, endTime, duration))
        }
        return sleepSegments
    }

/*private fun updateChart(entries: List<BarEntry>, labels: List<String>) {
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
    }*/

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
