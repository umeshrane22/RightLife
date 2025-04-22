package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.model.ScanMealNutritionResponse
import com.jetsynthesys.rightlife.ai_package.model.response.SnapRecipeData
import com.jetsynthesys.rightlife.ai_package.ui.eatright.RatingMealBottomSheet
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.SnapMealScanResultAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.MacroNutrientsAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.MicroNutrientsAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.createmeal.SearchDishFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MacroNutrientsModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MicroNutrientsModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.SnapDishLocalListModel
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentMealScanResultsBinding
import com.jetsynthesys.rightlife.newdashboard.HomeDashboardActivity
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MealScanResultFragment: BaseFragment<FragmentMealScanResultsBinding>() {

    private lateinit var macroItemRecyclerView : RecyclerView
    private lateinit var microItemRecyclerView : RecyclerView
    private lateinit var frequentlyLoggedRecyclerView : RecyclerView
    private lateinit var currentPhotoPath : String
    private lateinit var descriptionName : String
    private lateinit var foodNameEdit : EditText
    private lateinit var currentPhotoPathsecound : Uri
    private lateinit var tvFoodName : EditText
    private lateinit var imageFood : ImageView
    private lateinit var tvQuantity: TextView
    private lateinit var tvSelectedDate : TextView
    private lateinit var addToLogLayout : LinearLayoutCompat
    private lateinit var saveMealLayout : LinearLayoutCompat
    private lateinit var layoutMicroTitle : ConstraintLayout
    private lateinit var layoutMacroTitle : ConstraintLayout
    private lateinit var icMacroUP : ImageView
    private lateinit var microUP : ImageView
    private lateinit var addLayout : LinearLayoutCompat
    private lateinit var checkBox: CheckBox
    private lateinit var deleteSnapMealBottomSheet: DeleteSnapMealBottomSheet
    private var snapDishLocalListModel : SnapDishLocalListModel? = null
    private var snapRecipesList : ArrayList<SnapRecipeData> = ArrayList()
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    private var quantity = 1


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMealScanResultsBinding
        get() = FragmentMealScanResultsBinding::inflate

    private val microNutrientsAdapter by lazy { MicroNutrientsAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMicroNutrientsItem) }
    private val mealListAdapter by lazy { SnapMealScanResultAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMenuEditItem, :: onMenuDeleteItem) }

    private val macroNutrientsAdapter by lazy { MacroNutrientsAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMealLogDateItem) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(context)
        macroItemRecyclerView = view.findViewById(R.id.recyclerview_macro_item)
        microItemRecyclerView = view.findViewById(R.id.recyclerview_micro_item)
        frequentlyLoggedRecyclerView = view.findViewById(R.id.recyclerview_frequently_logged_item)
        var btnChange = view.findViewById<TextView>(R.id.change_btn)
        foodNameEdit = view.findViewById(R.id.foodNameEdit)
        imageFood = view.findViewById(R.id.imageFood)
        tvQuantity = view.findViewById(R.id.tvQuantity)
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate)
        addToLogLayout = view.findViewById(R.id.addToLogLayout)
        saveMealLayout = view.findViewById(R.id.saveMealLayout)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        layoutMicroTitle = view.findViewById(R.id.layoutMicroTitle)
        layoutMacroTitle = view.findViewById(R.id.layoutMacroTitle)
        microUP = view.findViewById(R.id.microUP)
        icMacroUP = view.findViewById(R.id.icMacroUP)
        addLayout = view.findViewById(R.id.addLayout)
        checkBox = view.findViewById(R.id.saveMealCheckBox)
        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked))
        val colors = intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.dark_green),   // when checked
            ContextCompat.getColor(requireContext(), R.color.dark_gray)  // when not checked
        )
        val colorStateList = ColorStateList(states, colors)
        checkBox.buttonTintList = colorStateList

        val dishLocalListModels = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("snapDishLocalListModel", SnapDishLocalListModel::class.java)
        } else {
            arguments?.getParcelable("snapDishLocalListModel")
        }

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Added To Log!", Toast.LENGTH_SHORT).show()
            }
        }

        if (dishLocalListModels != null){
            snapDishLocalListModel = dishLocalListModels
            snapRecipesList.addAll(snapDishLocalListModel!!.data)
            onFrequentlyLoggedItemRefresh(snapRecipesList)
            onMicroNutrientsList(snapRecipesList)
            onMacroNutrientsList(snapRecipesList)
        }

        frequentlyLoggedRecyclerView.layoutManager = LinearLayoutManager(context)
        frequentlyLoggedRecyclerView.adapter = mealListAdapter

        macroItemRecyclerView.layoutManager = GridLayoutManager(context, 4)
        macroItemRecyclerView.adapter = macroNutrientsAdapter

        microItemRecyclerView.layoutManager = GridLayoutManager(context, 4)
        microItemRecyclerView.adapter = microNutrientsAdapter

        // Data for Spinner
        val items = arrayOf("Breakfast", "Morning Snack", "Lunch", "Evening Snacks", "Dinner")

        // Create Adapter
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = adapter

        // Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
               // selectedText.text = "Selected: $selectedItem"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
             //  selectedText.text = "No selection"
            }
        }

