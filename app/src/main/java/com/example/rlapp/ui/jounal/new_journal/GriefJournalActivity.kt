package com.example.rlapp.ui.jounal.new_journal

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.databinding.ActivityGriefBinding
import com.example.rlapp.ui.utility.SharedPreferenceManager

class GriefJournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGriefBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private lateinit var journalItem: JournalItem
    private var questionsList: ArrayList<Question> = ArrayList()
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGriefBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        journalItem = intent.getSerializableExtra("Section") as JournalItem
        questionsList = intent.getSerializableExtra("QuestionList") as ArrayList<Question>
        position = intent.getIntExtra("Position", 0)

        binding.tvPrompt.text = questionsList[position].question

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnInfo.setOnClickListener {
            // Show tooltip or info dialog
        }

        binding.ivRefresh.setOnClickListener {
            if (questionsList.isNotEmpty()) {
                if (questionsList.size - 1 == position)
                    position = 0
                else
                    position += 1
                binding.tvPrompt.text = questionsList[position].question
            }
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
                Intent(this@GriefJournalActivity, Journal4QuestionsActivity::class.java).apply {
                    putExtra("Section", journalItem)
                    putExtra("Answer", binding.etJournalEntry.text.toString())
                    putExtra("QuestionId", questionsList[position].id)
                }
            startActivity(intent)

        }
    }
}
