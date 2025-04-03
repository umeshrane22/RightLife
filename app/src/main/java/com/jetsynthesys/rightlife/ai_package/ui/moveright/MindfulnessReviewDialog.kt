package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.jetsynthesys.rightlife.R

class MindfulnessReviewDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_mindfulness_review, null)

        // Customize your views here if you need to set dynamic data
        val closeButton = view.findViewById<ImageView>(R.id.btn_close)
        val got_it = view.findViewById<Button>(R.id.got_it)
        got_it.setOnClickListener{
            dismiss()
        }
        closeButton.setOnClickListener { dismiss() }

        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    companion object {
        fun newInstance(): MindfulnessReviewDialog {
            return MindfulnessReviewDialog()
        }
    }
}