package com.example.rlapp.ui.affirmation

import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.databinding.ActivityTodaysAffirmationBinding
import com.example.rlapp.ui.affirmation.pojo.AffirmationCategoryData
import com.example.rlapp.ui.affirmation.pojo.AffirmationCategoryListResponse
import com.example.rlapp.ui.affirmation.pojo.AffirmationSelectedCategoryResponse
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TodaysAffirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodaysAffirmationBinding
    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodaysAffirmationBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        setupBottomSheet()
        getCategoryList()
    }

    private fun setupBottomSheet() {
        // Create and configure BottomSheetDialog
        bottomSheetDialog = BottomSheetDialog(this)

        // Inflate the BottomSheet layout
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)

        // Set up RecyclerView in the BottomSheet
        recyclerViewCategory = bottomSheetView.findViewById(R.id.recyclerView)
        recyclerViewCategory.setLayoutManager(LinearLayoutManager(this))
        //recyclerViewCategory.setAdapter(myAdapter)

        bottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        val ivClose = bottomSheetView.findViewById<ImageView>(R.id.ivClose)
        ivClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

    }

    private fun setupCategoryAdapter(categoryList: List<AffirmationCategoryData>) {
        val affirmationCategoryListAdapter = AffirmationCategoryListAdapter(
            this, categoryList
        ) { category ->
            getSelectedCategoryData(category.id)
            if (bottomSheetDialog.isShowing)
                bottomSheetDialog.dismiss()
        }
        recyclerViewCategory.adapter = affirmationCategoryListAdapter
    }

    private fun getCategoryList() {
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getAffirmationCategoryList(authToken)

        call.enqueue(object : Callback<AffirmationCategoryListResponse> {
            override fun onResponse(
                call: Call<AffirmationCategoryListResponse>,
                response: Response<AffirmationCategoryListResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("AAAA", "AffirmationListResponse = $response")
                    response.body()?.data?.let { setupCategoryAdapter(it) }
                    bottomSheetDialog.show()
                } else {
                    Toast.makeText(
                        this@TodaysAffirmationActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AffirmationCategoryListResponse>, t: Throwable) {
                Toast.makeText(
                    this@TodaysAffirmationActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun getSelectedCategoryData(id: String?) {
        val authToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.getAffirmationSelectedCategoryData(authToken, id)

        call.enqueue(object : Callback<AffirmationSelectedCategoryResponse> {
            override fun onResponse(
                call: Call<AffirmationSelectedCategoryResponse>,
                response: Response<AffirmationSelectedCategoryResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("AAAA", "AffirmationSelectedCategoryResponse = $response")
                } else {
                    Toast.makeText(
                        this@TodaysAffirmationActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AffirmationSelectedCategoryResponse>, t: Throwable) {
                Toast.makeText(
                    this@TodaysAffirmationActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}