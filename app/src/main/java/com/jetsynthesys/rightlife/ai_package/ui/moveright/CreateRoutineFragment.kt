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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.AssignRoutineRequest
import com.jetsynthesys.rightlife.ai_package.model.AssignRoutineResponse
import com.jetsynthesys.rightlife.ai_package.model.CreateRoutineRequest
import com.jetsynthesys.rightlife.ai_package.model.WorkoutList
import com.jetsynthesys.rightlife.ai_package.model.WorkoutResponseRoutine
import com.jetsynthesys.rightlife.ai_package.model.WorkoutSessionRecord
import com.jetsynthesys.rightlife.ai_package.ui.adapter.CreateRoutineListAdapter
import com.jetsynthesys.rightlife.databinding.FragmentCreateRoutineBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CreateRoutineFragment : BaseFragment<FragmentCreateRoutineBinding>() {
    private lateinit var editText: EditText
    private lateinit var textViewRoutine: TextView
    private lateinit var createRoutineBackButton: ImageView
    private lateinit var createRoutineRecyclerView: RecyclerView
    private lateinit var layoutBtnLog: LinearLayoutCompat
    private lateinit var addNameLayout: ConstraintLayout
    private lateinit var createListRoutineLayout: ConstraintLayout
    private var workout: WorkoutList? = null
    private var workoutRecord: WorkoutSessionRecord? = null


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateRoutineBinding
        get() = FragmentCreateRoutineBinding::inflate

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

        createRoutineRecyclerView = view.findViewById(R.id.recyclerview_my_meals_item)
        editText = view.findViewById(R.id.editText)
        textViewRoutine = view.findViewById(R.id.name_routine_text_view)
        layoutBtnLog = view.findViewById(R.id.layout_btn_log)
        createRoutineBackButton = view.findViewById(R.id.back_button)
        addNameLayout = view.findViewById(R.id.add_name_layout)
        createListRoutineLayout = view.findViewById(R.id.list_create_routine_layout)
        workout = arguments?.getParcelable("workout")
         workoutRecord =
            arguments?.let { BundleCompat.getParcelable(it, "workoutRecord", WorkoutSessionRecord::class.java) }

        addNameLayout.visibility = View.VISIBLE
        createListRoutineLayout.visibility = View.GONE
        //deleteCalorieRecord()
       // updateCalorieRecord()


        val defaultBackground: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.add_cart_button_background)
        val filledBackground: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.button_background_filled)

        createRoutineRecyclerView.layoutManager = LinearLayoutManager(context)
        createRoutineRecyclerView.adapter = myRoutineListAdapter

        createRoutineBackButton.setOnClickListener {
           // navigateToFragment(YourworkOutsFragment(), "LandingFragment")
            val fragment = YourworkOutsFragment()
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
               // navigateToFragment(YourworkOutsFragment(), "LandingFragment")
                val fragment = YourworkOutsFragment()
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
            }
        })

        layoutBtnLog.setOnClickListener {
            addNameLayout.visibility = View.GONE
            createListRoutineLayout.visibility = View.VISIBLE
            textViewRoutine.text = editText.text
            createRoutine(editText.text.toString())
            // assignWorkoutRoutine() // Assign the routine first
            //fetchMoveRoutine()     // Then fetch the updated routine data
        }
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun createRoutine(name:String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userid = SharedPreferenceManager.getInstance(requireActivity()).userId
                    ?: "64763fe2fa0e40d9c0bc8264"

                // Map data to CreateRoutineRequest
                val request = CreateRoutineRequest(
                    user_id = userid,
                    routine_name = name,
                    workouts = listOf(

                        workoutRecord?.let {
                            CreateRoutineRequest.Workout(
                                activityId = it.activityId ,
                                duration = workoutRecord!!.durationMin.toInt(),
                                intensity = workoutRecord!!.intensity
                            )
                        }
                    )
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
                        //fetchMoveRoutine()
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

    private fun assignWorkoutRoutine() {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val id: String = "67e1317722a2406f2eceb627"
                val userId: String = "64763fe2fa0e40d9c0bc8264"
                val routineName: String = editText.text.toString()
                val workoutIds: List<String> = listOf("67e1317722a2406f2eceb63a")
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



    private fun fetchMoveRoutine() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userid = SharedPreferenceManager.getInstance(requireActivity()).userId
                    ?: "64763fe2fa0e40d9c0bc8264"
                val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                val response = ApiClient.apiServiceFastApi.getMoveRoutine(
                    userId = userid,
                   // providedDate = "2025-03-24"
                )

                if (response.isSuccessful) {
                    val workoutResponse: WorkoutResponseRoutine? = response.body()
                    workoutResponse?.let { routineResponse ->
                        // Flatten the list of workouts from all routines in the map
                        val allWorkouts = routineResponse.routines.values.flatMap { it.workouts }.toCollection(ArrayList())
                        withContext(Dispatchers.Main) {
                            Log.d("FetchMoveRoutine", "Workout Routines Fetched Successfully: ${allWorkouts.size} workouts")
                            // Update the RecyclerView adapter with the workout data
                            myRoutineListAdapter.addAll(allWorkouts, -1, null, false)
                            createRoutineRecyclerView.visibility = if (allWorkouts.isNotEmpty()) View.VISIBLE else View.GONE
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            Log.e("FetchMoveRoutine", "Response body is null")
                            createRoutineRecyclerView.visibility = View.GONE
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e("FetchMoveRoutine", "Error: ${response.code()} - ${response.message()}")
                        createRoutineRecyclerView.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("FetchMoveRoutine", "Exception: ${e.message}", e)
                    createRoutineRecyclerView.visibility = View.GONE
                }
            }
        }
    }
}