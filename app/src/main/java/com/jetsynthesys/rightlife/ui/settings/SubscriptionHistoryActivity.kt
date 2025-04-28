package com.jetsynthesys.rightlife.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivitySubscriptionHistoryBinding
import com.jetsynthesys.rightlife.ui.settings.adapter.SubscriptionHistoryAdapter
import com.jetsynthesys.rightlife.ui.settings.pojo.PurchaseHistoryResponse
import com.jetsynthesys.rightlife.ui.settings.pojo.Subscription
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionHistoryActivity : BaseActivity() {
    private lateinit var binding: ActivitySubscriptionHistoryBinding
    private lateinit var pastSubscriptionAdapter: SubscriptionHistoryAdapter
    private lateinit var activeSubscriptionAdapter: SubscriptionHistoryAdapter
    private val activeSubscriptions = ArrayList<Subscription>()
    private val pastSubscriptions = ArrayList<Subscription>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionHistoryBinding.inflate(layoutInflater)
        setChildContentView(binding.root)

        getSubscriptionHistory()

        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        pastSubscriptionAdapter =
            SubscriptionHistoryAdapter(pastSubscriptions) { subscription -> // code here for selected item from past subscriptions
            }
        binding.rvPastSubscription.layoutManager = LinearLayoutManager(this)
        binding.rvPastSubscription.adapter = pastSubscriptionAdapter

        activeSubscriptionAdapter =
            SubscriptionHistoryAdapter(activeSubscriptions) { subscription -> // code here for selected item from active subscriptions
            }
        binding.rvActiveSubscription.layoutManager = LinearLayoutManager(this)
        binding.rvActiveSubscription.adapter = activeSubscriptionAdapter

    }

    private fun getSubscriptionHistory() {
        val call = apiService.getSubscriptionHistory(
            SharedPreferenceManager.getInstance(this).accessToken,
            "PURCHASES"
        )
        call.enqueue(object : Callback<PurchaseHistoryResponse>{
            override fun onResponse(
                call: Call<PurchaseHistoryResponse>,
                response: Response<PurchaseHistoryResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.data?.subscriptions?.forEach {  subscription ->
                        if (subscription.status == "ACTIVE")
                            activeSubscriptions.add(subscription)
                        else
                            pastSubscriptions.add(subscription)

                        if (pastSubscriptions.isEmpty()){
                            binding.rvPastSubscription.visibility = View.GONE
                            binding.cardViewNoPastSubscription.visibility = View.VISIBLE
                        }

                        activeSubscriptionAdapter.notifyDataSetChanged()
                        pastSubscriptionAdapter.notifyDataSetChanged()
                    }
                }else{
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<PurchaseHistoryResponse>, t: Throwable) {
                handleNoInternetView(t)
            }

        })
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}