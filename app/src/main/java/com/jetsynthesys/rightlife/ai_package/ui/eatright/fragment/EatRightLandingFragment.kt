package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.RecipeResponseModel
import com.jetsynthesys.rightlife.ai_package.model.request.WeightIntakeRequest
import com.jetsynthesys.rightlife.ai_package.model.response.EatRightLandingPageDataResponse
import com.jetsynthesys.rightlife.ai_package.model.response.LogWeightResponse
import com.jetsynthesys.rightlife.ai_package.model.response.MealLogDataResponse
import com.jetsynthesys.rightlife.ai_package.model.response.OtherRecipe
import com.jetsynthesys.rightlife.ai_package.model.response.RegularRecipeEntry
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.LogWeightRulerAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.MealSuggestionListAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.OtherRecipeEatLandingAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.TodayMealLogEatLandingAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.macros.MacrosTabFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.microtab.MicrosTabFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.LandingPageResponse
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MealPlanModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MyMealModel
import com.jetsynthesys.rightlife.ai_package.utils.AppPreference
import com.jetsynthesys.rightlife.ai_package.utils.LoaderUtil
import com.jetsynthesys.rightlife.apimodel.userdata.UserProfileResponse
import com.jetsynthesys.rightlife.apimodel.userdata.Userdata
import com.jetsynthesys.rightlife.databinding.BottomsheetLogWeightSelectionBinding
import com.jetsynthesys.rightlife.databinding.FragmentEatRightLandingBinding
import com.jetsynthesys.rightlife.ui.utility.ConversionUtils
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.floor

class EatRightLandingFragment : BaseFragment<FragmentEatRightLandingBinding>() {

    private lateinit var mealPlanRecyclerView: RecyclerView
    private lateinit var frequentlyLoggedRecyclerView: RecyclerView
    private lateinit var otherReciepeRecyclerView: RecyclerView
    private lateinit var glassWithWaterView: GlassWithWaterView
    private lateinit var todayMacrosWithDataLayout: ConstraintLayout
    private lateinit var todayMacroNoDataLayout: ConstraintLayout
    private lateinit var todayMicrosWithDataLayout: ConstraintLayout
    private lateinit var todayMacroNoDataLayoutOne: ConstraintLayout
    private lateinit var todayMealLogNoDataHeading: ConstraintLayout
    private lateinit var log_your_meal_balance_layout: CardView
    private lateinit var tv_water_quantity: TextView
    private lateinit var last_logged_no_data: TextView
    private lateinit var weightIntake: TextView
    private lateinit var weightIntakeUnit: TextView
    private lateinit var otherRecipeMightLikeWithData: LinearLayout
    private lateinit var new_improvement_layout: LinearLayout
    private  var newBoolean: Boolean = false
    private lateinit var appPreference: AppPreference
    private lateinit var tvProteinValue : TextView
    private lateinit var tvFatsValue : TextView
    private lateinit var tvCabsValue : TextView
    private lateinit var log_your_water_intake_filled : LinearLayout
    private lateinit var loss_new_weight_filled : LinearLayout
    private lateinit var tvCaloriesValue : TextView
    private lateinit var fatsProgressBar : ProgressBar
    private lateinit var proteinProgressBar : ProgressBar
    private lateinit var cabsProgressBar : ProgressBar
    private lateinit var imageBack : ImageView
    private lateinit var hydration_tracker_forward_image : ImageView
    private lateinit var logWeightRulerAdapter: LogWeightRulerAdapter
    private val numbers = mutableListOf<Float>()
    private lateinit var waterIntakeBottomSheet: WaterIntakeBottomSheet
    private lateinit var macroIc : ImageView
    private lateinit var proteinUnitTv : TextView
    private lateinit var carbsUnitTv : TextView
    private lateinit var fatsUnitTv : TextView
    private lateinit var weightLastLogDateTv : TextView
    private lateinit var weightTrackerIc : ImageView
    private lateinit var logFirstMealLayout : LinearLayout
    private lateinit var logMealNoDataButton : ConstraintLayout
    private lateinit var snapMealNoData : ConstraintLayout
    private lateinit var microIc : ImageView
    private lateinit var microValueTv : TextView
    private lateinit var unitMicroTv : TextView
    private lateinit var energyTypeTv : TextView
    private lateinit var recipesButton : ConstraintLayout
    private lateinit var yourMealsLogBtn : ImageView
    private lateinit var landingPageResponse : EatRightLandingPageDataResponse
    private  var regularRecipesList : ArrayList<RegularRecipeEntry> = ArrayList()

