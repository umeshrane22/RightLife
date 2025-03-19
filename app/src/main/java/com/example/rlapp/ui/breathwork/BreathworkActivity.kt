package com.example.rlapp.ui.breathwork

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rlapp.R
import com.example.rlapp.newdashboard.HomeDashboardActivity

class BreathworkActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BreathworkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breathwork)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val items = listOf(
            BreathworkPattern(1, R.drawable.breathing_equal, "Equal Breathing", "Dummy Text Of The Printing And Typesetting"),
            BreathworkPattern(2, R.drawable.breathing_box, "Box Breathing", "Dummy Text Of The Printing And Typesetting"),
            BreathworkPattern(3, R.drawable.breathing_478, "4-7-8 Breathing", "Dummy Text Of The Printing And Typesetting"),
            BreathworkPattern(4, R.drawable.breathingholdtest, "Breath Holding Test", "Dummy Text Of The Printing And Typesetting"),
        )

        adapter = BreathworkAdapter(items) { selectedItem ->
            val intent = Intent(this, HomeDashboardActivity::class.java)
            intent.putExtra("ITEM_ID", selectedItem.id)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}