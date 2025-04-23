package com.jetsynthesys.rightlife.ui.settings

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.DialogFeedbackBottomsheetBinding

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

    fun sendEmail(context: Context, packageManager: PackageManager) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf("support@rightlife.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Subject of Email")
           // putExtra(Intent.EXTRA_TEXT, "Body of the email")
        }

    /*    if (emailIntent.resolveActivity(packageManager) != null) {
            context.startActivity(emailIntent)
        } else {
            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
        }*/

        val packageManager = context.packageManager
        if (emailIntent.resolveActivity(packageManager) != null) {
            context.startActivity(emailIntent)
        } else {
            // Try fallback with chooser (covers more cases)
            val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822" // MIME type for email
                putExtra(Intent.EXTRA_EMAIL, arrayOf("support@rightlife.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Subject of Email")
            }

            val chooser = Intent.createChooser(fallbackIntent, "Choose an Email client")
            if (fallbackIntent.resolveActivity(packageManager) != null) {
                context.startActivity(chooser)
            } else {
                Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}