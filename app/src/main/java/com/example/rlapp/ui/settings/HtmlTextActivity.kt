package com.example.rlapp.ui.settings

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.databinding.ActivityHtmlTextBinding

class HtmlTextActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHtmlTextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHtmlTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val string = intent.getStringExtra("GeneralInformation")

        if (!string.isNullOrEmpty())
            binding.tvAnswer.text =
                Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT)
    }
}