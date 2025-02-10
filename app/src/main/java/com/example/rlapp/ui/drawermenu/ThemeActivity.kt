package com.example.rlapp.ui.drawermenu

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rlapp.databinding.ActivityThemeBinding
import com.example.rlapp.ui.utility.SharedPreferenceManager

class ThemeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThemeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appMode = SharedPreferenceManager.getInstance(this).appMode

        val listItems = ArrayList<ThemeItem>()
        listItems.add(ThemeItem("System", appMode == "System"))
        listItems.add(ThemeItem("Light", appMode == "Light"))
        listItems.add(ThemeItem("Dark", appMode == "Dark"))

        val adapter = ThemeAdapter(this, listItems) { themeItem ->
            when (themeItem.value) {
                "System" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    SharedPreferenceManager.getInstance(this@ThemeActivity).saveAppMode("System")
                }

                "Light" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    SharedPreferenceManager.getInstance(this@ThemeActivity).saveAppMode("Light")
                }

                "Dark" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    SharedPreferenceManager.getInstance(this@ThemeActivity).saveAppMode("Dark")
                }
            }
        }

        binding.rvTheme.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvTheme.adapter = adapter

    }
}