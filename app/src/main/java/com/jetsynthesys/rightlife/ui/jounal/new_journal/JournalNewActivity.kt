package com.jetsynthesys.rightlife.ui.jounal.new_journal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityJournalNewBinding
import com.jetsynthesys.rightlife.ui.CommonAPICall
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JournalNewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJournalNewBinding
    private lateinit var adapter: JournalAdapter
    private var isFromTool = false
    private var whereToGo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFromTool = intent.getBooleanExtra("IS_FROM_TOOLS", false)
        whereToGo = intent.getStringExtra("TOOLS_VALUE").toString()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.iconBack.setOnClickListener {
            finish()
        }

        fetchJournalData()
    }

    private fun fetchJournalData() {
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getJournals(authToken)
        call.enqueue(object : Callback<JournalResponse> {
            override fun onResponse(
                call: Call<JournalResponse>,
                response: Response<JournalResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val journalList = response.body()?.data ?: emptyList()

                    adapter =
                        JournalAdapter(
                            this@JournalNewActivity,
                            journalList,
                            object : JournalAdapter.OnItemClickListener {
                                override fun onClick(journalItem: JournalItem) {
                                    startNextActivity(journalItem)
                                }

                                override fun onAddToolTip(journalItem: JournalItem) {
                                    CommonAPICall.addToToolKit(
                                        this@JournalNewActivity,
                                        journalItem.id,
                                        !journalItem.isAddedToToolKit
                                    )
                                }

                            })

                    var journalItemSend: JournalItem? = null
                    if (isFromTool) {
                        journalList.forEach { journalItem ->
                            if (journalItem.id.equals(whereToGo)) {
                                journalItemSend = journalItem
                                return@forEach
                            }
                        }
                        if (journalItemSend != null) {
                            startNextActivity(journalItemSend!!)
                            finish()
                        } else {
                            runOnUiThread {
                                binding.recyclerView.adapter = adapter
                            }
                        }
                    } else {
                        runOnUiThread {
                            binding.recyclerView.adapter = adapter
                        }
                    }
                } else {
                    Toast.makeText(
                        this@JournalNewActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<JournalResponse>, t: Throwable) {
                Toast.makeText(
                    this@JournalNewActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun startNextActivity(journalItem: JournalItem) {
        val intent = when (journalItem.title) {
            "Free Form" -> Intent(
                this@JournalNewActivity,
                FreeFormJournalActivity::class.java
            )

            "Bullet" -> Intent(
                this@JournalNewActivity,
                BulletJournalActivity::class.java
            )

            else -> Intent(
                this@JournalNewActivity,
                JournalPromptActivity::class.java
            )
        }
        intent.putExtra("Section", journalItem)
        startActivity(intent)
    }
}
