package com.example.rlapp.ui.breathwork

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rlapp.R
import com.example.rlapp.newdashboard.HomeDashboardActivity
import com.example.rlapp.databinding.ActivityBreathworkBinding
class BreathworkActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BreathworkAdapter

    private lateinit var binding: ActivityBreathworkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breathwork)
        binding = ActivityBreathworkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBackDialog.setOnClickListener {
            finish()
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        val items = listOf(
            BreathworkPattern(1, R.drawable.breathing_equal,
                BreathingPractice.EQUAL_BREATHING.toString(),"Equal Breathing", "Regulates the nervous system, reduces stress, and improves focus."),
            BreathworkPattern(2, R.drawable.breathing_box, BreathingPractice.BOX_BREATHING.toString(),"Box Breathing(4-4-4-4)", "Enhances focus, reduces anxiety, and improves emotional regulation."),
            BreathworkPattern(3, R.drawable.breathing_478, BreathingPractice.FOUR_SEVEN_EIGHT.toString(),"4-7-8 Breathing", "Lowers heart rate, promotes relaxation, and aids in sleep."),
            BreathworkPattern(4, R.drawable.breathingholdtest, BreathingPractice.ALTERNATE_NOSTRIL.toString(),"Alternate Nostril Breathing (Nadi Shodhana)", "Balances the nervous system, reduces stress, and enhances mental clarity"),
        )

        adapter = BreathworkAdapter(items) { selectedItem,position ->
            val intent = Intent(this, BreathworkSessionActivity::class.java)
            intent.putExtra("ITEM_ID", selectedItem.id)
            intent.putExtra("ITEM_TITLE", selectedItem.title)
            intent.putExtra("ITEM_DESCRIPTION", selectedItem.description)
            intent.putExtra("PRACTICE_TYPE", selectedItem.practiceType)

            startActivity(intent)
        }

        binding.recyclerView.adapter = adapter
    }
}