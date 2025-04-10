package com.jetsynthesys.rightlife.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.databinding.ActivityExportHealthDataBinding

class ExportHealthDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExportHealthDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportHealthDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnExport.setOnClickListener {
            startActivity(Intent(this, PreparingExportHealthDataActivity::class.java))
            finish()
        }
    }
}
