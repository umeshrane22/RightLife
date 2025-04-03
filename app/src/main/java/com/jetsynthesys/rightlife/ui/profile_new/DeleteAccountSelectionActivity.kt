package com.jetsynthesys.rightlife.ui.profile_new

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.databinding.ActivityDeleteAccountSelectionBinding
import com.jetsynthesys.rightlife.ui.profile_new.adapter.ReasonAdapter

class DeleteAccountSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteAccountSelectionBinding
    private lateinit var adapter: ReasonAdapter

    private val reasons = listOf(
        "Lorem Ipsum Is Simply Dummy 1",
        "Lorem Ipsum Is Simply Dummy 2",
        "Lorem Ipsum Is Simply Dummy 3",
        "Lorem Ipsum Is Simply Dummy 4"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteAccountSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ReasonAdapter(reasons)
        binding.reasonRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.reasonRecyclerView.adapter = adapter

        binding.btnCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnContinue.setOnClickListener {
            val selectedReasons = adapter.getSelectedReasons()
            if (selectedReasons.isEmpty()) {
                Toast.makeText(this, "Please select at least one reason.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Selected: $selectedReasons", Toast.LENGTH_SHORT).show()
                val arrayList = ArrayList(selectedReasons)
                startActivity(Intent(this,DeleteAccountReasonActivity::class.java).apply {
                    putStringArrayListExtra("SelectedReasons",arrayList)
                })
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
