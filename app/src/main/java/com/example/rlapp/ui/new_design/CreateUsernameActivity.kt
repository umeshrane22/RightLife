package com.example.rlapp.ui.new_design

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rlapp.R

class CreateUsernameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_username)

        val edtUsername = findViewById<EditText>(R.id.edt_username)
        val tvCharLeft = findViewById<TextView>(R.id.tv_char_left)
        val btnContinue = findViewById<Button>(R.id.btn_continue)
        val tvError = findViewById<TextView>(R.id.tv_username_error)
        val charLeft = edtUsername.text.length
        "$charLeft/20 ch".also { tvCharLeft.text = it }

        edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                "$count/20 ch".also { tvCharLeft.text = it }
                if (validateUsername(p0.toString()))
                    tvError.visibility = GONE
                else
                    tvError.visibility = VISIBLE
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        btnContinue.setOnClickListener {
            showFullScreenDialog(edtUsername.text.toString())
        }
    }

    fun validateUsername(username: String): Boolean {
        // Check if the username only contains alphabetic characters
        val regex = "^[A-Za-z]+$".toRegex()

        return when {
            !username.matches(regex) -> false
            else -> true
        }
    }

    private fun showFullScreenDialog(username: String) {

        // Create the dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_welcome)
        dialog.setCancelable(false)
        val window = dialog.window
        window.let {
            val params = it?.attributes
            params?.width = WindowManager.LayoutParams.MATCH_PARENT
            params?.height = WindowManager.LayoutParams.MATCH_PARENT
            it?.attributes = params
        }

        Handler().postDelayed({
            val intent = Intent(this, WellnessFocusActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }, 2000)

        val tvUsername = dialog.findViewById<TextView>(R.id.tv_username)
        tvUsername.text = username
        dialog.show()
    }
}