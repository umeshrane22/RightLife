package com.example.rlapp.ui.new_design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rlapp.R

class WeightSelectionFragment : Fragment() {

    private var llSelectedWeight: LinearLayout? = null
    private var tvSelectedWeight: TextView? = null
    private var selectedHeight = ""
    private var tvDescription: TextView? = null

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

        return view
    }

}