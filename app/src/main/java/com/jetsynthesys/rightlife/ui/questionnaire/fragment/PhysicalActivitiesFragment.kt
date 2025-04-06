package com.jetsynthesys.rightlife.ui.questionnaire.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.BottomsheetPhysicalActivityBinding
import com.jetsynthesys.rightlife.databinding.FragmentPhysicalActivitiesBinding
import com.jetsynthesys.rightlife.ui.questionnaire.QuestionnaireEatRightActivity
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.MRQuestionThree
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.PhysicalActivity
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.Question

class PhysicalActivitiesFragment : Fragment() {

    private var _binding: FragmentPhysicalActivitiesBinding? = null
    private val binding get() = _binding!!
    private val selectedActivities: ArrayList<String> = ArrayList()
    private var activities: ArrayList<PhysicalActivity> = ArrayList()

    private var question: Question? = null

    companion object {
        fun newInstance(question: Question): PhysicalActivitiesFragment {
            val fragment = PhysicalActivitiesFragment()
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
        _binding = FragmentPhysicalActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activities = arrayListOf(
            PhysicalActivity("American Football"),
            PhysicalActivity("Archery"),
            PhysicalActivity("Athletics"),
            PhysicalActivity("Australian Football"),
            PhysicalActivity("Badminton"),
            PhysicalActivity("Barre"),
            PhysicalActivity("Baseball"),
            PhysicalActivity("Basketball"),
            PhysicalActivity("Boxing"),
            PhysicalActivity("Climbing"),
            PhysicalActivity("Core Training"),
            PhysicalActivity("Cricket"),
            PhysicalActivity("Cross Training"),
            PhysicalActivity("Cycling"),
            PhysicalActivity("Dance (Zumba, Hip-Hop)"),
            PhysicalActivity("Disc Sports"),
            PhysicalActivity("Elliptical"),
            PhysicalActivity("Field Events"),
            PhysicalActivity("Football"),
            PhysicalActivity("Functional Strength Training"),
            PhysicalActivity("Golf"),
            PhysicalActivity("Gymnastics"),
            PhysicalActivity("Handball"),
            PhysicalActivity("Hiking"),
            PhysicalActivity("HIIT"),
            PhysicalActivity("Hockey"),
            PhysicalActivity("Kickboxing"),
            PhysicalActivity("Martial Arts"),
            PhysicalActivity("Other"),
            PhysicalActivity("Pickleball"),
            PhysicalActivity("Pilates"),
            PhysicalActivity("Power Yoga"),
            PhysicalActivity("Powerlifting"),
            PhysicalActivity("Pranayama"),
            PhysicalActivity("Rowing Machine"),
            PhysicalActivity("Rugby"),
            PhysicalActivity("Running"),
            PhysicalActivity("Skating"),
            PhysicalActivity("Skipping"),
            PhysicalActivity("Squash"),
            PhysicalActivity("Stairs"),
            PhysicalActivity("Stretching"),
            PhysicalActivity("Swimming"),
            PhysicalActivity("Table Tennis"),
            PhysicalActivity("Tennis"),
            PhysicalActivity("Track and Field Events"),
            PhysicalActivity("Traditional Strength Training"),
            PhysicalActivity("Trekking"),
            PhysicalActivity("Volleyball"),
            PhysicalActivity("Walking"),
            PhysicalActivity("Watersports"),
            PhysicalActivity("Weightlifting"),
            PhysicalActivity("Wrestling"),
            PhysicalActivity("Yoga")
        )


        activities.forEach { physicalActivity ->
            addChip(physicalActivity.title)
        }

        binding.btnContinue.setOnClickListener {
            if (selectedActivities.size == 3)
                showPhysicalActivitiesBottomSheet()
            else
                Toast.makeText(requireContext(), "Please select 3 activities", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addChip(name: String) {
        val chip = Chip(requireContext())
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.dmsans_regular)
        chip.apply {
            id = View.generateViewId() // Generate unique ID
            text = name
            isCheckable = true
            isChecked = false
            chipCornerRadius = 50f
            chipStrokeColor = ContextCompat.getColorStateList(
                requireContext(),
                R.color.gray_light_bg
            )
            setPadding(10, 0, 10, 0)
            textSize = 13f
            layoutParams
        }

        chip.typeface = typeface

        val heightInDp = 60 // or whatever height you want
        val heightInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            heightInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            heightInPx
        )
        chip.layoutParams = layoutParams

        // Set different colors for selected state
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.light_red),
                ContextCompat.getColor(requireContext(), R.color.white)
            )
        )

