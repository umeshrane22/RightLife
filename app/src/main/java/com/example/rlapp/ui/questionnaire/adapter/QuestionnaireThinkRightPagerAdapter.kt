package com.example.rlapp.ui.questionnaire.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rlapp.ui.questionnaire.fragment.AnexityPowerFragment
import com.example.rlapp.ui.questionnaire.fragment.BedWakeupTimeFragment
import com.example.rlapp.ui.questionnaire.fragment.BeforeGoingToBedFragment
import com.example.rlapp.ui.questionnaire.fragment.EmotionsPastWeekFragment
import com.example.rlapp.ui.questionnaire.fragment.FeelAfterWakingFragment
import com.example.rlapp.ui.questionnaire.fragment.OverthinkingYourselfFragment
import com.example.rlapp.ui.questionnaire.fragment.QualityOfSleepFragment
import com.example.rlapp.ui.questionnaire.fragment.RelaxAndUnwindFragment
import com.example.rlapp.ui.questionnaire.fragment.ShakeOffBadDayFragment
import com.example.rlapp.ui.questionnaire.fragment.SleepSelectionFragment
import com.example.rlapp.ui.questionnaire.fragment.SleepTimeFragment
import com.example.rlapp.ui.questionnaire.fragment.SocialInteractionFragment
import com.example.rlapp.ui.questionnaire.fragment.WhatsInYourMindFragment

class QuestionnaireThinkRightPagerAdapter(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {
    private val fragmentList = ArrayList<String>()

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EmotionsPastWeekFragment()
            1 -> ShakeOffBadDayFragment()
            2 -> WhatsInYourMindFragment()
            3 -> AnexityPowerFragment()
            4 -> OverthinkingYourselfFragment()
            5 -> SocialInteractionFragment()
            6 -> RelaxAndUnwindFragment()
            7 -> QualityOfSleepFragment()
            8 -> SleepTimeFragment()
            9 -> BedWakeupTimeFragment()
            10 -> SleepSelectionFragment()
            11 -> FeelAfterWakingFragment()
            12 -> BeforeGoingToBedFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }

    fun setQuestionnaireData(list: ArrayList<String>) = fragmentList.addAll(list)

    fun removeItem(name: String) = fragmentList.remove(name)

}