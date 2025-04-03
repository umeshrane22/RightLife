package com.jetsynthesys.rightlife.ui.jounal.new_journal

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.databinding.ActivityGriefBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager

class GriefJournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGriefBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private var journalItem: JournalItem? = JournalItem()
    private var journalEntry: JournalEntry? = JournalEntry()
    private var questionsList: ArrayList<Question>? = ArrayList()
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGriefBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        journalItem = intent.getSerializableExtra("Section") as? JournalItem
        journalEntry = intent.getSerializableExtra("JournalEntry") as? JournalEntry
        questionsList = intent.getSerializableExtra("QuestionList") as? ArrayList<Question>
        position = intent.getIntExtra("Position", 0)

        if (questionsList?.isNotEmpty() == true) {
            binding.tvPrompt.text = questionsList?.get(position)?.question
        } else {
            binding.tvPrompt.text = journalEntry?.question
        }

        journalEntry?.let {
            binding.etJournalEntry.setText(it.answer)
        }

        binding.btnSave.setTextColor(
            if (binding.etJournalEntry.text.isNotEmpty()) 0xFF984C01.toInt() else 0xFFBFBFBF.toInt()
        )
        binding.btnSave.isEnabled = binding.etJournalEntry.text.isNotEmpty()

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
            if (questionsList?.isNotEmpty() == true) {
                if (questionsList!!.size - 1 == position)
                    position = 0
                else
                    position += 1
                binding.tvPrompt.text = questionsList!![position].question
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
                    if (journalEntry != null)
                        putExtra("QuestionId", journalEntry?.questionId)
                    else
                        putExtra("QuestionId", questionsList?.get(position)?.id)
                    putExtra("JournalEntry", journalEntry)
                }
            startActivity(intent)

        }
    }
}
