package com.jetsynthesys.rightlife.ui.breathwork


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.databinding.ActivityBreathworkSessionBinding
import com.jetsynthesys.rightlife.ui.breathwork.pojo.BreathingData


class BreathworkSessionActivity : AppCompatActivity() {


    private lateinit var binding: ActivityBreathworkSessionBinding
    private var sessionCount = 3 // Default session count
    private var breathingData: BreathingData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreathworkSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the selected breathing practice from the intent

        breathingData = intent.getSerializableExtra("BREATHWORK") as BreathingData
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        // Set initial session count
        binding.tvSessionCount.text = sessionCount.toString()
        binding.tvTitle.text = breathingData?.title
        binding.tvDescription.text = breathingData?.subTitle
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener { finish() }

        binding.btnMinus.setOnClickListener {
            if (sessionCount > 1) {
                sessionCount--
                binding.tvSessionCount.text = sessionCount.toString()
            }
        }

        binding.btnPlus.setOnClickListener {
            sessionCount++
            binding.tvSessionCount.text = sessionCount.toString()
        }

        binding.btnContinue.setOnClickListener {
            Toast.makeText(this, "Session Started with $sessionCount sets!", Toast.LENGTH_SHORT)
                .show()
            // Navigate to actual breathing session logic here

            val intent = Intent(this, BreathworkPracticeActivity::class.java)
            intent.putExtra("sessionCount", sessionCount)
            intent.putExtra("BREATHWORK", breathingData)
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
            Toast.makeText(this, "Additional Options Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
