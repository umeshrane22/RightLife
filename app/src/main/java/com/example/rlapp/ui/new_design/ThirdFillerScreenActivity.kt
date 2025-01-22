package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R

class ThirdFillerScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thiird_filler_screen)

        val header = intent.getStringExtra("WellnessFocus")
        val btnContinue = findViewById<Button>(R.id.btn_continue)

        val originalText = getString(R.string.third_filler_screen5)

        val spannable = SpannableString(originalText)

        val start = originalText.indexOf("RightLife")
        val end = start + "RightLife".length

        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.rightlife)),
            start,                         // Start index of "RightLife"
            end,                           // End index of "RightLife"
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val textView = findViewById<TextView>(R.id.tv_filler_screen1)

        textView.text = spannable

        btnContinue.setOnClickListener {
            val intent = Intent(this, YourInterestActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            startActivity(intent)
            finish()
        }
    }
}