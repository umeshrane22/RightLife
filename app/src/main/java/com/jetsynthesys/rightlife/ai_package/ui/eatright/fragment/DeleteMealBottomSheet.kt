package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.R.color.transparent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.jetsynthesys.rightlife.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.response.MealUpdateResponse
import com.jetsynthesys.rightlife.ai_package.utils.LoaderUtil
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DeleteMealBottomSheet : BottomSheetDialogFragment() {

    private var listener: OnMealDeletedListener? = null
    private var mealName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    interface OnMealDeletedListener {
        fun onMealDeleted(mealData: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            context is OnMealDeletedListener -> context
            parentFragment is OnMealDeletedListener -> parentFragment as OnMealDeletedListener
            else -> throw ClassCastException("$context must implement OnMealDeletedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete_meal_bottom_sheet, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog = BottomSheetDialog(requireContext(), R.style.LoggedBottomSheetDialogTheme)
        dialog.setContentView(R.layout.fragment_frequently_logged)
        dialog.window?.setBackgroundDrawableResource(transparent)
        val deleteTitle = view.findViewById<TextView>(R.id.deleteTitle)
        val deleteConfirmTv = view.findViewById<TextView>(R.id.deleteConfirmTv)
        val deleteBtnLayout = view.findViewById<ConstraintLayout>(R.id.deleteBtnLayout)
        val yesBtn = view.findViewById<LinearLayoutCompat>(R.id.yesBtn)
        val noBtn = view.findViewById<LinearLayoutCompat>(R.id.noBtn)

        val mealId = arguments?.getString("mealId").toString()
        val deleteType = arguments?.getString("deleteType").toString()
        mealName = arguments?.getString("mealName").toString()

        noBtn.setOnClickListener {
            dismiss()
        }

        yesBtn.setOnClickListener {
            if (deleteType.contentEquals("MyMeal")){
                deleteMeal(mealId, dialog, deleteType)
            }
        }
    }

    private fun deleteMeal(mealId: String, dialog: BottomSheetDialog, deleteType: String) {
        LoaderUtil.showLoader(requireView())
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDateTime.format(formatter)
        val call = ApiClient.apiServiceFastApi.deleteMyMeal(userId, mealId)
        call.enqueue(object : Callback<MealUpdateResponse> {
            override fun onResponse(call: Call<MealUpdateResponse>, response: Response<MealUpdateResponse>) {
                if (response.isSuccessful) {
                    LoaderUtil.dismissLoader(requireView())
                    val mealData = response.body()?.message
                    Toast.makeText(context, mealData, Toast.LENGTH_SHORT).show()
                    listener?.onMealDeleted("deleted")
                    dismiss()
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    LoaderUtil.dismissLoader(requireView())
                }
            }
            override fun onFailure(call: Call<MealUpdateResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                LoaderUtil.dismissLoader(requireView())
            }
        })
    }

    companion object {
        const val TAG = "LoggedBottomSheet"
        @JvmStatic
        fun newInstance() = DeleteMealBottomSheet().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}

