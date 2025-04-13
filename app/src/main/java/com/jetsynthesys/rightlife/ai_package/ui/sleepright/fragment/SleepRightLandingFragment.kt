package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.RestorativeSleepDetail
import com.jetsynthesys.rightlife.ai_package.model.SleepIdealActualResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepLandingData
import com.jetsynthesys.rightlife.ai_package.model.SleepLandingResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepPerformanceDetail
import com.jetsynthesys.rightlife.ai_package.model.SleepStageData
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentSleepRightLandingBinding
import com.jetsynthesys.rightlife.ui.NewSleepSounds.NewSleepSoundActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.MPPointF
import com.jetsynthesys.rightlife.ai_package.model.SleepConsistencyResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepDurationData
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.jetsynthesys.rightlife.ui.utility.Utils
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class SleepRightLandingFragment : BaseFragment<FragmentSleepRightLandingBinding>(), WakeUpTimeDialogFragment.BottomSheetListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSleepRightLandingBinding
        get() = FragmentSleepRightLandingBinding::inflate

    private lateinit var wakeTime: TextView
    private lateinit var landingPageResponse: SleepLandingResponse
    private lateinit var sleepLandingData: SleepLandingData
    private lateinit var lineChart:LineChart
    private val remData: ArrayList<Float> = arrayListOf()
    private val awakeData: ArrayList<Float> = arrayListOf()
    private val coreData: ArrayList<Float> = arrayListOf()
    private val deepData: ArrayList<Float> = arrayListOf()
    private val formatters = DateTimeFormatter.ISO_DATE_TIME
    private lateinit var idealActualResponse: SleepIdealActualResponse
    private lateinit var sleepStagesView: SleepChartViewLanding
    private lateinit var sleepConsistencyChart: SleepGraphView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sleepConsistencyResponse: SleepConsistencyResponse
    private lateinit var logYourNap : LinearLayout
    private lateinit var actualNoDataCardView : CardView
    private lateinit var stageNoDataCardView : CardView
    private lateinit var performNoDataCardView : CardView
    private lateinit var restroNoDataCardView : CardView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSeatName = arguments?.getString("BottomSeatName").toString()
        //Not
        //LogLastNightSleep

        val sleepChart = view.findViewById<LineChart>(R.id.sleepChart)
        lineChart = view.findViewById(R.id.sleepIdealActualChart)
        val backButton = view.findViewById<ImageView>(R.id.img_back)
        sleepConsistencyChart = view.findViewById<SleepGraphView>(R.id.sleepConsistencyChart)
        logYourNap = view.findViewById(R.id.btn_log_nap)
        val sleepInfo = view.findViewById<ImageView>(R.id.img_sleep_right)
        val editWakeup = view.findViewById<ImageView>(R.id.img_edit_wakeup_time)
        val sleepPerform = view.findViewById<ImageView>(R.id.img_sleep_perform_right)
        val sleepIdeal = view.findViewById<ImageView>(R.id.img_sleep_ideal_actual)
        val restoSleep = view.findViewById<ImageView>(R.id.img_resto_sleep)
        val consistencySleep = view.findViewById<ImageView>(R.id.img_consistency_right)
         actualNoDataCardView = view.findViewById(R.id.ideal_actual_nodata_layout)
         restroNoDataCardView = view.findViewById(R.id.restro_nodata_layout)
         performNoDataCardView = view.findViewById(R.id.perform_nodata_layout)
         stageNoDataCardView = view.findViewById(R.id.lyt_sleep_stage_no_data)
        wakeTime = view.findViewById<TextView>(R.id.tv_wakeup_time)

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)

        if (bottomSeatName.contentEquals("LogLastNightSleep")){
            val bottomSheet = LogYourNapDialogFragment()
            bottomSheet.show(parentFragmentManager, "LogYourNapDialogFragment")
        }

        sleepInfo.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val stepGoalFragment = SleepStagesFragment()
                val args = Bundle()
                stepGoalFragment.arguments = args
                replace(R.id.flFragment, stepGoalFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        logYourNap.setOnClickListener {
            val bottomSheet = LogYourNapDialogFragment()
            bottomSheet.show(parentFragmentManager, "LogYourNapDialogFragment")
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(HomeBottomTabFragment(), "Home")
            }
        })

        backButton.setOnClickListener {
            navigateToFragment(HomeBottomTabFragment(), "Home")
        }

        sleepPerform.setOnClickListener {
            navigateToFragment(SleepPerformanceFragment(), "Performance")
        }

        sleepIdeal.setOnClickListener {
            navigateToFragment(SleepIdealActualFragment(), "IdealActual")
        }

        restoSleep.setOnClickListener {
            navigateToFragment(RestorativeSleepFragment(), "Restorative")
        }

        consistencySleep.setOnClickListener {
            navigateToFragment(SleepConsistencyFragment(), "Consistency")
        }

      //  setupBarChart(sleepBarChart)

        fetchSleepLandingData()
        fetchIdealActualData()
        fetchSleepConsistencyData()

        editWakeup.setOnClickListener {
            openBottomSheet()
        }

        // Sample data for Ideal vs Actual Sleep Time chart
        /*val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val idealEntries = listOf(
            Entry(0f, 8.3f), Entry(1f, 8.5f), Entry(2f, 9f),
            Entry(3f, 8.2f), Entry(4f, 8.7f), Entry(5f, 8.3f),
            Entry(6f, 8.2f)
        )
        val actualEntries = listOf(
            Entry(0f, 6f), Entry(1f, 6.5f), Entry(2f, 7f),
            Entry(3f, 6.2f), Entry(4f, 6.8f), Entry(5f, 6.3f),
            Entry(6f, 6.2f)
        )

        val idealDataSet = LineDataSet(idealEntries, "Ideal").apply {
            color = Color.GREEN
            valueTextSize = 12f
            setCircleColor(Color.GREEN)
            setDrawCircles(true)
            setDrawValues(false)
        }

        val actualDataSet = LineDataSet(actualEntries, "Actual").apply {
            color = Color.BLUE
            valueTextSize = 12f
            setCircleColor(Color.BLUE)
            setDrawCircles(true)
            setDrawValues(false)
        }

        val lineData = LineData(listOf<ILineDataSet>(idealDataSet, actualDataSet))
        sleepChart.data = lineData

        // Chart Customization
        sleepChart.apply {
            description.isEnabled = false
            axisRight.isEnabled = false
            axisLeft.axisMinimum = 0f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.valueFormatter = IndexAxisValueFormatter(weekdays)
            xAxis.granularity = 1f
            xAxis.labelCount = weekdays.size
            invalidate()
        }*/

        // Sleep Stages Bar Chart
        val barChart = view.findViewById<BarChart>(R.id.barChart)
        val barEntries = ArrayList<BarEntry>()
        barEntries.add(BarEntry(1f, 2f))  // Small bar
        barEntries.add(BarEntry(3f, 4f))  // Medium bar
        barEntries.add(BarEntry(5f, 3f))  // Large bar
        barEntries.add(BarEntry(7f, 4f))  // Large bar
        barEntries.add(BarEntry(9f, 2f))  // Small bar

        val blueDataSet = BarDataSet(barEntries, "Sleep Blocks")
        blueDataSet.color = Color.parseColor("#4444DD")
        val wakeEntries = ArrayList<BarEntry>()
        wakeEntries.add(BarEntry(2f, 1f))
        wakeEntries.add(BarEntry(4f, 2f))
        wakeEntries.add(BarEntry(6f, 2f))
        wakeEntries.add(BarEntry(8f, 3f))

        val lightBlueDataSet = BarDataSet(wakeEntries, "Wake Blocks")
        lightBlueDataSet.color = Color.parseColor("#66CCFF")

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(blueDataSet)
        dataSets.add(lightBlueDataSet)

        val data = BarData(dataSets)
        data.barWidth = 0.8f
        barChart.data = data
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setFitBars(true)
        barChart.animateY(1000)

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        val leftAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawAxisLine(false)

        val rightAxis = barChart.axisRight
        rightAxis.isEnabled = false
        barChart.invalidate()

        sleepStagesView = view.findViewById<SleepChartViewLanding>(R.id.sleepStagesView)

        view.findViewById<LinearLayout>(R.id.play_now).setOnClickListener {
            startActivity(Intent(requireContext(), NewSleepSoundActivity::class.java).apply {
                putExtra("PlayList", "PlayList")
            })
        }
    }

    private fun setupBarChart(barChart: BarChart) {
        val sleepEntries = ArrayList<BarEntry>()
        sleepEntries.add(BarEntry(0f, floatArrayOf(2f, 6f)))
        sleepEntries.add(BarEntry(1f, floatArrayOf(12f, 6f)))
        sleepEntries.add(BarEntry(2f, floatArrayOf(10f, 6f)))
        sleepEntries.add(BarEntry(3f, floatArrayOf(11f, 6f)))
        sleepEntries.add(BarEntry(4f, floatArrayOf(3f, 4f)))
        sleepEntries.add(BarEntry(5f, floatArrayOf(1f, 7f)))
        sleepEntries.add(BarEntry(6f, floatArrayOf(3f, 5f)))

        val barDataSet = BarDataSet(sleepEntries, "Sleep Duration")
        barDataSet.colors = listOf(
            Color.parseColor("#B0D8FF"),
            Color.parseColor("#B0D8FF"),
            Color.parseColor("#B0D8FF"),
            Color.parseColor("#B0D8FF"),
            Color.parseColor("#B0D8FF"),
            Color.parseColor("#B0D8FF"),
            Color.parseColor("#007AFF")
        )
        barDataSet.setDrawValues(false)
        barDataSet.stackLabels = arrayOf("Start Time", "Sleep Hours")

        val barData = BarData(barDataSet)
        barData.barWidth = 0.6f
        barChart.data = barData
        barChart.setFitBars(true)
        barChart.invalidate()

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 12f

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.axisMaximum = 14f
        yAxisLeft.setDrawGridLines(true)
        yAxisLeft.textSize = 12f

        val yAxisRight = barChart.axisRight
        yAxisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setTouchEnabled(false)
    }

    private fun openBottomSheet() {
        val bottomSheet = WakeUpTimeDialogFragment()
        bottomSheet.show(parentFragmentManager, "WakeUpTimeDialog")
    }

    override fun onDataReceived(data: String) {
        wakeTime.text = data
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    private fun fetchSleepLandingData() {
        Utils.showLoader(requireActivity())
      //  val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
      //  val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val userId = "67f6698fa213d14e22a47c2a"
        val date = "2025-03-18"
        val source = "apple"
        val preferences = "nature_sounds"
        val call = ApiClient.apiServiceFastApi.fetchSleepLandingPage(userId, source, date, preferences)
        call.enqueue(object : Callback<SleepLandingResponse> {
            override fun onResponse(call: Call<SleepLandingResponse>, response: Response<SleepLandingResponse>) {
                if (response.isSuccessful) {
                    Utils.dismissLoader(requireActivity())
                    landingPageResponse = response.body()!!
                    println(landingPageResponse)
                    setSleepRightLandingData(landingPageResponse)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Utils.dismissLoader(requireActivity())
                }
            }

            override fun onFailure(call: Call<SleepLandingResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                Utils.dismissLoader(requireActivity())
            }
        })
    }

    private fun setSleepRightLandingData(sleepLandingResponse: SleepLandingResponse) {
        sleepLandingData = sleepLandingResponse.sleepLandingData!!
        sleepLandingData.userId = sleepLandingResponse.sleepLandingData?.userId ?: ""
        sleepLandingData.source = sleepLandingResponse.sleepLandingData?.source ?: ""
        sleepLandingData.endtime = sleepLandingResponse.sleepLandingData?.endtime ?: ""
        sleepLandingData.starttime = sleepLandingResponse.sleepLandingData?.starttime ?: ""
        sleepLandingData.recommendedSound = sleepLandingResponse.sleepLandingData?.recommendedSound ?: ""

        // Set Sleep Stages Data
        val sleepStageData: ArrayList<SleepStageData> = arrayListOf()
        if (sleepLandingResponse.sleepLandingData?.sleepStagesData != null) {

            for (i in 0 until sleepLandingResponse.sleepLandingData!!.sleepStagesData.size) {
                sleepLandingResponse.sleepLandingData!!.sleepStagesData.getOrNull(i)?.let {
                    sleepStageData.add(it)
                }
            }
            setSleepRightStageData(sleepStageData)
        }

        // Set Sleep Performance Data
        val sleepPerformanceDetail: ArrayList<SleepPerformanceDetail> = arrayListOf()
        if (sleepLandingResponse.sleepLandingData?.sleepPerformanceDetail != null) {
            for (i in 0 until sleepLandingResponse.sleepLandingData!!.sleepPerformanceDetail.size) {
                sleepLandingResponse.sleepLandingData!!.sleepPerformanceDetail.getOrNull(i)?.let {
                    sleepPerformanceDetail.add(it)
                }
            }
            setSleepPerformanceData(sleepPerformanceDetail)
        }

        // Set Restorative Sleep Data
        val sleepRestorativeDetail: ArrayList<RestorativeSleepDetail> = arrayListOf()
        if (sleepLandingResponse.sleepLandingData?.sleepRestorativeDetail != null) {
            for (i in 0 until sleepLandingResponse.sleepLandingData!!.sleepRestorativeDetail.size) {
                sleepLandingResponse.sleepLandingData!!.sleepRestorativeDetail.getOrNull(i)?.let {
                    sleepRestorativeDetail.add(it)
                }
            }
            setRestorativeSleepData(sleepRestorativeDetail)
        }

        // Set Recommended Sound
        view?.findViewById<TextView>(R.id.tv_sleep_sound)?.text = "Recommended Sound"
        view?.findViewById<TextView>(R.id.tv_sleep_sound_save)?.text = sleepLandingData.recommendedSound
    }

    private fun fetchIdealActualData() {
        Utils.showLoader(requireActivity())
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val userId = "user_test_1"
        val period = "weekly"
        val source = "apple"
        val call = ApiClient.apiServiceFastApi.fetchSleepIdealActual(userId, source, period)
        call.enqueue(object : Callback<SleepIdealActualResponse> {
            override fun onResponse(call: Call<SleepIdealActualResponse>, response: Response<SleepIdealActualResponse>) {
                if (response.isSuccessful) {
                    Utils.dismissLoader(requireActivity())
                    if (response.body() != null) {
                        idealActualResponse = response.body()!!
                        // setSleepRightLandingData(idealActualResponse)
                        if (idealActualResponse.data?.sleepTimeDetail?.size!! > 0) {
                            actualNoDataCardView.visibility = View.GONE
                            lineChart.visibility = View.VISIBLE
                            val sleepDataList: List<SleepGraphData>? =
                                idealActualResponse.data?.sleepTimeDetail?.map { detail ->
                                    val formattedDate =
                                        detail.sleepDuration.first().startDatetime?.let {
                                            formatDate(
                                                it
                                            )
                                        }
                                    return@map formattedDate?.let {
                                        detail.sleepTimeData?.idealSleepData?.toFloat()
                                            ?.let { it1 ->
                                                detail.sleepTimeData!!.actualSleepData?.toFloat()
                                                    ?.let { it2 ->
                                                        SleepGraphData(
                                                            date = it,
                                                            idealSleep = it1,
                                                            actualSleep = it2
                                                        )
                                                    }
                                            }
                                    }!!
                                }

                            if (sleepDataList != null) {
                                setupIdealActualChart(lineChart, sleepDataList)
                            }
                        }else{
                            actualNoDataCardView.visibility = View.VISIBLE
                            lineChart.visibility = View.GONE
                        }
                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Utils.dismissLoader(requireActivity())
                }
            }

            override fun onFailure(call: Call<SleepIdealActualResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                Utils.dismissLoader(requireActivity())
            }
        })
    }

    private fun setupIdealActualChart(chart: LineChart, newData: List<SleepGraphData>) {
        val idealEntries = ArrayList<Entry>()
        val actualEntries = ArrayList<Entry>()

        for ((index, item) in newData.withIndex()) {
            idealEntries.add(Entry(index.toFloat(), item.idealSleep))
            actualEntries.add(Entry(index.toFloat(), item.actualSleep))
        }

        val idealSet = LineDataSet(idealEntries, "Ideal Sleep").apply {
            color = Color.GREEN
            setCircleColor(Color.GREEN)
            valueTextSize = 10f
        }

        val actualSet = LineDataSet(actualEntries, "Actual Sleep").apply {
            color = Color.BLUE
            setCircleColor(Color.BLUE)
            valueTextSize = 10f
        }

        val lineData = LineData(idealSet, actualSet) // ✅ Convert to LineData

        val markerView = SleepMarkerView(chart.context, newData)
        chart.marker = markerView

        chart.apply {
            data = lineData // ✅ Assign LineData to Chart
            description.isEnabled = false
            xAxis.setDrawGridLines(false)
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            invalidate() // Refresh chart
        }
    }

    private fun formatDate(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE dd MMM", Locale.getDefault())
        return outputFormat.format(inputFormat.parse(dateTime)!!)
    }

    private fun setSleepRightStageData(sleepStageResponse: ArrayList<SleepStageData>) {
        remData.clear()
        awakeData.clear()
        coreData.clear()
        deepData.clear()

        var totalRemDuration = 0f
        var totalAwakeDuration = 0f
        var totalCoreDuration = 0f
        var totalDeepDuration = 0f

        for (i in 0 until sleepStageResponse.size) {
            val startDateTime = LocalDateTime.parse(sleepStageResponse[i].startDatetime, formatters)
            val endDateTime = LocalDateTime.parse(sleepStageResponse[i].endDatetime, formatters)
            val duration = Duration.between(startDateTime, endDateTime).toMinutes().toFloat() / 60f // Convert to hours

            when (sleepStageResponse[i].entryValue) {
                "REM" -> {
                    remData.add(duration)
                    totalRemDuration += duration
                }
                "Deep" -> {
                    deepData.add(duration)
                    totalDeepDuration += duration
                }
                "Core" -> {
                    coreData.add(duration)
                    totalCoreDuration += duration
                }
                "Awake" -> {
                    awakeData.add(duration)
                    totalAwakeDuration += duration
                }
            }
        }

        // Update Sleep Stages UI
        val totalSleepDuration = totalRemDuration + totalDeepDuration + totalCoreDuration + totalAwakeDuration
        val totalSleepHours = totalSleepDuration.toInt()
        val totalSleepMinutes = ((totalSleepDuration - totalSleepHours) * 60).toInt()
        view?.findViewById<TextView>(R.id.total_sleep_title)?.text = "Total Sleep:"
        view?.findViewById<TextView>(R.id.total_sleep_title)?.nextFocusRightId?.let {
            view?.findViewById<TextView>(it)?.text = "${totalSleepHours}hr ${totalSleepMinutes}mins"
        }

        // Set Sleep Start and End Times
        val sleepPerformanceDetail = sleepLandingData.sleepPerformanceDetail?.firstOrNull()
        sleepPerformanceDetail?.actualSleepData?.let {
            val startTime = LocalDateTime.parse(it.sleepStartTime, formatters)
            val endTime = LocalDateTime.parse(it.sleepEndTime, formatters)
            view?.findViewById<TextView>(R.id.tv_start_sleep_time)?.text = startTime.format(DateTimeFormatter.ofPattern("h:mm a"))
            view?.findViewById<TextView>(R.id.tv_alarm_time)?.text = endTime.format(DateTimeFormatter.ofPattern("h:mm a"))
        }

        // Update Sleep Stages Durations
        view?.findViewById<LinearLayout>(R.id.lyt_top_rem)?.let { layout ->
            layout.findViewWithTag<TextView>("Rem")?.text = formatDuration(totalRemDuration)
            layout.findViewWithTag<TextView>("Core")?.text = formatDuration(totalCoreDuration)
        }
        view?.findViewById<LinearLayout>(R.id.lyt_bottom_rem)?.let { layout ->
            layout.findViewWithTag<TextView>("Deep")?.text = formatDuration(totalDeepDuration)
            layout.findViewWithTag<TextView>("Awake")?.text = formatDuration(totalAwakeDuration)
        }

        // Set Sleep Stages Graph
        setStageGraph(sleepStageResponse)
    }

    private fun formatDuration(durationHours: Float): String {
        val hours = durationHours.toInt()
        val minutes = ((durationHours - hours) * 60).toInt()
        return if (hours > 0) {
            "$hours hr $minutes mins"
        } else {
            "$minutes mins"
        }
    }

    private fun setStageGraph(sleepStageResponse: ArrayList<SleepStageData>) {
        val sleepData: ArrayList<SleepSegmentModel> = arrayListOf()
        var currentPosition = 0f
        val totalDuration = sleepStageResponse.sumOf {
            Duration.between(
                LocalDateTime.parse(it.startDatetime, formatters),
                LocalDateTime.parse(it.endDatetime, formatters)
            ).toMinutes().toDouble()
        }.toFloat()

        sleepStageResponse.forEach { stage ->
            val startDateTime = LocalDateTime.parse(stage.startDatetime, formatters)
            val endDateTime = LocalDateTime.parse(stage.endDatetime, formatters)
            val duration = Duration.between(startDateTime, endDateTime).toMinutes().toFloat()
            val start = currentPosition / totalDuration
            val end = (currentPosition + duration) / totalDuration
            val color = when (stage.entryValue) {
                "REM" -> Color.parseColor("#66CCFF") // Light blue
                "Deep" -> Color.parseColor("#4444DD") // Dark blue
                "Core" -> Color.parseColor("#B0D8FF") // Medium blue
                "Awake" -> Color.parseColor("#FF6666") // Red
                else -> Color.GRAY
            }
            sleepData.add(SleepSegmentModel(start, end, color, 110f))
            currentPosition += duration
        }

        sleepStagesView.setSleepData(sleepData)
    }

    private fun setSleepPerformanceData(sleepPerformanceDetail: ArrayList<SleepPerformanceDetail>) {
        val performance = sleepPerformanceDetail.firstOrNull()
        performance?.let {
            // Set Last Night Sleep Times
            val startTime = LocalDateTime.parse(it.actualSleepData?.sleepStartTime, formatters)
            val endTime = LocalDateTime.parse(it.actualSleepData?.sleepEndTime, formatters)
            view?.findViewById<TextView>(R.id.tv_night_time)?.text = startTime.format(DateTimeFormatter.ofPattern("h:mm a"))
            view?.findViewById<TextView>(R.id.tv_alarm_time1)?.text = endTime.format(DateTimeFormatter.ofPattern("h:mm a"))

            // Set Sleep Performance Percentage
            view?.findViewById<TextView>(R.id.tv_sleep_percent)?.text = it.sleepPerformanceData?.sleepPerformance?.toInt().toString()

            // Set Sleep Duration and Ideal Duration
            val actualDuration = it.actualSleepData?.actualSleepDurationHours
            val actualHours = actualDuration?.toInt()
            val actualMinutes = ((actualDuration?.minus(actualHours!!))?.times(60))?.toInt()
            view?.findViewById<TextView>(R.id.tv_sleep_duration)?.text = "${actualHours}hr ${actualMinutes}mins"

            it.idealSleepDuration?.let { ideal ->
                val idealHours = ideal.toInt()
                val idealMinutes = ((ideal - idealHours) * 60).toInt()
                view?.findViewById<TextView>(R.id.tv_ideal_duration)?.text = "${idealHours}hr ${idealMinutes}mins"
            }

            // Set Message and Action Step (if needed)
            // You can add TextViews in the layout to display these
        }
    }

    private fun setRestorativeSleepData(sleepRestorativeDetail: ArrayList<RestorativeSleepDetail>) {
        val restorative = sleepRestorativeDetail.firstOrNull()
        restorative?.let {
            // Set Restorative Sleep Times
            val startTime = LocalDateTime.parse(it.sleepStartTime, formatters)
            val endTime = LocalDateTime.parse(it.sleepEndTime, formatters)
          /*  view?.findViewById<>(R.id.barChart)?.nextFocusDownId?.let { id ->
                view?.findViewById<LinearLayout>(id)?.findViewWithTag<TextView>("SleepTime")?.text = startTime.format(DateTimeFormatter.ofPattern("h:mm a"))
                view?.findViewById<LinearLayout>(id)?.findViewById<TextView>(R.id.tv_wakeup_time)?.text = endTime.format(DateTimeFormatter.ofPattern("h:mm a"))
            }*/

            // Update REM and Deep durations (already calculated in setSleepRightStageData)
            /*view?.findViewById<LinearLayout>(R.id.barChart)?.parent?.let { parent ->
                (parent as ViewGroup).findViewWithTag<TextView>("Rem")?.text = formatDuration(remData.sum())
                (parent as ViewGroup).findViewWithTag<TextView>("Deep")?.text = formatDuration(deepData.sum())
            }*/
        }
    }

    private fun fetchSleepConsistencyData() {
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
        sleepConsistencyChart.setSleepData(result)
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
}


class SleepChartViewLanding(context: android.content.Context, attrs: android.util.AttributeSet? = null) :
    View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val sleepData = mutableListOf<SleepSegmentModel>()

    fun setSleepData(data: List<SleepSegmentModel>) {
        sleepData.clear()
        sleepData.addAll(data)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()

        val baselineY = height * 0.8f
        val maxBarHeight = height * 0.5f
        val cornerRadius = height * 0.1f

        for (segment in sleepData) {
            paint.color = segment.color
            val left = segment.start.coerceIn(0f, 1f) * width
            val right = segment.end.coerceIn(0f, 1f) * width
            val top = baselineY - (segment.height / maxBarHeight * maxBarHeight)
            val bottom = baselineY

            canvas.drawRoundRect(RectF(left, top, right, bottom), cornerRadius, cornerRadius, paint)
        }
    }
}

class SleepMarkerView1(context: Context, private val data: List<SleepEntry>) : MarkerView(context, R.layout.marker_view) {

    private val tvContent: TextView = findViewById(R.id.tv_content_type)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e != null) {
            val xIndex = e.x.toInt()

            // Find the corresponding data entry by index
            val selectedData = data.getOrNull(xIndex)

            selectedData?.let {
              //  val ideal = it.idealSleep
              //  val actual = it.actualSleep

             //   tvContent.text = "Ideal: $ideal hrs\nActual: $actual hrs"
            }
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}

class SleepMarkerView(context: Context, private val data: List<SleepGraphData>) : MarkerView(context, R.layout.marker_view) {

    private val tvContent: TextView = findViewById(R.id.tv_content_type)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e != null) {
            val xIndex = e.x.toInt()

            // Find the corresponding data entry by index
            val selectedData = data.getOrNull(xIndex)

            selectedData?.let {
                val ideal = it.idealSleep
                val actual = it.actualSleep

                tvContent.text = "Ideal: $ideal hrs\nActual: $actual hrs"
            }
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}

data class SleepSegmentModel(val start: Float, val end: Float, val color: Int, val height: Float)