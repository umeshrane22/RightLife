package com.example.rlapp.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rlapp.databinding.ActivityEmailNotificationsBinding
import com.example.rlapp.databinding.ActivityNotificationsNewBinding
import com.example.rlapp.ui.settings.adapter.SettingsAdapter
import com.example.rlapp.ui.settings.pojo.SettingItem

class EmailNotificationsNewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailNotificationsBinding
    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.newsletterSwitch.setOnCheckedChangeListener { _, isChecked ->
            showToast(if (isChecked) "Newsletter notification is ON" else "Newsletter notification is OFF")
        }

        binding.promotionalOffersSwitch.setOnCheckedChangeListener { _, isChecked ->
            showToast(if (isChecked) "Promotional Offers notification is ON" else "Promotional Offers notification is OFF")
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}