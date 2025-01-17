package com.example.rlapp.ui.new_design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rlapp.R

class AgeSelectionFragment : Fragment() {

    private var llSelectedAge: LinearLayout? = null
    private var tvSelectedAge: TextView? = null
    private var selectedAge = ""
    private var tvDescription: TextView? = null


    companion object {
        fun newInstance(pageIndex: Int): AgeSelectionFragment {
            val fragment = AgeSelectionFragment()
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
        val view: View = inflater.inflate(R.layout.fragment_age_selection, container, false)

        llSelectedAge = view.findViewById(R.id.ll_selected_age)
        tvSelectedAge = view.findViewById(R.id.tv_selected_age)
        tvDescription = view.findViewById(R.id.tv_description)

        return view
    }

}