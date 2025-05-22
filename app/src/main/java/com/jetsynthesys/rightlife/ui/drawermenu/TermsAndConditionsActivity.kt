package com.jetsynthesys.rightlife.ui.drawermenu

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.ActivityTermsAndConditionsBinding
import com.jetsynthesys.rightlife.ui.settings.pojo.GeneralInformationResponse
import com.jetsynthesys.rightlife.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsAndConditionsActivity : BaseActivity() {
    private lateinit var binding: ActivityTermsAndConditionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setChildContentView(binding.root)

        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        getGeneralInfo()
    }

    private fun getGeneralInfo() {
        val call = apiService.getGeneralInformation()

        call.enqueue(object : Callback<GeneralInformationResponse> {
            override fun onResponse(
                call: Call<GeneralInformationResponse>,
                response: Response<GeneralInformationResponse>
            ) {
                Utils.dismissLoader(this@TermsAndConditionsActivity)
                if (response.isSuccessful && response.body() != null) {
                    val generalInformationResponse = response.body()
                    binding.tvAnswer.text =
                        Html.fromHtml(
                            generalInformationResponse?.data?.termsConditions,
                            Html.FROM_HTML_MODE_COMPACT
                        )
                } else {
                    showToast("Server Error: " + response.code())
                }
            }

            override fun onFailure(call: Call<GeneralInformationResponse>, t: Throwable) {
                Utils.dismissLoader(this@TermsAndConditionsActivity)
                showToast("Network Error: " + t.message)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
