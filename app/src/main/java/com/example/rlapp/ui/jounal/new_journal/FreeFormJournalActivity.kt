package com.example.rlapp.ui.jounal.new_journal

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.databinding.ActivityFreeformBinding
import com.example.rlapp.ui.utility.SharedPreferenceManager

class FreeFormJournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFreeformBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private lateinit var journalItem: JournalItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeformBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        val name = sharedPreferenceManager.userProfile.userdata.firstName
        binding.tvGreeting.text = "Hello $name,\nWhat’s on your mind?"

        journalItem = intent.getSerializableExtra("Section") as JournalItem

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnInfo.setOnClickListener {
            // Show tooltip or info dialog
        }

        binding.etJournalEntry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val hasText = (s?.trim()?.length ?: 0) > 0
                binding.btnSave.isEnabled = hasText
                binding.btnSave.setTextColor(
                    if (hasText) 0xFF984C01.toInt() else 0xFFBFBFBF.toInt()
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.btnSave.setOnClickListener {
            // Save logic here
            val intent =
                Intent(this@FreeFormJournalActivity, Journal4QuestionsActivity::class.java).apply {
                    putExtra("Section", journalItem)
                    putExtra("Answer", binding.etJournalEntry.text.toString())
                }
            startActivity(intent)

        }
    }
}
