package com.jetsynthesys.rightlife.ai_package.ui.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.SnapMealFragment
import com.jetsynthesys.rightlife.ai_package.ui.moveright.MoveRightLandingFragment
import com.jetsynthesys.rightlife.databinding.FragmentSetYourStepGoalBinding
import com.jetsynthesys.rightlife.quiestionscustomviews.CoffeWaterIntakeView
import com.google.android.material.snackbar.Snackbar
import com.jetsynthesys.rightlife.ai_package.ui.moveright.StepIntake

class SetYourStepGoalFragment : BaseFragment<FragmentSetYourStepGoalBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSetYourStepGoalBinding
        get() = FragmentSetYourStepGoalBinding::inflate
    var snackbar: Snackbar? = null

    private lateinit var stepsSliderView: StepIntake
    private lateinit var setTargetButton: TextView
   // private lateinit tvCurrentGoal: TextView // Add this to display current goal dynamically

    private var currentGoal = 10500 // Initial goal
    private val averageSteps = 10000 // Fixed average
    private val recommendedSteps = 12000 // Recommended goal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.background = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_color_background_workout)

        // Initialize views
        stepsSliderView = view.findViewById(R.id.steps_slider_view)
        setTargetButton = view.findViewById(R.id.tv_set_target)
       // tvCurrentGoal = view.findViewById<TextView>(R.id.step_count_text_5) // Reuse 10k text to show current goal

        // Customize CoffeWaterIntakeView
        stepsSliderView.setMinSteps(0)
        stepsSliderView.setMaxSteps(12000)
        stepsSliderView.setFillPercentage((currentGoal.toFloat() / 12000).toFloat())
        stepsSliderView.setIntervalColors(ContextCompat.getColor(requireContext(), R.color.green_text)) // Set green fill
        stepsSliderView.setOnStepCountChangeListener(object : StepIntake.OnStepCountChangeListener {
            override fun onStepCountChanged(stepCount: Int) {
                currentGoal = stepCount
                updateCurrentGoalText()
            }
        })

        // Set Target Button Click Listener
        setTargetButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val stepGoalFragment = SnapMealFragment()
                val args = Bundle()
                stepGoalFragment.arguments = args
                replace(R.id.flFragment, stepGoalFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        // Back Press Handler
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = MoveRightLandingFragment()
                val args = Bundle()
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment, "landing")
                    addToBackStack("landing")
                    commit()
                }
            }
        })

        // Initial text update
        updateCurrentGoalText()
    }

    private fun updateCurrentGoalText() {
        //tvCurrentGoal.text = "${currentGoal / 1000}k steps\nYour Goal" // Display current goal
    }
}