        val textColorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.white),
                ContextCompat.getColor(requireContext(), R.color.txt_color_header)
            )
        )
        chip.chipBackgroundColor = colorStateList

        chip.setOnClickListener { view ->
            val position = binding.chipGroup.indexOfChild(view)
            if (selectedActivities.size == 3 && !activities[position].isSelected) {
                chip.isChecked = false
                return@setOnClickListener
            }
            if (activities[position].isSelected)
                selectedActivities.remove(activities[position].title)
            else
                selectedActivities.add(activities[position].title)

            activities[position].isSelected = !activities[position].isSelected
        }

        // Text color for selected state
        chip.setTextColor(textColorStateList)
        binding.chipGroup.addView(chip)
    }

    private fun showPhysicalActivitiesBottomSheet() {
        // Create and configure BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        // Inflate the BottomSheet layout
        val dialogBinding = BottomsheetPhysicalActivityBinding.inflate(layoutInflater)
        val bottomSheetView = dialogBinding.root

        bottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        dialogBinding.textTitle1.text = selectedActivities[0]
        dialogBinding.textTitle2.text = selectedActivities[1]
        dialogBinding.textTitle3.text = selectedActivities[2]

        dialogBinding.btnPlus1.setOnClickListener {
            val count = dialogBinding.textCount1.text.toString().toInt() + 1
            dialogBinding.textCount1.text = count.toString()
        }

        dialogBinding.btnMinus1.setOnClickListener {
            var count = dialogBinding.textCount1.text.toString().toInt()
            if (count > 0) {
                count--
                dialogBinding.textCount1.text = count.toString()
            }
        }

        dialogBinding.btnPlus2.setOnClickListener {
            val count = dialogBinding.textCount2.text.toString().toInt() + 1
            dialogBinding.textCount2.text = count.toString()
        }

        dialogBinding.btnMinus2.setOnClickListener {
            var count = dialogBinding.textCount2.text.toString().toInt()
            if (count > 0) {
                count--
                dialogBinding.textCount2.text = count.toString()
            }
        }

        dialogBinding.btnPlus3.setOnClickListener {
            val count = dialogBinding.textCount3.text.toString().toInt() + 1
            dialogBinding.textCount3.text = count.toString()
        }

        dialogBinding.btnMinus3.setOnClickListener {
            var count = dialogBinding.textCount3.text.toString().toInt()
            if (count > 0) {
                count--
                dialogBinding.textCount3.text = count.toString()
            }
        }

        dialogBinding.btnSetNow.setOnClickListener {
            val calculation = dialogBinding.textCount1.text.toString().toInt().plus(
                dialogBinding.textCount2.text.toString().toInt()
            ).plus(
                dialogBinding.textCount3.text.toString().toInt()
            )
            if (QuestionnaireEatRightActivity.questionnaireAnswerRequest.moveRight?.questionOne?.answer?.toInt()!! >= calculation) {
                bottomSheetDialog.dismiss()
                //QuestionnaireEatRightActivity.navigateToNextPage()
                submit(selectedActivities)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please set correct activity frequency",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        bottomSheetDialog.show()
    }

    private fun submit(answer: List<String>) {
        val questionThree = MRQuestionThree()
        questionThree.answer = answer
        QuestionnaireEatRightActivity.questionnaireAnswerRequest.moveRight?.questionThree =
            questionThree
        QuestionnaireEatRightActivity.submitQuestionnaireAnswerRequest(
            QuestionnaireEatRightActivity.questionnaireAnswerRequest
        )
    }
}
