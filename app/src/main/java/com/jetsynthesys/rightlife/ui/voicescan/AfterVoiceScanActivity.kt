package com.jetsynthesys.rightlife.ui.voicescan

import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.R

class AfterVoiceScanActivity : AppCompatActivity() {

    private val nextString = "We respect your privacy. \nYour data is stored securely."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_voicescan)
        val image: ImageView = findViewById(R.id.image_view)
        val textView: TextView = findViewById(R.id.tv_selected_topic)

        Handler().postDelayed({
            image.setImageResource(R.drawable.layer_1_1)
            textView.text = nextString
        }, 2000)
    }
}