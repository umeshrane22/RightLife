package com.jetsynthesys.rightlife.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.databinding.ActivityExportHealthDataBinding
import com.jetsynthesys.rightlife.databinding.ActivityPreparingExportHealthDataBinding

class PreparingExportHealthDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreparingExportHealthDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreparingExportHealthDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
