package com.example.rlapp.ui.new_design

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.rlapp.R
import com.example.rlapp.ui.utility.SharedPreferenceManager

class WeightSelectionFragment : Fragment() {

    private lateinit var llSelectedWeight: LinearLayout
    private lateinit var tvSelectedWeight: TextView
    private var selectedWeight = ""
    private var tvDescription: TextView? = null
    private var selected_number_text: TextView?=null
    private lateinit var cardViewSelection: CardView
    companion object {
        fun newInstance(pageIndex: Int): WeightSelectionFragment {
            val fragment = WeightSelectionFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_weight_selection, container, false)

        llSelectedWeight = view.findViewById(R.id.ll_selected_weight)
        tvSelectedWeight = view.findViewById(R.id.tv_selected_weight)
        tvDescription = view.findViewById(R.id.tv_description)
        cardViewSelection = view.findViewById(R.id.card_view_age_selector)

        (activity as OnboardingQuestionnaireActivity).tvSkip.visibility = VISIBLE

        //---------
        val recyclerView = view.findViewById<RecyclerView>(R.id.rulerView)
        selected_number_text = view.findViewById(R.id.selected_number_text)
        val rulerView= view.findViewById<RecyclerView>(R.id.rulerView)
        val rlRulerContainer = view.findViewById<RelativeLayout>(R.id.rl_ruler_container)
        val colorStateList = ContextCompat.getColorStateList(requireContext(), R.color.menuselected)


        val btnContinue = view.findViewById<Button>(R.id.btn_continue)
        btnContinue.setOnClickListener {
            val onboardingQuestionRequest =
                SharedPreferenceManager.getInstance(requireContext()).onboardingQuestionRequest
            onboardingQuestionRequest.weight = selectedWeight
            SharedPreferenceManager.getInstance(requireContext())
                .saveOnboardingQuestionAnswer(onboardingQuestionRequest)

            cardViewSelection.visibility = GONE
            llSelectedWeight.visibility = VISIBLE
            tvSelectedWeight.text = selectedWeight

            Handler(Looper.getMainLooper()).postDelayed({
                OnboardingQuestionnaireActivity.navigateToNextPage()
            },1000)
        }


        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // Generate numbers with increments of 0.1
        val numbers = mutableListOf<Float>()
        for (i in 0..1000) {
            numbers.add(i / 10f) // Increment by 0.1
        }



        val adapter = RulerAdapter(numbers) { number ->
            // Handle the selected number
        }
        recyclerView.adapter = adapter


        // Center number with snap alignment
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Get the currently snapped position
                    val snappedView = snapHelper.findSnapView(recyclerView.layoutManager)
                    if (snappedView != null) {
                        val position = recyclerView.layoutManager!!.getPosition(snappedView)
                        val snappedNumber = numbers[position]
                        Toast.makeText(
                            requireContext(),
                            "Snapped to: $snappedNumber",
                            Toast.LENGTH_SHORT
                        ).show()
                        //selected_number_text.setText("$snappedNumber Kg")
                        if (selected_number_text != null) {
                            selected_number_text!!.text = "$snappedNumber Kg"
                            selectedWeight = selected_number_text?.text.toString()
                            btnContinue.isEnabled = true
                            btnContinue.backgroundTintList = colorStateList
                        }
                    }
                }
            }
        })

        rlRulerContainer.post {
            // Get the width of the parent LinearLayout
            val parentWidth: Int = rlRulerContainer.getWidth()

            // Calculate horizontal padding (half of parent width)
            val paddingHorizontal = parentWidth / 2

            // Set horizontal padding programmatically
            rulerView.setPadding(
                paddingHorizontal,
                rulerView.paddingTop,
                paddingHorizontal,
                rulerView.paddingBottom
            )
        }

        // Scroll to the center after layout is measured
        rulerView.post {
            // Calculate the center position
            val itemCount = if (rulerView.adapter != null) rulerView.adapter!!.itemCount else 0
            val centerPosition = itemCount / 2

            // Scroll to the center position
            layoutManager.scrollToPositionWithOffset(centerPosition, 0)
        }

        return view
    }

}