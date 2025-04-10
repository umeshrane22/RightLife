package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
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
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.CardItem
import com.jetsynthesys.rightlife.ai_package.model.FitnessResponse
import com.jetsynthesys.rightlife.ai_package.model.GridItem
import com.jetsynthesys.rightlife.ai_package.ui.adapter.CarouselAdapter
import com.jetsynthesys.rightlife.ai_package.ui.adapter.GridAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.YourMealLogsFragment
import com.jetsynthesys.rightlife.ai_package.ui.moveright.graphs.LineGrapghViewSteps
import com.jetsynthesys.rightlife.ai_package.utils.AppPreference
import com.jetsynthesys.rightlife.databinding.FragmentLandingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import kotlin.math.abs
import androidx.core.net.toUri
import java.time.LocalDate

class MoveRightLandingFragment : BaseFragment<FragmentLandingBinding>() {

    private lateinit var carouselViewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: Array<ImageView?>
    private lateinit var totalCaloriesBurnedRecord: List<TotalCaloriesBurnedRecord>
    private lateinit var stepsRecord: List<StepsRecord>
    private lateinit var heartRateRecord: List<HeartRateRecord>
    private lateinit var sleepSessionRecord: List<SleepSessionRecord>
    private lateinit var exerciseSessionRecord: List<ExerciseSessionRecord>
    private lateinit var speedRecord: List<SpeedRecord>
    private lateinit var weightRecord: List<WeightRecord>
    private lateinit var distanceRecord: List<DistanceRecord>
    private lateinit var healthConnectClient: HealthConnectClient
    private lateinit var tvBurnValue: TextView
    private lateinit var calorieBalanceIcon: ImageView
    private lateinit var moveRightImageBack: ImageView
    private lateinit var stepLineGraphView: LineGrapghViewSteps
    private lateinit var stepsTv: TextView
    private lateinit var activeStepsTv: TextView
    private lateinit var steps_no_data_text: TextView
    private lateinit var stes_no_data_text_description: TextView
    private lateinit var sync_with_apple_button: ConstraintLayout
    private lateinit var goalStepsTv: TextView
    private lateinit var calorieCountText: TextView
    private lateinit var totalIntakeCalorieText: TextView
    private lateinit var calorieBalanceDescription: TextView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var appPreference: AppPreference
    private var totalIntakeCaloriesSum: Int = 0
    private val allReadPermissions = setOf(
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(SpeedRecord::class),
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class)
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
        appPreference = AppPreference(requireContext())
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        carouselViewPager = view.findViewById(R.id.carouselViewPager)
        totalIntakeCalorieText = view.findViewById(R.id.textView1)
        calorieCountText = view.findViewById(R.id.calorie_count)
        steps_no_data_text = view.findViewById(R.id.steps_no_data_text)
        stes_no_data_text_description = view.findViewById(R.id.stes_no_data_text_description)
        sync_with_apple_button = view.findViewById(R.id.sync_with_apple_button)
       // totalBurnedCalorieText = view.findViewById(R.id.textViewBurnValue)
        calorieBalanceIcon = view.findViewById(R.id.calorie_balance_icon)
        dotsLayout = view.findViewById(R.id.dotsLayout)
        moveRightImageBack = view.findViewById(R.id.moveright_image_back)
        calorieBalanceDescription = view.findViewById(R.id.on_track_textLine)
        tvBurnValue = view.findViewById(R.id.textViewBurnValue)
        stepLineGraphView = view.findViewById(R.id.line_graph_steps)
        stepsTv = view.findViewById(R.id.steps_text)
        activeStepsTv = view.findViewById(R.id.active_text)
        goalStepsTv = view.findViewById(R.id.goal_tex)
        moveRightImageBack.setOnClickListener {
            activity?.finish()
        }
        fetchUserWorkouts()
        //fetchHealthSummary()
        val workoutImageIcon = view.findViewById<ImageView>(R.id.workout_forward_icon)
        val activityFactorImageIcon = view.findViewById<ImageView>(R.id.activity_forward_icon)
        val logMealButton = view.findViewById<ConstraintLayout>(R.id.log_meal_button)
        val layoutAddWorkout = view.findViewById<ConstraintLayout>(R.id.lyt_snap_meal)
        calorieBalanceIcon.setOnClickListener {
            navigateToFragment(CalorieBalance(), "CalorieBalance")
        }

