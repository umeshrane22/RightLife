package com.example.rlapp.ui.questionnaire.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rlapp.ui.questionnaire.fragment.ActiveDuringSessionsFragment
import com.example.rlapp.ui.questionnaire.fragment.BreaksToStretchFragment
import com.example.rlapp.ui.questionnaire.fragment.EatAffectMoodFragment
import com.example.rlapp.ui.questionnaire.fragment.EnergyLevelFragment
import com.example.rlapp.ui.questionnaire.fragment.ExerciseLocationFragment
import com.example.rlapp.ui.questionnaire.fragment.ExercisePreferenceFragment
import com.example.rlapp.ui.questionnaire.fragment.FastfoodPreferenceFragment
import com.example.rlapp.ui.questionnaire.fragment.FoodPreferenceFragment
import com.example.rlapp.ui.questionnaire.fragment.FoodServingFragment
import com.example.rlapp.ui.questionnaire.fragment.MealPreferenceFragment
import com.example.rlapp.ui.questionnaire.fragment.PhysicalActivitiesFragment
import com.example.rlapp.ui.questionnaire.fragment.SchedulePreferenceFragment
import com.example.rlapp.ui.questionnaire.fragment.StepsTakenFragment
import com.example.rlapp.ui.questionnaire.fragment.WaterCaffeineIntakeFragment

class QuestionnaireEatRightPagerAdapter(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {
    private val fragmentList = ArrayList<String>()

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FoodPreferenceFragment()
            1 -> MealPreferenceFragment()
            2 -> SchedulePreferenceFragment()
            3 -> FoodServingFragment()
            4 -> WaterCaffeineIntakeFragment()
            5 -> FastfoodPreferenceFragment()
            6 -> EatAffectMoodFragment()
            7 -> ExercisePreferenceFragment()
            8 -> ActiveDuringSessionsFragment()
            9 -> PhysicalActivitiesFragment()
            10 -> ExerciseLocationFragment()
            11 -> StepsTakenFragment()
            12 -> EnergyLevelFragment()
            13 -> BreaksToStretchFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }

    fun setQuestionnaireData(list: ArrayList<String>) = fragmentList.addAll(list)

    fun removeItem(name: String) = fragmentList.remove(name)

}