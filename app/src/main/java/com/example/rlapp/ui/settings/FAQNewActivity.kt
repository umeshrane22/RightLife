package com.example.rlapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.databinding.ActivityFaqNewBinding
import com.example.rlapp.ui.settings.adapter.FAQNewAdapter
import com.example.rlapp.ui.settings.pojo.FAQDetails
import com.example.rlapp.ui.settings.pojo.FAQResponse
import com.example.rlapp.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FAQNewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaqNewBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private lateinit var faqNewAdapter: FAQNewAdapter
    private val faqList = mutableListOf<FAQDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        //back button
        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        getFAQ()

        faqNewAdapter = FAQNewAdapter(faqList, object : FAQNewAdapter.OnItemClickListener {
            override fun onItemClick(faqData: FAQDetails) {
                startActivity(Intent(this@FAQNewActivity, FAQDetailsActivity::class.java).apply {
                    putExtra("FAQDetails", faqData)
                })
            }
        })

        binding.rvFaq.apply {
            layoutManager = LinearLayoutManager(this@FAQNewActivity)
            adapter = faqNewAdapter
        }
    }

    private fun getFAQ() {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getFAQData(sharedPreferenceManager.accessToken)
        call.enqueue(object : Callback<FAQResponse> {
            override fun onResponse(call: Call<FAQResponse>, response: Response<FAQResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!.data?.forEach { faqData ->
                        faqData.details?.let { faqList.addAll(it) }
                    }
                    faqNewAdapter.notifyDataSetChanged()
                } else {
                    showToast("Server Error: " + response.code())
                }
            }

            override fun onFailure(call: Call<FAQResponse>, t: Throwable) {
                showToast("Network Error: " + t.message)
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}