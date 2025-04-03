package com.jetsynthesys.rightlife.newdashboard

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityFacialScanReportDetailsBinding
import com.jetsynthesys.rightlife.newdashboard.model.FacialScanReportResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class FacialScanReportDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFacialScanReportDetailsBinding
    private lateinit var dateRange: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacialScanReportDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        //updateChart(getWeekData(), getWeekLabels())

        // Handle Radio Button Selection
        binding.tabGroup.setOnCheckedChangeListener { _, checkedId ->
            val date = dateRange.split("-")
            when (checkedId) {
                R.id.rbWeek -> fetchPastFacialScanReport(
                    convertVitalNameToKey(binding.tvWitale.text.toString()),
                    date[0].trim(),
                    date[1].trim()
                )

                R.id.rbMonth -> fetchPastFacialScanReport(
                    convertVitalNameToKey(binding.tvWitale.text.toString()),
                    date[0].trim(),
                    date[1].trim()
                )

                R.id.rbSixMonths -> fetchPastFacialScanReport(
                    convertVitalNameToKey(binding.tvWitale.text.toString()),
                    date[0].trim(),
                    date[1].trim()
                )
            }
        }


        dateRange = getWeekRange()
        binding.tvDateRange.text = dateRange

        val date = dateRange.split("-")


        fetchPastFacialScanReport("PULSE_RATE", date[0].trim(), date[1].trim())

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
        updateChart(entries, getWeekLabels())

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
                    binding.averageNumber.text = graphData?.avgValue.toString()
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

                    updateChart(entries, getWeekLabels())
                } else {
                    showToast("Server Error: " + response.code())
                }
            }

            override fun onFailure(call: Call<FacialScanReportResponse>, t: Throwable) {
                showToast("Network Error: " + t.message)
            }

        })
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
                date[0].trim(),
                date[1].trim()
            )

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

    private fun getWeekRange(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val calendar = Calendar.getInstance()
        val startDate = dateFormat.format(calendar.time) // Current date

        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val endDate = dateFormat.format(calendar.time) // 7 days earlier

        return "$endDate - $startDate"
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
}