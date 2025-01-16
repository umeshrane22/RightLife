package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

class YourInterestActivity : AppCompatActivity() {
    private lateinit var adapter: YourInterestAdapter
    private val interestList = ArrayList<YourInterest>()
    private val selectedInterest = ArrayList<YourInterest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_interest)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_your_interest)
        val btnSaveInterest = findViewById<Button>(R.id.btn_save_interest)


        val colorStateListSelected = ContextCompat.getColorStateList(this, R.color.menuselected)
        val colorStateList = ContextCompat.getColorStateList(this, R.color.rightlife)
        val colorStateListSaved = ContextCompat.getColorStateList(this, R.color.bg_edittext_color)

        interestList.add(YourInterest("Restful Nights For Energized Days", "THINK_RIGHT"))
        interestList.add(YourInterest("Boost Flexibility", "EAT_RIGHT"))
        interestList.add(YourInterest("Avoid Energy Crashes", "MOVE_RIGHT"))
        interestList.add(YourInterest("Drift Off with Ease", "SLEEP_RIGHT"))
        interestList.add(YourInterest("Beat Bloating & Indigestion", "THINK_RIGHT"))
        interestList.add(YourInterest("Stay Active", "EAT_RIGHT"))
        interestList.add(YourInterest("Fix Your Sleep Schedule", "SLEEP_RIGHT"))
        interestList.add(YourInterest("Build Your Exercise Habit", "MOVE_RIGHT"))

        adapter = YourInterestAdapter(this, interestList) { interest ->
            if (interest.isSelected)
                selectedInterest.remove(interest)
            else
                selectedInterest.add(interest)

            btnSaveInterest.isEnabled = selectedInterest.size > 0
            btnSaveInterest.backgroundTintList =
                if (selectedInterest.size > 0) colorStateListSelected else colorStateList
        }
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.adapter = adapter


        btnSaveInterest.setOnClickListener {

            interestList.clear()
            interestList.addAll(selectedInterest)
            adapter.notifyDataSetChanged()

            btnSaveInterest.text = "Interest Saved"
            btnSaveInterest.backgroundTintList = colorStateListSaved
            btnSaveInterest.setTextColor(getColor(R.color.txt_color_header))
            btnSaveInterest.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_green, 0, 0, 0);

            Handler().postDelayed({
                val intent = Intent(this, DataControlActivity::class.java)
                startActivity(intent)
                //finish()
            }, 5000)
        }

    }
}