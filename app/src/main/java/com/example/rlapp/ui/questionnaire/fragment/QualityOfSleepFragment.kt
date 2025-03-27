package com.example.rlapp.ui.questionnaire.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.rlapp.R
import com.example.rlapp.databinding.FragmentQualityOfSleepBinding
import com.example.rlapp.ui.questionnaire.QuestionnaireThinkRightActivity
import com.example.rlapp.ui.questionnaire.pojo.Question
import com.example.rlapp.ui.questionnaire.pojo.SRQuestionOne

class QualityOfSleepFragment : Fragment() {
    private lateinit var sleepQualityLevelTexts: Array<TextView>
    private lateinit var selectedSleepQuality: String
    private var _binding: FragmentQualityOfSleepBinding? = null
    private val binding get() = _binding!!

    private var question: Question? = null

    companion object {
        fun newInstance(question: Question): QualityOfSleepFragment {
            val fragment = QualityOfSleepFragment()
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
        _binding = FragmentQualityOfSleepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sleepQualityLevelTexts = arrayOf(
            binding.anxietyLevelText1,
            binding.anxietyLevelText2,
            binding.anxietyLevelText3,
            binding.anxietyLevelText4,
            binding.anxietyLevelText5,
        )

        selectedSleepQuality = binding.anxietyLevelText1.text.toString()
        binding.sleepQualitySliderView.setMinSteps(0)
        binding.sleepQualitySliderView.setMaxSteps(10000)
        binding.sleepQualitySliderView.setIntervalColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.steps_dark_color
            )
        )
        binding.sleepQualitySliderView.setOnStepCountChangeListener { stepCount ->

            // Reset all TextViews to normal
            for (textView in sleepQualityLevelTexts) {
                textView.setTypeface(null, Typeface.NORMAL)
            }
            // Determine which TextView to bold based on stepCount
            val index = stepCount / 2000 - 1
            if (index >= 0 && index < sleepQualityLevelTexts.size) {
                sleepQualityLevelTexts[index].setTypeface(null, Typeface.BOLD)
                selectedSleepQuality = sleepQualityLevelTexts[index].text.toString()
            }
        }

        binding.btnContinue.setOnClickListener {
            //QuestionnaireThinkRightActivity.navigateToNextPage()
            submit(selectedSleepQuality)
        }
    }

    private fun submit(answer: String) {
        val questionOne = SRQuestionOne()
        questionOne.answer = answer
        QuestionnaireThinkRightActivity.questionnaireAnswerRequest.sleepRight?.questionOne =
            questionOne
        QuestionnaireThinkRightActivity.submitQuestionnaireAnswerRequest(
            QuestionnaireThinkRightActivity.questionnaireAnswerRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}