package com.example.rlapp.ui.new_design

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var rlview1: RelativeLayout
    private lateinit var imgview2: ImageView
    private val SPLASH_DELAY: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        videoView = findViewById(R.id.videoView)
        rlview1 = findViewById(R.id.rlview1)
        imgview2 = findViewById(R.id.imgview2)


        // Set the video URI from the raw folder
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.rewards_screen}")
        //videoView.setVideoURI(videoUri)

        // Set a listener for video completion
        //videoView.setOnCompletionListener {
        // Delay the transition to the next activity to allow the video to end properly
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, DataControlActivity::class.java)
            startActivity(intent)
            finish()  // Close the SplashActivity
        }, 3000)
        //}

        // Start the video
        // videoView.start()


        val view1: View = findViewById(R.id.rlview1)
        val view2: View = findViewById(R.id.imgview2)
        animateViews();
       /* val toggleButton: View = findViewById(R.id.btn_toggle)

        toggleButton.setOnClickListener {
            // Fade out view1 and fade in view2
            view1.animate()
                .alpha(0f) // Fade out
                .setDuration(700) // Animation duration in ms
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view1.visibility = View.GONE // Hide view1 after animation
                        view2.visibility = View.VISIBLE // Show view2 before animation
                        view2.alpha = 0f // Set initial alpha for fade in

                        view2.animate()
                            .alpha(1f) // Fade in
                            .setDuration(700) // Animation duration in ms
                            .setListener(null)
                    }
                })
        }*/

    }
    fun animateViews(){
        val view1: View = findViewById(R.id.rlview1)
        val view2: View = findViewById(R.id.imgview2)
        // Fade out view1 and fade in view2
        view1.animate()
            .alpha(0f) // Fade out
            .setDuration(2000) // Animation duration in ms
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view1.visibility = View.GONE // Hide view1 after animation
                    view2.visibility = View.VISIBLE // Show view2 before animation
                    view2.alpha = 0f // Set initial alpha for fade in

                    view2.animate()
                        .alpha(1f) // Fade in
                        .setDuration(1000) // Animation duration in ms
                        .setListener(null)
                }
            })
    }
}