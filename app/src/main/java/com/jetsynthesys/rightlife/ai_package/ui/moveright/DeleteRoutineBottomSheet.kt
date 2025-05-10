package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteRoutineBottomSheet: BottomSheetDialogFragment() {

    private var calorieId: String? = null
    private var userId: String? = null
    private var onDeleteSuccess: (() -> Unit)? = null

    companion object {
        const val ARG_CALORIE_ID = "calorie_id"
        const val ARG_USER_ID = "user_id"

        fun newInstance(calorieId: String, userId: String): DeleteRoutineBottomSheet {
            return DeleteRoutineBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_CALORIE_ID, calorieId)
                    putString(ARG_USER_ID, userId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            calorieId = it.getString(ARG_CALORIE_ID)
            userId = it.getString(ARG_USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_delete_routine_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yesButton = view.findViewById<LinearLayoutCompat>(R.id.yes_btn_bottom_sheet)
        val noButton = view.findViewById<LinearLayoutCompat>(R.id.layout_btn_log_meal)

        yesButton.setOnClickListener {
            calorieId?.let { id ->
                userId?.let { uid ->
                    deleteCalorieRecord(id, uid)
                } ?: run {
                    Toast.makeText(context, "User ID is missing", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            } ?: run {
                Toast.makeText(context, "Calorie ID is missing", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        noButton.setOnClickListener {
            dismiss()
        }
    }

    fun setOnDeleteSuccessListener(listener: () -> Unit) {
        this.onDeleteSuccess = listener
    }

    private fun deleteCalorieRecord(calorieId: String, userId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Call the suspend function directly
                val deleteResponse = ApiClient.apiServiceFastApi.deleteRoutines(
                    routineId = calorieId,
                    userId = userId
                )

                // Since deleteRoutines directly returns the response body (not retrofit2.Response),
                // we assume the call is successful if no exception is thrown
                withContext(Dispatchers.Main) {
                    if (deleteResponse != null) {
                        Toast.makeText(requireContext(), "Workout deleted: ", Toast.LENGTH_SHORT).show()
                        onDeleteSuccess?.invoke()
                    } else {
                        Toast.makeText(requireContext(), "Deletion failed: Empty response", Toast.LENGTH_SHORT).show()
                    }
                    dismiss()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}