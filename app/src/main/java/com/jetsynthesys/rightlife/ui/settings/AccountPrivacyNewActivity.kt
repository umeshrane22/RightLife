package com.jetsynthesys.rightlife.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.databinding.ActivityAccountPrivacyNewBinding
import com.jetsynthesys.rightlife.databinding.ActivityIntegrationsNewBinding
import com.jetsynthesys.rightlife.databinding.ActivityNotificationsNewBinding
import com.jetsynthesys.rightlife.ui.CommonAPICall
import com.jetsynthesys.rightlife.ui.profile_new.DeleteAccountSelectionActivity
import com.jetsynthesys.rightlife.ui.settings.adapter.SettingsAdapter
import com.jetsynthesys.rightlife.ui.settings.pojo.SettingItem

class AccountPrivacyNewActivity : BaseActivity() {

    private lateinit var binding: ActivityAccountPrivacyNewBinding
    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountPrivacyNewBinding.inflate(layoutInflater)
        setChildContentView(binding.root)
        setupEmailNotificationsRecyclerView()

        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    /*    CommonAPICall.getNotificationSettings(this){
            data ->
            binding.pushNotificationsSwitch.isChecked = data.pushNotification == true
        }*/

    }

    private fun setupEmailNotificationsRecyclerView() {
        val settingsItems = listOf(
            SettingItem("Delete Account")
        )

        settingsAdapter = SettingsAdapter(settingsItems) { item ->
            when (item.title) {
                "Delete Account" -> {
                    startActivity(Intent(this, DeleteAccountSelectionActivity::class.java))
                }
            }
        }

        binding.rvEmailNotifications.apply {
            layoutManager = LinearLayoutManager(this@AccountPrivacyNewActivity)
            adapter = settingsAdapter
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}