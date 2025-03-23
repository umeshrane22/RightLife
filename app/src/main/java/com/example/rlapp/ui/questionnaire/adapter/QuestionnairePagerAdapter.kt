package com.example.rlapp.ui.questionnaire.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rlapp.ui.questionnaire.fragment.ActiveDuringSessionsFragment
import com.example.rlapp.ui.questionnaire.fragment.BreaksToStretchFragment
import com.example.rlapp.ui.questionnaire.fragment.EatAffectMoodFragment
import com.example.rlapp.ui.questionnaire.fragment.ExerciseLocationFragment
import com.example.rlapp.ui.questionnaire.fragment.ExercisePreferenceFragment
import com.example.rlapp.ui.questionnaire.fragment.FastfoodPreferenceFragment
import com.example.rlapp.ui.questionnaire.fragment.FoodServingFragment
import com.example.rlapp.ui.questionnaire.fragment.SchedulePreferenceFragment
import com.example.rlapp.ui.questionnaire.fragment.FoodPreferenceFragment
import com.example.rlapp.ui.questionnaire.fragment.MealPreferenceFragment

class QuestionnairePagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    private val fragmentList = ArrayList<String>()

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FoodPreferenceFragment()
            1 -> MealPreferenceFragment()
            2 -> SchedulePreferenceFragment()
            3 -> FoodServingFragment()
            4 -> FastfoodPreferenceFragment()
            5 -> EatAffectMoodFragment()
            6 -> ExercisePreferenceFragment()
            7 -> ActiveDuringSessionsFragment()
            8 -> ExerciseLocationFragment()
            9 -> BreaksToStretchFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }

    fun setQuestionnaireData(list: ArrayList<String>) = fragmentList.addAll(list)

    fun removeItem(name: String) = fragmentList.remove(name)

}