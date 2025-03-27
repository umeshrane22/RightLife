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
import com.example.rlapp.databinding.FragmentAnexityPowerBinding
import com.example.rlapp.ui.questionnaire.QuestionnaireThinkRightActivity
import com.example.rlapp.ui.questionnaire.pojo.Question
import com.example.rlapp.ui.questionnaire.pojo.TRQuestionFour
import com.example.rlapp.ui.questionnaire.pojo.TRQuestionThree

class AnexityPowerFragment : Fragment() {

    private lateinit var anxietyLevelTexts: Array<TextView>
    private var _binding: FragmentAnexityPowerBinding? = null
    private val binding get() = _binding!!

    private var question: Question? = null

    companion object {
        fun newInstance(question: Question): AnexityPowerFragment {
            val fragment = AnexityPowerFragment()
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
        _binding = FragmentAnexityPowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnContinue.setOnClickListener {
            //QuestionnaireThinkRightActivity.navigateToNextPage()
            submit("")
        }
        anxietyLevelTexts = arrayOf<TextView>(
            binding.anxietyLevelText1,
            binding.anxietyLevelText2,
            binding.anxietyLevelText3,
            binding.anxietyLevelText4,
            binding.anxietyLevelText5,
        )

        binding.anxietySliderView.setMinSteps(0)
        binding.anxietySliderView.setMaxSteps(10000)
        binding.anxietySliderView.setIntervalColors(ContextCompat.getColor(requireContext(), R.color.steps_dark_color))
        binding.anxietySliderView.setOnStepCountChangeListener { stepCount ->

            // Reset all TextViews to normal
            for (textView in anxietyLevelTexts) {
                textView.setTypeface(null, Typeface.NORMAL)
            }
            // Determine which TextView to bold based on stepCount
            val index = stepCount / 2000 - 1
            if (index >= 0 && index < anxietyLevelTexts.size) {
                anxietyLevelTexts.get(index).setTypeface(null, Typeface.BOLD)

            }
        }
    }

    private fun submit(answer: String) {
        val questionFour = TRQuestionFour()
        questionFour.answer = answer
        QuestionnaireThinkRightActivity.thinkRightAnswerRequest.questionFour = questionFour
        QuestionnaireThinkRightActivity.submitThinkRightRightAnswerRequest(
            QuestionnaireThinkRightActivity.thinkRightAnswerRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}