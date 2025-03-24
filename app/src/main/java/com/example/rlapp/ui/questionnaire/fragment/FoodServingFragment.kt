package com.example.rlapp.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rlapp.R
import com.example.rlapp.databinding.FragmentServingBinding
import com.example.rlapp.ui.questionnaire.QuestionnaireEatRightActivity
import com.example.rlapp.ui.questionnaire.adapter.ServingAdapter
import com.example.rlapp.ui.questionnaire.pojo.ServingItem

class FoodServingFragment : Fragment() {

    private var _binding: FragmentServingBinding? = null
    private val binding get() = _binding!!
    private var fruitCount = 0
    private var vegetableCount = 0

    private val foodList = arrayListOf(
        ServingItem(R.drawable.ic_fruits,"Fruits",  "Serving per day",1),
        ServingItem(R.drawable.ic_vegitables,"Vegetables",  "Per day",1)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ServingAdapter(foodList){ servingItem ->
            when(servingItem.title){
                "Fruits" -> fruitCount = servingItem.count
                "Vegetables"-> vegetableCount = servingItem.count
            }
        }
        binding.recyclerViewServing.adapter = adapter
        binding.recyclerViewServing.layoutManager = LinearLayoutManager(requireContext())

        binding.btnContinue.setOnClickListener {
            QuestionnaireEatRightActivity.navigateToNextPage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
