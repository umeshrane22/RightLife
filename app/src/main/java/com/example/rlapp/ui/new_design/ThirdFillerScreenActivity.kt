package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R

class ThirdFillerScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thiird_filler_screen)

        val header = intent.getStringExtra("WellnessFocus")
        val btnContinue = findViewById<Button>(R.id.btn_continue)

        btnContinue.setOnClickListener {
            val intent = Intent(this, YourInterestActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            startActivity(intent)
            finish()
        }
    }
}