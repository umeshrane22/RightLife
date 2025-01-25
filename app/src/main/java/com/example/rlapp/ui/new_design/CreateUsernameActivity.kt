package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.RetrofitData.LogoutUserRequest
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.example.rlapp.ui.utility.Utils
import com.google.gson.Gson
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateUsernameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_username)


        val username = intent.getStringExtra("USERNAME_KEY")


        val edtUsername = findViewById<EditText>(R.id.edt_username)

        if (!username.isNullOrEmpty()) {
            edtUsername.setText(username) // Set the username to the EditText
        }

        val colorStateListSelected = ContextCompat.getColorStateList(this, R.color.menuselected)
        val colorStateList = ContextCompat.getColorStateList(this, R.color.rightlife)

        val tvCharLeft = findViewById<TextView>(R.id.tv_char_left)
        val btnContinue = findViewById<Button>(R.id.btn_continue)
        val tvError = findViewById<TextView>(R.id.tv_username_error)
        val charLeft = edtUsername.text.length
        if (validateUsername(edtUsername.text.toString())) {
            tvError.visibility = GONE
            btnContinue.backgroundTintList = colorStateListSelected
            btnContinue.isEnabled = true
        }
        else {
            tvError.visibility = VISIBLE
            btnContinue.backgroundTintList = colorStateList
            btnContinue.isEnabled = false
        }

        "$charLeft/20 ch".also { tvCharLeft.text = it }

        edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                val c = 20 - edtUsername.text.length
                "$c/20 ch".also { tvCharLeft.text = it }
                if (validateUsername(p0.toString())) {
                    tvError.visibility = GONE
                    btnContinue.backgroundTintList = colorStateListSelected
                    btnContinue.isEnabled = true
                }
                else {
                    tvError.visibility = VISIBLE
                    btnContinue.backgroundTintList = colorStateList
                    btnContinue.isEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        btnContinue.setOnClickListener {
            showFullScreenDialog(edtUsername.text.toString())
            //postUserLogout("")
        }
    }


    private fun postUserLogout(s: String) {
        //-----------
        val accessToken = SharedPreferenceManager.getInstance(this).accessToken

        val apiService = ApiClient.getClient().create(ApiService::class.java)

        // Create a request body (replace with actual email and phone number)
        val deviceId = Utils.getDeviceId(this)
        val request = LogoutUserRequest()
        request.deviceId = deviceId

        // Make the API call
        val call = apiService.LogoutUser(accessToken, request)
        call.enqueue(object : Callback<JsonElement?> {
            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                if (response.isSuccessful && response.body() != null) {
                    val promotionResponse2 = response.body()
                    Log.d("API Response", "Success: " + promotionResponse2.toString())
                    val gson = Gson()
                    val jsonResponse = gson.toJson(response.body())
                    //                    PromotionResponse promotionResponse = gson.fromJson(jsonResponse, PromotionResponse.class);
                    Log.d("API Response body", "Success: promotion $jsonResponse")
                    //clearUserDataAndFinish()
                } else {
                    Toast.makeText(
                        this@CreateUsernameActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Toast.makeText(
                    this@CreateUsernameActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("API ERROR", "onFailure: " + t.message)
                t.printStackTrace() // Print the full stack trace for more details
            }
        })
    }

    fun validateUsername(username: String): Boolean {
        // Check if the username only contains alphabetic characters
        //val regex = "^[A-Za-z]+$".toRegex()
        val regex = "^[A-Za-z]+( [A-Za-z]+)*$".toRegex()

        return when {
            !username.matches(regex) -> false
            else -> true
        }
    }

    private fun showFullScreenDialog(username: String) {

        findViewById<LinearLayout>(R.id.dialog_welcome).visibility = VISIBLE



        Handler().postDelayed({
            val intent = Intent(this, WellnessFocusActivity::class.java)
            startActivity(intent)
            finishAffinity();
            //dialog.dismiss()
        }, 2000)

        val tvUsername = findViewById<TextView>(R.id.tv_username)
        tvUsername.text = username

    }
}