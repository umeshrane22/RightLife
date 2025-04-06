package com.jetsynthesys.rightlife.ui.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.FragmentBreaksToStretchBinding
import com.jetsynthesys.rightlife.ui.questionnaire.QuestionnaireEatRightActivity
import com.jetsynthesys.rightlife.ui.questionnaire.adapter.ScheduleOptionAdapter
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.MRQuestionSeven
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.Question
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.ScheduleOption

class BreaksToStretchFragment : Fragment() {

    private var _binding: FragmentBreaksToStretchBinding? = null
    private val binding get() = _binding!!

    private val scheduleOptions = listOf(
        ScheduleOption(R.drawable.ic_breaks_stress_1, "I never take breaks", ""),
        ScheduleOption(R.drawable.ic_breaks_stress_2, "Once every hour", ""),
        ScheduleOption(R.drawable.ic_breaks_stress_3, "Every 2-3 hours", ""),
        ScheduleOption(R.drawable.ic_breaks_stress_4, "A few times a day", ""),
        ScheduleOption(
            R.drawable.ic_breaks_stress_5,
            "Regular small movements throughout the day",
            ""
        ),
    )

    private var question: Question? = null

    companion object {
        fun newInstance(question: Question): BreaksToStretchFragment {
            val fragment = BreaksToStretchFragment()
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
        _binding = FragmentBreaksToStretchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ScheduleOptionAdapter(scheduleOptions, "MoveRight") { selectedOption ->
            /*Handler(Looper.getMainLooper()).postDelayed({
                QuestionnaireEatRightActivity.navigateToNextPage()
            }, 500)*/
            submit(selectedOption.title)
        }
        binding.rvScheduleOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvScheduleOptions.adapter = adapter
    }

    private fun submit(answer: String) {
        val questionSeven = MRQuestionSeven()
        questionSeven.answer = answer
        QuestionnaireEatRightActivity.questionnaireAnswerRequest.moveRight?.questionSeven =
            questionSeven
        QuestionnaireEatRightActivity.submitQuestionnaireAnswerRequest(
            QuestionnaireEatRightActivity.questionnaireAnswerRequest
        )
        Toast.makeText(requireContext(), "Saved!!  Have a Great Day!!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
