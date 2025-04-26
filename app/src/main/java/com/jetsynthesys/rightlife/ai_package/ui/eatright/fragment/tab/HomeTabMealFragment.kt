package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.YourMealLogsFragment
import com.jetsynthesys.rightlife.databinding.FragmentHomeTabMealBinding
import com.google.android.material.tabs.TabLayout
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.request.MealLogItem
import com.jetsynthesys.rightlife.ai_package.model.request.SaveMealLogRequest
import com.jetsynthesys.rightlife.ai_package.model.response.MealUpdateResponse
import com.jetsynthesys.rightlife.ai_package.model.response.SnapRecipeData
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.SearchDishToLogFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment.tab.frequentlylogged.FrequentlyAddDishBottomSheet
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.SnapDishLocalListModel
import com.jetsynthesys.rightlife.ai_package.utils.LoaderUtil
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeTabMealFragment : BaseFragment<FragmentHomeTabMealBinding>() {

    private lateinit var tabLayout : TabLayout
    private lateinit var backIc : ImageView
    private lateinit var searchLayout : LinearLayoutCompat
    private var dishLists : ArrayList<SnapRecipeData> = ArrayList()
    private  var snapDishLocalListModel : SnapDishLocalListModel? = null
    private lateinit var searchType : String
    private lateinit var mealType : String

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeTabMealBinding
        get() = FragmentHomeTabMealBinding::inflate

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.meal_log_background))
        tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        backIc = view.findViewById(R.id.backIc)
        searchLayout = view.findViewById(R.id.searchLayout)

        searchType = arguments?.getString("searchType").toString()
        mealType = arguments?.getString("mealType").toString()
        val dishLocalListModels = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("snapDishLocalListModel", SnapDishLocalListModel::class.java)
        } else {
            arguments?.getParcelable("snapDishLocalListModel")
        }

        if (dishLocalListModels != null){
            snapDishLocalListModel = dishLocalListModels
            dishLists.addAll(snapDishLocalListModel!!.data)
        }

        val tabTitles = arrayOf("Frequently Logged", "My Meal", "My Recipe")

        for (title in tabTitles) {
            val tab = tabLayout.newTab()
            val customView =
                LayoutInflater.from(context).inflate(R.layout.custom_tab, null) as TextView
            customView.text = title
            tab.customView = customView
            tabLayout.addTab(tab)
        }

        // Set default fragment
        if (savedInstanceState == null) {
            replaceFragment(FrequentlyLoggedFragment())
            updateTabColors()
        }

        // Handle tab clicks manually
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> replaceFragment(FrequentlyLoggedFragment())
                    1 -> replaceFragment(MyMealFragment())
                  //  2 -> replaceFragment(MealPlanFragment())
                    2 -> replaceFragment(MyRecipeFragment())
                }
                updateTabColors()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
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

        backIc.setOnClickListener {
            val fragment = YourMealLogsFragment()
            val args = Bundle()
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "landing")
                addToBackStack("landing")
                commit()
            }
        }

        searchLayout.setOnClickListener {
            val fragment = SearchDishToLogFragment()
            val args = Bundle()
            args.putString("searchType", "HomeTabMeal")
            args.putString("mealType", mealType)
            args.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "landing")
                addToBackStack("landing")
                commit()
            }
        }

        if (searchType.contentEquals("DishToLog")){
            if (snapDishLocalListModel != null){
                loggedAddDish(snapDishLocalListModel)
            }
        }
    }

    // Function to replace fragments
    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    // Function to update tab selection colors
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTabColors() {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            val customView = tab?.customView
            val tabText = customView?.findViewById<TextView>(R.id.tabText)

            if (tab?.isSelected == true) {
                tabText?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                val typeface = resources.getFont(R.font.dmsans_bold)
                tabText?.typeface = typeface
            } else {
                val typeface = resources.getFont(R.font.dmsans_regular)
                tabText?.typeface = typeface
                tabText?.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_unselected_text))
            }
        }
    }

    private fun loggedAddDish(snapDishLocalListModel: SnapDishLocalListModel?) {
        val frequentlyAddDishBottomSheet = FrequentlyAddDishBottomSheet()
        frequentlyAddDishBottomSheet.isCancelable = true
        val args = Bundle()
        args.putString("mealType", mealType)
        args.putParcelable("snapDishLocalListModel", snapDishLocalListModel)
        args.putBoolean("test",false)
        frequentlyAddDishBottomSheet.arguments = args
        activity?.supportFragmentManager?.let { frequentlyAddDishBottomSheet.show(it, "FrequentlyAddDishBottomSheet") }
    }

    private fun createMealsSave(snapRecipeList : ArrayList<SnapRecipeData>) {
        LoaderUtil.showLoader(requireActivity())
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        // val userId = "64763fe2fa0e40d9c0bc8264"
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDateTime.format(formatter)

        val mealLogList : ArrayList<MealLogItem> = ArrayList()
        val mealNamesString = snapRecipeList.map { it.recipe_name ?: "" }.joinToString(", ")

        snapRecipeList?.forEach { snapRecipe ->
            val mealLogData = MealLogItem(
                meal_id = snapRecipe.id,
                meal_quantity = 1,
                unit = "g",
                measure = "Bowl"
            )
            mealLogList.add(mealLogData)
        }

        val mealLogRequest = SaveMealLogRequest(
            meal_name = "",
            meal_type = "",
            meal_log = mealLogList
        )

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
        val call = ApiClient.apiServiceFastApi.createSaveMealsToLog(userId, formattedDate, mealLogRequest)
        call.enqueue(object : Callback<MealUpdateResponse> {
            override fun onResponse(call: Call<MealUpdateResponse>, response: Response<MealUpdateResponse>) {
                if (response.isSuccessful) {
                    LoaderUtil.dismissLoader(requireActivity())
                    val mealData = response.body()?.message
                    Toast.makeText(activity, mealData, Toast.LENGTH_SHORT).show()
//                    val fragment = HomeTabMealFragment()
//                    val args = Bundle()
//                    fragment.arguments = args
//                    requireActivity().supportFragmentManager.beginTransaction().apply {
//                        replace(R.id.flFragment, fragment, "landing")
//                        addToBackStack("landing")
//                        commit()
//                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    LoaderUtil.dismissLoader(requireActivity())
                }
            }
            override fun onFailure(call: Call<MealUpdateResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                LoaderUtil.dismissLoader(requireActivity())
            }
        })
    }
}