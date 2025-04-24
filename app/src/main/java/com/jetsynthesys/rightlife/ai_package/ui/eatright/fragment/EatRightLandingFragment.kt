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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.RecipeResponseModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.TodayMealLogEatLandingAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.MealSuggestionListAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.OtherRecipeEatLandingAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.LandingPageResponse
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MealPlanModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MyMealModel
import com.jetsynthesys.rightlife.ai_package.utils.AppPreference
import com.jetsynthesys.rightlife.databinding.FragmentEatRightLandingBinding
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.LogWeightRulerAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.macros.MacrosTabFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MealList
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.RecipeSuggestion
import com.jetsynthesys.rightlife.databinding.BottomsheetLogWeightSelectionBinding
import com.jetsynthesys.rightlife.ui.utility.ConversionUtils
import com.jetsynthesys.rightlife.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    private lateinit var text_heading_calories: TextView
    private lateinit var text_heading_calories_unit: TextView
    private lateinit var other_reciepie_might_like_with_data: LinearLayout
    private lateinit var new_improvement_layout: LinearLayout
    private  var newBoolean: Boolean = false
    private lateinit var appPreference: AppPreference
    private lateinit var landingPageResponse : LandingPageResponse
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
    private lateinit var waterIntakeLayout : LinearLayout
    private lateinit var logMealNoDataButton : ConstraintLayout
    private lateinit var snapMealNoData : ConstraintLayout

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
        //SelectMealTypeEat
        //LogWeightEat
        //LogWaterIntakeEat
        //Not

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
        other_reciepie_might_like_with_data = view.findViewById(R.id.other_reciepie_might_like_with_data)
        tv_water_quantity = view.findViewById(R.id.tv_water_quantity)
        text_heading_calories = view.findViewById(R.id.text_heading_calories)
        last_logged_no_data = view.findViewById(R.id.last_logged_no_data)
        text_heading_calories_unit = view.findViewById(R.id.text_heading_calories_unit)
        new_improvement_layout = view.findViewById(R.id.new_improvement_layout)
        loss_new_weight_filled = view.findViewById(R.id.loss_new_weight_filled)
        macroIc = view.findViewById(R.id.macroIc)
        fatsUnitTv = view.findViewById(R.id.fatsUnitTv)
        proteinUnitTv = view.findViewById(R.id.proteinUnitTv)
        carbsUnitTv = view.findViewById(R.id.carbsUnitTv)
        logMealNoDataButton = view.findViewById(R.id.logMealNoDataButton)
        snapMealNoData = view.findViewById(R.id.lyt_snap_meal_no_data)

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

        getMealRecipesList(halfCurveProgressBar)
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
        val waterIntake = 1000f
        val waterGoal = 3000f
        glassWithWaterView.setTargetWaterLevel(waterIntake, waterGoal)

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

    private fun onTodayMealLogList(landingPageResponse : LandingPageResponse) {
        val meal = listOf(
            MealPlanModel("Breakfast", "Poha", "Vegeterian", "25", "1", "1,157", "8", "308", "17"),
            MealPlanModel("Lunch", "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun", "Vegeterian", "25", "1", "1,157", "8", "308", "17"),
            MealPlanModel("Dinner", "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun", "Vegeterian", "25", "1", "1,157", "8", "308", "17")
        )

        val valueLists: ArrayList<MealList> = ArrayList()
        valueLists.addAll(landingPageResponse.meals as Collection<MealList>)
        val mealPlanData: MealList? = null
        todayMealLogAdapter.addAll(valueLists, -1, mealPlanData, false)
    }

    private fun onTodayMealLogItem(mealLogDateModel: MealList, position: Int, isRefresh: Boolean) {
        val mealLogs = listOf(
            MealPlanModel("Breakfast", "Poha", "Vegeterian", "25", "1", "1,157", "8", "308", "17"),
            MealPlanModel("Lunch", "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun", "Vegeterian", "25", "1", "1,157", "8", "308", "17"),
            MealPlanModel("Dinner", "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun", "Vegeterian", "25", "1", "1,157", "8", "308", "17")
        )
//        val valueLists: ArrayList<MealPlanModel> = ArrayList()
//        valueLists.addAll(mealLogs as Collection<MealPlanModel>)
    }

    private fun onOtherRecipeList(recipeSuggestion: List<RecipeSuggestion>) {
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


        val valueLists: ArrayList<RecipeSuggestion> = ArrayList()
        valueLists.addAll(recipeSuggestion as Collection<RecipeSuggestion>)
        val mealLogDateData: RecipeSuggestion? = null
        otherRecipeAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onOtherRecipeItem(recipesModel: RecipeSuggestion, position: Int, isRefresh: Boolean) {

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


    private fun getMealRecipesList(halfCurveProgressBar: HalfCurveProgressBar) {
        Utils.showLoader(requireActivity())
       // val userId = appPreference.getUserId().toString()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val userId = "64763fe2fa0e40d9c0bc8264"
        val startDate = "2025-03-25"
        val call = ApiClient.apiServiceFastApi.getMealSummary(userId)
        call.enqueue(object : Callback<LandingPageResponse> {
            override fun onResponse(call: Call<LandingPageResponse>, response: Response<LandingPageResponse>) {
                if (response.isSuccessful) {
                    Utils.dismissLoader(requireActivity())
                    landingPageResponse = response.body()!!
                    println(landingPageResponse)
//                    val mealPlanLists = response.body()?.data ?: emptyList()
//                    recipesList.addAll(mealPlanLists)
                    setMealSummaryData(landingPageResponse, halfCurveProgressBar)
                    onTodayMealLogList(landingPageResponse)
                    onMealSuggestionList(landingPageResponse)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Utils.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<LandingPageResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                Utils.dismissLoader(requireActivity())
            }
        })
    }

    private fun setMealSummaryData(landingPageResponse: LandingPageResponse, halfCurveProgressBar: HalfCurveProgressBar){

        if(landingPageResponse.meals.isNotEmpty()){
            todayMacrosWithDataLayout.visibility = View.VISIBLE
            todayMacroNoDataLayout.visibility = View.GONE
            todayMicrosWithDataLayout.visibility = View.VISIBLE
            todayMacroNoDataLayoutOne.visibility = View.GONE
            todayMealLogNoDataHeading.visibility = View.GONE
            mealPlanRecyclerView.visibility =View.VISIBLE
            log_your_meal_balance_layout.visibility = View.GONE
            other_reciepie_might_like_with_data.visibility = View.VISIBLE
            otherReciepeRecyclerView.visibility = View.VISIBLE
            tv_water_quantity.text = "400"
            last_logged_no_data.visibility = View.GONE
            text_heading_calories.visibility = View.VISIBLE
            text_heading_calories_unit.visibility = View.VISIBLE
            new_improvement_layout.visibility = View.VISIBLE
        }else{
            todayMacroNoDataLayout.visibility = View.VISIBLE
            todayMacrosWithDataLayout.visibility = View.GONE
            todayMicrosWithDataLayout.visibility = View.GONE
            todayMacroNoDataLayoutOne.visibility = View.VISIBLE
            todayMealLogNoDataHeading.visibility = View.VISIBLE
            mealPlanRecyclerView.visibility = View.GONE
            log_your_meal_balance_layout.visibility = View.GONE
            other_reciepie_might_like_with_data.visibility = View.GONE
            otherReciepeRecyclerView.visibility = View.GONE
            tv_water_quantity.text = "0"
            last_logged_no_data.visibility = View.VISIBLE
            text_heading_calories.visibility = View.GONE
            text_heading_calories_unit.visibility = View.GONE
            new_improvement_layout.visibility = View.GONE
        }

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

    private fun getMealRecipesLists() {
        Utils.showLoader(requireActivity())
        val userId = appPreference.getUserId().toString()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val call = ApiClient.apiService.getMealRecipesList(token)
        call.enqueue(object : Callback<RecipeResponseModel> {
            override fun onResponse(call: Call<RecipeResponseModel>, response: Response<RecipeResponseModel>) {
                if (response.isSuccessful) {
                    Utils.dismissLoader(requireActivity())
                    val mealPlanLists = response.body()?.data ?: emptyList()
                        //  recipesList.addAll(mealPlanLists)
                    //onOtherReciepeDateItemRefresh(mealPlanLists)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Utils.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<RecipeResponseModel>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                Utils.dismissLoader(requireActivity())
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
            bottomSheetDialog.dismiss()
            dialogBinding.rulerView.adapter = null
            val fullWeight = selectedWeight.trim()
            val parts = fullWeight.split(Regex("\\s+"))
            val weightValue = parts[0]   // "50.7"
            val weightUnit = parts.getOrElse(1) { "kg" }
            text_heading_calories.text = weightValue
            text_heading_calories_unit.text = weightUnit
         //   binding.tvWeight.text = selectedWeight
        }

        dialogBinding.closeIV.setOnClickListener {

            bottomSheetDialog.dismiss()
            dialogBinding.rulerView.adapter = null

        }
        bottomSheetDialog.show()
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


}