package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.R.*
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.tab.FrequentlyLoggedListAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.YourMealLogsFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.frequentlylogged.LogMealFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.frequentlylogged.LoggedBottomSheet
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MyMealModel
import com.jetsynthesys.rightlife.databinding.FragmentFrequentlyLoggedBinding
import com.google.android.flexbox.FlexboxLayout
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.response.FrequentRecipe
import com.jetsynthesys.rightlife.ai_package.model.response.FrequentRecipesResponse
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.createmeal.CreateMealFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MealLogItems
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.SelectedMealLogList
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FrequentlyLoggedFragment : BaseFragment<FragmentFrequentlyLoggedBinding>() {

    private lateinit var frequentlyLoggedRecyclerView : RecyclerView
    private lateinit var layoutNoMeals : LinearLayoutCompat
    private lateinit var layoutCreateMeal : LinearLayoutCompat
    private lateinit var loggedBottomSheetFragment : LoggedBottomSheet
    private lateinit var flexboxLayout : FlexboxLayout
    private lateinit var addDishBottomSheet : LinearLayout
   // private val ingredientsList = mutableListOf("Poha")
    val ingredientsList : ArrayList<MyMealModel> = ArrayList()
    private val mealLogList : ArrayList<MealLogItems> = ArrayList()
    private var frequentRecipeLogList : ArrayList<FrequentRecipe> = ArrayList()
    private var mealLogRequests : SelectedMealLogList? = null
    private lateinit var mealType : String


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFrequentlyLoggedBinding
        get() = FragmentFrequentlyLoggedBinding::inflate

    private val frequentlyLoggedListAdapter by lazy { FrequentlyLoggedListAdapter(requireContext(), arrayListOf(),
        -1, null, false, :: onFrequentlyLoggedItem) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundColor(ContextCompat.getColor(requireContext(), color.meal_log_background))

        frequentlyLoggedRecyclerView = view.findViewById(R.id.recyclerview_frequently_logged_item)
        layoutNoMeals = view.findViewById(R.id.layout_no_meals)
        layoutCreateMeal = view.findViewById(R.id.layout_create_meal)
        flexboxLayout = view.findViewById(R.id.flexboxLayout)
        val btnAdd: LinearLayoutCompat = view.findViewById(R.id.layout_btnAdd)
        val btnLogMeal: LinearLayoutCompat = view.findViewById(R.id.layout_btnLogMeal)
        val layoutTitle = view.findViewById<LinearLayout>(R.id.layout_title)
        val checkCircle = view.findViewById<ImageView>(R.id.check_circle_icon)
        val loggedSuccess = view.findViewById<TextView>(R.id.tv_logged_success)
         addDishBottomSheet = view.findViewById<LinearLayout>(R.id.layout_add_dish_bottom_sheet)

        mealType = arguments?.getString("mealType").toString()
        frequentlyLoggedRecyclerView.layoutManager = LinearLayoutManager(context)
        frequentlyLoggedRecyclerView.adapter = frequentlyLoggedListAdapter

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = YourMealLogsFragment()
                val args = Bundle()
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment, "landing")
                    addToBackStack("landing")
                    commit()
                }
            }
        })

        getFrequentlyLog()

        layoutCreateMeal.setOnClickListener {
            val fragment = CreateMealFragment()
            val args = Bundle()
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "mealLog")
                addToBackStack("mealLog")
                commit()
            }
        }

        //updateIngredientChips()

        // Add button clicked (For demonstration, adding a dummy ingredient)
        btnAdd.setOnClickListener {
//            val newIngredient = "New Item ${ingredientsList.size + 1}"
//            ingredientsList.add(newIngredient)
//            updateIngredientChips()
            val fragment = LogMealFragment()
            val args = Bundle()
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "mealLog")
                addToBackStack("mealLog")
                commit()
            }
        }

        // Log Meal button click
        btnLogMeal.setOnClickListener {
            //   Toast.makeText(context, "Meal Logged Successfully!", Toast.LENGTH_SHORT).show()
            addDishBottomSheet.visibility = View.GONE
            loggedBottomSheetFragment = LoggedBottomSheet()
            loggedBottomSheetFragment.isCancelable = true
            val bundle = Bundle()
            bundle.putBoolean("test",false)
            loggedBottomSheetFragment.arguments = bundle
            activity?.supportFragmentManager?.let { loggedBottomSheetFragment.show(it, "LoggedBottomSheet") }
        }
    }

    private fun onFrequentlyLoggedList (){

        if (frequentRecipeLogList.size > 0){
            frequentlyLoggedRecyclerView.visibility = View.VISIBLE
            layoutNoMeals.visibility = View.GONE
        }else{
            layoutNoMeals.visibility = View.VISIBLE
            frequentlyLoggedRecyclerView.visibility = View.GONE
        }
        val valueLists : ArrayList<FrequentRecipe> = ArrayList()
        valueLists.addAll(frequentRecipeLogList as Collection<FrequentRecipe>)
        val mealLogDateData: FrequentRecipe? = null
        frequentlyLoggedListAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onFrequentlyLoggedItem(mealLogDateModel: FrequentRecipe, position: Int, isRefresh: Boolean) {
        val valueLists : ArrayList<FrequentRecipe> = ArrayList()
        valueLists.addAll(frequentRecipeLogList as Collection<FrequentRecipe>)
        frequentlyLoggedListAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
      //  addDishBottomSheet.visibility = View.VISIBLE
//        val newIngredient = mealLogDateModel
//        for (ingredient in valueLists) {
//            if (ingredient.isAddDish == true){
//                ingredientsList.add(newIngredient)
//                updateIngredientChips()
//            }
//        }
        val mealLogData = MealLogItems(
            meal_id = mealLogDateModel._id,
            recipe_name = mealLogDateModel.recipe_name,
            meal_quantity = 1,
            unit = "g",
            measure = "Bowl"
        )
//        mealLogList.add(mealLogData)
//        val mealLogRequest = SelectedMealLogList(
//            meal_name = "",
//            meal_type = mealType,
//            meal_log = mealLogList
//        )
      //  mealLogRequests = mealLogRequest
        val parent = parentFragment as? HomeTabMealFragment
        parent?.setSelectedFrequentlyLog(mealLogData, false, null, null)
    }

    // Function to update Flexbox with chips
    private fun updateIngredientChips() {
        flexboxLayout.removeAllViews() // Clear existing chips

        for (ingredient in ingredientsList) {
            val chipView = LayoutInflater.from(context).inflate(layout.chip_ingredient, flexboxLayout, false)
            val tvIngredient: TextView = chipView.findViewById(R.id.tvIngredient)
            val btnRemove: ImageView = chipView.findViewById(R.id.btnRemove)

            tvIngredient.text = ingredient.mealName
            btnRemove.setOnClickListener {
                ingredientsList.remove(ingredient)
                updateIngredientChips()
            }
            flexboxLayout.addView(chipView)
        }
    }

    private fun getFrequentlyLog() {
        //LoaderUtil.showLoader(requireActivity())
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val call = ApiClient.apiServiceFastApi.getFrequentlyLog(userId)
        call.enqueue(object : Callback<FrequentRecipesResponse> {
            override fun onResponse(call: Call<FrequentRecipesResponse>, response: Response<FrequentRecipesResponse>) {
                if (response.isSuccessful) {
                    //LoaderUtil.dismissLoader(requireActivity())
                    if (response.body()?.data?.frequent_recipes != null){
                        if (response.body()?.data?.frequent_recipes!!.isNotEmpty()){
                            frequentRecipeLogList.addAll(response.body()!!.data.frequent_recipes)
                            onFrequentlyLoggedList()
                        }
                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    //LoaderUtil.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<FrequentRecipesResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                //LoaderUtil.dismissLoader(requireActivity())
            }
        })
    }
}