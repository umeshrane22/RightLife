package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.model.FoodDetailsResponse
import com.jetsynthesys.rightlife.ai_package.model.MealDetails
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.MacroNutrientsAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.MicroNutrientsAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.tab.FrequentlyLoggedListAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MacroNutrientsModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MicroNutrientsModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MyMealModel
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentDishBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.core.view.isVisible
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.MealLogRequest
import com.jetsynthesys.rightlife.ai_package.model.MealLogResponse
import com.jetsynthesys.rightlife.ai_package.model.MealsResponse
import com.jetsynthesys.rightlife.ai_package.model.response.RecipeResponse
import com.jetsynthesys.rightlife.ai_package.model.response.SnapRecipeData
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.createmeal.SearchDishFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.DishLocalListModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.SnapDishLocalListModel
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import com.jetsynthesys.rightlife.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SnapDishFragment : BaseFragment<FragmentDishBinding>() {

    private lateinit var macroItemRecyclerView : RecyclerView
    private lateinit var microItemRecyclerView : RecyclerView
    private lateinit var addToTheMealLayout : LinearLayoutCompat
    private lateinit var layoutMicroTitle : ConstraintLayout
    private lateinit var layoutMacroTitle : ConstraintLayout
    private lateinit var icMacroUP : ImageView
    private lateinit var microUP : ImageView
    private lateinit var imgFood : ImageView
    private lateinit var btnLogMeal : LinearLayoutCompat
    private lateinit var editDeleteBreakfast : CardView
    private lateinit var tvMealName : TextView
    private lateinit var addToTheMealTV : TextView
    private lateinit var layoutMain : ConstraintLayout
    private lateinit var searchType : String
    private lateinit var recipeName : String
    private lateinit var recipeImage : String
    private lateinit var tvCheckOutRecipe: TextView
    private lateinit var tvChange: TextView
    private lateinit var tvQuantity: EditText
    private var quantity = 1
    private lateinit var tvMeasure :TextView
    private var dishLists : ArrayList<SnapRecipeData> = ArrayList()
    private lateinit var snapDishLocalListModel : SnapDishLocalListModel

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDishBinding
        get() = FragmentDishBinding::inflate

    private val macroNutrientsAdapter by lazy { MacroNutrientsAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMacroNutrientsItemClick) }
    private val microNutrientsAdapter by lazy { MicroNutrientsAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMicroNutrientsItemClick) }
    private val frequentlyLoggedListAdapter by lazy { FrequentlyLoggedListAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onFrequentlyLoggedItem) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.meal_log_background))

        macroItemRecyclerView = view.findViewById(R.id.recyclerview_macro_item)
        microItemRecyclerView = view.findViewById(R.id.recyclerview_micro_item)
        addToTheMealLayout = view.findViewById(R.id.layout_addToTheMeal)
        tvCheckOutRecipe = view.findViewById(R.id.tv_CheckOutRecipe)
        tvChange = view.findViewById(R.id.tv_change)
        tvQuantity = view.findViewById(R.id.tvQuantity)
        tvMeasure = view.findViewById(R.id.tvMeasure)
        addToTheMealTV = view.findViewById(R.id.tv_addToTheMeal)
        tvMealName = view.findViewById(R.id.tvMealName)
        imgFood = view.findViewById(R.id.imgFood)
        layoutMicroTitle = view.findViewById(R.id.layoutMicroTitle)
        layoutMacroTitle = view.findViewById(R.id.layoutMacroTitle)
        microUP = view.findViewById(R.id.microUP)
        icMacroUP = view.findViewById(R.id.icMacroUP)

        searchType = arguments?.getString("searchType").toString()
        val snapRecipeName = arguments?.getString("snapRecipeName").toString()
        val foodDetailsResponse = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("recipeResponse", RecipeResponse::class.java)
        } else {
            arguments?.getParcelable("recipeResponse")
        }

        val snapDishLocalListModels = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("snapDishLocalListModel", SnapDishLocalListModel::class.java)
        } else {
            arguments?.getParcelable("snapDishLocalListModel")
        }

        if (snapDishLocalListModels != null){
            snapDishLocalListModel = snapDishLocalListModels
            if (foodDetailsResponse?.data != null){
                if (snapDishLocalListModel.data.size > 0){
                    dishLists.addAll(snapDishLocalListModel.data)
                }
            }
        }else{
            if (foodDetailsResponse?.data != null) {
                val datas = foodDetailsResponse?.data
                dishLists.add(datas)
                snapDishLocalListModel = SnapDishLocalListModel(dishLists)
            }
        }

