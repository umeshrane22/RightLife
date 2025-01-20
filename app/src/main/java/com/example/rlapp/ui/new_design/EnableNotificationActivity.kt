package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R

class EnableNotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enable_notification)

        val btnEnableNotification = findViewById<Button>(R.id.btn_enable_notification)

        btnEnableNotification.setOnClickListener {
            startActivity(Intent(this, OnboardingFinalActivity::class.java))
        }
    }
}