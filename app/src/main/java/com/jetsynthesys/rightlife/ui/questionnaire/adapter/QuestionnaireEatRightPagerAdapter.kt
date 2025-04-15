package com.jetsynthesys.rightlife.ui.questionnaire.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.ActiveDuringSessionsFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.BreaksToStretchFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.EatAffectMoodFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.EnergyLevelFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.ExerciseLocationFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.ExercisePreferenceFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.FastfoodPreferenceFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.FoodPreferenceFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.FoodServingFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.MealPreferenceFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.PhysicalActivitiesFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.SchedulePreferenceFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.StepsTakenFragment
import com.jetsynthesys.rightlife.ui.questionnaire.fragment.WaterCaffeineIntakeFragment
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.Question

class QuestionnaireEatRightPagerAdapter(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {
    private val fragmentList = ArrayList<String>()

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        val question = Question("Q1", "How do you to feel?", "EatRight")
        return when (fragmentList[position]) {
            "FoodPreferenceFragment" -> FoodPreferenceFragment.newInstance(question)
            "MealPreferenceFragment" -> MealPreferenceFragment.newInstance(question)
            "SchedulePreferenceFragment" -> SchedulePreferenceFragment.newInstance(question)
            "FoodServingFragment" -> FoodServingFragment.newInstance(question)
            "WaterCaffeineIntakeFragment" -> WaterCaffeineIntakeFragment.newInstance(question)
            "FastfoodPreferenceFragment" -> FastfoodPreferenceFragment.newInstance(question)
            "EatAffectMoodFragment" -> EatAffectMoodFragment.newInstance(question)
            "ExercisePreferenceFragment" -> ExercisePreferenceFragment.newInstance(question)
            "ActiveDuringSessionsFragment" -> ActiveDuringSessionsFragment.newInstance(question)
            "PhysicalActivitiesFragment" -> PhysicalActivitiesFragment.newInstance(question)
            "ExerciseLocationFragment" -> ExerciseLocationFragment.newInstance(question)
            "StepsTakenFragment" -> StepsTakenFragment.newInstance(question)
            "EnergyLevelFragment" -> EnergyLevelFragment.newInstance(question)
            "BreaksToStretchFragment" -> BreaksToStretchFragment.newInstance(question)
            else -> throw IllegalStateException("Invalid position")
        }
    }

    fun setQuestionnaireData(list: ArrayList<String>) = fragmentList.addAll(list)

    fun removeItem(name: String) = fragmentList.remove(name)

    fun addItem(position: Int, name: String) = fragmentList.add(position, name)

}