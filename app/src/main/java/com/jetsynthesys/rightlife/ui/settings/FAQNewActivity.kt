package com.jetsynthesys.rightlife.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityFaqNewBinding
import com.jetsynthesys.rightlife.ui.settings.adapter.FAQNewAdapter
import com.jetsynthesys.rightlife.ui.settings.pojo.FAQDetails
import com.jetsynthesys.rightlife.ui.settings.pojo.FAQResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FAQNewActivity : BaseActivity() {
    private lateinit var binding: ActivityFaqNewBinding
    private lateinit var faqNewAdapter: FAQNewAdapter
    private val faqList = mutableListOf<FAQDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqNewBinding.inflate(layoutInflater)
        setChildContentView(binding.root)

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