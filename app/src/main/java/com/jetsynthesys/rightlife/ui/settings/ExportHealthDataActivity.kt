package com.jetsynthesys.rightlife.ui.settings

import android.os.Bundle
import android.widget.Toast
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

            Toast.makeText(this, "Exporting data...", Toast.LENGTH_SHORT).show()
        }
    }
}
