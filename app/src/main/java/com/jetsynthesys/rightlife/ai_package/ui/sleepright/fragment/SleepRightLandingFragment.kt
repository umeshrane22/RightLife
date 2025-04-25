package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import androidx.annotation.RequiresPermission
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import com.jetsynthesys.rightlife.ai_package.model.SleepDetails
import com.jetsynthesys.rightlife.ai_package.model.SleepDurationData
import com.jetsynthesys.rightlife.ai_package.model.SleepPerformanceResponse
import com.jetsynthesys.rightlife.ai_package.model.WakeupData
import com.jetsynthesys.rightlife.ai_package.model.WakeupTimeResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.jetsynthesys.rightlife.ui.utility.Utils
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class SleepRightLandingFragment : BaseFragment<FragmentSleepRightLandingBinding>(), WakeUpTimeDialogFragment.BottomSheetListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSleepRightLandingBinding
        get() = FragmentSleepRightLandingBinding::inflate

    private lateinit var restoChart : BarChart
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
    private lateinit var wakeupTimeResponse: WakeupTimeResponse
    private lateinit var sleepStagesView: SleepChartViewLanding
    private lateinit var sleepConsistencyChart: SleepGraphView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sleepConsistencyResponse: SleepConsistencyResponse
    private lateinit var logYourNap : LinearLayout
    private lateinit var actualNoDataCardView : CardView
    private lateinit var stageNoDataCardView : CardView
    private lateinit var performCardView : CardView
    private lateinit var performNoDataCardView : CardView
    private lateinit var restroNoDataCardView : CardView
    private lateinit var consistencyNoDataCardView : CardView
    private lateinit var mainView : LinearLayout
    private lateinit var downloadView: ImageView
    private lateinit var sleepArrowView: ImageView
    private lateinit var sleepPerformView: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSeatName = arguments?.getString("BottomSeatName").toString()
        //Not
        //LogLastNightSleep

        val sleepChart = view.findViewById<LineChart>(R.id.sleepChart)
        sleepArrowView = view.findViewById(R.id.img_sleep_arrow)
        sleepPerformView = view.findViewById(R.id.img_sleep_perform_arrow)
        lineChart = view.findViewById(R.id.sleepIdealActualChart)
        val backButton = view.findViewById<ImageView>(R.id.img_back)
        sleepConsistencyChart = view.findViewById<SleepGraphView>(R.id.sleepConsistencyChart)
        downloadView = view.findViewById(R.id.sleep_download_icon)
        mainView = view.findViewById(R.id.lyt_main_view)
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
        performCardView = view.findViewById(R.id.sleep_perform_layout)
         stageNoDataCardView = view.findViewById(R.id.lyt_sleep_stage_no_data)
        consistencyNoDataCardView = view.findViewById(R.id.consistency_nodata_layout)
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

        downloadView.setOnClickListener {
            saveViewAsPdf(requireContext(),mainView,"SleepRight")
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

        sleepArrowView.setOnClickListener {
            navigateToFragment(SleepStagesFragment(), "SleepStagesFragment")
        }

        sleepPerformView.setOnClickListener {
            navigateToFragment(SleepPerformanceFragment(), "SleepPerformanceFragment")
        }

        sleepPerform.setOnClickListener {
            navigateToFragment(SleepPerformanceFragment(), "SleepPerformanceFragment")
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
        fetchWakeupData()

        editWakeup.setOnClickListener {
            openBottomSheet()
        }

        // Sleep Stages Bar Chart
        restoChart = view.findViewById<BarChart>(R.id.barChart)
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
        restoChart.data = data
        restoChart.description.isEnabled = false
        restoChart.legend.isEnabled = false
        restoChart.setFitBars(true)
        restoChart.animateY(1000)

        val xAxis = restoChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        val leftAxis = restoChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawAxisLine(false)

        val rightAxis = restoChart.axisRight
        rightAxis.isEnabled = false
        restoChart.invalidate()

        sleepStagesView = view.findViewById<SleepChartViewLanding>(R.id.sleepStagesView)

        view.findViewById<LinearLayout>(R.id.play_now).setOnClickListener {
            startActivity(Intent(requireContext(), NewSleepSoundActivity::class.java).apply {
                putExtra("PlayList", "PlayList")
            })
        }
    }

    private fun fetchWakeupData() {
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val date = getCurrentDate()
        val source = "apple"
        val call = ApiClient.apiServiceFastApi.fetchWakeupTime(userId, source, date)
        call.enqueue(object : Callback<WakeupTimeResponse> {
            override fun onResponse(call: Call<WakeupTimeResponse>, response: Response<WakeupTimeResponse>) {
                if (response.isSuccessful) {
                    wakeupTimeResponse = response.body()!!
                    setWakeupData(wakeupTimeResponse.data.getOrNull(0))
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                   // Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<WakeupTimeResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
            }
        })
    }

    fun setWakeupData(wakeupData: WakeupData?){

    }

    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.now().format(formatter)
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
        val token = SharedPreferenceManager.getInstance(requireActivity()).accessToken
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
      //  val userId = "user_test_1"
        val date = getCurrentDate()
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
                  //  Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Utils.dismissLoader(requireActivity())
                    stageNoDataCardView.visibility = View.VISIBLE
                    sleepStagesView.visibility = View.GONE
                    performNoDataCardView.visibility = View.VISIBLE
                    performCardView.visibility = View.GONE
                    restroNoDataCardView.visibility = View.VISIBLE
                    restoChart.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<SleepLandingResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
         //       Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                Utils.dismissLoader(requireActivity())
                stageNoDataCardView.visibility = View.VISIBLE
                sleepStagesView.visibility = View.GONE
                performNoDataCardView.visibility = View.VISIBLE
                performCardView.visibility = View.GONE
                restroNoDataCardView.visibility = View.VISIBLE
                restoChart.visibility = View.GONE
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
            if (sleepLandingResponse.sleepLandingData?.sleepPerformanceDetail!!.size > 0) {
                performNoDataCardView.visibility = View.GONE
                performCardView.visibility = View.VISIBLE
                for (i in 0 until sleepLandingResponse.sleepLandingData!!.sleepPerformanceDetail.size) {
                    sleepLandingResponse.sleepLandingData!!.sleepPerformanceDetail.getOrNull(i)
                        ?.let {
                            sleepPerformanceDetail.add(it)
                        }
                }
                setSleepPerformanceData(sleepPerformanceDetail)
            }else{
                performNoDataCardView.visibility = View.VISIBLE
                performCardView.visibility = View.GONE
            }
        }

        // Set Restorative Sleep Data
        val sleepRestorativeDetail: ArrayList<RestorativeSleepDetail> = arrayListOf()
        if (sleepLandingResponse.sleepLandingData?.sleepRestorativeDetail != null) {
            if (sleepLandingResponse.sleepLandingData?.sleepRestorativeDetail!!.size > 0) {
                restroNoDataCardView.visibility = View.GONE
                restoChart.visibility = View.VISIBLE
                for (i in 0 until sleepLandingResponse.sleepLandingData!!.sleepRestorativeDetail.size) {
                    sleepLandingResponse.sleepLandingData!!.sleepRestorativeDetail.getOrNull(i)
                        ?.let {
                            restroNoDataCardView.visibility = View.VISIBLE
                            restoChart.visibility = View.GONE
                            sleepRestorativeDetail.add(it)
                        }
                }
                setRestorativeSleepData(sleepRestorativeDetail)
            } else{
                restroNoDataCardView.visibility = View.VISIBLE
                restoChart.visibility = View.GONE
            }
        }

        // Set Recommended Sound
        view?.findViewById<TextView>(R.id.tv_sleep_sound)?.text = "Recommended Sound"
        view?.findViewById<TextView>(R.id.tv_sleep_sound_save)?.text = sleepLandingData.recommendedSound
    }

    private fun fetchIdealActualData() {
        Utils.showLoader(requireActivity())
        val token = SharedPreferenceManager.getInstance(requireActivity()).accessToken
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        //val userId = "user_test_1"
        val period = "daily"
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
                            val sleepDataList: List<SleepGraphData>? = idealActualResponse.data?.sleepTimeDetail?.map { detail ->
                                    val formattedDate = detail.sleepDuration.first().startDatetime?.let {
                                            formatDate(it)
                                        }
                                    return@map formattedDate?.let {
                                        detail.sleepTimeData?.idealSleepData?.toFloat()
                                            ?.let { it1 ->
                                                detail.sleepTimeData!!.actualSleepData?.toFloat()
                                                    ?.let { it2 ->
                                                        SleepGraphData(date = it, idealSleep = it1, actualSleep = it2)
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
              //      Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Utils.dismissLoader(requireActivity())
                    actualNoDataCardView.visibility = View.VISIBLE
                    lineChart.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<SleepIdealActualResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
        //        Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                Utils.dismissLoader(requireActivity())
                actualNoDataCardView.visibility = View.VISIBLE
                lineChart.visibility = View.GONE
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
        if (sleepStageResponse.size > 0) {
            stageNoDataCardView.visibility = View.GONE
            sleepStagesView.visibility = View.VISIBLE
            remData.clear()
            awakeData.clear()
            coreData.clear()
            deepData.clear()

            var totalRemDuration = 0f
            var totalAwakeDuration = 0f
            var totalCoreDuration = 0f
            var totalDeepDuration = 0f

            for (i in 0 until sleepStageResponse.size) {
                val startDateTime =
                    LocalDateTime.parse(sleepStageResponse[i].startDatetime, formatters)
                val endDateTime = LocalDateTime.parse(sleepStageResponse[i].endDatetime, formatters)
                val duration = Duration.between(startDateTime, endDateTime).toMinutes()
                    .toFloat() / 60f // Convert to hours

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
            val totalSleepDuration =
                totalRemDuration + totalDeepDuration + totalCoreDuration + totalAwakeDuration
            val totalSleepHours = totalSleepDuration.toInt()
            val totalSleepMinutes = ((totalSleepDuration - totalSleepHours) * 60).toInt()
            view?.findViewById<TextView>(R.id.total_sleep_title)?.text = "Total Sleep:"
            view?.findViewById<TextView>(R.id.total_sleep_title)?.nextFocusRightId?.let {
                view?.findViewById<TextView>(it)?.text =
                    "${totalSleepHours}hr ${totalSleepMinutes}mins"
            }


            // Set Sleep Start and End Times
            val sleepPerformanceDetail = sleepLandingData.sleepPerformanceDetail?.firstOrNull()
            sleepPerformanceDetail?.actualSleepData?.let {
                val startTime = LocalDateTime.parse(it.sleepStartTime, formatters)
                val endTime = LocalDateTime.parse(it.sleepEndTime, formatters)
                view?.findViewById<TextView>(R.id.tv_start_sleep_time)?.text =
                    startTime.format(DateTimeFormatter.ofPattern("h:mm a"))
                view?.findViewById<TextView>(R.id.tv_alarm_time)?.text =
                    endTime.format(DateTimeFormatter.ofPattern("h:mm a"))
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
        }else{
            stageNoDataCardView.visibility = View.VISIBLE
            sleepStagesView.visibility = View.GONE
        }
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
        val token = SharedPreferenceManager.getInstance(requireActivity()).accessToken
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
       // val userId = "user_test_1"
        val period = "weekly"
        val source = "apple"
        val call = ApiClient.apiServiceFastApi.fetchSleepConsistencyDetail(userId, source, period)
        call.enqueue(object : Callback<SleepConsistencyResponse> {
            override fun onResponse(call: Call<SleepConsistencyResponse>, response: Response<SleepConsistencyResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    if (response.body() != null) {
                        sleepConsistencyResponse = response.body()!!
                        if (sleepConsistencyResponse.sleepConsistencyEntry?.sleepDetails?.size!! > 0) {
                            sleepConsistencyChart.visibility = View.VISIBLE
                            consistencyNoDataCardView.visibility = View.GONE
                            sleepConsistencyResponse.sleepConsistencyEntry?.sleepDetails?.let {
                                setData(it)
                            }
                        }else{
                            sleepConsistencyChart.visibility = View.GONE
                            consistencyNoDataCardView.visibility = View.VISIBLE
                        }
                    }

                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
           //         Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                    sleepConsistencyChart.visibility = View.GONE
                    consistencyNoDataCardView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<SleepConsistencyResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
        //        Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                sleepConsistencyChart.visibility = View.GONE
                consistencyNoDataCardView.visibility = View.VISIBLE
            }
        })
    }

    fun setData(parseSleepData: ArrayList<SleepDetails>) = runBlocking {
        val result = async {
            parseSleepData(parseSleepData)
        }.await()
        sleepConsistencyChart.setSleepData(result)
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

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun saveViewAsPdf(context: Context, view: View, fileName: String) {
        val bitmap = getBitmapFromView(view)
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        var fileUri: Uri? = null
        var success = false
        var outputStream: OutputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.pdf")
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    fileUri = uri
                    outputStream = resolver.openOutputStream(uri)
                }
            } else {
                val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(directory, "$fileName.pdf")
                fileUri = Uri.fromFile(file)
                outputStream = file.outputStream()
            }

            outputStream?.use {
                document.writeTo(it)
                success = true
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            document.close()
            outputStream?.close()
        }

        if (success && fileUri != null) {
            showDownloadNotification(context, fileName, fileUri)
        }
    }

    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showDownloadNotification(context: Context, fileName: String, fileUri: Uri) {
        val channelId = "download_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Downloads",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Download Notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent to open the PDF file
        val openPdfIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, openPdfIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle("Download Complete")
            .setContentText("$fileName.pdf saved to Downloads")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent) // Open PDF on click
            .setAutoCancel(true) // Dismiss when tapped
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
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