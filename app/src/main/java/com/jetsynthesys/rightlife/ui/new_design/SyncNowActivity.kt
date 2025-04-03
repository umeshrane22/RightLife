package com.jetsynthesys.rightlife.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager

class SyncNowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync_now)

        var header = intent.getStringExtra("WellnessFocus")
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        if (header.isNullOrEmpty()){
            header = sharedPreferenceManager.selectedWellnessFocus
        }

        val btnSyncNow = findViewById<LinearLayout>(R.id.btn_sync_now)
        val btnSkipForNOw = findViewById<Button>(R.id.btn_skip_for_now)

        btnSyncNow.setOnClickListener {
            val intent = Intent(this, OnboardingQuestionnaireActivity::class.java)
            sharedPreferenceManager.syncNow = true
            intent.putExtra("WellnessFocus", header)
            startActivity(intent)
            //finish()
        }
        btnSkipForNOw.setOnClickListener {
            val intent = Intent(this, OnboardingQuestionnaireActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            sharedPreferenceManager.syncNow = true
            startActivity(intent)
            // finish()
        }
    }
}