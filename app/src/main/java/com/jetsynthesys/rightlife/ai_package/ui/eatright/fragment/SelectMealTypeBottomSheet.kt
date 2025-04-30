package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.R.color.transparent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.HomeTabMealFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectMealTypeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var layoutBreakfast : LinearLayoutCompat
    private lateinit var layoutMorningSnack : LinearLayoutCompat
    private lateinit var layoutLunch : LinearLayoutCompat
    private lateinit var layoutEveningSnacks : LinearLayoutCompat
    private lateinit var layoutDinner : LinearLayoutCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_meal_type_bottom_sheet, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog = BottomSheetDialog(requireContext(), R.style.LoggedBottomSheetDialogTheme)
        dialog.setContentView(R.layout.fragment_frequently_logged)
        dialog.window?.setBackgroundDrawableResource(transparent)
        layoutBreakfast = view.findViewById(R.id.layoutBreakfast)
        layoutMorningSnack = view.findViewById(R.id.layoutMorningSnack)
        layoutLunch = view.findViewById(R.id.layoutLunch)
        layoutEveningSnacks = view.findViewById(R.id.layoutEveningSnacks)
        layoutDinner = view.findViewById(R.id.layoutDinner)

        val items = arrayOf("Breakfast", "Morning Snack", "Lunch", "Evening Snacks", "Dinner")

        layoutBreakfast.setOnClickListener {
            dismiss()
            callTabMealFragment("breakFast")
        }

        layoutMorningSnack.setOnClickListener {
            dismiss()
            callTabMealFragment("morningSnack")
        }

        layoutLunch.setOnClickListener {
            dismiss()
            callTabMealFragment("lunch")
        }

        layoutEveningSnacks.setOnClickListener {
            dismiss()
            callTabMealFragment("eveningSnacks")
        }

        layoutDinner.setOnClickListener {
            dismiss()
            callTabMealFragment("dinner")
        }
    }

    private fun callTabMealFragment(mealType: String) {
        val fragment = HomeTabMealFragment()
        val args = Bundle()
        args.putString("mealType", mealType)
        fragment.arguments = args
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, "mealLog")
            addToBackStack("mealLog")
            commit()
        }
    }

    companion object {
        const val TAG = "LoggedBottomSheet"
        @JvmStatic
        fun newInstance() = SelectMealTypeBottomSheet().apply {
            arguments = Bundle().apply {
                // Add any required arguments here
            }
        }
    }
}

