package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.response.DeleteCaloriesResponse
import com.jetsynthesys.rightlife.ai_package.model.response.MealUpdateResponse
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DeleteRoutineBottomSheet: BottomSheetDialogFragment() {

    private var calorieId: String? = null
    private var userId: String? = null
    private var onDeleteSuccess: (() -> Unit)? = null
    private var loadingOverlay : FrameLayout? = null

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

    private fun deleteCalorieRecord(calorieId: String, userID: String) {
        if (isAdded  && view != null){
            requireActivity().runOnUiThread {
                showLoader(requireView())
            }
        }
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val call = ApiClient.apiServiceFastApi.deleteRoutines(routineId = calorieId,
            userId = userId)
        call.enqueue(object : Callback<DeleteCaloriesResponse> {
            override fun onResponse(call: Call<DeleteCaloriesResponse>, response: Response<DeleteCaloriesResponse>) {
                if (response.isSuccessful) {
                    if (isAdded  && view != null){
                        requireActivity().runOnUiThread {
                            dismissLoader(requireView())
                        }
                    }
                    val mealData = response.body()?.message
                    Toast.makeText(context, mealData, Toast.LENGTH_SHORT).show()
                    onDeleteSuccess?.invoke()
                    dismiss()
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    if (isAdded  && view != null){
                        requireActivity().runOnUiThread {
                            dismissLoader(requireView())
                        }
                    }
                }
            }
            override fun onFailure(call: Call<DeleteCaloriesResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                if (isAdded  && view != null){
                    requireActivity().runOnUiThread {
                        dismissLoader(requireView())
                    }
                }
            }
        })
    }

    fun showLoader(view: View) {
        loadingOverlay = view.findViewById(R.id.loading_overlay)
        loadingOverlay?.visibility = View.VISIBLE
    }
    fun dismissLoader(view: View) {
        loadingOverlay = view.findViewById(R.id.loading_overlay)
        loadingOverlay?.visibility = View.GONE
    }
}