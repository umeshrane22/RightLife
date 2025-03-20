package com.example.rlapp.ui.breathwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity



import android.os.Bundle
import android.widget.Toast
import com.example.rlapp.databinding.ActivityBreathworkSessionBinding


class BreathworkSessionActivity : AppCompatActivity() {



    private lateinit var binding: ActivityBreathworkSessionBinding
    private var sessionCount = 3 // Default session count
    private var practiceType ="ALTERNATE_NOSTRIL";
    private var itemTitle ="";
    private var itemDescription ="";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreathworkSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the selected breathing practice from the intent
         practiceType = intent.getStringExtra("PRACTICE_TYPE") ?: "ALTERNATE_NOSTRIL"
        itemTitle = intent.getStringExtra("ITEM_TITLE") ?: ""
         itemDescription = intent.getStringExtra("ITEM_DESCRIPTION") ?: ""
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        // Set initial session count
        binding.tvSessionCount.text = sessionCount.toString()
        binding.tvTitle.text = itemTitle
        binding.tvDescription.text = itemDescription
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
            Toast.makeText(this, "Session Started with $sessionCount sets!", Toast.LENGTH_SHORT).show()
            // Navigate to actual breathing session logic here

            val intent = Intent(this, BreathworkPracticeActivity::class.java)
            intent.putExtra("sessionCount", sessionCount)
            intent.putExtra("PRACTICE_TYPE", practiceType)
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
