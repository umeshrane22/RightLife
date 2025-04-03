package com.jetsynthesys.rightlife.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.R

class OnboardingFinalActivity : AppCompatActivity() {

    private lateinit var imageFinalOnboarding: ImageView
    private lateinit var imageRefresh: ImageView
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var tvRefresh: TextView
    private lateinit var llOnboardingFinal1: LinearLayout
    private lateinit var llOnboardingFinal2: LinearLayout
    private lateinit var llOnboardingFinal3: LinearLayout
    private lateinit var llOnboardingFinal4: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_final)

        imageFinalOnboarding = findViewById(R.id.img_final_onboarding)
        imageRefresh = findViewById(R.id.img_refresh)
        textView1 = findViewById(R.id.textview1)
        textView2 = findViewById(R.id.textview2)
        tvRefresh = findViewById(R.id.tv_refresh)
        llOnboardingFinal1 = findViewById(R.id.ll_final_onboarding1)
        llOnboardingFinal2 = findViewById(R.id.ll_final_onboarding2)
        llOnboardingFinal3 = findViewById(R.id.ll_final_onboarding3)
        llOnboardingFinal4 = findViewById(R.id.ll_final_onboarding4)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            changeUI(1)
            handler.postDelayed({
                changeUI(2)
                handler.postDelayed({
                    changeUI(3)
                    startActivity(Intent(this@OnboardingFinalActivity, WelcomeActivity::class.java))
                }, 1000)
            }, 1000)
        }, 1000)

    }

    private fun changeUI(position: Int) {
        when (position) {
            1 -> {
                imageRefresh.setImageResource(R.drawable.icon_refresh50)
                tvRefresh.text = "50 %"
                llOnboardingFinal2.visibility = View.VISIBLE
                imageFinalOnboarding.setImageResource(R.drawable.final_onboarding2)
            }

            2 -> {
                imageRefresh.setImageResource(R.drawable.icon_refresh70)
                tvRefresh.text = "70 %"
                llOnboardingFinal2.visibility = View.VISIBLE
                llOnboardingFinal3.visibility = View.VISIBLE
                imageFinalOnboarding.setImageResource(R.drawable.final_onboarding3)
            }

            3 -> {
                imageRefresh.setImageResource(R.drawable.icon_refresh70)
                tvRefresh.text = "100 %"
                llOnboardingFinal2.visibility = View.VISIBLE
                llOnboardingFinal3.visibility = View.VISIBLE
                llOnboardingFinal4.visibility = View.VISIBLE
                imageFinalOnboarding.setImageResource(R.drawable.final_onboarding4)
            }

        }

    }
}