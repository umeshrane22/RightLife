package com.jetsynthesys.rightlife.ai_package.ui.eatright.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.CalendarAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.CalendarSummaryAdapter
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.CalendarDateModel
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.CalendarSummaryModel
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

        recyclerCalendar.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerCalendar.layoutManager = GridLayoutManager(context, 7)
        recyclerCalendar.adapter = calendarAdapter

        recyclerSummary.layoutManager = LinearLayoutManager(context)
        recyclerSummary.adapter = calendarSummaryAdapter

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

        btnClose.setOnClickListener {
            val fragment = YourMealLogsFragment()
            val args = Bundle()
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "landing")
                addToBackStack("landing")
                commit()
            }
        }

        icLeftArrow.setOnClickListener {
            txtDate.text = "Thur, 5 Feb 2025"
        }

        icRightArrow.setOnClickListener {
            txtDate.text = "Thur, 7 Feb 2025"
        }

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedCurrentDate = currentDateTime.format(formatter)
        val ninetyDaysAgo = currentDateTime.minusDays(60)
        val dateRange  = ninetyDaysAgo.format(formatter) + "_to_" + formattedCurrentDate
        getMealsLogHistory(dateRange)
        mealLogYearlyList = generateYearCalendar()

      //  onMealLogCalenderItemRefresh()
        onMealLogCalenderSummaryRefresh()
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

        val now = Calendar.getInstance()
        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH) // 0-11
        val targetIndex = mealLogYearlyList.indexOfFirst {
            it.year == currentYear && it.month == getMonthAbbreviation(currentMonth) && it.date == 1
        }
        if (targetIndex != -1) {
            recyclerCalendar.post {
                recyclerCalendar.scrollToPosition(targetIndex+50)
            }
        }
        Log.d("CalendarScroll", "Scrolling to index: $targetIndex, month: ${getMonthAbbreviation(currentMonth)}")
    }

    private fun onMealLogCalenderSummaryRefresh (){
        val summaryList = listOf(
            CalendarSummaryModel("Deficit", "2140"),
            CalendarSummaryModel("Surplus", "12.3 kCal"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0")
        )
        val valueLists : ArrayList<CalendarSummaryModel> = ArrayList()
        valueLists.addAll(summaryList as Collection<CalendarSummaryModel>)
        val mealLogDateData: CalendarSummaryModel? = null
        calendarSummaryAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onMealLogCalenderItem(mealLogDateModel: CalendarDateModel, position: Int, isRefresh: Boolean) {
        val yearDays = generateYearCalendar()
     //   val calendarDays = List(60) { CalendarDateModel(it + 1, it % 5 == 0) }
        val valueLists : ArrayList<CalendarDateModel> = ArrayList()
        valueLists.addAll(yearDays as Collection<CalendarDateModel>)
        calendarAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
    }

    private fun onMealLogCalenderSummaryItem(mealLogDateModel: CalendarSummaryModel, position: Int, isRefresh: Boolean) {
        val summaryList = listOf(
            CalendarSummaryModel("Deficit", "2140"),
            CalendarSummaryModel("Surplus", "12.3 kCal"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0"),
            CalendarSummaryModel("Surplus", "0")
        )
        val valueLists : ArrayList<CalendarSummaryModel> = ArrayList()
        valueLists.addAll(summaryList as Collection<CalendarSummaryModel>)
        calendarSummaryAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
    }

//    private fun generateYearDays(year: Int): List<CalendarDateModel> {
//        val calendar = Calendar.getInstance()
//        val daysList = mutableListOf<CalendarDateModel>()
//
//        for (month in 0..11) { // 0 = January, 11 = December
//            calendar.set(year, month, 1)
//            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
//
//            for (day in 1..daysInMonth) {
//                daysList.add(
//                    CalendarDateModel(
//                        date = day,
//                        month = month,
//                        year = year,
//                        surplus = (day * 50) % 500 // Random surplus example
//                    )
//                )
//            }
//        }
//        return daysList
//    }

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