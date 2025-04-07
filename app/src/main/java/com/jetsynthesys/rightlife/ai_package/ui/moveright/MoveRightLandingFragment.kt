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

class MoveRightLandingFragment : BaseFragment<FragmentLandingBinding>() {

    private lateinit var carouselViewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: Array<ImageView?>
    private lateinit var totalCaloriesBurnedRecord: List<TotalCaloriesBurnedRecord>
    private lateinit var stepsRecord: List<StepsRecord>
    private lateinit var healthConnectClient: HealthConnectClient
    private lateinit var tvBurnValue: TextView
    private lateinit var calorieBalanceIcon: ImageView
    private lateinit var moveRightImageBack: ImageView
    private lateinit var stepLineGraphView: LineGrapghViewSteps
    private lateinit var stepsTv: TextView
    private lateinit var calorieCountText: TextView
    private lateinit var totalIntakeCalorieText: TextView
    private lateinit var totalBurnedCalorieText: TextView
    private lateinit var calorieBalanceDescription: TextView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var appPreference: AppPreference
    // private lateinit var progressBar: HalfCurveProgressBar

    // Define all required read permissions
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
        appPreference = AppPreference(requireContext())
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        carouselViewPager = view.findViewById(R.id.carouselViewPager)
        totalIntakeCalorieText= view.findViewById(R.id.textView1)
        calorieCountText= view.findViewById(R.id.calorie_count)
       // totalBurnedCalorieText= view.findViewById(R.id.textViewBurnValue)
        calorieBalanceIcon = view.findViewById(R.id.calorie_balance_icon)
        dotsLayout = view.findViewById(R.id.dotsLayout)
        moveRightImageBack = view.findViewById(R.id.moveright_image_back)
        calorieBalanceDescription = view.findViewById(R.id.on_track_textLine)
        tvBurnValue = view.findViewById(R.id.textViewBurnValue)
        stepLineGraphView = view.findViewById(R.id.line_graph_steps)
        stepsTv = view.findViewById(R.id.steps_text)
        moveRightImageBack.setOnClickListener {
            activity?.finish()
        }
        // progressBar = view.findViewById(R.id.progressBarCalories)

        // Fetch workout data and set up the carousel
        fetchUserWorkouts()
        fetchHealthSummary()

        // Set up click listeners
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


        // Set up Health Connect
//        if (!isHealthConnectAvailable(requireContext())) {
//            installHealthConnect(requireContext())
//        }else{
//            requestHealthConnectPermission(requireActivity())
//        }

