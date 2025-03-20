package com.example.rlapp.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import com.example.rlapp.R
import com.example.rlapp.databinding.DialogFeedbackBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

object WriteToUsUtils {

     fun showAddEditBottomSheet(context: Context, layoutInflater: LayoutInflater) {
        // Create and configure BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(context)

        // Inflate the BottomSheet layout
        val dialogBinding = DialogFeedbackBottomsheetBinding.inflate(layoutInflater)
        val bottomSheetView = dialogBinding.root

        bottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(context, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        dialogBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnSubmit.setOnClickListener {
            if (dialogBinding.edtMessage.text.isNullOrEmpty()) {
                Toast.makeText(context, "Message should not be empty", Toast.LENGTH_SHORT).show()
            } else {
                /***
                 *  Api needs to be implemented here
                 */
                Toast.makeText(context, "API is not provided", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()
    }
}