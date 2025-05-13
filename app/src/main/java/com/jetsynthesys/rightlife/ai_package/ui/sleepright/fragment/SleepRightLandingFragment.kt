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
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.lifecycleScope
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.SleepLandingResponse
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentSleepRightLandingBinding
import com.jetsynthesys.rightlife.ui.NewSleepSounds.NewSleepSoundActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jetsynthesys.rightlife.ai_package.model.BloodPressure
import com.jetsynthesys.rightlife.ai_package.model.BodyFatPercentage
import com.jetsynthesys.rightlife.ai_package.model.BodyMass
import com.jetsynthesys.rightlife.ai_package.model.Distance
import com.jetsynthesys.rightlife.ai_package.model.EnergyBurnedRequest
import com.jetsynthesys.rightlife.ai_package.model.HeartRateRequest
import com.jetsynthesys.rightlife.ai_package.model.HeartRateVariabilityRequest
import com.jetsynthesys.rightlife.ai_package.model.OxygenSaturation
import com.jetsynthesys.rightlife.ai_package.model.RespiratoryRate
import com.jetsynthesys.rightlife.ai_package.model.SleepConsistency
import com.jetsynthesys.rightlife.ai_package.model.SleepConsistencyResponse
import com.jetsynthesys.rightlife.ai_package.model.SleepDetails
import com.jetsynthesys.rightlife.ai_package.model.SleepJson
import com.jetsynthesys.rightlife.ai_package.model.SleepJsonRequest
import com.jetsynthesys.rightlife.ai_package.model.SleepLandingAllData
import com.jetsynthesys.rightlife.ai_package.model.SleepPerformanceDetail
import com.jetsynthesys.rightlife.ai_package.model.SleepRestorativeDetail
import com.jetsynthesys.rightlife.ai_package.model.SleepStage
import com.jetsynthesys.rightlife.ai_package.model.SleepStageJson
import com.jetsynthesys.rightlife.ai_package.model.SleepStagesData
import com.jetsynthesys.rightlife.ai_package.model.StepCountRequest
import com.jetsynthesys.rightlife.ai_package.model.StoreHealthDataRequest
import com.jetsynthesys.rightlife.ai_package.model.WakeupData
import com.jetsynthesys.rightlife.ai_package.model.WakeupTimeResponse
import com.jetsynthesys.rightlife.ai_package.model.WorkoutRequest
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.jetsynthesys.rightlife.ui.utility.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max

