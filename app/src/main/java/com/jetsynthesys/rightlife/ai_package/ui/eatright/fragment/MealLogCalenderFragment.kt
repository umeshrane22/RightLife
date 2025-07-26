package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.CalendarAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.CalendarSummaryAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.CalendarDateModel
import com.jetsynthesys.rightlife.databinding.FragmentMealLogCalenderBinding
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.response.LoggedMeal
import com.jetsynthesys.rightlife.ai_package.model.response.MealLogsHistoryResponse
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MealLogWeeklyDayModel
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class MealLogCalenderFragment : BaseFragment<FragmentMealLogCalenderBinding>() {

    private lateinit var icLeftArrow : ImageView
    private lateinit var txtDate : TextView
    private lateinit var icRightArrow : ImageView
    private lateinit var btnClose : ImageView
    private lateinit var recyclerCalendar : RecyclerView
    private lateinit var recyclerSummary : RecyclerView
    private lateinit var nestedScrollView : NestedScrollView
    private var mealLogsHistoryResponse : MealLogsHistoryResponse? = null
    private var  mealLogHistory :  ArrayList<LoggedMeal> = ArrayList()
    private var mealLogYearlyList : List<CalendarDateModel> = ArrayList()
    private var loadingOverlay : FrameLayout? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMealLogCalenderBinding
        get() = FragmentMealLogCalenderBinding::inflate

    private val calendarAdapter by lazy { CalendarAdapter(requireContext(), arrayListOf(), -1, null,
        false, :: onMealLogCalenderItem) }

    private val calendarSummaryAdapter by lazy { CalendarSummaryAdapter(requireContext(), arrayListOf(), -1, null,
        false, :: onMealLogCalenderSummaryItem) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerCalendar = view.findViewById(R.id.recyclerCalendar)
        recyclerSummary = view.findViewById(R.id.recyclerSummary)
        icLeftArrow = view.findViewById(R.id.icLeftArrow)
        txtDate = view.findViewById(R.id.txtDate)
        icRightArrow = view.findViewById(R.id.icRightArrow)
        btnClose = view.findViewById(R.id.btnClose)
        nestedScrollView = view.findViewById(R.id.nestedScrollView)

        recyclerCalendar.isNestedScrollingEnabled = false
        recyclerCalendar.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        recyclerCalendar.layoutManager = GridLayoutManager(context, 7)
        recyclerCalendar.adapter = calendarAdapter

        recyclerSummary.layoutManager = LinearLayoutManager(context)
        recyclerSummary.adapter = calendarSummaryAdapter

        val moduleName = arguments?.getString("ModuleName").toString()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = YourMealLogsFragment()
                val args = Bundle()
                args.putString("ModuleName", moduleName)
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment, "landing")
                    addToBackStack("landing")
                    commit()
                }
            }
        })

        btnClose.setOnClickListener {
            val fragment = YourMealLogsFragment()
            val args = Bundle()
            args.putString("ModuleName", moduleName)
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "landing")
                addToBackStack("landing")
                commit()
            }
        }

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedCurrentDate = currentDateTime.format(formatter)
        val ninetyDaysAgo = currentDateTime.minusDays(60)
        val dateRange  = ninetyDaysAgo.format(formatter) + "_to_" + formattedCurrentDate
        getMealsLogHistory(dateRange)
        mealLogYearlyList = generateYearCalendar()
    }

    private fun onMealLogCalenderList (yearList: List<CalendarDateModel>, mealLogHistory: ArrayList<LoggedMeal>){
        val today = LocalDate.now()
        val yearLists : ArrayList<MealLogWeeklyDayModel> = ArrayList()
        if (mealLogHistory.size > 0 && yearList.isNotEmpty()){
            mealLogHistory.forEach { mealLog ->
                for (item in yearList){
                    if (item.fullDate == mealLog.date){
                        if (mealLog.is_available == true){
                            item.is_available = true
                        }
                    }
                }
            }
        }
        val valueLists : ArrayList<CalendarDateModel> = ArrayList()
        valueLists.addAll(yearList as Collection<CalendarDateModel>)
        val mealLogDateData: CalendarDateModel? = null
        calendarAdapter.addAll(valueLists, -1, mealLogDateData, false)

        val sampledList = ArrayList<CalendarDateModel>()
        val step = valueLists.size / 53f
        for (i in 0 until 53) {
            val index = (i * step).toInt().coerceAtMost(valueLists.lastIndex)
            sampledList.add(valueLists[index])
        }
        calendarSummaryAdapter.addAll(sampledList, -1, mealLogDateData, false)

        val now = Calendar.getInstance()
        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH) // 0-11

        val targetIndex = mealLogYearlyList.indexOfFirst {
            it.year == currentYear && it.month == getMonthAbbreviation(currentMonth) && it.date == 1
        }

        Log.d("CalendarScroll", "Trying to scroll to: $targetIndex")
        recyclerCalendar.post {
            recyclerCalendar.scrollToPosition(targetIndex)
            recyclerCalendar.post {
                val holder = recyclerCalendar.findViewHolderForAdapterPosition(targetIndex)
                if (holder != null) {
                    val y = holder.itemView.top
                    nestedScrollView.smoothScrollTo(0, recyclerCalendar.top + y)
                }
            }
        }

        val layoutManager = recyclerCalendar.layoutManager as GridLayoutManager
        val daysPerMonth = 30 // average days per month

