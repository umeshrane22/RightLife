package com.jetsynthesys.rightlife.ui.questionnaire.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.FragmentFeelAfterWakingBinding
import com.jetsynthesys.rightlife.ui.jounal.new_journal.JournalMoodAdapter
import com.jetsynthesys.rightlife.ui.jounal.new_journal.Mood
import com.jetsynthesys.rightlife.ui.questionnaire.QuestionnaireThinkRightActivity
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.Question
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.SRQuestionFive

class FeelAfterWakingFragment : Fragment() {

    private var _binding: FragmentFeelAfterWakingBinding? = null
    private val binding get() = _binding!!

    private var question: Question? = null
    private var mSelectedMood: Mood = Mood("Refreshed", R.drawable.ic_happy)

    companion object {
        fun newInstance(question: Question): FeelAfterWakingFragment {
            val fragment = FeelAfterWakingFragment()
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
        _binding = FragmentFeelAfterWakingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMoodList()

        binding.btnContinue.setOnClickListener {
            //QuestionnaireThinkRightActivity.navigateToNextPage()
            submit(mSelectedMood.name)
        }
    }

    private fun submit(answer: String) {
        val questionFive = SRQuestionFive()
        questionFive.answer = answer
        QuestionnaireThinkRightActivity.questionnaireAnswerRequest.sleepRight?.questionFive =
            questionFive
        QuestionnaireThinkRightActivity.submitQuestionnaireAnswerRequest(
            QuestionnaireThinkRightActivity.questionnaireAnswerRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpMoodList() {
        val moodList = listOf(
            Mood("Refreshed", R.drawable.ic_happy, isSelected = true),
            Mood("Rested", R.drawable.ic_relaxed),
            Mood("Neutral", R.drawable.ic_unsure),
            Mood("Exhausted", R.drawable.ic_stressed),
            Mood("Drained", R.drawable.ic_sad)
        )

        val adapter = JournalMoodAdapter(moodList, 0) { selectedMood ->
            mSelectedMood = selectedMood
            when (selectedMood.name) {
                "Refreshed" -> {
                    binding.ivSelectedImage.setImageResource(R.drawable.ic_happy)
                    setMoodColor(Color.parseColor("#7CCE4E")) //refreshed color
                }

                "Rested" -> {
                    binding.ivSelectedImage.setImageResource(R.drawable.ic_relaxed)
                    setMoodColor(Color.parseColor("#D0E92A")) //rested color
                }

                "Neutral" -> {
                    binding.ivSelectedImage.setImageResource(R.drawable.ic_unsure)
                    setMoodColor(Color.parseColor("#F7F600")) //Neutral color
                }

                "Exhausted" -> {
                    binding.ivSelectedImage.setImageResource(R.drawable.ic_stressed)
                    setMoodColor(Color.parseColor("#FB9900")) //Exausted  color
                }

                "Drained" -> {
                    binding.ivSelectedImage.setImageResource(R.drawable.ic_sad)
                    setMoodColor(Color.parseColor("#FC2611")) //Drained  color
                }
            }
            //val moodView = LayoutInflater.from(context).inflate(R.layout.item_mood_emoji, container, false)


        }

        binding.moodRecyclerView.adapter = adapter
        binding.moodRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.ivSelectedImage.setImageResource(R.drawable.ic_happy)
        setMoodColor(Color.parseColor("#7CCE4E")) //refreshed color
    }

    fun setMoodColor(baseColor: Int) {
        val darker1 = adjustAlpha(baseColor, 0.3f)
        val darker2 = adjustAlpha(baseColor, 0.5f)
        val darker3 = adjustAlpha(baseColor, 0.8f)

        binding.layer1.background.setTint(darker3)
        binding.layer2.background.setTint(darker2)
        binding.layer3.background.setTint(darker1)
    }

    fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = Math.round(Color.alpha(color) * factor)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }
}