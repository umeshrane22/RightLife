package com.jetsynthesys.rightlife.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.FragmentExcerciseLocationBinding
import com.jetsynthesys.rightlife.ui.questionnaire.QuestionnaireEatRightActivity
import com.jetsynthesys.rightlife.ui.questionnaire.adapter.ScheduleOptionAdapter
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.MRQuestionFour
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.Question
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.ScheduleOption
import com.jetsynthesys.rightlife.ui.utility.runWithCooldown

class ExerciseLocationFragment : Fragment() {

    private var _binding: FragmentExcerciseLocationBinding? = null
    private val binding get() = _binding!!

    private val scheduleOptions = listOf(
        ScheduleOption(R.drawable.ic_gym, "Gym", ""),
        ScheduleOption(R.drawable.ic_home_exercise, "At home", ""),
        ScheduleOption(R.drawable.ic_work, "At work", ""),
        ScheduleOption(R.drawable.ic_online_live, "Online/Virtual classes", ""),
        ScheduleOption(R.drawable.ic_dont_fix_place, "I don’t have a fixed place", ""),
    )

    private var question: Question? = null

    companion object {
        fun newInstance(question: Question): ExerciseLocationFragment {
            val fragment = ExerciseLocationFragment()
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
        _binding = FragmentExcerciseLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter =
            ScheduleOptionAdapter(scheduleOptions, "MoveRight", { selectedOption: ScheduleOption ->
                submit(selectedOption.title)
            }.runWithCooldown())
        binding.rvScheduleOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvScheduleOptions.adapter = adapter
    }

    private fun submit(answer: String) {
        val questionFour = MRQuestionFour()
        questionFour.answer = answer
        QuestionnaireEatRightActivity.questionnaireAnswerRequest.moveRight?.questionFour =
            questionFour
        QuestionnaireEatRightActivity.submitQuestionnaireAnswerRequest(
            QuestionnaireEatRightActivity.questionnaireAnswerRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
