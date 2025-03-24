package com.example.rlapp.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rlapp.databinding.FragmentExercisePreferenceBinding
import com.example.rlapp.ui.questionnaire.QuestionnaireEatRightActivity

class ExercisePreferenceFragment : Fragment() {
    private var _binding: FragmentExercisePreferenceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisePreferenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnAdd.setOnClickListener {
            val times = binding.inputTimes.text.toString()
            if (times.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter value", Toast.LENGTH_SHORT).show()
            } else {
                QuestionnaireEatRightActivity.navigateToNextPage()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}