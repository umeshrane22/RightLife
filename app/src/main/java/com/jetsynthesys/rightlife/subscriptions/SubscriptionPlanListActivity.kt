package com.jetsynthesys.rightlife.subscriptions

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.databinding.ActivitySubscriptionPlanListBinding
import com.jetsynthesys.rightlife.subscriptions.adapter.SubscriptionPlanAdapter
import com.jetsynthesys.rightlife.subscriptions.pojo.PaymentIntentResponse
import com.jetsynthesys.rightlife.subscriptions.pojo.PaymentSuccessRequest
import com.jetsynthesys.rightlife.subscriptions.pojo.PaymentSuccessResponse
import com.jetsynthesys.rightlife.subscriptions.pojo.PlanList
import com.jetsynthesys.rightlife.subscriptions.pojo.SdkDetail
import com.jetsynthesys.rightlife.subscriptions.pojo.SubscriptionPlansResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionPlanListActivity : BaseActivity() {

    private lateinit var binding: ActivitySubscriptionPlanListBinding
    private lateinit var adapter: SubscriptionPlanAdapter
    private val planList = ArrayList<PlanList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionPlanListBinding.inflate(layoutInflater)
        setChildContentView(binding.root)
        val type = intent.getStringExtra("SUBSCRIPTION_TYPE")

        getSubscriptionList(type ?: "FACIAL_SCAN")

        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        adapter = SubscriptionPlanAdapter(planList) { plan ->
            showToast("Plan Clicked" + plan.price?.inr)


            val paymentSuccessRequest = PaymentSuccessRequest()
            paymentSuccessRequest.planId = plan.id
            paymentSuccessRequest.planName = plan.purchase?.planName
            paymentSuccessRequest.paymentGateway = "googlePlay"
            paymentSuccessRequest.orderId = ""//may be getting from payment response
            paymentSuccessRequest.environment = "test"
            paymentSuccessRequest.notifyType = "SDK"
            paymentSuccessRequest.couponId = ""//may be getting from payment response
            paymentSuccessRequest.obfuscatedExternalAccountId =
                ""//may be getting from payment response

            val sdkDetail = SdkDetail()
            sdkDetail.price = plan.price?.inr.toString()
            sdkDetail.orderId = ""//may be getting from payment response
            sdkDetail.title = ""
            sdkDetail.environment = "payment"
            sdkDetail.description = ""
            sdkDetail.currencyCode = "INR"
            sdkDetail.currencySymbol = "₹"

            paymentSuccessRequest.sdkDetail = sdkDetail
            saveSubscriptionSuccess(paymentSuccessRequest)

        }

        binding.plansRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.plansRecyclerView.adapter = adapter
    }

    private fun getSubscriptionList(type: String) {
        val call = apiService.getSubscriptionPlanList(
            sharedPreferenceManager.accessToken,
            type
        )
        call.enqueue(object : Callback<SubscriptionPlansResponse> {
            override fun onResponse(
                call: Call<SubscriptionPlansResponse>,
                response: Response<SubscriptionPlansResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.data?.result?.list?.let { planList.addAll(it) }
                    adapter.notifyDataSetChanged()
                } else {
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<SubscriptionPlansResponse>, t: Throwable) {
                handleNoInternetView(t)
            }

        })
    }

    private fun saveSubscriptionSuccess(paymentSuccessRequest: PaymentSuccessRequest) {
        val call = apiService.savePaymentSuccess(
            sharedPreferenceManager.accessToken,
            paymentSuccessRequest
        )
        call.enqueue(object : Callback<PaymentSuccessResponse> {
            override fun onResponse(
                call: Call<PaymentSuccessResponse>,
                response: Response<PaymentSuccessResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.data?.id?.let { updatePaymentId(it) }
                } else {
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<PaymentSuccessResponse>, t: Throwable) {
                handleNoInternetView(t)
            }

        })
    }

    private fun updatePaymentId(paymentId: String) {
        val call = apiService.getPaymentIntent(sharedPreferenceManager.accessToken, paymentId)
        call.enqueue(object : Callback<PaymentIntentResponse> {
            override fun onResponse(
                call: Call<PaymentIntentResponse>,
                response: Response<PaymentIntentResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    showToast("Subscribed Successfully!!")
                } else {
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<PaymentIntentResponse>, t: Throwable) {
                handleNoInternetView(t)
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}