package com.example.rlapp.ui.new_design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ui.utility.ConversionUtils
import com.example.rlapp.ui.utility.SharedPreferenceManager
import java.text.DecimalFormat
import kotlin.math.floor

class HeightSelectionFragment : Fragment() {

    private lateinit var llSelectedHeight: LinearLayout
    private lateinit var tvSelectedHeight: TextView
    private var selectedHeight = ""
    private var tvDescription: TextView? = null
    private var selected_number_text: TextView? = null
    private lateinit var cardViewSelection: CardView
    private val numbers = mutableListOf<Float>()
    private lateinit var adapter: RulerAdapterVertical
    private var selectedLabel: String = " feet"
    private val decimalFormat = DecimalFormat("###.##")

    companion object {
        fun newInstance(pageIndex: Int): HeightSelectionFragment {
            val fragment = HeightSelectionFragment()
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
        val view: View = inflater.inflate(R.layout.fragment_height_selection, container, false)

        llSelectedHeight = view.findViewById(R.id.ll_selected_height)
        tvSelectedHeight = view.findViewById(R.id.tv_selected_height)
        tvDescription = view.findViewById(R.id.tv_description)
        selected_number_text = view.findViewById(R.id.selected_number_text)
        cardViewSelection = view.findViewById(R.id.card_view_age_selector)

        (activity as OnboardingQuestionnaireActivity).tvSkip.visibility = VISIBLE

        val rulerView = view.findViewById<RecyclerView>(R.id.rulerView)
        val markerView = view.findViewById<View>(R.id.markerView)
        val rlRulerContainer = view.findViewById<RelativeLayout>(R.id.rl_ruler_container)
        val colorStateList = ContextCompat.getColorStateList(requireContext(), R.color.menuselected)

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
        rulerView.layoutManager = layoutManager

        // Generate numbers with increments of 0.1
        for (i in 0..250) {
            numbers.add(i * 1f) // Increment by 0.1  numbers.add(i * 1f)
        }

        val switch = view.findViewById<SwitchCompat>(R.id.switch_height_metric)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val h = selectedHeight.split(" ")
                val feet = "${h[0]}.${h[2]}"
                selectedLabel = " cms"
                selectedHeight = ConversionUtils.convertFeetToCentimeter(feet)
                setCms()
                selectedHeight = decimalFormat.format(selectedHeight.toDouble())
                rulerView.layoutManager?.scrollToPosition(floor(selectedHeight.toDouble()).toInt())
                selectedHeight += selectedLabel
            } else {
                val w = selectedHeight.split(" ")
                selectedLabel = " feet"
                selectedHeight = ConversionUtils.convertCentimeterToFtInch(w[0])
                setFtIn()
                val h = selectedHeight.split(".")
                val ft = h[0]
                val inch = h[1]
                rulerView.layoutManager?.scrollToPosition(floor(selectedHeight.toDouble()).toInt() * 12)
                selectedHeight = "$ft Ft $inch In"
            }
            selected_number_text!!.text = selectedHeight
        }

        val btnContinue = view.findViewById<Button>(R.id.btn_continue)
        btnContinue.setOnClickListener {
            val onboardingQuestionRequest =
                SharedPreferenceManager.getInstance(requireContext()).onboardingQuestionRequest
            onboardingQuestionRequest.height = selectedHeight
            SharedPreferenceManager.getInstance(requireContext())
                .saveOnboardingQuestionAnswer(onboardingQuestionRequest)

            cardViewSelection.visibility = GONE
            llSelectedHeight.visibility = VISIBLE
            tvSelectedHeight.text = selectedHeight

            (activity as OnboardingQuestionnaireActivity).submitAnswer(onboardingQuestionRequest)
        }


        adapter = RulerAdapterVertical(numbers) { number ->
            // Handle the selected number
        }
        adapter.setType("feet")
        rulerView.adapter = adapter

        // Attach a LinearSnapHelper for center alignment
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rulerView)

        // Add scroll listener to handle snapping logic
        rulerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val snappedView = snapHelper.findSnapView(recyclerView.layoutManager)
                    if (snappedView != null) {
                        val position =
                            recyclerView.layoutManager?.getPosition(snappedView) ?: return
                        val snappedNumber = numbers[position]
                        if (selected_number_text != null) {
                            selected_number_text!!.text =
                                "${decimalFormat.format(snappedNumber)} $selectedLabel"
                            if (selectedLabel == " feet") {
                                val feet = decimalFormat.format(snappedNumber / 12)
                                val h = (feet).toString().split(".")
                                val ft = h[0]
                                var inch = "0"
                                if (h.size > 1) {
                                    inch = h[1]
                                }
                                selected_number_text!!.text = "$ft Ft $inch In"
                            }
                            selectedHeight = selected_number_text?.text.toString()
                            btnContinue.isEnabled = true
                            btnContinue.backgroundTintList = colorStateList
                            switch.isEnabled = true
                        }

                    }
                }
            }
        })

        // Set vertical padding for the marker view programmatically
        rlRulerContainer.post {
            val parentHeight = rlRulerContainer.height
            val paddingVertical = parentHeight / 2
            markerView.setPadding(
                markerView.paddingLeft,
                paddingVertical,
                markerView.paddingRight,
                paddingVertical
            )
        }

        // Scroll to the center position after layout is measured
        rulerView.post {
            val itemCount = rulerView.adapter?.itemCount ?: 0
            val centerPosition = itemCount / 2
            layoutManager.scrollToPositionWithOffset(centerPosition, 0)
        }

        return view
    }

    private fun setCms() {
        numbers.clear()
        for (i in 0..250) {
            numbers.add(i * 1f) // Increment by 0.1  numbers.add(i * 1f)
        }
        adapter.setType("cms")
        adapter.notifyDataSetChanged()
    }

    private fun setFtIn() {
        numbers.clear()
        for (i in 0..250) {
            numbers.add(i * 1f) // Increment by 0.1  numbers.add(i * 1f)
        }
        adapter.setType("feet")
        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        cardViewSelection.visibility = VISIBLE
        llSelectedHeight.visibility = GONE
    }

}