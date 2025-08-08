package com.jetsynthesys.rightlife.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.ui.utility.AnalyticsEvent
import com.jetsynthesys.rightlife.ui.utility.AnalyticsLogger
import com.jetsynthesys.rightlife.ui.utility.AnalyticsParam
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager

class PersonalisationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChildContentView(R.layout.activity_personalisation)

        var header = intent.getStringExtra("WellnessFocus")
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        if (header.isNullOrEmpty()) {
            header = sharedPreferenceManager.selectedWellnessFocus
        }

        AnalyticsLogger.logEvent(
            AnalyticsEvent.ALLOW_PERSONALISATION_VISIT,
            mapOf(
                AnalyticsParam.USER_ID to sharedPreferenceManager.userId,
                AnalyticsParam.TIMESTAMP to System.currentTimeMillis(),
                AnalyticsParam.GOAL to sharedPreferenceManager.selectedOnboardingModule,
                AnalyticsParam.SUB_GOAL to sharedPreferenceManager.selectedOnboardingSubModule
            )
        )

        val tvSkip = findViewById<TextView>(R.id.tv_skip)
        tvSkip.setOnClickListener {
            sharedPreferenceManager.allowPersonalization = true
            sharedPreferenceManager.onBoardingQuestion = true
            val intent = Intent(this, EnableNotificationActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            startActivity(intent)
            AnalyticsLogger.logEvent(
                AnalyticsEvent.ALLOW_PERSONALISATION_SKIP_CLICK,
                mapOf(
                    AnalyticsParam.USER_ID to sharedPreferenceManager.userId,
                    AnalyticsParam.TIMESTAMP to System.currentTimeMillis(),
                    AnalyticsParam.GOAL to sharedPreferenceManager.selectedOnboardingModule,
                    AnalyticsParam.SUB_GOAL to sharedPreferenceManager.selectedOnboardingSubModule
                )
            )
        }

        val btnAllowPersonalisation = findViewById<Button>(R.id.btn_allow_personalisation)
        btnAllowPersonalisation.setOnClickListener {
            val intent = Intent(this, OnboardingQuestionnaireActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            sharedPreferenceManager.allowPersonalization = true
            startActivity(intent)
            AnalyticsLogger.logEvent(
                AnalyticsEvent.ALLOW_PERSONALISATION_CLICK,
                mapOf(
                    AnalyticsParam.USER_ID to sharedPreferenceManager.userId,
                    AnalyticsParam.TIMESTAMP to System.currentTimeMillis(),
                    AnalyticsParam.GOAL to sharedPreferenceManager.selectedOnboardingModule,
                    AnalyticsParam.SUB_GOAL to sharedPreferenceManager.selectedOnboardingSubModule
                )
            )
        }
    }
}