//        view.findViewById<ImageView>(R.id.ivDatePicker).setOnClickListener {
//            // Open Date Picker
//            showDatePicker()
//        }

//        view.findViewById<ImageView>(R.id.ivMealDropdown).setOnClickListener {
//            // Open Meal Selection Dialog
//            showMealSelection()
//        }

//        view.findViewById<ImageView>(R.id.ivDecrease).setOnClickListener {
//            if (quantity > 1) {
//                quantity--
//                tvQuantity.text = quantity.toString()
//            }
//        }

        layoutMacroTitle.setOnClickListener {
          if (macroItemRecyclerView.isVisible){
              macroItemRecyclerView.visibility = View.GONE
              icMacroUP.setImageResource(R.drawable.ic_down)
              view.findViewById<View>(R.id.view_macro).visibility = View.GONE
          }else{
              macroItemRecyclerView.visibility = View.VISIBLE
              icMacroUP.setImageResource(R.drawable.ic_up)
              view.findViewById<View>(R.id.view_macro).visibility = View.VISIBLE
          }
        }

        layoutMicroTitle.setOnClickListener {
            if (microItemRecyclerView.isVisible){
                microItemRecyclerView.visibility = View.GONE
                microUP.setImageResource(R.drawable.ic_down)
                view.findViewById<View>(R.id.view_micro).visibility = View.GONE
            }else{
                microItemRecyclerView.visibility = View.VISIBLE
                microUP.setImageResource(R.drawable.ic_up)
                view.findViewById<View>(R.id.view_micro).visibility = View.VISIBLE
            }
        }

