package com.jetsynthesys.rightlife.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.databinding.ActivityNotificationsNewBinding
import com.jetsynthesys.rightlife.ui.settings.adapter.SettingsAdapter
import com.jetsynthesys.rightlife.ui.settings.pojo.SettingItem

class NotificationsNewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsNewBinding
    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEmailNotificationsRecyclerView()

        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.pushNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            showToast(if (isChecked) "Now push notification is ON" else "Now push notification is OFF")
        }
    }

    private fun setupEmailNotificationsRecyclerView() {
        val settingsItems = listOf(
            SettingItem("Email Notifications")
        )

        settingsAdapter = SettingsAdapter(settingsItems) { item ->
            when (item.title) {
                "Email Notifications" -> {
                    startActivity(Intent(this,EmailNotificationsNewActivity::class.java))
                }
            }
        }

        binding.rvEmailNotifications.apply {
            layoutManager = LinearLayoutManager(this@NotificationsNewActivity)
            adapter = settingsAdapter
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}