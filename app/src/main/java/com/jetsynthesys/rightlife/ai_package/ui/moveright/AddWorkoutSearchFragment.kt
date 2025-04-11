package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.graphics.Color
import android.graphics.Typeface
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
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.BundleCompat
import com.google.gson.Gson
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.CalculateCaloriesRequest
import com.jetsynthesys.rightlife.ai_package.model.CalculateCaloriesResponse
import com.jetsynthesys.rightlife.ai_package.model.WorkoutList
import com.jetsynthesys.rightlife.ai_package.model.WorkoutSessionRecord
import com.jetsynthesys.rightlife.ai_package.ui.moveright.customProgressBar.CustomProgressBar
import com.jetsynthesys.rightlife.databinding.FragmentAddWorkoutSearchBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class AddWorkoutSearchFragment : BaseFragment<FragmentAddWorkoutSearchBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddWorkoutSearchBinding
        get() = FragmentAddWorkoutSearchBinding::inflate

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var intensityProgressBar: CustomProgressBar
    private lateinit var caloriesText: TextView
    private var selectedTime: String = "1 hr 0 min"
    private var selectedIntensity: String = "Low" // Default to lowercase
    private var workout: WorkoutList? = null
    private var workoutRecord: WorkoutSessionRecord? = null
    private var lastWorkoutRecord: WorkoutSessionRecord? = null // Store all session data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workout = arguments?.getParcelable("workout")
        if (workout == null) {
            Toast.makeText(requireContext(), "No workout selected", Toast.LENGTH_SHORT).show()
            Log.e("AddWorkoutSearch", "Workout is null")
        } else {
            Log.d("AddWorkoutSearch", "Workout ID: ${workout?._id}")
        }
        lastWorkoutRecord =
            arguments?.let { BundleCompat.getParcelable(it, "workoutRecord", WorkoutSessionRecord::class.java) }

        val hourPicker = view.findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = view.findViewById<NumberPicker>(R.id.minutePicker)
        val addLog = view.findViewById<LinearLayoutCompat>(R.id.layout_btn_log_meal)
        val addSearchFragmentBackButton = view.findViewById<ImageView>(R.id.back_button)
        intensityProgressBar = view.findViewById(R.id.customSeekBar)
        caloriesText = view.findViewById(R.id.calories_text)

        addSearchFragmentBackButton.setOnClickListener {
            navigateToFragment(SearchWorkoutFragment(), "SearchWorkoutFragment")
        }

        addLog.setOnClickListener {
            workout?.let { workout ->
                val hours = hourPicker.value
                val minutes = minutePicker.value
                val durationMinutes = hours * 60 + minutes
                if (durationMinutes > 0) {
                    // Create a new WorkoutSessionRecord with current input values
                    val newWorkoutRecord = WorkoutSessionRecord(
                        userId = "64763fe2fa0e40d9c0bc8264",
                        activityId = workout._id,
                        durationMin = durationMinutes,
                        intensity = selectedIntensity,
                        sessions = 1,
                        // Use lastWorkoutRecord's response data if available
                        message = lastWorkoutRecord?.message,
                        caloriesBurned = lastWorkoutRecord?.caloriesBurned,
                        activityFactor = lastWorkoutRecord?.activityFactor,
                        moduleName = workout.title.toString()
                    )

                    // Save the new record
                    lastWorkoutRecord = newWorkoutRecord

                    // Navigate to YourworkOutsFragment with the new record
                    val fragment = YourworkOutsFragment()
                    val args = Bundle().apply {
                        putInt("duration", durationMinutes)
                        putString("time", selectedTime)
                        putString("intensity", selectedIntensity)
                        lastWorkoutRecord?.caloriesBurned?.let { calories ->
                            putDouble("calories", calories)
                        }
                        putParcelable("workoutRecord", lastWorkoutRecord)
                        putParcelable("workout", workout)// Pass the full record
                    }
                    fragment.arguments = args
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, fragment, "YourworkOutsFragment")
                        addToBackStack("YourworkOutsFragment")
                        commit()
                    }

                    // Trigger API call if calories haven't been calculated yet
                    if (lastWorkoutRecord?.caloriesBurned == null) {
                        Toast.makeText(requireContext(), "Calculating calories...", Toast.LENGTH_SHORT).show()
                        calculateUserCalories(durationMinutes, selectedIntensity, workout._id)
                    }
                } else {
                    Toast.makeText(requireContext(), "Please select a duration", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(requireContext(), "Please select a workout", Toast.LENGTH_SHORT).show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(SearchWorkoutFragment(), "SearchWorkoutFragment")
            }
        })

        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        minutePicker.minValue = 0
        minutePicker.maxValue = 59

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
            workout?.let { workout ->
                if (durationMinutes > 0) {
                    calculateUserCalories(durationMinutes, selectedIntensity, workout._id)
                }
            }
            refreshPickers()
        }

        hourPicker.setOnValueChangedListener(timeListener)
        minutePicker.setOnValueChangedListener(timeListener)
        refreshPickers()

        intensityProgressBar.progress = 0.0f
        intensityProgressBar.setOnProgressChangedListener { _ ->
            selectedIntensity = intensityProgressBar.getCurrentIntensity()
            Log.d("AddWorkoutSearch", "Intensity: $selectedIntensity")
            val hours = hourPicker.value
            val minutes = minutePicker.value
            val durationMinutes = hours * 60 + minutes
            workout?.let { workout ->
                if (durationMinutes > 0) {
                    calculateUserCalories(durationMinutes, selectedIntensity, workout._id)
                }
            }
        }

        // Initial API call with default values if workout is available
        workout?.let { workout ->
            calculateUserCalories(60, selectedIntensity, workout._id) // 1 hr default
        }
    }

    private fun calculateUserCalories(durationMinutes: Int, selectedIntensity: String, activityId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
                val userId = "64763fe2fa0e40d9c0bc8264"
                val request = CalculateCaloriesRequest(
                    userId = userId,
                    activityId = activityId,
                    durationMin = durationMinutes,
                    intensity = selectedIntensity,
                    sessions = 1
                )

                // Log the request for debugging
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
                        } // No response data
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
                    } // No response data
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
                } // No response data
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("CalculateCalories", "Exception: ${e.stackTraceToString()}")
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