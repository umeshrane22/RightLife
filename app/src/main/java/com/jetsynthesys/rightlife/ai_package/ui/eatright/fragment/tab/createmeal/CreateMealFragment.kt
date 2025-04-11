package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.createmeal

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.MealDetails
import com.jetsynthesys.rightlife.ai_package.model.MealLists
import com.jetsynthesys.rightlife.ai_package.model.MealLogRequest
import com.jetsynthesys.rightlife.ai_package.model.MealLogResponse
import com.jetsynthesys.rightlife.ai_package.model.MealsResponse
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.tab.createmeal.DishListAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.HomeTabMealFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.DishLocalListModel
import com.jetsynthesys.rightlife.databinding.FragmentCreateMealBinding
import com.jetsynthesys.rightlife.ui.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateMealFragment : BaseFragment<FragmentCreateMealBinding>() {

    private lateinit var addedDishItemRecyclerview : RecyclerView
    private lateinit var etAddName : EditText
    private lateinit var tvContinue : TextView
    private lateinit var editMeal : ImageView
    private lateinit var dinnerDotMenu : ImageView
    private lateinit var btnAddLayout : LinearLayoutCompat
    private lateinit var editDeleteBreakfast : CardView
    private lateinit var editDeleteLunch : CardView
    private lateinit var addedMealListLayout : LinearLayoutCompat
    private lateinit var saveMealLayout : LinearLayoutCompat
    private lateinit var layoutNoDishes: LinearLayoutCompat
    private lateinit var addMealNameLayout : LinearLayoutCompat
    private lateinit var continueLayout : LinearLayoutCompat
    private lateinit var addedNameTv : TextView
  //  var mealLogLists : ArrayList<MealLists> = ArrayList()
    private var dishLists : ArrayList<MealDetails> = ArrayList()
    private  var dishLocalListModel : DishLocalListModel? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateMealBinding
        get() = FragmentCreateMealBinding::inflate

    private val dishListAdapter by lazy { DishListAdapter(requireContext(), arrayListOf(), -1,
        null, false, :: onMealLogClickItem, :: onMealLogDeleteItem, :: onMealLogEditItem) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addMealNameLayout = view.findViewById(R.id.layout_add_meal_name)
        etAddName = view.findViewById(R.id.et_add_name)
        continueLayout = view.findViewById(R.id.layout_continue)
        tvContinue = view.findViewById(R.id.tv_continue)
        addedDishItemRecyclerview = view.findViewById(R.id.recyclerview_added_dish_item)
        layoutNoDishes = view.findViewById(R.id.layout_no_dishes)
        saveMealLayout = view.findViewById(R.id.layout_save_meal)
        editMeal = view.findViewById(R.id.ic_edit)
        addedMealListLayout = view.findViewById(R.id.layout_added_meal_list)
        btnAddLayout = view.findViewById(R.id.layout_btnAdd)
        addedNameTv = view.findViewById(R.id.addedNameTv)
//        editDeleteLunch = view.findViewById(R.id.btn_edit_delete_lunch)
//        editDeleteDinner = view.findViewById(R.id.btn_edit_delete_dinner)
//        layoutMain = view.findViewById(R.id.layout_main)
//        layoutDelete = view.findViewById(R.id.layout_delete)
//        layoutViewFood = view.findViewById(R.id.layout_view_food)
  //      val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
     //   val circleIndicator = view.findViewById<View>(R.id.circleIndicator)
        continueLayout.isEnabled = false
        continueLayout.setBackgroundResource(R.drawable.light_green_bg)

        addedDishItemRecyclerview.layoutManager = LinearLayoutManager(context)
        addedDishItemRecyclerview.adapter = dishListAdapter

       val dishLocalListModels = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("dishLocalListModel", DishLocalListModel::class.java)
        } else {
            arguments?.getParcelable("dishLocalListModel")
        }

        if (dishLocalListModels != null){
            dishLocalListModel = dishLocalListModels
            dishLists.addAll(dishLocalListModel!!.meals)
            onMealLoggedList()
        }

        etAddName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 0){
                    continueLayout.isEnabled = true
                    continueLayout.setBackgroundResource(R.drawable.green_meal_bg)
                }else{
                    continueLayout.isEnabled = false
                    continueLayout.setBackgroundResource(R.drawable.light_green_bg)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = HomeTabMealFragment()
                val args = Bundle()
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment, "landing")
                    addToBackStack("landing")
                    commit()
                }
            }
        })

        //getMealList()

        saveMealLayout.setOnClickListener {

            val fragment = HomeTabMealFragment()
            val args = Bundle()
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "mealLog")
                addToBackStack("mealLog")
                commit()
            }
        }

        continueLayout.setOnClickListener {
            addMealNameLayout.visibility = View.GONE
            continueLayout.visibility = View.GONE
            addedMealListLayout.visibility = View.VISIBLE
            saveMealLayout.visibility = View.VISIBLE

            if (layoutNoDishes.visibility == View.GONE){
                saveMealLayout.isEnabled = true
                saveMealLayout.setBackgroundResource(R.drawable.green_meal_bg)
                addedNameTv.text = etAddName.text
            }else{
                saveMealLayout.isEnabled = false
                saveMealLayout.setBackgroundResource(R.drawable.light_green_bg)
            }
        }

        editMeal.setOnClickListener {
            addMealNameLayout.visibility = View.VISIBLE
            addedMealListLayout.visibility = View.GONE
            saveMealLayout.visibility = View.GONE
            continueLayout.visibility = View.VISIBLE
            if (etAddName.text.length > 0){
                continueLayout.isEnabled = true
                continueLayout.setBackgroundResource(R.drawable.green_meal_bg)
            }else{
                continueLayout.isEnabled = false
                continueLayout.setBackgroundResource(R.drawable.light_green_bg)
            }
        }

