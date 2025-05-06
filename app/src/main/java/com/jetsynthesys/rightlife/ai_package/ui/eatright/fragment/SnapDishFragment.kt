package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.net.Uri
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
import com.jetsynthesys.rightlife.ai_package.model.MealsResponse
import com.jetsynthesys.rightlife.ai_package.model.response.Macros
import com.jetsynthesys.rightlife.ai_package.model.response.Micros
import com.jetsynthesys.rightlife.ai_package.model.response.Nutrients
import com.jetsynthesys.rightlife.ai_package.model.response.RecipeResponse
import com.jetsynthesys.rightlife.ai_package.model.response.SearchResultItem
import com.jetsynthesys.rightlife.ai_package.model.response.SnapRecipeData
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.createmeal.SearchDishFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.SnapDishLocalListModel
import com.jetsynthesys.rightlife.ai_package.utils.LoaderUtil
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    private lateinit var quantityEdit: EditText
    private lateinit var ivEdit : ImageView
    private var quantity = 1
    private lateinit var tvMeasure :TextView
    private var dishLists : ArrayList<SearchResultItem> = ArrayList()
    private lateinit var snapDishLocalListModel : SnapDishLocalListModel
    private var currentPhotoPathsecound : Uri? = null
    private var mealId : String = ""
    private var mealName : String = ""

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDishBinding
        get() = FragmentDishBinding::inflate

    private val macroNutrientsAdapter by lazy { MacroNutrientsAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMacroNutrientsItemClick) }
    private val microNutrientsAdapter by lazy { MicroNutrientsAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMicroNutrientsItemClick) }

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
        quantityEdit = view.findViewById(R.id.quantityEdit)
        tvMeasure = view.findViewById(R.id.tvMeasure)
        addToTheMealTV = view.findViewById(R.id.tv_addToTheMeal)
        tvMealName = view.findViewById(R.id.tvMealName)
        imgFood = view.findViewById(R.id.imgFood)
        layoutMicroTitle = view.findViewById(R.id.layoutMicroTitle)
        layoutMacroTitle = view.findViewById(R.id.layoutMacroTitle)
        microUP = view.findViewById(R.id.microUP)
        icMacroUP = view.findViewById(R.id.icMacroUP)
        ivEdit = view.findViewById(R.id.ivEdit)

        mealId = arguments?.getString("mealId").toString()
        mealName = arguments?.getString("mealName").toString()
        val imagePathString = arguments?.getString("ImagePathsecound")
        if (imagePathString != null){
            currentPhotoPathsecound = imagePathString?.let { Uri.parse(it) }!!
        }else{
            currentPhotoPathsecound = null
        }
        searchType = arguments?.getString("searchType").toString()
        val snapRecipeName = arguments?.getString("snapRecipeName").toString()
        val foodDetailsResponse = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("searchResultItem", SearchResultItem::class.java)
        } else {
            arguments?.getParcelable("searchResultItem")
        }

        val snapDishLocalListModels = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("snapDishLocalListModel", SnapDishLocalListModel::class.java)
        } else {
            arguments?.getParcelable("snapDishLocalListModel")
        }

        if (foodDetailsResponse != null){
            if (snapDishLocalListModels != null) {
                snapDishLocalListModel = snapDishLocalListModels
                if (snapDishLocalListModel?.data != null){
                    if (snapDishLocalListModel.data.size > 0){
                        dishLists.addAll(snapDishLocalListModel.data)
                    }
                }
            }
        }else{
            if (snapDishLocalListModels != null) {
                snapDishLocalListModel = snapDishLocalListModels
                if (snapDishLocalListModel?.data != null){
                    if (snapDishLocalListModel.data.size > 0){
                        dishLists.addAll(snapDishLocalListModel.data)
                    }
                }
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
                }else if (searchType.contentEquals("searchScanResult")){
                    val fragment = SearchDishFragment()
                    val args = Bundle()
                    fragment.arguments = args
                    args.putString("searchType", searchType)
                    args.putString("ImagePathsecound", currentPhotoPathsecound.toString())
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, fragment, "landing")
                        addToBackStack("landing")
                        commit()
                    }
                }else{
                    val fragment = MealScanResultFragment()
                    val args = Bundle()
                    fragment.arguments = args
                    args.putString("searchType", searchType)
                    args.putString("ImagePathsecound", currentPhotoPathsecound.toString())
                    args.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, fragment, "landing")
                        addToBackStack("landing")
                        commit()
                    }
                }
            }
        })

        ivEdit.setOnClickListener {
          val value =  quantityEdit.text.toString().toInt()
            if (foodDetailsResponse != null){
                setDishData(foodDetailsResponse)
                onMacroNutrientsList(foodDetailsResponse, value)
                onMicroNutrientsList(foodDetailsResponse, value)
                // onFrequentlyLoggedItemRefresh(foodDetailsResponse.data)
            }else{
                if (snapDishLocalListModel != null){
                    for (item in snapDishLocalListModel.data) {
                        if (item.name.contentEquals(snapRecipeName)) {
                            setDishData(item)
                            onMacroNutrientsList(item, value)
                            onMicroNutrientsList(item, value)
                            break
                        }
                    }
                }
            }
        }

        if (foodDetailsResponse != null){
            setDishData(foodDetailsResponse)
            onMacroNutrientsList(foodDetailsResponse, 1)
            onMicroNutrientsList(foodDetailsResponse, 1)
            // onFrequentlyLoggedItemRefresh(foodDetailsResponse.data)
        }else{
            if (snapDishLocalListModel != null){
                for (item in snapDishLocalListModel.data) {
                    if (item.name.contentEquals(snapRecipeName)) {
                        setDishData(item)
                        onMacroNutrientsList(item, 1)
                        onMicroNutrientsList(item, 1)
                        break
                    }
                }
            }
        }

        addToTheMealLayout.setOnClickListener {

            if (searchType.contentEquals("SearchDish")){
                if (foodDetailsResponse != null){
                    val foodData = foodDetailsResponse
                    val macrosData = Macros(
                        Calories = foodData.nutrients.macros.Calories,
                        Carbs = foodData.nutrients.macros.Carbs,
                        Fats = foodData.nutrients.macros.Fats,
                        Protein = foodData.nutrients.macros.Protein)
                    val microsData =  Micros(
                        Cholesterol = foodData.nutrients.micros.Cholesterol,
                        Vitamin_A = foodData.nutrients.micros.Vitamin_A,
                        Vitamin_C = foodData.nutrients.micros.Vitamin_A,
                        Vitamin_K = foodData.nutrients.micros.Vitamin_K,
                        Vitamin_D = foodData.nutrients.micros.Vitamin_D,
                        Folate = foodData.nutrients.micros.Folate,
                        Iron = foodData.nutrients.micros.Iron,
                        Calcium = foodData.nutrients.micros.Calcium,
                        Magnesium = foodData.nutrients.micros.Magnesium,
                        Potassium = foodData.nutrients.micros.Potassium,
                        Fiber = foodData.nutrients.micros.Fiber,
                        Zinc = foodData.nutrients.micros.Zinc,
                        Sodium = foodData.nutrients.micros.Sodium,
                        Sugar = foodData.nutrients.micros.Sugar,
                        b12_mcg = foodData.nutrients.micros.b12_mcg,
                        b1_mg = foodData.nutrients.micros.b1_mg,
                        b2_mg = foodData.nutrients.micros.b2_mg,
                        b5_mg = foodData.nutrients.micros.b5_mg,
                        b3_mg = foodData.nutrients.micros.b3_mg,
                        b6_mg = foodData.nutrients.micros.b6_mg,
                        vitamin_e_mg = foodData.nutrients.micros.vitamin_e_mg,
                        omega_3_fatty_acids_g = foodData.nutrients.micros.omega_3_fatty_acids_g,
                        omega_6_fatty_acids_g = foodData.nutrients.micros.omega_6_fatty_acids_g,
                        copper_mg = foodData.nutrients.micros.copper_mg,
                        phosphorus_mg = foodData.nutrients.micros.phosphorus_mg,
                        saturated_fats_g = foodData.nutrients.micros.saturated_fats_g,
                        selenium_mcg = foodData.nutrients.micros.selenium_mcg,
                        trans_fats_g = foodData.nutrients.micros.trans_fats_g,
                        polyunsaturated_g = foodData.nutrients.micros.polyunsaturated_g,
                        is_beverage = foodData.nutrients.micros.is_beverage,
                        mass_g = foodData.nutrients.micros.mass_g,
                        monounsaturated_g = foodData.nutrients.micros.monounsaturated_g,
                        percent_fruit = foodData.nutrients.micros.percent_fruit,
                        percent_vegetable = foodData.nutrients.micros.percent_vegetable,
                        percent_legume_or_nuts = foodData.nutrients.micros.percent_legume_or_nuts,
                        source_urls = foodData.nutrients.micros.source_urls
                    )
                    val nutrientsData =  Nutrients(
                        macros = macrosData,
                        micros =  microsData)
                    val snapRecipeData = SearchResultItem(
                        id = foodData.id,
                        name = foodData.name,
                        category = foodData.category,
                        photo_url = foodData.photo_url,
                        servings = foodData.servings,
                        cooking_time_in_seconds = foodData.cooking_time_in_seconds,
                        calories = foodData.calories,
                        nutrients = nutrientsData,
                        source = foodData.source,
                        unit = foodData.unit,
                        mealQuantity = quantityEdit.text.toString().toDouble()
                    )
                    dishLists.add(snapRecipeData)
                    snapDishLocalListModel = SnapDishLocalListModel(dishLists)
                }
                Toast.makeText(activity, "Added To Meal", Toast.LENGTH_SHORT).show()
                val fragment = MealScanResultFragment()
                    val args = Bundle()
                args.putString("mealId", mealId)
                args.putString("mealName", mealName)
                    args.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
                    args.putString("ImagePathsecound", currentPhotoPathsecound.toString())
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
                                if (item.name.contentEquals(snapRecipeName)) {
                               //     snapDishLocalListModel.data.remove(item)
                                    val snapRecipesListScan : ArrayList<SnapRecipeData> = ArrayList()
                                    val foodData = item
                                    val index = snapDishLocalListModel.data.indexOfFirst { it.name == snapRecipeName }
                                    val macrosData = Macros(
                                        Calories = foodData.nutrients.macros.Calories,
                                        Carbs = foodData.nutrients.macros.Carbs,
                                        Fats = foodData.nutrients.macros.Fats,
                                        Protein = foodData.nutrients.macros.Protein)
                                    val microsData =  Micros(
                                        Cholesterol = foodData.nutrients.micros.Cholesterol,
                                        Vitamin_A = foodData.nutrients.micros.Vitamin_A,
                                        Vitamin_C = foodData.nutrients.micros.Vitamin_A,
                                        Vitamin_K = foodData.nutrients.micros.Vitamin_K,
                                        Vitamin_D = foodData.nutrients.micros.Vitamin_D,
                                        Folate = foodData.nutrients.micros.Folate,
                                        Iron = foodData.nutrients.micros.Iron,
                                        Calcium = foodData.nutrients.micros.Calcium,
                                        Magnesium = foodData.nutrients.micros.Magnesium,
                                        Potassium = foodData.nutrients.micros.Potassium,
                                        Fiber = foodData.nutrients.micros.Fiber,
                                        Zinc = foodData.nutrients.micros.Zinc,
                                        Sodium = foodData.nutrients.micros.Sodium,
                                        Sugar = foodData.nutrients.micros.Sugar,
                                        b12_mcg = foodData.nutrients.micros.b12_mcg,
                                        b1_mg = foodData.nutrients.micros.b1_mg,
                                        b2_mg = foodData.nutrients.micros.b2_mg,
                                        b5_mg = foodData.nutrients.micros.b5_mg,
                                        b3_mg = foodData.nutrients.micros.b3_mg,
                                        b6_mg = foodData.nutrients.micros.b6_mg,
                                        vitamin_e_mg = foodData.nutrients.micros.vitamin_e_mg,
                                        omega_3_fatty_acids_g = foodData.nutrients.micros.omega_3_fatty_acids_g,
                                        omega_6_fatty_acids_g = foodData.nutrients.micros.omega_6_fatty_acids_g,
                                        copper_mg = foodData.nutrients.micros.copper_mg,
                                        phosphorus_mg = foodData.nutrients.micros.phosphorus_mg,
                                        saturated_fats_g = foodData.nutrients.micros.saturated_fats_g,
                                        selenium_mcg = foodData.nutrients.micros.selenium_mcg,
                                        trans_fats_g = foodData.nutrients.micros.trans_fats_g,
                                        polyunsaturated_g = foodData.nutrients.micros.polyunsaturated_g,
                                        is_beverage = foodData.nutrients.micros.is_beverage,
                                        mass_g = foodData.nutrients.micros.mass_g,
                                        monounsaturated_g = foodData.nutrients.micros.monounsaturated_g,
                                        percent_fruit = foodData.nutrients.micros.percent_fruit,
                                        percent_vegetable = foodData.nutrients.micros.percent_vegetable,
                                        percent_legume_or_nuts = foodData.nutrients.micros.percent_legume_or_nuts,
                                        source_urls = foodData.nutrients.micros.source_urls
                                    )
                                    val nutrientsData =  Nutrients(
                                        macros = macrosData,
                                        micros =  microsData)
                                    val snapRecipeData = SearchResultItem(
                                        id = foodData.id,
                                        name = foodData.name,
                                        category = foodData.category,
                                        photo_url = foodData.photo_url,
                                        servings = foodData.servings,
                                        cooking_time_in_seconds = foodData.cooking_time_in_seconds,
                                        calories = foodData.calories,
                                        nutrients = nutrientsData,
                                        source = foodData.source,
                                        unit = foodData.unit,
                                        mealQuantity = quantityEdit.text.toString().toDouble()
                                    )
                                    if (index != -1) {
                                        dishLists[index] = snapRecipeData
                                    }
                                    dishLists.get(index)
                                    snapDishLocalListModel = SnapDishLocalListModel(dishLists)
                                    Toast.makeText(activity, "Changes Save", Toast.LENGTH_SHORT).show()
                                    val fragment = MealScanResultFragment()
                                    val args = Bundle()
                                    args.putString("mealId", mealId)
                                    args.putString("mealName", mealName)
                                    args.putString("ImagePathsecound", currentPhotoPathsecound.toString())
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
        }
    }

    private fun setDishData(snapRecipeData: SearchResultItem) {
        if (searchType.contentEquals("SearchDish")){
            addToTheMealTV.text = "Add To The Meal"
        }else {
            addToTheMealTV.text = "Save Changes"
        }
            val capitalized = snapRecipeData.name.toString().replaceFirstChar { it.uppercase() }
            tvMealName.text = capitalized
            if (snapRecipeData.unit != null){
                tvMeasure.text = snapRecipeData.unit
            }
            if (snapRecipeData.mealQuantity != null ){
                quantityEdit.setText(snapRecipeData.mealQuantity?.toInt().toString())
            }
            val imageUrl = getDriveImageUrl(snapRecipeData.photo_url)
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_breakfast)
                .error(R.drawable.ic_breakfast)
                .into(imgFood)

    }

    private fun onMacroNutrientsList(mealDetails: SearchResultItem, value: Int) {

        val calories_kcal : String = mealDetails.nutrients.macros.Calories?.times(value)?.toInt().toString()?: "NA"
        val protein_g : String = mealDetails.nutrients.macros.Protein?.times(value)?.toInt().toString()?: "NA"
        val carb_g : String = mealDetails.nutrients.macros.Carbs?.times(value)?.toInt().toString()?: "NA"
        val fat_g : String = mealDetails.nutrients.macros.Fats?.times(value)?.toInt().toString()?: "NA"

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

    private fun onMicroNutrientsList(mealDetails: SearchResultItem, value: Int) {

        val cholesterol = if (mealDetails.nutrients.micros.Cholesterol != null){
            mealDetails.nutrients.micros.Cholesterol.times(value)?.toInt().toString()
        }else{
            "0"
        }

        val vitamin_A = if (mealDetails.nutrients.micros.Vitamin_A != null){
            mealDetails.nutrients.micros.Vitamin_A.times(value)?.toInt().toString()
        }else{
            "0"
        }

        val vitamin_C = if (mealDetails.nutrients.micros.Vitamin_C != null){
            mealDetails.nutrients.micros.Vitamin_C.times(value)?.toInt().toString()
        }else{
            "0"
        }

        val vitamin_k = if (mealDetails.nutrients.micros.Vitamin_K != null){
            mealDetails.nutrients.micros.Vitamin_K.times(value)?.toInt().toString()
        }else{
            "0"
        }

        val vitaminD = if (mealDetails.nutrients.micros.Vitamin_D != null){
            mealDetails.nutrients.micros.Vitamin_D.toInt().toString()
        }else{
            "0"
        }

        val folate = if (mealDetails.nutrients.micros.Folate != null){
            mealDetails.nutrients.micros.Folate.toInt().toString()
        }else{
            "0"
        }

        val iron_mg = if (mealDetails.nutrients.micros.Iron != null){
            mealDetails.nutrients.micros.Iron.toInt().toString()
        }else{
            "0"
        }

        val calcium = if (mealDetails.nutrients.micros.Calcium != null){
            mealDetails.nutrients.micros.Calcium.toInt().toString()
        }else{
            "0"
        }

        val magnesium = if (mealDetails.nutrients.micros.Magnesium != null){
            mealDetails.nutrients.micros.Magnesium.times(value)?.toInt().toString()
        }else{
            "0"
        }

        val potassium_mg = if (mealDetails.nutrients.micros.Potassium != null){
            mealDetails.nutrients.micros.Potassium.times(value)?.toInt().toString()
        }else{
            "0"
        }

        val fiber_mg = if (mealDetails.nutrients.micros.Fiber != null){
            mealDetails.nutrients.micros.Fiber.times(value)?.toInt().toString()
        }else{
            "0"
        }

        val zinc = if (mealDetails.nutrients.micros.Zinc != null){
            mealDetails.nutrients.micros.Zinc.times(value)?.toInt().toString()
        }else{
            "0"
        }

        val sodium = if (mealDetails.nutrients.micros.Sodium != null){
            mealDetails.nutrients.micros.Sodium.times(value)?.toInt().toString()
        }else{
            "0"
        }

        val sugar_mg = if (mealDetails.nutrients.micros.Sugar != null){
            mealDetails.nutrients.micros.Sugar.times(value)?.toInt().toString()
        }else{
            "0"
        }

        val mealLogs = listOf(
            MicroNutrientsModel(cholesterol, "mg", "Cholesterol", R.drawable.ic_fats),
            MicroNutrientsModel(vitamin_A, "mg", "Vitamin A", R.drawable.ic_fats),
            MicroNutrientsModel(vitamin_C, "mg", "Vitamin C", R.drawable.ic_fats),
            MicroNutrientsModel(vitamin_k, "mg", "Vitamin K", R.drawable.ic_fats),
            MicroNutrientsModel(vitaminD, "mg", "Vitamin D", R.drawable.ic_fats),
            MicroNutrientsModel(folate, "mg", "Folate", R.drawable.ic_fats),
            MicroNutrientsModel(iron_mg, "mg", "Iron", R.drawable.ic_fats),
            MicroNutrientsModel(calcium, "mg", "Calcium", R.drawable.ic_fats),
            MicroNutrientsModel(magnesium, "mg", "Magnesium", R.drawable.ic_fats),
            MicroNutrientsModel(potassium_mg, "mg", "Potassium", R.drawable.ic_fats),
            MicroNutrientsModel(fiber_mg, "mg", "Fiber", R.drawable.ic_fats),
            MicroNutrientsModel(zinc, "mg", "Zinc", R.drawable.ic_fats),
            MicroNutrientsModel(sodium, "mg", "Sodium", R.drawable.ic_fats),
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

    fun getDriveImageUrl(originalUrl: String): String? {
        val regex = Regex("(?<=/d/)(.*?)(?=/|$)")
        val matchResult = regex.find(originalUrl)
        val fileId = matchResult?.value
        return if (!fileId.isNullOrEmpty()) {
            "https://drive.google.com/uc?export=view&id=$fileId"
        } else {
            null
        }
    }

    private fun showMealSelection() {
        val meals = arrayOf("Breakfast", "Lunch", "Dinner")
        val builder = AlertDialog.Builder(requireContext())
        builder.setItems(meals) { _, which ->
        }
        builder.show()
    }

    private fun getMealList() {
        LoaderUtil.showLoader(requireActivity())
         val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
       // val userId = "64763fe2fa0e40d9c0bc8264"
        val startDate = "2025-03-24"
        val call = ApiClient.apiServiceFastApi.getMealList(userId, startDate)
        call.enqueue(object : Callback<MealsResponse> {
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                if (response.isSuccessful) {
                    LoaderUtil.dismissLoader(requireActivity())
                    val mealPlanLists = response.body()?.meals ?: emptyList()
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    LoaderUtil.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                LoaderUtil.dismissLoader(requireActivity())
            }
        })
    }
}