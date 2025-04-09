package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.model.NutritionData
import com.jetsynthesys.rightlife.ai_package.model.NutritionDetails
import com.jetsynthesys.rightlife.ai_package.model.ScanMealNutritionResponse
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.FrequentlyLoggedMealScanResultAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.MacroNutrientsAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.MicroNutrientsAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MacroNutrientsModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MicroNutrientsModel
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.databinding.FragmentMealScanResultsBinding
import com.jetsynthesys.rightlife.newdashboard.HomeDashboardActivity
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
    private lateinit var tvFoodName : TextView
    private lateinit var imageFood : ImageView
    private lateinit var tvQuantity: TextView
    private lateinit var tvSelectedDate : TextView
    private lateinit var addToLogLayout : LinearLayoutCompat
    private lateinit var saveMealLayout : LinearLayoutCompat
    private lateinit var layoutMicroTitle : ConstraintLayout
    private lateinit var layoutMacroTitle : ConstraintLayout
    private lateinit var icMacroUP : ImageView
    private lateinit var microUP : ImageView

    private var quantity = 1


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMealScanResultsBinding
        get() = FragmentMealScanResultsBinding::inflate

    private val microNutrientsAdapter by lazy { MicroNutrientsAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMicroNutrientsItem) }
    private val mealListAdapter by lazy { FrequentlyLoggedMealScanResultAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onFrequentlyLoggedItem) }

    private val macroNutrientsAdapter by lazy { MacroNutrientsAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMealLogDateItem) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        macroItemRecyclerView = view.findViewById(R.id.recyclerview_macro_item)
        microItemRecyclerView = view.findViewById(R.id.recyclerview_micro_item)
        frequentlyLoggedRecyclerView = view.findViewById(R.id.recyclerview_frequently_logged_item)
        var btnChange = view.findViewById<TextView>(R.id.change_btn)
        tvFoodName = view.findViewById(R.id.tvFoodName)
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

        currentPhotoPath = arguments?.getString("ImagePath") .toString()

        view.findViewById<ImageView>(R.id.ivDatePicker).setOnClickListener {
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
            onFrequentlyLoggedItemRefresh(foodDataResponses.data)
            if (foodDataResponses.data.size > 0){
                onMicroNutrientsList(foodDataResponses.data.get(0).nutrition_per_100g)
                onMacroNutrientsList(foodDataResponses.data.get(0).nutrition_per_100g)
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

    }

    private fun onMicroNutrientsList (nutrition: NutritionDetails){

        val b12_mcg = if (nutrition.b12_mcg != null){
            nutrition.b12_mcg.toInt().toString()
        }else{
            "0"
        }

        val iron_mg = if (nutrition.iron_mg != null){
            nutrition.iron_mg.toInt().toString()
        }else{
            "0"
        }
        val magnesium_mg = if (nutrition.magnesium_mg != null){
            nutrition.magnesium_mg.toInt().toString()
        }else{
            "0"
        }

        val phosphorus_mg = if (nutrition.phosphorus_mg != null){
            nutrition.phosphorus_mg.toInt().toString()
        }else{
            "0"
        }

        val potassium_mg = if (nutrition.potassium_mg != null){
            nutrition.potassium_mg.toInt().toString()
        }else{
            "0"
        }

        val zinc_mg = if (nutrition.zinc_mg != null){
            nutrition.zinc_mg.toInt().toString()
        }else{
            "0"
        }

        val vitaminD = if (nutrition.vitamin_d_iu != null){
            nutrition.vitamin_d_iu.toInt().toString()
        }else{
            "0"
        }

        val folate = if (nutrition.zinc_mg != null){
            nutrition.zinc_mg.toInt().toString()
        }else{
            "0"
        }

        val vitaminC = if (nutrition.zinc_mg != null){
            nutrition.zinc_mg.toInt().toString()
        }else{
            "0"
        }

        val vitaminA = if (nutrition.zinc_mg != null){
            nutrition.zinc_mg.toInt().toString()
        }else{
            "0"
        }

        val vitaminK = if (nutrition.zinc_mg != null){
            nutrition.zinc_mg.toInt().toString()
        }else{
            "0"
        }

        val calcium = if (nutrition.calcium_mg != null){
            nutrition.calcium_mg.toInt().toString()
        }else{
            "0"
        }

        val omega = if (nutrition.zinc_mg != null){
            nutrition.zinc_mg.toInt().toString()
        }else{
            "0"
        }

        val sodium = if (nutrition.sodium_mg != null){
            nutrition.sodium_mg.toInt().toString()
        }else{
            "0"
        }

        val cholesterol = if (nutrition.cholesterol_mg != null){
            nutrition.cholesterol_mg.toInt().toString()
        }else{
            "0"
        }

        val sugar = if (nutrition.sugar_g != null){
            nutrition.sugar_g.toInt().toString()
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
            MicroNutrientsModel(omega, "mg", "Omega-3", R.drawable.ic_fats),
            MicroNutrientsModel(sodium, "mg", "Sodium", R.drawable.ic_fats),
            MicroNutrientsModel(cholesterol, "mg", "Cholesterol", R.drawable.ic_fats),
            MicroNutrientsModel(sugar, "mg", "Sugar", R.drawable.ic_fats)

        )

        val valueLists : ArrayList<MicroNutrientsModel> = ArrayList()
        valueLists.addAll(mealLogs as Collection<MicroNutrientsModel>)
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
    private fun onMacroNutrientsList(nutrition: NutritionDetails) {

        val calories_kcal : String = nutrition.calories_kcal.toInt().toString()?: "NA"
        val protein_g : String = nutrition.protein_g.toInt().toString()?: "NA"
        val carb_g : String = nutrition.carb_g.toInt().toString()?: "NA"
        val fat_g : String = nutrition.fat_g.toInt().toString()?: "NA"

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

    private fun onFrequentlyLoggedItemRefresh(recipes: List<NutritionData>) {

        if (recipes.size > 0){
            frequentlyLoggedRecyclerView.visibility = View.VISIBLE
            //   layoutNoMeals.visibility = View.GONE
        }else{
            //    layoutNoMeals.visibility = View.VISIBLE
            frequentlyLoggedRecyclerView.visibility = View.GONE
        }

        val valueLists : ArrayList<NutritionData> = ArrayList()
        valueLists.addAll(recipes as Collection<NutritionData>)
        val mealLogDateData: NutritionData? = null
        mealListAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onFrequentlyLoggedItem(mealLogDateModel: NutritionDetails, position: Int, isRefresh: Boolean) {

//        val mealLogs = listOf(
//            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", true),
//            MyMealModel("Breakfast", "Dal", "1", "1,157", "8", "308", "17", false),
//            MyMealModel("Breakfast", "Rice", "1", "1,157", "8", "308", "17", false),
//            MyMealModel("Breakfast", "Roti", "1", "1,157", "8", "308", "17", false)
//        )

//        val valueLists : ArrayList<RecipeLists> = ArrayList()
//        valueLists.addAll(mealLogs as Collection<RecipeLists>)
//        frequentlyLoggedListAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
    }

    private fun setFoodData(nutritionResponse: ScanMealNutritionResponse) {

        if (nutritionResponse.data != null){
            val file = File(currentPhotoPath)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                // Rotate the image if required
                val imageUri = FileProvider.getUriForFile(requireContext(),
                    "${requireContext().packageName}.fileprovider", file)
                val rotatedBitmap = rotateImageIfRequired(requireContext(), bitmap, imageUri)
                // Set the image in the UI
                imageFood.setImageBitmap(rotatedBitmap)
            } else {
                Log.e("ImageCapture", "File does not exist at $currentPhotoPath")
            }
            if (nutritionResponse.data.size > 0){
                tvFoodName.text = nutritionResponse.data.get(0).name
            }
        }
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
}