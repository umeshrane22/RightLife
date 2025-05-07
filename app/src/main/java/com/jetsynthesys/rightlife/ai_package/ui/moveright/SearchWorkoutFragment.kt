package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.moveright.viewmodel.WorkoutViewModel
import com.jetsynthesys.rightlife.databinding.FragmentSearchWorkoutBinding
import com.google.android.material.tabs.TabLayout

class SearchWorkoutFragment : BaseFragment<FragmentSearchWorkoutBinding>() {

    private val workoutViewModel: WorkoutViewModel by activityViewModels()
    private lateinit var searchWorkoutBackButton: ImageView

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchWorkoutBinding
        get() = FragmentSearchWorkoutBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.gradient_color_background_workout)
        val routine = arguments?.getString("routine")
        val routineName = arguments?.getString("routineName")
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val searchEditText: EditText = view.findViewById(R.id.searchEditText)
        searchWorkoutBackButton = view.findViewById(R.id.search_workout_back_button)
        searchWorkoutBackButton.setOnClickListener {
            navigateToYourActivityFragment()
        }

        // Set up tabs
        val tabTitles = arrayOf("All Workouts", "My Routine", "Frequently Logged")
        for (title in tabTitles) {
            val tab = tabLayout.newTab()
            val customView = LayoutInflater.from(context).inflate(R.layout.custom_search_tab, null) as TextView
            customView.text = title
            tab.customView = customView
            tabLayout.addTab(tab)
        }

        // Initial fragment
        if (savedInstanceState == null) {
            val allWorkoutFragment = AllWorkoutFragment().apply {
                arguments = Bundle().apply {
                    putString("routine", routine)
                    putString("routineName", routineName)
                }
            }
            replaceFragment(allWorkoutFragment)        }

        // Handle back press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToYourActivityFragment()
            }
        })
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val allWorkoutFragment = AllWorkoutFragment().apply {
                            arguments = Bundle().apply {
                                putString("routine", routine)
                                putString("routineName", routineName)
                            }
                        }
                        replaceFragment(allWorkoutFragment)
                    }
                       // replaceFragment(allWorkoutFragment)} replaceFragment(AllWorkoutFragment())
                    1 -> replaceFragment(MyRoutineFragment())
                    2 -> replaceFragment(FrequentlyLoggedSearchFragment())
                }
                updateTabColors()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Search text listener
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                workoutViewModel.setSearchQuery(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun updateTabColors() {
        for (i in 0 until bi.tabLayout.tabCount) {
            val tab = bi.tabLayout.getTabAt(i)
            val tabText = tab?.customView?.findViewById<TextView>(R.id.tabText)
            tabText?.setTextColor(
                if (tab?.isSelected == true) ContextCompat.getColor(requireContext(), R.color.white)
                else ContextCompat.getColor(requireContext(), R.color.dotted_red)
            )
        }
    }

    private fun navigateToYourActivityFragment() {
        val fragment = YourActivityFragment()
        val args = Bundle()
        fragment.arguments = args
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment, "YourActivityFragment")
            .addToBackStack("YourActivityFragment")
            .commit()
    }
}