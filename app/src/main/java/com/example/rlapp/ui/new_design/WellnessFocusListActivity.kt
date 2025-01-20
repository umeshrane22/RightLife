package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.ui.new_design.pojo.ModuleTopic
import com.example.rlapp.ui.new_design.pojo.OnBoardingDataModuleResponse
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.example.rlapp.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WellnessFocusListActivity : AppCompatActivity() {

    private val selectedWellnessFocus = ArrayList<ModuleTopic>()
    private lateinit var wellnessFocusListAdapter: WellnessFocusListAdapter
    val topicList = ArrayList<ModuleTopic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellness_focus_list)

        val header = intent.getStringExtra("WellnessFocus")
        val tvHeader = findViewById<TextView>(R.id.tv_header)
        val rvWellnessFocusList = findViewById<RecyclerView>(R.id.rv_wellness_focus_list)
        val btnContinue = findViewById<Button>(R.id.btn_continue)
        tvHeader.text = Utils.getModuleText(header)

        tvHeader.setTextColor(Utils.getModuleDarkColor(this, header))
        getOnboardingDataModule(header)


        val colorStateListSelected = ContextCompat.getColorStateList(this, R.color.menuselected)
        val colorStateList = ContextCompat.getColorStateList(this, R.color.rightlife)
        btnContinue.isEnabled = true

        wellnessFocusListAdapter =
            WellnessFocusListAdapter(
                this,
                topicList,
                { wellnessFocus ->
                    if (wellnessFocus.isSelected)
                        selectedWellnessFocus.remove(wellnessFocus)
                    else
                        selectedWellnessFocus.add(wellnessFocus)

                    if (selectedWellnessFocus.isEmpty()) {
                        btnContinue.backgroundTintList = colorStateList
                        btnContinue.isEnabled = false
                    } else {
                        btnContinue.backgroundTintList = colorStateListSelected
                        btnContinue.isEnabled = true
                    }
                },
                module = header!!
            )

        val gridLayoutManager = GridLayoutManager(this, 2)
        rvWellnessFocusList.setLayoutManager(gridLayoutManager)

        rvWellnessFocusList.adapter = wellnessFocusListAdapter

        btnContinue.setOnClickListener {
            val intent = Intent(this, UnlockPowerOfYourMindActivity::class.java)
            intent.putExtra("WellnessFocus", header)
            intent.putExtra("SelectedTopic", selectedWellnessFocus)
            startActivity(intent)
        }
    }

    private fun getOnboardingDataModule(moduleName: String?){
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getDevClient().create(ApiService::class.java)

        val call = apiService.getOnboardingDataModule(authToken,moduleName)
        call.enqueue(object : Callback<OnBoardingDataModuleResponse>{
            override fun onResponse(
                call: Call<OnBoardingDataModuleResponse>,
                response: Response<OnBoardingDataModuleResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()

                    val data = apiResponse?.data
                    data?.topics?.let { topicList.addAll(it) }
                    wellnessFocusListAdapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(
                        this@WellnessFocusListActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<OnBoardingDataModuleResponse>, t: Throwable) {
                Toast.makeText(
                    this@WellnessFocusListActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}