package com.example.rlapp.ui.new_design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rlapp.R

class BodyFatSelectionFragment : Fragment() {

    private var llSelectedBodyFat: LinearLayout? = null
    private var tvSelectedBodyFat: TextView? = null
    private var selectedBodyFat = ""
    private var tvDescription: TextView? = null

    companion object {
        fun newInstance(pageIndex: Int): BodyFatSelectionFragment {
            val fragment = BodyFatSelectionFragment()
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
        val view: View = inflater.inflate(R.layout.fragment_body_fat_selection, container, false)

        llSelectedBodyFat = view.findViewById(R.id.ll_selected_body_fat)
        tvSelectedBodyFat = view.findViewById(R.id.tv_selected_body_fat)
        tvDescription = view.findViewById(R.id.tv_description)

        return view
    }

}