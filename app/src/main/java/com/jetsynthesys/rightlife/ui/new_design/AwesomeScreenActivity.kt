package com.jetsynthesys.rightlife.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.BaseActivity

class AwesomeScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChildContentView(R.layout.activity_awesome_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, EnableNotificationActivity::class.java))
        }, 1000)
    }
}