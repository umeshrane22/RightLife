package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R

class SyncNowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync_now)

        val header = intent.getStringExtra("WellnessFocus")

        val btnSyncNow = findViewById<LinearLayout>(R.id.btn_sync_now)
        val btnSkipForNOw = findViewById<Button>(R.id.btn_skip_for_now)

        btnSyncNow.setOnClickListener {
            val intent = Intent(this, OnboardingQuestionnaireActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            startActivity(intent)
            //finish()
        }
        btnSkipForNOw.setOnClickListener {
            val intent = Intent(this, OnboardingQuestionnaireActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            startActivity(intent)
            // finish()
        }
    }
}