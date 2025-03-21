package com.example.rlapp.ui.profile_new

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.databinding.ActivityDeleteAccountEmailDataBinding

class DeleteAccountEmailDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteAccountEmailDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteAccountEmailDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnContinue.setOnClickListener {
            //API needs to hit here for email data to user
            if (binding.checkBoxSendData.isChecked) {
                finish()
                startActivity(
                    Intent(
                        this@DeleteAccountEmailDataActivity,
                        ProfileSettingsActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        putExtra("start_profile", true)
                    })
            }else{
                Toast.makeText(this, "Please select checkbox", Toast.LENGTH_SHORT).show()
            }
        }
    }
}