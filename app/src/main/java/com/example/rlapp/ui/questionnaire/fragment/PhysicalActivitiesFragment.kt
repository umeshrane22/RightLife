package com.example.rlapp.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rlapp.R
import com.example.rlapp.databinding.FragmentPhysicalActivitiesBinding
import com.example.rlapp.databinding.FragmentServingBinding
import com.example.rlapp.databinding.FragmentWaterCaffeineIntakeBinding
import com.example.rlapp.ui.questionnaire.QuestionnaireActivity
import com.example.rlapp.ui.questionnaire.adapter.ServingAdapter
import com.example.rlapp.ui.questionnaire.pojo.ServingItem

class PhysicalActivitiesFragment : Fragment() {

    private var _binding: FragmentPhysicalActivitiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhysicalActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.btnContinue.setOnClickListener {
            QuestionnaireActivity.navigateToNextPage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
