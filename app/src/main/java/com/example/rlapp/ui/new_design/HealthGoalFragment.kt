package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.ui.new_design.pojo.HealthGoal
import com.example.rlapp.ui.new_design.pojo.OnboardingQuestionRequest
import com.example.rlapp.ui.new_design.pojo.SaveUserInterestResponse
import com.example.rlapp.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HealthGoalFragment : Fragment() {

    private lateinit var llSelectedHealthGoal: LinearLayout
    private lateinit var rlHealthGoal: RelativeLayout
    private lateinit var tvSelectedHealthGoal: TextView
    private lateinit var tvDescription: TextView
    private var selectedHealthGoal = ""

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HealthGoalAdapter
    private val healthGoalList = ArrayList<HealthGoal>()

    companion object {
        fun newInstance(pageIndex: Int): HealthGoalFragment {
            val fragment = HealthGoalFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_health_goals, container, false)

        llSelectedHealthGoal = view.findViewById(R.id.ll_selected_health_goals)
        rlHealthGoal = view.findViewById(R.id.rl_health_goals)
        tvSelectedHealthGoal = view.findViewById(R.id.tv_selected_health_goals)
        recyclerView = view.findViewById(R.id.rv_health_goals)
        tvDescription = view.findViewById(R.id.tv_description)

        healthGoalList.add(HealthGoal("0-10 minutes"))
        healthGoalList.add(HealthGoal("10-20 minutes"))
        healthGoalList.add(HealthGoal("20-30 minutes"))
        healthGoalList.add(HealthGoal("30+ minutes"))

        val btnContinue = view.findViewById<Button>(R.id.btn_continue)

        val colorStateListSelected =
            ContextCompat.getColorStateList(requireContext(), R.color.menuselected)

        adapter = HealthGoalAdapter(requireContext(), healthGoalList) { healthGoal ->
            btnContinue.isEnabled = true
            btnContinue.backgroundTintList = colorStateListSelected
            selectedHealthGoal = healthGoal.header
        }

        recyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
        recyclerView.adapter = adapter

        btnContinue.setOnClickListener {
            llSelectedHealthGoal.visibility = View.VISIBLE
            rlHealthGoal.visibility = View.GONE
            tvSelectedHealthGoal.text = selectedHealthGoal
            tvDescription.visibility = View.GONE

            val onboardingQuestionRequest =
                SharedPreferenceManager.getInstance(requireContext()).onboardingQuestionRequest
            onboardingQuestionRequest.dailyGoalAchieveTime = selectedHealthGoal
            SharedPreferenceManager.getInstance(requireContext())
                .saveOnboardingQuestionAnswer(onboardingQuestionRequest)
            submitAnswer(onboardingQuestionRequest)
        }


        return view
    }

    private fun submitAnswer(onboardingQuestionRequest: OnboardingQuestionRequest) {
        val authToken = SharedPreferenceManager.getInstance(requireContext()).accessToken
        val apiService = ApiClient.getDevClient().create(ApiService::class.java)

        val call = apiService.submitOnBoardingAnswers(authToken, onboardingQuestionRequest)

        call.enqueue(object : Callback<SaveUserInterestResponse>{
            override fun onResponse(
                call: Call<SaveUserInterestResponse>,
                response: Response<SaveUserInterestResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()

                    Toast.makeText(
                        requireContext(),
                        apiResponse?.successMessage,
                        Toast.LENGTH_SHORT
                    ).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        // Submit Questions answer here
                        requireContext().startActivity(
                            Intent(
                                requireContext(),
                                AwesomeScreenActivity::class.java
                            )
                        )

                    }, 1000)

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SaveUserInterestResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}