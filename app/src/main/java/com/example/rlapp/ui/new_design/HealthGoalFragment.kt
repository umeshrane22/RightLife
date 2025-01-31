package com.example.rlapp.ui.new_design

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.apimodel.userdata.Userdata
import com.example.rlapp.ui.new_design.pojo.HealthGoal
import com.example.rlapp.ui.new_design.pojo.OnboardingQuestionRequest
import com.example.rlapp.ui.utility.SharedPreferenceManager
import okhttp3.ResponseBody
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

        (activity as OnboardingQuestionnaireActivity).tvSkip.visibility = VISIBLE

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

        (activity as OnboardingQuestionnaireActivity).tvSkip.setOnClickListener {
            updateUserData(SharedPreferenceManager.getInstance(requireContext()).onboardingQuestionRequest)
            startActivity(
                Intent(
                    requireActivity(),
                    AwesomeScreenActivity::class.java
                )
            )
            finishAffinity(requireActivity())
            SharedPreferenceManager.getInstance(requireActivity())
                .clearOnboardingQuestionRequest()
        }

        btnContinue.setOnClickListener {
            llSelectedHealthGoal.visibility = VISIBLE
            rlHealthGoal.visibility = GONE
            tvSelectedHealthGoal.text = selectedHealthGoal
            tvDescription.visibility = GONE

            val onboardingQuestionRequest =
                SharedPreferenceManager.getInstance(requireContext()).onboardingQuestionRequest
            onboardingQuestionRequest.dailyGoalAchieveTime = selectedHealthGoal
            SharedPreferenceManager.getInstance(requireContext())
                .saveOnboardingQuestionAnswer(onboardingQuestionRequest)

            updateUserData(onboardingQuestionRequest)

            (activity as OnboardingQuestionnaireActivity).submitAnswer(onboardingQuestionRequest)
        }


        return view
    }

    override fun onPause() {
        super.onPause()
        llSelectedHealthGoal.visibility = GONE
        rlHealthGoal.visibility = VISIBLE
        tvDescription.visibility = VISIBLE
    }

    private fun updateUserData(onboardingQuestionRequest : OnboardingQuestionRequest) {

        val userData = Userdata()
        userData.gender = onboardingQuestionRequest.gender
        if (onboardingQuestionRequest.height != null) {
            val stringArray = (onboardingQuestionRequest.height)?.split(" ")
            userData.height = stringArray?.get(0)?.toDouble()
            userData.heightUnit = stringArray?.get(1)
        }
        if (onboardingQuestionRequest.weight != null) {
            val stringArray = (onboardingQuestionRequest.weight)?.split(" ")
            userData.weight = stringArray?.get(0)?.toDouble()
            userData.weightUnit = stringArray?.get(1)
        }

        val token = SharedPreferenceManager.getInstance(requireActivity()).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call: Call<ResponseBody> = apiService.updateUser(token, userData)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("AAAA", "Response = " + response.body())
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}