        val availabilityStatus = HealthConnectClient.getSdkStatus(requireContext())
        if (availabilityStatus == HealthConnectClient.SDK_AVAILABLE) {
            healthConnectClient = HealthConnectClient.getOrCreate(requireContext())
            lifecycleScope.launch {
                requestPermissionsAndReadSteps()
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

        val todaySteps = floatArrayOf(100f, 200f, 100f, 300f, 50f, 400f, 100f)
        val averageSteps = floatArrayOf(150f, 250f, 350f, 450f, 550f, 650f, 750f)
        val goalSteps = floatArrayOf(700f, 700f, 700f, 700f, 700f, 700f, 700f)
        stepLineGraphView.addDataSet(todaySteps, 0xFFFD6967.toInt())
        stepLineGraphView.addDataSet(averageSteps, 0xFF707070.toInt())
        stepLineGraphView.addDataSet(goalSteps, 0xFF03B27B.toInt())
        stepLineGraphView.invalidate()

        // Handle back press
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
    private fun requestPermissionsAndReadSteps() {
        lifecycleScope.launch {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (allReadPermissions.all { it in granted }) {
                retrieveStepsData()
                fetchActiveCaloriesBurned()
                fetchHeartRateData()
            } else {
                requestPermissionsLauncher.launch(allReadPermissions.toTypedArray())
              //  requestHealthConnectPermission(requireActivity())
            }
        }
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.all { it }) {
                Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }

    private suspend fun retrieveStepsData() {
        try {
            val endTime = Instant.now()
            val startTime = endTime.minusSeconds(24 * 60 * 60)
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            stepsRecord = response.records
            for (record in response.records) {
                val startDateTime = record.startTime.atZone(ZoneId.systemDefault())
                val endDateTime = record.endTime.atZone(ZoneId.systemDefault())
                val steps = record.count
                println("Steps: $steps, from $startDateTime to $endDateTime")
            }
            val stepCountArray = fillFloatArrayFromSteps(stepsRecord)

            val todaySteps = stepCountArray
            val averageSteps = stepCountArray
            val goalSteps = stepCountArray
            stepLineGraphView.addDataSet(todaySteps, 0xFFFD6967.toInt())
            stepLineGraphView.addDataSet(averageSteps, 0xFF707070.toInt())
            stepLineGraphView.addDataSet(goalSteps, 0xFF03B27B.toInt())
            stepLineGraphView.invalidate()
            println(stepCountArray)

            println("Steps data retrieved successfully!")

            val stepsToday = fetchTodaySteps(healthConnectClient)
            stepsTv.text = stepsToday.toString()
            println("Total steps today: $stepsToday")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun fetchActiveCaloriesBurned() {
        val timeRangeFilter = TimeRangeFilter.between(
            Instant.now().minusSeconds(24 * 60 * 60),
            Instant.now()
        )
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = TotalCaloriesBurnedRecord::class,
                timeRangeFilter = timeRangeFilter
            )
        )
        totalCaloriesBurnedRecord = response.records
        println(totalCaloriesBurnedRecord)
        response.records.forEach { record ->
            println("Calories burned: ${record.energy.inKilocalories} kcal")
            println("Start time: ${record.startTime}")
            println("End time: ${record.endTime}")
            tvBurnValue.text = record.energy.inKilocalories.toInt().toString()
        }
    }

    private suspend fun fetchHeartRateData() {
        val endTime = Instant.now()
        val startTime = endTime.minusSeconds(7 * 24 * 60 * 60)
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        )
        for (record in response.records) {
            Log.d(
                "HeartRateRecord",
                "Start: ${record.startTime}, End: ${record.endTime}, " +
                        "BPM: ${record.samples.map { it.beatsPerMinute }}"
            )
        }
    }

    private fun fillFloatArrayFromSteps(stepsRecords: List<StepsRecord>): FloatArray {
        return FloatArray(stepsRecords.size) { index ->
            stepsRecords[index].count.toFloat()
        }
    }

    private suspend fun fetchTodaySteps(healthConnectClient: HealthConnectClient): Long {
        val endTime = Instant.now()
        val startTime = endTime.minusSeconds(24 * 60 * 60)
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        )
        val totalSteps = response.records.sumOf { it.count }
        return totalSteps
    }

    fun isSamsungDevice(): Boolean {
        return Build.MANUFACTURER.equals("Samsung", ignoreCase = true)
    }

    private fun fetchUserWorkouts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiServiceFastApi.getUserWorkouts(
                    userId = "64763fe2fa0e40d9c0bc8264",
                    startDate = "2025-03-17",
                    endDate = "2025-03-25",
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
                                        putSerializable("cardItem", cardItem) // Use putSerializable() instead of putParcelable()
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
                          //  tvBurnValue.text = measuredValue.toString()
                            totalBurnedCalorieText.text = totalBurnedSum.toString()
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

    /*private fun fetchHealthSummary() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiServiceFastApi.getMoveLanding(
                    userId = "64763fe2fa0e40d9c0bc8264",
                    date = "2025-03-24" // Fixed variable name
                )

                if (response.isSuccessful) {
                    val healthSummary = response.body()
                    healthSummary?.let {


                        // Store heart rate zones for use in fetchUserWorkouts
                       // heartRateZones = it.heartRateZones

                        // Update UI with health summary data
                        withContext(Dispatchers.Main) {
                            calorieBalanceDescription.text = it.message.toString()
                            println("Health Summary Fetched Successfully")
                            // TODO: Update UI here
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
    }*/

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