//        view.findViewById<ImageView>(R.id.ivIncrease).setOnClickListener {
//            quantity++
//            tvQuantity.setText( quantity.toString())
//        }

        macroItemRecyclerView.layoutManager = GridLayoutManager(context, 4)
        macroItemRecyclerView.adapter = macroNutrientsAdapter
        microItemRecyclerView.layoutManager = GridLayoutManager(context, 4)
        microItemRecyclerView.adapter = microNutrientsAdapter

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (searchType.contentEquals("EatRight")){
                    val fragment = HomeBottomTabFragment()
                    val args = Bundle()
                    fragment.arguments = args
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, fragment, "landing")
                        addToBackStack("landing")
                        commit()
                    }
                }else{
                    val fragment = SearchDishFragment()
                    val args = Bundle()
                    fragment.arguments = args
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, fragment, "landing")
                        addToBackStack("landing")
                        commit()
                    }
                }
            }
        })

        if (foodDetailsResponse?.data != null){
            setDishData(foodDetailsResponse)
            onMacroNutrientsList(foodDetailsResponse.data)
            onMicroNutrientsList(foodDetailsResponse.data)
            // onFrequentlyLoggedItemRefresh(foodDetailsResponse.data)
        }

        addToTheMealLayout.setOnClickListener {

            if (searchType.contentEquals("SearchDish")){
                if (foodDetailsResponse?.data != null){
                    val foodData = foodDetailsResponse.data
                    val snapRecipeData = SnapRecipeData(
                        id = foodData.id,
                        recipe_name = foodData.recipe_name,
                        ingredients = foodData.ingredients,
                        instructions = foodData.instructions,
                        author = foodData.author,
                        total_time = foodData.total_time,
                        servings = foodData.servings,
                        course = foodData.course,
                        tags = foodData.tags,
                        cuisine = foodData.cuisine,
                        photo_url = foodData.photo_url,
                        serving_weight = foodData.serving_weight,
                        calories = foodData.calories,
                        carbs = foodData.carbs,
                        sugar = foodData.sugar,
                        fiber = foodData.fiber,
                        protein = foodData.protein,
                        fat = foodData.fat,
                        saturated_fat = foodData.saturated_fat,
                        trans_fat = foodData.trans_fat,
                        cholesterol = foodData.cholesterol,
                        sodium = foodData.sodium,
                        potassium = foodData.potassium,
                        recipe_id = foodData.recipe_id,
                        mealType = foodData.mealType,
                        mealQuantity = tvQuantity.text.toString().toDouble(),
                        cookingTime = foodData.cookingTime,
                        calcium = foodData.calcium,
                        iron = foodData.iron,
                        vitaminD = foodData.vitaminD,
                        unit = foodData.unit,
                        isConsumed = foodData.isConsumed,
                        isAteSomethingElse = foodData.isAteSomethingElse,
                        isSkipped = foodData.isSkipped,
                        isSwapped = foodData.isSwapped,
                        isRepeat = foodData.isRepeat,
                        isFavourite = foodData.isFavourite,
                        notes = foodData.notes,
                        b12 = foodData.b12,
                        folate = foodData.folate,
                        vitaminC = foodData.vitaminC,
                        vitaminA = foodData.vitaminA,
                        vitaminK = foodData.vitaminK,
                        magnesium = foodData.magnesium,
                        zinc = foodData.zinc,
                        omega3 = foodData.omega3,
                        phosphorus = foodData.phosphorus
                    )
                    dishLists.add(snapRecipeData)
                    snapDishLocalListModel = SnapDishLocalListModel(dishLists)
                }
                Toast.makeText(activity, "Added To Meal", Toast.LENGTH_SHORT).show()
                val fragment = MealScanResultFragment()
                    val args = Bundle()
                    args.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
                    fragment.arguments = args
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, fragment, "mealLog")
                        addToBackStack("mealLog")
                        commit()
                    }
            }else{
                    if (snapDishLocalListModel != null) {
                        if (snapDishLocalListModel.data.size > 0) {
                            for (item in snapDishLocalListModel.data) {
                                if (item.recipe_name.contentEquals(snapRecipeName)) {
                               //     snapDishLocalListModel.data.remove(item)
                                    val snapRecipesListScan : ArrayList<SnapRecipeData> = ArrayList()
                                    val foodData = item
                                    val snapRecipeData = SnapRecipeData(
                                        id = foodData.id,
                                        recipe_name = foodData.recipe_name,
                                        ingredients = foodData.ingredients,
                                        instructions = foodData.instructions,
                                        author = foodData.author,
                                        total_time = foodData.total_time,
                                        servings = foodData.servings,
                                        course = foodData.course,
                                        tags = foodData.tags,
                                        cuisine = foodData.cuisine,
                                        photo_url = foodData.photo_url,
                                        serving_weight = foodData.serving_weight,
                                        calories = foodData.calories,
                                        carbs = foodData.carbs,
                                        sugar = foodData.sugar,
                                        fiber = foodData.fiber,
                                        protein = foodData.protein,
                                        fat = foodData.fat,
                                        saturated_fat = foodData.saturated_fat,
                                        trans_fat = foodData.trans_fat,
                                        cholesterol = foodData.cholesterol,
                                        sodium = foodData.sodium,
                                        potassium = foodData.potassium,
                                        recipe_id = foodData.recipe_id,
                                        mealType = foodData.mealType,
                                        mealQuantity = tvQuantity.text.toString().toDouble(),
                                        cookingTime = foodData.cookingTime,
                                        calcium = foodData.calcium,
                                        iron = foodData.iron,
                                        vitaminD = foodData.vitaminD,
                                        unit = foodData.unit,
                                        isConsumed = foodData.isConsumed,
                                        isAteSomethingElse = foodData.isAteSomethingElse,
                                        isSkipped = foodData.isSkipped,
                                        isSwapped = foodData.isSwapped,
                                        isRepeat = foodData.isRepeat,
                                        isFavourite = foodData.isFavourite,
                                        notes = foodData.notes,
                                        b12 = foodData.b12,
                                        folate = foodData.folate,
                                        vitaminC = foodData.vitaminC,
                                        vitaminA = foodData.vitaminA,
                                        vitaminK = foodData.vitaminK,
                                        magnesium = foodData.magnesium,
                                        zinc = foodData.zinc,
                                        omega3 = foodData.omega3,
                                        phosphorus = foodData.phosphorus
                                    )
                                    dishLists.add(snapRecipeData)
                                    snapDishLocalListModel = SnapDishLocalListModel(dishLists)
                                    Toast.makeText(activity, "Changes Save", Toast.LENGTH_SHORT).show()
                                    val fragment = MealScanResultFragment()
                                    val args = Bundle()
                                    args.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
                                    fragment.arguments = args
                                    requireActivity().supportFragmentManager.beginTransaction().apply {
                                        replace(R.id.flFragment, fragment, "mealLog")
                                        addToBackStack("mealLog")
                                        commit()
                                    }
                                    break
                                }
                            }
                        }
                }
            }
//            if (foodDetailsResponse?.data != null){
//                createMeal(foodDetailsResponse.data)
//            }

