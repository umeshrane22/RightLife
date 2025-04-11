package com.jetsynthesys.rightlife.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.view.LayoutInflater
import com.jetsynthesys.rightlife.databinding.DialogChecklistQuestionsBinding
import com.jetsynthesys.rightlife.databinding.DialogJournalCommonBinding

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

}