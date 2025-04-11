package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.BundleCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.model.WorkoutList
import com.jetsynthesys.rightlife.ai_package.model.WorkoutSessionRecord
import com.jetsynthesys.rightlife.ai_package.model.YourActivityLogMeal
import com.jetsynthesys.rightlife.ai_package.ui.adapter.YourActivitiesListAdapter
import com.jetsynthesys.rightlife.ai_package.ui.adapter.YourWorkoutsListAdapter
import com.jetsynthesys.rightlife.databinding.FragmentYourworkOutsBinding

class YourworkOutsFragment : BaseFragment<FragmentYourworkOutsBinding>() {
    private lateinit var mealLogDateRecyclerView: RecyclerView
    private lateinit var yourWorkOutsBackButton: ImageView
    private lateinit var layout_btn_log_meal: LinearLayoutCompat
    private lateinit var myMealRecyclerView: RecyclerView
    private lateinit var saveWorkoutRoutine: LinearLayoutCompat
    private var workout: WorkoutList? = null

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
        yourWorkOutsBackButton = view.findViewById(R.id.back_button)
        mealLogDateRecyclerView = view.findViewById(R.id.recyclerview_calender)
        layout_btn_log_meal = view.findViewById(R.id.layout_btn_log_meal)
        workout = arguments?.getParcelable("workout")

        layout_btn_log_meal.setOnClickListener{
            val fragment = SearchWorkoutFragment()

           // fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "YourworkOutsFragment")
                addToBackStack("YourworkOutsFragment")
                commit()
            }
        }

        mealLogDateRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mealLogDateRecyclerView.adapter = mealLogDateAdapter

        myMealRecyclerView.layoutManager = LinearLayoutManager(context)
        myMealRecyclerView.adapter = myWorkoutListAdapter

        // Fetch data from bundle
        val workoutRecord =
            arguments?.let { BundleCompat.getParcelable(it, "workoutRecord", WorkoutSessionRecord::class.java) }
        workoutRecord?.let { record ->
            Log.d("YourworkOuts", "Received WorkoutSessionRecord: ${record}")
            // Convert WorkoutSessionRecord to WorkoutModel
            val workoutModel = convertToWorkoutModel(record)
            // Update RecyclerView with the new data
            val workoutModels = arrayListOf(workoutModel)
            onMyWorkoutItemRefresh(workoutModels)
        } ?: run {
            Log.e("YourworkOuts", "No WorkoutSessionRecord received")
            onMyWorkoutItemRefresh(arrayListOf()) // Show empty state if no data
        }
        yourWorkOutsBackButton.setOnClickListener {
            val fragment = AddWorkoutSearchFragment()
            val args = Bundle().apply {

                putParcelable("workout", workout)
                putParcelable("workoutRecord", workoutRecord)// Pass the full record
            }
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "YourworkOutsFragment")
                addToBackStack("YourworkOutsFragment")
                commit()
            }
            // navigateToFragment(AddWorkoutSearchFragment(), "LandingFragment")
        }
        // Comment out the API call
        /*
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
        */

        // Set up other UI components
        onMealLogDateItemRefresh()

        saveWorkoutRoutine.setOnClickListener {
            val fragment = CreateRoutineFragment()
            val args = Bundle().apply {
                putParcelable("workout", workout)
                putParcelable("workoutRecord", workoutRecord)
            }
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "YourworkOutsFragment")
                addToBackStack("YourworkOutsFragment")
                commit()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = AddWorkoutSearchFragment()
                val args = Bundle().apply {
                    putParcelable("workout", workout)
                }
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment, "YourworkOutsFragment")
                    addToBackStack("YourworkOutsFragment")
                    commit()
                }
            }
        })
    }

    private fun convertToWorkoutModel(record: WorkoutSessionRecord): WorkoutModel {
        // Calculate duration in "X hr Y min" format
        val hours = record.durationMin / 60
        val minutes = record.durationMin % 60
        val durationText = buildString {
            if (hours > 0) append("$hours hr ")
            append("$minutes min")
        }

        // Use intensity from record
        val intensity = when (record.intensity.lowercase()) {
            "high" -> "High Intensity"
            "moderate" -> "Moderate Intensity"
            else -> "Low Intensity"
        }

        // Use workout type as "Custom Workout" (since activityId isn't mapped to a name here)
        val workoutType = record.moduleName

        // Calories burned or "N/A" if not calculated
        val caloriesBurned = record.caloriesBurned?.let { "${it.toInt()} cal" } ?: "N/A"

        return WorkoutModel(
            workoutType = workoutType,
            duration = durationText,
            caloriesBurned = caloriesBurned,
            intensity = intensity
        )
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
        Log.d("WorkoutClick", "Clicked on ${workoutModel.workoutType} at position $position")
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    /*
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
    */
}

data class WorkoutModel(
    val workoutType: String, // e.g., "Running", "Basketball"
    val duration: String, // e.g., "1 hr 30 min"
    val caloriesBurned: String, // e.g., "337 cal"
    val intensity: String // e.g., "Low Intensity"
)