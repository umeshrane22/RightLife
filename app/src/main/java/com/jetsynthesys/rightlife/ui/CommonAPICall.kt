package com.jetsynthesys.rightlife.ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException


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

    fun uploadImageToPreSignedUrl(
        context: Context,
        file: File,
        preSignedUrl: String,
        onResult: (Boolean) -> Unit
    ) {
        val client = OkHttpClient()

        val mediaType = "image/png".toMediaTypeOrNull() // or "image/jpeg"
        val requestBody = file.asRequestBody(mediaType)

        val request = Request.Builder()
            .url(preSignedUrl)
            .put(requestBody)
            .addHeader("x-amz-acl", "public-read") // if required
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    onResult(false)
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use {
                    Handler(Looper.getMainLooper()).post {
                        onResult(response.isSuccessful)
                    }
                }
            }

        })
    }

}