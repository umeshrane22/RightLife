package com.example.rlapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ui.utility.Utils

class WellnessFocusListActivity : AppCompatActivity() {

    private val selectedWellnessFocus = ArrayList<WellnessFocus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellness_focus_list)

        val header = intent.getStringExtra("WellnessFocus")
        val tvHeader = findViewById<TextView>(R.id.tv_header)
        val rvWellnessFocusList = findViewById<RecyclerView>(R.id.rv_wellness_focus_list)
        val btnContinue = findViewById<Button>(R.id.btn_continue)
        tvHeader.text = header

        tvHeader.setTextColor(Utils.getModuleDarkColor(this, header))

        val list = ArrayList<WellnessFocus>()

        list.add(WellnessFocus("Quiet My Mind", R.mipmap.quit_my_mind))
        list.add(WellnessFocus("Stress Relief Hacks", R.mipmap.stress_releif_hack))
        list.add(WellnessFocus("Mental Wellness\nInsights", R.mipmap.mental_wellness))
        list.add(WellnessFocus("Build A Positive\nMindset", R.mipmap.build_positive_mindset))
        list.add(WellnessFocus("Be Resilient", R.mipmap.be_resilent))
        list.add(WellnessFocus("Stop Overthinking,\nFind Peace", R.mipmap.stop_overthinking))
        list.add(WellnessFocus("Boost Focus &\nProductivity", R.mipmap.boost_focus))
        list.add(WellnessFocus("Make Smarter\nLife Choices", R.mipmap.make_smarter_life))
        list.add(WellnessFocus("Connect &\n Build Bonds", R.mipmap.connect_build_minds))
        list.add(WellnessFocus("Boost Self-\nWorth", R.mipmap.boost_self_worth))
        list.add(WellnessFocus("Recharge\nEmotionally", R.mipmap.recharge_emotionally))
        list.add(WellnessFocus("Heal & Move\nForward", R.mipmap.heal_move_forward))
        list.add(WellnessFocus("Balance Motivation\nAnd Discipline", R.mipmap.balance_motivation))
        list.add(WellnessFocus("Stabilise\nMood Swings", R.mipmap.stabilise_mood_swings))

        val colorStateListSelected = ContextCompat.getColorStateList(this, R.color.menuselected)
        val colorStateList = ContextCompat.getColorStateList(this, R.color.rightlife)

        val wellnessFocusListAdapter =
            WellnessFocusListAdapter(
                this,
                list,
                { wellnessFocus ->
                    if (wellnessFocus.isSelected)
                        selectedWellnessFocus.remove(wellnessFocus)
                    else
                        selectedWellnessFocus.add(wellnessFocus)

                    if (selectedWellnessFocus.isEmpty()) {
                        btnContinue.backgroundTintList = colorStateList
                        btnContinue.isEnabled = false
                    } else {
                        btnContinue.backgroundTintList = colorStateListSelected
                        btnContinue.isEnabled = true
                    }
                },
                module = header!!
            )

        val gridLayoutManager = GridLayoutManager(this, 2)
        rvWellnessFocusList.setLayoutManager(gridLayoutManager)

        rvWellnessFocusList.adapter = wellnessFocusListAdapter

    }
}