        workoutImageIcon.setOnClickListener {
            navigateToFragment(YourActivityFragment(), "YourActivityFragment")
        }

        layoutAddWorkout.setOnClickListener {
            // navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")
        }

        activityFactorImageIcon.setOnClickListener {
            // Add click listener if needed
        }

        logMealButton.setOnClickListener {
            navigateToFragment(YourMealLogsFragment(), "YourMealLogs")
        }

        val availabilityStatus = HealthConnectClient.getSdkStatus(requireContext())
        if (availabilityStatus == HealthConnectClient.SDK_AVAILABLE) {
            healthConnectClient = HealthConnectClient.getOrCreate(requireContext())
            lifecycleScope.launch {
                requestPermissionsAndReadAllData()
            }
        } else {
            Toast.makeText(context, "Please install or update Health Connect from the Play Store.", Toast.LENGTH_LONG).show()
        }

        val progressBarSteps = view.findViewById<ProgressBar>(R.id.progressBar)
        val circleIndicator = view.findViewById<View>(R.id.circleIndicator)
        val transparentOverlay = view.findViewById<View>(R.id.transparentOverlay)
        val belowTransparent = view.findViewById<ImageView>(R.id.imageViewBelowOverlay)
        val progressBarLayout = view.findViewById<ConstraintLayout>(R.id.progressBarLayout)

