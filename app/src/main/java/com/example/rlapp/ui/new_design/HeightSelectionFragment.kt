package com.example.rlapp.ui.new_design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

class HeightSelectionFragment : Fragment() {

    private var llSelectedHeight: LinearLayout? = null
    private var tvSelectedHeight: TextView? = null
    private var selectedHeight = ""
    private var tvDescription: TextView? = null
    private var selected_number_text: TextView?=null

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


        val rulerView: RecyclerView = view.findViewById<RecyclerView>(R.id.rulerView)
        val markerView = view.findViewById<View>(R.id.markerView)
        val rlRulerContainer = view.findViewById<RelativeLayout>(R.id.rl_ruler_container)

        // new number picker
        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.rulerView)
        // Initialize RecyclerView with a vertical LinearLayoutManager
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rulerView.layoutManager = layoutManager

        // Generate numbers with increments of 0.1
        val numbers = mutableListOf<Float>()
        for (i in 0..1000) {
            numbers.add(i / 10f) // Increment by 0.1
        }

        val btnContinue = view.findViewById<Button>(R.id.btn_continue)
        btnContinue.setOnClickListener {
            Toast.makeText(requireContext(), "Continue button clicked", Toast.LENGTH_SHORT).show()
        }

        val adapter = RulerAdapterVertical(numbers) { number ->
            // Handle the selected number
            Toast.makeText(activity, "Selected: $number", Toast.LENGTH_SHORT).show()
        }
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
                        val position = recyclerView.layoutManager?.getPosition(snappedView) ?: return
                        val snappedNumber = numbers[position]
                        Toast.makeText(activity, "Snapped to: $snappedNumber", Toast.LENGTH_SHORT).show()
                        if (selected_number_text != null) {
                            selected_number_text!!.text = "$snappedNumber Cms"
                            btnContinue.isEnabled = true
                        }

                    }
                }
            }
        })

        // Set vertical padding for the marker view programmatically
        rlRulerContainer.post {
            val parentHeight = rlRulerContainer.height
            val paddingVertical = parentHeight / 2
            markerView.setPadding(markerView.paddingLeft, paddingVertical, markerView.paddingRight, paddingVertical)
        }

        // Scroll to the center position after layout is measured
        rulerView.post {
            val itemCount = rulerView.adapter?.itemCount ?: 0
            val centerPosition = itemCount / 2
            layoutManager.scrollToPositionWithOffset(centerPosition, 0)
        }





        return view
    }

}