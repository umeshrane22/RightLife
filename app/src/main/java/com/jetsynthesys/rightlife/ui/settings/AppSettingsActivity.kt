package com.jetsynthesys.rightlife.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.databinding.ActivityAppSettingsBinding
import com.jetsynthesys.rightlife.ui.settings.adapter.SettingsAdapter
import com.jetsynthesys.rightlife.ui.settings.pojo.SettingItem

class AppSettingsActivity : BaseActivity() {

    private lateinit var binding: ActivityAppSettingsBinding
    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppSettingsBinding.inflate(layoutInflater)
        setChildContentView(binding.root)

        setupAppSettingsRecyclerView()

        //back button
        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupAppSettingsRecyclerView() {
        val settingsItems = listOf(
            SettingItem("Notifications"),
            SettingItem("Export All Health Data"),
            SettingItem("Integrations"),
            SettingItem("Account Privacy")
        )

        settingsAdapter = SettingsAdapter(settingsItems) { item ->
            when (item.title) {
                "Notifications" -> {
                    startActivity(Intent(this, NotificationsNewActivity::class.java))
                }

                "Export All Health Data" -> {
                    startActivity(Intent(this, ExportHealthDataActivity::class.java))
                }

                "Integrations" -> {
                    startActivity(Intent(this, IntegrationsNewActivity::class.java))
                }
                "Account Privacy" -> {
                    startActivity(Intent(this, AccountPrivacyNewActivity::class.java))
                }
            }
        }

        binding.rvAppSettings.apply {
            layoutManager = LinearLayoutManager(this@AppSettingsActivity)
            adapter = settingsAdapter
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}