package com.jetsynthesys.rightlife.ui.breathwork

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityBreathworkBinding
import com.jetsynthesys.rightlife.ui.CommonAPICall
import com.jetsynthesys.rightlife.ui.breathwork.pojo.BreathingData
import com.jetsynthesys.rightlife.ui.breathwork.pojo.GetBreathingResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BreathworkActivity : AppCompatActivity() {

    private lateinit var adapter: BreathworkAdapter
    private val breathWorks = ArrayList<BreathingData>()
    private var isFromTool = false
    private var whereToGo = ""

    private lateinit var binding: ActivityBreathworkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breathwork)
        binding = ActivityBreathworkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFromTool = intent.getBooleanExtra("IS_FROM_TOOLS", false)
        whereToGo = intent.getStringExtra("TOOLS_VALUE").toString()

        getBreathingWork()

        binding.icBackDialog.setOnClickListener {
            finish()
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = BreathworkAdapter(breathWorks, object : BreathworkAdapter.OnItemClickListener {
            override fun onClick(breathingData: BreathingData) {
                val intent =
                    Intent(this@BreathworkActivity, BreathworkSessionActivity::class.java).apply {
                        putExtra("BREATHWORK", breathingData)
                    }
                startActivity(intent)
            }

            override fun onAddToolTip(breathingData: BreathingData) {
                CommonAPICall.addToToolKit(
                    this@BreathworkActivity,
                    breathingData.id,
                    !breathingData.isAddedToToolKit
                )
            }

        })

        binding.recyclerView.adapter = adapter
    }

    private fun getBreathingWork() {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        apiService.getBreathingWork(SharedPreferenceManager.getInstance(this).accessToken)
            .enqueue(object : Callback<GetBreathingResponse> {
                override fun onResponse(
                    call: Call<GetBreathingResponse>,
                    response: Response<GetBreathingResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.data?.let { breathWorks.addAll(it) }
                        var breathingDataSend: BreathingData? = null
                        if (isFromTool) {
                            breathWorks.forEach { breathingData ->
                                if (breathingData.id.equals(whereToGo)) {
                                    breathingDataSend = breathingData
                                    return@forEach
                                }
                            }
                            if (breathingDataSend != null) {
                                startActivity(
                                    Intent(
                                        this@BreathworkActivity,
                                        BreathworkSessionActivity::class.java
                                    ).apply {
                                        putExtra("BREATHWORK", breathingDataSend)
                                    })
                                finish()
                            }else
                                adapter.notifyDataSetChanged()
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            this@BreathworkActivity,
                            "Server Error: " + response.code(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<GetBreathingResponse>, t: Throwable) {
                    Toast.makeText(
                        this@BreathworkActivity,
                        "Network Error: " + t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }
}