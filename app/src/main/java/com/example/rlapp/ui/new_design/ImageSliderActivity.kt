package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.rlapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ImageSliderActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val timeDurationForImageSlider = 2000L

    // List of images (replace with your own images)
    private val images = listOf(
        R.mipmap.mask_group69,
        R.mipmap.mask_group70,
        R.mipmap.mask_group71,
        R.mipmap.mask_group72
    )

    private val headers = listOf(
        "Four Foundations, One Powerful App",
        "AI-Powered Personal Health Guide",
        "Wellness, Anytime, Anywhere",
        "No Social Media Fads. Just Science"
    )

    private val descriptions = listOf(
        "The only app you need to optimize your mind, body,\n nutrition, and sleep, all from your smartphone.",
        "Predict risks, get personalized insights, and transform\n your health with data-driven recommendations.",
        "Live classes, meditation, sleep sounds, nutrition\n tracking, tailored just for you, accessible 24/7.",
        "Forget the gimmicks—our information is backed by\n evidence and tailored to your unique health goals."
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_slider)

        // Initialize the ViewPager2 and TabLayout
        viewPager = findViewById(R.id.viewPager_image_slider)
        tabLayout = findViewById(R.id.tabLayout)

        viewPager.adapter = ImageSliderAdapter(this, images, headers, descriptions)

        // Set up the TabLayoutMediator to sync dots with the images
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // You can set custom content for tabs if needed, but we are using default dots
            val dot = ImageView(this)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ) // Size of the dot
            params.setMargins(0, 0, 0, 0) // Adjust the margin between dots
            dot.layoutParams = params
            dot.setImageResource(R.drawable.dot) // Default inactive dot image
            tab.customView = dot
        }.attach()

        // Set up the auto-slide functionality
        handler = Handler(mainLooper)
        runnable = Runnable {
            val currentItem = viewPager.currentItem
            val nextItem = if (currentItem < images.size - 1) currentItem + 1 else 0
            viewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(runnable, timeDurationForImageSlider)
        }

        // Auto-slide images every 2 seconds


        // Update the dots on page change
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDotColors(position)
            }
        })

        val btnGoogle = findViewById<TextView>(R.id.btn_google)
        btnGoogle.setOnClickListener {
            startActivity(Intent(this, CreateUsernameActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, timeDurationForImageSlider)
    }

    override fun onPause() {
        super.onPause()
        // Stop the auto-slide when the activity is paused
        handler.removeCallbacks(runnable)
    }

    private fun updateDotColors(position: Int) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            val dot = tab?.customView as? ImageView
            if (i == position) {
                dot?.setImageResource(R.drawable.dot_selected) // Active dot image
            } else {
                dot?.setImageResource(R.drawable.dot) // Inactive dot image
            }
        }
    }
}