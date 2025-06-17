package com.jetsynthesys.rightlife.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.RetrofitData.LogoutUserRequest
import com.jetsynthesys.rightlife.databinding.ActivitySettingsNewBinding
import com.jetsynthesys.rightlife.databinding.BottomsheetDeleteTagBinding
import com.jetsynthesys.rightlife.ui.new_design.ImageSliderActivity
import com.jetsynthesys.rightlife.ui.settings.adapter.SettingsAdapter
import com.jetsynthesys.rightlife.ui.settings.pojo.SettingItem
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.jetsynthesys.rightlife.ui.utility.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.BuildConfig
import com.jetsynthesys.rightlife.ui.new_design.DataControlActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsNewActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsNewBinding
    private lateinit var settingsAdapter: SettingsAdapter
    private lateinit var othersAdapter: SettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsNewBinding.inflate(layoutInflater)
        setChildContentView(binding.root)

        setupSettingsRecyclerView()
        setupOtherRecyclerView()

        // Switch
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            showToast(if (isChecked) "Dark Mode ON" else "Dark Mode OFF")
            /*if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                SharedPreferenceManager.getInstance(this).saveAppMode("Light")
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                SharedPreferenceManager.getInstance(this).saveAppMode("Dark")
            }*/
        }

        //back button
        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Logout
        binding.btnLogout.setOnClickListener {
            showLogoutBottomSheet()
        }
        val versionName = BuildConfig.VERSION_NAME
        binding.tvVersion.text = "Version $versionName"

    }

    private fun setupSettingsRecyclerView() {
        val settingsItems = listOf(
            SettingItem("App Settings"),
            SettingItem("Support"),
            SettingItem("General Information"),
            //SettingItem("Manage Membership")
        )

        settingsAdapter = SettingsAdapter(settingsItems) { item ->
            when (item.title) {
                "App Settings" ->
                    startActivity(Intent(this, AppSettingsActivity::class.java))

                "Support" ->
                    startActivity(Intent(this, SupportActivity::class.java))

                "General Information" ->
                    startActivity(Intent(this, GeneralInformationActivity::class.java))

                "Manage Membership" ->
                    startActivity(Intent(this, SubscriptionPlansActivity::class.java))
            }
        }

        binding.rvSettings.apply {
            layoutManager = LinearLayoutManager(this@SettingsNewActivity)
            adapter = settingsAdapter
        }
    }

    private fun setupOtherRecyclerView() {
        val settingsItems = listOf(
            SettingItem("Rate RightLife"),
            SettingItem("Share")
        )

        othersAdapter = SettingsAdapter(settingsItems) { item ->
            when (item.title) {
                "Rate RightLife" -> {
                    val uri = Uri.parse("market://details?id=${packageName}")
                    val rateIntent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(rateIntent)
                }

                "Share" -> {
                    shareIntent()
                }
            }
        }

        binding.rvOther.apply {
            layoutManager = LinearLayoutManager(this@SettingsNewActivity)
            adapter = othersAdapter
        }
    }

    private fun showLogoutBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val dialogBinding = BottomsheetDeleteTagBinding.inflate(layoutInflater)
        val bottomSheetView = dialogBinding.root
        bottomSheetDialog.setContentView(bottomSheetView)

        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        dialogBinding.tvTitle.text = "Logout?"
        dialogBinding.tvDescription.text = "Are you sure you want to Logout?"

        dialogBinding.ivDialogClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnYes.setOnClickListener {
            logoutUser()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun shareIntent() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            //type = "image/*"
            type = "text/plain";
            //putExtra(Intent.EXTRA_STREAM, getImageToShare(bitmap))
            putExtra(
                Intent.EXTRA_TEXT,
                """
                    I'm inviting you to join me on the RightLife App, where health meets happiness in every tap.
                    HealthCam and Voice Scan: Get insights into your physical and emotional well-being through facial recognition and voice pattern analysis."
                    
                    Together, let's craft our own adventure towards wellbeing! 
                    Download Now:  Play Store:\n https://play.google.com/store/apps/details?id=${packageName}
                    """.trimIndent()
            )
        }

        startActivity(Intent.createChooser(intent, "Share"))
    }

    private fun logoutUser() {
        val deviceId = Utils.getDeviceId(this)
        val request = LogoutUserRequest()
        request.deviceId = deviceId

        val call = apiService.LogoutUser(sharedPreferenceManager.accessToken, request)
        call.enqueue(object : Callback<JsonElement?> {
            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                if (response.isSuccessful && response.body() != null) {
                    val gson = Gson()
                    val jsonResponse = gson.toJson(response.body())
                    Log.d("API Response body", "Success: Logout $jsonResponse")
                    clearUserDataAndFinish()
                } else {
                    showToast("Server Error: " + response.code())
                }
            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                handleNoInternetView(t)
            }
        })
    }

    private fun clearUserDataAndFinish() {
        val sharedPreferences =
            getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        SharedPreferenceManager.getInstance(this).clearData()

        val intent = Intent(this, DataControlActivity::class.java)
        startActivity(intent)

        finishAffinity()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
