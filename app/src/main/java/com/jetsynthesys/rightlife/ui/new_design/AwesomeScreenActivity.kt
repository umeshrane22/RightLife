package com.jetsynthesys.rightlife.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.R

class AwesomeScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awesome_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, EnableNotificationActivity::class.java))
        }, 1000)
    }
}