package com.example.rlapp.ui.new_design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ui.new_design.pojo.StressManagement
import com.example.rlapp.ui.utility.SharedPreferenceManager

class StressManagementSelectionFragment : Fragment() {
    private lateinit var llSelectedStressManagement: LinearLayout
    private lateinit var rlStressManagement: RelativeLayout
    private lateinit var tvSelectedStressManagementHeader: TextView
    private lateinit var tvSelectedStressManagementDesc: TextView
    private lateinit var selectedStressManagement: StressManagement

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StressManagementAdapter
    private val stressManagementList = ArrayList<StressManagement>()

    companion object {
        fun newInstance(pageIndex: Int): StressManagementSelectionFragment {
            val fragment = StressManagementSelectionFragment()
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
            inflater.inflate(R.layout.fragment_stress_management_selection, container, false)

        llSelectedStressManagement = view.findViewById(R.id.ll_selected_stress_management)
        rlStressManagement = view.findViewById(R.id.rl_stress_management)
        tvSelectedStressManagementHeader =
            view.findViewById(R.id.tv_selected_stress_management_header)
        tvSelectedStressManagementDesc = view.findViewById(R.id.tv_selected_stress_management_desc)

        recyclerView = view.findViewById(R.id.rv_stress_management)

        (activity as OnboardingQuestionnaireActivity).tvSkip.visibility = VISIBLE

        val btnContinue = view.findViewById<Button>(R.id.btn_continue)

        stressManagementList.add(
            StressManagement(
                "Beginner",
                "I occasionally meditate or practise breathing exercises.",
                R.drawable.beginer
            )
        )

        stressManagementList.add(
            StressManagement(
                "Intermediate",
                "I haven’t tried mindfulness or stress management yet.",
                R.drawable.intermediate
            )
        )

        stressManagementList.add(
            StressManagement(
                "Advanced",
                "I regularly practise mindfulness, journaling, or stress-relief techniques.",
                R.drawable.advance
            )
        )

        stressManagementList.add(
            StressManagement(
                "Expert",
                "I have a solid routine for managing stress and maintaining mental clarity.",
                R.drawable.expert
            )
        )

        recyclerView.setLayoutManager(LinearLayoutManager(requireContext()))

        adapter =
            StressManagementAdapter(requireContext(), stressManagementList) { stressManagement ->
                selectedStressManagement = stressManagement
                btnContinue.isEnabled = true
                val colorStateList =
                    ContextCompat.getColorStateList(requireContext(), R.color.menuselected)
                btnContinue.backgroundTintList = colorStateList
            }

        recyclerView.adapter = adapter


        btnContinue.setOnClickListener {
            llSelectedStressManagement.visibility = VISIBLE
            rlStressManagement.visibility = View.GONE
            tvSelectedStressManagementHeader.text = selectedStressManagement.header
            tvSelectedStressManagementDesc.text = selectedStressManagement.description

            val onboardingQuestionRequest =
                SharedPreferenceManager.getInstance(requireContext()).onboardingQuestionRequest
            onboardingQuestionRequest.experienceStressMindfulManagement =
                selectedStressManagement.header
            SharedPreferenceManager.getInstance(requireContext())
                .saveOnboardingQuestionAnswer(onboardingQuestionRequest)
            (activity as OnboardingQuestionnaireActivity).submitAnswer(onboardingQuestionRequest)
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        llSelectedStressManagement.visibility = GONE
        rlStressManagement.visibility = VISIBLE
    }
}