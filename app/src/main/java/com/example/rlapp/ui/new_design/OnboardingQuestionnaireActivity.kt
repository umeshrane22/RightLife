package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.ui.new_design.pojo.OnboardingQuestionRequest
import com.example.rlapp.ui.new_design.pojo.SaveUserInterestResponse
import com.example.rlapp.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnboardingQuestionnaireActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    lateinit var tvSkip: TextView
    lateinit var tv_fragment_count: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_questionnaire)

        val header = intent.getStringExtra("WellnessFocus")

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

        tv_fragment_count = findViewById(R.id.tv_fragment_count)
        tvSkip = findViewById(R.id.tv_skip)
        tvSkip.setOnClickListener {
            if (viewPager.currentItem == 0) {
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
        adapter.setHeader(header!!)

        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateProgress(position)
            }
        })

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentItem = viewPager.currentItem
                if (currentItem == 0) {
                    finish()
                } else {
                    navigateToPreviousPage()
                }
            }

        })

    }


    private fun updateProgress(fragmentIndex: Int) {
        // Set progress percentage based on the current fragment (out of 8)
        val progressPercentage =
            (((fragmentIndex + 1) / adapter.itemCount.toDouble()) * 100).toInt()
        progressBar.progress = progressPercentage
        tv_fragment_count.text = "${fragmentIndex + 1}/8"
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

    fun submitAnswer(onboardingQuestionRequest: OnboardingQuestionRequest) {
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val call = apiService.submitOnBoardingAnswers(authToken, onboardingQuestionRequest)

        call.enqueue(object : Callback<SaveUserInterestResponse> {
            override fun onResponse(
                call: Call<SaveUserInterestResponse>,
                response: Response<SaveUserInterestResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()
                    //Handler(Looper.getMainLooper()).postDelayed({
                    // Submit Questions answer here
                    if (viewPager.currentItem == adapter.itemCount - 1) {
                        startActivity(
                            Intent(
                                this@OnboardingQuestionnaireActivity,
                                AwesomeScreenActivity::class.java
                            )
                        )
                        finishAffinity()
                        SharedPreferenceManager.getInstance(this@OnboardingQuestionnaireActivity)
                            .clearOnboardingQuestionRequest()
                    } else {
                        navigateToNextPage()
                    }

                    //}, 1000)

                } else {
                    Toast.makeText(
                        this@OnboardingQuestionnaireActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToNextPage()
                }
            }

            override fun onFailure(call: Call<SaveUserInterestResponse>, t: Throwable) {
                Toast.makeText(
                    this@OnboardingQuestionnaireActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
                navigateToNextPage()
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateToPreviousPage()
    }

}