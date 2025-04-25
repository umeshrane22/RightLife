package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.R.color.transparent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import com.jetsynthesys.rightlife.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.SnapDishLocalListModel

class DeleteSnapMealBottomSheet : BottomSheetDialogFragment() {

    private lateinit var currentPhotoPathsecound : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete_snap_meal_bottom_sheet, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = BottomSheetDialog(requireContext(), R.style.LoggedBottomSheetDialogTheme)
        dialog.setContentView(R.layout.fragment_meal_scan_results)
        dialog.window?.setBackgroundDrawableResource(transparent)
        val closeIcon = view.findViewById<ImageView>(R.id.closeIc)
        val layoutCancel = view.findViewById<LinearLayoutCompat>(R.id.layoutCancel)
        val layoutDelete = view.findViewById<LinearLayoutCompat>(R.id.layoutDelete)

        val snapRecipeName = arguments?.getString("snapRecipeName").toString()

        val imagePathString = arguments?.getString("ImagePathsecound")
        currentPhotoPathsecound = imagePathString?.let { Uri.parse(it) }!!

        val snapDishLocalListModel = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("snapDishLocalListModel", SnapDishLocalListModel::class.java)
        } else {
            arguments?.getParcelable("snapDishLocalListModel")
        }

        layoutDelete.setOnClickListener {
            if (snapDishLocalListModel != null) {
                if (snapDishLocalListModel.data.size > 0) {
                    for (item in snapDishLocalListModel.data) {
                        if (item.recipe_name.contentEquals(snapRecipeName)) {
                            snapDishLocalListModel.data.remove(item)
                            dismiss()
                            Toast.makeText(view.context, "Dish Removed", Toast.LENGTH_SHORT).show()
                            val fragment = MealScanResultFragment()
                            val args = Bundle()
                            args.putString("ImagePathsecound", currentPhotoPathsecound.toString())
                            args.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
                            fragment.arguments = args
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                replace(R.id.flFragment, fragment, "mealLog")
                                addToBackStack("mealLog")
                                commit()
                            }
                            break
                        }
                    }
                }
            }
        }

        closeIcon.setOnClickListener {
            dismiss()
        }

        layoutCancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "LoggedBottomSheet"
        @JvmStatic
        fun newInstance() = DeleteSnapMealBottomSheet().apply {
            arguments = Bundle().apply {
                // Add any required arguments here
            }
        }
    }
}

