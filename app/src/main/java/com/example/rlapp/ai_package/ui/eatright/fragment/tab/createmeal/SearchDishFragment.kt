package com.example.rlapp.ai_package.ui.eatright.fragment.tab.createmeal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ai_package.base.BaseFragment
import com.example.rlapp.ai_package.ui.eatright.adapter.tab.createmeal.SearchDishesAdapter
import com.example.rlapp.ai_package.ui.eatright.fragment.YourMealLogsFragment
import com.example.rlapp.ai_package.ui.eatright.model.MyMealModel
import com.example.rlapp.databinding.FragmentSearchDishBinding

class SearchDishFragment : BaseFragment<FragmentSearchDishBinding>() {

    private lateinit var searchLayout: LinearLayoutCompat
    private lateinit var etSearch: EditText
    private lateinit var cancel: TextView
    private lateinit var searchResultLayout: LinearLayout
    private lateinit var tvSearchResult: TextView
    private lateinit var searchResultListLayout: ConstraintLayout
    private lateinit var tvAllDishes: TextView
    private lateinit var allDishesRecyclerview: RecyclerView
    private lateinit var searchType: String

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchDishBinding
        get() = FragmentSearchDishBinding::inflate

    private val searchDishAdapter by lazy {
        SearchDishesAdapter(
            requireContext(), arrayListOf(), -1, null,
            false, ::onSearchDishItem
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.meal_log_background
            )
        )


        searchLayout = view.findViewById(R.id.layout_search)
        etSearch = view.findViewById(R.id.et_search)
        cancel = view.findViewById(R.id.tv_cancel)
        searchResultLayout = view.findViewById(R.id.layout_search_result)
        tvSearchResult = view.findViewById(R.id.tv_search_result)
        searchResultListLayout = view.findViewById(R.id.layout_search_resultList)
        tvAllDishes = view.findViewById(R.id.tv_all_dishes)
        allDishesRecyclerview = view.findViewById(R.id.recyclerView_all_dishes)

        allDishesRecyclerview.layoutManager = LinearLayoutManager(context)
        allDishesRecyclerview.adapter = searchDishAdapter

        searchType = arguments?.getString("searchType").toString()


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

        onSearchDishItemRefresh()
    }

    private fun onSearchDishItemRefresh() {

        val mealLogs = listOf(
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false)
        )

        val valueLists: ArrayList<MyMealModel> = ArrayList()
        valueLists.addAll(mealLogs as Collection<MyMealModel>)
        val mealLogDateData: MyMealModel? = null
        searchDishAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onSearchDishItem(mealLogDateModel: MyMealModel, position: Int, isRefresh: Boolean) {

        val mealLogs = listOf(
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", false)
        )

        val valueLists: ArrayList<MyMealModel> = ArrayList()
        valueLists.addAll(mealLogs as Collection<MyMealModel>)
        searchDishAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)

        val fragment = DishFragment()
        val args = Bundle()
        args.putString("searchType", searchType)
        fragment.arguments = args
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, "landing")
            addToBackStack("landing")
            commit()
        }
    }
}