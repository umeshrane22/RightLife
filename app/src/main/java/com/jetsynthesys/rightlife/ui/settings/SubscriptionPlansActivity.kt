package com.jetsynthesys.rightlife.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.databinding.ActivitySubscriptionPlansBinding
import com.jetsynthesys.rightlife.ui.settings.adapter.PlanAdapter
import com.jetsynthesys.rightlife.ui.settings.pojo.Plan

class SubscriptionPlansActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubscriptionPlansBinding
    private lateinit var planAdapter: PlanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionPlansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val plans = listOf(
            Plan("Monthly Plan", "First 14 days free · XXX/month", "XXX/month"),
            Plan("Yearly Plan", "First 14 days free · XXX/year", "XXX/year")
        )

        planAdapter = PlanAdapter(plans) { selectedPlan ->
            // Handle selected plan
            showToast("Selected: ${selectedPlan.name}")
        }

        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.plansRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.plansRecyclerView.adapter = planAdapter

        binding.upgradeButton.setOnClickListener {
            // Handle upgrade
            showToast("Purchase successfully")
        }

        binding.cancelButton.setOnClickListener {
            showToast("Purchase Cancelled")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
