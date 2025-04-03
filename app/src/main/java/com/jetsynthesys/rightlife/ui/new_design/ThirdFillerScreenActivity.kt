package com.jetsynthesys.rightlife.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ui.utility.AppConstants
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager

class ThirdFillerScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thiird_filler_screen)

        var header = intent.getStringExtra("WellnessFocus")
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        if (header.isNullOrEmpty()) {
            header = sharedPreferenceManager.selectedWellnessFocus
        }
        val btnContinue = findViewById<Button>(R.id.btn_continue)

        var originalText = getString(R.string.third_filler_screen5_thinkright)

        val tvFillerText1 = findViewById<TextView>(R.id.tvFillerText1)
        val tvFillerText2 = findViewById<TextView>(R.id.tvFillerText2)
        val tvFillerText3 = findViewById<TextView>(R.id.tvFillerText3)
        val tvFillerText4 = findViewById<TextView>(R.id.tv_filler_screen)
        val tvFillerText5 = findViewById<TextView>(R.id.tv_filler_screen1)

        val ivFillerImage1 = findViewById<ImageView>(R.id.ivFillerImage1)
        val ivFillerImage2 = findViewById<ImageView>(R.id.ivFillerImage2)
        val ivFillerImage3 = findViewById<ImageView>(R.id.ivFillerImage3)

        when (header) {
            AppConstants.EAT_RIGHT, "EAT_RIGHT" -> {
                ivFillerImage1.setImageResource(R.mipmap.fillerscreen1_thinkright)
                ivFillerImage2.setImageResource(R.mipmap.fillerscreen2_eatright)
                ivFillerImage3.setImageResource(R.drawable.fillerscreen3_eatright)

                tvFillerText1.setText(R.string.third_filler_screen1_eatright)
                tvFillerText2.setText(R.string.third_filler_screen2_eatright)
                tvFillerText3.setText(R.string.third_filler_screen3_eatright)
                tvFillerText4.setText(R.string.third_filler_screen4_eatright)

                originalText = getString(R.string.third_filler_screen5_eatright)
            }

            AppConstants.THINK_RIGHT, "THINK_RIGHT" -> {
                ivFillerImage1.setImageResource(R.mipmap.fillerscreen1_thinkright)
                ivFillerImage2.setImageResource(R.mipmap.fillerscreen2_thinkright)
                ivFillerImage3.setImageResource(R.mipmap.fillerscreen3_thinkright)

                tvFillerText1.setText(R.string.third_filler_screen1_thinkright)
                tvFillerText2.setText(R.string.third_filler_screen2_thinkright)
                tvFillerText3.setText(R.string.third_filler_screen3_thinkright)
                tvFillerText4.setText(R.string.third_filler_screen4_thinkright)

                originalText = getString(R.string.third_filler_screen5_thinkright)
            }

            AppConstants.SLEEP_RIGHT, "SLEEP_RIGHT" -> {
                ivFillerImage1.setImageResource(R.mipmap.fillerscreen1_sleepright)
                ivFillerImage2.setImageResource(R.mipmap.fillerscreen2_sleepright)
                ivFillerImage3.setImageResource(R.mipmap.fillerscreen3_sleepright)

                tvFillerText1.setText(R.string.third_filler_screen1_sleepright)
                tvFillerText2.setText(R.string.third_filler_screen2_sleepright)
                tvFillerText3.setText(R.string.third_filler_screen3_sleepright)
                tvFillerText4.setText(R.string.third_filler_screen4_sleepright)
                originalText = getString(R.string.third_filler_screen5_sleepright)
            }

            AppConstants.MOVE_RIGHT, "MOVE_RIGHT" -> {
                ivFillerImage1.setImageResource(R.mipmap.fillerscreen1_moveright)
                ivFillerImage2.setImageResource(R.mipmap.fillerscreen1_thinkright)
                ivFillerImage3.setImageResource(R.mipmap.fillerscreen3_moveright)

                tvFillerText1.setText(R.string.third_filler_screen1_moveright)
                tvFillerText2.setText(R.string.third_filler_screen2_moveright)
                tvFillerText3.setText(R.string.third_filler_screen3_moveright)
                tvFillerText4.setText(R.string.third_filler_screen4_moveright)

                originalText = getString(R.string.third_filler_screen5_moveright)
            }

            else -> {

            }
        }

        val start = originalText.indexOf("RightLife")
        val end = start + "RightLife".length

        val spannable = SpannableString(originalText)

        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.rightlife)),
            start,                         // Start index of "RightLife"
            end,                           // End index of "RightLife"
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvFillerText5.text = spannable

        btnContinue.setOnClickListener {
            val intent = Intent(this, YourInterestActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            sharedPreferenceManager.thirdFiller = true
            startActivity(intent)
            //finish()
        }
    }

}