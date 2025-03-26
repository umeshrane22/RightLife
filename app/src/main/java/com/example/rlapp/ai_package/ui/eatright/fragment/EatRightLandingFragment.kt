package com.example.rlapp.ai_package.ui.eatright.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ai_package.base.BaseFragment
import com.example.rlapp.ai_package.ui.eatright.adapter.MealPlanEatLandingAdapter
import com.example.rlapp.ai_package.ui.eatright.adapter.OtherRecepieEatLandingAdapter
import com.example.rlapp.ai_package.ui.eatright.adapter.tab.FrequentlyLoggedListAdapter
import com.example.rlapp.ai_package.ui.eatright.model.MealPlanModel
import com.example.rlapp.ai_package.ui.eatright.model.MyMealModel
import com.example.rlapp.databinding.FragmentEatRightLandingBinding
import com.google.android.material.snackbar.Snackbar

class EatRightLandingFragment : BaseFragment<FragmentEatRightLandingBinding>() {
    private lateinit var mealPlanRecyclerView: RecyclerView
    private lateinit var frequentlyLoggedRecyclerView: RecyclerView
    private lateinit var otherReciepeRecyclerView: RecyclerView


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEatRightLandingBinding
        get() = FragmentEatRightLandingBinding::inflate
    var snackbar: Snackbar? = null
    private val mealPlanAdapter by lazy {
        MealPlanEatLandingAdapter(
            requireContext(),
            arrayListOf(),
            -1,
            null,
            false,
            ::onMealPlanDateItem
        )
    }
    private val otherReciepeAdapter by lazy {
        OtherRecepieEatLandingAdapter(
            requireContext(),
            arrayListOf(),
            -1,
            null,
            false,
            ::onOtherReciepeDateItem
        )
    }
    private val frequentlyLoggedListAdapter by lazy {
        FrequentlyLoggedListAdapter(
            requireContext(),
            arrayListOf(),
            -1,
            null,
            false,
            ::onFrequentlyLoggedItem
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val halfCurveProgressBar =
            view.findViewById<HalfCurveProgressBar>(R.id.halfCurveProgressBar)
        val snapMealBtn = view.findViewById<ConstraintLayout>(R.id.lyt_snap_meal)
        val mealLogLayout = view.findViewById<LinearLayout>(R.id.layout_meal_log)
        mealPlanRecyclerView = view.findViewById(R.id.recyclerview_meal_plan_item)
        frequentlyLoggedRecyclerView = view.findViewById(R.id.recyclerview_frequently_logged_item)
        otherReciepeRecyclerView = view.findViewById(R.id.recyclerview_other_reciepe_item)

        otherReciepeRecyclerView.layoutManager = LinearLayoutManager(context)
        otherReciepeRecyclerView.adapter = otherReciepeAdapter
        onOtherReciepeDateItemRefresh()
        mealPlanRecyclerView.layoutManager = LinearLayoutManager(context)
        mealPlanRecyclerView.adapter = mealPlanAdapter
        onMealPlanItemRefresh()
        frequentlyLoggedRecyclerView.layoutManager = LinearLayoutManager(context)
        frequentlyLoggedRecyclerView.adapter = frequentlyLoggedListAdapter
        onFrequentlyLoggedItemRefresh()

        halfCurveProgressBar.setProgress(50f)

        val animator = ValueAnimator.ofFloat(0f, 70f)
        animator.duration = 2000 // 2 seconds
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            halfCurveProgressBar.setProgress(value)
        }
        animator.start()

        // val glassWithWaterView: GlassWithWaterView = view.findViewById(R.id.glassWithWaterView)
        //glassWithWaterView.startWaterAnimation()

        snapMealBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = MealScanResultFragment()
                val args = Bundle()
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

        mealLogLayout.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                val mealSearchFragment = YourMealLogsFragment()
                val args = Bundle()
                mealSearchFragment.arguments = args
                replace(R.id.flFragment, mealSearchFragment, "Steps")
                addToBackStack(null)
                commit()
            }
        }

    }

    private fun onFrequentlyLoggedItemRefresh() {

        val meal = listOf(
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", true),
            MyMealModel("Breakfast", "Dal", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Rice", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Roti", "1", "1,157", "8", "308", "17", false)
        )

        if (meal.size > 0) {
            frequentlyLoggedRecyclerView.visibility = View.VISIBLE
            //   layoutNoMeals.visibility = View.GONE
        } else {
            //    layoutNoMeals.visibility = View.VISIBLE
            frequentlyLoggedRecyclerView.visibility = View.GONE
        }

        val valueLists: ArrayList<MyMealModel> = ArrayList()
        valueLists.addAll(meal as Collection<MyMealModel>)
        val mealLogDateData: MyMealModel? = null
        frequentlyLoggedListAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onFrequentlyLoggedItem(
        mealLogDateModel: MyMealModel,
        position: Int,
        isRefresh: Boolean
    ) {

        val mealLogs = listOf(
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", true),
            MyMealModel("Breakfast", "Dal", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Rice", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Roti", "1", "1,157", "8", "308", "17", false)
        )

        val valueLists: ArrayList<MyMealModel> = ArrayList()
        valueLists.addAll(mealLogs as Collection<MyMealModel>)
        frequentlyLoggedListAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
        // addDishBottomSheet.visibility = View.VISIBLE

        val newIngredient = mealLogDateModel
        for (ingredient in valueLists) {
            if (ingredient.isAddDish == true) {
                //  ingredientsList.add(newIngredient)
                // updateIngredientChips()
            }
        }
    }

    private fun onMealPlanItemRefresh() {

        val meal = listOf(
            MealPlanModel("Breakfast", "Poha", "Vegeterian", "25", "1", "1,157", "8", "308", "17"),
            MealPlanModel(
                "Lunch",
                "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun",
                "Vegeterian",
                "25",
                "1",
                "1,157",
                "8",
                "308",
                "17"
            ),
            MealPlanModel(
                "Dinner",
                "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun",
                "Vegeterian",
                "25",
                "1",
                "1,157",
                "8",
                "308",
                "17"
            )
        )

        /* if (meal.size > 0){
             mealPlanRecyclerView.visibility = View.VISIBLE
             layoutNoMealPlan.visibility = View.GONE
         }else{
             layoutNoMealPlan.visibility = View.VISIBLE
             mealPlanRecyclerView.visibility = View.GONE
         }*/

        val valueLists: ArrayList<MealPlanModel> = ArrayList()
        valueLists.addAll(meal as Collection<MealPlanModel>)
        val mealPlanData: MealPlanModel? = null
        mealPlanAdapter.addAll(valueLists, -1, mealPlanData, false)
        //mealPlanAdapter.addAll(valueLists, -1, mealPlanData, false)
    }

    private fun onMealPlanDateItem(
        mealLogDateModel: MealPlanModel,
        position: Int,
        isRefresh: Boolean
    ) {

        val mealLogs = listOf(
            MealPlanModel("Breakfast", "Poha", "Vegeterian", "25", "1", "1,157", "8", "308", "17"),
            MealPlanModel(
                "Lunch",
                "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun",
                "Vegeterian",
                "25",
                "1",
                "1,157",
                "8",
                "308",
                "17"
            ),
            MealPlanModel(
                "Dinner",
                "Dal,Rice,Chapati,Spinach,Paneer,Gulab Jamun",
                "Vegeterian",
                "25",
                "1",
                "1,157",
                "8",
                "308",
                "17"
            )
        )

        val valueLists: ArrayList<MealPlanModel> = ArrayList()
        valueLists.addAll(mealLogs as Collection<MealPlanModel>)
        //  mealLogDateAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
    }

    private fun onOtherReciepeDateItemRefresh() {

        val meal = listOf(
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", true),
            MyMealModel("Breakfast", "Dal", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Rice", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Roti", "1", "1,157", "8", "308", "17", false)
        )

        if (meal.size > 0) {
            frequentlyLoggedRecyclerView.visibility = View.VISIBLE
            //   layoutNoMeals.visibility = View.GONE
        } else {
            //    layoutNoMeals.visibility = View.VISIBLE
            frequentlyLoggedRecyclerView.visibility = View.GONE
        }

        val valueLists: ArrayList<MyMealModel> = ArrayList()
        valueLists.addAll(meal as Collection<MyMealModel>)
        val mealLogDateData: MyMealModel? = null
        otherReciepeAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onOtherReciepeDateItem(
        mealLogDateModel: MyMealModel,
        position: Int,
        isRefresh: Boolean
    ) {

        val mealLogs = listOf(
            MyMealModel("Breakfast", "Poha", "1", "1,157", "8", "308", "17", true),
            MyMealModel("Breakfast", "Dal", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Rice", "1", "1,157", "8", "308", "17", false),
            MyMealModel("Breakfast", "Roti", "1", "1,157", "8", "308", "17", false)
        )

        val valueLists: ArrayList<MyMealModel> = ArrayList()
        valueLists.addAll(mealLogs as Collection<MyMealModel>)
        otherReciepeAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
        // addDishBottomSheet.visibility = View.VISIBLE

        val newIngredient = mealLogDateModel
        for (ingredient in valueLists) {
            if (ingredient.isAddDish == true) {
                //  ingredientsList.add(newIngredient)
                // updateIngredientChips()
            }
        }
    }
}