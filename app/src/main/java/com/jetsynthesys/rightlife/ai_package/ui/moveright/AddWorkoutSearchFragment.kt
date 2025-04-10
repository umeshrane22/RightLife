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
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.moveright.customProgressBar.CustomProgressBar
import com.jetsynthesys.rightlife.databinding.FragmentAddWorkoutSearchBinding
import com.shawnlin.numberpicker.NumberPicker

class AddWorkoutSearchFragment : BaseFragment<FragmentAddWorkoutSearchBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddWorkoutSearchBinding
        get() = FragmentAddWorkoutSearchBinding::inflate

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var intensityProgressBar: CustomProgressBar
    private var selectedTime: String = "1:00 AM"
    private var selectedIntensity: String = "Low" // Default value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hourPicker = view.findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = view.findViewById<NumberPicker>(R.id.minutePicker)
        val amPmPicker = view.findViewById<NumberPicker>(R.id.amPmPicker)
        val addLog = view.findViewById<LinearLayoutCompat>(R.id.layout_btn_log_meal)
        val addSearchFragmentBackButton = view.findViewById<ImageView>(R.id.back_button)
        intensityProgressBar = view.findViewById(R.id.customSeekBar)
        addSearchFragmentBackButton.setOnClickListener {
            navigateToFragment(SearchWorkoutFragment(), "SearchWorkoutFragment")
        }
        addLog.setOnClickListener {
            val hours = hourPicker.value
            val minutes = minutePicker.value
            val amPm = amPmPicker.displayedValues[amPmPicker.value]
            val durationMinutes = hours * 60 + minutes
            Toast.makeText(
                requireContext(),
                "Workout logged: $durationMinutes mins at $selectedTime, Intensity: $selectedIntensity",
                Toast.LENGTH_LONG
            ).show()
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(SearchWorkoutFragment(), "SearchWorkoutFragment")
            }
        })
        hourPicker.minValue = 1
        hourPicker.maxValue = 12
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        amPmPicker.minValue = 0
        amPmPicker.maxValue = 1
        amPmPicker.displayedValues = arrayOf("AM", "PM")
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
                updateNumberPickerText(amPmPicker)
            }
        }
        val timeListener = NumberPicker.OnValueChangeListener { _, _, _ ->
            val hours = hourPicker.value
            val minutes = minutePicker.value.toString().padStart(2, '0') // Ensure two digits
            val amPm = amPmPicker.displayedValues[amPmPicker.value]
            selectedTime = "$hours:$minutes $amPm"
            refreshPickers()
        }

        hourPicker.setOnValueChangedListener(timeListener)
        minutePicker.setOnValueChangedListener(timeListener)
        amPmPicker.setOnValueChangedListener(timeListener)
        refreshPickers()

        intensityProgressBar.progress = 0.0f
        intensityProgressBar.setOnProgressChangedListener { _ ->
            selectedIntensity = intensityProgressBar.getCurrentIntensity()
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