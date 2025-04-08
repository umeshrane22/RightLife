package com.jetsynthesys.rightlife.ui.mindaudit

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityMindAuditResultBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MindAuditResultActivity : AppCompatActivity() {
    private val allAssessments = java.util.ArrayList<String>()
    private var userEmotionsString: ArrayList<String> = ArrayList()
    private lateinit var binding: ActivityMindAuditResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMindAuditResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnTakeAssessment.setOnClickListener {
            startActivity(Intent(this, MindAuditFromActivity::class.java))
        }
        getAssessmentResult("CAS")
        val userEmotions = UserEmotions(userEmotionsString)
        SharedPreferenceManager.getInstance(this).saveUserEmotions(userEmotions)
        getSuggestedAssessment(userEmotions)
    }


    private fun getAssessmentResult(assessment: String) {
        val accessToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(
            ApiService::class.java
        )

        val call = apiService.getMindAuditAssessmentResult(accessToken,assessment)
        call.enqueue(object : Callback<MindAuditResultResponse?> {
            override fun onResponse(
                call: Call<MindAuditResultResponse?>,
                response: Response<MindAuditResultResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        binding.tvScore.text =
                            response.body()!!.result[0].assessmentsTaken[0].interpretations.anger.level.toString() + " " + response.body()!!.result[0].assessmentsTaken[0].interpretations.anger.score.toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                } else {
                    Toast.makeText(
                        this@MindAuditResultActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<MindAuditResultResponse?>, t: Throwable) {
                Toast.makeText(
                    this@MindAuditResultActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    //get all assesments
    private fun getSuggestedAssessment(userEmotions: UserEmotions) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            SharedPreferenceConstants.ACCESS_TOKEN,
            MODE_PRIVATE
        )
        val accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null)

        val apiService = ApiClient.getClient().create(
            ApiService::class.java
        )

        val call = apiService.getSuggestedAssessment(accessToken, userEmotions)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    //Toast.makeText(requireContext(), "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        val jsonString = response.body()!!.string()
                        val gson = Gson()
                        val assessments = gson.fromJson(
                            jsonString,
                            Assessments::class.java
                        )
                        handleAssessmentResult(assessments)
                        /*val intent =
                            Intent(this@MindAuditResultActivity, MASuggestedAssessmentActivity::class.java)
                        intent.putExtra("AssessmentData", assessments)
                        startActivity(intent)*/
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                } else {
                    Toast.makeText(
                        this@MindAuditResultActivity,
                        "Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@MindAuditResultActivity, "Network Error: " + t.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun handleAssessmentResult(assessments: Assessments?) {
        val assessment = assessments!!.allAssessment

        if (assessment.dass21 != null) {
            allAssessments.add(assessment.dass21)
        } else {
            allAssessments.add("DASS-21")
        }

        if (assessment.sleepAudit != null) {
            allAssessments.add(assessment.sleepAudit)
        } else {
            allAssessments.add("Sleep Audit")
        }

        if (assessment.gad7 != null) {
            allAssessments.add(assessment.gad7)
        } else {
            allAssessments.add("GAD-7")
        }
        if (assessment.ohq != null) {
            allAssessments.add(assessment.ohq)
        } else {
            allAssessments.add("OHQ")
        }

        if (assessment.cas != null) {
            allAssessments.add(assessment.cas)
        } else {
            allAssessments.add("CAS")
        }

        if (assessment.phq9 != null) {
            allAssessments.add(assessment.phq9)
        } else {
            allAssessments.add("PHQ-9")
        }
         for (assessment in allAssessments){
             addChip(assessment,false)
         }
    }

    private fun addChip(
        name: String,
        isSelected: Boolean = false
    ) {
        val chip = Chip(this)
        chip.id = View.generateViewId() // Generate unique ID
        chip.text = name
        chip.isCheckable = true
        chip.isChecked = false
        chip.chipCornerRadius = 50f
        chip.chipStrokeColor = ContextCompat.getColorStateList(
            this,
            R.color.color_think_right
        )

        chip.textSize = 13f

        val heightInDp = 50 // or whatever height you want
        val heightInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            heightInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            heightInPx
        )

        chip.layoutParams = layoutParams

        chip.isChecked = isSelected

        // Set different colors for selected state
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.color_think_right),
                ContextCompat.getColor(this, R.color.white)
            )
        )

        val textColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.color_think_right)
            )
        )
        chip.chipBackgroundColor = colorStateList


        chip.setOnClickListener { view ->
            val position = binding.chipGroup1.indexOfChild(view)
            val selectedChip = binding.chipGroup1.getChildAt(position) as Chip
            Log.d("selected chip", " --"+selectedChip.text.toString())
            getAssessmentResult(selectedChip.text.toString())
        }

        // Text color for selected state
        chip.setTextColor(textColorStateList)
        binding.chipGroup1.addView(chip)
    }
}