package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ui.utility.Utils

class UnlockPowerOfYourMindActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock_power_of_your_mind)

        val header = intent.getStringExtra("WellnessFocus")
        val tvHeader = findViewById<TextView>(R.id.tv_header)
        val tvUnlockPower = findViewById<TextView>(R.id.tv_unlock_power)
        val rvUnlockPower = findViewById<RecyclerView>(R.id.rv_unlock_power)
        val btnContinue = findViewById<Button>(R.id.btn_continue)
        tvHeader.text = header

        val selectedColor = Utils.getModuleDarkColor(this, header)
        tvHeader.setTextColor(selectedColor)
        tvUnlockPower.setTextColor(selectedColor)

        val list = ArrayList<UnlockPower>()

        list.add(
            UnlockPower(
                "Visualise and Manifest",
                "Achieve clarity with 500+ uplifting affirmations",
                R.drawable.visualise_manifest
            )
        )
        list.add(
            UnlockPower(
                "Master Stress",
                "Use voice scans to decode mood and stress level daily",
                R.drawable.master_stress
            )
        )
        list.add(
            UnlockPower(
                "Deepen Your Awareness",
                "Guided meditation to calm and balance your thoughts",
                R.drawable.deepen_your_awareness
            )
        )
        list.add(
            UnlockPower(
                "Simplified Journaling",
                "Capture growth and reflect with journaling prompts",
                R.drawable.simplified_journaling
            )
        )
        list.add(
            UnlockPower(
                "Stay Ahead",
                "Access mental health texts to address concerns early",
                R.drawable.stay_ahead
            )
        )

        val unwrappedDrawable =
            AppCompatResources.getDrawable(this, R.drawable.bg_gray_border)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(
            wrappedDrawable,
            Utils.getModuleColor(this, header)
        )

        rvUnlockPower.background = wrappedDrawable
        val unlockPowerAdapter = UnlockPowerAdapter(this, list)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvUnlockPower.layoutManager = linearLayoutManager
        rvUnlockPower.adapter = unlockPowerAdapter

        btnContinue.setOnClickListener {
            val intent = Intent(this, ThirdFillerScreenActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            startActivity(intent)
            clearColor()
            finish()
        }
    }

    private fun clearColor() {
        val unwrappedDrawable =
            AppCompatResources.getDrawable(this, R.drawable.bg_gray_border)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, getColor(R.color.white))
    }
}