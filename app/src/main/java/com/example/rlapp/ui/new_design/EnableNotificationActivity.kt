package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R
import com.example.rlapp.ui.new_design.pojo.LoggedInUser
import com.example.rlapp.ui.utility.SharedPreferenceManager

class EnableNotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enable_notification)

        val btnEnableNotification = findViewById<Button>(R.id.btn_enable_notification)

        val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)



        btnEnableNotification.setOnClickListener {
            var loggedInUsers = sharedPreferenceManager.loggedUserList

            var loggedInUser: LoggedInUser? = null
            val iterator = loggedInUsers.iterator()
            while (iterator.hasNext()) {
                val user = iterator.next()
                if (sharedPreferenceManager.email == user.email) {
                    iterator.remove() // Safe removal
                    user.isOnboardingComplete = true
                    loggedInUser = user
                }
            }

            if (loggedInUser != null){
                loggedInUsers.add(loggedInUser)
                sharedPreferenceManager.setLoggedInUsers(loggedInUsers)
            }
            sharedPreferenceManager.loggedUserList
            sharedPreferenceManager.clearOnboardingData()
            startActivity(Intent(this, FreeTrailActivity::class.java))
        }
    }
}