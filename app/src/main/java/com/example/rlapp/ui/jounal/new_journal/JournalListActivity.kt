package com.example.rlapp.ui.jounal.new_journal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.databinding.ActivityJournalListBinding
import com.example.rlapp.ui.utility.SharedPreferenceManager

class JournalListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJournalListBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

    }
}