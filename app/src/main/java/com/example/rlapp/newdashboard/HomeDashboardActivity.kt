package com.example.rlapp.newdashboard

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.ai_package.ui.MainAIActivity
import com.example.rlapp.ai_package.ui.sleepright.fragment.SleepSegmentModel
import com.example.rlapp.apimodel.newreportfacescan.FacialReportResponseNew
import com.example.rlapp.apimodel.userdata.UserProfileResponse
import com.example.rlapp.databinding.ActivityHomeDashboardBinding
import com.example.rlapp.newdashboard.NewHomeFragment.HomeFragment
import com.example.rlapp.newdashboard.model.AiDashboardResponseMain
import com.example.rlapp.newdashboard.model.ChecklistResponse
import com.example.rlapp.ui.HomeActivity
import com.example.rlapp.ui.NewSleepSounds.NewSleepSoundActivity
import com.example.rlapp.ui.affirmation.TodaysAffirmationActivity
import com.example.rlapp.ui.breathwork.BreathworkActivity
import com.example.rlapp.ui.healthcam.HealthCamActivity
import com.example.rlapp.ui.healthcam.NewHealthCamReportActivity
import com.example.rlapp.ui.jounal.new_journal.JournalListActivity
import com.example.rlapp.ui.profile_new.ProfileSettingsActivity
import com.example.rlapp.ui.questionnaire.QuestionnaireEatRightActivity
import com.example.rlapp.ui.questionnaire.QuestionnaireThinkRightActivity
import com.example.rlapp.ui.utility.SharedPreferenceConstants
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class HomeDashboardActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var binding: ActivityHomeDashboardBinding
    private var isAdd = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default fragment
        loadFragment(HomeFragment())

        //set report list dummy for demo
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val heartRateList = mutableListOf<HeartRateData>()
        for (i in 1..7) {
            heartRateList.add(
                HeartRateData(
                    heartRate = (60..120).random(),  // Random heart rate
                    date = "Week $i",
                    //trendData = (60..120).map { it.toString() }.take(7) as ArrayList<String>
                    trendData = ArrayList(List(7) { (60..120).random().toString() })  // U
                )
            )
        }

        /*    recyclerView.adapter = HeartRateAdapter(
                heartRateList,
                this
            )*/

        //handle bottom menu

        with(binding) {
            binding.includedhomebottomsheet.llJournal.setOnClickListener { startActivity(Intent(this@HomeDashboardActivity, JournalListActivity::class.java)) }
            binding.includedhomebottomsheet.llAffirmations.setOnClickListener { startActivity(Intent(this@HomeDashboardActivity, TodaysAffirmationActivity::class.java)) }
            binding.includedhomebottomsheet.llSleepsounds.setOnClickListener { startActivity(Intent(this@HomeDashboardActivity, NewSleepSoundActivity::class.java)) }
            binding.includedhomebottomsheet.llBreathwork.setOnClickListener { startActivity(Intent(this@HomeDashboardActivity, BreathworkActivity::class.java)) }
            binding.includedhomebottomsheet.llHealthCamQl.setOnClickListener { getMyRLHealthCamResult() }
            binding.includedhomebottomsheet.llMealplan.setOnClickListener {
                Toast.makeText(this@HomeDashboardActivity, "Meal Plan Coming Soon...", Toast.LENGTH_LONG).show()
            }
        }

        val todayDate = SimpleDateFormat(
            "EEEE, d MMMM",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)


        // Set to TextView
        binding.tvDateDashboard.text = todayDate

        // Handle menu item clicks
        binding.menuHome.setOnClickListener {
            //loadFragment(HomeFragment())
            updateMenuSelection(R.id.menu_home)


        }

        binding.menuExplore.setOnClickListener {
            //loadFragment(ExploreFragment())
            startActivity(Intent(this, HomeActivity::class.java))
            updateMenuSelection(R.id.menu_explore)
        }

        // Set initial selection
        updateMenuSelection(R.id.menu_home)

        // Handle FAB click


        // Handle FAB click
        binding.fab.setBackgroundTintList(
            ContextCompat.getColorStateList(
                this,
                android.R.color.white
            )
        )
        binding.fab.setImageTintList(ColorStateList.valueOf(resources.getColor(R.color.black)))
        val bottom_sheet = binding.includedhomebottomsheet.bottomSheet
        binding.fab.setOnClickListener { v ->
            if (binding.includedhomebottomsheet.bottomSheet.getVisibility() == View.VISIBLE) {
                bottom_sheet.setVisibility(View.GONE)
                binding.iconHome.setBackgroundResource(R.drawable.homeselected)
                binding.labelHome.setTextColor(resources.getColor(R.color.menuselected))
                val typeface =
                    ResourcesCompat.getFont(this, R.font.dmsans_bold)
                binding.labelHome.setTypeface(typeface)
            } else {
                bottom_sheet.setVisibility(View.VISIBLE)
                //binding.iconHome.setBackgroundColor(Color.TRANSPARENT)
                //binding.labelHome.setTextColor(resources.getColor(R.color.txt_color_header))
                val typeface =
                    ResourcesCompat.getFont(this, R.font.dmsans_regular)
                //binding.labelHome.setTypeface(typeface)
            }
            v.setSelected(!v.isSelected())

            /*BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());*/
            binding.fab.animate().rotationBy(180f).setDuration(60)
                .setInterpolator(DecelerateInterpolator()).withEndAction {
                    // Change icon after rotation
                    if (isAdd) {
                        binding.fab.setImageResource(R.drawable.icon_quicklink_plus_black) // Change to close icon
                        binding.fab.setBackgroundTintList(
                            ContextCompat.getColorStateList(
                                this,
                                R.color.rightlife
                            )
                        )
                        binding.fab.setImageTintList(
                            ColorStateList.valueOf(
                                resources.getColor(
                                    R.color.black
                                )
                            )
                        )
                    } else {
                        binding.fab.setImageResource(R.drawable.icon_quicklink_plus) // Change back to add icon
                        binding.fab.setBackgroundTintList(
                            ContextCompat.getColorStateList(
                                this,
                                R.color.white
                            )
                        )
                        binding.fab.setImageTintList(
                            ColorStateList.valueOf(
                                resources.getColor(
                                    R.color.rightlife
                                )
                            )
                        )
                    }
                    isAdd = !isAdd // Toggle the state
                }.start()
        }


        /*     binding.fab.backgroundTintList =
                 ContextCompat.getColorStateList(this, android.R.color.white)
             binding.fab.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
             binding.fab.setOnClickListener {
                 *//*val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)*//*
            binding.fab.animate().rotationBy(180f).setDuration(60)
                .setInterpolator(DecelerateInterpolator()).withEndAction {
                    // Change icon after rotation
                    if (isAdd) {
                        binding.fab.setImageResource(R.drawable.icon_quicklink_plus_black)  // Change to close icon
                        binding.fab.backgroundTintList =
                            ContextCompat.getColorStateList(this, R.color.rightlife)
                        binding.fab.imageTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.black))
                    } else {
                        binding.fab.setImageResource(R.drawable.icon_quicklink_plus)    // Change back to add icon
                        binding.fab.backgroundTintList =
                            ContextCompat.getColorStateList(this, R.color.white)
                        binding.fab.imageTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.rightlife))
                    }
                    isAdd = !isAdd  // Toggle the state
                }.start()
        }*/
        // Api calls
        getUserDetails("")

        getAiDashboard("")
        getDashboardChecklist("")

        binding.progressBarOnboarding.post {
            val progressPercentage =
                binding.progressBarOnboarding.progress / binding.progressBarOnboarding.max.toFloat()
            val progressWidth = binding.progressBarOnboarding.width + 80
            val thumbX = (progressPercentage) * progressWidth - binding.progressThumb.width / 2
            binding.progressThumb.translationX = thumbX
            binding.tvWeightlossZone.translationX = thumbX
        }

        // click listners for checklist
        binding.includeChecklist.rlChecklistEatright.setOnClickListener {
            //Toast.makeText(this, "Eat Right", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, QuestionnaireEatRightActivity::class.java))
        }
        binding.includeChecklist.rlChecklistSleepright.setOnClickListener {
            //Toast.makeText(this, "Think Right", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, QuestionnaireThinkRightActivity::class.java))
        }
        binding.includeChecklist.rlChecklistSynchealth.setOnClickListener {
            Toast.makeText(this, "Sync Health", Toast.LENGTH_SHORT).show()
        }
        binding.includeChecklist.rlChecklistProfile.setOnClickListener {
            //Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, QuestionnaireThinkRightActivity::class.java))
        }
        binding.includeChecklist.rlChecklistSnapmeal.setOnClickListener {
            Toast.makeText(this, "Snap Meal", Toast.LENGTH_SHORT).show()
        }
        binding.includeChecklist.rlChecklistFacescan.setOnClickListener {
            //Toast.makeText(this, "Face Scan", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@HomeDashboardActivity, HealthCamActivity::class.java))
        }
        binding.profileImage.setOnClickListener {

            /*if (!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
                else drawer.closeDrawer(Gravity.RIGHT);*/
            startActivity(Intent(this@HomeDashboardActivity, ProfileSettingsActivity::class.java))
        }

        binding.cardThinkrightMain.setOnClickListener {
            startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                putExtra("ModuleName", "ThinkRight")
            })
            Toast.makeText(this, "MoveRight AI Dashboard", Toast.LENGTH_SHORT).show()
        }
        binding.cardEatrightMain.setOnClickListener {
            startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                putExtra("ModuleName", "EatRight")
            })
            Toast.makeText(this, "MoveRight AI Dashboard", Toast.LENGTH_SHORT).show()
        }

        binding.cardMoverightMain.setOnClickListener {
            startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                putExtra("ModuleName", "MoveRight")
            })
        }
        binding.cardEatright.setOnClickListener {
            startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                putExtra("ModuleName", "EatRight")
            })
        }
        binding.cardSleepright.setOnClickListener {
            startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                putExtra("ModuleName", "SleepRight")
            })
        }
        binding.cardThinkright.setOnClickListener {
            startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java)
                .apply {
                    putExtra("ModuleName", "ThinkRight")
                })
        }
        binding.cardMoveright.setOnClickListener {
            startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java)
                .apply {
                    putExtra("ModuleName", "MoveRight")
                })
        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun updateMenuSelection(selectedMenuId: Int) {
        // Reset all icons and labels
        binding.iconHome.setImageResource(R.drawable.new_home_unselected_svg) // Unselected icon
        binding.iconExplore.setImageResource(R.drawable.new_explore_unselected_svg) // Unselected icon
        binding.labelHome.setTextColor(ContextCompat.getColor(this, R.color.gray))
        binding.labelExplore.setTextColor(ContextCompat.getColor(this, R.color.gray))
        binding.labelHome.setTypeface(null, Typeface.NORMAL) // Reset to normal font
        binding.labelExplore.setTypeface(null, Typeface.NORMAL) // Reset to normal font

        // Highlight selected icon and label
        when (selectedMenuId) {
            R.id.menu_home -> {
                binding.iconHome.setImageResource(R.drawable.new_home_selected_svg) // Selected icon
                binding.labelHome.setTextColor(ContextCompat.getColor(this, R.color.rightlife))
                binding.labelHome.setTypeface(null, Typeface.BOLD) // Make text bold
            }

            R.id.menu_explore -> {
                binding.iconExplore.setImageResource(R.drawable.new_explore_selected_svg) // Selected icon
                binding.labelExplore.setTextColor(ContextCompat.getColor(this, R.color.rightlife))
                binding.labelExplore.setTypeface(null, Typeface.BOLD) // Make text bold
            }
        }
    }


    //APIs

    // get user details
    private fun getUserDetails(s: String) {
        //-----------
        val sharedPreferences =
            getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null)

        val apiService = ApiClient.getClient().create(ApiService::class.java)

        // Create a request body (replace with actual email and phone number)
        // SignupOtpRequest request = new SignupOtpRequest("+91"+mobileNumber);

        // Make the API call
        val call = apiService.getUserDetais(accessToken)
        call.enqueue(object : Callback<JsonElement?> {
            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                if (response.isSuccessful && response.body() != null) {
                    val promotionResponse2 = response.body()
                    Log.d("API Response", "User Details: " + promotionResponse2.toString())
                    val gson = Gson()
                    val jsonResponse = gson.toJson(response.body())

                    val ResponseObj = gson.fromJson(
                        jsonResponse,
                        UserProfileResponse::class.java
                    )
                    Log.d("API Response body", "Success: User Details" + ResponseObj.userdata.id)
                    SharedPreferenceManager.getInstance(applicationContext)
                        .saveUserId(ResponseObj.userdata.id)
                    SharedPreferenceManager.getInstance(applicationContext)
                        .saveUserProfile(ResponseObj)


                    if (ResponseObj.userdata.profilePicture != null) {
                        Glide.with(this@HomeDashboardActivity)
                            .load(ApiClient.CDN_URL_QA + ResponseObj.userdata.profilePicture)
                            .placeholder(R.drawable.profile_man).error(R.drawable.profile_man)
                            .into(binding.profileImage)
                    }
                    binding.userName.text = ResponseObj.userdata.firstName


                    Log.d(
                        "UserID", "USerID: User Details" + SharedPreferenceManager.getInstance(
                            applicationContext
                        ).userId
                    )
                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Toast.makeText(
                    this@HomeDashboardActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.e("API ERROR", "onFailure: " + t.message)
                t.printStackTrace() // Print the full stack trace for more details
            }
        })
    }


    // get AI Dashboard Data
    private fun getAiDashboard(s: String) {
        //-----------
        val sharedPreferences =
            getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null)

        val apiService = ApiClient.getClient().create(ApiService::class.java)

        // Make the API call
        val call = apiService.getAiDashboard(accessToken)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    val promotionResponse2 = response.body()!!.string()


                    val gson = Gson()


                    val aiDashboardResponseMain = gson.fromJson(
                        promotionResponse2,
                        AiDashboardResponseMain::class.java
                    )
                    Log.d(
                        "dashboard",
                        "Success: Scan Details" + aiDashboardResponseMain.data?.facialScan?.get(0)!!.avgParameter
                    )
                    handleSelectedModule(aiDashboardResponseMain)
                    binding.recyclerView.adapter = HeartRateAdapter(
                        aiDashboardResponseMain.data.facialScan,
                        this@HomeDashboardActivity
                    )
                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(
                    this@HomeDashboardActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.e("API ERROR", "onFailure: " + t.message)
                t.printStackTrace() // Print the full stack trace for more details
            }
        })
    }

    /*    fun extractValues(input: String): Pair<String, String> {
            val parts = input.split(" / ")
            return if (parts.size == 2) {
                Pair(parts[0].trim(), parts[1].trim())
            } else {
                Pair(input, "") // Handle cases where `/` is missing
            }
        }*/
    /*fun extractNumericValues(input: String): Pair<String, String> {
        val parts = input.split(" / ")
        val firstValue = parts[0].trim()  // First value before '/'
        val secondValue = parts.getOrNull(1)?.trim()?.replace(Regex("[^0-9]"), "") ?: "" // Remove non-numeric characters

        return Pair(firstValue, secondValue)
    }*/

    fun extractNumericValues(input: String): Pair<String, String> {
        val parts = input.split("/") // Splitting by '/'

        if (parts.size < 2) {
            throw IllegalArgumentException("Invalid format: $input")
        }

        val firstValue = parts[0].trim()
        val secondValue = parts[1].trim().replace(Regex("[^0-9]"), "")

        return Pair(firstValue, secondValue)
    }

    fun calculatePercentage(current: Int, total: Int): Int {
        if (total == 0) return 0 // Avoid division by zero
        return ((current.toFloat() / total.toFloat()) * 100).toInt()
    }


    private fun handleSelectedModule(aiDashboardResponseMain: AiDashboardResponseMain?) {
        for (module in aiDashboardResponseMain?.data?.updatedModules!!) {
            val moduleId = module.moduleId
            val isSelected = module.isSelectedModule


            when (moduleId) {
                "MOVE_RIGHT" -> {

                    if (isSelected == true) {
                        binding.cardMoverightMain.visibility = View.VISIBLE
                        binding.cardMoveright.visibility = View.GONE
                    }
                    //set data on card once resposne works
                    binding.tvCaloryValue.text = module.calorieBalance.toString()
                    binding.tvCaloryIntake.text = module.intake.toString()
                    binding.tvCaloryBurn.text = module.burned.toString()

                    binding.tvModuleValueEatright.text = module.calories.toString()
                    binding.tvModuleValueSleepright.text = module.sleepDuration.toString()
                    binding.tvModuleValueThinkright.text = module.mindfulTime.toString()
                }

                "THINK_RIGHT" -> {
                    if (isSelected == true) {
                        binding.cardThinkrightMain.visibility = View.VISIBLE
                        binding.cardThinkright.visibility = View.GONE
                    }
                    binding.tvMinutesTextValue.setText(module.mindfulnessMinutes)
                    binding.tvDaysTextValue.setText(module.wellnessDays)
                }

                "EAT_RIGHT" -> {
                    if (isSelected == true) {
                        binding.cardEatrightMain.visibility = View.VISIBLE
                        binding.cardEatrightMain.visibility = View.GONE
                    }

                    val (proteinValue, proteinTotal) = extractNumericValues(module.protein.toString())
                    val (carbsValue, carbsTotal) = extractNumericValues(module.carbs.toString())
                    val (fatsValue, fatsTotal) = extractNumericValues(module.fats.toString())

                    binding.tvSubtractionCalValue.text = proteinValue
                    binding.tvSubtractionCalUnit.text = "/" + proteinTotal + " g"
                    binding.carbsProgressBar.max = proteinTotal.toInt()
                    binding.carbsProgressBar.progress = proteinValue.toInt()


                    binding.tvSubtractionCarbsValue.text = carbsValue
                    binding.tvSubtractionCarbsUnit.text = "/" + carbsTotal + " g"
                    binding.protienProgressBar.max = carbsTotal.toInt()
                    binding.protienProgressBar.progress = carbsValue.toInt()

                    binding.tvSubtractionFatsValue.text = fatsValue
                    binding.tvSubtractionFatsUnit.text = "/" + fatsTotal + " g"
                    binding.fatsProgressBar.max = fatsTotal.toInt()
                    binding.fatsProgressBar.progress = fatsValue.toInt()


                    //binding.halfCurveProgressBar.setProgress(60f)
                    val (curent, max) = extractNumericValues(module.calories.toString())
                    binding.halfCurveProgressBar.setValues(curent.toInt(), max.toInt())
                    val percentage = calculatePercentage(curent.toInt(), max.toInt())
                    binding.halfCurveProgressBar.setProgress(percentage.toFloat())
                }

                "SLEEP_RIGHT" -> {
                    if (isSelected == true) {
                        binding.cardSleeprightMain.visibility = View.VISIBLE
                        binding.cardSleepright.visibility = View.GONE
                    }
                    binding.tvRem.text = module.rem.toString()
                    binding.tvCore.text = module.core.toString()
                    binding.tvDeep.text = module.deep.toString()
                    binding.tvAwake.text = module.awake.toString()
                    binding.tvSleepTime.text = module.sleepTime.toString()
                    binding.tvWakeupTime.text = module.wakeUpTime.toString()

                    val sleepData = listOf(
                        SleepSegmentModel(0.001f, 0.100f, resources.getColor(R.color.blue_bar), 110f),
                        SleepSegmentModel(0.101f, 0.150f, resources.getColor(R.color.blue_bar), 110f),
                        SleepSegmentModel(0.151f, 0.300f, resources.getColor(R.color.purple_bar), 110f),
                        SleepSegmentModel(0.301f, 0.400f, resources.getColor(R.color.light_blue_bar), 110f),
                        SleepSegmentModel(0.401f, 0.450f, resources.getColor(R.color.black), 110f),
                        SleepSegmentModel(0.451f, 0.550f, resources.getColor(R.color.red_orange_bar), 110f),
                        SleepSegmentModel(0.551f, 0.660f, resources.getColor(R.color.light_cyan_bar), 110f),
                        SleepSegmentModel(0.661f, 0.690f, resources.getColor(R.color.bright_blue_bar), 110f),
                        SleepSegmentModel(0.691f, 0.750f, resources.getColor(R.color.deep_purple_bar), 110f),
                        SleepSegmentModel(0.751f, 0.860f, resources.getColor(R.color.sky_blue_bar), 110f),
                        SleepSegmentModel(0.861f, 0.990f, resources.getColor(R.color.dark_purple_bar), 110f)
                    )

                    binding.sleepStagesView.setSleepData(sleepData)

                }

                else -> {
                    binding.cardMoverightMain.visibility = View.VISIBLE
                    binding.cardMoveright.visibility = View.GONE
                }
            }


        }
    }
    /*private fun dummyLogic(aiDashboardResponseMain: AiDashboardResponseMain?) {
        for () {
            val moduleId = module.moduleId
            val isSelected = module.isSelectedModule

            if (isSelected == true) {
                when (moduleId) {
                    "MOVE_RIGHT" -> {
                        binding.cardMoverightMain.visibility = View.VISIBLE
                        binding.cardMoveright.visibility = View.GONE
                        //set data on card once resposne works
                    }

                    "THINK_RIGHT" -> {
                        binding.cardThinkrightMain.visibility = View.VISIBLE
                        binding.cardThinkright.visibility = View.GONE
                    }

                    "EAT_RIGHT" -> {
                        binding.cardEatrightMain.visibility = View.VISIBLE
                        binding.cardEatrightMain.visibility = View.GONE
                    }

                    "SLEEP_RIGHT" -> {
                        binding.cardSleeprightMain.visibility = View.VISIBLE
                        binding.cardSleepright.visibility = View.GONE
                    }

                    else -> {
                        binding.cardMoverightMain.visibility = View.VISIBLE
                        binding.cardMoveright.visibility = View.GONE
                    }
                }
            }

        }
    }*/


    //getDashboardChecklist
    private fun getDashboardChecklist(s: String) {
        //-----------
        val sharedPreferences =
            getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null)

        val apiService = ApiClient.getClient().create(ApiService::class.java)

        // Make the API call
        val call = apiService.getDashboardChecklist(accessToken)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    val promotionResponse2 = response.body()!!.string()
                    Log.d("API Response", "User Details: " + promotionResponse2.toString())
                    val gson = Gson()
                    val jsonResponse = gson.toJson(response.body())

                    val checklistResponse = gson.fromJson(
                        promotionResponse2,
                        ChecklistResponse::class.java
                    )
                    val data = checklistResponse.data
                    Log.d("API", "User ID: ${data.userId}")
                    Log.d("API", "Profile Status: ${data.profile}")
                    Log.d("API", "Meal Snap: ${data.meal_snap}")
                    Log.d("API", "Sync Health Data: ${data.sync_health_data}")
                    handleChecklistResponse(checklistResponse)
                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(
                    this@HomeDashboardActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.e("API ERROR", "onFailure: " + t.message)
                t.printStackTrace() // Print the full stack trace for more details
            }
        })
    }

    private fun handleChecklistResponse(checklistResponse: ChecklistResponse?) {
        // profile
        setStatusOfChecklist(
            checklistResponse?.data?.profile!!,
            binding.includeChecklist.imgCheck,
            binding.includeChecklist.rlChecklistProfile
        )
        //snap Meal
        setStatusOfChecklist(
            checklistResponse.data.meal_snap,
            binding.includeChecklist.imgCheckSnapmeal,
            binding.includeChecklist.rlChecklistSnapmeal
        )
        //sync health
        setStatusOfChecklist(
            checklistResponse.data.sync_health_data,
            binding.includeChecklist.imgCheckSynchealth,
            binding.includeChecklist.rlChecklistSynchealth
        )
        // face Scan
        setStatusOfChecklist(
            checklistResponse.data.vital_facial_scan,
            binding.includeChecklist.imgCheckFacescan,
            binding.includeChecklist.rlChecklistFacescan
        )
        // sleep right question
        setStatusOfChecklist(
            checklistResponse.data.unlock_sleep,
            binding.includeChecklist.imgCheckSleepright,
            binding.includeChecklist.rlChecklistSleepright
        )
        // Eat right question
        setStatusOfChecklist(
            checklistResponse.data.discover_eating,
            binding.includeChecklist.imgCheckEatright,
            binding.includeChecklist.rlChecklistEatright
        )
    }

    private fun setStatusOfChecklist(
        profile: String,
        imageView: ImageView,
        relativeLayout: RelativeLayout
    ) {
        when (profile) {
            "INITIAL" -> {
                imageView.setImageResource(R.drawable.ic_checklist_tick_bg)
            }

            "INPROGRESS" -> {
                imageView.setImageResource(R.drawable.ic_checklist_pending)
            }

            "COMPLETED" -> {
                imageView.setImageResource(R.drawable.ic_checklist_complete)
                relativeLayout.setOnClickListener(null)
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {

    }

    private fun getMyRLHealthCamResult() {
        val accessToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(
            ApiService::class.java
        )

        val call = apiService.getMyRLHealthCamResult(accessToken)

        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        this@HomeDashboardActivity,
                        "Success: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                    try {
                        val jsonString = response.body()!!.string()
                        Log.d("Response Body", " My RL HEalth Cam Result - $jsonString")
                        val gson = Gson()
                        val facialReportResponseNew = gson.fromJson(
                            jsonString,
                            FacialReportResponseNew::class.java
                        )

                        startActivity(
                            Intent(
                                this@HomeDashboardActivity,
                                NewHealthCamReportActivity::class.java
                            )
                        )
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                } else {
                    //   Toast.makeText(RLPageActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.d("MyRLHealthCamResult", "Error:" + response.message())
                    startActivity(Intent(this@HomeDashboardActivity, HealthCamActivity::class.java))
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@HomeDashboardActivity, "Network Error: " + t.message, Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this@HomeDashboardActivity, HealthCamActivity::class.java))
            }
        })
    }

}