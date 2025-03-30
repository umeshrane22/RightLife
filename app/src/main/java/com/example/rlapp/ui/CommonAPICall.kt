package com.example.rlapp.ui

import android.content.Context
import android.widget.Toast
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.ui.utility.SharedPreferenceManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CommonAPICall {
    fun addToToolKit(
        context: Context,
        moduleName: String?,
        moduleId: String?,
        subTitle: String? = "",
        categoryId: String? = "",
        moduleType: String? = ""
    ) {
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(context)
        val authToken = sharedPreferenceManager.accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val toolKitRequest = ToolKitRequest()
        toolKitRequest.userId = sharedPreferenceManager.userId
        toolKitRequest.moduleName = moduleName
        toolKitRequest.moduleId = moduleId
        toolKitRequest.subtitle = subTitle
        toolKitRequest.categoryId = categoryId
        toolKitRequest.moduleType = moduleType

        val call = apiService.addToToolKit(authToken, toolKitRequest)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        context,
                        "Added to ToolKit",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}