class SleepRightLandingFragment : BaseFragment<FragmentSleepRightLandingBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSleepRightLandingBinding
        get() = FragmentSleepRightLandingBinding::inflate

    private lateinit var todaysSleepRequirement: TextView
    private lateinit var todaysSleepStartTime: TextView
    private lateinit var tvStageSleepStartTime: TextView
    private lateinit var tvStageSleepEndTime: TextView
    private lateinit var tvStageSleepTotalTime: TextView
    private lateinit var tvStageCoreTime: TextView
    private lateinit var tvStageDeepTime: TextView
    private lateinit var tvStageRemTime: TextView
    private lateinit var tvStageAwakeTime: TextView
    private lateinit var todaysWakeupTime: TextView
    private lateinit var tvPerformStartTime: TextView
    private lateinit var tvPerformWakeTime: TextView
    private lateinit var tvPerformSleepPercent: TextView
    private lateinit var tvPerformSleepDuration: TextView
    private lateinit var tvPerformIdealDuration: TextView
    private lateinit var imgPerformAction: ImageView
    private lateinit var tvPerformAction: TextView
    private lateinit var tvPerformMessage: TextView
    private lateinit var tvIdealActualDate: TextView
    private lateinit var tvActualTime: TextView
    private lateinit var tvIdealTime: TextView
    private lateinit var tvRestoRemTime: TextView
    private lateinit var tvRestoDeepTime: TextView
    private lateinit var tvRestoStartTime: TextView
    private lateinit var tvRestoEndTime: TextView
    private lateinit var tvConsistencyDate: TextView
    private lateinit var tvConsistencyTime: TextView
    private lateinit var landingPageResponse: SleepLandingResponse
    private lateinit var landingAllData: SleepLandingAllData
    private lateinit var lineChart:LineChart
    private val remData: ArrayList<Float> = arrayListOf()
    private val awakeData: ArrayList<Float> = arrayListOf()
    private val coreData: ArrayList<Float> = arrayListOf()
    private val deepData: ArrayList<Float> = arrayListOf()
    private val formatters = DateTimeFormatter.ISO_DATE_TIME
    private lateinit var wakeupTimeResponse: WakeupTimeResponse
    private lateinit var sleepStagesView: SleepChartViewLanding
    private lateinit var sleepConsistencyChart: SleepGraphView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sleepConsistencyResponse: SleepConsistencyResponse
    private lateinit var logYourNap : LinearLayout
    private lateinit var actualNoDataCardView : CardView
    private lateinit var stageNoDataCardView : CardView
    private lateinit var performCardView : CardView
    private lateinit var sleepTimeRequirementCardView : CardView
    private lateinit var performNoDataCardView : CardView
    private lateinit var restroNoDataCardView : CardView
    private lateinit var consistencyNoDataCardView : CardView
    private lateinit var mainView : LinearLayout
    private lateinit var downloadView: ImageView
    private lateinit var sleepArrowView: ImageView
    private lateinit var sleepPerformView: ImageView
    private lateinit var restorativeChart: SleepRestoChartView
    private var sleepSessionRecord: List<SleepSessionRecord>? = null
    private lateinit var healthConnectClient: HealthConnectClient
    private var mWakeupTime = ""
    private var mRecordId = ""

    private val allReadPermissions = setOf(
        HealthPermission.getReadPermission(SleepSessionRecord::class)
    )

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSeatName = arguments?.getString("BottomSeatName").toString()

        val sleepChart = view.findViewById<LineChart>(R.id.sleepChart)
        sleepArrowView = view.findViewById(R.id.img_sleep_arrow)
        sleepPerformView = view.findViewById(R.id.img_sleep_perform_arrow)
        lineChart = view.findViewById(R.id.sleepIdealActualChart)
        val backButton = view.findViewById<ImageView>(R.id.img_back)
        sleepConsistencyChart = view.findViewById<SleepGraphView>(R.id.sleepConsistencyChart)
        downloadView = view.findViewById(R.id.sleep_download_icon)
        mainView = view.findViewById(R.id.lyt_main_view)
        logYourNap = view.findViewById(R.id.btn_log_nap)
        sleepTimeRequirementCardView = view.findViewById(R.id.card_sleep_time_requirement)
        val sleepInfo = view.findViewById<ImageView>(R.id.img_sleep_right)
        val editWakeup = view.findViewById<ImageView>(R.id.img_edit_wakeup_time)
        val sleepPerform = view.findViewById<ImageView>(R.id.img_sleep_perform_right)
        val sleepIdeal = view.findViewById<ImageView>(R.id.img_sleep_ideal_actual)
        val restoSleep = view.findViewById<ImageView>(R.id.img_resto_sleep)
        val consistencySleep = view.findViewById<ImageView>(R.id.img_consistency_right)
        val openConsistency = view.findViewById<ImageView>(R.id.consistency_right_arrow)
         actualNoDataCardView = view.findViewById(R.id.ideal_actual_nodata_layout)
         restroNoDataCardView = view.findViewById(R.id.restro_nodata_layout)
         performNoDataCardView = view.findViewById(R.id.perform_nodata_layout)
        performCardView = view.findViewById(R.id.sleep_perform_layout)
         stageNoDataCardView = view.findViewById(R.id.lyt_sleep_stage_no_data)
        consistencyNoDataCardView = view.findViewById(R.id.consistency_nodata_layout)
        todaysWakeupTime = view.findViewById(R.id.tv_todays_wakeup_time)
        todaysSleepRequirement = view.findViewById(R.id.tv_todays_sleep_time_requirement)
        todaysSleepStartTime = view.findViewById(R.id.tv_todays_sleep_start_time)
        tvStageSleepStartTime = view.findViewById(R.id.tv_stage_start_sleep_time)
        tvStageSleepEndTime = view.findViewById(R.id.tv_stage_end_sleep_time)
        tvPerformStartTime = view.findViewById(R.id.tv_perform_start_time)
        tvPerformWakeTime = view.findViewById(R.id.tv_perform_wake_time)
        tvPerformSleepPercent = view.findViewById(R.id.tv_perform_sleep_percent)
        tvPerformSleepDuration = view.findViewById(R.id.tv_perform_sleep_duration)
        tvPerformIdealDuration = view.findViewById(R.id.tv_perform_ideal_duration)
        tvPerformAction = view.findViewById(R.id.tv_perform_action)
        imgPerformAction = view.findViewById(R.id.img_perform_action)
        tvPerformMessage = view.findViewById(R.id.tv_perform_message)
        tvIdealActualDate = view.findViewById(R.id.tv_ideal_actual_date)
        tvActualTime = view.findViewById(R.id.tv_actual_time)
        tvIdealTime = view.findViewById(R.id.tv_ideal_time)
        tvRestoStartTime = view.findViewById(R.id.tv_resto_start_time)
        tvRestoEndTime = view.findViewById(R.id.tv_resto_end_time)
        tvRestoRemTime = view.findViewById(R.id.tv_resto_rem_time)
        tvRestoDeepTime = view.findViewById(R.id.tv_resto_deep_time)
        tvConsistencyDate = view.findViewById(R.id.tv_consistency_date)
        tvConsistencyTime = view.findViewById(R.id.tv_consistency_time)
        tvStageSleepTotalTime = view.findViewById(R.id.tv_stage_total_time)
        tvStageRemTime = view.findViewById(R.id.tv_stage_rem_time)
        tvStageCoreTime = view.findViewById(R.id.tv_stage_core_time)
        tvStageDeepTime = view.findViewById(R.id.tv_stage_deep_time)
        tvStageAwakeTime = view.findViewById(R.id.tv_stage_awake_time)

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)

        if (bottomSeatName.contentEquals("LogLastNightSleep")){
            val bottomSheet = LogYourNapDialogFragment(requireContext())
            bottomSheet.show(parentFragmentManager, "LogYourNapDialogFragment")
        }
        val availabilityStatus = HealthConnectClient.getSdkStatus(requireContext())
        if (availabilityStatus == HealthConnectClient.SDK_AVAILABLE) {
            healthConnectClient = HealthConnectClient.getOrCreate(requireContext())
            lifecycleScope.launch {
                requestPermissionsAndReadAllData()
            }
        } else {
            Toast.makeText(context, "Please install or update samsung from the Play Store.", Toast.LENGTH_LONG).show()
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
            val bottomSheet = LogYourNapDialogFragment(requireContext())
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
            navigateToFragment(SleepConsistencyFragment(), "SleepConsistencyFragment")
        }
        openConsistency.setOnClickListener {
            navigateToFragment(SleepConsistencyFragment(), "SleepConsistencyFragment")
        }

        fetchSleepLandingData()
        fetchWakeupData()

        editWakeup.setOnClickListener {
            openBottomSheet()
        }

        restorativeChart = view.findViewById(R.id.restorativeChart)
        sleepStagesView = view.findViewById<SleepChartViewLanding>(R.id.sleepStagesView)

        view.findViewById<LinearLayout>(R.id.play_now).setOnClickListener {
            startActivity(Intent(requireContext(), NewSleepSoundActivity::class.java).apply {
                putExtra("PlayList", "PlayList")
            })
        }
      //  storeData()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private suspend fun requestPermissionsAndReadAllData() {
        try {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (allReadPermissions.all { it in granted }) {
                fetchAllHealthData()
                storeHealthData()
            } else {
                requestPermissionsLauncher.launch(allReadPermissions)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error checking permissions: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private val requestPermissionsLauncher = registerForActivityResult(PermissionController.createRequestPermissionResultContract()) { granted ->
        lifecycleScope.launch {
            if (granted.containsAll(allReadPermissions)) {
                fetchAllHealthData()
                storeHealthData()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Some permissions denied, using available data", Toast.LENGTH_SHORT).show()
                }
                fetchAllHealthData()
                storeHealthData()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private suspend fun fetchAllHealthData() {
        val endTime = Instant.now()
        val startTime = endTime.minusSeconds(7 * 24 * 60 * 60)
        try {
            val grantedPermissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(SleepSessionRecord::class) in grantedPermissions) {
                val sleepResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = SleepSessionRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                sleepSessionRecord = sleepResponse.records
                sleepSessionRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Sleep Session: Start: ${record.startTime}, End: ${record.endTime}, Stages: ${record.stages}")
                }
            } else {
                sleepSessionRecord = emptyList()
                android.util.Log.d("HealthData", "Sleep session permission denied")
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Health Data Fetched", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error fetching health data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun storeHealthData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userid = SharedPreferenceManager.getInstance(requireActivity()).userId ?: "68010b615a508d0cfd6ac9ca"
                val sleepStage = sleepSessionRecord?.flatMap { record ->
                    record.stages.mapNotNull { stage ->
                        val stageValue = when (stage.stage) {
                            SleepSessionRecord.STAGE_TYPE_DEEP -> "5"
                            SleepSessionRecord.STAGE_TYPE_LIGHT -> "4"
                            SleepSessionRecord.STAGE_TYPE_REM -> "6"
                            SleepSessionRecord.STAGE_TYPE_AWAKE -> "1"
                            else -> null
                        }
                        stageValue?.let {
                            SleepStageJson(
                                start_datetime = convertToTargetFormat(stage.startTime.toString()),
                                end_datetime = convertToTargetFormat(stage.endTime.toString()),
                                record_type = it,
                                unit = "stage",
                                value = it,
                                source_name = "samsung"
                            )
                        }
                    }
                } ?: emptyList()
                val sleepJsonRequest = SleepJsonRequest(user_id = userid, source = "samsung", sleep_stage = sleepStage)
                val response = ApiClient.apiServiceFastApi.storeSleepData(sleepJsonRequest)
                withContext(Dispatchers.Main) {
                    /*if (response.isSuccessful) {
                        Toast.makeText(requireContext(), response.body()?.message ?: "Health data stored successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error storing data: ${response.code()} - ${response.message()}", Toast.LENGTH_SHORT).show()
                    }*/
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchWakeupData() {
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val date = getCurrentDate()
        val source = "apple"
        val call = ApiClient.apiServiceFastApi.fetchWakeupTime(userId, source)
        call.enqueue(object : Callback<WakeupTimeResponse> {
            override fun onResponse(call: Call<WakeupTimeResponse>, response: Response<WakeupTimeResponse>) {
                if (response.isSuccessful) {
                    sleepTimeRequirementCardView.visibility = View.VISIBLE
                    wakeupTimeResponse = response.body()!!
                    setWakeupData(wakeupTimeResponse.data.getOrNull(0))
                }else if(response.code() == 400){
                    progressDialog.dismiss()
                    sleepTimeRequirementCardView.visibility = View.GONE
                  //  Toast.makeText(activity, "Record Not Found", Toast.LENGTH_SHORT).show()
                } else {
                    sleepTimeRequirementCardView.visibility = View.GONE
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<WakeupTimeResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
            }
        })
    }

    fun setWakeupData(wakeupData: WakeupData?){
        todaysSleepRequirement.setText(convertDecimalHoursToHrMinFormat(wakeupData?.currentRequirement!!))
        todaysSleepStartTime.setText(formatIsoTo12Hour(wakeupData.sleepDatetime!!))
        todaysWakeupTime.setText(formatIsoTo12Hour(wakeupData.wakeupDatetime!!))
        mWakeupTime = wakeupData.wakeupDatetime!!
        mRecordId = wakeupData.Id!!
    }

    private fun convertDecimalHoursToHrMinFormat(hoursDecimal: Double): String {
        val totalMinutes = (hoursDecimal * 60).toInt()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return String.format("%02dhr %02dmins", hours, minutes)
    }

    fun convertDecimalMinutesToHrMinFormat(decimalMinutes: Double): String {
        val totalMinutes = decimalMinutes.toInt()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return when {
            hours > 0 && minutes > 0 -> "${hours} hr ${minutes} min"
            hours > 0 -> "${hours} hr"
            else -> "${minutes} min"
        }
    }

    fun formatIsoTo12Hour(timeStr: String): String {
        val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("h:mm a")
        val dateTime = LocalDateTime.parse(timeStr, inputFormatter)
        return dateTime.format(outputFormatter)
    }

    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.now().format(formatter)
    }

    private fun openBottomSheet() {
        val dialog = WakeUpTimeDialogFragment(
            context = requireContext(),
            wakeupTime = mWakeupTime,
            recordId = mRecordId,
            listener = object : OnWakeUpTimeSelectedListener {
                override fun onWakeUpTimeSelected(time: String) {
                    todaysWakeupTime.setText(time)
                }
            }
        )

        dialog.show(parentFragmentManager, "WakeUpTimeDialog")
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
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId ?: ""
        val date = "2025-04-30"
        val source = "apple"
        val preferences = "nature_sounds"
        val call = ApiClient.apiServiceFastApi.fetchSleepLandingPage(userId, source, date, preferences)
        call.enqueue(object : Callback<SleepLandingResponse> {
            override fun onResponse(call: Call<SleepLandingResponse>, response: Response<SleepLandingResponse>) {
                if (response.isSuccessful) {
                    Utils.dismissLoader(requireActivity())
                    landingPageResponse = response.body()!!
                    setSleepRightLandingData(landingPageResponse)
                }else if(response.code() == 400){
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Record Not Found", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Utils.dismissLoader(requireActivity())
                    stageNoDataCardView.visibility = View.VISIBLE
                    sleepStagesView.visibility = View.GONE
                    performNoDataCardView.visibility = View.VISIBLE
                    performCardView.visibility = View.GONE
                    restroNoDataCardView.visibility = View.VISIBLE
                    restorativeChart.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<SleepLandingResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                Utils.dismissLoader(requireActivity())
                stageNoDataCardView.visibility = View.VISIBLE
                sleepStagesView.visibility = View.GONE
                performNoDataCardView.visibility = View.VISIBLE
                performCardView.visibility = View.GONE
                restroNoDataCardView.visibility = View.VISIBLE
                restorativeChart.visibility = View.GONE
            }
        })
    }

    private fun setSleepRightLandingData(sleepLandingResponse: SleepLandingResponse) {
        landingAllData = sleepLandingResponse.sleepLandingAllData!!
        landingAllData.userId = sleepLandingResponse.sleepLandingAllData?.userId ?: ""
        landingAllData.source = sleepLandingResponse.sleepLandingAllData?.source ?: ""
        landingAllData.endDate = sleepLandingResponse.sleepLandingAllData?.endDate ?: ""
        landingAllData.startDate = sleepLandingResponse.sleepLandingAllData?.startDate ?: ""
        landingAllData.recommendedSound = sleepLandingResponse.sleepLandingAllData?.recommendedSound ?: ""

        // Set Sleep Stages Data
        val sleepStageData: ArrayList<SleepStagesData> = arrayListOf()
        if (sleepLandingResponse.sleepLandingAllData?.sleepStagesDetail?.sleepStage != null) {
            for (i in 0 until sleepLandingResponse.sleepLandingAllData?.sleepStagesDetail?.sleepStage?.size!!) {
                sleepLandingResponse.sleepLandingAllData?.sleepStagesDetail?.sleepStage?.getOrNull(i)?.let {
                    sleepStageData.add(it)
                }
            }
            setSleepRightStageData(sleepStageData)
            tvStageSleepStartTime.setText(convertTo12HourFormat(sleepLandingResponse.sleepLandingAllData?.sleepStagesDetail?.sleepStartTime!!))
            tvStageSleepEndTime.setText(convertTo12HourFormat(sleepLandingResponse.sleepLandingAllData?.sleepStagesDetail?.sleepEndTime!!))
            tvStageSleepTotalTime.text = convertDecimalMinutesToHrMinFormat(sleepLandingResponse.sleepLandingAllData?.sleepStagesDetail?.totalSleepDurationMinutes!!)
        }

        // Set Sleep Performance Data
        if (sleepLandingResponse.sleepLandingAllData?.sleepPerformanceDetail != null) {
                performNoDataCardView.visibility = View.GONE
                performCardView.visibility = View.VISIBLE
                setSleepPerformanceData(sleepLandingResponse.sleepLandingAllData?.sleepPerformanceDetail!!)
        }else{
                performNoDataCardView.visibility = View.VISIBLE
                performCardView.visibility = View.GONE
        }

        //IdealActualResponse
        if (landingAllData.idealVsActualSleepTime.isNotEmpty()) {
            tvActualTime.text = convertDecimalHoursToHrMinFormat(landingAllData.idealVsActualSleepTime.getOrNull(landingAllData.idealVsActualSleepTime.size.minus(1))?.actualSleepHours!!)
            tvIdealTime.text = convertDecimalHoursToHrMinFormat(landingAllData.idealVsActualSleepTime.getOrNull(landingAllData.idealVsActualSleepTime.size.minus(1))?.idealSleepHours!!)
            tvIdealActualDate.text = convertDateToNormalDate(landingAllData.idealVsActualSleepTime.getOrNull(landingAllData.idealVsActualSleepTime.size.minus(1))?.date!!)
            var sleepDataList: List<SleepGraphData>? = arrayListOf()
                actualNoDataCardView.visibility = View.GONE
                lineChart.visibility = View.VISIBLE
                sleepDataList = landingAllData.idealVsActualSleepTime.map { detail ->
                    val formattedDate = detail.date?.let { formatIdealDate(it) }
                    return@map formattedDate?.let {
                        detail.idealSleepHours?.toFloat()?.let { it1 ->
                            detail.actualSleepHours?.toFloat()?.let { it2 ->
                                SleepGraphData(date = it, idealSleep = it1, actualSleep = it2)
                            }
                        }
                    }!!
                }
                val weekRanges = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                setIdealGraphDataFromSleepList(sleepDataList, weekRanges)
            } else {
                actualNoDataCardView.visibility = View.VISIBLE
                lineChart.visibility = View.GONE
            }

        // Set Restorative Sleep Data
        if (sleepLandingResponse.sleepLandingAllData?.sleepRestorativeDetail != null) {
                restroNoDataCardView.visibility = View.GONE
                restorativeChart.visibility = View.VISIBLE
                setRestorativeSleepData(sleepLandingResponse.sleepLandingAllData?.sleepRestorativeDetail)
            } else{
                restroNoDataCardView.visibility = View.VISIBLE
                restorativeChart.visibility = View.GONE
            }

        //Set Consistency Sleep Data
        if (sleepLandingResponse.sleepLandingAllData?.sleepConsistency != null) {
            consistencyNoDataCardView.visibility = View.GONE
            sleepConsistencyChart.visibility = View.VISIBLE

            tvConsistencyTime.text = convertDecimalHoursToHrMinFormat(landingAllData.sleepConsistency?.sleepConsistencyDetail?.averageSleepDurationHours!!)
            tvConsistencyDate.text = convertDateToNormalDate(landingAllData.sleepConsistency?.sleepDetails?.getOrNull(
                landingAllData.sleepConsistency?.sleepDetails?.size?.minus(1) ?:0)?.date!!)

            setConsistencySleepData(sleepLandingResponse.sleepLandingAllData?.sleepConsistency)
        } else{
            consistencyNoDataCardView.visibility = View.VISIBLE
            sleepConsistencyChart.visibility = View.GONE
        }

        // Set Recommended Sound
        view?.findViewById<TextView>(R.id.tv_sleep_sound)?.text = "Recommended Sound"
        view?.findViewById<TextView>(R.id.tv_sleep_sound_save)?.text = sleepLandingResponse.sleepLandingAllData?.recommendedSound
    }

    private fun setIdealGraphDataFromSleepList(sleepData: List<SleepGraphData>?, weekRanges: List<String>) {
        val idealEntries = ArrayList<Entry>()
        val actualEntries = ArrayList<Entry>()
        val labels = weekRanges

        sleepData?.forEachIndexed { index, data ->
            idealEntries.add(Entry(index.toFloat(), data.idealSleep))
            actualEntries.add(Entry(index.toFloat(), data.actualSleep))
        }

        val idealSet = LineDataSet(idealEntries, "Ideal").apply {
            color = Color.parseColor("#00C853") // green
            circleRadius = 5f
            setCircleColor(color)
            valueTextSize = 10f
        }

        val actualSet = LineDataSet(actualEntries, "Actual").apply {
            color = Color.parseColor("#2979FF") // blue
            circleRadius = 5f
            setCircleColor(color)
            valueTextSize = 10f
        }

        lineChart.data = LineData(idealSet, actualSet)

        lineChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 12f
        }

        lineChart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 20f
            granularity = 1f
            textSize = 12f
        }

        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.legend.textSize = 12f
        lineChart.setPinchZoom(false)
        lineChart.setDrawGridBackground(false)
        lineChart.setScaleEnabled(false) // disables pinch and double-tap zoom
        lineChart.isDoubleTapToZoomEnabled = false // disables zoom on double tap

        lineChart.isDragEnabled = false
        lineChart.isHighlightPerTapEnabled = false

        lineChart.invalidate()
    }

    fun convertDateToNormalDate(dateStr: String): String{
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("EEEE dd MMM, yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(dateStr, inputFormatter)
        val formatted = date.format(outputFormatter)
        return formatted
    }

    fun convertTo12HourFormat(input: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a") // 12-hour format with AM/PM
        val dateTime = LocalDateTime.parse(input, inputFormatter)
        return outputFormatter.format(dateTime)
    }


    private fun formatIdealDate(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE dd MMM", Locale.getDefault())
        return outputFormat.format(inputFormat.parse(dateTime)!!)
    }

    private fun formatDate(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE dd MMM", Locale.getDefault())
        return outputFormat.format(inputFormat.parse(dateTime)!!)
    }

    private fun setSleepRightStageData(sleepStageResponse: ArrayList<SleepStagesData>) {
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

                when (sleepStageResponse[i].stage) {
                    "REM Sleep" -> {
                        remData.add(duration)
                        totalRemDuration += duration
                    }

                    "Deep Sleep" -> {
                        deepData.add(duration)
                        totalDeepDuration += duration
                    }

                    "Light Sleep" -> {
                        coreData.add(duration)
                        totalCoreDuration += duration
                    }

                    "Awake" -> {
                        awakeData.add(duration)
                        totalAwakeDuration += duration
                    }
                }
            }
            tvStageCoreTime.text = formatDuration(totalCoreDuration)
            tvStageAwakeTime.text = formatDuration(totalAwakeDuration)
            tvStageDeepTime.text = formatDuration(totalDeepDuration)
            tvStageRemTime.text = formatDuration(totalRemDuration)
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

    private fun setStageGraph(sleepStageResponse: ArrayList<SleepStagesData>) {
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
            val color = when (stage.stage) {
                "REM Sleep" -> resources.getColor(R.color.light_blue_bar) // Light blue
                "Deep Sleep" -> resources.getColor(R.color.purple_bar) // Dark blue
                "Light Sleep" -> resources.getColor(R.color.blue_bar) // Medium blue
                "Awake" -> resources.getColor(R.color.red_orange_bar) // Red
                else -> Color.GRAY
            }
            sleepData.add(SleepSegmentModel(start, end, color, 110f))
            currentPosition += duration
        }

        sleepStagesView.setSleepData(sleepData)
    }

    private fun setSleepPerformanceData(sleepPerformanceDetail: SleepPerformanceDetail) {
            tvPerformStartTime.text = formatIsoTo12Hour(sleepPerformanceDetail.actualSleepData?.sleepStartTime!!)
            tvPerformWakeTime.text = formatIsoTo12Hour(sleepPerformanceDetail.actualSleepData?.sleepEndTime!!)
            tvPerformSleepPercent.text = sleepPerformanceDetail.sleepPerformanceData?.sleepPerformance?.toInt().toString()
            tvPerformSleepDuration.text = convertDecimalHoursToHrMinFormat(sleepPerformanceDetail.actualSleepData?.actualSleepDurationHours!!)
            tvPerformIdealDuration.text = convertDecimalHoursToHrMinFormat(sleepPerformanceDetail.idealSleepDuration!!)
            tvPerformAction.text = "Under"
            tvPerformMessage.text = sleepPerformanceDetail.sleepPerformanceData?.actionStep + " " + sleepPerformanceDetail.sleepPerformanceData?.message
            imgPerformAction.setImageResource(R.drawable.yellow_info)
    }

    private fun setRestorativeSleepData(sleepRestorativeDetail: SleepRestorativeDetail?) {
        tvRestoRemTime.text = addRemStageData(sleepRestorativeDetail?.sleepStagesData)
        tvRestoDeepTime.text = addDeepStageData(sleepRestorativeDetail?.sleepStagesData)
        tvRestoStartTime.text = formatIsoTo12Hour(sleepRestorativeDetail?.sleepStartTime!!)
        tvRestoEndTime.text = formatIsoTo12Hour(sleepRestorativeDetail.sleepEndTime!!)
        restorativeChart.setSleepData(sleepRestorativeDetail.sleepStagesData)
        }

    private fun addDeepStageData(sleepStagesData: ArrayList<SleepStagesData>?): String {
        var totalDeepDuration = 0.0

        for (i in 0 until sleepStagesData?.size!!) {
            val startDateTime = LocalDateTime.parse(sleepStagesData[i].startDatetime, formatters)
            val endDateTime = LocalDateTime.parse(sleepStagesData[i].endDatetime, formatters)
            val duration = Duration.between(startDateTime, endDateTime).toMinutes()
                .toFloat() / 60 // Convert to hours

            when (sleepStagesData[i].stage) {
                "Deep Sleep" -> {
                    deepData.add(duration)
                    totalDeepDuration += duration
                }
            }
        }
        return convertDecimalHoursToHrMinFormat(totalDeepDuration)
    }

    private fun addRemStageData(sleepStagesData: ArrayList<SleepStagesData>?): String {
        var totalRemDuration = 0.0

        for (i in 0 until sleepStagesData?.size!!) {
            val startDateTime =
                LocalDateTime.parse(sleepStagesData[i].startDatetime, formatters)
            val endDateTime = LocalDateTime.parse(sleepStagesData[i].endDatetime, formatters)
            val duration = Duration.between(startDateTime, endDateTime).toMinutes()
                .toFloat() / 60 // Convert to hours

            when (sleepStagesData[i].stage) {
                "REM Sleep" -> {
                    remData.add(duration)
                    totalRemDuration += duration
                }
            }
        }
        return convertDecimalHoursToHrMinFormat(totalRemDuration)
    }

    fun setConsistencySleepData(consistencyData: SleepConsistency?) = runBlocking {
        val parseSleepData: ArrayList<SleepDetails> = arrayListOf()
        if (consistencyData?.sleepDetails!=null) {
            for (sleepEntry in consistencyData.sleepDetails) {
                parseSleepData.add(sleepEntry)
            }
        }
        val result = async {
            parseSleepData(parseSleepData)
        }.await()
        sleepConsistencyChart.setSleepData(result)
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

    fun convertToTargetFormat(input: String): String {
        val possibleFormats = listOf(
            "yyyy-MM-dd'T'HH:mm:ssX",        // ISO with timezone
            "yyyy-MM-dd'T'HH:mm:ss.SSSX",    // ISO with milliseconds
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSX", // ISO with nanoseconds
            "yyyy-MM-dd HH:mm:ss",           // Common DB format
            "yyyy/MM/dd HH:mm:ss",           // Slash format
            "dd-MM-yyyy HH:mm:ss",           // Day-Month-Year
            "MM/dd/yyyy HH:mm:ss"            // US format
        )

        // Check if it's a nanosecond timestamp (very large number)
        if (input.matches(Regex("^\\d{18,}$"))) {
            return try {
                val nanos = input.toLong()
                val seconds = nanos / 1_000_000_000
                val nanoAdjustment = (nanos % 1_000_000_000).toInt()
                val instant = Instant.ofEpochSecond(seconds, nanoAdjustment.toLong())
                val targetFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .withZone(ZoneOffset.UTC)
                return targetFormatter.format(instant)
            } catch (e: Exception) {
                ""
            }
        }

        // Try known patterns
        for (pattern in possibleFormats) {
            try {
                val formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneOffset.UTC)
                val temporal = formatter.parse(input)
                val instant = Instant.from(temporal)
                val targetFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .withZone(ZoneOffset.UTC)
                return targetFormatter.format(instant)
            } catch (e: DateTimeParseException) {
                // Try next format
            }
        }

        return "" // Unable to parse
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

    private fun storeData(){
        val jsonData : SleepJson = loadStepData()
        val fullList = jsonData.dataPoints
        val sleepJsonRequest = SleepJsonRequest(
            user_id = "68010b615a508d0cfd6ac9ca",
            source = "google",
            sleep_stage = fullList.map {
                SleepStageJson(
                    value = it.fitValue[0].value?.intVal.toString(),
                    record_type = it.fitValue[0].value?.intVal.toString(),
                    end_datetime = convertToTargetFormat(it.endTimeNanos.toString()),
                    unit = it.dataTypeName.toString(),
                    start_datetime = convertToTargetFormat(it.startTimeNanos.toString()),
                    source_name = "google"
                )
            } as ArrayList<SleepStageJson>
        )
        storeJsonHealthData(sleepJsonRequest)
    }

    fun convertNanoToFormattedDate(nanoTime: Long): String {
        val millis = nanoTime / 1_000_000
        val date = Date(millis)

        // Format the date
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun storeJsonHealthData(sleepJsonRequest: SleepJsonRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userid = SharedPreferenceManager.getInstance(requireActivity()).userId ?: "68010b615a508d0cfd6ac9ca"

                val response = ApiClient.apiServiceFastApi.storeSleepData(sleepJsonRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Toast.makeText(requireContext(), responseBody?.message ?: "Health data stored successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error storing data: ${response.code()} - ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadStepData(): SleepJson {
        val json = context?.assets?.open("sleep_raw_data.json")
            ?.bufferedReader().use { it?.readText() }
        return Gson().fromJson(json, object : TypeToken<SleepJson>() {}.type)
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

interface OnWakeUpTimeSelectedListener {
    fun onWakeUpTimeSelected(time: String)
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

class SleepRestoChartView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val sleepSegments = mutableListOf<Segment>()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    private data class Segment(
        val position: Position1,
        val widthFraction: Float,
        val color: Int
    )

    fun setSleepData(data: List<SleepStagesData>) {
        sleepSegments.clear()

        if (data.isEmpty()) return

        // Parse timestamps and calculate total duration
        val parsedData = data.mapNotNull {
            try {
                val start = dateFormat.parse(it.startDatetime)?.time ?: return@mapNotNull null
                val end = dateFormat.parse(it.endDatetime)?.time ?: return@mapNotNull null
                val durationMinutes = TimeUnit.MILLISECONDS.toMinutes(end - start).toInt()
                Triple(it.stage, durationMinutes, getPositionForRecordType(it.stage!!))
            } catch (e: Exception) {
                null
            }
        }

        val totalMinutes = parsedData.sumOf { it.second }

        parsedData.forEach { (recordType, duration, position) ->
            val fraction = duration.toFloat() / max(totalMinutes, 1)
            val color = getColorForRecordType(recordType!!)
            sleepSegments.add(Segment(position, fraction, color))
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        // Set the background color (replace with desired color)
        canvas.drawColor(Color.parseColor("#F5F9FF")) // Example: light gray

        val barHeight = h * 0.25f
        val cornerRadius = barHeight / 4
        var currentX = 0f

        sleepSegments.forEach { segment ->
            paint.color = segment.color

            val top = when (segment.position) {
                Position1.UPPER -> h * 0.1f
                Position1.LOWER -> h * 0.35f
            }
            val bottom = top + barHeight
            val right = currentX + (segment.widthFraction * w)

            canvas.drawRoundRect(RectF(currentX, top, right, bottom), cornerRadius, cornerRadius, paint)
            currentX = right
        }
    }

    private fun getPositionForRecordType(type: String): Position1 {
        return when (type) {
            "REM Sleep" -> Position1.UPPER
            "Deep Sleep" -> Position1.LOWER
            else -> error("Unknown record type: $type")
        }
    }

    private fun getColorForRecordType(type: String): Int {
        return when (type) {
            "REM Sleep" -> resources.getColor(R.color.light_blue_bar)
            "Deep Sleep" -> resources.getColor(R.color.purple_bar)
            else -> error("Unknown record type: $type")
        }
    }
}

enum class Position1 {
    UPPER, LOWER
}

data class SleepSegmentModel(val start: Float, val end: Float, val color: Int, val height: Float)