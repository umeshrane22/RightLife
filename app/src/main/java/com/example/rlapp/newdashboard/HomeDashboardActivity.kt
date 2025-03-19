package com.example.rlapp.newdashboard

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.rlapp.R
import com.example.rlapp.databinding.ActivityHomeDashboardBinding
import com.example.rlapp.newdashboard.NewExploreFragment.ExploreFragment
import com.example.rlapp.newdashboard.NewHomeFragment.HomeFragment
import com.example.rlapp.ui.HomeActivity

class HomeDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeDashboardBinding
    private var isAdd = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default fragment
        loadFragment(HomeFragment())

        // Handle menu item clicks
        binding.menuHome.setOnClickListener {
            //loadFragment(HomeFragment())
            updateMenuSelection(R.id.menu_home)
        }

        binding.menuExplore.setOnClickListener {
            //loadFragment(ExploreFragment())
            startActivity(Intent(this, HomeActivity::class.java))
            updateMenuSelection(R.id.menu_explore)
        }

        // Set initial selection
        updateMenuSelection(R.id.menu_home)

        // Handle FAB click
        binding.fab.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.white)
        binding.fab.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
        binding.fab.setOnClickListener {
            /*val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)*/
            binding.fab.animate().rotationBy(180f).setDuration(60).setInterpolator(DecelerateInterpolator()).withEndAction {
                // Change icon after rotation
                if (isAdd) {
                    binding.fab.setImageResource(R.drawable.icon_quicklink_plus_black)  // Change to close icon
                    binding.fab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.rightlife)
                    binding.fab.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                } else {
                    binding.fab.setImageResource(R.drawable.icon_quicklink_plus)    // Change back to add icon
                    binding.fab.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
                    binding.fab.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.rightlife))
                }
                isAdd = !isAdd  // Toggle the state
            }.start()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun updateMenuSelection(selectedMenuId: Int) {
        // Reset all icons and labels
        binding.iconHome.setImageResource(R.drawable.new_home_unselected_svg) // Unselected icon
        binding.iconExplore.setImageResource(R.drawable.new_explore_unselected_svg) // Unselected icon
        binding.labelHome.setTextColor(ContextCompat.getColor(this, R.color.gray))
        binding.labelExplore.setTextColor(ContextCompat.getColor(this, R.color.gray))
        binding.labelHome.setTypeface(null, Typeface.NORMAL) // Reset to normal font
        binding.labelExplore.setTypeface(null, Typeface.NORMAL) // Reset to normal font

        // Highlight selected icon and label
        when (selectedMenuId) {
            R.id.menu_home -> {
                binding.iconHome.setImageResource(R.drawable.new_home_selected_svg) // Selected icon
                binding.labelHome.setTextColor(ContextCompat.getColor(this, R.color.rightlife))
                binding.labelHome.setTypeface(null, Typeface.BOLD) // Make text bold
            }
            R.id.menu_explore -> {
                binding.iconExplore.setImageResource(R.drawable.new_explore_selected_svg) // Selected icon
                binding.labelExplore.setTextColor(ContextCompat.getColor(this, R.color.rightlife))
                binding.labelExplore.setTypeface(null, Typeface.BOLD) // Make text bold
            }
        }
    }
}