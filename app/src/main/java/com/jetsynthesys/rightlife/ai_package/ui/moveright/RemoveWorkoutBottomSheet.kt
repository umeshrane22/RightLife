package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsynthesys.rightlife.R

class RemoveWorkoutBottomSheet : BottomSheetDialogFragment() {

    private var position: Int? = null
    private var onRemoveSuccess: (() -> Unit)? = null

    companion object {
        const val ARG_POSITION = "position"

        fun newInstance(position: Int): RemoveWorkoutBottomSheet {
            return RemoveWorkoutBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setStyle(STYLE_NORMAL, R.style.LoggedBottomSheetDialogTheme)
        return inflater.inflate(R.layout.fragment_edit_workout_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set transparent background for the dialog
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val yesButton = view.findViewById<LinearLayoutCompat>(R.id.yes_btn_bottom_sheet)
        val noButton = view.findViewById<LinearLayoutCompat>(R.id.layout_btn_log_meal)

        yesButton.setOnClickListener {
            position?.let {
                Toast.makeText(requireContext(), "Workout removed", Toast.LENGTH_SHORT).show()
                onRemoveSuccess?.invoke()
                dismiss()
            } ?: run {
                Toast.makeText(requireContext(), "Invalid workout position", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        noButton.setOnClickListener {
            dismiss()
        }
    }

    fun setOnRemoveSuccessListener(listener: () -> Unit) {
        this.onRemoveSuccess = listener
    }
}