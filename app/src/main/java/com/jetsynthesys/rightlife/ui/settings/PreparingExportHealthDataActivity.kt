package com.jetsynthesys.rightlife.ui.settings

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityPreparingExportHealthDataBinding
import com.jetsynthesys.rightlife.ui.CommonResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreparingExportHealthDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreparingExportHealthDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreparingExportHealthDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        exportHealthData()
    }

    private fun exportHealthData() {
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        apiService.exportHealthData(authToken).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.isSuccessful) {
                    binding.llExportMain.visibility = View.GONE
                    binding.llSuccess.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 3000)
                } else {
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                t.message?.let { showToast(it) }
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
