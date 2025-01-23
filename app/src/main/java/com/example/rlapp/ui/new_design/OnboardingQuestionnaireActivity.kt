package com.example.rlapp.ui.new_design

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.rlapp.R

class OnboardingQuestionnaireActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    lateinit var tvSkip: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_questionnaire)

        progressBar = findViewById(R.id.progress_bar_onboarding)
        viewPager = findViewById(R.id.viewPagerOnboarding)
        viewPager.isUserInputEnabled = false

        val ivBack = findViewById<ImageView>(R.id.icon_back)
        ivBack.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem == 0) {
                finish()
            } else {
                navigateToPreviousPage()
            }
        }

        tvSkip = findViewById(R.id.tv_skip)
        tvSkip.setOnClickListener {
            if (viewPager.currentItem == 0){
                adapter.removeItem("BodyFatSelection")
            }
            navigateToNextPage()
        }

        adapter = OnBoardingPagerAdapter(this)


        val fragmentList = ArrayList<String>()

        fragmentList.add("GenderSelection")
        fragmentList.add("AgeSelection")
        fragmentList.add("HeightSelection")
        fragmentList.add("WeightSelection")
        fragmentList.add("BodyFatSelection")
        fragmentList.add("TargetWeightSelection")
        fragmentList.add("StressManagementSelection")
        fragmentList.add("HealthGoalFragment")
        adapter.setData(fragmentList)

        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateProgress(position)
            }
        })

    }


    private fun updateProgress(fragmentIndex: Int) {
        // Set progress percentage based on the current fragment (out of 8)
        val progressPercentage =
            (((fragmentIndex + 1) / adapter.itemCount.toDouble()) * 100).toInt()
        progressBar.progress = progressPercentage
    }

    companion object {
        private lateinit var viewPager: ViewPager2
        private lateinit var adapter: OnBoardingPagerAdapter

        // This is a static-like function
        fun navigateToPreviousPage() {
            if (viewPager.currentItem > 0) {
                viewPager.currentItem -= 1
            }
        }

        fun navigateToNextPage() {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            }
        }
    }

}