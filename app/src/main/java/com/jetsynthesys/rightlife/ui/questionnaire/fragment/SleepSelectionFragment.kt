package com.jetsynthesys.rightlife.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.FragmentSleepSelectionBinding
import com.jetsynthesys.rightlife.ui.questionnaire.QuestionnaireThinkRightActivity
import com.jetsynthesys.rightlife.ui.questionnaire.adapter.SleepSelectionAdapter
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.Question
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.SRQuestionFour
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.SleepOption
import com.jetsynthesys.rightlife.ui.utility.runWithCooldown

class SleepSelectionFragment : Fragment() {

    private var _binding: FragmentSleepSelectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SleepSelectionAdapter

    private val sleepOptions = listOf(
        SleepOption("Sleep through the night", "", R.drawable.ic_sleep_option_1),
        SleepOption("Rarely", "(1–2 times/week)", R.drawable.ic_sleep_option_2),
        SleepOption("Occasionally", "(few nights/week)", R.drawable.ic_sleep_option_3),
        SleepOption("Frequently", "(almost every night)", R.drawable.ic_sleep_option_4)
    )

    private var question: Question? = null

    companion object {
        fun newInstance(question: Question): SleepSelectionFragment {
            val fragment = SleepSelectionFragment()
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
        _binding = FragmentSleepSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SleepSelectionAdapter(sleepOptions, { sleepOption: SleepOption ->
            submit(sleepOption.title)
        }.runWithCooldown())

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
    }

    private fun submit(answer: String) {
        val questionFour = SRQuestionFour()
        questionFour.answer = answer
        QuestionnaireThinkRightActivity.questionnaireAnswerRequest.sleepRight?.questionFour =
            questionFour
        QuestionnaireThinkRightActivity.submitQuestionnaireAnswerRequest(
            QuestionnaireThinkRightActivity.questionnaireAnswerRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
