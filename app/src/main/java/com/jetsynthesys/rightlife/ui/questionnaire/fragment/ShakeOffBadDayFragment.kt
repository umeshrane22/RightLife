package com.jetsynthesys.rightlife.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.databinding.FragmentShakeOffBadDayBinding
import com.jetsynthesys.rightlife.ui.questionnaire.QuestionnaireThinkRightActivity
import com.jetsynthesys.rightlife.ui.questionnaire.adapter.ShakeOffBadDayAdapter
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.Question
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.TRQuestionTwo
import com.jetsynthesys.rightlife.ui.utility.runWithCooldown

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

    private var question: Question? = null

    companion object {
        fun newInstance(question: Question): ShakeOffBadDayFragment {
            val fragment = ShakeOffBadDayFragment()
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
        _binding = FragmentShakeOffBadDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ShakeOffBadDayAdapter(shakeOffOptions, { shakeOffOption: String ->
            submit(shakeOffOption)
        }.runWithCooldown())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun submit(answer: String) {
        val questionTwo = TRQuestionTwo()
        questionTwo.answer = answer
        QuestionnaireThinkRightActivity.questionnaireAnswerRequest.thinkRight?.questionTwo =
            questionTwo
        QuestionnaireThinkRightActivity.submitQuestionnaireAnswerRequest(
            QuestionnaireThinkRightActivity.questionnaireAnswerRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}