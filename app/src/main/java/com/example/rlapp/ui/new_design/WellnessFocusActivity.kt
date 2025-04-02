package com.example.rlapp.ui.new_design

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.ui.new_design.pojo.ModuleService
import com.example.rlapp.ui.new_design.pojo.OnBoardingModuleResponse
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.example.rlapp.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WellnessFocusActivity : AppCompatActivity() {

    private var selectedWellness: String = ""
    private var selectedService: ModuleService? = null
    private lateinit var adapter: OnBoardingModuleListAdapter
    private val moduleList: ArrayList<ModuleService> = ArrayList()
    private lateinit var isFrom: String
    private lateinit var colorStateListSelected: ColorStateList
    private lateinit var btnContinue: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellness_focus)

        btnContinue = findViewById(R.id.btn_continue)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_onboarding_module)

        colorStateListSelected = ContextCompat.getColorStateList(this, R.color.menuselected)!!

        isFrom = intent.getStringExtra("FROM").toString()

        setAdapter(-1)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.adapter = adapter

        getModuleList()


        btnContinue.setOnClickListener {
            val intent = Intent(this, WellnessFocusListActivity::class.java).apply {
                putExtra("WellnessFocus", selectedService?.moduleName)
                putExtra("FROM", isFrom)
            }
            SharedPreferenceManager.getInstance(this).selectedWellnessFocus =
                selectedService?.moduleName
            startActivity(intent)
        }
    }

    private fun getModuleList() {
        Utils.showLoader(this)
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val call = apiService.getOnboardingModule(authToken)

        call.enqueue(object : Callback<OnBoardingModuleResponse> {
            override fun onResponse(
                call: Call<OnBoardingModuleResponse>,
                response: Response<OnBoardingModuleResponse>
            ) {
                Utils.dismissLoader(this@WellnessFocusActivity)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()

                    // Access the 'data' and 'services' fields
                    val data = apiResponse?.data
                    data?.services?.let { moduleList.addAll(it) }
                    var position = -1
                    for ((index, item) in moduleList.withIndex()) {
                        if (item.isSelected) {
                            position = index
                            selectedWellness = Utils.getModuleText(item.moduleName)
                            selectedService = item
                            SharedPreferenceManager.getInstance(this@WellnessFocusActivity).selectedOnboardingModule =
                                selectedService?.moduleName
                            btnContinue.backgroundTintList = colorStateListSelected
                            btnContinue.isEnabled = true
                        }
                    }
                    adapter.setSelectedPosition(position)
                } else {
                    Toast.makeText(
                        this@WellnessFocusActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<OnBoardingModuleResponse>, t: Throwable) {
                Utils.dismissLoader(this@WellnessFocusActivity)
                Toast.makeText(
                    this@WellnessFocusActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }

    private fun setAdapter(position: Int) {
        adapter = OnBoardingModuleListAdapter(this, { moduleService ->
            selectedWellness = Utils.getModuleText(moduleService.moduleName)
            selectedService = moduleService
            SharedPreferenceManager.getInstance(this@WellnessFocusActivity).selectedOnboardingModule =
                selectedService?.moduleName
            btnContinue.backgroundTintList = colorStateListSelected
            btnContinue.isEnabled = true
        }, moduleList, position)
    }
}