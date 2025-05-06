package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.BundleCompat
import com.google.gson.Gson
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.ActivityModel
import com.jetsynthesys.rightlife.ai_package.model.CalculateCaloriesRequest
import com.jetsynthesys.rightlife.ai_package.model.CalculateCaloriesResponse
import com.jetsynthesys.rightlife.ai_package.model.WorkoutList
import com.jetsynthesys.rightlife.ai_package.model.WorkoutSessionRecord
import com.jetsynthesys.rightlife.ai_package.model.request.CreateWorkoutRequest
import com.jetsynthesys.rightlife.ai_package.model.request.UpdateCaloriesRequest
import com.jetsynthesys.rightlife.ai_package.ui.moveright.customProgressBar.CustomProgressBar
import com.jetsynthesys.rightlife.databinding.FragmentAddWorkoutSearchBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddWorkoutSearchFragment : BaseFragment<FragmentAddWorkoutSearchBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddWorkoutSearchBinding
        get() = FragmentAddWorkoutSearchBinding::inflate

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var intensityProgressBar: CustomProgressBar
    private lateinit var caloriesText: TextView
    private lateinit var addLog: LinearLayoutCompat
    private var selectedTime: String = "1 hr 0 min"
    private var selectedIntensity: String = "Low" // Default to API-accepted value
    private var edit: String = ""
    private var workout: WorkoutList? = null
    private var activityModel: ActivityModel? = null
    private var lastWorkoutRecord: WorkoutSessionRecord? = null

    // Normalize intensity to match API's expected values
    private fun normalizeIntensity(intensity: String): String {
        return when (intensity.lowercase()) {
            "low" -> "Low"
            "medium", "moderate" -> "Moderate"
            "high" -> "High"
            "very high", "veryhigh" -> "Very High"
            else -> "Low" // Default to Low if unknown
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve workout from arguments
        activityModel = arguments?.getParcelable("ACTIVITY_MODEL")
        edit = arguments?.getString("edit") ?: ""
        workout = arguments?.getParcelable("workout")
        if (edit == "edit") {
            if (activityModel == null) {
                Toast.makeText(requireContext(), "No activity data provided for editing", Toast.LENGTH_SHORT).show()
                Log.e("AddWorkoutSearch", "ActivityModel is null in edit mode")
                return
            } else {
                Log.d("AddWorkoutSearch", "Editing activity: ${activityModel?.activityType}, Calorie ID: ${activityModel?.calorieId}")
            }
        } else {
            if (workout == null) {
                Toast.makeText(requireContext(), "No workout selected", Toast.LENGTH_SHORT).show()
                Log.e("AddWorkoutSearch", "Workout is null")
                navigateToFragment(AllWorkoutFragment(), "AllWorkoutFragment")
                return
            } else {
                Log.d("AddWorkoutSearch", "Workout ID: ${workout?._id}, Title: ${workout?.title}")
            }
        }

        lastWorkoutRecord = arguments?.let { BundleCompat.getParcelable(it, "workoutRecord", WorkoutSessionRecord::class.java) }

        val hourPicker = view.findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = view.findViewById<NumberPicker>(R.id.minutePicker)
        addLog = view.findViewById<LinearLayoutCompat>(R.id.layout_btn_log_meal)
        val addSearchFragmentBackButton = view.findViewById<ImageView>(R.id.back_button)
        intensityProgressBar = view.findViewById(R.id.customSeekBar)
        caloriesText = view.findViewById(R.id.calories_text)

        // Initially disable the addLog button until conditions are met
        addLog.isEnabled = false

        addSearchFragmentBackButton.setOnClickListener {
            if (edit.equals("edit")){
                navigateToFragment(YourActivityFragment(), "AllWorkoutFragment")
            }else{
                navigateToFragment(AllWorkoutFragment(), "AllWorkoutFragment")
            }

        }

        addLog.setOnClickListener {
            val hours = hourPicker.value
            val minutes = minutePicker.value
            val durationMinutes = hours * 60 + minutes
            if (edit == "edit") {
                activityModel?.calorieId?.let { calorieId ->
                    val normalizedIntensity = normalizeIntensity(selectedIntensity)
                    updateCalories(calorieId, durationMinutes, normalizedIntensity)
                } ?: run {
                    Toast.makeText(requireContext(), "Calorie ID is missing", Toast.LENGTH_SHORT).show()
                }
            } else {
                workout?.let { workout ->
                    if (durationMinutes > 0) {
                        val normalizedIntensity = normalizeIntensity(selectedIntensity)
                        val newWorkoutRecord = WorkoutSessionRecord(
                            userId = "64763fe2fa0e40d9c0bc8264",
                            activityId = workout._id,
                            durationMin = durationMinutes,
                            intensity = normalizedIntensity,
                            sessions = 1,
                            message = lastWorkoutRecord?.message,
                            caloriesBurned = lastWorkoutRecord?.caloriesBurned,
                            activityFactor = lastWorkoutRecord?.activityFactor,
                            moduleName = workout.title.toString()
                        )

                        lastWorkoutRecord = newWorkoutRecord
                        if (lastWorkoutRecord?.caloriesBurned == null) {
                            Toast.makeText(requireContext(), "Calculating calories...", Toast.LENGTH_SHORT).show()
                            val activityId = workout._id ?: activityModel?.activity_id
                            if (activityId != null) {
                                calculateUserCalories(durationMinutes, normalizedIntensity, activityId)
                            } else {
                                Toast.makeText(requireContext(), "Activity ID is missing", Toast.LENGTH_SHORT).show()
                            }
                        }
                        createWorkout(lastWorkoutRecord)
                    } else {
                        Toast.makeText(requireContext(), "Please select a duration", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(requireContext(), "Please select a workout", Toast.LENGTH_SHORT).show()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (edit != "edit") {
                    navigateToFragment(YourActivityFragment(), "AllWorkoutFragment")
                }else{
                    navigateToFragment(AllWorkoutFragment(), "AllWorkoutFragment")
                }

            }
        })

        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        minutePicker.minValue = 0
        minutePicker.maxValue = 59

        // Prefill data in edit mode
        if (edit == "edit" && activityModel != null) {
            val durationMin = activityModel?.duration?.replace(" min", "")?.toIntOrNull() ?: 0
            val hours = durationMin / 60
            val minutes = durationMin % 60
            hourPicker.value = hours
            minutePicker.value = minutes
            selectedIntensity = normalizeIntensity(activityModel?.intensity ?: "Low")
            when (selectedIntensity) {
                "Low" -> intensityProgressBar.progress = 0.0f
                "Moderate" -> intensityProgressBar.progress = 50.0f
                "High" -> intensityProgressBar.progress = 75.0f
                "Very High" -> intensityProgressBar.progress = 100.0f
            }
            selectedTime = "$hours hr ${minutes.toString().padStart(2, '0')} min"
            // Trigger initial calorie calculation in edit mode
            activityModel?.activity_id?.let { calorieId ->
                calculateUserCalories(durationMin, selectedIntensity, calorieId)
                addLog.isEnabled = true
            }
        }

        fun updateNumberPickerText(picker: NumberPicker) {
            try {
                val field = NumberPicker::class.java.getDeclaredField("mInputText")
                field.isAccessible = true
                val editText = field.get(picker) as EditText
                editText.setTextColor(Color.RED)
                editText.textSize = 24f
                editText.typeface = Typeface.DEFAULT_BOLD
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun refreshPickers() {
            handler.post {
                updateNumberPickerText(hourPicker)
                updateNumberPickerText(minutePicker)
            }
        }

        val timeListener = NumberPicker.OnValueChangeListener { _, _, _ ->
            val hours = hourPicker.value
            val minutes = minutePicker.value
            selectedTime = "$hours hr ${minutes.toString().padStart(2, '0')} min"
            val durationMinutes = hours * 60 + minutes
            if (durationMinutes > 0) {
                val activityId = if (edit == "edit") activityModel?.activity_id else workout?._id
                if (activityId != null) {
                    val normalizedIntensity = normalizeIntensity(selectedIntensity)
                    calculateUserCalories(durationMinutes, normalizedIntensity, activityId)
                    addLog.isEnabled = true
                } else {
                    Toast.makeText(requireContext(), "Activity ID is missing", Toast.LENGTH_SHORT).show()
                }
            } else {
                addLog.isEnabled = false
            }
            refreshPickers()
        }

        hourPicker.setOnValueChangedListener(timeListener)
        minutePicker.setOnValueChangedListener(timeListener)
        refreshPickers()

        intensityProgressBar.progress = 0.0f
        intensityProgressBar.setOnProgressChangedListener { progress ->
            selectedIntensity = when {
                progress <= 25.0f -> "Low"
                progress <= 50.0f -> "Moderate"
                progress <= 75.0f -> "High"
                else -> "Very High"
            }
            Log.d("AddWorkoutSearch", "Intensity set to: $selectedIntensity")
            val hours = hourPicker.value
            val minutes = minutePicker.value
            val durationMinutes = hours * 60 + minutes
            if (durationMinutes > 0) {
                val activityId = if (edit == "edit") activityModel?.activity_id else workout?._id
                if (activityId != null) {
                    calculateUserCalories(durationMinutes, selectedIntensity, activityId)
                    addLog.isEnabled = true
                } else {
                    Toast.makeText(requireContext(), "Activity ID is missing", Toast.LENGTH_SHORT).show()
                }
            } else {
                addLog.isEnabled = false
            }
        }

        // Initial API call with default values if in non-edit mode
        if (edit != "edit") {
            workout?.let { workout ->
                calculateUserCalories(60, selectedIntensity, workout._id) // 1 hr default
                addLog.isEnabled = true
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun createWorkout(workoutSession: WorkoutSessionRecord?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
                    ?: "64763fe2fa0e40d9c0bc8264"

                val currentDate = getCurrentDate()
                val request = workoutSession?.let {
                    CreateWorkoutRequest(
                        userId = userId,
                        activityId = it.activityId,
                        durationMin = it.durationMin,
                        intensity = it.intensity,
                        sessions = 1,
                        date = currentDate
                    )
                } ?: run {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Workout data is missing", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val response = ApiClient.apiServiceFastApi.createWorkout(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            val fragment = YourActivityFragment()
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                replace(R.id.flFragment, fragment, "YourActivityFragment")
                                addToBackStack("YourActivityFragment")
                                commit()
                            }
                            Toast.makeText(requireContext(), "Workout Created Successfully", Toast.LENGTH_SHORT).show()
                        } ?: Toast.makeText(
                            requireContext(),
                            "Empty response",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error creating workout: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Exception: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.e("AddWorkoutSearch", "Exception in createWorkout: ${e.stackTraceToString()}")
            }
        }
    }

    private fun calculateUserCalories(durationMinutes: Int, selectedIntensity: String, activityId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
                    ?: "64763fe2fa0e40d9c0bc8264"

                val request = CalculateCaloriesRequest(
                    userId = userId,
                    activityId = activityId,
                    durationMin = durationMinutes,
                    intensity = selectedIntensity,
                    sessions = 1,
                    date = getCurrentDate()
                )

                val requestJson = Gson().toJson(request)
                Log.d("CalculateCalories", "Request: $requestJson")

                val response: Response<CalculateCaloriesResponse> = ApiClient.apiServiceFastApi.calculateCalories(request)

                if (response.isSuccessful) {
                    val caloriesResponse = response.body()
                    if (caloriesResponse != null) {
                        lastWorkoutRecord = workout?.title?.let {
                            WorkoutSessionRecord(
                                userId = request.userId,
                                activityId = request.activityId,
                                durationMin = request.durationMin,
                                intensity = request.intensity,
                                sessions = request.sessions,
                                message = caloriesResponse.message,
                                caloriesBurned = caloriesResponse.caloriesBurned,
                                activityFactor = caloriesResponse.activityFactor,
                                moduleName = it
                            )
                        }
                        withContext(Dispatchers.Main) {
                            caloriesText.text = caloriesResponse.caloriesBurned.toInt().toString()
                            Toast.makeText(
                                requireContext(),
                                "Calories Burned: ${String.format("%.2f", caloriesResponse.caloriesBurned)} kcal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        lastWorkoutRecord = workout?.let {
                            WorkoutSessionRecord(
                                userId = request.userId,
                                activityId = request.activityId,
                                durationMin = request.durationMin,
                                intensity = request.intensity,
                                sessions = request.sessions,
                                moduleName = it.title
                            )
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "No calories data received", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    lastWorkoutRecord = workout?.let {
                        WorkoutSessionRecord(
                            userId = request.userId,
                            activityId = request.activityId,
                            durationMin = request.durationMin,
                            intensity = request.intensity,
                            sessions = request.sessions,
                            moduleName = it.title
                        )
                    }
                    val errorBody = response.errorBody()?.string()
                    Log.e("CalculateCalories", "Error: ${response.code()} - ${response.message()}, Body: $errorBody")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${response.code()} - ${response.message()}\nDetails: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                lastWorkoutRecord = workout?.let {
                    WorkoutSessionRecord(
                        userId = "64763fe2fa0e40d9c0bc8264",
                        activityId = activityId,
                        durationMin = durationMinutes,
                        intensity = selectedIntensity,
                        sessions = 1,
                        moduleName = it.title
                    )
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("CalculateCalories", "Exception: ${e.stackTraceToString()}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun updateCalories(calorieId: String, durationMinutes: Int, intensity: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
                    ?: "64763fe2fa0e40d9c0bc8264"

                val currentDate = getCurrentDate()
                val request = UpdateCaloriesRequest(
                    userId = userId,
                    durationMin = durationMinutes,
                    intensity = intensity,
                    sessions = 1,
                    date = currentDate
                )

                val response = ApiClient.apiServiceFastApi.updateCalories(calorieId, request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            val fragment = YourActivityFragment()
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                replace(R.id.flFragment, fragment, "YourActivityFragment")
                                addToBackStack("YourActivityFragment")
                                commit()
                            }
                            Toast.makeText(requireContext(), "Calories Updated Successfully", Toast.LENGTH_SHORT).show()
                        } ?: Toast.makeText(
                            requireContext(),
                            "Empty response",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error updating calories: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Exception: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.e("UpdateCalories", "Exception in updateCalories: ${e.stackTraceToString()}")
            }
        }
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
}