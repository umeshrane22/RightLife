package com.jetsynthesys.rightlife.ui.breathwork


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.databinding.ActivityBreathworkSessionBinding
import com.jetsynthesys.rightlife.ui.CommonAPICall
import com.jetsynthesys.rightlife.ui.breathwork.pojo.BreathingData
import java.time.Instant
import java.time.format.DateTimeFormatter


class BreathworkSessionActivity : BaseActivity() {


    private lateinit var binding: ActivityBreathworkSessionBinding
    private var sessionCount = 1 // Default session count
    private var breathingData: BreathingData? = null
    private var startDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreathworkSessionBinding.inflate(layoutInflater)
        setChildContentView(binding.root)

        // Retrieve the selected breathing practice from the intent

        breathingData = intent.getSerializableExtra("BREATHWORK") as BreathingData
        startDate = intent.getStringExtra("StartDate").toString()
        if (startDate.isEmpty())
            startDate = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

        binding.ivPlus.setImageResource(
            if (breathingData?.isAddedToToolKit!!) R.drawable.greentick else R.drawable.ic_breathing_toolkit
        )

        setupUI()
        setupListeners(breathingData!!)
        calculateSessiontime()
    }

    private fun setupUI() {
        // Set initial session count
        binding.tvSessionCount.text = sessionCount.toString()
        binding.tvTitle.text = breathingData?.title
        binding.tvDescription.text = breathingData?.subTitle

        Glide.with(this)
            .load(ApiClient.CDN_URL_QA + breathingData?.thumbnail)
            .placeholder(R.drawable.rl_placeholder)
            .error(R.drawable.rl_placeholder)
            .into(binding.ivBreathworkImage)
    }

    private fun setupListeners(breathWorData: BreathingData) {
        binding.ivBack.setOnClickListener { finish() }

        binding.btnMinus.setOnClickListener {
            if (sessionCount > 1) {
                sessionCount--
                binding.tvSessionCount.text = sessionCount.toString()
                calculateSessiontime()
            }
        }

        binding.btnPlus.setOnClickListener {
            sessionCount++
            binding.tvSessionCount.text = sessionCount.toString()
            calculateSessiontime()
        }

        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, BreathworkPracticeActivity::class.java)
            intent.putExtra("sessionCount", sessionCount*4)
            intent.putExtra("BREATHWORK", breathingData)
            intent.putExtra("StartDate", startDate)
            //intent.putExtra("ITEM_DESCRIPTION", selectedItem.description)

            startActivity(intent)

        }

        binding.switchHaptic.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Haptic Feedback Enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Haptic Feedback Disabled", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivPlus.setOnClickListener {
            CommonAPICall.addToToolKit(
                this@BreathworkSessionActivity,
                breathingData?.id,
                !breathingData?.isAddedToToolKit!!
            )
            breathingData?.isAddedToToolKit = !breathingData?.isAddedToToolKit!!
            binding.ivPlus.setImageResource(
                if (breathingData?.isAddedToToolKit!!) R.drawable.greentick else R.drawable.ic_breathing_toolkit
            )
        }
    }

    fun calculateSessiontime() {
        var totalSets = sessionCount
        var inhaleTime = breathingData?.breathInhaleTime?.toLong()!! * 1000
        var exhaleTime = breathingData?.breathExhaleTime?.toLong()!! * 1000
        var holdTime = breathingData?.breathHoldTime?.toLong()!! * 1000

        // Calculate session duration based on the selected practice
        val cycleDuration = inhaleTime +
                holdTime +
                exhaleTime +
                (if (breathingData?.title == "Box Breathing") holdTime else 0L)
        var sessionDurationSeconds = (totalSets * cycleDuration / 1000).toInt()

        // Set initial values

        updateSessionTimer(sessionDurationSeconds * 4 * 1000L)
    }

    private fun updateSessionTimer(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000) / 60
        val seconds = (millisUntilFinished / 1000) % 60
        binding.tvSettime.text = String.format("%02d:%02d", minutes, seconds)
    }
}
