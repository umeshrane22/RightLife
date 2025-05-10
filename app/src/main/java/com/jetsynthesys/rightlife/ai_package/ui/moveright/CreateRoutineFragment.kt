package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.graphics.drawable.Drawable
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
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.BundleCompat
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.AssignRoutineRequest
import com.jetsynthesys.rightlife.ai_package.model.AssignRoutineResponse
import com.jetsynthesys.rightlife.ai_package.model.CreateRoutineRequest
import com.jetsynthesys.rightlife.ai_package.model.RoutineWorkoutDisplayModel
import com.jetsynthesys.rightlife.ai_package.model.WorkoutList
import com.jetsynthesys.rightlife.ai_package.model.WorkoutResponseRoutine
import com.jetsynthesys.rightlife.ai_package.model.WorkoutSessionRecord
import com.jetsynthesys.rightlife.ai_package.model.request.CreateWorkoutRequest
import com.jetsynthesys.rightlife.ai_package.ui.adapter.CreateRoutineListAdapter
import com.jetsynthesys.rightlife.ai_package.ui.adapter.RoutineWorkoutListAdapter
import com.jetsynthesys.rightlife.databinding.FragmentCreateRoutineBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class CreateRoutineFragment : BaseFragment<FragmentCreateRoutineBinding>() {
    private lateinit var editText: EditText
    private lateinit var textViewRoutine: TextView
    private lateinit var createRoutineBackButton: ImageView
    private lateinit var createRoutineRecyclerView: RecyclerView
    private lateinit var layoutBtnLog: LinearLayoutCompat
    private lateinit var addBtnLog: LinearLayoutCompat
    private lateinit var save_workout_routine_btn: LinearLayoutCompat
    private lateinit var addNameLayout: ConstraintLayout
    private lateinit var createListRoutineLayout: ConstraintLayout
    private var workout: WorkoutList? = null
    private var workoutRecord: WorkoutSessionRecord? = null
    private var myroutine: String = ""
    private var routine: String = ""
    private var routineName: String = ""
    private var workoutList = ArrayList<WorkoutSessionRecord>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateRoutineBinding
        get() = FragmentCreateRoutineBinding::inflate

    private val routineWorkoutListAdapter by lazy {
        RoutineWorkoutListAdapter(
            requireContext(),
            arrayListOf(),
            ::onWorkoutItemClick
        )
    }

    private val myRoutineListAdapter by lazy {
        CreateRoutineListAdapter(
            context = requireContext(),
            dataLists = arrayListOf(),
            clickPos = -1,
            selectedWorkout = null,
            isClickView = false,
            onWorkoutItemClick = { workout, position, isClicked ->
                Log.d("RoutineAdapter", "Workout clicked: ${workout.workoutType} at position $position")
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(R.drawable.gradient_color_background_workout)
        routine = arguments?.getString("routine").toString()
        routineName = arguments?.getString("routineName").toString()
        workoutList = arguments?.getParcelableArrayList("workoutList") ?: ArrayList()

        createRoutineRecyclerView = view.findViewById(R.id.recyclerview_my_meals_item)
        editText = view.findViewById(R.id.editText)
        textViewRoutine = view.findViewById(R.id.name_routine_text_view)
        layoutBtnLog = view.findViewById(R.id.layout_btn_log)
        createRoutineBackButton = view.findViewById(R.id.back_button)
        addNameLayout = view.findViewById(R.id.add_name_layout)
        addBtnLog = view.findViewById(R.id.add_btn_log)
        save_workout_routine_btn = view.findViewById(R.id.save_workout_routine_btn)
        createListRoutineLayout = view.findViewById(R.id.list_create_routine_layout)

        // Show/hide layouts based on routine mode
        if (routine == "routine") {
            if (workoutList.isNotEmpty()) {
                addNameLayout.visibility = View.GONE
                createListRoutineLayout.visibility = View.VISIBLE
                textViewRoutine.text = routineName
                // Map workoutList to RoutineWorkoutDisplayModel and update the adapter
                val routineWorkoutModels = mapWorkoutSessionRecordsToRoutineWorkoutModels(workoutList)
                routineWorkoutListAdapter.setData(routineWorkoutModels)
                createRoutineRecyclerView.visibility = View.VISIBLE
            } else {
                addNameLayout.visibility = View.GONE
                createListRoutineLayout.visibility = View.VISIBLE
                textViewRoutine.text = routineName
                createRoutineRecyclerView.visibility = View.GONE
            }
        } else {
            addNameLayout.visibility = View.VISIBLE
            createListRoutineLayout.visibility = View.GONE
        }

        addBtnLog.setOnClickListener {
            val fragment = SearchWorkoutFragment()
            val args = Bundle().apply {
                putString("routine", "routine")
                putString("routineName", textViewRoutine.text.toString())
                putParcelableArrayList("workoutList", workoutList)
            }
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "SearchWorkoutFragment")
                addToBackStack(null)
                commit()
            }
        }

        save_workout_routine_btn.setOnClickListener {
            if (textViewRoutine.text.isNotEmpty()) {
                if (workoutList.isNotEmpty()) {
                    createRoutine(textViewRoutine.text.toString())
                } else {
                    Toast.makeText(requireContext(), "No workouts added to the routine", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Routine name is empty", Toast.LENGTH_SHORT).show()
            }
        }

        val defaultBackground: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.add_cart_button_background)
        val filledBackground: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.button_background_filled)

        createRoutineRecyclerView.layoutManager = LinearLayoutManager(context)
        createRoutineRecyclerView.adapter = routineWorkoutListAdapter

        createRoutineBackButton.setOnClickListener {
            navigateToFragment(YourActivityFragment(), "AllWorkoutFragment")
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    layoutBtnLog.background = filledBackground
                    layoutBtnLog.isEnabled = false
                } else {
                    layoutBtnLog.background = defaultBackground
                    layoutBtnLog.isEnabled = true
                }
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setFragmentResult("workoutListUpdate", Bundle().apply {
                    putParcelableArrayList("workoutList", workoutList)
                })
                navigateToFragment(YourActivityFragment(), "AllWorkoutFragment")
            }
        })

        layoutBtnLog.setOnClickListener {
            addNameLayout.visibility = View.GONE
            createListRoutineLayout.visibility = View.VISIBLE
            textViewRoutine.text = editText.text
        }
    }

    // Function to map WorkoutSessionRecord to RoutineWorkoutDisplayModel
    private fun mapWorkoutSessionRecordsToRoutineWorkoutModels(records: ArrayList<WorkoutSessionRecord>): ArrayList<RoutineWorkoutDisplayModel> {
        return records.map { record ->
            RoutineWorkoutDisplayModel(
                name = record.moduleName ?: "Unknown Workout",
                duration = "${record.durationMin} min",
                caloriesBurned = record.caloriesBurned?.toInt().toString() ?: "N/A",
                intensity = record.intensity
            )
        }.toCollection(ArrayList())
    }

    private fun onWorkoutItemClick(workoutModel: RoutineWorkoutDisplayModel, position: Int) {
        Log.d("WorkoutClick", "Clicked on ${workoutModel.name} at position $position")
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun createRoutine(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userid = SharedPreferenceManager.getInstance(requireActivity()).userId
                    ?: "64763fe2fa0e40d9c0bc8264"

                // Map workoutList to CreateRoutineRequest.Workout
                val workoutRequests = workoutList.map { record ->
                    CreateRoutineRequest.Workout(
                        activityId = record.activityId ?: "",
                        duration = record.durationMin,
                        intensity = record.intensity
                    )
                }

                val request = CreateRoutineRequest(
                    user_id = userid,
                    routine_name = name,
                    workouts = workoutRequests
                )

                val response = ApiClient.apiServiceFastApi.createRoutine(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Toast.makeText(
                            requireContext(),
                            responseBody?.message ?: "Routine created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToFragment(YourActivityFragment(), "YourActivityFragment")
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error creating routine: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Exception: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun createWorkout(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = SharedPreferenceManager.getInstance(requireActivity()).userId
                    ?: "64763fe2fa0e40d9c0bc8264"

                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val request = workoutRecord?.let {
                    CreateWorkoutRequest(
                        userId = userId,
                        activityId = it.activityId,
                        durationMin = it.durationMin,
                        intensity = it.intensity,
                        sessions = 1,
                        date = currentDate
                    )
                } ?: run {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Workout data is missing", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val response = ApiClient.apiServiceFastApi.createWorkout(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            Toast.makeText(requireContext(), "Workout Created Successfully", Toast.LENGTH_SHORT).show()
                        } ?: Toast.makeText(
                            requireContext(),
                            "Empty response",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error creating workout: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Exception: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun assignWorkoutRoutine() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val id: String = "67e1317722a2406f2eceb627"
                val userId: String = "64763fe2fa0e40d9c0bc8264"
                val routineName: String = editText.text.toString()
                val workoutIds: List<String> = workoutList.mapNotNull { it.activityId }
                val date: String = "2025-03-24"

                val request = AssignRoutineRequest(
                    routineName = routineName,
                    workoutIds = workoutIds,
                    date = date
                )
                val response: Response<AssignRoutineResponse> = ApiClient.apiServiceFastApi.assignRoutine(
                    id = id,
                    userId = userId,
                    request = request
                )

                if (response.isSuccessful) {
                    val assignResponse: AssignRoutineResponse? = response.body()
                    if (assignResponse != null) {
                        withContext(Dispatchers.Main) {
                            Log.d("AssignRoutine", "Success: ${assignResponse.message}")
                            Log.d("AssignRoutine", "Document ID: ${assignResponse.documentId}")
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Log.e("AssignRoutine", "Response body is null")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        val errorMessage = "Error: ${response.code()} - ${response.message()}"
                        Log.e("AssignRoutine", errorMessage)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("AssignRoutine", "Exception: ${e.message}", e)
                }
            }
        }
    }


}