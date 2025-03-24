package com.example.rlapp.ui.questionnaire.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rlapp.databinding.FragmentShakeOffBadDayBinding
import com.example.rlapp.ui.questionnaire.QuestionnaireThinkRightActivity
import com.example.rlapp.ui.questionnaire.adapter.ShakeOffBadDayAdapter

class ShakeOffBadDayFragment : Fragment() {

    private var _binding: FragmentShakeOffBadDayBinding? = null
    private val binding get() = _binding!!

    private val shakeOffOptions = listOf(
        "Right away",
        "A few hours",
        "By the next day",
        "A few days",
        "It takes a long time"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShakeOffBadDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ShakeOffBadDayAdapter(shakeOffOptions) {
            Handler(Looper.getMainLooper()).postDelayed({
                QuestionnaireThinkRightActivity.navigateToNextPage()
            }, 500)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}