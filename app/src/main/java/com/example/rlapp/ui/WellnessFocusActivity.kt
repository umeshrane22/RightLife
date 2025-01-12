package com.example.rlapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.rlapp.R
import com.example.rlapp.ui.utility.AppConstants
import com.example.rlapp.ui.utility.Utils


class WellnessFocusActivity : AppCompatActivity() {

    private var selectedWellness: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellness_focus)

        val rlThinkRight = findViewById<RelativeLayout>(R.id.rl_think_right)
        val rlMoveRight = findViewById<RelativeLayout>(R.id.rl_move_right)
        val rlEatRight = findViewById<RelativeLayout>(R.id.rl_eat_right)
        val rlSleepRight = findViewById<RelativeLayout>(R.id.rl_sleep_right)

        val btnContinue = findViewById<Button>(R.id.btn_continue)

        val colorStateListSelected = ContextCompat.getColorStateList(this, R.color.menuselected)
        val bgDrawable = AppCompatResources.getDrawable(this, R.drawable.bg_gray_border)

        val unwrappedDrawable =
            AppCompatResources.getDrawable(this, R.drawable.bg_gray_border)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)

        rlThinkRight.setOnClickListener {
            if (selectedWellness == AppConstants.THINK_RIGHT) {
                return@setOnClickListener
            } else {
                DrawableCompat.setTint(
                    wrappedDrawable,
                    Utils.getModuleColor(this, AppConstants.THINK_RIGHT)
                )
                selectedWellness = AppConstants.THINK_RIGHT
                rlThinkRight.background = wrappedDrawable
                rlSleepRight.background = bgDrawable
                rlEatRight.background = bgDrawable
                rlMoveRight.background = bgDrawable
            }
            btnContinue.backgroundTintList = colorStateListSelected
            btnContinue.isEnabled = true
        }

        rlMoveRight.setOnClickListener {
            if (selectedWellness == AppConstants.MOVE_RIGHT) {
                return@setOnClickListener
            } else {
                DrawableCompat.setTint(
                    wrappedDrawable,
                    Utils.getModuleColor(this, AppConstants.MOVE_RIGHT)
                )

                selectedWellness = AppConstants.MOVE_RIGHT
                rlThinkRight.background = bgDrawable
                rlSleepRight.background = bgDrawable
                rlEatRight.background = bgDrawable
                rlMoveRight.background = wrappedDrawable
            }
            btnContinue.backgroundTintList = colorStateListSelected
            btnContinue.isEnabled = true
        }

        rlEatRight.setOnClickListener {
            if (selectedWellness == AppConstants.EAT_RIGHT) {
                return@setOnClickListener
            } else {
                DrawableCompat.setTint(
                    wrappedDrawable,
                    Utils.getModuleColor(this, AppConstants.EAT_RIGHT)
                )
                selectedWellness = AppConstants.EAT_RIGHT
                rlThinkRight.background = bgDrawable
                rlSleepRight.background = bgDrawable
                rlEatRight.background = wrappedDrawable
                rlMoveRight.background = bgDrawable
            }
            btnContinue.backgroundTintList = colorStateListSelected
            btnContinue.isEnabled = true
        }

        rlSleepRight.setOnClickListener {
            if (selectedWellness == AppConstants.SLEEP_RIGHT) {
                return@setOnClickListener
            } else {
                DrawableCompat.setTint(
                    wrappedDrawable,
                    Utils.getModuleColor(this, AppConstants.SLEEP_RIGHT)
                )
                selectedWellness = AppConstants.SLEEP_RIGHT
                rlThinkRight.background = bgDrawable
                rlSleepRight.background = wrappedDrawable
                rlEatRight.background = bgDrawable
                rlMoveRight.background = bgDrawable
            }
            btnContinue.backgroundTintList = colorStateListSelected
            btnContinue.isEnabled = true
        }

        btnContinue.setOnClickListener {
            val intent = Intent(this, WellnessFocusListActivity::class.java)
            intent.putExtra("WellnessFocus",selectedWellness)
            startActivity(intent)
        }
    }
}