     lateinit var userData: Userdata
     lateinit var userDataResponse: UserProfileResponse
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    val viewModel: MasterCalculationsViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEatRightLandingBinding
        get() = FragmentEatRightLandingBinding::inflate

    private val todayMealLogAdapter by lazy { TodayMealLogEatLandingAdapter(requireContext(), arrayListOf(), -1,
        null, false, ::onTodayMealLogItem) }
    private val otherRecipeAdapter by lazy { OtherRecipeEatLandingAdapter(requireContext(), arrayListOf(), -1,
        null, false, ::onOtherRecipeItem) }
    private val mealSuggestionAdapter by lazy { MealSuggestionListAdapter(requireContext(), arrayListOf(),
        -1, null, false, ::onMealSuggestionItem) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newBoolean = true
        appPreference = AppPreference(requireContext())
        val bottomSeatName = arguments?.getString("BottomSeatName").toString()
        sharedPreferenceManager = SharedPreferenceManager.getInstance(context)

        userDataResponse = sharedPreferenceManager.userProfile
        userData = userDataResponse.userdata
        //viewModel.userResponse = userData

        val halfCurveProgressBar = view.findViewById<HalfCurveProgressBar>(R.id.halfCurveProgressBar)
        val snapMealBtn = view.findViewById<ConstraintLayout>(R.id.lyt_snap_meal)
        val mealLogLayout = view.findViewById<LinearLayout>(R.id.layout_meal_log)
        mealPlanRecyclerView = view.findViewById(R.id.recyclerview_meal_plan_item)
        todayMacrosWithDataLayout = view.findViewById(R.id.today_macros_with_data_layout)
        tvProteinValue = view.findViewById(R.id.tv_protien_value)
        hydration_tracker_forward_image = view.findViewById(R.id.hydration_tracker_forward_image)
        tvFatsValue = view.findViewById(R.id.tv_fats_value)
        tvCabsValue = view.findViewById(R.id.tv_carbs_value)
        log_your_water_intake_filled = view.findViewById(R.id.log_your_water_intake_filled)
        weightTrackerIc = view.findViewById(R.id.weightTrackerIc)
        fatsProgressBar = view.findViewById(R.id.fats_progressBar)
        proteinProgressBar = view.findViewById(R.id.protein_progressBar)
        cabsProgressBar = view.findViewById(R.id.carbs_progressBar)
        imageBack = view.findViewById(R.id.imageBack)
      //  tvCaloriesValue = view.findViewById(R.id.tvCaloriesValue)
        todayMacroNoDataLayout = view.findViewById(R.id.today_macro_no_data_layout)
        todayMicrosWithDataLayout = view.findViewById(R.id.today_micros_with_data_layout)
        todayMacroNoDataLayoutOne = view.findViewById(R.id.today_macro_no_data_layout_one)
        todayMealLogNoDataHeading = view.findViewById(R.id.today_meal_log_no_data)
        log_your_meal_balance_layout = view.findViewById(R.id.log_your_meal_balance_layout)
        otherRecipeMightLikeWithData = view.findViewById(R.id.other_reciepie_might_like_with_data)
        tv_water_quantity = view.findViewById(R.id.tv_water_quantity)
        weightIntake = view.findViewById(R.id.weightIntake)
        last_logged_no_data = view.findViewById(R.id.last_logged_no_data)
        weightIntakeUnit = view.findViewById(R.id.weightIntakeUnit)
        new_improvement_layout = view.findViewById(R.id.new_improvement_layout)
        loss_new_weight_filled = view.findViewById(R.id.loss_new_weight_filled)
        macroIc = view.findViewById(R.id.macroIc)
        fatsUnitTv = view.findViewById(R.id.fatsUnitTv)
        proteinUnitTv = view.findViewById(R.id.proteinUnitTv)
        carbsUnitTv = view.findViewById(R.id.carbsUnitTv)
        logMealNoDataButton = view.findViewById(R.id.logMealNoDataButton)
        snapMealNoData = view.findViewById(R.id.lyt_snap_meal_no_data)
        microIc = view.findViewById(R.id.microIc)
        energyTypeTv = view.findViewById(R.id.energyTypeTv)
        microValueTv = view.findViewById(R.id.microValueTv)
        unitMicroTv = view.findViewById(R.id.unitMicroTv)
        recipesButton = view.findViewById(R.id.recipes_button)
        yourMealsLogBtn = view.findViewById(R.id.yourMealsLogBtn)
        logFirstMealLayout = view.findViewById(R.id.logFirstMealLayout)
        weightLastLogDateTv = view.findViewById(R.id.weightLastLogDateTv)

        frequentlyLoggedRecyclerView = view.findViewById(R.id.recyclerview_frequently_logged_item)
        otherReciepeRecyclerView = view.findViewById(R.id.recyclerview_other_reciepe_item)
        otherReciepeRecyclerView.layoutManager = LinearLayoutManager(context)
        otherReciepeRecyclerView.adapter = otherRecipeAdapter

        if (bottomSeatName.contentEquals("LogWeightEat")){
            showLogWeightBottomSheet()
        }else if (bottomSeatName.contentEquals("LogWaterIntakeEat")){
            showWaterIntakeBottomSheet()
        }
        loss_new_weight_filled.setOnClickListener {
            showLogWeightBottomSheet()
        }
        log_your_water_intake_filled.setOnClickListener {
            showWaterIntakeBottomSheet()
        }

        getMealLandingSummary(halfCurveProgressBar)
        hydration_tracker_forward_image.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = HydrationTrackerFragment()
                val args = Bundle()
                args.putString("ModuleName", "EatRight")
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        weightTrackerIc.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = WeightTrackerFragment()
                val args = Bundle()
                args.putString("ModuleName", "EatRight")
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }
       // onOtherReciepeDateItemRefresh()
        mealPlanRecyclerView.layoutManager = LinearLayoutManager(context)
        mealPlanRecyclerView.adapter = todayMealLogAdapter
       // onMealPlanItemRefresh()
        frequentlyLoggedRecyclerView.layoutManager = LinearLayoutManager(context)
        frequentlyLoggedRecyclerView.adapter = mealSuggestionAdapter
       // onFrequentlyLoggedItemRefresh()
      //  halfCurveProgressBar.setValues(2000,2000)
         glassWithWaterView = view.findViewById<GlassWithWaterView>(R.id.glass_with_water_view)

        snapMealBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = SnapMealFragment()
                val args = Bundle()
                args.putString("ModuleName", "EatRight")
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        imageBack.setOnClickListener {
            activity?.finish()
        }

        mealLogLayout.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = YourMealLogsFragment()
                val args = Bundle()
                args.putString("ModuleName", "EatRightLanding")
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        logMealNoDataButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = YourMealLogsFragment()
                val args = Bundle()
                args.putString("ModuleName", "EatRightLanding")
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        snapMealNoData.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = SnapMealFragment()
                val args = Bundle()
                args.putString("ModuleName", "EatRight")
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        macroIc.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = MacrosTabFragment()
                val args = Bundle()
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        microIc.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = MicrosTabFragment()
                val args = Bundle()
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        recipesButton.setOnClickListener {
            val fragment = SearchDishToLogFragment()
            val args = Bundle()
            args.putString("searchType", "EatRight")
            args.putParcelable("snapDishLocalListModel", null)
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "landing")
                addToBackStack("landing")
                commit()
            }
        }

        yourMealsLogBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = YourMealLogsFragment()
                val args = Bundle()
                args.putString("ModuleName", "EatRightLandingWithoutPopup")
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        otherRecipeMightLikeWithData.setOnClickListener {
            val fragment = SearchDishToLogFragment()
            val args = Bundle()
            args.putString("searchType", "EatRight")
            args.putParcelable("snapDishLocalListModel", null)
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "landing")
                addToBackStack("landing")
                commit()
            }
        }

        logFirstMealLayout.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = YourMealLogsFragment()
                val args = Bundle()
                args.putString("ModuleName", "EatRightLanding")
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

    }

    private fun onMealSuggestionList(landingPageResponse : LandingPageResponse) {
        val meal = listOf(
            MyMealModel("Breakfast", landingPageResponse.next_meal_suggestion.meal_name, "1",
                landingPageResponse.next_meal_suggestion.calories.toString(),
                landingPageResponse.next_meal_suggestion.protein.toString(),
                landingPageResponse.next_meal_suggestion.carbs.toString(),
                landingPageResponse.next_meal_suggestion.fats.toString(),
                true),
        )
        if (meal.size > 0) {
            frequentlyLoggedRecyclerView.visibility = View.VISIBLE
        } else {
            frequentlyLoggedRecyclerView.visibility = View.GONE
        }
        val valueLists: ArrayList<MyMealModel> = ArrayList()
        valueLists.addAll(meal as Collection<MyMealModel>)
        val mealLogDateData: MyMealModel? = null
        mealSuggestionAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onMealSuggestionItem(mealLogDateModel: MyMealModel, position: Int, isRefresh: Boolean) {
//        val mealLogs = listOf(
//            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", true),
//            MyMealModel("Breakfast", "Dal", "1", "1,157", "8", "308", "17", false),
//            MyMealModel("Breakfast", "Rice", "1", "1,157", "8", "308", "17", false),
//            MyMealModel("Breakfast", "Roti", "1", "1,157", "8", "308", "17", false)
//        )
//
//        val valueLists: ArrayList<MyMealModel> = ArrayList()
//        valueLists.addAll(mealLogs as Collection<MyMealModel>)
//        frequentlyLoggedListAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
//
//        val newIngredient = mealLogDateModel
//        for (ingredient in valueLists) {
//            if (ingredient.isAddDish == true) {
//                // Handle ingredient addition if needed
//            }
//        }
    }

    private fun onTodayMealLogList(landingPageResponse: ArrayList<RegularRecipeEntry>) {
        val meal = listOf(
            MealPlanModel("Breakfast", "Poha", "Vegeterian", "25", "1", "1,157", "8", "308", "17"),
            MealPlanModel("Lunch", "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun", "Vegeterian", "25", "1", "1,157", "8", "308", "17"),
            MealPlanModel("Dinner", "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun", "Vegeterian", "25", "1", "1,157", "8", "308", "17")
        )

        val valueLists: ArrayList<RegularRecipeEntry> = ArrayList()
        valueLists.addAll(landingPageResponse as Collection<RegularRecipeEntry>)
        val mealPlanData: RegularRecipeEntry? = null
        todayMealLogAdapter.addAll(valueLists, -1, mealPlanData, false)
    }

    private fun onTodayMealLogItem(mealLogDateModel: RegularRecipeEntry, position: Int, isRefresh: Boolean) {
        val mealLogs = listOf(
            MealPlanModel("Breakfast", "Poha", "Vegeterian", "25", "1", "1,157", "8", "308", "17"),
            MealPlanModel("Lunch", "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun", "Vegeterian", "25", "1", "1,157", "8", "308", "17"),
            MealPlanModel("Dinner", "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun", "Vegeterian", "25", "1", "1,157", "8", "308", "17")
        )
//        val valueLists: ArrayList<MealPlanModel> = ArrayList()
//        valueLists.addAll(mealLogs as Collection<MealPlanModel>)
    }

    private fun onOtherRecipeList(recipeSuggestion: List<OtherRecipe>) {
        val meal = listOf(
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", true),
            MyMealModel("Breakfast", "Dal", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Rice", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Roti", "1", "1,157", "8", "308", "17", false)
        )

        if (recipeSuggestion.size > 0) {
            frequentlyLoggedRecyclerView.visibility = View.VISIBLE
        } else {
            frequentlyLoggedRecyclerView.visibility = View.GONE
        }
        val valueLists: ArrayList<OtherRecipe> = ArrayList()
        valueLists.addAll(recipeSuggestion as Collection<OtherRecipe>)
        val mealLogDateData: OtherRecipe? = null
        otherRecipeAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onOtherRecipeItem(recipesModel: OtherRecipe, position: Int, isRefresh: Boolean) {

//        val valueLists : ArrayList<RecipeList> = ArrayList()
//        valueLists.addAll(recipesList as Collection<RecipeList>)
//        searchDishAdapter.addAll(valueLists, position, recipesModel, isRefresh)

//        val fragment = DishFragment()
//        val args = Bundle()
//        args.putString("searchType", "EatRight")
//        args.putString("recipeName", recipesModel.name)
//        args.putString("recipeImage", recipesModel.image)
//        fragment.arguments = args
//        requireActivity().supportFragmentManager.beginTransaction().apply {
//            replace(R.id.flFragment, fragment, "landing")
//            addToBackStack("landing")
//            commit()
//        }
    }


    private fun getMealLandingSummary(halfCurveProgressBar: HalfCurveProgressBar) {
        LoaderUtil.showLoader(requireActivity())
       // val userId = appPreference.getUserId().toString()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
       // val userId = "67f6698fa213d14e22a47c2a"
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDateTime.format(formatter)

        val call = ApiClient.apiServiceFastApi.getMealLandingSummary(userId, formattedDate)
        call.enqueue(object : Callback<EatRightLandingPageDataResponse> {
            override fun onResponse(call: Call<EatRightLandingPageDataResponse>, response: Response<EatRightLandingPageDataResponse>) {
                if (response.isSuccessful) {
                    LoaderUtil.dismissLoader(requireActivity())
                    landingPageResponse = response.body()!!
                    println(landingPageResponse)
//                    val mealPlanLists = response.body()?.data ?: emptyList()
//                    recipesList.addAll(mealPlanLists)
                    setMealSummaryData(landingPageResponse, halfCurveProgressBar)
                   // onTodayMealLogList(landingPageResponse)
                    //onMealSuggestionList(landingPageResponse)
                    getMealsLogList()
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    LoaderUtil.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<EatRightLandingPageDataResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                LoaderUtil.dismissLoader(requireActivity())
            }
        })
    }

    private fun setMealSummaryData(landingPageResponse: EatRightLandingPageDataResponse, halfCurveProgressBar: HalfCurveProgressBar){

        if(landingPageResponse.total_calories.toInt() > 0){
            todayMacrosWithDataLayout.visibility = View.VISIBLE
            todayMacroNoDataLayout.visibility = View.GONE
            todayMicrosWithDataLayout.visibility = View.VISIBLE
            todayMacroNoDataLayoutOne.visibility = View.GONE
            todayMealLogNoDataHeading.visibility = View.GONE
            mealPlanRecyclerView.visibility =View.VISIBLE
            log_your_meal_balance_layout.visibility = View.GONE
            otherRecipeMightLikeWithData.visibility = View.VISIBLE
            otherReciepeRecyclerView.visibility = View.VISIBLE
            tv_water_quantity.text = landingPageResponse.total_water_ml.toInt().toString()
            val waterIntake = landingPageResponse.total_water_ml.toFloat()
            val waterGoal = 3000f
            glassWithWaterView.setTargetWaterLevel(waterIntake, waterGoal)
            last_logged_no_data.visibility = View.GONE
            weightIntake.visibility = View.VISIBLE
            weightIntakeUnit.visibility = View.VISIBLE
            new_improvement_layout.visibility = View.VISIBLE
            logFirstMealLayout.visibility = View.GONE
            weightLastLogDateTv.visibility = View.VISIBLE
        }else{
            todayMacroNoDataLayout.visibility = View.VISIBLE
            todayMacrosWithDataLayout.visibility = View.GONE
            todayMicrosWithDataLayout.visibility = View.GONE
            todayMacroNoDataLayoutOne.visibility = View.VISIBLE
            todayMealLogNoDataHeading.visibility = View.VISIBLE
            mealPlanRecyclerView.visibility = View.GONE
            log_your_meal_balance_layout.visibility = View.GONE
            otherRecipeMightLikeWithData.visibility = View.GONE
            otherReciepeRecyclerView.visibility = View.GONE
            tv_water_quantity.text = "0"
            last_logged_no_data.visibility = View.VISIBLE
            weightIntake.visibility = View.GONE
            weightIntakeUnit.visibility = View.GONE
            new_improvement_layout.visibility = View.GONE
            logFirstMealLayout.visibility = View.VISIBLE
            weightLastLogDateTv.visibility = View.GONE
        }

        weightIntake.text = landingPageResponse.last_weight_log?.weight?.toFloat().toString()
        weightIntakeUnit.text = landingPageResponse.last_weight_log?.type
        val convertedDate = convertDate(landingPageResponse.last_weight_log?.date.toString())
        weightLastLogDateTv.text = convertedDate

        if (landingPageResponse.total_protein.toInt() >  landingPageResponse.max_protein.toInt()) {
            tvProteinValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.macros_high_color))
        }else{
            tvProteinValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_no_meals))
        }

        if (landingPageResponse.total_carbs.toInt() >  landingPageResponse.max_carbs.toInt()) {
            tvCabsValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.macros_high_color))
        }else{
            tvCabsValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_no_meals))
        }

        if (landingPageResponse.total_fat.toInt() >  landingPageResponse.max_fat.toInt()) {
            tvFatsValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.macros_high_color))
        }else{
            tvFatsValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_no_meals))
        }

      //  tvCaloriesValue.text = landingPageResponse.total_calories.toString()
        tvProteinValue.text = landingPageResponse.total_protein.toInt().toString()
        tvCabsValue.text = landingPageResponse.total_carbs.toInt().toString()
        tvFatsValue.text = landingPageResponse.total_fat.toInt().toString()
        carbsUnitTv.text = " / " + landingPageResponse.max_carbs.toInt().toString() +" g"
        proteinUnitTv.text = " / " + landingPageResponse.max_protein.toInt().toString() +" g"
        fatsUnitTv.text = " / " + landingPageResponse.max_fat.toInt().toString() +" g"

        cabsProgressBar.max = landingPageResponse.max_carbs.toInt()
        cabsProgressBar.progress = landingPageResponse.total_carbs.toInt()
        proteinProgressBar.max = landingPageResponse.max_protein.toInt()
        proteinProgressBar.progress = landingPageResponse.total_protein.toInt()
        fatsProgressBar.max = landingPageResponse.max_fat.toInt()
        fatsProgressBar.progress = landingPageResponse.total_fat.toInt()

        halfCurveProgressBar.setValues(landingPageResponse.total_calories.toInt(),landingPageResponse.max_calories.toInt())
        halfCurveProgressBar.setProgress(100f)
        val animator = ValueAnimator.ofFloat(0f, 100f)
        animator.duration = 1000
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            halfCurveProgressBar.setProgress(value)
        }
        animator.start()
        onOtherRecipeList(landingPageResponse.other_recipes_you_might_like)
    }

    fun convertDate(inputDate: String): String {
        return inputDate.substringBefore("T")
    }

    private fun getMealRecipesLists() {
        LoaderUtil.showLoader(requireActivity())
        val userId = appPreference.getUserId().toString()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val call = ApiClient.apiService.getMealRecipesList(token)
        call.enqueue(object : Callback<RecipeResponseModel> {
            override fun onResponse(call: Call<RecipeResponseModel>, response: Response<RecipeResponseModel>) {
                if (response.isSuccessful) {
                    LoaderUtil.dismissLoader(requireActivity())
                    val mealPlanLists = response.body()?.data ?: emptyList()
                        //  recipesList.addAll(mealPlanLists)
                    //onOtherReciepeDateItemRefresh(mealPlanLists)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    LoaderUtil.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<RecipeResponseModel>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                LoaderUtil.dismissLoader(requireActivity())
            }
        })
    }

    private fun showLogWeightBottomSheet() {
        // Create and configure BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        var selectedLabel = " kg"
        var selectedWeight = ""//binding.tvWeight.text.toString()
        if (selectedWeight.isEmpty()) {
            selectedWeight = "50 kg"
        } else {
            val w = selectedWeight.split(" ")
            selectedLabel = " ${w[1]}"
        }
        // Inflate the BottomSheet layout
        val dialogBinding = BottomsheetLogWeightSelectionBinding.inflate(layoutInflater)
        val bottomSheetView = dialogBinding.root
        bottomSheetDialog.setContentView(bottomSheetView)
        dialogBinding.selectedNumberText.text = selectedWeight
        if (selectedLabel == " lbs") {
            dialogBinding.switchWeightMetric.isChecked = true
        }

        val thumbColors = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
            intArrayOf(Color.parseColor("#03B27B"), Color.parseColor("#03B27B"))
        )

        val trackColors = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
            intArrayOf(Color.parseColor("#F2F2F2"), Color.parseColor("#F2F2F2"))
        )

        dialogBinding.switchWeightMetric.thumbTintList = thumbColors
        dialogBinding.switchWeightMetric.trackTintList = trackColors
        dialogBinding.switchWeightMetric.setOnCheckedChangeListener { buttonView, isChecked ->
            val w = selectedWeight.split(" ")
            if (isChecked) {
                selectedLabel = " lbs"
                selectedWeight = ConversionUtils.convertLbsToKgs(w[0])
                setLbsValue()
            } else {
                selectedLabel = " kgs"
                selectedWeight = ConversionUtils.convertKgToLbs(w[0])
                setKgsValue()
            }
            dialogBinding.rulerView.layoutManager?.scrollToPosition(floor(selectedWeight.toDouble() * 10).toInt())
            selectedWeight += selectedLabel
            dialogBinding.selectedNumberText.text = selectedWeight
        }

        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dialogBinding.rulerView.layoutManager = layoutManager
        // Generate numbers with increments of 0.1

        for (i in 0..1000) {
            numbers.add(i / 10f) // Increment by 0.1
        }

        logWeightRulerAdapter = LogWeightRulerAdapter(numbers) { number ->
            // Handle the selected number
        }
        dialogBinding.rulerView.adapter = logWeightRulerAdapter

        // Center number with snap alignment
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(dialogBinding.rulerView)

        dialogBinding.rulerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Get the currently snapped position
                    val snappedView = snapHelper.findSnapView(recyclerView.layoutManager)
                    if (snappedView != null) {
                        val position = recyclerView.layoutManager!!.getPosition(snappedView)
                        val snappedNumber = numbers[position]
                        //selected_number_text.setText("$snappedNumber Kg")
                        dialogBinding.selectedNumberText.text = "$snappedNumber $selectedLabel"
                        selectedWeight = dialogBinding.selectedNumberText.text.toString()
                    }
                }
            }
        })

        dialogBinding.rlRulerContainer.post {
            // Get the width of the parent LinearLayout
            val parentWidth: Int = dialogBinding.rlRulerContainer.width
            // Calculate horizontal padding (half of parent width)
            val paddingHorizontal = parentWidth / 2
            // Set horizontal padding programmatically
            dialogBinding.rulerView.setPadding(
                paddingHorizontal,
                dialogBinding.rulerView.paddingTop,
                paddingHorizontal,
                dialogBinding.rulerView.paddingBottom
            )
        }

        // Scroll to the center after layout is measured
        dialogBinding.rulerView.post {
            // Calculate the center position
            val itemCount =
                if (dialogBinding.rulerView.adapter != null) dialogBinding.rulerView.adapter!!.itemCount else 0
            val centerPosition = itemCount / 2
            // Scroll to the center position
            layoutManager.scrollToPositionWithOffset(centerPosition, 0)
        }

        dialogBinding.btnConfirm.setOnClickListener {
           // bottomSheetDialog.dismiss()
            dialogBinding.rulerView.adapter = null
            val fullWeight = selectedWeight.trim()
            val parts = fullWeight.split(Regex("\\s+"))
            val weightValue = parts[0]   // "50.7"
            val weightUnit = parts.getOrElse(1) { "kg" }
            weightIntake.text = weightValue
            weightIntakeUnit.text = weightUnit
            val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val request = WeightIntakeRequest(
                userId = userId,
                source = "apple",
                type = weightUnit,
                waterMl = weightValue.toFloat(),
                date = currentDate
            )
            val call = com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient.apiServiceFastApi.logWeightIntake(request)
            call.enqueue(object : Callback<LogWeightResponse> {
                override fun onResponse(
                    call: Call<LogWeightResponse>,
                    response: Response<LogWeightResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        bottomSheetDialog.dismiss()
                        dialogBinding.rulerView.adapter = null
                        val fullWeight = selectedWeight.trim()
                        val parts = fullWeight.split(Regex("\\s+"))
                        val weightValue = parts[0]   // "50.7"
                        val weightUnit = parts.getOrElse(1) { "kg" }
                        weightIntake.text = response.body()?.waterMl.toString()
                        weightIntakeUnit.text = weightUnit
                        Log.d("LogWaterAPI", "Success: $responseBody")
                        // You can do something with responseBody here
                    } else {
                        Log.e(
                            "LogWaterAPI",
                            "Error: ${response.code()} - ${response.errorBody()?.string()}"
                        )
                    }
                }
                override fun onFailure(call: Call<LogWeightResponse>, t: Throwable) {
                    Log.e("LogWaterAPI", "Failure: ${t.localizedMessage}")
                }
            })
         //   binding.tvWeight.text = selectedWeight
        }

        dialogBinding.closeIV.setOnClickListener {
            bottomSheetDialog.dismiss()
            dialogBinding.rulerView.adapter = null
        }
        bottomSheetDialog.show()
         fun logUserWaterIntake(
            userId: String,
            source: String,
            waterMl: Int,
            date: String
        ) {
        }
    }

    private fun setKgsValue() {
        numbers.clear()
        for (i in 0..1000) {
            numbers.add(i / 10f) // Increment by 0.1
        }
        logWeightRulerAdapter.notifyDataSetChanged()
    }

    private fun setLbsValue() {
        numbers.clear()
        for (i in 0..2204) {
            numbers.add(i / 10f)
        }
        logWeightRulerAdapter.notifyDataSetChanged()
    }

    private fun showWaterIntakeBottomSheet() {
        val waterIntakeBottomSheet = WaterIntakeBottomSheet()
        waterIntakeBottomSheet.isCancelable = false
        waterIntakeBottomSheet.listener = object : OnWaterIntakeConfirmedListener {
            override fun onWaterIntakeConfirmed(amount: Int) {
                // 👇 Use the amount here
                //Toast.makeText(requireContext(), "Water Intake: $amount ml", Toast.LENGTH_SHORT).show()
                tv_water_quantity.text = amount.toString()
                val waterIntake = amount.toFloat()
                val waterGoal = 3000f
                glassWithWaterView.setTargetWaterLevel(waterIntake, waterGoal)
                // You can now call API or update UI etc.
            }
        }
        waterIntakeBottomSheet.show(parentFragmentManager, WaterIntakeBottomSheet.TAG)
    }

    private fun getMealsLogList() {
        LoaderUtil.showLoader(requireActivity())
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDateTime.format(formatter)
        val call = ApiClient.apiServiceFastApi.getMealsLogByDate(userId, formattedDate)
        call.enqueue(object : Callback<MealLogDataResponse> {
            override fun onResponse(call: Call<MealLogDataResponse>, response: Response<MealLogDataResponse>) {
                if (response.isSuccessful) {
                    LoaderUtil.dismissLoader(requireActivity())
                    //  val mealPlanLists = response.body()?.meals ?: emptyList()
                    //   mealList.addAll(mealPlanLists)
                    //  onMealLogDateItemRefresh()
                    val gson = Gson()
                  //  val response = gson.fromJson(jsonString, MealLogDataResponse::class.java)
                    val breakfastRecipes = response.body()?.data!!.meal_detai["Breakfast"]?.regular_receipes
                    val dinnerastRecipes = response.body()?.data!!.meal_detai["Dinner"]?.regular_receipes
                    val lunchSnapMeals = response.body()?.data!!.meal_detai["Lunch"]?.snap_meals
                    if (dinnerastRecipes != null) {
                        regularRecipesList.addAll(dinnerastRecipes)
                        onTodayMealLogList(regularRecipesList)
                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, response.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                    LoaderUtil.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<MealLogDataResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                LoaderUtil.dismissLoader(requireActivity())
            }
        })
    }
}

class MasterCalculationsViewModel  : ViewModel() {



    private val _userResponse = MutableStateFlow<Userdata?>(null)
    val userResponse: StateFlow<Userdata?> = _userResponse

//    fun getUserDetails(callback: (Boolean, String?) -> Unit) {
//        viewModelScope.launch {
//            try {
//                val response = u // suspend function
//                _userResponse.value = response
//                callback(true, null)
//            } catch (e: Exception) {
//                val errorMessage = AppUtility.fetchErrorMessage(e)
//                callback(false, errorMessage ?: "Something went wrong.")
//            }
//        }
//    }

    fun calculateBMI(weight: Double, height: Double): Double {
        return weight / (height * height)
    }

    fun calculateBodyFatPercentage(weight: Double, height: Double, age: Int, gender: String): Double {
        val bmi = calculateBMI(weight, height)
        return if (gender.lowercase() == "male") {
            1.20 * bmi + 0.23 * age - 16.2
        } else {
            1.20 * bmi + 0.23 * age - 5.4
        }
    }

    fun calculateBMR(weight: Double, height: Double, age: Int, gender: String): Double {
        return if (gender.lowercase() == "male") {
            10 * weight + 6.25 * height * 100 - 5 * age + 5
        } else {
            10 * weight + 6.25 * height * 100 - 5 * age - 161
        }
    }

    fun calculateTDEE(bmr: Double, activityLevel: Double): Double {
        return bmr * activityLevel
    }

//    fun getCalculatedValues(): NutrientIntakes? {
//        val model = _userResponse.value
//        return model?.data?.let {
//            calculateIntakes(
//                weight = it.weight ?: 0.0,
//                height = (it.height ?: 0.0) / 100.0,
//                age = (it.age ?: 0.0).toInt(),
//                gender = "male",
//                activityLevel = 1.55
//            )
//        }
//    }
//
//    fun calculateIntakes(
//        weight: Double,
//        height: Double,
//        age: Int,
//        gender: String,
//        activityLevel: Double
//    ): NutrientIntakes {
//        val bodyFatPercentage = calculateBodyFatPercentage(weight, height, age, gender)
//        val bmr = calculateBMR(weight, height, age, gender)
//        val tdee = calculateTDEE(bmr, activityLevel)
//
//        val fatMass = weight * (bodyFatPercentage / 100)
//        val leanMass = weight - fatMass
//        val idealProtein = 2.6 * leanMass
//        val caloriesFromProtein = 4 * idealProtein
//        val remainingCalories = tdee - caloriesFromProtein
//        val caloriesFromCarbs = 0.6 * remainingCalories
//        val caloriesFromFats = 0.4 * remainingCalories
//
//        return NutrientIntakes(
//            dailyCaloricGoal = tdee,
//            protein = idealProtein,
//            carbs = caloriesFromCarbs / 4,
//            fats = caloriesFromFats / 9,
//            bodyFatPercentage = bodyFatPercentage,
//            tdee = tdee
//        )
 //   }
}
