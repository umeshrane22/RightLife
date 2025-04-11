package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.BundleCompat
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.CalculateCaloriesRequest
import com.jetsynthesys.rightlife.ai_package.model.CalculateCaloriesResponse
import com.jetsynthesys.rightlife.ai_package.model.WorkoutList
import com.jetsynthesys.rightlife.ai_package.ui.moveright.customProgressBar.CustomProgressBar
import com.jetsynthesys.rightlife.databinding.FragmentAddWorkoutSearchBinding
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
    private var selectedTime: String = "1 hr 0 min" // Default value without AM/PM
    private var selectedIntensity: String = "Low" // Default value
    private var workout: WorkoutList? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use BundleCompat for compatibility across API levels
        workout = arguments?.getParcelable("workout")
        val hourPicker = view.findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = view.findViewById<NumberPicker>(R.id.minutePicker)
        val addLog = view.findViewById<LinearLayoutCompat>(R.id.layout_btn_log_meal)
        val addSearchFragmentBackButton = view.findViewById<ImageView>(R.id.back_button)
        intensityProgressBar = view.findViewById(R.id.customSeekBar)

        addSearchFragmentBackButton.setOnClickListener {
            navigateToFragment(SearchWorkoutFragment(), "SearchWorkoutFragment")
        }

        addLog.setOnClickListener {
            val hours = hourPicker.value
            val minutes = minutePicker.value
            val durationMinutes = hours * 60 + minutes
            workout?.let { it1 ->
                calculateUserCalories(durationMinutes, selectedIntensity, it1._id)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(SearchWorkoutFragment(), "SearchWorkoutFragment")
            }
        })

        // Configure NumberPickers
        hourPicker.minValue = 0 // Allow 0 hours
        hourPicker.maxValue = 23 // Up to 23 hours
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
            val minutes = minutePicker.value.toString().padStart(2, '0')
            selectedTime = "$hours hr $minutes min" // Updated format without AM/PM
            refreshPickers()
        }

        hourPicker.setOnValueChangedListener(timeListener)
        minutePicker.setOnValueChangedListener(timeListener)
        refreshPickers()

        intensityProgressBar.progress = 0.0f
        intensityProgressBar.setOnProgressChangedListener { _ ->
            selectedIntensity = intensityProgressBar.getCurrentIntensity()
        }
    }

    private fun calculateUserCalories(durationMinutes: Int, selectedIntensity: String, activityId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = "64763fe2fa0e40d9c0bc8264"
                val request = CalculateCaloriesRequest(
                    userId = userId,
                    activityId = activityId,
                    durationMin = durationMinutes,
                    intensity = selectedIntensity.lowercase(),
                    sessions = 1
                )

                val response: Response<CalculateCaloriesResponse> = ApiClient.apiServiceFastApi.calculateCalories(
                    request = request
                )

                if (response.isSuccessful) {
                    val caloriesResponse = response.body()
                    if (caloriesResponse != null) {
                        withContext(Dispatchers.Main) {
                            val fragment = YourworkOutsFragment()
                            val args = Bundle().apply {
                                putInt("duration", durationMinutes)
                                putString("time", selectedTime)
                                putString("intensity", selectedIntensity)
                            }
                            fragment.arguments = args
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                replace(R.id.flFragment, fragment, "YourworkOutsFragment")
                                addToBackStack("YourworkOutsFragment")
                                commit()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "No calories data received",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${response.code()} - ${response.message()}",
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