//            val fragment = HomeBottomTabFragment()
//            val args = Bundle()
//            fragment.arguments = args
//            requireActivity().supportFragmentManager.beginTransaction().apply {
//                replace(R.id.flFragment, fragment, "mealLog")
//                addToBackStack("mealLog")
//                commit()
//            }
        }
        onFrequentlyLoggedItemRefresh()
    }

    private fun setDishData(foodDetailsResponse: RecipeResponse) {
        if (searchType.contentEquals("SearchDish")){
            addToTheMealTV.text = "Add To The Meal"
        }else{
            addToTheMealTV.text = "Save Changes"
            tvMealName.text = foodDetailsResponse.data.recipe_name
            Glide.with(this)
                .load(foodDetailsResponse.data.photo_url)
                .placeholder(R.drawable.ic_breakfast)
                .error(R.drawable.ic_breakfast)
                .into(imgFood)
        }
    }

    private fun onMacroNutrientsList(mealDetails : SnapRecipeData) {

        val calories_kcal : String = mealDetails.calories.toInt().toString()?: "NA"
        val protein_g : String = mealDetails.protein.toInt().toString()?: "NA"
        val carb_g : String = mealDetails.carbs.toInt().toString()?: "NA"
        val fat_g : String = mealDetails.fat.toInt().toString()?: "NA"

        val mealLogs = listOf(
            MacroNutrientsModel(calories_kcal, "kcal", "Calorie", R.drawable.ic_cal),
            MacroNutrientsModel(protein_g, "g", "Protein", R.drawable.ic_cabs),
            MacroNutrientsModel(carb_g, "g", "Carbs", R.drawable.ic_protein),
            MacroNutrientsModel(fat_g, "g", "Fats", R.drawable.ic_fats),
        )

        val valueLists : ArrayList<MacroNutrientsModel> = ArrayList()
        valueLists.addAll(mealLogs as Collection<MacroNutrientsModel>)
        val mealLogDateData: MacroNutrientsModel? = null
        macroNutrientsAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onMicroNutrientsList(mealDetails : SnapRecipeData) {

        val transFat = if (mealDetails.trans_fat != null){
            mealDetails.trans_fat.toInt().toString()
        }else{
            "0"
        }

        val cholesterol = if (mealDetails.cholesterol != null){
            mealDetails.cholesterol.toInt().toString()
        }else{
            "0"
        }

        val sodium = if (mealDetails.sodium != null){
            mealDetails.sodium.toInt().toString()
        }else{
            "0"
        }

//        val calcium = if (mealDetails.calcium != null){
//            mealDetails.calcium.toInt().toString()
//        }else{
//            "0"
//        }

//        val vitaminD = if (mealDetails.vitaminD != null){
//            mealDetails.vitaminD.toInt().toString()
//        }else{
//            "0"
//        }

//        val iron_mg = if (mealDetails.iron != null){
//            mealDetails.iron.toInt().toString()
//        }else{
//            "0"
//        }
        val fiber_mg = if (mealDetails.fiber != null){
            mealDetails.fiber.toInt().toString()
        }else{
            "0"
        }

        val saturatedFat_mg = if (mealDetails.saturated_fat != null){
            mealDetails.saturated_fat.toInt().toString()
        }else{
            "0"
        }

        val potassium_mg = if (mealDetails.potassium != null){
            mealDetails.potassium.toInt().toString()
        }else{
            "0"
        }

        val sugar_mg = if (mealDetails.sugar != null){
            mealDetails.sugar.toInt().toString()
        }else{
            "0"
        }

        val mealLogs = listOf(
//            MicroNutrientsModel(vitaminD, "mg", "Vitamin D", R.drawable.ic_cal),
//            MicroNutrientsModel(iron_mg, "mg", "Iron", R.drawable.ic_cabs),
            MicroNutrientsModel(transFat, "mg", "Trans Fat", R.drawable.ic_protein),
            MicroNutrientsModel(cholesterol, "mg", "Cholesterol", R.drawable.ic_fats),
            MicroNutrientsModel(potassium_mg, "mg", "Potassium", R.drawable.ic_fats),
            MicroNutrientsModel(sodium, "mg", "Sodium", R.drawable.ic_fats),
  //          MicroNutrientsModel(calcium, "mg", "Calcium", R.drawable.ic_fats),
            MicroNutrientsModel(fiber_mg, "mg", "Fiber", R.drawable.ic_fats),
            MicroNutrientsModel(saturatedFat_mg, "mg", "Saturated Fat", R.drawable.ic_fats),
            MicroNutrientsModel(sugar_mg, "mg", "Sugar", R.drawable.ic_fats)
        )

        val valueLists : ArrayList<MicroNutrientsModel> = ArrayList()
        //  valueLists.addAll(mealLogs as Collection<MicroNutrientsModel>)
        for (item in mealLogs){
            if (item.nutrientsValue != "0"){
                valueLists.add(item)
            }
        }
        val mealLogDateData: MicroNutrientsModel? = null
        microNutrientsAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onFrequentlyLoggedItemRefresh (){

        val meal = listOf(
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", true),
            MyMealModel("Breakfast", "Dal", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Rice", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Roti", "1", "1,157", "8", "308", "17", false)
        )

        val valueLists : ArrayList<MyMealModel> = ArrayList()
        valueLists.addAll(meal as Collection<MyMealModel>)
        val mealLogDateData: MyMealModel? = null
        frequentlyLoggedListAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onFrequentlyLoggedItem(myMealModel: MyMealModel, position: Int, isRefresh: Boolean) {

    }

    private fun onMacroNutrientsItemClick(macroNutrientsModel: MacroNutrientsModel, position: Int, isRefresh: Boolean) {

    }

    private fun onMicroNutrientsItemClick(microNutrientsModel: MicroNutrientsModel, position: Int, isRefresh: Boolean) {

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(), { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth ${getMonthName(month)} $year"
             //   tvSelectedDate.text = date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun getMonthName(month: Int): String {
        return SimpleDateFormat("MMMM", Locale.getDefault()).format(Date(0, month, 0))
    }

    private fun showMealSelection() {
        val meals = arrayOf("Breakfast", "Lunch", "Dinner")
        val builder = AlertDialog.Builder(requireContext())
        builder.setItems(meals) { _, which ->
          //  tvMealType.text = meals[which]
        }
        builder.show()
    }

    private fun getMealList() {
        Utils.showLoader(requireActivity())
         val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
       // val userId = "64763fe2fa0e40d9c0bc8264"
        val startDate = "2025-03-24"
        val call = ApiClient.apiServiceFastApi.getMealList(userId, startDate)
        call.enqueue(object : Callback<MealsResponse> {
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                if (response.isSuccessful) {
                    Utils.dismissLoader(requireActivity())
                    val mealPlanLists = response.body()?.meals ?: emptyList()
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Utils.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                Utils.dismissLoader(requireActivity())
            }
        })
    }

//    private fun createMeal(mealDetails: MealDetails) {
//        Utils.showLoader(requireActivity())
//         val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
//        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
//       // val userId = "64763fe2fa0e40d9c0bc8264"
//        val currentDateTime = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
//        val formattedDate = currentDateTime.format(formatter)
//
//        val mealLogRequest = MealLogRequest(
//            mealId = mealDetails._id,
//            userId = "64763fe2fa0e40d9c0bc8264",
//            meal = mealDetails.name,
//            date = formattedDate,
//            image = mealDetails.image,
//            mealType = mealDetails.mealType,
//            mealQuantity = mealDetails.mealQuantity,
//            unit = mealDetails.unit,
//            isRepeat = mealDetails.isRepeat,
//            isFavourite = mealDetails.isFavourite,
//            isLogged = true
//        )
//        val call = ApiClient.apiServiceFastApi.createLogDish(mealLogRequest)
//        call.enqueue(object : Callback<MealLogResponse> {
//            override fun onResponse(call: Call<MealLogResponse>, response: Response<MealLogResponse>) {
//                if (response.isSuccessful) {
//                    Utils.dismissLoader(requireActivity())
//                    val mealData = response.body()?.message
//                    Toast.makeText(activity, mealData, Toast.LENGTH_SHORT).show()
//                    val fragment = CreateMealFragment()
//                    val args = Bundle()
//                    args.putParcelable("dishLocalListModel", dishLocalListModel)
//                    fragment.arguments = args
//                    requireActivity().supportFragmentManager.beginTransaction().apply {
//                        replace(R.id.flFragment, fragment, "mealLog")
//                        addToBackStack("mealLog")
//                        commit()
//                    }
//                } else {
//                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
//                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
//                    Utils.dismissLoader(requireActivity())
//                }
//            }
//            override fun onFailure(call: Call<MealLogResponse>, t: Throwable) {
//                Log.e("Error", "API call failed: ${t.message}")
//                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
//                Utils.dismissLoader(requireActivity())
//            }
//        })
//    }
}