package com.jetsynthesys.rightlife.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.FragmentSleepTimeBinding
import com.jetsynthesys.rightlife.ui.questionnaire.QuestionnaireThinkRightActivity
import com.jetsynthesys.rightlife.ui.questionnaire.adapter.ScheduleOptionAdapter
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.Question
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.SRQuestionTwo
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.ScheduleOption
import com.jetsynthesys.rightlife.ui.utility.runWithCooldown

class SleepTimeFragment : Fragment() {

    private var _binding: FragmentSleepTimeBinding? = null
    private val binding get() = _binding!!

    private val scheduleOptions = listOf(
        ScheduleOption(R.drawable.ic_st_1, "I always sleep at the same time", ""),
        ScheduleOption(R.drawable.ic_st_2, "I’m mostly consistent, with slight variations", ""),
        ScheduleOption(R.drawable.ic_no_3_q6, "I hardly follow a routine", ""),
        ScheduleOption(R.drawable.ic_no_4_q6, "My bedtime is reliant on external factors", ""),
    )

    private var question: Question? = null

    companion object {
        fun newInstance(question: Question): SleepTimeFragment {
            val fragment = SleepTimeFragment()
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
        _binding = FragmentSleepTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter =
            ScheduleOptionAdapter(scheduleOptions, "SleepRight", { selectedOption: ScheduleOption ->
                submit(selectedOption.title)
            }.runWithCooldown())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun submit(answer: String) {
        val questionTwo = SRQuestionTwo()
        questionTwo.answer = answer
        QuestionnaireThinkRightActivity.questionnaireAnswerRequest.sleepRight?.questionTwo =
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
