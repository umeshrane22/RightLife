package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.jetsynthesys.rightlife.ai_package.model.response.SearchResultItem
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.HomeTabMealFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.SelectedMealLogList
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.SnapDishLocalListModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.SnapMealRequestLocalListModel

class DishToLogFragment : BaseFragment<FragmentDishBinding>() {

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
    private lateinit var quantityEdit: EditText
    private lateinit var tvCheckOutRecipe: TextView
    private lateinit var tvChange: TextView
    private lateinit var tvQuantity: TextView
    private var quantity = 1
    private lateinit var backButton : ImageView
    private lateinit var ivEdit : ImageView
    private lateinit var tvMeasure :TextView
    private var dishLists : ArrayList<SearchResultItem> = ArrayList()
    private lateinit var snapDishLocalListModel : SnapDishLocalListModel
    private var mealLogRequests : SelectedMealLogList? = null
    private var snapMealLogRequests : SelectedMealLogList? = null
    private lateinit var mealType : String
    private var snapMealRequestLocalListModel : SnapMealRequestLocalListModel? = null
    private var moduleName : String = ""
    private var selectedMealDate : String = ""

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
     //   tvQuantity = view.findViewById(R.id.tvQuantity)
        tvMeasure = view.findViewById(R.id.tvMeasure)
        addToTheMealTV = view.findViewById(R.id.tv_addToTheMeal)
        tvMealName = view.findViewById(R.id.tvMealName)
        imgFood = view.findViewById(R.id.imgFood)
        layoutMicroTitle = view.findViewById(R.id.layoutMicroTitle)
        layoutMacroTitle = view.findViewById(R.id.layoutMacroTitle)
        microUP = view.findViewById(R.id.microUP)
        icMacroUP = view.findViewById(R.id.icMacroUP)
        quantityEdit = view.findViewById(R.id.quantityEdit)
        ivEdit = view.findViewById(R.id.ivEdit)
        backButton = view.findViewById(R.id.backButton)

