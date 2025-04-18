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
import com.jetsynthesys.rightlife.ai_package.ui.moveright.StepIntake

class SetYourStepGoalFragment : BaseFragment<FragmentSetYourStepGoalBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSetYourStepGoalBinding
        get() = FragmentSetYourStepGoalBinding::inflate

    private lateinit var stepsSliderView: StepIntake
    private lateinit var setTargetButton: TextView
    private lateinit var tvCurrentGoal: TextView

    private var currentGoal = 10500 // Initial goal
    private val maxSteps = 12000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.background = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_color_background_workout)

        // Initialize views
        stepsSliderView = view.findViewById(R.id.steps_slider_view)
        setTargetButton = view.findViewById(R.id.tv_set_target)
        tvCurrentGoal = view.findViewById(R.id.step_count_text_5)

        // Configure StepIntake
        stepsSliderView.setMinSteps(0)
        stepsSliderView.setMaxSteps(maxSteps)
        stepsSliderView.setFillPercentage(currentGoal.toFloat() / maxSteps)
        stepsSliderView.setIntervalColors(ContextCompat.getColor(requireContext(), R.color.green_text))
        stepsSliderView.setOnStepCountChangeListener(object : StepIntake.OnStepCountChangeListener {
            override fun onStepCountChanged(stepCount: Int) {
                currentGoal = stepCount
                updateCurrentGoalText()
            }
        })

        // Set Target Button Click Listener
        setTargetButton.setOnClickListener {
            // Optional: Save currentGoal to SharedPreferences or backend
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val stepGoalFragment = SnapMealFragment()
                val args = Bundle().apply {
                    putInt("stepGoal", currentGoal)
                }
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
        tvCurrentGoal.text = "${currentGoal / 1000}k steps\nYour Goal"
    }
}