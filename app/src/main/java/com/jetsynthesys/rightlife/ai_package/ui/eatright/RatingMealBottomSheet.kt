package com.jetsynthesys.rightlife.ai_package.ui.eatright

import android.R.color.transparent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.DeleteSnapMealBottomSheet

class RatingMealBottomSheet: BottomSheetDialogFragment() {
    private var isRating:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rating_snap_meal_bottom_sheet, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = BottomSheetDialog(requireContext(), R.style.LoggedBottomSheetDialogTheme)
        dialog.setContentView(R.layout.fragment_meal_scan_results)
        dialog.window?.setBackgroundDrawableResource(transparent)
        val closeIcon = view.findViewById<ImageView>(R.id.closeIc)
        val layoutCancel = view.findViewById<LinearLayoutCompat>(R.id.layoutCancel)
        val ratingLayout = view.findViewById<ConstraintLayout>(R.id.rating_snap_meal_layout)
        val successLayout = view.findViewById<ConstraintLayout>(R.id.rate_successfull_layout)
        val disabledLayoutCancel = view.findViewById<LinearLayoutCompat>(R.id.disabled_layoutCancel)
        val layoutDelete = view.findViewById<LinearLayoutCompat>(R.id.layoutDelete)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
      /*  if (isRating){
            layoutCancel.visibility = View.VISIBLE
            layoutCancel.setBackgroundResource(R.drawable.add_cart_button_background)
            layoutCancel.isEnabled = true
           // disabledLayoutCancel.visibility =View.GONE
        }else{
            layoutCancel.visibility = View.VISIBLE
            layoutCancel.setBackgroundResource(R.drawable.add_card_background_disabled)
            layoutCancel.isEnabled = false
            //disabledLayoutCancel.visibility =View.VISIBLE
        }*/
        layoutCancel.setBackgroundResource(R.drawable.add_card_background_disabled)
        layoutCancel.isEnabled = false

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            isRating = true
            layoutCancel.setBackgroundResource(R.drawable.add_cart_button_background)
            layoutCancel.isEnabled = true
            Toast.makeText(context, "You rated $rating stars", Toast.LENGTH_SHORT).show()
        }

        closeIcon.setOnClickListener {
            dismiss()
        }

        layoutCancel.setOnClickListener {
            ratingLayout.visibility =View.GONE
            successLayout.visibility= View.VISIBLE

            //dismiss()
        }

        layoutDelete.setOnClickListener {
            dismiss()
            Toast.makeText(view.context, "Dish Removed", Toast.LENGTH_SHORT).show()
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