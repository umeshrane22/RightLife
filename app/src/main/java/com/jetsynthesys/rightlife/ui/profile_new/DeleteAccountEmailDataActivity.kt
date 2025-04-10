package com.jetsynthesys.rightlife.ui.profile_new

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityDeleteAccountEmailDataBinding
import com.jetsynthesys.rightlife.ui.CommonResponse
import com.jetsynthesys.rightlife.ui.new_design.ImageSliderActivity
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteAccountEmailDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteAccountEmailDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteAccountEmailDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reasonsList = intent.getStringExtra("SelectedReasons")
        val option = intent.getStringExtra("Message")

        binding.btnCancel.setOnClickListener {
            finish()
            startActivity(
                Intent(
                    this@DeleteAccountEmailDataActivity,
                    ProfileSettingsActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    putExtra("start_profile", true)
                })
        }

        binding.btnContinue.setOnClickListener {
            deleteAccountAPI(reasonsList!!, option!!)
        }
    }

    private fun deleteAccountAPI(reason: String, message: String) {
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        val authToken = sharedPreferenceManager.accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val body = mapOf(
            "name" to sharedPreferenceManager.userProfile.userdata.firstName,
            "email" to sharedPreferenceManager.userProfile.userdata.email,
            "phoneNumber" to sharedPreferenceManager.userProfile.userdata.phoneNumber,
            "option" to reason,
            "message" to message

        )

        apiService.deleteAccount(authToken, body).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.successMessage?.let { showToast(it) }
                    clearUserDataAndFinish()
                }else{
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                t.message?.let { showToast(it) }
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

        val intent = Intent(this, ImageSliderActivity::class.java)
        startActivity(intent)

        finishAffinity()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}