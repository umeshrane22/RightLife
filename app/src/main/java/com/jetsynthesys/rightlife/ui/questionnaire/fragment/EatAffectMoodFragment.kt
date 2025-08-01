package com.jetsynthesys.rightlife.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.FragmentEatAffectMoodBinding
import com.jetsynthesys.rightlife.ui.questionnaire.QuestionnaireEatRightActivity
import com.jetsynthesys.rightlife.ui.questionnaire.adapter.ScheduleOptionAdapter
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.ERQuestionSeven
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.Question
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.ScheduleOption
import com.jetsynthesys.rightlife.ui.utility.runWithCooldown

class EatAffectMoodFragment : Fragment() {

    private var _binding: FragmentEatAffectMoodBinding? = null
    private val binding get() = _binding!!

    private val scheduleOptions = listOf(
        ScheduleOption(R.drawable.ic_eat_affect_1, "Never", "I eat only when I’m hungry"),
        ScheduleOption(R.drawable.ic_eat_affect_2, "Rarely", "I control my emotions"),
        ScheduleOption(R.drawable.ic_eat_affect_3, "Sometimes", "it depends on the day"),
        ScheduleOption(R.drawable.ic_eat_affect_4, "Always", "I eat based on my mood"),
    )

    private var question: Question? = null

    companion object {
        fun newInstance(question: Question): EatAffectMoodFragment {
            val fragment = EatAffectMoodFragment()
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
        _binding = FragmentEatAffectMoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter =
            ScheduleOptionAdapter(scheduleOptions, "EatRight", { selectedOption: ScheduleOption ->
                submit(selectedOption.title)
            }.runWithCooldown())
        binding.rvScheduleOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvScheduleOptions.adapter = adapter
    }

    private fun submit(answer: String) {
        val questionSeven = ERQuestionSeven()
        questionSeven.answer = answer
        QuestionnaireEatRightActivity.questionnaireAnswerRequest.eatRight?.questionSeven =
            questionSeven
        QuestionnaireEatRightActivity.submitQuestionnaireAnswerRequest(
            QuestionnaireEatRightActivity.questionnaireAnswerRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
