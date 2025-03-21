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
import com.example.rlapp.ui.new_design.pojo.InterestDataList
import com.example.rlapp.ui.new_design.pojo.SaveUserInterestRequest
import com.example.rlapp.ui.new_design.pojo.SaveUserInterestResponse
import com.example.rlapp.ui.profile_new.ProfileSettingsActivity
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.example.rlapp.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YourInterestActivity : AppCompatActivity() {
    private lateinit var adapter: YourInterestAdapter
    private val interestList = ArrayList<InterestDataList>()
    private val selectedInterest = ArrayList<InterestDataList>()
    private lateinit var btnSaveInterest: Button
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    private lateinit var isFrom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_interest)

        var header = intent.getStringExtra("WellnessFocus")
        isFrom = intent.getStringExtra("FROM").toString()
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        if (header.isNullOrEmpty()) {
            header = sharedPreferenceManager.selectedWellnessFocus
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv_your_interest)
        btnSaveInterest = findViewById(R.id.btn_save_interest)


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
        recyclerView.isScrollbarFadingEnabled = false
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.adapter = adapter


        btnSaveInterest.setOnClickListener {
            val ids = ArrayList<String>()
            selectedInterest.forEach { interest ->
                interest.id?.let { it1 -> ids.add(it1) }
            }
            val saveUserInterestRequest = SaveUserInterestRequest()
            saveUserInterestRequest.intrestId = ids
            saveUserInterest(saveUserInterestRequest, header!!)
        }

        if (sharedPreferenceManager.savedInterest.isNotEmpty()) {
            selectedInterest.addAll(sharedPreferenceManager.savedInterest)
            uiChangesOnSaveInterest(header!!)
        }
    }

    private fun getInterests() {
        Utils.showLoader(this)
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getUserInterest(authToken)
        call.enqueue(object : Callback<GetInterestResponse> {
            override fun onResponse(
                call: Call<GetInterestResponse>,
                response: Response<GetInterestResponse>
            ) {
                Utils.dismissLoader(this@YourInterestActivity)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()

                    val data = apiResponse?.data
                    data?.InterestDatata?.let { interestList.addAll(it) }
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
                Utils.dismissLoader(this@YourInterestActivity)
                Toast.makeText(
                    this@YourInterestActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun saveUserInterest(saveUserInterestRequest: SaveUserInterestRequest, header: String) {
        // Utils.showLoader(this)
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.saveUserInterest(authToken, saveUserInterestRequest)

        call.enqueue(object : Callback<SaveUserInterestResponse> {
            override fun onResponse(
                call: Call<SaveUserInterestResponse>,
                response: Response<SaveUserInterestResponse>
            ) {
                Utils.dismissLoader(this@YourInterestActivity)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()
                    uiChangesOnSaveInterest(header = header)
                    if (!isFrom.isNullOrEmpty() && isFrom != "ProfileSetting")
                        sharedPreferenceManager.setSavedInterest(selectedInterest)

                } else {
                    Toast.makeText(
                        this@YourInterestActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SaveUserInterestResponse>, t: Throwable) {
                // Utils.dismissLoader(this@YourInterestActivity)
                Toast.makeText(
                    this@YourInterestActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun uiChangesOnSaveInterest(header: String) {
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
            if (!isFrom.isNullOrEmpty() && isFrom == "ProfileSetting") {
                finish()
                startActivity(
                    Intent(
                        this@YourInterestActivity,
                        ProfileSettingsActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        putExtra("start_profile", true)
                    })
            } else {
                val intent = Intent(this, PersonalisationActivity::class.java)
                intent.putExtra("WellnessFocus", header)
                sharedPreferenceManager.interest = true
                startActivity(intent)
                //finish()
            }
        }, 1000)

    }

}