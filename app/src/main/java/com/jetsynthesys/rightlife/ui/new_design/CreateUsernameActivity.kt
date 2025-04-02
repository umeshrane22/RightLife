package com.jetsynthesys.rightlife.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.apimodel.userdata.Userdata
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.jetsynthesys.rightlife.ui.utility.Utils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateUsernameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_username)


        var username = intent.getStringExtra("USERNAME_KEY")
        var email = intent.getStringExtra("EMAIL")


        val edtUsername = findViewById<EditText>(R.id.edt_username)
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        if (!username.isNullOrEmpty()) {
            edtUsername.setText(username) // Set the username to the EditText
        } else if (!sharedPreferenceManager.displayName.isNullOrEmpty()) {
            username = sharedPreferenceManager.userName
            edtUsername.setText(sharedPreferenceManager.displayName) // Set the username to the EditText
            email = sharedPreferenceManager.email
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
        } else {
            tvError.visibility = VISIBLE
            btnContinue.backgroundTintList = colorStateList
            btnContinue.isEnabled = false
        }

        "$charLeft/20 ch".also { tvCharLeft.text = it }

        edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                val c = edtUsername.text.length
                "$c/20 ch".also { tvCharLeft.text = it }
                if (validateUsername(p0.toString())) {
                    tvError.visibility = GONE
                    btnContinue.backgroundTintList = colorStateListSelected
                    btnContinue.isEnabled = true
                } else {
                    tvError.visibility = VISIBLE
                    btnContinue.backgroundTintList = colorStateList
                    btnContinue.isEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        btnContinue.setOnClickListener {
            Utils.hideSoftKeyboard(this@CreateUsernameActivity)
            showFullScreenDialog(edtUsername.text.toString(), email!!)
            sharedPreferenceManager.userName = edtUsername.text.toString()
        }
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

    private fun showFullScreenDialog(username: String, email: String) {

        findViewById<LinearLayout>(R.id.dialog_welcome).visibility = VISIBLE
        val userdata = Userdata()
        userdata.firstName = username
        userdata.email = email
        updateUserData(userdata)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WellnessFocusActivity::class.java)
            startActivity(intent)
            findViewById<LinearLayout>(R.id.dialog_welcome).visibility = GONE
            //dialog.dismiss()
        }, 2000)

        val tvUsername = findViewById<TextView>(R.id.tv_username)
        tvUsername.text = username

    }

    private fun updateUserData(userdata: Userdata) {
        val token = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call: Call<ResponseBody> = apiService.updateUser(token, userdata)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("AAAA", "Response = " + response.body())
                } else {
                    Toast.makeText(
                        this@CreateUsernameActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(
                    this@CreateUsernameActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}