package com.example.rlapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.databinding.ActivityGeneralInformationBinding
import com.example.rlapp.ui.settings.adapter.SettingsAdapter
import com.example.rlapp.ui.settings.pojo.GeneralInformationResponse
import com.example.rlapp.ui.settings.pojo.SettingItem
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.example.rlapp.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GeneralInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeneralInformationBinding
    private lateinit var settingsAdapter: SettingsAdapter
    private var generalInformationResponse: GeneralInformationResponse? = null
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneralInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        getGeneralInfo()

        setupGeneralInformationRecyclerView()

        //back button
        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupGeneralInformationRecyclerView() {
        val settingsItems = listOf(
            SettingItem("About Us"),
            SettingItem("Terms & Conditions"),
            SettingItem("Policies")
        )

        settingsAdapter = SettingsAdapter(settingsItems) { item ->

            val intent = Intent(this, HtmlTextActivity::class.java)
            when (item.title) {
                "About Us" ->
                    intent.putExtra("GeneralInformation", generalInformationResponse?.data?.aboutus)

                "Terms & Conditions" ->
                    intent.putExtra(
                        "GeneralInformation",
                        generalInformationResponse?.data?.termsConditions
                    )

                "Policies" ->
                    intent.putExtra(
                        "GeneralInformation",
                        generalInformationResponse?.data?.privacyPolicy
                    )
            }
            startActivity(intent)
        }

        binding.rvGeneralInformation.apply {
            layoutManager = LinearLayoutManager(this@GeneralInformationActivity)
            adapter = settingsAdapter
        }
    }

    private fun getGeneralInfo(){
        Utils.showLoader(this)
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getGeneralInformation(sharedPreferenceManager.accessToken)

        call.enqueue(object : Callback<GeneralInformationResponse>{
            override fun onResponse(
                call: Call<GeneralInformationResponse>,
                response: Response<GeneralInformationResponse>
            ) {
                Utils.dismissLoader(this@GeneralInformationActivity)
                if (response.isSuccessful && response.body() != null) {
                        generalInformationResponse = response.body()
                }else{
                    showToast("Server Error: " + response.code())
                }
            }

            override fun onFailure(call: Call<GeneralInformationResponse>, t: Throwable) {
                Utils.dismissLoader(this@GeneralInformationActivity)
                showToast("Network Error: " + t.message)
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}