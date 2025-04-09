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
              /*      try {
                        if (response.body()!!.result.isNullOrEmpty()){

                            binding.rlAssessmentNotTaken.visibility = View.GONE
                            binding.scrollviewResult.visibility = View.VISIBLE
                        // binding.tvMainScore.text = response.body()!!.result[0].assessmentsTaken[0].interpretations.anger.level.toString() + " " + response.body()!!.result[0].assessmentsTaken[0].interpretations.anger.score.toString()
                            handleAssessmentScore(response)
                        }else{
                            binding.rlAssessmentNotTaken.visibility = View.VISIBLE
                            binding.scrollviewResult.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }*/

                    try {
                        val resultList = response.body()?.result

                        if (!resultList.isNullOrEmpty()) {
                            // Result exists, show result layout
                            binding.rlAssessmentNotTaken.visibility = View.GONE
                            binding.scrollviewResult.visibility = View.VISIBLE

                            handleAssessmentScore(response)
                        } else {
                            // No result, show "not taken" layout
                            binding.rlAssessmentNotTaken.visibility = View.VISIBLE
                            binding.scrollviewResult.visibility = View.GONE
                        }
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


    private fun handleAssessmentScore(response: Response<MindAuditResultResponse?>) {
        var assessmentTaken = response.body()!!.result[0].assessmentsTaken[0]
        with(binding) {
            when (assessmentTaken.assessment) {
                "DASS-21" -> {
                    assessmentTaken.interpretations.anxiety?.let {
                        cardviewMainscore.visibility = View.VISIBLE
                        binding.mainScoreTitle.text = "Anxiety"
                        binding.mainScoreTitle.visibility = View.VISIBLE
                        binding.mainScoreLevel.text = assessmentTaken.interpretations.anxiety.level
                        binding.mainScoreLevel.visibility = View.VISIBLE
                        binding.tvMainScore.text = assessmentTaken.interpretations.anxiety.score.toString()
                        binding.cardviewMainscore.setCardBackgroundColor(getResources().getColor(getColorResForScore(assessmentTaken.interpretations.anxiety.score)))
                        setExplanationTitle(assessmentTaken.interpretations.anxiety.level,"Anxiety")
                        setRainbowView(assessmentTaken.interpretations.anxiety.score.toInt())
                        //setLeftRainbowView(assessmentTaken.interpretations.anxiety.score.toInt())
                    }
                    assessmentTaken.interpretations.depression?.let {
                        cardviewMainscore2.visibility = View.VISIBLE
                        binding.mainScoreTitle2.text = "Depression"
                        binding.mainScoreTitle2.visibility = View.VISIBLE
                        binding.mainScoreLevel2.text = assessmentTaken.interpretations.depression.level
                        binding.mainScoreLevel2.visibility = View.VISIBLE
                        binding.tvMainScore2.text = assessmentTaken.interpretations.depression.score.toString()
                        setExplanationTitle(assessmentTaken.interpretations.depression.level,"Depression")
                        binding.cardviewMainscore2.setCardBackgroundColor(getResources().getColor(getColorResForScore(assessmentTaken.interpretations.depression.score)))
                        //setCenterRainbowView(assessmentTaken.interpretations.depression.score.toInt())
                    }
                    assessmentTaken.interpretations.stress?.let {
                        cardviewMainscore3.visibility = View.VISIBLE
                        binding.mainScoreTitle3.text = "Stress"
                        binding.mainScoreTitle3.visibility = View.VISIBLE
                        binding.mainScoreLevel3.text = assessmentTaken.interpretations.stress.level
                        binding.mainScoreLevel3.visibility = View.VISIBLE
                        binding.tvMainScore3.text = assessmentTaken.interpretations.stress.score.toString()
                        binding.cardviewMainscore3.setCardBackgroundColor(getResources().getColor(getColorResForScore(assessmentTaken.interpretations.stress.score)))
                        setExplanationTitle(assessmentTaken.interpretations.stress.level,"Stress")
                        //setRightRainbowView(assessmentTaken.interpretations.stress.score.toInt())
                    }
                    scoreBarContainer.visibility = View.VISIBLE
                    scoreBarContainerhappiness.visibility = View.GONE
                }

                "PHQ-9" -> {
                    assessmentTaken.interpretations.depression?.let {
                        binding.mainScoreTitle.text = "Depression"
                        binding.mainScoreTitle.visibility = View.VISIBLE
                        binding.mainScoreLevel.text = assessmentTaken.interpretations.depression.level
                        binding.mainScoreLevel.visibility = View.VISIBLE
                        binding.tvMainScore.text = assessmentTaken.interpretations.depression.score.toString()
                        setRainbowView(assessmentTaken.interpretations.depression.score.toInt())
                        binding.cardviewMainscore.setCardBackgroundColor(getResources().getColor(getColorResForScore(assessmentTaken.interpretations.depression.score)))
                        setExplanationTitle(assessmentTaken.interpretations.depression.level,"Depression")
                    }
                    cardviewMainscore3.visibility = View.GONE
                    cardviewMainscore2.visibility = View.GONE
                    scoreBarContainer.visibility = View.VISIBLE
                    scoreBarContainerhappiness.visibility = View.GONE
                }

                "OHQ" -> {
                    assessmentTaken.interpretations.happiness?.let {
                        binding.mainScoreTitle.text = "Happiness"
                        binding.mainScoreTitle.visibility = View.VISIBLE
                        binding.mainScoreLevel.text = assessmentTaken.interpretations.happiness.level
                        binding.mainScoreLevel.visibility = View.VISIBLE
                        binding.tvMainScore.text = assessmentTaken.interpretations.happiness.score.toString()
                        setHappinessRainbowView(assessmentTaken.interpretations.happiness.score.toInt())
                        binding.cardviewMainscore.setCardBackgroundColor(getResources().getColor(getColorResForScore(assessmentTaken.interpretations.happiness.score.toInt())))
                        setExplanationTitle(assessmentTaken.interpretations.happiness.level,"Happiness")
                    }
                    cardviewMainscore3.visibility = View.GONE
                    cardviewMainscore2.visibility = View.GONE
                    scoreBarContainer.visibility = View.GONE
                    scoreBarContainerhappiness.visibility = View.VISIBLE
                }

                "GAD-7" -> {
                    assessmentTaken.interpretations.anxiety?.let {
                        binding.mainScoreTitle.text = "Anxiety"
                        binding.mainScoreTitle.visibility = View.VISIBLE
                        binding.mainScoreLevel.text = assessmentTaken.interpretations.anxiety.level
                        binding.mainScoreLevel.visibility = View.VISIBLE
                        binding.tvMainScore.text =
                            assessmentTaken.interpretations.anxiety.score.toString()
                        setRainbowView(assessmentTaken.interpretations.anxiety.score)
                        binding.cardviewMainscore.setCardBackgroundColor(getResources().getColor(getColorResForScore(assessmentTaken.interpretations.anxiety.score)))
                        setExplanationTitle(assessmentTaken.interpretations.anxiety.level,"Anxiety")
                    }
                    cardviewMainscore3.visibility = View.GONE
                    cardviewMainscore2.visibility = View.GONE
                    scoreBarContainer.visibility = View.VISIBLE
                    scoreBarContainerhappiness.visibility = View.GONE
                }

                "CAS" -> {
                    assessmentTaken.interpretations.anger?.let {
                        binding.mainScoreTitle.text = "Anger"
                        binding.mainScoreTitle.visibility = View.VISIBLE
                        binding.mainScoreLevel.text = assessmentTaken.interpretations.anger.level
                        binding.mainScoreLevel.visibility = View.VISIBLE
                        binding.tvMainScore.text = assessmentTaken.interpretations.anger.score.toString()
                        setRainbowView(assessmentTaken.interpretations.anger.score)
                        binding.cardviewMainscore.setCardBackgroundColor(getResources().getColor(getColorResForScore(assessmentTaken.interpretations.anger.score)))
                        setExplanationTitle(assessmentTaken.interpretations.anger.level,"Anger")
                    }
                    cardviewMainscore3.visibility = View.GONE
                    cardviewMainscore2.visibility = View.GONE
                    scoreBarContainer.visibility = View.VISIBLE
                    scoreBarContainerhappiness.visibility = View.GONE
                }

                else -> {}
            }
        }


    }

    private fun setLeftRainbowView(score: Int) {
        binding.leftArc.visibility = View.VISIBLE
        binding.leftArc.setRainbowColors(getColorArrayForScore(score))
        binding.leftArc.setStrokeWidth(30f)
        binding.leftArc.setArcSpacing(8f)
        binding.leftArc.startAngle = 180f
        binding.leftArc.sweepAngle = 90f
        binding.leftArc.invalidate()
    }
    private fun setRightRainbowView(score: Int) {
        binding.rightArc.visibility = View.VISIBLE
        binding.rightArc.setRainbowColors(getColorArrayForScore(score))
        binding.rightArc.setStrokeWidth(30f)
        binding.rightArc.setArcSpacing(8f)
        binding.rightArc.startAngle = 270f
        binding.rightArc.sweepAngle = 90f
    }
    private fun setCenterRainbowView(score: Int) {
        binding.centerArc.visibility = View.VISIBLE
        binding.centerArc.setRainbowColors(getColorArrayForScore(score))
        binding.centerArc.setStrokeWidth(30f)
        binding.centerArc.setArcSpacing(8f)
        binding.centerArc.startAngle = 180f
        binding.centerArc.sweepAngle = 180f
    }

    private fun setRainbowView(score: Int) {
        binding.rainbowView.visibility = View.VISIBLE
        binding.rainbowView.setRainbowColors(getColorArrayForScore(score))
        binding.rainbowView.setStrokeWidth(60f)
        binding.rainbowView.setArcSpacing(8f)
    }
    private fun setHappinessRainbowView(score: Int) {
        binding.rainbowView.visibility = View.VISIBLE
        binding.rainbowView.setRainbowColors(getColorArrayForHappinessScore(7))
        binding.rainbowView.setStrokeWidth(40f)
        binding.rainbowView.setArcSpacing(8f)
    }

    private fun setExplanationTitle(level: String?, title: String) {
        binding.tvResultExplanationTitle.text = level+" "+title
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

    fun getColorResForScore(score: Int): Int {
        return when (score) {
            in 0..3 -> R.color.green_minimal
            in 4..8 -> R.color.cyan_mild
            in 9..13 -> R.color.blue_moderate
            in 14..18 -> R.color.orange_severe
            else -> R.color.red_ext_severe
        }
    }


    fun getColorArrayForScore(score: Int): IntArray {
        val colorLevels = listOf(
            0xFF06B27B.toInt(), // green_minimal
            0xFF54C8DB.toInt(), // cyan_mild
            0xFF57A3FC.toInt(), // blue_moderate
            0xFFFFBD44.toInt(), // orange_severe
            0xFFFC6656.toInt()  // red_ext_severe
        )

        val fallbackColor = 0xFFEFF0F6.toInt() // light gray for unfilled slots

        val activeColorCount = when (score) {
            in 0..3 -> 1
            in 4..8 -> 2
            in 9..13 -> 3
            in 14..18 -> 4
            else -> 5
        }

        return IntArray(5) { index ->
            if (index < activeColorCount) colorLevels[index] else fallbackColor
        }
    }

    fun getColorArrayForHappinessScore(score: Int): IntArray {
        /*val colorLevels = listOf(
            0xFF06B27B.toInt(), // green_minimal
            0xFF54C8DB.toInt(), // cyan_mild
            0xFF57A3FC.toInt(), // blue_moderate
            0xFFFFBD44.toInt(), // orange_severe
            0xFFFC6656.toInt()  // red_ext_severe
        )*/
        //#F7E6B7,#FDE6FF,#FDD3D2,#A6E0CE,#C5DEF9,#D5C6ED,#D9DBE9
        val colorLevels = listOf(
            0xFFD9DBE9.toInt(), // pastel lavender
            0xFFD5C6ED.toInt(), // soft lilac
            0xFFC5DEF9.toInt(), // light sky blue
            0xFFA6E0CE.toInt(), // mint green
            0xFFFDD3D2.toInt(), // soft peach
            0xFFFDE6FF.toInt(), // light pink-lavender
            0xFFF7E6B7.toInt()  // soft beige
        )


        val fallbackColor = 0xFFEFF0F6.toInt() // light gray for unfilled slots

        val activeColorCount = when (score) {
            in 0..2 -> 1
            in 2..3 -> 2
            in 3..4 -> 3
            in 4..5 -> 4
            in 5..6 -> 5
            in 6..7 -> 6
            else -> 7
        }

        return IntArray(7) { index ->
            if (index < activeColorCount) colorLevels[index] else fallbackColor
        }
    }

}