//        dinnerDotMenu.setOnClickListener {
//            if (editDeleteDinner.visibility == View.GONE){
//              //  layoutMain.setBackgroundColor(Color.parseColor("#0A1214"))
//                editDeleteDinner.visibility = View.VISIBLE
//            }else{
//              //  layoutMain.setBackgroundColor(Color.parseColor("#F0FFFA"))
//                editDeleteDinner.visibility = View.GONE
//            }
//        }

        btnAddLayout.setOnClickListener {
            val fragment = SearchDishFragment()
            val args = Bundle()
            args.putString("searchType", "createMeal")
            args.putParcelable("dishLocalListModel", dishLocalListModel)
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "landing")
                addToBackStack("landing")
                commit()
            }
        }
    }

    private fun onMealLoggedList (){

//        val meal = listOf(
//            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", true),
//            MyMealModel("Breakfast", "Dal", "1", "1,157", "8", "308", "17", false)
//        )

        if (dishLists.size > 0){
            addedDishItemRecyclerview.visibility = View.VISIBLE
            layoutNoDishes.visibility = View.GONE
            saveMealLayout.visibility = View.VISIBLE
        }else{
            layoutNoDishes.visibility = View.VISIBLE
            addedDishItemRecyclerview.visibility = View.GONE
            saveMealLayout.visibility = View.GONE
        }

        val valueLists : ArrayList<MealDetails> = ArrayList()
        valueLists.addAll(dishLists as Collection<MealDetails>)
        val mealLog: MealDetails? = null
        dishListAdapter.addAll(valueLists, -1, mealLog, false)
    }

    private fun onMealLogClickItem(mealLog: MealDetails, position: Int, isRefresh: Boolean) {

        val valueLists : ArrayList<MealDetails> = ArrayList()
        valueLists.addAll(dishLists as Collection<MealDetails>)
        dishListAdapter.addAll(valueLists, position, mealLog, isRefresh)
    }

    private fun onMealLogDeleteItem(mealItem: MealDetails, position: Int, isRefresh: Boolean) {

        val valueLists : ArrayList<MealDetails> = ArrayList()
        valueLists.addAll(dishLists as Collection<MealDetails>)
        dishListAdapter.addAll(valueLists, position, mealItem, isRefresh)
    }

    private fun onMealLogEditItem(mealItem: MealDetails, position: Int, isRefresh: Boolean) {

        val valueLists : ArrayList<MealDetails> = ArrayList()
        valueLists.addAll(dishLists as Collection<MealDetails>)
        dishListAdapter.addAll(valueLists, position, mealItem, isRefresh)
    }

//    private fun deleteMealDialog(){
//
//        deleteBottomSheetFragment = DeleteMealBottomSheet()
//        deleteBottomSheetFragment.isCancelable = true
//        val bundle = Bundle()
//        bundle.putBoolean("test",false)
//        deleteBottomSheetFragment.arguments = bundle
//        activity?.supportFragmentManager?.let { deleteBottomSheetFragment.show(it, "DeleteMealBottomSheet") }
//    }

    private fun getMealList() {
        Utils.showLoader(requireActivity())
        // val userId = appPreference.getUserId().toString()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val userId = "64763fe2fa0e40d9c0bc8264"
        val startDate = "2025-04-10"
        val call = ApiClient.apiServiceFastApi.getMealList(userId, startDate)
        call.enqueue(object : Callback<MealsResponse> {
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                if (response.isSuccessful) {
                    Utils.dismissLoader(requireActivity())
                    val mealPlanLists = response.body()?.meals ?: emptyList()
                  //  mealLogLists.addAll(mealPlanLists)
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

    private fun createMeal(mealDetails: MealDetails) {
        Utils.showLoader(requireActivity())
        // val userId = appPreference.getUserId().toString()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val userId = "64763fe2fa0e40d9c0bc8264"
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formattedDate = currentDateTime.format(formatter)

        val mealLogRequest = MealLogRequest(
            mealId = mealDetails._id,
            userId = "64763fe2fa0e40d9c0bc8264",
            meal = mealDetails.name,
            date = formattedDate,
            image = mealDetails.image,
            mealType = mealDetails.mealType,
            mealQuantity = mealDetails.mealQuantity,
            unit = mealDetails.unit,
            isRepeat = mealDetails.isRepeat,
            isFavourite = mealDetails.isFavourite,
            isLogged = true
        )
        val call = ApiClient.apiServiceFastApi.createLogMeal(mealLogRequest)
        call.enqueue(object : Callback<MealLogResponse> {
            override fun onResponse(call: Call<MealLogResponse>, response: Response<MealLogResponse>) {
                if (response.isSuccessful) {
                    Utils.dismissLoader(requireActivity())
                    val mealData = response.body()?.message
                    Toast.makeText(activity, mealData, Toast.LENGTH_SHORT).show()
                    val fragment = CreateMealFragment()
                    val args = Bundle()
                    args.putParcelable("dishLocalListModel", dishLocalListModel)
                    fragment.arguments = args
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, fragment, "mealLog")
                        addToBackStack("mealLog")
                        commit()
                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    Utils.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<MealLogResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                Utils.dismissLoader(requireActivity())
            }
        })
    }
}