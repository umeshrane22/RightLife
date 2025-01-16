package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rlapp.R

class DataControlActivity : AppCompatActivity() {
    private val agreeString =
        "<html>I agree to process my personal data for providing me RightLife App Functions. See more in <a href=\"URL_TO_PRIVACY_POLICY\">Privacy Policy</a>.</html>"
    private val policyTermsCondition =
        "I agree to the <a href=\"URL_TO_PRIVACY_POLICY\">Privacy Policy</a> and <a href=\"URL_TO_TERMS_CONDITIONS\">Terms and Conditions</a>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_control)
        val chkPrivacyPolicy: CheckBox = findViewById(R.id.ck_privacy_policy)
        val chkTermsAndConditions: CheckBox = findViewById(R.id.ck_terms_conditions)

        val tvPrivacyPolicy: TextView = findViewById(R.id.tv_privacy_policy)
        val tvTermsAndConditions: TextView = findViewById(R.id.tv_terms_condition)

        val btnContinue = findViewById<Button>(R.id.btn_continue)

        tvTermsAndConditions.text =
            Html.fromHtml(policyTermsCondition, Html.FROM_HTML_MODE_COMPACT)
        tvPrivacyPolicy.text =
            Html.fromHtml(agreeString, Html.FROM_HTML_MODE_COMPACT)

        val colorStateListEnabled = ContextCompat.getColorStateList(this, R.color.menuselected)
        val colorStateListDisabled = ContextCompat.getColorStateList(this, R.color.rightlife)

        chkPrivacyPolicy.setOnClickListener {
            btnContinue.isEnabled = chkPrivacyPolicy.isChecked && chkTermsAndConditions.isChecked
            btnContinue.backgroundTintList =
                if (btnContinue.isEnabled) colorStateListEnabled else colorStateListDisabled
        }

        chkTermsAndConditions.setOnClickListener {
            btnContinue.isEnabled = chkPrivacyPolicy.isChecked && chkTermsAndConditions.isChecked
            btnContinue.backgroundTintList =
                if (btnContinue.isEnabled) colorStateListEnabled else colorStateListDisabled
        }

        btnContinue.setOnClickListener {
            if (chkPrivacyPolicy.isChecked && chkTermsAndConditions.isChecked) {
                startActivity(Intent(this, ImageSliderActivity::class.java))
                finish()
            }
        }
    }
}