package com.jetsynthesys.rightlife.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import com.jetsynthesys.rightlife.databinding.DialogChecklistQuestionsBinding
import com.jetsynthesys.rightlife.databinding.DialogJournalCommonBinding
import com.jetsynthesys.rightlife.databinding.DialogPlaylistCreatedBinding

object DialogUtils {

    fun showJournalCommonDialog(context: Context, header: String, htmlText: String) {
        val dialog = Dialog(context)
        val binding = DialogJournalCommonBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        // Set the dim amount
        val layoutParams = window!!.attributes
        layoutParams.dimAmount = 0.7f // Adjust the dim amount (0.0 - 1.0)
        window.attributes = layoutParams

        binding.tvTitle.text = header
        binding.tvDescription.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)

        // Handle close button click
        binding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showCheckListQuestionCommonDialog(context: Context) {
        val htmlText = """
    <h3>Finish Checklist to Unlock</h3>
    ompleting your checklist unlocks the full power of RightLife.</p>

    <b>Smart logging, Deep–dive insights.</b><br>
    Here’s what’s waiting for you on the other side:</p>

    🍽️ <b>Eat</b><br>
    Get meal insights, nutrient tracking, and food logging tailored to your goals.</p>

    🏃‍♂️ <b>Move</b><br>
    See activity trends, heart rate zones, and movement suggestions and insights.</p>

    <p>😴 <b>Sleep</b><br>
    Understand your sleep patterns, consistency, and get smarter wind–down tips.</p>

    <p>🧠 <b>Think</b><br>
    Access affirmations, mood logging, and guided mental wellness tools.</p>

    <p>The sooner you complete it, the sooner your health journey truly begins.</p>
""".trimIndent()
        val dialog = Dialog(context)
        val binding = DialogChecklistQuestionsBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        // Set the dim amount
        val layoutParams = window!!.attributes
        layoutParams.dimAmount = 0.7f // Adjust the dim amount (0.0 - 1.0)
        window.attributes = layoutParams

        binding.tvTitle.text = "Finish Checklist to Unlock"
        binding.tvDescription.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)

        // Handle close button click
        binding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showSuccessDialog(
        activity: Activity,
        message: String,
        desc: String = ""
    ) {
        // Inflate the view binding for the dialog layout
        val inflater = LayoutInflater.from(activity)
        val binding = DialogPlaylistCreatedBinding.inflate(inflater)

        // Create the dialog and set the root view
        val dialog = Dialog(activity)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Dim background
        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.dimAmount = 0.7f
        window?.attributes = layoutParams

        // Set full width
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        layoutParams?.width = width

        // Set message using view binding
        binding.tvDialogPlaylistCreated.text = message

        if (desc.isNotEmpty()) {
            binding.tvDialogDescription.visibility = View.VISIBLE
            binding.tvDialogDescription.text = desc
        }

        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            activity.finish()
        }, 2000)
    }


}