        moduleName = arguments?.getString("ModuleName").toString()
        searchType = arguments?.getString("searchType").toString()
        mealType = arguments?.getString("mealType").toString()
        selectedMealDate = arguments?.getString("selectedMealDate").toString()
        val snapRecipeName = arguments?.getString("snapRecipeName").toString()
        val searchResultItem = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("searchResultItem", SearchResultItem::class.java)
        } else {
            arguments?.getParcelable("searchResultItem")
        }

        val snapDishLocalListModels = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("snapDishLocalListModel", SnapDishLocalListModel::class.java)
        } else {
            arguments?.getParcelable("snapDishLocalListModel")
        }

        val selectedMealLogListModels = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("selectedMealLogList", SelectedMealLogList::class.java)
        } else {
            arguments?.getParcelable("selectedMealLogList")
        }

        val selectedSnapMealLogListModels = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("selectedSnapMealLogList", SelectedMealLogList::class.java)
        } else {
            arguments?.getParcelable("selectedSnapMealLogList")
        }

        val snapMealRequestLocalListModels = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("snapMealRequestLocalListModel", SnapMealRequestLocalListModel::class.java)
        } else {
            arguments?.getParcelable("snapMealRequestLocalListModel")
        }

        if (snapMealRequestLocalListModels != null){
            snapMealRequestLocalListModel = snapMealRequestLocalListModels
        }

        if (selectedMealLogListModels != null){
            mealLogRequests = selectedMealLogListModels
        }

        if (selectedSnapMealLogListModels != null){
            snapMealLogRequests = selectedSnapMealLogListModels
        }

        if (snapDishLocalListModels != null){
            snapDishLocalListModel = snapDishLocalListModels

            if (searchResultItem != null){
                val data = searchResultItem
                if (snapDishLocalListModel.data.size > 0){
                    dishLists.addAll(snapDishLocalListModel.data)
                }
                dishLists.add(data)
                snapDishLocalListModel = SnapDishLocalListModel(dishLists)
            }
        }else{
            if (searchResultItem != null) {
                val data = searchResultItem
                dishLists.add(data)
                snapDishLocalListModel = SnapDishLocalListModel(dishLists)
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

        macroItemRecyclerView.layoutManager = GridLayoutManager(context, 4)
        macroItemRecyclerView.adapter = macroNutrientsAdapter
        microItemRecyclerView.layoutManager = GridLayoutManager(context, 4)
        microItemRecyclerView.adapter = microNutrientsAdapter

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (searchType.contentEquals("EatRight")){
                    val fragment = HomeBottomTabFragment()
                    val args = Bundle()
                    args.putString("ModuleName", moduleName)
                    args.putString("selectedMealDate", selectedMealDate)
                    fragment.arguments = args
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, fragment, "landing")
                        addToBackStack("landing")
                        commit()
                    }
                }else{
                    val fragment = SearchDishToLogFragment()
                    val args = Bundle()
                    args.putString("ModuleName", moduleName)
                    args.putString("selectedMealDate", selectedMealDate)
                    fragment.arguments = args
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, fragment, "landing")
                        addToBackStack("landing")
                        commit()
                    }
                }
            }
        })

        backButton.setOnClickListener {
            if (searchType.contentEquals("EatRight")){
                val fragment = HomeBottomTabFragment()
                val args = Bundle()
                args.putString("ModuleName", moduleName)
                args.putString("selectedMealDate", selectedMealDate)
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment, "landing")
                    addToBackStack("landing")
                    commit()
                }
            }else{
                val fragment = SearchDishToLogFragment()
                val args = Bundle()
                args.putString("ModuleName", moduleName)
                args.putString("selectedMealDate", selectedMealDate)
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment, "landing")
                    addToBackStack("landing")
                    commit()
                }
            }
        }

        quantityEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(update: Editable?) {
                if (update!!.isNotEmpty() && update.toString() != "."){
                    if (quantityEdit.text.toString().toDouble() > 0.0){
                        val targetValue : Double = quantityEdit.text.toString().toDouble()
                        if (searchResultItem != null){
                            setDishData(searchResultItem, true)
                            var ingredientQuantity = 0.0
                            if (searchResultItem.mealQuantity != null && searchResultItem.mealQuantity > 0.0){
                                ingredientQuantity = searchResultItem.mealQuantity
                            }else{
                                ingredientQuantity = 1.0
                            }
                            onMacroNutrientsList(searchResultItem, ingredientQuantity, targetValue)
                            onMicroNutrientsList(searchResultItem, ingredientQuantity, targetValue)
                        }else{
                            if (snapDishLocalListModel != null){
                                for (item in snapDishLocalListModel.data) {
                                    if (item.name.contentEquals(snapRecipeName)) {
                                        setDishData(item, true)
                                        var ingredientQuantity = 0.0
                                        if (item.mealQuantity != null && item.mealQuantity > 0.0){
                                            ingredientQuantity = item.mealQuantity
                                        }else{
                                            ingredientQuantity = 1.0
                                        }
                                        onMacroNutrientsList(item, ingredientQuantity, targetValue)
                                        onMicroNutrientsList(item, ingredientQuantity, targetValue)
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        if (searchResultItem != null){
            setDishData(searchResultItem, false)
            onMacroNutrientsList(searchResultItem, 1.0, 1.0)
            onMicroNutrientsList(searchResultItem, 1.0, 1.0)
        }

        addToTheMealLayout.setOnClickListener {

            if (quantityEdit.text.toString().isNotEmpty() && quantityEdit.text.toString() != "."){
                if (quantityEdit.text.toString().toDouble() > 0.0){
                    if (snapDishLocalListModel?.data != null){
                        val fragment = HomeTabMealFragment()
                        val args = Bundle()
                        args.putString("ModuleName", moduleName)
                        args.putString("searchType","DishToLog")
                        args.putString("mealType", mealType)
                        args.putString("selectedMealDate", selectedMealDate)
                        args.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
                        args.putParcelable("selectedMealLogList", mealLogRequests)
                        args.putParcelable("selectedSnapMealLogList", snapMealLogRequests)
                        args.putParcelable("snapMealRequestLocalListModel", snapMealRequestLocalListModel)
                        fragment.arguments = args
                        requireActivity().supportFragmentManager.beginTransaction().apply {
                            replace(R.id.flFragment, fragment, "mealLog")
                            addToBackStack("mealLog")
                            commit()
                        }
                    }
                }else{
                    Toast.makeText(activity, "Please input quantity greater than 0", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(activity, "Please input quantity greater than 0", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDishData(snapRecipeData: SearchResultItem,  isEdit: Boolean) {
        if (searchType.contentEquals("createRecipe")){
            addToTheMealTV.text = "Proceed"
        }else{
            addToTheMealTV.text = "Proceed"
            val capitalized = snapRecipeData.name.toString().replaceFirstChar { it.uppercase() }
            tvMealName.text = capitalized
            if (snapRecipeData.unit != null){
                tvMeasure.text = snapRecipeData.unit
            }
            if (!isEdit){
                if (snapRecipeData.mealQuantity != null ){
                    if (snapRecipeData.mealQuantity > 0.0){
                        quantityEdit.setText(snapRecipeData.mealQuantity.toInt().toString())
                    }else{
                        quantityEdit.setText("1")
                    }
                }
            }
            var imageUrl : String? = ""
            imageUrl = if (snapRecipeData.photo_url.contains("drive.google.com")) {
                getDriveImageUrl(snapRecipeData.photo_url)
            }else{
                snapRecipeData.photo_url
            }
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_view_meal_place)
                .error(R.drawable.ic_view_meal_place)
                .into(imgFood)
        }
    }

    private fun onMacroNutrientsList(mealDetails: SearchResultItem, defaultValue: Double, targetValue: Double) {

        val calories = calculateValue(mealDetails.nutrients.macros.Calories, defaultValue, targetValue)
        val protein = calculateValue(mealDetails.nutrients.macros.Protein, defaultValue, targetValue)
        val carbs = calculateValue(mealDetails.nutrients.macros.Carbs, defaultValue, targetValue)
        val fats = calculateValue(mealDetails.nutrients.macros.Fats, defaultValue, targetValue)
        val calories_kcal : String = calories.toInt().toString()?: "NA"
        val protein_g : String = protein.toInt().toString()?: "NA"
        val carb_g : String = carbs.toInt().toString()?: "NA"
        val fat_g : String = fats.toInt().toString()?: "NA"

        val mealLogs = listOf(
            MacroNutrientsModel(calories_kcal, "kcal", "Calorie", R.drawable.ic_cal),
            MacroNutrientsModel(protein_g, "g", "Protein", R.drawable.ic_protein),
            MacroNutrientsModel(carb_g, "g", "Carbs", R.drawable.ic_cabs),
            MacroNutrientsModel(fat_g, "g", "Fats", R.drawable.ic_fats),
        )

        val valueLists : ArrayList<MacroNutrientsModel> = ArrayList()
        valueLists.addAll(mealLogs as Collection<MacroNutrientsModel>)
        val mealLogDateData: MacroNutrientsModel? = null
        macroNutrientsAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onMicroNutrientsList(mealDetails: SearchResultItem, defaultValue: Double, targetValue: Double) {

        val cholesterol = if (mealDetails.nutrients.micros.Cholesterol != null){
            calculateValue( mealDetails.nutrients.micros.Cholesterol, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val vitamin_A = if (mealDetails.nutrients.micros.Vitamin_A != null){
            calculateValue( mealDetails.nutrients.micros.Vitamin_A, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val vitamin_C = if (mealDetails.nutrients.micros.Vitamin_C != null){
            calculateValue( mealDetails.nutrients.micros.Vitamin_C, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val vitamin_k = if (mealDetails.nutrients.micros.Vitamin_K != null){
            calculateValue( mealDetails.nutrients.micros.Vitamin_K, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val vitaminD = if (mealDetails.nutrients.micros.Vitamin_D != null){
            calculateValue( mealDetails.nutrients.micros.Vitamin_D, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val folate = if (mealDetails.nutrients.micros.Folate != null){
            calculateValue( mealDetails.nutrients.micros.Folate, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val iron_mg = if (mealDetails.nutrients.micros.Iron != null){
            calculateValue( mealDetails.nutrients.micros.Iron, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val calcium = if (mealDetails.nutrients.micros.Calcium != null){
            calculateValue( mealDetails.nutrients.micros.Calcium, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val magnesium = if (mealDetails.nutrients.micros.Magnesium != null){
            calculateValue( mealDetails.nutrients.micros.Magnesium, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val potassium_mg = if (mealDetails.nutrients.micros.Potassium != null){
            calculateValue( mealDetails.nutrients.micros.Potassium, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val fiber_mg = if (mealDetails.nutrients.micros.Fiber != null){
            calculateValue( mealDetails.nutrients.micros.Fiber, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val zinc = if (mealDetails.nutrients.micros.Zinc != null){
            calculateValue( mealDetails.nutrients.micros.Zinc, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val sodium = if (mealDetails.nutrients.micros.Sodium != null){
            calculateValue( mealDetails.nutrients.micros.Sodium, defaultValue, targetValue).toInt().toString()
        }else{
            "0"
        }

        val sugar_mg = if (mealDetails.nutrients.micros.Sugar != null){
            calculateValue( mealDetails.nutrients.micros.Sugar, defaultValue, targetValue).toInt().toString()
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
            MicroNutrientsModel(sugar_mg, "g", "Sugar", R.drawable.ic_fats)
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

    private fun calculateValue(givenValue: Double?, defaultQuantity: Double, targetQuantity: Double): Double {
        return if (givenValue != null && defaultQuantity > 0.0) {
            (givenValue / defaultQuantity) * targetQuantity
        } else {
            0.0
        }
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
}