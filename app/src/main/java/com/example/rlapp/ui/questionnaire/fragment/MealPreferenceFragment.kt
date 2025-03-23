package com.example.rlapp.ui.questionnaire.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rlapp.R
import com.example.rlapp.databinding.FragmentMealPreferenceBinding
import com.example.rlapp.ui.questionnaire.QuestionnaireActivity
import com.example.rlapp.ui.questionnaire.adapter.MealOptionAdapter
import com.example.rlapp.ui.questionnaire.pojo.MealOption

class MealPreferenceFragment : Fragment() {

    private var _binding: FragmentMealPreferenceBinding? = null
    private val binding get() = _binding!!

    private val mealOptions = listOf(
        MealOption(R.drawable.ic_meal_1, "1-2 meals—it's enough for me"),
        MealOption(R.drawable.ic_meal_2, "3 meals—a balanced routine"),
        MealOption(R.drawable.ic_meal_3, "4-5 meals—I eat often but manage well"),
        MealOption(R.drawable.ic_meal_4, "6+ meals—I eat throughout the day")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealPreferenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = MealOptionAdapter(mealOptions) { selectedOption ->
            Handler(Looper.getMainLooper()).postDelayed({
                QuestionnaireActivity.navigateToNextPage()
            },500)
        }
        binding.rvMealOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMealOptions.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
