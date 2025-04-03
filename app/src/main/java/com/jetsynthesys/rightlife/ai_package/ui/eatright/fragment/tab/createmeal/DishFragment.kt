package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.createmeal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class DishFragment : BaseFragment<FragmentDishBinding>() {

    private lateinit var macroItemRecyclerView : RecyclerView
    private lateinit var microItemRecyclerView : RecyclerView
    private lateinit var addToTheMealLayout : LinearLayoutCompat
    private lateinit var imageCalender : ImageView
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
    private lateinit var tvQuantity: TextView
    private var quantity = 1
    private lateinit var tvMeasure :TextView

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

        searchType = arguments?.getString("searchType").toString()
        val foodDetailsResponse = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("foodDetailsResponse", FoodDetailsResponse::class.java)
        } else {
            arguments?.getParcelable("foodDetailsResponse")
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

        view.findViewById<ImageView>(R.id.ivIncrease).setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
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
            Toast.makeText(context, "Meal Added", Toast.LENGTH_SHORT).show()
            val fragment = HomeBottomTabFragment()
            val args = Bundle()
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "mealLog")
                addToBackStack("mealLog")
                commit()
            }
        }
        onFrequentlyLoggedItemRefresh()
    }

    private fun setDishData(foodDetailsResponse: FoodDetailsResponse) {
        if (searchType.contentEquals("createRecipe")){
            addToTheMealTV.text = "Add To The Recipe"
        }else{
            addToTheMealTV.text = "Add To The Meal"
            tvMealName.text = foodDetailsResponse.data.name
            Glide.with(this)
                .load(foodDetailsResponse.data.image)
                .placeholder(R.drawable.ic_breakfast)
                .error(R.drawable.ic_breakfast)
                .into(imgFood)
        }
    }

    private fun onMacroNutrientsList(mealDetails : MealDetails) {

        val calories_kcal : String = mealDetails.calories.toInt().toString()?: "NA"
        val protein_g : String = mealDetails.protein.toInt().toString()?: "NA"
        val carb_g : String = mealDetails.carbs.toInt().toString()?: "NA"
        val fat_g : String = mealDetails.fats.toInt().toString()?: "NA"

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

    private fun onMicroNutrientsList(mealDetails : MealDetails) {

        val b12_mcg = if (null != null){
            mealDetails.vitaminD.toInt().toString()
        }else{
            "5"
        }

        val iron_mg = if (mealDetails.iron != null){
            mealDetails.iron.toInt().toString()
        }else{
            "8"
        }
        val magnesium_mg = if (null != null){
            mealDetails.iron.toInt().toString()
        }else{
            "6"
        }

        val phosphorus_mg = if (null != null){
            mealDetails.potassium.toInt().toString()
        }else{
            "10"
        }

        val potassium_mg = if (mealDetails.potassium != null){
            mealDetails.potassium.toInt().toString()
        }else{
            "2"
        }

        val zinc_mg = if (null != null){
            mealDetails.potassium.toInt().toString()
        }else{
            "7"
        }

        val mealLogs = listOf(
            MicroNutrientsModel(b12_mcg, "mg", "Vitamin B", R.drawable.ic_cal),
            MicroNutrientsModel(iron_mg, "mg", "Iron", R.drawable.ic_cabs),
            MicroNutrientsModel(magnesium_mg, "mg", "Magnesium", R.drawable.ic_protein),
            MicroNutrientsModel(phosphorus_mg, "mg", "Phasphorus", R.drawable.ic_fats),
            MicroNutrientsModel(potassium_mg, "mg", "Potassium", R.drawable.ic_fats),
            MicroNutrientsModel(zinc_mg, "mg", "Zinc", R.drawable.ic_fats)
        )

        val valueLists : ArrayList<MicroNutrientsModel> = ArrayList()
        valueLists.addAll(mealLogs as Collection<MicroNutrientsModel>)
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
}