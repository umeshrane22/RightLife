package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.tab.MyMealListAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.DeleteMealBottomSheet
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.YourMealLogsFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.createmeal.CreateMealFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.frequentlylogged.LoggedBottomSheet
import com.jetsynthesys.rightlife.databinding.FragmentMyMealBinding
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.request.DishLog
import com.jetsynthesys.rightlife.ai_package.model.request.MealPlanLogRequest
import com.jetsynthesys.rightlife.ai_package.model.request.SaveDishLogRequest
import com.jetsynthesys.rightlife.ai_package.model.response.MealDetails
import com.jetsynthesys.rightlife.ai_package.model.response.MealLogPlanResponse
import com.jetsynthesys.rightlife.ai_package.model.response.MealPlan
import com.jetsynthesys.rightlife.ai_package.model.response.MealPlanResponse
import com.jetsynthesys.rightlife.ai_package.model.response.MealUpdateResponse
import com.jetsynthesys.rightlife.ai_package.model.response.MergedMealItem
import com.jetsynthesys.rightlife.ai_package.model.response.MyMealsSaveResponse
import com.jetsynthesys.rightlife.ai_package.model.response.SnapMealDetail
import com.jetsynthesys.rightlife.ai_package.model.response.SnapRecipeData
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MealLogItems
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.SelectedMealLogList
import com.jetsynthesys.rightlife.ai_package.utils.LoaderUtil
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyMealFragment : BaseFragment<FragmentMyMealBinding>() {

    private lateinit var myMealRecyclerView : RecyclerView
    private lateinit var layoutNoMeals : LinearLayoutCompat
    private lateinit var layoutBottomCreateMeal : LinearLayoutCompat
    private lateinit var layoutCreateMeal : LinearLayoutCompat
    private lateinit var loggedBottomSheetFragment : LoggedBottomSheet
    private lateinit var deleteBottomSheetFragment: DeleteMealBottomSheet
    private lateinit var mealPlanTitleLayout : ConstraintLayout
    private lateinit var addLayout : LinearLayoutCompat
    private lateinit var mealType : String
    private val mergedList = mutableListOf<MergedMealItem>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMyMealBinding
        get() = FragmentMyMealBinding::inflate

    private val myMealListAdapter by lazy { MyMealListAdapter(requireContext(), arrayListOf(), -1, null,null,
        false, :: onMealDeleteItem, :: onMealLogItem, :: onSnapMealDeleteItem, :: onSnapMealLogItem) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.meal_log_background))

        myMealRecyclerView = view.findViewById(R.id.recyclerview_my_meals_item)
        layoutNoMeals = view.findViewById(R.id.layout_no_meals)
        layoutBottomCreateMeal = view.findViewById(R.id.layout_bottom_create_meal)
        layoutCreateMeal = view.findViewById(R.id.layout_create_meal)
        mealPlanTitleLayout = view.findViewById(R.id.layout_meal_plan_title)
        addLayout = view.findViewById(R.id.addLayout)

        mealType = arguments?.getString("mealType").toString()
        myMealRecyclerView.layoutManager = LinearLayoutManager(context)
        myMealRecyclerView.adapter = myMealListAdapter

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = YourMealLogsFragment()
                val args = Bundle()
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment, "landing")
                    addToBackStack("landing")
                    commit()
                }
            }
        })

        getMyMealList()
        //getMealLog()

        addLayout.setOnClickListener {
            val fragment = CreateMealFragment()
            val args = Bundle()
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "mealLog")
                addToBackStack("mealLog")
                commit()
            }
        }

        layoutCreateMeal.setOnClickListener {
            val fragment = CreateMealFragment()
            val args = Bundle()
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "mealLog")
                addToBackStack("mealLog")
                commit()
            }
        }

        layoutBottomCreateMeal.setOnClickListener {
            val fragment = CreateMealFragment()
            val args = Bundle()
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "mealLog")
                addToBackStack("mealLog")
                commit()
            }
        }

       // myMealLists(mealData)
    }

    private fun myMealsList(mealData: MutableList<MergedMealItem>) {

        if (mealData.size > 0){
            myMealRecyclerView.visibility = View.VISIBLE
            layoutBottomCreateMeal.visibility = View.GONE
            mealPlanTitleLayout.visibility = View.VISIBLE
            layoutNoMeals.visibility = View.GONE
        }else{
            layoutNoMeals.visibility = View.VISIBLE
            myMealRecyclerView.visibility = View.GONE
            layoutBottomCreateMeal.visibility = View.GONE
            mealPlanTitleLayout.visibility = View.GONE
        }
        val valueLists : ArrayList<MergedMealItem> = ArrayList()
        valueLists.addAll(mealData as Collection<MergedMealItem>)
        val mealDetails: MealDetails? = null
        val snapMealDetail: SnapMealDetail? = null
        myMealListAdapter.addAll(valueLists, -1, mealDetails, snapMealDetail, false)
    }

    private fun onMealDeleteItem(mealLogDateModel: MealDetails, position: Int, isRefresh: Boolean) {

//        val valueLists : ArrayList<MyMealModel> = ArrayList()
//        valueLists.addAll(mealLogs as Collection<MyMealModel>)
//        myMealListAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
        deleteBottomSheetFragment = DeleteMealBottomSheet()
        deleteBottomSheetFragment.isCancelable = true
        val bundle = Bundle()
        bundle.putBoolean("test",false)
        deleteBottomSheetFragment.arguments = bundle
        activity?.supportFragmentManager?.let { deleteBottomSheetFragment.show(it, "DeleteMealBottomSheet") }
    }

    private fun onMealLogItem(mealDetails: MealDetails, position: Int, isRefresh: Boolean) {

        val valueLists : ArrayList<MergedMealItem> = ArrayList()
        valueLists.addAll(mergedList as Collection<MergedMealItem>)
        val snapMealDetail: SnapMealDetail? = null
        myMealListAdapter.addAll(valueLists, position, mealDetails, snapMealDetail, isRefresh)
         val mealLogList : ArrayList<MealLogItems> = ArrayList()

        val dishList = mealDetails.receipe_data
        dishList?.forEach { selectedDish ->
            val mealLogData = MealLogItems(
                meal_id = selectedDish.receipe._id,
                recipe_name = "My Meal " + mealDetails.meal_name,
                meal_quantity = 1,
                unit = "g",
                measure = "Bowl"
            )
            mealLogList.add(mealLogData)
        }
        val mealLogRequest = SelectedMealLogList(
            meal_name = "",
            meal_type = mealType,
            meal_log = mealLogList
        )
        //  mealLogRequests = mealLogRequest
        val parent = parentFragment as? HomeTabMealFragment
        parent?.setSelectedFrequentlyLog(null, false, mealLogRequest)
      //  createMealPlanLog(mealPlan)
//       val frequentlyAddDishBottomSheet = FrequentlyAddDishBottomSheet()
//        frequentlyAddDishBottomSheet.isCancelable = true
//        val bundle = Bundle()
//        bundle.putBoolean("test",false)
//        frequentlyAddDishBottomSheet.arguments = bundle
//        activity?.supportFragmentManager?.let { frequentlyAddDishBottomSheet.show(it, "FrequentlyAddDishBottomSheet") }
    }

    private fun onSnapMealDeleteItem(mealLogDateModel: SnapMealDetail, position: Int, isRefresh: Boolean) {
//        val valueLists : ArrayList<MyMealModel> = ArrayList()
//        valueLists.addAll(mealLogs as Collection<MyMealModel>)
//        myMealListAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
        deleteBottomSheetFragment = DeleteMealBottomSheet()
        deleteBottomSheetFragment.isCancelable = true
        val bundle = Bundle()
        bundle.putBoolean("test",false)
        deleteBottomSheetFragment.arguments = bundle
        activity?.supportFragmentManager?.let { deleteBottomSheetFragment.show(it, "DeleteMealBottomSheet") }
    }

    private fun onSnapMealLogItem(snapMealDetail: SnapMealDetail, position: Int, isRefresh: Boolean) {
        val valueLists : ArrayList<MergedMealItem> = ArrayList()
        valueLists.addAll(mergedList as Collection<MergedMealItem>)
        val mealDetails: MealDetails? = null
        myMealListAdapter.addAll(valueLists, position,mealDetails, snapMealDetail, isRefresh)
        val mealLogData = MealLogItems(
            meal_id = snapMealDetail._id,
            recipe_name = "My Meal " + snapMealDetail.meal_name,
            meal_quantity = 1,
            unit = "g",
            measure = "Bowl"
        )
        val parent = parentFragment as? HomeTabMealFragment
        parent?.setSelectedFrequentlyLog(mealLogData, true, null)
    }

    private fun getMealLog() {
        LoaderUtil.showLoader(requireActivity())
         val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
      //  val userId = "64763fe2fa0e40d9c0bc8264"
        val call = ApiClient.apiServiceFastApi.getLogMealList(userId)
        call.enqueue(object : Callback<MealLogPlanResponse> {
            override fun onResponse(call: Call<MealLogPlanResponse>, response: Response<MealLogPlanResponse>) {
                if (response.isSuccessful) {
                    LoaderUtil.dismissLoader(requireActivity())
                    if (response.body() != null){
                        var mealData = response.body()?.meal_plans
                        mealData = mealData
                       // onMyMealItemRefresh(mealData)
                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    LoaderUtil.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<MealLogPlanResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                LoaderUtil.dismissLoader(requireActivity())
            }
        })
    }

    private fun createMealPlanLog(mealPlan: MealPlan) {
        LoaderUtil.showLoader(requireActivity())
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        // val userId = "64763fe2fa0e40d9c0bc8264"
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDateTime.format(formatter)
        val dishIds = mutableListOf<String>()

//        for (item in mealDetails){
//            dishIds.add(item._id)
//        }

        val mealLogRequest = MealPlanLogRequest(
            date = formattedDate
        )
        val call = ApiClient.apiServiceFastApi.createMealPlanLog(userId, mealPlan._id, mealLogRequest)
        call.enqueue(object : Callback<MealPlanResponse> {
            override fun onResponse(call: Call<MealPlanResponse>, response: Response<MealPlanResponse>) {
                if (response.isSuccessful) {
                    LoaderUtil.dismissLoader(requireActivity())
                    val mealData = response.body()?.message
                   // Toast.makeText(activity, mealData, Toast.LENGTH_SHORT).show()
                    loggedBottomSheetFragment = LoggedBottomSheet()
                    loggedBottomSheetFragment.isCancelable = true
                    val bundle = Bundle()
                    bundle.putBoolean("test",false)
                    loggedBottomSheetFragment.arguments = bundle
                    activity?.supportFragmentManager?.let { loggedBottomSheetFragment.show(it, "LoggedBottomSheet") }
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    LoaderUtil.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<MealPlanResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                LoaderUtil.dismissLoader(requireActivity())
            }
        })
    }

    private fun getMyMealList() {
       // LoaderUtil.showLoader(requireActivity())
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val call = ApiClient.apiServiceFastApi.getMyMealList(userId)
        call.enqueue(object : Callback<MyMealsSaveResponse> {
            override fun onResponse(call: Call<MyMealsSaveResponse>, response: Response<MyMealsSaveResponse>) {
                if (response.isSuccessful) {
//                    LoaderUtil.dismissLoader(requireActivity())
                    if (response.body() != null){
                        val myMealsSaveList = response.body()!!.data
                        myMealsSaveList.snap_meal_detail.forEach { snap ->
                            mergedList.add(MergedMealItem.SnapMeal(snap))
                        }
                        myMealsSaveList.meal_details.forEach { saved ->
                            mergedList.add(MergedMealItem.SavedMeal(saved))
                        }
                        myMealsList(mergedList)
                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                  //  LoaderUtil.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<MyMealsSaveResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
              //  LoaderUtil.dismissLoader(requireActivity())
            }
        })
    }

    private fun createDishLog(snapRecipeList : ArrayList<SnapRecipeData>) {
        LoaderUtil.showLoader(requireActivity())
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        // val userId = "64763fe2fa0e40d9c0bc8264"
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDateTime.format(formatter)

        val mealLogList : ArrayList<DishLog> = ArrayList()
        val mealNamesString = snapRecipeList.map { it.recipe_name ?: "" }.joinToString(", ")

        snapRecipeList?.forEach { snapRecipe ->
            val mealLogData = DishLog(
                receipe_id = snapRecipe.id,
                meal_quantity = 1,
                unit = "g",
                measure = "Bowl"
            )
            mealLogList.add(mealLogData)
        }

        val mealLogRequest = SaveDishLogRequest(
            meal_type = "",
            meal_log = mealLogList
        )

        val call = ApiClient.apiServiceFastApi.createSaveMealsToLog(userId, formattedDate, mealLogRequest)
        call.enqueue(object : Callback<MealUpdateResponse> {
            override fun onResponse(call: Call<MealUpdateResponse>, response: Response<MealUpdateResponse>) {
                if (response.isSuccessful) {
                    LoaderUtil.dismissLoader(requireActivity())
                    val mealData = response.body()?.message
                    Toast.makeText(activity, mealData, Toast.LENGTH_SHORT).show()
//                    val fragment = HomeTabMealFragment()
//                    val args = Bundle()
//                    fragment.arguments = args
//                    requireActivity().supportFragmentManager.beginTransaction().apply {
//                        replace(R.id.flFragment, fragment, "landing")
//                        addToBackStack("landing")
//                        commit()
//                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    LoaderUtil.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<MealUpdateResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                LoaderUtil.dismissLoader(requireActivity())
            }
        })
    }
}