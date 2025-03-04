package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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

        var header = intent.getStringExtra("WellnessFocus")
        if (header.isNullOrEmpty()) {
            header = SharedPreferenceManager.getInstance(this).selectedWellnessFocus
        }
        val tvHeader = findViewById<TextView>(R.id.tv_header)
        val rvWellnessFocusList = findViewById<RecyclerView>(R.id.rv_wellness_focus_list)
        val btnContinue = findViewById<Button>(R.id.btn_continue)
        val imgHeader = findViewById<ImageView>(R.id.img_header)

        when (header) {
            "MoveRight" -> imgHeader.setImageResource(R.drawable.header_move_right)
            "SleepRight" -> imgHeader.setImageResource(R.drawable.header_sleep_right)
            "EatRight" -> imgHeader.setImageResource(R.drawable.header_eat_right)
            else
            -> imgHeader.setImageResource(R.drawable.header_think_right)
        }
        tvHeader.text = header

        tvHeader.setTextColor(Utils.getModuleDarkColor(this, header))
        getOnboardingDataModule(header)


        val colorStateListSelected = ContextCompat.getColorStateList(this, R.color.menuselected)
        val colorStateList = ContextCompat.getColorStateList(this, R.color.rightlife)

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
            SharedPreferenceManager.getInstance(this).setWellnessFocusTopics(selectedWellnessFocus)
            startActivity(intent)
        }
    }

    private fun getOnboardingDataModule(moduleName: String?) {
        Utils.showLoader(this)
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val call = apiService.getOnboardingDataModule(authToken, moduleName)
        call.enqueue(object : Callback<OnBoardingDataModuleResponse> {
            override fun onResponse(
                call: Call<OnBoardingDataModuleResponse>,
                response: Response<OnBoardingDataModuleResponse>
            ) {
                Utils.dismissLoader(this@WellnessFocusListActivity)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()

                    val data = apiResponse?.data
                    data?.data?.let { topicList.addAll(it) }
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
                Utils.dismissLoader(this@WellnessFocusListActivity)
                Toast.makeText(
                    this@WellnessFocusListActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}