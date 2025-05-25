package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.*
import com.jetsynthesys.rightlife.ai_package.ui.adapter.CarouselAdapter
import com.jetsynthesys.rightlife.ai_package.ui.adapter.GridAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.MealSummaryInfoBottomSheet
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.YourMealLogsFragment
import com.jetsynthesys.rightlife.ai_package.ui.moveright.graphs.LineGrapghViewSteps
import com.jetsynthesys.rightlife.ai_package.ui.moveright.graphs.LineGraphView
import com.jetsynthesys.rightlife.ai_package.ui.steps.SetYourStepGoalFragment
import com.jetsynthesys.rightlife.ai_package.utils.AppPreference
import com.jetsynthesys.rightlife.databinding.FragmentLandingBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.math.abs

class MoveRightLandingFragment : BaseFragment<FragmentLandingBinding>() {

    private lateinit var carouselViewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private lateinit var nodataWorkout: ConstraintLayout
    private lateinit var dataFilledworkout: ConstraintLayout
    private lateinit var calorie_no_data_filled_layout: ConstraintLayout
    private lateinit var calorie_layout_data_filled: ConstraintLayout
    private lateinit var dots: Array<ImageView?>
    private var totalCaloriesBurnedRecord: List<TotalCaloriesBurnedRecord>? = null
    private var activeCalorieBurnedRecord: List<ActiveCaloriesBurnedRecord>? = null
    private var stepsRecord: List<StepsRecord>? = null
    private var heartRateRecord: List<HeartRateRecord>? = null
    private var heartRateVariability: List<HeartRateVariabilityRmssdRecord>? = null
    private var restingHeartRecord: List<RestingHeartRateRecord>? = null
    private var basalMetabolicRateRecord: List<BasalMetabolicRateRecord>? = null
    private var bloodPressureRecord: List<BloodPressureRecord>? = null
    private var sleepSessionRecord: List<SleepSessionRecord>? = null
    private var exerciseSessionRecord: List<ExerciseSessionRecord>? = null
    private var speedRecord: List<SpeedRecord>? = null
    private var weightRecord: List<WeightRecord>? = null
    private var distanceRecord: List<DistanceRecord>? = null
    private var bodyFatRecord: List<BodyFatRecord>? = null
    private var oxygenSaturationRecord: List<OxygenSaturationRecord>? = null
    private var respiratoryRateRecord: List<RespiratoryRateRecord>? = null
    private lateinit var healthConnectClient: HealthConnectClient
    private lateinit var tvBurnValue: TextView
    private lateinit var text_activity: TextView
    private lateinit var lightZoneBelow: TextView
    private lateinit var lightZoneHighl: TextView
    private lateinit var fatLossHighl: TextView
    private lateinit var cardioHighl: TextView
    private lateinit var heartRateZoneNoDataTv : TextView
    private lateinit var text_no_data_activity_factor: TextView
    private lateinit var peakHighl: TextView
    private lateinit var line_graph: LineGraphView
    private lateinit var calorieBalanceIcon: ImageView
    private lateinit var step_forward_icon: ImageView
    private lateinit var moveRightImageBack: ImageView
    private lateinit var stepLineGraphView: LineGrapghViewSteps
    private lateinit var todayStepsTv: TextView
    private lateinit var averageStepsTv: TextView
    private lateinit var steps_no_data_text: TextView
    private lateinit var stes_no_data_text_description: TextView
    private lateinit var syncWithHealthConnectButton: ConstraintLayout
    private lateinit var goalStepsTv: TextView
    private lateinit var calorieCountText: TextView
    private lateinit var totalIntakeCalorieText: TextView
    private lateinit var calorieBalanceDescription: TextView
    private lateinit var calorieBalanceMessageTitle : TextView

    private lateinit var appPreference: AppPreference
    private lateinit var transparentOverlay : View
    private lateinit var circleIndicator : View
    private lateinit var workoutImageIcon : ImageView
    private lateinit var progressBarCalorieBalance : ProgressBar
    private lateinit var belowTransparent : ImageView
    private lateinit var progressBarLayout : ConstraintLayout
    private lateinit var stepWithDataCardLayout : ConstraintLayout
    private lateinit var stepNoDataLayout : ConstraintLayout
    private lateinit var verticalLineStartLightBpmTv : TextView
    private lateinit var verticalLineFatLossBpmTv : TextView
    private lateinit var verticalLineCardioBpmTv : TextView
    private lateinit var verticalLinePeakBpmTv : TextView
    private lateinit var verticalLinePeakEndBpmTv : TextView
    private lateinit var caloricInfo : ImageView
    private lateinit var yourMovementSummary : ImageView
    private lateinit var yourVitals : ImageView
    private lateinit var yourHeartRateZone : ImageView
    private var totalIntakeCaloriesSum: Int = 0
    private var loadingOverlay : FrameLayout? = null

