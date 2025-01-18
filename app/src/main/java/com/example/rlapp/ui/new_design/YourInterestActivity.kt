package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.ui.new_design.pojo.GetInterestResponse
import com.example.rlapp.ui.new_design.pojo.InterestTopic
import com.example.rlapp.ui.new_design.pojo.SaveUserInterestRequest
import com.example.rlapp.ui.new_design.pojo.SaveUserInterestResponse
import com.example.rlapp.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YourInterestActivity : AppCompatActivity() {
    private lateinit var adapter: YourInterestAdapter
    private val interestList = ArrayList<InterestTopic>()
    private val selectedInterest = ArrayList<InterestTopic>()
    private lateinit var btnSaveInterest: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_interest)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_your_interest)
        btnSaveInterest = findViewById<Button>(R.id.btn_save_interest)


        val colorStateListSelected = ContextCompat.getColorStateList(this, R.color.menuselected)
        val colorStateList = ContextCompat.getColorStateList(this, R.color.rightlife)

        getInterests()
        adapter = YourInterestAdapter(this, interestList) { interest ->
            if (interest.isSelected)
                selectedInterest.remove(interest)
            else
                selectedInterest.add(interest)

            btnSaveInterest.isEnabled = selectedInterest.size > 0
            btnSaveInterest.backgroundTintList =
                if (selectedInterest.size > 0) colorStateListSelected else colorStateList
        }
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.adapter = adapter


        btnSaveInterest.setOnClickListener {
            val ids = ArrayList<String>()
            selectedInterest.forEach { interest ->
                interest.id?.let { it1 -> ids.add(it1) }
            }
            val saveUserInterestRequest = SaveUserInterestRequest()
            saveUserInterestRequest.intrestId = ids
            saveUserInterest(saveUserInterestRequest)
        }
    }

    private fun getInterests() {
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getDevClient().create(ApiService::class.java)
        val call = apiService.getUserInterest(authToken)
        call.enqueue(object : Callback<GetInterestResponse> {
            override fun onResponse(
                call: Call<GetInterestResponse>,
                response: Response<GetInterestResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()

                    val data = apiResponse?.data
                    data?.topics?.let { interestList.addAll(it) }
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(
                        this@YourInterestActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<GetInterestResponse>, t: Throwable) {
                Toast.makeText(
                    this@YourInterestActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun saveUserInterest(saveUserInterestRequest: SaveUserInterestRequest) {
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getDevClient().create(ApiService::class.java)
        val call = apiService.saveUserInterest(authToken, saveUserInterestRequest)

        call.enqueue(object : Callback<SaveUserInterestResponse> {
            override fun onResponse(
                call: Call<SaveUserInterestResponse>,
                response: Response<SaveUserInterestResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()

                    Toast.makeText(
                        this@YourInterestActivity,
                        apiResponse?.successMessage,
                        Toast.LENGTH_SHORT
                    ).show()

                    uiChangesOnSaveInterest()

                } else {
                    Toast.makeText(
                        this@YourInterestActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SaveUserInterestResponse>, t: Throwable) {
                Toast.makeText(
                    this@YourInterestActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun uiChangesOnSaveInterest(){
        interestList.clear()
        interestList.addAll(selectedInterest)
        adapter.notifyDataSetChanged()

        val colorStateListSaved = ContextCompat.getColorStateList(this, R.color.bg_edittext_color)

        btnSaveInterest.text = "Interest Saved"
        btnSaveInterest.backgroundTintList = colorStateListSaved
        btnSaveInterest.setTextColor(getColor(R.color.txt_color_header))
        btnSaveInterest.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.correct_green,
            0,
            0,
            0
        )

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, PersonalisationActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

}