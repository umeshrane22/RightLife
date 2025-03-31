package com.example.rlapp.ai_package.ui.thinkright.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.rlapp.R
import com.example.rlapp.ai_package.base.BaseFragment
import com.example.rlapp.ai_package.data.repository.ApiClient
import com.example.rlapp.ai_package.model.SleepLandingResponse
import com.example.rlapp.ai_package.model.ThinkQuoteResponse
import com.example.rlapp.databinding.FragmentViewQuoteBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewQuoteFragment  : BaseFragment<FragmentViewQuoteBinding>() {

    private lateinit var tvQuote: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var thinkQuoteResponse : ThinkQuoteResponse

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentViewQuoteBinding
        get() = FragmentViewQuoteBinding::inflate
    var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvQuote = view.findViewById(R.id.tv_quote)
        tvAuthor = view.findViewById(R.id.tv_author)
        val backButton = view.findViewById<ImageView>(R.id.img_back)

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)
        fetchQuoteData()



        backButton.setOnClickListener {
            navigateToFragment(ThinkRightReportFragment(), "ThinkRightLandingFragment")
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(ThinkRightReportFragment(), "ThinkRightLandingFragment")
            }
        })
    }
    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
    private fun fetchQuoteData() {
        progressDialog.show()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val call = ApiClient.apiService.quoteOfDay(token)
        call.enqueue(object : Callback<ThinkQuoteResponse> {
            override fun onResponse(call: Call<ThinkQuoteResponse>, response: Response<ThinkQuoteResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    thinkQuoteResponse = response.body()!!
                    tvQuote.setText(thinkQuoteResponse.data.getOrNull(0)?.quote)
                    tvAuthor.setText("-" + thinkQuoteResponse.data.getOrNull(0)?.authorName)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
            override fun onFailure(call: Call<ThinkQuoteResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }
}