    private val allReadPermissions = setOf(
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(SpeedRecord::class),
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class),
        HealthPermission.getReadPermission(OxygenSaturationRecord::class),
        HealthPermission.getReadPermission(RespiratoryRateRecord::class),
        HealthPermission.getReadPermission(BodyFatRecord::class),
        HealthPermission.getReadPermission(RestingHeartRateRecord::class),
        HealthPermission.getReadPermission(BasalMetabolicRateRecord::class),
        HealthPermission.getReadPermission(HeartRateVariabilityRmssdRecord::class),
        HealthPermission.getReadPermission(BloodPressureRecord::class)
    )

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLandingBinding
        get() = FragmentLandingBinding::inflate

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        appPreference = AppPreference(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSeatName = arguments?.getString("BottomSeatName").toString()
        carouselViewPager = view.findViewById(R.id.carouselViewPager)
        calorie_no_data_filled_layout = view.findViewById(R.id.calorie_no_data_filled_layout)
        calorie_layout_data_filled = view.findViewById(R.id.calorie_layout_data_filled)
        text_no_data_activity_factor = view.findViewById(R.id.text_no_data_activity_factor)
        line_graph = view.findViewById(R.id.line_graph)
        lightZoneBelow = view.findViewById(R.id.lightZoneBelow)
        lightZoneHighl = view.findViewById(R.id.lightZoneHigh)
        fatLossHighl = view.findViewById(R.id.fatLossHigh)
        cardioHighl = view.findViewById(R.id.cardioHigh)
        peakHighl = view.findViewById(R.id.peakHigh)
        text_activity = view.findViewById(R.id.text_activity)
        nodataWorkout = view.findViewById(R.id.no_data_workout_landing)
        dataFilledworkout = view.findViewById(R.id.data_filled_workout)
        step_forward_icon = view.findViewById(R.id.step_forward_icon)
        totalIntakeCalorieText = view.findViewById(R.id.textView1)
        calorieCountText = view.findViewById(R.id.calorie_count)
        steps_no_data_text = view.findViewById(R.id.steps_no_data_text)
        stes_no_data_text_description = view.findViewById(R.id.stes_no_data_text_description)
        syncWithHealthConnectButton = view.findViewById(R.id.syncWithHealthConnectButton)
        calorieBalanceIcon = view.findViewById(R.id.calorie_balance_icon)
        dotsLayout = view.findViewById(R.id.dotsLayout)
        moveRightImageBack = view.findViewById(R.id.moveright_image_back)
        calorieBalanceDescription = view.findViewById(R.id.calorieBalanceMessage)
        calorieBalanceMessageTitle = view.findViewById(R.id.calorieBalanceMessageTitle)
        tvBurnValue = view.findViewById(R.id.textViewBurnValue)
        stepLineGraphView = view.findViewById(R.id.line_graph_steps)
        todayStepsTv = view.findViewById(R.id.todayStepsTv)
        averageStepsTv = view.findViewById(R.id.averageStepsTv)
        goalStepsTv = view.findViewById(R.id.goal_tex)
        progressBarCalorieBalance = view.findViewById(R.id.progressBar)
        circleIndicator = view.findViewById(R.id.circleIndicator)
        transparentOverlay = view.findViewById(R.id.transparentOverlay)
        belowTransparent = view.findViewById(R.id.imageViewBelowOverlay)
        progressBarLayout = view.findViewById(R.id.progressBarLayout)
        workoutImageIcon = view.findViewById(R.id.workout_forward_icon)
        stepNoDataLayout = view.findViewById(R.id.stepNoDataLayout)
        stepWithDataCardLayout = view.findViewById(R.id.stepWithDataCardLayout)
        verticalLineStartLightBpmTv = view.findViewById(R.id.vertical_line_start_light_bgmtext)
        verticalLineFatLossBpmTv = view.findViewById(R.id.vertical_line_fatloss_button_bgmtext)
        verticalLineCardioBpmTv = view.findViewById(R.id.vertical_line_cardio_button_bgmtext)
        verticalLinePeakBpmTv = view.findViewById(R.id.vertical_line_peak_button_bgmtext)
        verticalLinePeakEndBpmTv = view.findViewById(R.id.vertical_line_peak_button_end_bgmtext)
        heartRateZoneNoDataTv = view.findViewById(R.id.heartRateZoneNoDataTv)
        caloricInfo = view.findViewById(R.id.caloricInfo)
        yourMovementSummary = view.findViewById(R.id.yourMovementSummary)
        yourVitals = view.findViewById(R.id.yourVitals)
        yourHeartRateZone = view.findViewById(R.id.yourHeartRateZone)

        setupRecyclerView(view)
        fetchUserWorkouts()

        moveRightImageBack.setOnClickListener {
            activity?.finish()
        }
        step_forward_icon.setOnClickListener {
            navigateToFragment(StepFragment(), "StepTakenFragment")
        }
        averageStepsTv.setOnClickListener {
            navigateToFragment(SetYourStepGoalFragment(), "StepTakenFragment")
        }
        val activityFactorImageIcon = view.findViewById<ImageView>(R.id.activity_forward_icon)
        val logMealButton = view.findViewById<ConstraintLayout>(R.id.log_meal_button)
        val layoutAddWorkout = view.findViewById<ConstraintLayout>(R.id.lyt_snap_meal)
        val lytNoDataAddWorkoutBtn = view.findViewById<ConstraintLayout>(R.id.lytNoDataAddWorkoutBtn)
        val logMealNoDataBtn = view.findViewById<ConstraintLayout>(R.id.logMealNoDataBtn)

        calorieBalanceIcon.setOnClickListener {
            val fragment = CalorieBalance()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment, "CalorieBalance")
                .addToBackStack(null)
                .commit()
        }
        workoutImageIcon.setOnClickListener {
            // Define action if needed
            val fragment = WorkoutAnalyticsFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment, "workoutAnalysisFragment")
                .addToBackStack(null)
                .commit()
        }
        lytNoDataAddWorkoutBtn.setOnClickListener{
            navigateToFragment(YourActivityFragment(), "YourActivityFragment")
        }
        layoutAddWorkout.setOnClickListener {
            navigateToFragment(YourActivityFragment(), "YourActivityFragment")
        }
        activityFactorImageIcon.setOnClickListener {
            navigateToFragment(ActivityFactorFragment(), "ActivityFactorFragment")
        }
        logMealNoDataBtn.setOnClickListener {
            navigateToFragment(YourMealLogsFragment(), "YourMealLogs")
        }
        logMealButton.setOnClickListener {
            navigateToFragment(YourMealLogsFragment(), "YourMealLogs")
        }
        caloricInfo.setOnClickListener {
            val yourCaloricSummaryInfoBottomSheet = YourCaloricSummaryInfoBottomSheet()
            yourCaloricSummaryInfoBottomSheet.isCancelable = true
            parentFragment.let { yourCaloricSummaryInfoBottomSheet.show(childFragmentManager, "YourCaloricSummaryInfoBottomSheet") }
        }

        yourMovementSummary.setOnClickListener {
            val yourMovementSummaryInfoBottomSheet = YourMovementSummaryInfoBottomSheet()
            yourMovementSummaryInfoBottomSheet.isCancelable = true
            parentFragment.let { yourMovementSummaryInfoBottomSheet.show(childFragmentManager, "YourMovementSummaryInfoBottomSheet") }
        }

        yourVitals.setOnClickListener {
            val yourVitalsInfoBottomSheet = YourVitalsInfoBottomSheet()
            yourVitalsInfoBottomSheet.isCancelable = true
            parentFragment.let { yourVitalsInfoBottomSheet.show(childFragmentManager, "YourVitalsInfoBottomSheet") }
        }

        yourHeartRateZone.setOnClickListener {
            val yourHeartRateZonesInfoBottomSheet = YourHeartRateZonesInfoBottomSheet()
            yourHeartRateZonesInfoBottomSheet.isCancelable = true
            parentFragment.let { yourHeartRateZonesInfoBottomSheet.show(childFragmentManager, "YourHeartRateZonesInfoBottomSheet") }
        }

        val availabilityStatus = HealthConnectClient.getSdkStatus(requireContext())
        if (availabilityStatus == HealthConnectClient.SDK_AVAILABLE) {
            healthConnectClient = HealthConnectClient.getOrCreate(requireContext())
            lifecycleScope.launch {
                requestPermissionsAndReadAllData()
            }
        } else {
            Toast.makeText(context, "Please install or update health connect from the Play Store.", Toast.LENGTH_LONG).show()
        }

        progressBarCalorieBalance.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                progressBarCalorieBalance.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val progressBarWidth = progressBarCalorieBalance.width.toFloat()
                val overlayPositionPercentage = 0.6f
                val progress = progressBarCalorieBalance.progress
                val max = progressBarCalorieBalance.max
                val progressPercentage = progress.toFloat() / max
                val constraintSet = ConstraintSet()
                constraintSet.clone(progressBarLayout)
                constraintSet.setGuidelinePercent(R.id.circleIndicatorGuideline, progressPercentage)
                constraintSet.setGuidelinePercent(R.id.overlayGuideline, overlayPositionPercentage)
                constraintSet.applyTo(progressBarLayout)
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
        loadStepData()
      //  storeHealthData()

        syncWithHealthConnectButton.setOnClickListener {
            storeHealthData()
        }
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val adapter = GridAdapter(emptyList()) { itemName ->
            when (itemName) {
                "RHR" -> navigateToFragment(RestingHeartRateFragment(), "RestingHeartRateFragment")
                "Avg HR" -> navigateToFragment(AverageHeartRateFragment(), "AverageHeartRateFragment")
                "HRV" -> navigateToFragment(HeartRateVariabilityFragment(), "HeartRateVariabilityFragment")
                "Burn" -> navigateToFragment(BurnFragment(), "BurnFragment")
            }
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter
        fetchMoveLanding(recyclerView, adapter)
    }

    private fun fetchMoveLanding(recyclerView: RecyclerView, adapter: GridAdapter) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (isAdded  && view != null){
                    requireActivity().runOnUiThread {
                        showLoader(requireView())
                    }
                }
                val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val selectedDate = currentDateTime.format(formatter)
                val response = ApiClient.apiServiceFastApi.getMoveLanding(
                    userId = userId,
                    date = selectedDate
                )
                if (response.isSuccessful) {
                    if (isAdded  && view != null){
                        requireActivity().runOnUiThread {
                            dismissLoader(requireView())
                        }
                    }
                    val fitnessData = response.body()
                    fitnessData?.let {
                        val rhrData = padData(it.data.restingHeartRate.last7Days.map { day -> day.bpm }, 7)
                        val avgHrData = padData(it.data.averageHeartRate.last7Days.map { day -> day.heartRate }, 7)
                        val hrvData = padData(it.data.heartRateVariability.last7Days.map { day -> day.hrv }, 7)
                        val burnData = padData(it.data.caloriesBurned.last7Days.map { day -> day.caloriesBurned }, 7)
                        val activityFactorData = padData(it.data.activityFactor.last7Days.map { day -> day.activityFactor }, 7)
                        val todayStepCount = it.data.steps.todayTotal
                        val averageStepCount = it.data.steps.averageSteps
                        val goalStepCount = it.data.steps.goalSteps
                        val todayStepsData = it.data.steps.todayCumulativeSteps?.takeIf { it.isNotEmpty() }?.let { data ->
                            data.map { it.cumulativeSteps.toFloat() }.toFloatArray()
                        } ?: FloatArray(24) { 0f }.also {
                            //errorMessages.add("Today Steps")
                        }
                        val averageStepsData = it.data.steps.averageCumulativeSteps?.takeIf { it.isNotEmpty() }?.let { data ->
                            data.map { it.cumulativeSteps.toFloat() }.toFloatArray()
                        } ?: FloatArray(24) { 0f }.also {
                          //  errorMessages.add("Average Steps")
                        }
                        val goalStepsData = it.data.steps.goalSteps.let { goal ->
                            FloatArray(24) { goal.toFloat() }
                        } ?: FloatArray(24) { 0f }.also {
                            //errorMessages.add("Goal Steps")
                        }
                        val items = listOf(
                            GridItem(
                                name = "RHR",
                                imageRes = R.drawable.rhr_icon,
                                additionalInfo = it.data.restingHeartRate.unit,
                                fourthParameter = it.data.restingHeartRate.today.toInt().toString(),
                                dataPoints = rhrData
                            ),
                            GridItem(
                                name = "Avg HR",
                                imageRes = R.drawable.rhr_icon,
                                additionalInfo = it.data.averageHeartRate.unit,
                                fourthParameter = it.data.averageHeartRate.today.toInt().toString(),
                                dataPoints = avgHrData
                            ),
                            GridItem(
                                name = "HRV",
                                imageRes = R.drawable.hrv_icon,
                                additionalInfo = it.data.heartRateVariability.unit,
                                fourthParameter = it.data.heartRateVariability.today.toInt().toString(),
                                dataPoints = hrvData
                            ),
                            GridItem(
                                name = "Burn",
                                imageRes = R.drawable.burn_icon,
                                additionalInfo = it.data.caloriesBurned.unit,
                                fourthParameter = it.data.caloriesBurned.today.toInt().toString(),
                                dataPoints = burnData
                            )
                        )
                        // Heart rate zone checks and UI updates
                        withContext(Dispatchers.Main) {
                            text_activity.text = it.data.activityFactor.today.toString() ?: "0"
                            val allInvalid = (/*it.data.calorieBalance.calorieBurnTarget == null || it.data.calorieBalance.calorieBurnTarget == 0f) &&
                                    (it.data.calorieBalance.difference == null || it.data.calorieBalance.difference == 0f) &&*/
                                    it.data.calorieBalance.calorieIntake == null || it.data.calorieBalance.calorieIntake == 0.0)
                            // Always set layout to VISIBLE
                            calorie_no_data_filled_layout.visibility = View.VISIBLE

                            if (allInvalid) {
                                // No data state
                                calorie_no_data_filled_layout.visibility = View.VISIBLE
                                calorie_layout_data_filled.visibility = View.GONE
                            } else {
                                // Data state
                                calorie_no_data_filled_layout.visibility = View.GONE
                                calorie_layout_data_filled.visibility = View.VISIBLE
                                tvBurnValue.text = if (it.data.calorieBalance.calorieBurnTarget == null || it.data.calorieBalance.calorieBurnTarget == 0.0) "0" else it.data.calorieBalance.calorieBurnTarget.toInt().toString()
                                calorieCountText.text = if (it.data.calorieBalance.difference == null || it.data.calorieBalance.difference == 0.0) "0" else it.data.calorieBalance.difference.toInt().toString()
                                totalIntakeCalorieText.text = if (it.data.calorieBalance.calorieIntake == null || it.data.calorieBalance.calorieIntake == 0.0) "0" else it.data.calorieBalance.calorieIntake.toInt().toString()
                                calorieBalanceMessageTitle.text = it.data.calorieBalance.heading
                                calorieBalanceDescription.text = it.data.calorieBalance.message
                                progressBarCalorieBalance.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                                    override fun onGlobalLayout() {
                                        progressBarCalorieBalance.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                        val progressBarWidth = progressBarCalorieBalance.width.toFloat()
                                        val percentage = (( it.data.calorieBalance.calorieBurnTarget - it.data.calorieBalance.calorieRange.get(0)) / (it.data.calorieBalance.calorieRange.get(1) - it.data.calorieBalance.calorieRange.get(0))).toFloat()
                                      //  val percentage = (it.data.calorieBalance.calorieRange.get(0) / it.data.calorieBalance.calorieBurnTarget) * 100
                                        val value = (percentage / 10)
                                        val overlayPositionPercentage : Float = String.format("%.1f", value).toFloat()
                                        progressBarCalorieBalance.progress = it.data.calorieBalance.calorieIntake.toInt()
                                        val progress = progressBarCalorieBalance.progress
                                        progressBarCalorieBalance.max = it.data.calorieBalance.calorieBurnTarget.toInt()
                                        val max = progressBarCalorieBalance.max
                                        val progressPercentage = progress.toFloat() / max
                                        val constraintSet = ConstraintSet()
                                        constraintSet.clone(progressBarLayout)
                                        constraintSet.setGuidelinePercent(R.id.circleIndicatorGuideline, progressPercentage)
                                        constraintSet.setGuidelinePercent(R.id.overlayGuideline, overlayPositionPercentage)
                                        constraintSet.applyTo(progressBarLayout)
                                    }
                                })
                            }
                            val heartRateZones = it.data.heartRateZones
                            val errorMessages = mutableListOf<String>()
                            if (activityFactorData.isEmpty() || activityFactorData.all { it == 0f }) {
                                text_no_data_activity_factor.visibility = View.VISIBLE
                                line_graph.visibility = View.GONE
                                //line_graph.setDataPoints(emptyList())
                            } else {
                                text_no_data_activity_factor.visibility = View.GONE
                                line_graph.visibility = View.VISIBLE
                                line_graph.setDataPoints(activityFactorData)
                            }
                           // line_graph.setDataPoints(activityFactorData)
                            withContext(Dispatchers.Main) {
                                if (it.data.steps.goalSteps > 0){
                                    stepNoDataLayout.visibility = View.GONE
                                    stepWithDataCardLayout.visibility = View.VISIBLE
                                    stepLineGraphView.clear()
                                    stepLineGraphView.addDataSet(todayStepsData, 0xFFFD6967.toInt()) // Red
                                    stepLineGraphView.addDataSet(averageStepsData, 0xFF707070.toInt()) // Gray
                                    stepLineGraphView.addDataSet(goalStepsData, 0xFF03B27B.toInt()) // Green (dotted)
                                    stepLineGraphView.invalidate()
                                    todayStepsTv.text = todayStepCount.toString()
                                    averageStepsTv.text = averageStepCount.toString()
                                    goalStepsTv.text = goalStepCount.toString()
                                }else{
                                    stepNoDataLayout.visibility = View.VISIBLE
                                    stepWithDataCardLayout.visibility = View.GONE
                                }
                            }
                            if (heartRateZones != null) {
                                // Check Light Zone
                                heartRateZoneNoDataTv.visibility = View.GONE
                                lightZoneBelow.visibility = View.VISIBLE
                                lightZoneHighl.visibility = View.VISIBLE
                                fatLossHighl.visibility = View.VISIBLE
                                cardioHighl.visibility = View.VISIBLE
                                peakHighl.visibility = View.VISIBLE
                                verticalLineStartLightBpmTv.visibility = View.VISIBLE
                                verticalLineFatLossBpmTv.visibility = View.VISIBLE
                                verticalLineCardioBpmTv.visibility = View.VISIBLE
                                verticalLinePeakBpmTv.visibility = View.VISIBLE
                                verticalLinePeakEndBpmTv.visibility = View.VISIBLE
                                if (heartRateZones.heartRateZones.lightZone?.size?.let { it >= 2 } == true) {
                                    lightZoneBelow.text = heartRateZones.heartRateZones.lightZone[0].toString()
                                    lightZoneHighl.text = heartRateZones.heartRateZones.lightZone[1].toString()
                                } else {
                                    lightZoneBelow.text = "N/A"
                                    lightZoneHighl.text = "N/A"
                                    errorMessages.add("Light Zone")
                                }

                                // Check Fat Burn Zone
                                if (heartRateZones.heartRateZones.fatBurnZone?.size?.let { it >= 2 } == true) {
                                    fatLossHighl.text = heartRateZones.heartRateZones.fatBurnZone[1].toString()
                                } else {
                                    fatLossHighl.text = "N/A"
                                    errorMessages.add("Fat Burn Zone")
                                }

                                // Check Cardio Zone
                                if (heartRateZones.heartRateZones.cardioZone?.size?.let { it >= 2 } == true) {
                                    cardioHighl.text = heartRateZones.heartRateZones.cardioZone[1].toString()
                                } else {
                                    cardioHighl.text = "N/A"
                                    errorMessages.add("Cardio Zone")
                                }

                                // Check Peak Zone
                                if (heartRateZones.heartRateZones.peakZone?.size?.let { it >= 2 } == true) {
                                    peakHighl.text = heartRateZones.heartRateZones.peakZone[1].toString()
                                } else {
                                    peakHighl.text = "N/A"
                                    errorMessages.add("Peak Zone")
                                }

                                // Show a single Toast for all errors
                                if (errorMessages.isNotEmpty()) {
                                    val message = "Incomplete data for: ${errorMessages.joinToString(", ")}"
                                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                if (isAdded  && view != null){
                                    requireActivity().runOnUiThread {
                                        dismissLoader(requireView())
                                    }
                                }
                                heartRateZoneNoDataTv.visibility = View.VISIBLE
                                lightZoneBelow.visibility = View.GONE
                                lightZoneHighl.visibility = View.GONE
                                fatLossHighl.visibility = View.GONE
                                cardioHighl.visibility = View.GONE
                                peakHighl.visibility = View.GONE
                                verticalLineStartLightBpmTv.visibility = View.GONE
                                verticalLineFatLossBpmTv.visibility = View.GONE
                                verticalLineCardioBpmTv.visibility = View.GONE
                                verticalLinePeakBpmTv.visibility = View.GONE
                                verticalLinePeakEndBpmTv.visibility = View.GONE
                                lightZoneBelow.text = "N/A"
                                lightZoneHighl.text = "N/A"
                                fatLossHighl.text = "N/A"
                                cardioHighl.text = "N/A"
                                peakHighl.text = "N/A"
                                Toast.makeText(requireContext(), "Heart Rate Zones data missing", Toast.LENGTH_SHORT).show()
                            }
                            // Update RecyclerView
                            adapter.updateItems(items)
                            recyclerView.adapter = adapter
                        }
                    } ?: withContext(Dispatchers.Main) {
                        if (isAdded  && view != null){
                            requireActivity().runOnUiThread {
                                dismissLoader(requireView())
                            }
                        }
                        Toast.makeText(requireContext(), "No data received from API", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        if (isAdded  && view != null){
                            requireActivity().runOnUiThread {
                                dismissLoader(requireView())
                            }
                        }
                        Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    if (isAdded  && view != null){
                        requireActivity().runOnUiThread {
                            dismissLoader(requireView())
                        }
                    }
                    Toast.makeText(requireContext(), "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun padData(data: List<Float>, targetSize: Int): FloatArray {
        val result = FloatArray(targetSize) { 0f }
        data.forEachIndexed { index, value ->
            if (index < targetSize) result[index] = value
        }
        return result
    }

    private fun addDotsIndicator(count: Int) {
        dots = arrayOfNulls(count)
        dotsLayout.removeAllViews()
        for (i in 0 until count) {
            dots[i] = ImageView(requireContext()).apply {
                setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.dot_unselected))
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(8, 0, 8, 0) }
                layoutParams = params
            }
            dotsLayout.addView(dots[i])
        }
        updateDots(0)
    }

    private fun updateDots(position: Int) {
        dots.forEachIndexed { index, imageView ->
            imageView?.setImageResource(if (index == position) R.drawable.dot_selected else R.drawable.dot_unselected)
        }
    }

    private fun navigateToFragment(fragment: Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

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

    fun convertUtcToInstant(utcString: String): Instant {
        return Instant.from(DateTimeFormatter.ISO_INSTANT.parse(utcString))
    }

    private suspend fun fetchAllHealthData() {
        lifecycleScope.launch {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = SleepSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.after(Instant.EPOCH)
                )
            )
            for (record in response.records) {
                val deviceInfo = record.metadata.device
                if (deviceInfo != null) {
                    SharedPreferenceManager.getInstance(requireContext()).saveDeviceName(deviceInfo.manufacturer)
                    Log.d("Device Info", """ Manufacturer: ${deviceInfo.manufacturer}
                Model: ${deviceInfo.model} Type: ${deviceInfo.type} """.trimIndent())
                } else {
                    Log.d("Device Info", "No device info available")
                }
            }
        }
        var endTime = Instant.now()
        var startTime = Instant.now()
        val syncTime = SharedPreferenceManager.getInstance(requireContext()).moveRightSyncTime ?: ""
        if (syncTime == "") {
            endTime = Instant.now()
             startTime = endTime.minus(Duration.ofDays(31))
        }else{
            endTime = Instant.now()
            startTime = convertUtcToInstant(syncTime).plus(Duration.ofMinutes(1))
        }
        try {
            val grantedPermissions = healthConnectClient.permissionController.getGrantedPermissions()
            if (HealthPermission.getReadPermission(StepsRecord::class) in grantedPermissions) {
                val endT = Instant.now()
                val startT = endT.minusSeconds(7 * 24 * 60 * 60)
                val stepsResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = StepsRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endT)
                    )
                )
                stepsRecord = stepsResponse.records
                if (stepsRecord?.isEmpty() == true) {
                    android.util.Log.d("HealthData", "No steps data found")
                } else {
//                    val totalSteps = stepsRecord?.sumOf { it.count } ?: 0
//                    withContext(Dispatchers.Main) {
//                        stepsTv.text = totalSteps.toString()
//                    }
//                    android.util.Log.d("HealthData", "Steps: $totalSteps")
                    val dailySteps = FloatArray(7) { index ->
                        val dayStart = endT.minusSeconds(((6 - index) * 24 * 60 * 60).toLong())
                        val dayEnd = dayStart.plusSeconds(24 * 60 * 60)
                        stepsRecord?.filter { it.startTime.isAfter(dayStart) && it.endTime.isBefore(dayEnd) }
                            ?.sumOf { it.count }?.toFloat() ?: 0f
                    }
                    val totalAverageSteps = dailySteps.average().toFloat()
                    val totalGoalSteps = 700f * 7
//                    withContext(Dispatchers.Main) {
//                        activeStepsTv.text = totalAverageSteps.toInt().toString()
//                        goalStepsTv.text = totalGoalSteps.toInt().toString()
//                    }
                    val averageSteps = FloatArray(7) { dailySteps.average().toFloat() }
                    val goalSteps = FloatArray(7) { 3500f }
//                    withContext(Dispatchers.Main) {
//                        stepLineGraphView.clear()
//                        stepLineGraphView.addDataSet(dailySteps, 0xFFFD6967.toInt())
//                        stepLineGraphView.addDataSet(averageSteps, 0xFF707070.toInt())
//                        stepLineGraphView.addDataSet(goalSteps, 0xFF03B27B.toInt())
//                        stepLineGraphView.invalidate()
//                    }
                }
            } else {
                stepsRecord = emptyList()
                android.util.Log.d("HealthData", "Steps permission denied")
            }
            if (HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class) in grantedPermissions) {
                val caloriesResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = TotalCaloriesBurnedRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                totalCaloriesBurnedRecord = caloriesResponse.records
                val totalBurnedCalories = totalCaloriesBurnedRecord?.sumOf { it.energy.inKilocalories.toInt() } ?: 0
                android.util.Log.d("HealthData", "Total Burned Calories: $totalBurnedCalories kcal")
//                withContext(Dispatchers.Main) {
//                    tvBurnValue.text = totalBurnedCalories.toString()
//                    totalIntakeCalorieText.text = totalIntakeCaloriesSum.toString()
//                    val calorieBalance = totalIntakeCaloriesSum - totalBurnedCalories
//                    val absoluteCalorieBalance = abs(calorieBalance)
//                    calorieCountText.text = if (calorieBalance >= 0) "+$absoluteCalorieBalance" else "$absoluteCalorieBalance"
//                }
            } else {
                totalCaloriesBurnedRecord = emptyList()
                android.util.Log.d("HealthData", "Calories permission denied")
            }
            if (HealthPermission.getReadPermission(HeartRateRecord::class) in grantedPermissions) {
                val response = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = HeartRateRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                heartRateRecord = response.records
            }else {
                heartRateRecord = emptyList()
                android.util.Log.d("HealthData", "Heart rate permission denied")
            }
            if (HealthPermission.getReadPermission(RestingHeartRateRecord::class) in grantedPermissions) {
                val restingHRResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = RestingHeartRateRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
               restingHeartRecord = restingHRResponse.records
                restingHeartRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Resting Heart Rate: ${record.beatsPerMinute} bpm, Time: ${record.time}")
                }
            }else {
                restingHeartRecord = emptyList()
                android.util.Log.d("HealthData", "Heart rate permission denied")
            }
            if (HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class) in grantedPermissions) {
                val activeCalorieResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = ActiveCaloriesBurnedRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                activeCalorieBurnedRecord = activeCalorieResponse.records
                activeCalorieBurnedRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Resting Heart Rate: ${record.energy} kCal, Time: ${record.startTime}")
                }
            }else {
                activeCalorieBurnedRecord = emptyList()
                android.util.Log.d("HealthData", "Heart rate permission denied")
            }
            if (HealthPermission.getReadPermission(BasalMetabolicRateRecord::class) in grantedPermissions) {
                val basalMetabolic = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = BasalMetabolicRateRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                basalMetabolicRateRecord = basalMetabolic.records
                basalMetabolicRateRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Resting Heart Rate: ${record.basalMetabolicRate}, Time: ${record.time}")
                }
            }else {
                basalMetabolicRateRecord = emptyList()
                android.util.Log.d("HealthData", "Heart rate permission denied")
            }
            if (HealthPermission.getReadPermission(BloodPressureRecord::class) in grantedPermissions) {
                val bloodPressure = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = BloodPressureRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                bloodPressureRecord = bloodPressure.records
                bloodPressureRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Resting Heart Rate: ${record.systolic}, Time: ${record.time}")
                }
            }else {
                bloodPressureRecord = emptyList()
                android.util.Log.d("HealthData", "Heart rate permission denied")
            }
            if (HealthPermission.getReadPermission(HeartRateVariabilityRmssdRecord::class) in grantedPermissions) {
                val restingVresponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = HeartRateVariabilityRmssdRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                heartRateVariability = restingVresponse.records
                heartRateVariability?.forEach { record ->
                    android.util.Log.d("HealthData", "Resting Heart Rate: ${record.heartRateVariabilityMillis}, Time: ${record.time}")
                }
            }else {
                heartRateVariability = emptyList()
                android.util.Log.d("HealthData", "Heart rate permission denied")
            }
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
            if (HealthPermission.getReadPermission(ExerciseSessionRecord::class) in grantedPermissions) {
                val exerciseResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = ExerciseSessionRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                exerciseSessionRecord = exerciseResponse.records
                exerciseSessionRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Exercise Session: Type: ${record.exerciseType}, Start: ${record.startTime}, End: ${record.endTime}")
                }
            } else {
                exerciseSessionRecord = emptyList()
                android.util.Log.d("HealthData", "Exercise session permission denied")
            }
            if (HealthPermission.getReadPermission(SpeedRecord::class) in grantedPermissions) {
                val speedResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = SpeedRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                speedRecord = speedResponse.records
                speedRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Speed: ${record.samples.joinToString { it.speed.inMetersPerSecond.toString() }} m/s")
                }
            } else {
                speedRecord = emptyList()
                android.util.Log.d("HealthData", "Speed permission denied")
            }
            if (HealthPermission.getReadPermission(WeightRecord::class) in grantedPermissions) {
                val weightResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = WeightRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                weightRecord = weightResponse.records
                weightRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Weight: ${record.weight.inKilograms} kg, Time: ${record.time}")
                }
            } else {
                weightRecord = emptyList()
                android.util.Log.d("HealthData", "Weight permission denied")
            }
            if (HealthPermission.getReadPermission(BodyFatRecord::class) in grantedPermissions) {
                val bodyFatResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = BodyFatRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                bodyFatRecord = bodyFatResponse.records
                bodyFatRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Body Fat: ${record.percentage.value * 100}%, Time: ${record.time}")
                }
            } else {
                bodyFatRecord = emptyList()
                android.util.Log.d("HealthData", "Weight permission denied")
            }
            if (HealthPermission.getReadPermission(DistanceRecord::class) in grantedPermissions) {
                val distanceResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = DistanceRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                distanceRecord = distanceResponse.records
                val totalDistance = distanceRecord?.sumOf { it.distance.inMeters } ?: 0.0
                android.util.Log.d("HealthData", "Total Distance: $totalDistance meters")
            } else {
                distanceRecord = emptyList()
                android.util.Log.d("HealthData", "Distance permission denied")
            }
            if (HealthPermission.getReadPermission(OxygenSaturationRecord::class) in grantedPermissions) {
                val oxygenSaturationResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = OxygenSaturationRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                oxygenSaturationRecord = oxygenSaturationResponse.records
                oxygenSaturationRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Oxygen Saturation: ${record.percentage.value}%, Time: ${record.time}")
                }
            } else {
                oxygenSaturationRecord = emptyList()
                android.util.Log.d("HealthData", "Oxygen saturation permission denied")
            }
            if (HealthPermission.getReadPermission(RespiratoryRateRecord::class) in grantedPermissions) {
                val respiratoryRateResponse = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = RespiratoryRateRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                    )
                )
                respiratoryRateRecord = respiratoryRateResponse.records
                respiratoryRateRecord?.forEach { record ->
                    android.util.Log.d("HealthData", "Respiratory Rate: ${record.rate} breaths/min, Time: ${record.time}")
                }
            } else {
                respiratoryRateRecord = emptyList()
                android.util.Log.d("HealthData", "Respiratory rate permission denied")
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

    private fun fetchUserWorkouts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userid = SharedPreferenceManager.getInstance(requireActivity()).userId
                val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                val response = ApiClient.apiServiceFastApi.getNewUserWorkouts(
                    userId = userid,
                    start_date = currentDate,
                    end_date = currentDate,
                    page = 1,
                    limit = 10
                )
                if (response.isSuccessful) {
                    val workouts = response.body()
                    workouts?.let {
                        val hasHeartRateData = it.syncedWorkouts.any { workout -> workout.heartRateData.isNotEmpty() }
                        if (hasHeartRateData) {
                            val totalSyncedCalories = it.syncedWorkouts.sumOf { workout -> workout.caloriesBurned.toDoubleOrNull() ?: 0.0 }
                            val cardItems = it.syncedWorkouts.map { workout ->
                                val durationMinutes = workout.duration.toIntOrNull() ?: 0
                                val hours = durationMinutes / 60
                                val minutes = durationMinutes % 60
                                val durationText = if (hours > 0) "$hours hr ${minutes.toString().padStart(2, '0')} mins" else "$minutes mins"
                                val caloriesText = "${workout.caloriesBurned} cal"
                                val avgHeartRate = if (workout.heartRateData.isNotEmpty()) {
                                    val totalHeartRate = workout.heartRateData.sumOf { it.heartRate }
                                    val count = workout.heartRateData.size
                                    "${(totalHeartRate / count).toInt()} bpm"
                                } else "N/A"
                                workout.heartRateData.forEach { heartRateData ->
                                    heartRateData.trendData.addAll(listOf(listOf(110, 112, 115, 118, 120, 122, 125).toString()))
                                }
                                CardItem(
                                    title = workout.workoutType,
                                    duration = durationText,
                                    caloriesBurned = caloriesText,
                                    avgHeartRate = avgHeartRate,
                                    heartRateData = workout.heartRateData,
                                    heartRateZones = workout.heartRateZones,
                                    heartRateZoneMinutes = workout.heartRateZoneMinutes,
                                    heartRateZonePercentages = workout.heartRateZonePercentages
                                )
                            }
//                            val cardItemUnSync = it.unsyncedWorkouts.map { workout ->
//                                val durationMinutes = workout.duration
//                                val hours = durationMinutes / 60
//                                val minutes = durationMinutes % 60
//                                val durationText = if (hours > 0) "$hours hr ${minutes.toString().padStart(2, '0')} mins" else "$minutes mins"
//                                val caloriesText = "${workout.calories_burned.toInt()} cal"
////                                val avgHeartRate = if (workout.heartRateData.isNotEmpty()) {
////                                    val totalHeartRate = workout.heartRateData.sumOf { it.heartRate }
////                                    val count = workout.heartRateData.size
////                                    "${(totalHeartRate / count).toInt()} bpm"
////                                } else "N/A"
////                                workout.heartRateData.forEach { heartRateData ->
////                                    heartRateData.trendData.addAll(listOf(listOf(110, 112, 115, 118, 120, 122, 125).toString()))
////                                }
//                                val heartRateZones: HeartRateZones? = null
//                                val heartRateZoneMinutes: HeartRateZoneMinutes? = null
//                                val heartRateZonePercentages: HeartRateZonePercentages? = null
//                                CardItem(
//                                    title = workout.workout_type,
//                                    duration = durationText,
//                                    caloriesBurned = caloriesText,
//                                    avgHeartRate = "",
//                                    heartRateData = emptyList(),
//                                    heartRateZones = heartRateZones!!,
//                                    heartRateZoneMinutes = heartRateZoneMinutes!!,
//                                    heartRateZonePercentages = heartRateZonePercentages!!
//                                )
//                            }
                            withContext(Dispatchers.Main) {
                                workoutImageIcon.visibility = View.VISIBLE
                                dataFilledworkout.visibility = View.VISIBLE
                                nodataWorkout.visibility = View.GONE
                                val adapter = CarouselAdapter(cardItems) { cardItem, position ->
                                    val fragment = WorkoutAnalyticsFragment().apply {
                                        arguments = Bundle().apply { putSerializable("cardItem", cardItem) }
                                    }
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.flFragment, fragment, "workoutAnalysisFragment")
                                        .addToBackStack(null)
                                        .commit()
                                }
                                carouselViewPager.adapter = adapter
                                addDotsIndicator(cardItems.size)
                                carouselViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                                    override fun onPageSelected(position: Int) {
                                        updateDots(position)
                                    }
                                })
                                carouselViewPager.setPageTransformer { page, position ->
                                    val offset = abs(position)
                                    page.scaleY = 1 - (offset * 0.1f)
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                nodataWorkout.visibility = View.VISIBLE
                                dataFilledworkout.visibility = View.GONE
                              //  Toast.makeText(requireContext(), "No heart rate data available", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } ?: withContext(Dispatchers.Main) {
                        nodataWorkout.visibility = View.VISIBLE
                        dataFilledworkout.visibility = View.GONE
                      //  Toast.makeText(requireContext(), "No workout data received", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        nodataWorkout.visibility = View.VISIBLE
                        dataFilledworkout.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error: ${response.code()} - ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    nodataWorkout.visibility = View.VISIBLE
                    dataFilledworkout.visibility = View.GONE
                    Toast.makeText(requireContext(), "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun storeHealthData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userid = SharedPreferenceManager.getInstance(requireActivity()).userId
                val activeEnergyBurned = activeCalorieBurnedRecord?.mapNotNull { record ->
                    if (record.energy.inKilocalories > 0) {
                        EnergyBurnedRequest(
                            start_datetime = convertToTargetFormat(record.startTime.toString()),
                            end_datetime = convertToTargetFormat(record.endTime.toString()),
                            record_type = "ActiveEnergyBurned",
                            unit = "kcal",
                            value = record.energy.inKilocalories.toString(),
                            source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                        )
                    } else null
                } ?: emptyList()
                val basalEnergyBurned = basalMetabolicRateRecord?.map { record ->
                    EnergyBurnedRequest(
                        start_datetime = convertToTargetFormat(record.time.toString()),
                        end_datetime = convertToTargetFormat(record.time.toString()),
                        record_type = "BasalMetabolic",
                        unit = "power",
                        value = record.basalMetabolicRate.toString(),
                        source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                    )
                } ?: emptyList()
                val distanceWalkingRunning = distanceRecord?.mapNotNull { record ->
                    if (record.distance.inKilometers > 0) {
                        Distance(
                            start_datetime = convertToTargetFormat(record.startTime.toString()),
                            end_datetime = convertToTargetFormat(record.endTime.toString()),
                            record_type = "DistanceWalkingRunning",
                            unit = "km",
                            value = String.format("%.2f", record.distance.inKilometers),
                            source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                        )
                    } else null
                } ?: emptyList()
                val stepCount = stepsRecord?.mapNotNull { record ->
                    if (record.count > 0) {
                        StepCountRequest(
                            start_datetime = convertToTargetFormat(record.startTime.toString()),
                            end_datetime = convertToTargetFormat(record.endTime.toString()),
                            record_type = "StepCount",
                            unit = "count",
                            value = record.count.toString(),
                            source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                        )
                    } else null
                } ?: emptyList()
                val heartRate = heartRateRecord?.flatMap { record ->
                    record.samples.mapNotNull { sample ->
                        if (sample.beatsPerMinute > 0) {
                            HeartRateRequest(
                                start_datetime = convertToTargetFormat(record.startTime.toString()),
                                end_datetime = convertToTargetFormat(record.endTime.toString()),
                                record_type = "HeartRate",
                                unit = "bpm",
                                value = sample.beatsPerMinute.toInt().toString(),
                                source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                            )
                        } else null
                    }
                } ?: emptyList()
                val heartRateVariability = heartRateVariability?.map { record ->
                    HeartRateVariabilityRequest(
                                start_datetime = convertToTargetFormat(record.time.toString()),
                                end_datetime = convertToTargetFormat(record.time.toString()),
                                record_type = "HeartRateVariability",
                                unit = "double",
                                value = record.heartRateVariabilityMillis.toString(),
                                source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                            )
                } ?: emptyList()
                val restingHeartRate = restingHeartRecord?.map { record ->
                            HeartRateRequest(
                                start_datetime = convertToTargetFormat(record.time.toString()),
                                end_datetime = convertToTargetFormat(record.time.toString()),
                                record_type = "RestingHeartRate",
                                unit = "bpm",
                                value = record.beatsPerMinute.toString(),
                                source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                            )
                } ?: emptyList()
                val respiratoryRate = respiratoryRateRecord?.mapNotNull { record ->
                    if (record.rate > 0) {
                        RespiratoryRate(
                            start_datetime = convertToTargetFormat(record.time.toString()),
                            end_datetime = convertToTargetFormat(record.time.toString()),
                            record_type = "RespiratoryRate",
                            unit = "breaths/min",
                            value = String.format("%.1f", record.rate),
                            source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                        )
                    } else null
                } ?: emptyList()
                val oxygenSaturation = oxygenSaturationRecord?.mapNotNull { record ->
                    if (record.percentage.value > 0) {
                        OxygenSaturation(
                            start_datetime = convertToTargetFormat(record.time.toString()),
                            end_datetime = convertToTargetFormat(record.time.toString()),
                            record_type = "OxygenSaturation",
                            unit = "%",
                            value = String.format("%.1f", record.percentage.value),
                            source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                        )
                    } else null
                } ?: emptyList()
                val bloodPressureSystolic = bloodPressureRecord?.mapNotNull { record ->
                        BloodPressure(
                            start_datetime = convertToTargetFormat(record.time.toString()),
                            end_datetime = convertToTargetFormat(record.time.toString()),
                            record_type = "BloodPressureSystolic",
                            unit = "millimeterOfMercury",
                            value = record.systolic.inMillimetersOfMercury.toString(),
                            source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                        )
                } ?: emptyList()
                val bloodPressureDiastolic = bloodPressureRecord?.mapNotNull { record ->
                    BloodPressure(
                        start_datetime = convertToTargetFormat(record.time.toString()),
                        end_datetime = convertToTargetFormat(record.time.toString()),
                        record_type = "BloodPressureDiastolic",
                        unit = "millimeterOfMercury",
                        value = record.diastolic.inMillimetersOfMercury.toString(),
                        source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                    )
                } ?: emptyList()
                val bodyMass = weightRecord?.mapNotNull { record ->
                    if (record.weight.inKilograms > 0) {
                        BodyMass(
                            start_datetime = convertToTargetFormat(record.time.toString()),
                            end_datetime = convertToTargetFormat(record.time.toString()),
                            record_type = "BodyMass",
                            unit = "kg",
                            value = String.format("%.1f", record.weight.inKilograms),
                            source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                        )
                    } else null
                } ?: emptyList()
                val bodyFatPercentage = bodyFatRecord?.mapNotNull { record ->
                        BodyFatPercentage(
                            start_datetime = convertToTargetFormat(record.time.toString()),
                            end_datetime = convertToTargetFormat(record.time.toString()),
                            record_type = "BodyFat",
                            unit = "percentage",
                            value = String.format("%.1f", record.percentage),
                            source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                        )
                } ?: emptyList()
                val sleepStage = sleepSessionRecord?.flatMap { record ->
                    record.stages.mapNotNull { stage ->
                        val stageValue = when (stage.stage) {
                            SleepSessionRecord.STAGE_TYPE_DEEP -> "Deep Sleep"
                            SleepSessionRecord.STAGE_TYPE_LIGHT -> "Light Sleep"
                            SleepSessionRecord.STAGE_TYPE_REM -> "REM Sleep"
                            SleepSessionRecord.STAGE_TYPE_AWAKE -> "Awake"
                            else -> null
                        }
                        stageValue?.let {
                            SleepStageJson(
                                start_datetime = convertToTargetFormat(stage.startTime.toString()),
                                end_datetime = convertToTargetFormat(stage.endTime.toString()),
                                record_type = it,
                                unit = "sleep_stage",
                                value = it,
                                source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung"
                            )
                        }
                    }
                } ?: emptyList()
                val workout = exerciseSessionRecord?.mapNotNull { record ->
                    val workoutType = when (record.exerciseType) {
                        ExerciseSessionRecord.EXERCISE_TYPE_RUNNING -> "Running"
                        ExerciseSessionRecord.EXERCISE_TYPE_WALKING -> "Walking"
                        else -> "Other"
                    }
                    val calories = record.metadata.dataOrigin?.let { 300 } ?: 0
                    val distance = record.metadata.dataOrigin?.let { 5.0 } ?: 0.0
                    if (calories > 0) {
                        WorkoutRequest(
                            start_datetime = convertToTargetFormat(record.startTime.toString()),
                            end_datetime = convertToTargetFormat(record.endTime.toString()),
                            source_name = SharedPreferenceManager.getInstance(requireActivity()).deviceName ?: "samsung",
                            record_type = "Workout",
                            workout_type = workoutType,
                            duration = ((record.endTime.toEpochMilli() - record.startTime.toEpochMilli()) / 1000 / 60).toString(),
                            calories_burned = calories.toString(),
                            distance = String.format("%.1f", distance),
                            duration_unit = "minutes",
                            calories_unit = "kcal",
                            distance_unit = "km"
                        )
                    } else null
                } ?: emptyList()
                val request = StoreHealthDataRequest(
                    user_id = userid,
                    source = "android",
                    active_energy_burned = activeEnergyBurned,
                    basal_energy_burned = basalEnergyBurned,
                    distance_walking_running = distanceWalkingRunning,
                    step_count = stepCount,
                    heart_rate = heartRate,
                    heart_rate_variability_SDNN = heartRateVariability,
                    resting_heart_rate = restingHeartRate,
                    respiratory_rate = respiratoryRate,
                    oxygen_saturation = oxygenSaturation,
                    blood_pressure_systolic = bloodPressureSystolic,
                    blood_pressure_diastolic = bloodPressureDiastolic,
                    body_mass = bodyMass,
                    body_fat_percentage = bodyFatPercentage,
                    sleep_stage = sleepStage,
                    workout = workout
                )
                val response = ApiClient.apiServiceFastApi.storeHealthData(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val todaysTime = Instant.now()
                        val syncTime = convertToTargetFormat(todaysTime.toString())
                        SharedPreferenceManager.getInstance(requireContext()).saveMoveRightSyncTime(syncTime)
                      //  Toast.makeText(requireContext(), response.body()?.message ?: "Health data stored successfully", Toast.LENGTH_SHORT).show()
                    } else {
                       // Toast.makeText(requireContext(), "Error storing data: ${response.code()} - ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun storeBodyFatData(): List<BodyFatPercentage>? {
        val jsonData: BodyFatJson = loadBodyFatJsonData()
        val fullList = jsonData.dataFatPoints
        var bodyFatList: List<BodyFatPercentage> = emptyList()
        bodyFatList = fullList.map {
            BodyFatPercentage(
                value = it.fitValue[0].value?.fpVal.toString(),
                record_type = "bodyFatPercentage",
                end_datetime = convertToTargetFormat(it.endTimeNanos.toString()),
                unit = it.dataTypeName.toString(),
                start_datetime = convertToTargetFormat(it.startTimeNanos.toString()),
                source_name = "google"
            )
        }
        // storeJsonHealthData(sleepJsonRequest)
        return bodyFatList
    }


    private fun loadBodyFatJsonData(): BodyFatJson {
        val json = context?.assets?.open("assets/fit/alldata/derived_com.google.body.fat.percentage_com.goo.json")
            ?.bufferedReader().use { it?.readText() }
        return Gson().fromJson(json, object : TypeToken<BodyFatJson>() {}.type)
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
                val targetFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC)
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
                val targetFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC)
                return targetFormatter.format(instant)
            } catch (e: DateTimeParseException) {
                // Try next format
            }
        }

        return "" // Unable to parse
    }

    fun openAppSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = ("package:" + context.packageName).toUri()
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Unable to open settings", Toast.LENGTH_SHORT).show()
        }
    }

    fun isHealthConnectAvailable(context: Context): Boolean {
        val packageManager = context.packageManager
        return try {
            packageManager.getPackageInfo("com.google.android.apps.healthdata", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun installHealthConnect(context: Context) {
        val uri = "https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun requestHealthConnectPermission(activity: Activity) {
        val permissions = setOf(
            HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getReadPermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(SleepSessionRecord::class),
            HealthPermission.getReadPermission(ExerciseSessionRecord::class),
            HealthPermission.getReadPermission(SpeedRecord::class),
            HealthPermission.getReadPermission(WeightRecord::class),
            HealthPermission.getReadPermission(DistanceRecord::class),
            HealthPermission.getReadPermission(OxygenSaturationRecord::class),
            HealthPermission.getReadPermission(RespiratoryRateRecord::class)
        )
        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
        val permissionLauncher = requireActivity().registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(permissions)) {
                Toast.makeText(activity, "samsung Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }
        permissionLauncher.launch(permissions)
    }

    fun loadStepData() {
        // Placeholder for loading step data from assets if needed
    }

    fun showLoader(view: View) {
        loadingOverlay = view.findViewById(R.id.loading_overlay)
        loadingOverlay?.visibility = View.VISIBLE
    }

    fun dismissLoader(view: View) {
        loadingOverlay = view.findViewById(R.id.loading_overlay)
        loadingOverlay?.visibility = View.GONE
    }
}