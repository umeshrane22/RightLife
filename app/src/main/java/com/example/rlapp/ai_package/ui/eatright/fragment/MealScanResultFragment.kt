package com.example.rlapp.ai_package.ui.eatright.fragment

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
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ai_package.base.BaseFragment
import com.example.rlapp.ai_package.model.Nutrition
import com.example.rlapp.ai_package.model.NutritionData
import com.example.rlapp.ai_package.model.NutritionDetails
import com.example.rlapp.ai_package.model.NutritionResponse
import com.example.rlapp.ai_package.model.RecipeLists
import com.example.rlapp.ai_package.model.ScanMealNutritionResponse
import com.example.rlapp.ai_package.ui.eatright.adapter.FrequentlyLoggedMealScanResultAdapter
import com.example.rlapp.ai_package.ui.eatright.adapter.MacroNutientsListAdapter
import com.example.rlapp.ai_package.ui.eatright.adapter.MicroNutientsListAdapter
import com.example.rlapp.ai_package.ui.eatright.model.MacroNutrientsModel
import com.example.rlapp.ai_package.ui.eatright.model.MicroNutrientsModel
import com.example.rlapp.ai_package.ui.home.HomeBottomTabFragment
import com.example.rlapp.databinding.FragmentMealScanResultsBinding
import com.example.rlapp.newdashboard.HomeDashboardActivity
import com.example.rlapp.ui.profile_new.ProfileSettingsActivity
import com.google.android.material.snackbar.Snackbar
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
    private var quantity = 1


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMealScanResultsBinding
        get() = FragmentMealScanResultsBinding::inflate
    var snackbar: Snackbar? = null
    private val microNutientsAdapter by lazy { MicroNutientsListAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMicroNutrientsItem) }
    private val mealListAdapter by lazy { FrequentlyLoggedMealScanResultAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onFrequentlyLoggedItem) }

    private val macroNutientsAdapter by lazy { MacroNutientsListAdapter(requireContext(), arrayListOf(), -1,
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

        frequentlyLoggedRecyclerView.layoutManager = LinearLayoutManager(context)
        frequentlyLoggedRecyclerView.adapter = mealListAdapter

        macroItemRecyclerView.layoutManager = GridLayoutManager(context, 4)
        macroItemRecyclerView.adapter = macroNutientsAdapter

        microItemRecyclerView.layoutManager = GridLayoutManager(context, 4)
        microItemRecyclerView.adapter = microNutientsAdapter

        // Data for Spinner
        val items = arrayOf("Breakfast", "Lunch", "Dinner")

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
                onMicroNutrientsItemRefresh(foodDataResponses.data.get(0).nutrition_per_100g)
                onMacroNutrientsItemRefresh(foodDataResponses.data.get(0).nutrition_per_100g)
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
    }
    private fun onMicroNutrientsItemRefresh (nutrition: NutritionDetails){

        val b12_mcg = if (nutrition.b12_mcg != null){
            nutrition.b12_mcg.toInt().toString()
        }else{
            "5"
        }

        val iron_mg = if (nutrition.iron_mg != null){
            nutrition.iron_mg.toInt().toString()
        }else{
            "8"
        }
        val magnesium_mg = if (nutrition.magnesium_mg != null){
            nutrition.magnesium_mg.toInt().toString()
        }else{
            "6"
        }

        val phosphorus_mg = if (nutrition.phosphorus_mg != null){
            nutrition.phosphorus_mg.toInt().toString()
        }else{
            "10"
        }

        val potassium_mg = if (nutrition.potassium_mg != null){
            nutrition.potassium_mg.toInt().toString()
        }else{
            "2"
        }

        val zinc_mg = if (nutrition.zinc_mg != null){
            nutrition.zinc_mg.toInt().toString()
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
        microNutientsAdapter.addAll(valueLists, -1, mealLogDateData, false)
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
    private fun onMacroNutrientsItemRefresh(nutrition: NutritionDetails) {

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
        macroNutientsAdapter.addAll(valueLists, -1, mealLogDateData, false)
        //microNutientsAdapter.addAll(valueLists, -1, mealLogDateData, false)
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