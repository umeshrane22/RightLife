package com.example.rlapp.ui.questionnaire.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rlapp.ui.questionnaire.fragment.AnexityPowerFragment
import com.example.rlapp.ui.questionnaire.fragment.BedWakeupTimeFragment
import com.example.rlapp.ui.questionnaire.fragment.OverthinkingYourselfFragment
import com.example.rlapp.ui.questionnaire.fragment.QualityOfSleepFragment
import com.example.rlapp.ui.questionnaire.fragment.RelaxAndUnwindFragment
import com.example.rlapp.ui.questionnaire.fragment.ShakeOffBadDayFragment
import com.example.rlapp.ui.questionnaire.fragment.SleepTimeFragment
import com.example.rlapp.ui.questionnaire.fragment.SocialInteractionFragment
import com.example.rlapp.ui.questionnaire.fragment.WhatsInYourMindFragment

class QuestionnaireThinkRightPagerAdapter(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {
    private val fragmentList = ArrayList<String>()

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ShakeOffBadDayFragment()
            1 -> WhatsInYourMindFragment()
            2 -> AnexityPowerFragment()
            3 -> OverthinkingYourselfFragment()
            4 -> SocialInteractionFragment()
            5 -> RelaxAndUnwindFragment()
            6 -> QualityOfSleepFragment()
            7 -> SleepTimeFragment()
            8 -> BedWakeupTimeFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }

    fun setQuestionnaireData(list: ArrayList<String>) = fragmentList.addAll(list)

    fun removeItem(name: String) = fragmentList.remove(name)

}