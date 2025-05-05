package com.jetsynthesys.rightlife.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.databinding.ActivityEmailNotificationsBinding
import com.jetsynthesys.rightlife.ui.CommonAPICall

class EmailNotificationsNewActivity : BaseActivity() {

    private lateinit var binding: ActivityEmailNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailNotificationsBinding.inflate(layoutInflater)
        setChildContentView(binding.root)

        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        CommonAPICall.getNotificationSettings(this) { data ->
            binding.newsletterSwitch.isChecked = data.newsLetter == true
            binding.promotionalOffersSwitch.isChecked = data.promotionalOffers == true
        }

        binding.newsletterSwitch.setOnCheckedChangeListener { _, isChecked ->
            val requestBody = mapOf("newsLetter" to isChecked)
            CommonAPICall.updateNotificationSettings(this, requestBody) { result, message ->
                showToast(message)
                if (!result) binding.newsletterSwitch.isChecked = !isChecked
            }
        }

        binding.promotionalOffersSwitch.setOnCheckedChangeListener { _, isChecked ->
            val requestBody = mapOf("promotionalOffers" to isChecked)
            CommonAPICall.updateNotificationSettings(this, requestBody) { result, message ->
                showToast(message)
                if (!result) binding.promotionalOffersSwitch.isChecked = !isChecked
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}