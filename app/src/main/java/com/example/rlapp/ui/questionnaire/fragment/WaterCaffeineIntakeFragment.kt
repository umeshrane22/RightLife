package com.example.rlapp.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rlapp.databinding.FragmentWaterCaffeineIntakeBinding
import com.example.rlapp.ui.questionnaire.QuestionnaireEatRightActivity
import com.example.rlapp.ui.questionnaire.pojo.AnswerWaterCoffee
import com.example.rlapp.ui.questionnaire.pojo.Coffee
import com.example.rlapp.ui.questionnaire.pojo.ERQuestionFive
import com.example.rlapp.ui.questionnaire.pojo.Question
import com.example.rlapp.ui.questionnaire.pojo.Water

class WaterCaffeineIntakeFragment : Fragment() {

    private var _binding: FragmentWaterCaffeineIntakeBinding? = null
    private val binding get() = _binding!!

    private var question: Question? = null

    companion object {
        fun newInstance(question: Question): WaterCaffeineIntakeFragment {
            val fragment = WaterCaffeineIntakeFragment()
            val args = Bundle().apply {
                putSerializable("question", question)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = it.getSerializable("question") as? Question
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaterCaffeineIntakeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.btnContinue.setOnClickListener {
            //QuestionnaireEatRightActivity.navigateToNextPage()
            val answerWaterCoffee = AnswerWaterCoffee()
            val water = Water()
            water.cups = ""
            water.quantity = ""
            answerWaterCoffee.water = water
            val coffee = Coffee()
            coffee.cups = ""
            coffee.quantity = ""
            answerWaterCoffee.coffee = coffee
            submit(answerWaterCoffee)
        }
    }

    private fun submit(answer: AnswerWaterCoffee) {
        val questionFive = ERQuestionFive()
        questionFive.answer = answer
        QuestionnaireEatRightActivity.eatRightAnswerRequest.questionFive = questionFive
        QuestionnaireEatRightActivity.submitEatRightAnswerRequest(
            QuestionnaireEatRightActivity.eatRightAnswerRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
