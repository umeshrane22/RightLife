package com.example.rlapp.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rlapp.databinding.FragmentEmotionsPastWeekBinding
import com.example.rlapp.databinding.FragmentEnergyLevelBinding
import com.example.rlapp.ui.questionnaire.QuestionnaireEatRightActivity

class EmotionsPastWeekFragment : Fragment() {

    private var _binding: FragmentEmotionsPastWeekBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionsPastWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnContinue.setOnClickListener {
            QuestionnaireEatRightActivity.navigateToNextPage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
