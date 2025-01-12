package com.example.rlapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        videoView = findViewById(R.id.videoView)

        // Set the video URI from the raw folder
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.rewards_screen}")
        videoView.setVideoURI(videoUri)

        // Set a listener for video completion
        //videoView.setOnCompletionListener {
        // Delay the transition to the next activity to allow the video to end properly
        Handler().postDelayed({
            val intent = Intent(this, DataControlActivity::class.java)
            startActivity(intent)
            finish()  // Close the SplashActivity
        }, 500)
        //}

        // Start the video
        // videoView.start()
    }
}