        progressBarSteps.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                progressBarSteps.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val progressBarWidth = progressBarSteps.width.toFloat()
                val overlayPositionPercentage = 0.6f
                val progress = progressBarSteps.progress
                val max = progressBarSteps.max
                val progressPercentage = progress.toFloat() / max
                println("Progress Percentage: $progressPercentage")
                val constraintSet = ConstraintSet()
                constraintSet.clone(progressBarLayout)
                constraintSet.setGuidelinePercent(R.id.circleIndicatorGuideline, progressPercentage)
                constraintSet.setGuidelinePercent(R.id.overlayGuideline, overlayPositionPercentage)
                constraintSet.applyTo(progressBarLayout)
            }
        })
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val items = listOf(
            GridItem("RHR", R.drawable.rhr_icon, "bpm", "64"),
            GridItem("Avg HR", R.drawable.rhr_icon, "bpm", "84"),
            GridItem("HRV", R.drawable.hrv_icon, "ms", "71"),
            GridItem("Burn", R.drawable.burn_icon, "Kcal", "844")
        )
        val adapter1 = GridAdapter(items) { itemName ->
            when (itemName) {
                "RHR" -> {
                    navigateToFragment(AverageHeartRateFragment(), "AverageHeartRateFragment")
                }
                "Avg HR" -> {
                    navigateToFragment(RestingHeartRateFragment(), "RestingHeartRateFragment")
                }
                "HRV" -> {
                    navigateToFragment(HeartRateVariabilityFragment(), "HeartRateVariabilityFragment")
                }
                "Burn" -> {
                    navigateToFragment(BurnFragment(), "BurnFragment")
                }
            }
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter1

       /* val todaySteps = floatArrayOf(100f, 200f, 100f, 300f, 50f, 400f, 100f)
        val averageSteps = floatArrayOf(150f, 250f, 350f, 450f, 550f, 650f, 750f)
        val goalSteps = floatArrayOf(700f, 700f, 700f, 700f, 700f, 700f, 700f)
        stepLineGraphView.addDataSet(todaySteps, 0xFFFD6967.toInt())
        stepLineGraphView.addDataSet(averageSteps, 0xFF707070.toInt())
        stepLineGraphView.addDataSet(goalSteps, 0xFF03B27B.toInt())
        stepLineGraphView.invalidate()*/

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    private fun addDotsIndicator(count: Int) {
        dots = arrayOfNulls(count)
        dotsLayout.removeAllViews()
        for (i in 0 until count) {
            dots[i] = ImageView(requireContext()).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.dot_unselected
                    )
                )
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
                layoutParams = params
            }
            dotsLayout.addView(dots[i])
        }
        updateDots(0)
    }

    private fun updateDots(position: Int) {
        for (i in dots.indices) {
            val drawable = if (i == position) {
                R.drawable.dot_selected
            } else {
                R.drawable.dot_unselected
            }
            dots[i]?.setImageResource(drawable)
        }
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private suspend fun requestPermissionsAndReadAllData() {
        try {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (allReadPermissions.all { it in granted }) {
                fetchAllHealthData()
            } else {
                requestPermissionsLauncher.launch(allReadPermissions.toTypedArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error checking permissions: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.all { it }) {
                lifecycleScope.launch {
                    fetchAllHealthData()
                }
                Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private suspend fun fetchAllHealthData() {
        val endTime = Instant.now()
        val startTime = endTime.minusSeconds(7 * 24 * 60 * 60) // Last 7 days

        try {
            // Fetch Steps Data
            val stepsResponse = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            stepsRecord = stepsResponse.records
            if(stepsRecord.isEmpty()){

            }else{
                val totalSteps = stepsRecord.sumOf { it.count }
                withContext(Dispatchers.Main) {
                    stepsTv.text = totalSteps.toString()
                }
                Log.d("HealthData", "Steps: $totalSteps")

                // Aggregate steps by day for the last 7 days
                val dailySteps = FloatArray(7) { index ->
                    val dayStart = endTime.minusSeconds(((6 - index) * 24 * 60 * 60).toLong())
                    val dayEnd = dayStart.plusSeconds(24 * 60 * 60)
                    stepsRecord.filter { record ->
                        record.startTime.isAfter(dayStart) && record.endTime.isBefore(dayEnd)
                    }.sumOf { it.count }.toFloat()
                }
                val totalDailySteps = dailySteps.sum()

                print(totalDailySteps)

                val totalAverageSteps = dailySteps.average().toFloat()
                print(totalAverageSteps)
                val totalGoalSteps = 700f * 7
                withContext(Dispatchers.Main) {
                    activeStepsTv.text = totalAverageSteps.toInt().toString()
                    goalStepsTv.text = totalGoalSteps.toInt().toString()
                }
                val averageSteps = FloatArray(7) { dailySteps.average().toFloat() }
                val goalSteps = FloatArray(7) { 3500f }
                withContext(Dispatchers.Main) {
                    stepLineGraphView.clear()
                    stepLineGraphView.addDataSet(dailySteps, 0xFFFD6967.toInt())
                    stepLineGraphView.addDataSet(averageSteps, 0xFF707070.toInt())
                    stepLineGraphView.addDataSet(goalSteps, 0xFF03B27B.toInt())
                    stepLineGraphView.invalidate()
                }
            }


            // ... (Rest of the fetchAllHealthData function for other record types remains unchanged)

            // Fetch Total Calories Burned
            val caloriesResponse = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = TotalCaloriesBurnedRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            totalCaloriesBurnedRecord = caloriesResponse.records
            val totalBurnedCalories = totalCaloriesBurnedRecord.sumOf { it.energy.inKilocalories.toInt() }
            Log.d("HealthData", "Total Burned Calories: $totalBurnedCalories kcal")

            // Save total intake calories (from fetchHealthSummary) and burned calories to variables
            val totalIntakeCalories = if (totalIntakeCaloriesSum > 0) totalIntakeCaloriesSum else 0
            // totalBurnedCalories is already defined above

            // Optionally log the saved values for verification
            Log.d("HealthData", "Saved Total Intake Calories: $totalIntakeCalories kcal")
            Log.d("HealthData", "Saved Total Burned Calories: $totalBurnedCalories kcal")

            withContext(Dispatchers.Main) {
                tvBurnValue.text = totalBurnedCalories.toString()
                totalIntakeCalorieText.text = totalIntakeCalories.toString()
                val calorieBalance = totalIntakeCalories - totalBurnedCalories
                val absoluteCalorieBalance = kotlin.math.abs(calorieBalance)
                calorieCountText.text = if (calorieBalance >= 0) "+$absoluteCalorieBalance" else "$absoluteCalorieBalance"


            }
            //Log.d("HealthData", "Total Calories Burned: $totalCalories kcal")

            // Fetch Heart Rate Data
            val todayStart = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()

            val now = Instant.now()

            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = HeartRateRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(todayStart, now)
                )
            )

            heartRateRecord = response.records
            if (heartRateRecord.isEmpty()) {
                Log.d("HealthData", "No heart rate records found for today.")
            } else {
                heartRateRecord.forEach { record ->
                    val bpm = record.samples.map { it.beatsPerMinute }.average().toInt()
                    Log.d("HealthData", "Heart Rate: $bpm bpm, Start: ${record.startTime}, End: ${record.endTime}")
                }
            }

            // Fetch Sleep Session Data
            val sleepResponse = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = SleepSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            sleepSessionRecord = sleepResponse.records
            sleepSessionRecord.forEach { record ->
                Log.d("HealthData", "Sleep Session: Start: ${record.startTime}, End: ${record.endTime}, Stages: ${record.stages}")
            }

            // Fetch Exercise Session Data
            val exerciseResponse = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = ExerciseSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            exerciseSessionRecord = exerciseResponse.records
            exerciseSessionRecord.forEach { record ->
                Log.d("HealthData", "Exercise Session: Type: ${record.exerciseType}, Start: ${record.startTime}, End: ${record.endTime}")
            }

            // Fetch Speed Data
            val speedResponse = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = SpeedRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            speedRecord = speedResponse.records
            speedRecord.forEach { record ->
                //Log.d("HealthData", "Speed: ${record.speed.inMetersPerSecond} m/s, Start: ${record.startTime}, End: ${record.endTime}")
            }

            // Fetch Weight Data
            val weightResponse = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = WeightRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            weightRecord = weightResponse.records
            weightRecord.forEach { record ->
                Log.d("HealthData", "Weight: ${record.weight.inKilograms} kg, Time: ${record.time}")
            }

            // Fetch Distance Data
            val distanceResponse = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = DistanceRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            distanceRecord = distanceResponse.records
            val totalDistance = distanceRecord.sumOf { it.distance.inMeters }
            withContext(Dispatchers.Main) {
               // calorieCountText.text = totalDistance.toString() // Example usage, adjust as needed
            }
            Log.d("HealthData", "Total Distance: $totalDistance meters")

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "All Health Data Fetched Successfully", Toast.LENGTH_SHORT).show()
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
                val response = ApiClient.apiServiceFastApi.getNewUserWorkouts(
                    userId = "64763fe2fa0e40d9c0bc8264",
                    rangeType = "daily",
                    date = "2025-03-24",
                    page = 1,
                    limit = 10
                )

                if (response.isSuccessful) {
                    val workouts = response.body()
                    workouts?.let {
                        val totalSyncedCalories = it.syncedWorkouts.sumOf { workout ->
                            workout.caloriesBurned.toIntOrNull() ?: 0
                        }
                        val totalUnsyncedCalories = it.unsyncedWorkouts.sumOf { it.caloriesBurned }
                        val totalCalories = totalSyncedCalories + totalUnsyncedCalories

                        val cardItems = it.syncedWorkouts.map { workout ->
                            val durationMinutes = workout.duration.toIntOrNull() ?: 0
                            val hours = durationMinutes / 60
                            val minutes = durationMinutes % 60
                            val durationText = if (hours > 0) {
                                "$hours hr ${minutes.toString().padStart(2, '0')} mins"
                            } else {
                                "$minutes mins"
                            }
                            val caloriesText = "${workout.caloriesBurned} cal"
                            val avgHeartRate = if (workout.heartRateData.isNotEmpty()) {
                                val totalHeartRate = workout.heartRateData.sumOf { it.heartRate }
                                val count = workout.heartRateData.size
                                (totalHeartRate / count).toString() + " bpm"
                            } else {
                                "N/A"
                            }

                            workout.heartRateData.forEach { heartRateData ->
                                heartRateData.trendData.addAll(
                                    listOf("110", "112", "115", "118", "120", "122", "125")
                                )
                            }

                            CardItem(
                                title = workout.workoutType,
                                duration = durationText,
                                caloriesBurned = caloriesText,
                                avgHeartRate = avgHeartRate,
                                heartRateData = workout.heartRateData
                            )
                        }
                        withContext(Dispatchers.Main) {
                            val maxCalories = 3000
                            val progressPercentage = (totalCalories.toFloat() / maxCalories.toFloat() * 100f).coerceIn(0f, 100f)
                            val adapter = CarouselAdapter(cardItems) { cardItem, position ->
                                val fragment = WorkoutAnalyticsFragment().apply {
                                    arguments = Bundle().apply {
                                        putSerializable("cardItem", cardItem)
                                    }
                                }
                                requireActivity().supportFragmentManager.beginTransaction().apply {
                                    replace(R.id.flFragment, fragment, "workoutAnalysisFragment")
                                    addToBackStack(null)
                                    commit()
                                }
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
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        println("Error: ${response.code()} - ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    println("Exception: ${e.message}")
                }
            }
        }
    }

    private fun fetchHealthSummary() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId: String = "64763fe2fa0e40d9c0bc8264"
                val date: String = "2025-03-24"

                val response: Response<FitnessResponse> = ApiClient.apiServiceFastApi.getMoveLanding(
                    userId = userId,
                    date = date
                )

                if (response.isSuccessful) {
                    val healthSummary: FitnessResponse? = response.body()
                    if (healthSummary != null) {
                        val heartRateZones = healthSummary.heartRateZones
                        val steps = healthSummary.steps
                        val totalBurnedSum = healthSummary.totalBurnedSum
                        val heartRateVariabilitySDNN = healthSummary.heartRateVariabilitySDNN
                        val totalStepsSum = healthSummary.totalStepsSum
                        val totalIntakeCaloriesSum = healthSummary.totalIntakeCaloriesSum
                        val burnedCaloriesSum = healthSummary.totalBurnedSum
                        val message = healthSummary.message
                        val measuredValue = totalIntakeCaloriesSum - totalBurnedSum
                        withContext(Dispatchers.Main) {
                            tvBurnValue.text = measuredValue.toString()
                            //totalBurnedCalorieText.text = totalBurnedSum.toString()
                            totalIntakeCalorieText.text = totalIntakeCaloriesSum.toString()
                            calorieBalanceDescription.text = message.toString()
                            Log.d("HealthSummary", "Full Response: $healthSummary")
                            Log.d("HealthSummary", "Total Steps Sum: $totalStepsSum")
                            Log.d("HealthSummary", "Total Burned Sum: $totalBurnedSum")
                            Log.d("HealthSummary", "Total Intake Calories Sum: $totalIntakeCaloriesSum")
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Log.e("HealthSummary", "Response body is null")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        val errorMessage = "Error: ${response.code()} - ${response.message()}"
                        Log.e("HealthSummary", errorMessage)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("HealthSummary", "Exception: ${e.message}", e)
                }
            }
        }
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
        val uri =
            "https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata".toUri()
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
            HealthPermission.getReadPermission(DistanceRecord::class)
        )

        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

        val permissionLauncher = requireActivity().registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(permissions)) {
                Toast.makeText(activity, "Health Connect Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }
        permissionLauncher.launch(permissions)
    }
}