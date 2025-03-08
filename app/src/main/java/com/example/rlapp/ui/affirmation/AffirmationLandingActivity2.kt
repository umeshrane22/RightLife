package com.example.rlapp.ui.affirmation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R

class AffirmationLandingActivity2: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_affirmation_landing2)

        val btnGetStarted = findViewById<Button>(R.id.btnGetStarted)
        btnGetStarted.setOnClickListener {
            startActivity(
                Intent(
                    this@AffirmationLandingActivity2,
                    TodaysAffirmationActivity::class.java
                )
            )
            finish()
        }
    }
}