// 👉 Right arrow = Next month (scroll down)
        icRightArrow.setOnClickListener {
            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            val targetPosition = (currentPosition + daysPerMonth).coerceAtMost(calendarAdapter.itemCount - 1)

            recyclerCalendar.smoothScrollToPosition(targetPosition)
            recyclerCalendar.post {
                val holder = recyclerCalendar.findViewHolderForAdapterPosition(targetPosition)
                holder?.let {
                    val y = it.itemView.top
                    nestedScrollView.smoothScrollTo(0, recyclerCalendar.top + y)
                }
            }
        }

// 👉 Left arrow = Previous month (scroll up)
        icLeftArrow.setOnClickListener {
            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            val targetPosition = (currentPosition - daysPerMonth).coerceAtLeast(0)

            recyclerCalendar.smoothScrollToPosition(targetPosition)
            recyclerCalendar.post {
                val holder = recyclerCalendar.findViewHolderForAdapterPosition(targetPosition)
                holder?.let {
                    val y = it.itemView.top
                    nestedScrollView.smoothScrollTo(0, recyclerCalendar.top + y)
                }
            }
        }

    }

    private fun onMealLogCalenderItem(calendarDateModel: CalendarDateModel, position: Int, isRefresh: Boolean) {
        val mealLogCalenderBottomSheet = MealLogCalenderBottomSheet()
        mealLogCalenderBottomSheet.isCancelable = true
        val args = Bundle()
        args.putString("SelectedDate", calendarDateModel.fullDate)
        mealLogCalenderBottomSheet.arguments = args
        parentFragment.let { mealLogCalenderBottomSheet.show(childFragmentManager, "MealLogCalenderBottomSheet") }
    }

    private fun onMealLogCalenderSummaryItem(mealLogDateModel: CalendarDateModel, position: Int, isRefresh: Boolean) {

    }

    private fun generateYearCalendar(): List<CalendarDateModel> {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val daysList = mutableListOf<CalendarDateModel>()
        val currentMonth = getMonthAbbreviation(calendar.get(Calendar.MONTH))
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy")
        val currentDate = dateFormat.format(calendar.time)
        txtDate.text = currentDate

        // Set calendar to January 1st of the given year
        calendar.set(year, Calendar.JANUARY, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // Sunday = 1, Monday = 2, etc.

        // Calculate how many previous year days we need to add to start from Monday
        val daysToFill = if (firstDayOfWeek == Calendar.MONDAY) 0 else (firstDayOfWeek - 2 + 7) % 7

        // Add previous year days
        calendar.add(Calendar.DAY_OF_YEAR, -daysToFill)
        for (i in 0 until daysToFill) {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            daysList.add(
                CalendarDateModel(
                    date = calendar.get(Calendar.DAY_OF_MONTH),
                    month = getMonthAbbreviation(calendar.get(Calendar.MONTH)),
                    year = calendar.get(Calendar.YEAR),
                    dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK),
                    currentDate = currentDate,
                    currentMonth = currentMonth,
                    fullDate = formatter.format(calendar.time),
                    surplus = (i * 50) % 500 // Random surplus example
                )
            )
            calendar.add(Calendar.DAY_OF_YEAR, 1) // Move forward
        }
        // Now reset to actual year and start adding days
        calendar.set(year, Calendar.JANUARY, 1)
        while (calendar.get(Calendar.YEAR) == year) {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            daysList.add(
                CalendarDateModel(
                    date = calendar.get(Calendar.DAY_OF_MONTH),
                    month = getMonthAbbreviation(calendar.get(Calendar.MONTH)),
                    year = calendar.get(Calendar.YEAR),
                    dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK),
                    currentDate = currentDate,
                    currentMonth = currentMonth,
                    fullDate = formatter.format(calendar.time),
                    surplus = (1 * 50) % 500 // Random surplus example
                )
            )
            calendar.add(Calendar.DAY_OF_YEAR, 1) // Move forward
        }
        return daysList
    }

    private fun getMonthAbbreviation(monthNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthNumber) // Months are 0-based
        val dateFormat = SimpleDateFormat("MMM", Locale.ENGLISH) // "MMM" for 3-letter month
        return dateFormat.format(calendar.time)
    }

    private fun getMealsLogHistory(dateRange: String) {
        if (isAdded  && view != null){
            requireActivity().runOnUiThread {
                showLoader(requireView())
            }
        }
        val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
        val call = ApiClient.apiServiceFastApi.getMealsLogHistoryCalender(userId, dateRange)
        call.enqueue(object : Callback<MealLogsHistoryResponse> {
            override fun onResponse(call: Call<MealLogsHistoryResponse>, response: Response<MealLogsHistoryResponse>) {
                if (response.isSuccessful) {
                    if (isAdded  && view != null){
                        requireActivity().runOnUiThread {
                            dismissLoader(requireView())
                        }
                    }
                    if (response.body() != null){
                        mealLogsHistoryResponse = response.body()
                        if (mealLogsHistoryResponse?.is_logged_meal_list!!.isNotEmpty()){
                            mealLogHistory.addAll(mealLogsHistoryResponse!!.is_logged_meal_list!!)
                            onMealLogCalenderList(mealLogYearlyList , mealLogHistory)
                        }
                    }
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    if (isAdded  && view != null){
                        requireActivity().runOnUiThread {
                            dismissLoader(requireView())
                        }
                    }
                }
            }
            override fun onFailure(call: Call<MealLogsHistoryResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                if (isAdded  && view != null){
                    requireActivity().runOnUiThread {
                        dismissLoader(requireView())
                    }
                }
            }
        })
    }

    fun showLoader(view: View) {
        loadingOverlay = view.findViewById(R.id.loading_overlay)
        loadingOverlay?.visibility = View.VISIBLE
    }

    fun dismissLoader(view: View) {
        loadingOverlay = view.findViewById(R.id.loading_overlay)
        loadingOverlay?.visibility = View.GONE
    }
}