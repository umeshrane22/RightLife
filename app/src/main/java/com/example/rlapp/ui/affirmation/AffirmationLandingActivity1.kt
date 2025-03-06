package com.example.rlapp.ui.affirmation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R

class AffirmationLandingActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_affirmation_landing1)

        val btnGetStarted = findViewById<Button>(R.id.btnGetStarted)
        btnGetStarted.setOnClickListener {
            startActivity(
                Intent(
                    this@AffirmationLandingActivity1,
                    AffirmationLandingActivity2::class.java
                )
            )
            finish()
        }
    }
}