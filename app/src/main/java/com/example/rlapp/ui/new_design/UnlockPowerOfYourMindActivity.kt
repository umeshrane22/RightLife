package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.ui.new_design.pojo.ModuleTopic
import com.example.rlapp.ui.new_design.pojo.OnboardingModuleResultDataList
import com.example.rlapp.ui.new_design.pojo.OnboardingModuleResultRequest
import com.example.rlapp.ui.new_design.pojo.OnboardingModuleResultResponse
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.example.rlapp.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnlockPowerOfYourMindActivity : AppCompatActivity() {

    private val selectedWellnessFocus = ArrayList<ModuleTopic>()
    private lateinit var unlockPowerAdapter: UnlockPowerAdapter
    private lateinit var tvUnlockPower: TextView
    private val unlockPowerList = ArrayList<OnboardingModuleResultDataList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock_power_of_your_mind)

        val header = intent.getStringExtra("WellnessFocus")
        @Suppress("DEPRECATION")
        selectedWellnessFocus.addAll(intent.getSerializableExtra("SelectedTopic") as ArrayList<ModuleTopic>)

        val tvHeader = findViewById<TextView>(R.id.tv_header)
        tvUnlockPower = findViewById(R.id.tv_unlock_power)
        val rvUnlockPower = findViewById<RecyclerView>(R.id.rv_unlock_power)
        val btnContinue = findViewById<Button>(R.id.btn_continue)
        tvHeader.text = header

        when (header) {
            "ThinkRight" -> tvUnlockPower.text = "Unlock The Power Of Your Mind"
            "MoveRight" -> tvUnlockPower.text = "Revolutionise Your Movement Journey"
            "SleepRight" -> tvUnlockPower.text = "Redefine Rest, Recharge Fully"
            "EatRight" -> tvUnlockPower.text = "Fuel Your Body, Transform Your Health"
            else
            -> tvUnlockPower.text = "Unlock The Power Of Your Mind"
        }

        val ids = ArrayList<String>()
        selectedWellnessFocus.forEach { module ->
            module.id?.let { ids.add(it) }
        }
        val onboardingModuleResultRequest = OnboardingModuleResultRequest()
        onboardingModuleResultRequest.id = ids

        getOnboardingModuleResult(header!!, onboardingModuleResultRequest)

        val selectedColor = Utils.getModuleDarkColor(this, header)
        tvHeader.setTextColor(selectedColor)
        tvUnlockPower.setTextColor(selectedColor)

        val unwrappedDrawable =
            AppCompatResources.getDrawable(this, R.drawable.rounded_corder_border_gray)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(
            wrappedDrawable,
            Utils.getModuleColor(this, header)
        )

        rvUnlockPower.background = wrappedDrawable
        unlockPowerAdapter = UnlockPowerAdapter(this, unlockPowerList)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvUnlockPower.layoutManager = linearLayoutManager
        rvUnlockPower.adapter = unlockPowerAdapter

        btnContinue.setOnClickListener {
            val intent = Intent(this, ThirdFillerScreenActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            startActivity(intent)
            //finish()
        }
    }

    private fun getOnboardingModuleResult(
        moduleName: String,
        onboardingModuleResultRequest: OnboardingModuleResultRequest
    ) {
        Utils.showLoader(this)
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val call = apiService.getOnboardingModuleResult(
            authToken,
            moduleName,
            onboardingModuleResultRequest
        )

        call.enqueue(object : Callback<OnboardingModuleResultResponse> {
            override fun onResponse(
                call: Call<OnboardingModuleResultResponse>,
                response: Response<OnboardingModuleResultResponse>
            ) {
                Utils.dismissLoader(this@UnlockPowerOfYourMindActivity)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()

                    val data = apiResponse?.data
                    data?.data?.let { unlockPowerList.addAll(it) }
                    unlockPowerAdapter.notifyDataSetChanged()
                    tvUnlockPower.text = data?.sectionTitle
                } else {
                    Toast.makeText(
                        this@UnlockPowerOfYourMindActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<OnboardingModuleResultResponse>, t: Throwable) {
                Utils.dismissLoader(this@UnlockPowerOfYourMindActivity)
                Toast.makeText(
                    this@UnlockPowerOfYourMindActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}