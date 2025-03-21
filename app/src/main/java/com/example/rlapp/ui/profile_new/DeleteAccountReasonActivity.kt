package com.example.rlapp.ui.profile_new

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.databinding.ActivityDeleteAccountReasonBinding

class DeleteAccountReasonActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDeleteAccountReasonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteAccountReasonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reasonsList = intent.getStringArrayListExtra("SelectedReasons")

        binding.btnCancel.setOnClickListener {
            finish()
            startActivity(
                Intent(
                    this@DeleteAccountReasonActivity,
                    ProfileSettingsActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    putExtra("start_profile", true)
                })
        }

        binding.btnContinue.setOnClickListener {
            if (binding.etMessage.text.isEmpty()){
                Toast.makeText(this,"Please enter message",Toast.LENGTH_SHORT).show()
            }else{
                //delete API call pending
                startActivity(Intent(this,DeleteAccountEmailDataActivity::class.java))
            }
        }
    }
}