//        val foodDataResponses = if (Build.VERSION.SDK_INT >= 33) {
//            arguments?.getParcelable("foodDataResponses", NutritionResponse::class.java)
//        } else {
//            arguments?.getParcelable("foodDataResponses")
//        }

        val foodDataResponses = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("foodDataResponses", ScanMealNutritionResponse::class.java)
        } else {
            arguments?.getParcelable("foodDataResponses")
        }

        currentPhotoPath = arguments?.get("ImagePath").toString()
        val imagePathString = arguments?.getString("ImagePathsecound")
         currentPhotoPathsecound = imagePathString?.let { Uri.parse(it) }!!
        //currentPhotoPathsecound = arguments?.get("ImagePathsecound") as Uri

        descriptionName = arguments?.getString("description") .toString()

        view.findViewById<LinearLayoutCompat>(R.id.datePickerLayout).setOnClickListener {
            // Open Date Picker
            showDatePicker()
        }

        view.findViewById<ImageView>(R.id.ivDecrease).setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
            }
        }

            view.findViewById<ImageView>(R.id.ivIncrease).setOnClickListener {
                quantity++
                tvQuantity.text = quantity.toString()
            }

        if (foodDataResponses?.data != null){
            setFoodData(foodDataResponses)

            val snapRecipesListScan : ArrayList<SnapRecipeData> = ArrayList()
            if (foodDataResponses.data.size > 0){

                val items = foodDataResponses.data
                items?.forEach { foodData ->
                    val snapRecipeData = SnapRecipeData(
                        id = "",
                        recipe_name = foodData.name,
                        ingredients = null,
                        instructions = null,
                        author = "",
                        total_time = "",
                        servings = 0,
                        course = "",
                        tags = null,
                        cuisine = "",
                        photo_url = "",
                        serving_weight = 0.0,
                        calories = foodData.selected_portion_nutrition.calories_kcal,
                        carbs = foodData.selected_portion_nutrition.carb_g,
                        sugar = foodData.selected_portion_nutrition.sugar_g,
                        fiber = foodData.selected_portion_nutrition.fiber_g,
                        protein = foodData.selected_portion_nutrition.protein_g,
                        fat = foodData.selected_portion_nutrition.fat_g,
                        saturated_fat = foodData.selected_portion_nutrition.saturated_fats_g,
                        trans_fat = foodData.selected_portion_nutrition.trans_fats_g,
                        cholesterol = foodData.selected_portion_nutrition.cholesterol_mg,
                        sodium = foodData.selected_portion_nutrition.sodium_mg,
                        potassium = foodData.selected_portion_nutrition.potassium_mg,
                        recipe_id = "",
                        mealType = "",
                        mealQuantity = 0.0,
                        cookingTime = "",
                        calcium = foodData.selected_portion_nutrition.calcium_mg,
                        iron = foodData.selected_portion_nutrition.iron_mg,
                        vitaminD = foodData.selected_portion_nutrition.vitamin_d_iu,
                        unit = "",
                        isConsumed = false,
                        isAteSomethingElse = false,
                        isSkipped = false,
                        isSwapped = false,
                        isRepeat = false,
                        isFavourite = false,
                        notes = null,
                        b12 = foodData.selected_portion_nutrition.b12_mcg,
                        folate = foodData.selected_portion_nutrition.folate_mcg,
                        vitaminC = foodData.selected_portion_nutrition.vitamin_c_mg,
                        vitaminA = foodData.selected_portion_nutrition.vitamin_a_mcg,
                        vitaminK = foodData.selected_portion_nutrition.vitamin_k_mcg,
                        magnesium = foodData.selected_portion_nutrition.magnesium_mg,
                        zinc = foodData.selected_portion_nutrition.zinc_mg,
                        omega3 = foodData.selected_portion_nutrition.omega_3_fatty_acids_g,
                        phosphorus = foodData.selected_portion_nutrition.phosphorus_mg
                    )
                    snapRecipesListScan.add(snapRecipeData)
                }
                snapRecipesList = snapRecipesListScan
                onFrequentlyLoggedItemRefresh(snapRecipesList)
                onMicroNutrientsList(snapRecipesList)
                onMacroNutrientsList(snapRecipesList)
                snapDishLocalListModel = SnapDishLocalListModel(snapRecipesList)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(HomeBottomTabFragment(), "LandingFragment")
            }
        })

        btnChange.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = MealSearchFragment()
                val args = Bundle()
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        addLayout.setOnClickListener {
            val fragment = SearchDishFragment()
            val args = Bundle()
            args.putString("searchType", "mealScanResult")
            args.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "landing")
                addToBackStack("landing")
                commit()
            }
        }

        saveMealLayout.setOnClickListener {
            Toast.makeText(context, "Save Meal", Toast.LENGTH_SHORT).show()
            val moduleName = arguments?.getString("ModuleName").toString()
            if (moduleName.contentEquals("EatRight")){
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    val snapMealFragment = HomeBottomTabFragment()
                    val args = Bundle()
                    args.putString("ModuleName", moduleName)
                    snapMealFragment.arguments = args
                    replace(R.id.flFragment, snapMealFragment, "Steps")
                    addToBackStack(null)
                    commit()
                }
            }else{
                startActivity(Intent(context, HomeDashboardActivity::class.java))
                requireActivity().finish()
            }
        }

        addToLogLayout.setOnClickListener {
            Toast.makeText(context, "Added To Log", Toast.LENGTH_SHORT).show()
            val moduleName = arguments?.getString("ModuleName").toString()
            if (moduleName.contentEquals("EatRight")){
                val ratingMealBottomSheet = RatingMealBottomSheet()
                ratingMealBottomSheet.isCancelable = true
                val bundle = Bundle()
                bundle.putBoolean("test",false)
                ratingMealBottomSheet.arguments = bundle
                activity?.supportFragmentManager?.let { ratingMealBottomSheet.show(it, "RatingMealBottomSheet") }
                sharedPreferenceManager.setFirstTimeUserForSnapMealRating(true)
//                requireActivity().supportFragmentManager.beginTransaction().apply {
//                    val snapMealFragment = HomeBottomTabFragment()
//                    val args = Bundle()
//                    args.putString("ModuleName", moduleName)
//                    snapMealFragment.arguments = args
//                    replace(R.id.flFragment, snapMealFragment, "Steps")
//                    addToBackStack(null)
//                    commit()
//                }
            }else{
                startActivity(Intent(context, HomeDashboardActivity::class.java))
                requireActivity().finish()
            }
        }

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
    //    CommonAPICall.updateChecklistStatus(requireContext(), "meal_snap", AppConstants.CHECKLIST_COMPLETED)
    }

    private fun onMicroNutrientsList (nutrition: ArrayList<SnapRecipeData>){

        var totalVitaminD = 0
        var totalB12 = 0
        var totalFolate = 0
        var totalVitaminC = 0
        var totalVitaminA = 0
        var totalVitaminK = 0
        var totalIron = 0
        var totalCalcium = 0
        var totalMagnesium = 0
        var totalZinc = 0
        var totalOmega3 = 0
        var totalSodium = 0
        var totalCholesterol = 0
        var totalSugar = 0
        var totalPhosphorus = 0
        var totalPotassium = 0

        nutrition.forEach { item ->
            totalVitaminD += (item.vitaminD ?: 0.0).toInt()
            totalB12 += (item.b12 ?: 0.0).toInt()
            totalFolate += (item.folate ?: 0.0).toInt()
            totalVitaminC += (item.vitaminC ?: 0.0).toInt()
            totalVitaminA += (item.vitaminA ?: 0.0).toInt()
            totalVitaminK += (item.vitaminK ?: 0.0).toInt()
            totalIron += (item.iron ?: 0.0).toInt()
            totalCalcium += (item.calcium ?: 0.0).toInt()
            totalMagnesium += (item.magnesium ?: 0.0).toInt()
            totalZinc += (item.zinc ?: 0.0).toInt()
            totalOmega3 += (item.omega3 ?: 0.0).toInt()
            totalSodium += (item.sodium ?: 0.0).toInt()
            totalCholesterol += (item.cholesterol ?: 0.0).toInt()
            totalSugar += (item.sugar ?: 0.0).toInt()
            totalPhosphorus += (item.phosphorus ?: 0.0).toInt()
            totalPotassium += (item.potassium ?: 0.0).toInt()
        }


        val vitaminD = if (totalVitaminD != null){
            totalVitaminD.toInt().toString()
        }else{
            "0"
        }

        val b12_mcg = if (totalB12 != null){
            totalB12.toInt().toString()
        }else{
            "0"
        }

        val folate = if (totalFolate != null){
            totalFolate.toInt().toString()
        }else{
            "0"
        }

        val vitaminC = if (totalVitaminC != null){
            totalVitaminC.toInt().toString()
        }else{
            "0"
        }

        val vitaminA = if (totalVitaminA != null){
            totalVitaminA.toInt().toString()
        }else{
            "0"
        }

        val vitaminK = if (totalVitaminK != null){
            totalVitaminK.toInt().toString()
        }else{
            "0"
        }

        val iron_mg = if (totalIron != null){
            totalIron.toInt().toString()
        }else{
            "0"
        }

        val calcium = if (totalCalcium != null){
            totalCalcium.toInt().toString()
        }else{
            "0"
        }

        val magnesium_mg = if (totalMagnesium != null){
            totalMagnesium.toInt().toString()
        }else{
            "0"
        }

        val zinc_mg = if (totalZinc != null){
            totalZinc.toInt().toString()
        }else{
            "0"
        }

        val omega3 = if (totalOmega3 != null){
            totalOmega3.toInt().toString()
        }else{
            "0"
        }

        val sodium = if (totalSodium != null){
            totalSodium.toInt().toString()
        }else{
            "0"
        }

        val cholesterol = if (totalCholesterol != null){
            totalCholesterol.toInt().toString()
        }else{
            "0"
        }

        val sugar = if (totalSugar != null){
            totalSugar.toInt().toString()
        }else{
            "0"
        }

        val phosphorus_mg = if (totalPhosphorus != null){
            totalPhosphorus.toInt().toString()
        }else{
            "0"
        }
        val potassium_mg = if (totalPotassium != null){
            totalPotassium.toInt().toString()
        }else{
            "0"
        }

        val mealLogs = listOf(
//            MicroNutrientsModel(phosphorus_mg, "mg", "Phasphorus", R.drawable.ic_fats),
//            MicroNutrientsModel(potassium_mg, "mg", "Potassium", R.drawable.ic_fats),
            MicroNutrientsModel(vitaminD, "μg", "Vitamin D", R.drawable.ic_fats),
            MicroNutrientsModel(b12_mcg, "μg", "Vitamin B12", R.drawable.ic_fats),
            MicroNutrientsModel(folate, "μg", "Folate", R.drawable.ic_fats),
            MicroNutrientsModel(vitaminC, "mg", "Vitamin C", R.drawable.ic_fats),
            MicroNutrientsModel(vitaminA, "IU", "Vitamin A", R.drawable.ic_fats),
            MicroNutrientsModel(vitaminK, "μg", "Vitamin K", R.drawable.ic_fats),
            MicroNutrientsModel(iron_mg, "mg", "Iron", R.drawable.ic_fats),
            MicroNutrientsModel(calcium, "mg", "Calcium", R.drawable.ic_fats),
            MicroNutrientsModel(magnesium_mg, "mg", "Magnesium", R.drawable.ic_fats),
            MicroNutrientsModel(zinc_mg, "mg", "Zinc", R.drawable.ic_fats),
            MicroNutrientsModel(omega3, "mg", "Omega-3", R.drawable.ic_fats),
            MicroNutrientsModel(sodium, "mg", "Sodium", R.drawable.ic_fats),
            MicroNutrientsModel(cholesterol, "mg", "Cholesterol", R.drawable.ic_fats),
            MicroNutrientsModel(sugar, "mg", "Sugar", R.drawable.ic_fats)
        )
        val valueLists : ArrayList<MicroNutrientsModel> = ArrayList()
       // valueLists.addAll(mealLogs as Collection<MicroNutrientsModel>)
        for (item in mealLogs){
            if (item.nutrientsValue != "0"){
                valueLists.add(item)
            }
        }
        val mealLogDateData: MicroNutrientsModel? = null
        microNutrientsAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }
    private fun onMicroNutrientsItem(microNutrientsModel: MicroNutrientsModel, position: Int, isRefresh: Boolean) {

//        val microNutrients = listOf(
//            MicroNutrientsModel("NA", "mg", "Vitamin B", R.drawable.ic_cal),
//            MicroNutrientsModel("NA", "mg", "Iron", R.drawable.ic_cabs),
//            MicroNutrientsModel("NA", "mg", "Magnesium", R.drawable.ic_protein),
//            MicroNutrientsModel("NA", "mg", "Phasphorus", R.drawable.ic_fats),
//            MicroNutrientsModel("NA", "mg", "Potassium", R.drawable.ic_fats),
//            MicroNutrientsModel("NA", "mg", "Zinc", R.drawable.ic_fats)
//        )
//        val valueLists : ArrayList<MicroNutrientsModel> = ArrayList()
//        valueLists.addAll(microNutrients as Collection<MicroNutrientsModel>)
//        microNutientsAdapter.addAll(valueLists, position, microNutrientsModel, isRefresh)
    }
    private fun onMealLogDateItem(mealLogDateModel: MacroNutrientsModel, position: Int, isRefresh: Boolean) {
//        val mealLogs = listOf(
//            MacroNutrientsModel("1285", "kcal", "calorie", R.drawable.ic_cal),
//            MacroNutrientsModel("11", "g", "protien", R.drawable.ic_cabs),
//            MacroNutrientsModel("338", "g", "carbs", R.drawable.ic_protein),
//            MacroNutrientsModel("25", "g", "fats", R.drawable.ic_fats),
//        )
//        val valueLists : ArrayList<MacroNutrientsModel> = ArrayList()
//        valueLists.addAll(mealLogs as Collection<MacroNutrientsModel>)
//        macroNutientsAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
    }

    private fun onMacroNutrientsList(nutritionList: ArrayList<SnapRecipeData>) {

        val totalCalories = nutritionList.sumOf { it.calories ?: 0.0 }
        val totalProtein = nutritionList.sumOf { it.protein ?: 0.0 }
        val totalCarbs = nutritionList.sumOf { it.carbs ?: 0.0 }
        val totalFat = nutritionList.sumOf { it.fat ?: 0.0 }


        val calories_kcal : String = totalCalories.toInt().toString()?: "NA"
        val protein_g : String = totalProtein.toInt().toString()?: "NA"
        val carb_g : String = totalCarbs.toInt().toString()?: "NA"
        val fat_g : String = totalFat.toInt().toString()?: "NA"

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

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    private fun onFrequentlyLoggedItemRefresh(recipes: List<SnapRecipeData>) {
        if (recipes.size > 0){
            frequentlyLoggedRecyclerView.visibility = View.VISIBLE
            //   layoutNoMeals.visibility = View.GONE
        }else{
            //    layoutNoMeals.visibility = View.VISIBLE
            frequentlyLoggedRecyclerView.visibility = View.GONE
        }
        val valueLists : ArrayList<SnapRecipeData> = ArrayList()
        valueLists.addAll(recipes as Collection<SnapRecipeData>)
        val mealLogDateData: SnapRecipeData? = null
        mealListAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onMenuEditItem(snapRecipeData: SnapRecipeData, position: Int, isRefresh: Boolean) {

        requireActivity().supportFragmentManager.beginTransaction().apply {
            val snapMealFragment = SnapDishFragment()
            val args = Bundle()
            args.putString("searchType", "MealScanResult")
            args.putString("snapRecipeName", snapRecipeData.recipe_name)
            args.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
            snapMealFragment.arguments = args
            replace(R.id.flFragment, snapMealFragment, "Steps")
            addToBackStack(null)
            commit()
        }
    }

    private fun onMenuDeleteItem(snapRecipeData: SnapRecipeData, position: Int, isRefresh: Boolean) {
        deleteSnapMealBottomSheet = DeleteSnapMealBottomSheet()
        deleteSnapMealBottomSheet.isCancelable = true
        val bundle = Bundle()
        bundle.putBoolean("test",false)
        bundle.putString("snapRecipeName", snapRecipeData.recipe_name)
        bundle.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
        deleteSnapMealBottomSheet.arguments = bundle
        activity?.supportFragmentManager?.let { deleteSnapMealBottomSheet.show(it, "DeleteMealBottomSheet") }
    }

    private fun setFoodData(nutritionResponse: ScanMealNutritionResponse) {
        if (nutritionResponse.data != null) {
            try {
                val path = getRealPathFromURI(requireContext(), currentPhotoPathsecound)
                if (path != null) {
                    val scaledBitmap = decodeAndScaleBitmap(path, 1080, 1080)
                    if (scaledBitmap != null) {
                        val rotatedBitmap = rotateImageIfRequired(requireContext(), scaledBitmap, currentPhotoPathsecound)
                        imageFood.visibility = View.VISIBLE
                        imageFood.setImageBitmap(rotatedBitmap)
                    } else {
                        Toast.makeText(requireContext(), "Failed to decode image", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("ImageCapture", "File does not exist at $currentPhotoPath")
                }
            } catch (e: Exception) {
                Log.e("ImageLoad", "Error loading image from file path: $currentPhotoPath", e)
            }

            // Set food name
            if (nutritionResponse.data.size > 0){
                val capitalized = nutritionResponse.data.get(0).name.replaceFirstChar { it.uppercase() }
                foodNameEdit.setText(capitalized)
            }
        }
    }

    private fun decodeAndScaleBitmap(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(filePath, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(filePath, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun rotateImageIfRequired(context: Context, bitmap: Bitmap, imageUri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val exifInterface = inputStream?.let { ExifInterface(it) }
        val orientation = exifInterface?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        inputStream?.close()

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            else -> bitmap
        }
    }
    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        var filePath: String? = null

        // Try getting path from content resolver
        if (uri.scheme == "content") {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    filePath = it.getString(columnIndex)
                }
            }
        } else if (uri.scheme == "file") {
            filePath = uri.path
        }

        return filePath
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(), { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth ${getMonthName(month)} $year"
                tvSelectedDate.text = date
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

    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_item_snap_meal, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
//        dialogView.findViewById<TextView>(R.id.dialogTitle).text = item.title
//        dialogView.findViewById<TextView>(R.id.dialogDescription).text = item.description
//        dialogView.findViewById<Button>(R.id.dialogOkBtn).setOnClickListener {
//            dialog.dismiss()
//        }
        dialog.show()
    }
}