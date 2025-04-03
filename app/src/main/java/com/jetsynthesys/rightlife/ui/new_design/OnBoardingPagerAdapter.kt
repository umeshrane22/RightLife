package com.jetsynthesys.rightlife.ui.new_design

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragmentList = ArrayList<String>()
    private lateinit var header: String

    override fun getItemCount(): Int {
        return fragmentList.size // The number of fragments
    }

    override fun createFragment(position: Int): Fragment {
        return when (fragmentList[position]) {
            "GenderSelection" -> GenderSelectionFragment.newInstance(position)
            "AgeSelection" -> AgeSelectionFragment.newInstance(position)
            "HeightSelection" -> HeightSelectionFragment.newInstance(position)
            "WeightSelection" -> WeightSelectionFragment.newInstance(position)
            "BodyFatSelection" -> BodyFatSelectionFragment.newInstance(position)
            "TargetWeightSelection" -> TargetWeightSelectionFragment.newInstance(position)
            "StressManagementSelection" -> StressManagementSelectionFragment.newInstance(
                position,
                header
            )

            "HealthGoalFragment" -> HealthGoalFragment.newInstance(position)

            else -> GenderSelectionFragment.newInstance(0)
        }
    }

    fun setData(list: ArrayList<String>) {
        fragmentList.addAll(list)
    }

    fun removeItem(name: String) {
        fragmentList.remove(name)
    }

    fun setHeader(header: String) {
        this.header = header
    }
}