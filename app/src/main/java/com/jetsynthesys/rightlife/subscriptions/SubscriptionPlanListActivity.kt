package com.jetsynthesys.rightlife.subscriptions

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.databinding.ActivitySubscriptionPlanListBinding
import com.jetsynthesys.rightlife.subsciptions.BillingActivity
import com.jetsynthesys.rightlife.subscriptions.adapter.SubscriptionPlanAdapter
import com.jetsynthesys.rightlife.subscriptions.pojo.PlanList
import com.jetsynthesys.rightlife.subscriptions.pojo.SubscriptionPlansResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionPlanListActivity : BaseActivity() {

    private lateinit var binding: ActivitySubscriptionPlanListBinding
    private lateinit var adapter: SubscriptionPlanAdapter
    private val planList = ArrayList<PlanList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionPlanListBinding.inflate(layoutInflater)
        setChildContentView(binding.root)
        val type = intent.getStringExtra("SUBSCRIPTION_TYPE")
        //val type = "FACIAL_SCAN"
        getSubscriptionList(type ?: "FACIAL_SCAN")

        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        adapter = SubscriptionPlanAdapter(planList) { plan ->
            showToast("Plan Clicked - " + plan.googlePlay)
            val intent = Intent(this, BillingActivity::class.java)
            intent.putExtra("PRODUCT_ID", plan.googlePlay) // Replace "your_specific_product_id"
            if (type == "FACIAL_SCAN") {
                intent.putExtra("PRODUCT_TYPE", "BOOSTER") // Replace "your_specific_product_id"
            } else {
                intent.putExtra("PRODUCT_TYPE", "SUBSCRIPTION") // Replace "your_specific_product_id"
            }
            startActivity(intent)

        }

        binding.plansRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.plansRecyclerView.adapter = adapter
    }

    private fun getSubscriptionList(type: String) {
        val call = apiService.getSubscriptionPlanList(
            SharedPreferenceManager.getInstance(this).accessToken,
            type
        )
        call.enqueue(object : Callback<SubscriptionPlansResponse> {
            override fun onResponse(
                call: Call<SubscriptionPlansResponse>,
                response: Response<SubscriptionPlansResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.data?.result?.list?.let { planList.addAll(it) }
                    adapter.notifyDataSetChanged()
                } else {
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<SubscriptionPlansResponse>, t: Throwable) {
                handleNoInternetView(t)
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}