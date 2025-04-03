package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient.apiServiceFastApi
import com.jetsynthesys.rightlife.ai_package.model.WorkoutMoveResponseRoutine
import com.jetsynthesys.rightlife.ai_package.model.YourActivityLogMeal
import com.jetsynthesys.rightlife.ai_package.ui.adapter.YourActivitiesListAdapter
import com.jetsynthesys.rightlife.ai_package.ui.adapter.YourWorkoutsListAdapter
import com.jetsynthesys.rightlife.databinding.FragmentYourworkOutsBinding
import kotlinx.coroutines.launch

class YourworkOutsFragment : BaseFragment<FragmentYourworkOutsBinding>() {
    private lateinit var mealLogDateRecyclerView: RecyclerView
    private lateinit var yourWOrkOutsBackButton: ImageView
    private lateinit var myMealRecyclerView: RecyclerView
    private lateinit var saveWorkoutRoutine: LinearLayoutCompat

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentYourworkOutsBinding
        get() = FragmentYourworkOutsBinding::inflate

    private val mealLogDateAdapter by lazy {
        YourActivitiesListAdapter(
            requireContext(),
            arrayListOf(),
            -1,
            null,
            false,
            ::onMealLogDateItem
        )
    }
    private val myWorkoutListAdapter by lazy {
        YourWorkoutsListAdapter(
            requireContext(),
            arrayListOf(),
            -1,
            null,
            false,
            ::onWorkoutItemClick
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the UI
        view.setBackgroundResource(R.drawable.gradient_color_background_workout)
        myMealRecyclerView = view.findViewById(R.id.recyclerview_my_meals_item)
        saveWorkoutRoutine = view.findViewById(R.id.layout_btn_log)
        yourWOrkOutsBackButton = view.findViewById(R.id.back_button)
        mealLogDateRecyclerView = view.findViewById(R.id.recyclerview_calender)
        yourWOrkOutsBackButton.setOnClickListener {
            navigateToFragment(AddWorkoutSearchFragment(), "LandingFragment")
        }

        mealLogDateRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mealLogDateRecyclerView.adapter = mealLogDateAdapter

        myMealRecyclerView.layoutManager = LinearLayoutManager(context)
        myMealRecyclerView.adapter = myWorkoutListAdapter

        // Fetch workouts and update the adapter
        lifecycleScope.launch {
            val workouts = fetchWorkouts(
                userId = "64763fe2fa0e40d9c0bc8264",
                startDate = "2025-03-17",
                endDate = "2025-03-25",
                page = 1,
                limit = 10,
                includeStats = false
            )

            if (workouts != null) {
                Log.d("API_SUCCESS", "Fetched Workouts: $workouts")
                // Map the API response to WorkoutModel and update the adapter
                val workoutModels = mapToWorkoutModels(workouts)
                onMyWorkoutItemRefresh(workoutModels)
            } else {
                Log.e("API_ERROR", "Failed to fetch workouts")
                // Show empty state if needed
                onMyWorkoutItemRefresh(arrayListOf())
            }
        }

        // Set up other UI components
        onMealLogDateItemRefresh()

        saveWorkoutRoutine.setOnClickListener {
            val fragment = CreateRoutineFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(AddWorkoutSearchFragment(), "LandingFragment")
            }
        })
    }

    private fun mapToWorkoutModels(response: WorkoutMoveResponseRoutine): ArrayList<WorkoutModel> {
        val workoutModels = ArrayList<WorkoutModel>()
        response.data?.forEach { workoutData ->
            // Calculate duration in "X hr Y min" format
            val hours = workoutData.duration / 60
            val minutes = workoutData.duration % 60
            val durationText = buildString {
                if (hours > 0) append("$hours hr ")
                append("$minutes min")
            }

            // Determine intensity based on activity factor (you can adjust this logic)
            val intensity = when {
                workoutData.activityFactor > 1.5 -> "High Intensity"
                workoutData.activityFactor > 1.3 -> "Moderate Intensity"
                else -> "Low Intensity"
            }

            val workoutModel = WorkoutModel(
                workoutType = workoutData.workoutType ?: "Unknown",
                duration = durationText,
                caloriesBurned = "${workoutData.caloriesBurned.toInt()} cal",
                intensity = intensity
            )
            workoutModels.add(workoutModel)
        }
        return workoutModels
    }

    private fun onMealLogDateItemRefresh() {
        val mealLogs = listOf(
            YourActivityLogMeal("01", "M", true),
            YourActivityLogMeal("02", "T", false),
            YourActivityLogMeal("03", "W", true),
            YourActivityLogMeal("04", "T", false),
            YourActivityLogMeal("05", "F", true),
            YourActivityLogMeal("06", "S", true),
            YourActivityLogMeal("07", "S", true)
        )

        val valueLists: ArrayList<YourActivityLogMeal> = ArrayList()
        valueLists.addAll(mealLogs)
        val mealLogDateData: YourActivityLogMeal? = null
        mealLogDateAdapter.addAll(valueLists, -1, mealLogDateData, false)
    }

    private fun onMealLogDateItem(mealLogDateModel: YourActivityLogMeal, position: Int, isRefresh: Boolean) {
        val mealLogs = listOf(
            YourActivityLogMeal("01", "M", true),
            YourActivityLogMeal("02", "T", false),
            YourActivityLogMeal("03", "W", true),
            YourActivityLogMeal("04", "T", false),
            YourActivityLogMeal("05", "F", true),
            YourActivityLogMeal("06", "S", true),
            YourActivityLogMeal("07", "S", true)
        )

        val valueLists: ArrayList<YourActivityLogMeal> = ArrayList()
        valueLists.addAll(mealLogs)
        mealLogDateAdapter.addAll(valueLists, position, mealLogDateModel, isRefresh)
    }

    private fun onMyWorkoutItemRefresh(workoutModels: ArrayList<WorkoutModel>) {
        if (workoutModels.isNotEmpty()) {
            myMealRecyclerView.visibility = View.VISIBLE
        } else {
            myMealRecyclerView.visibility = View.GONE
        }

        val workoutData: WorkoutModel? = null
        myWorkoutListAdapter.addAll(workoutModels, -1, workoutData, false)
    }

    private fun onWorkoutItemClick(workoutModel: WorkoutModel, position: Int, isRefresh: Boolean) {
        // Handle workout item click if needed
        Log.d("WorkoutClick", "Clicked on ${workoutModel.workoutType} at position $position")
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    suspend fun fetchWorkouts(
        userId: String,
        startDate: String,
        endDate: String,
        page: Int,
        limit: Int,
        includeStats: Boolean
    ): WorkoutMoveResponseRoutine? {
        return try {
            val response = apiServiceFastApi.getFetchWorkouts(userId, startDate, endDate, page, limit, includeStats)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Exception: ${e.message}")
            null
        }
    }
}

data class WorkoutModel(
    val workoutType: String, // e.g., "Running", "Basketball"
    val duration: String, // e.g., "1 hr 30 min"
    val caloriesBurned: String, // e.g., "337 cal"
    val intensity: String // e.g., "Low Intensity"
)