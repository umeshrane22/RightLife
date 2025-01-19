package com.example.rlapp.ui.new_design

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 8 // The number of fragments
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GenderSelectionFragment.newInstance(position)
            1 -> AgeSelectionFragment.newInstance(position)
            2 -> HeightSelectionFragment.newInstance(position)
            3 -> WeightSelectionFragment.newInstance(position)
            4 -> BodyFatSelectionFragment.newInstance(position)
            5 -> TargetWeightSelectionFragment.newInstance(position)
            6 -> StressManagementSelectionFragment.newInstance(position)
            7 -> HealthGoalFragment.newInstance(position)

            else -> GenderSelectionFragment.newInstance(0)
        }
    }
}