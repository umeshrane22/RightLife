package com.jetsynthesys.rightlife.newdashboard

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.ai_package.ui.MainAIActivity
import com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment.SleepSegmentModel
import com.jetsynthesys.rightlife.apimodel.userdata.UserProfileResponse
import com.jetsynthesys.rightlife.databinding.ActivityHomeDashboardBinding
import com.jetsynthesys.rightlife.databinding.BottomsheetTrialEndedBinding
import com.jetsynthesys.rightlife.newdashboard.NewHomeFragment.HomeFragment
import com.jetsynthesys.rightlife.newdashboard.model.AiDashboardResponseMain
import com.jetsynthesys.rightlife.newdashboard.model.ChecklistResponse
import com.jetsynthesys.rightlife.newdashboard.model.DashboardChecklistManager
import com.jetsynthesys.rightlife.newdashboard.model.DashboardChecklistResponse
import com.jetsynthesys.rightlife.newdashboard.model.DiscoverDataItem
import com.jetsynthesys.rightlife.ui.CommonAPICall
import com.jetsynthesys.rightlife.ui.DialogUtils
import com.jetsynthesys.rightlife.ui.HomeActivity
import com.jetsynthesys.rightlife.ui.NewSleepSounds.NewSleepSoundActivity
import com.jetsynthesys.rightlife.ui.affirmation.TodaysAffirmationActivity
import com.jetsynthesys.rightlife.ui.breathwork.BreathworkActivity
import com.jetsynthesys.rightlife.ui.healthcam.HealthCamActivity
import com.jetsynthesys.rightlife.ui.healthcam.NewHealthCamReportActivity
import com.jetsynthesys.rightlife.ui.jounal.new_journal.JournalListActivity
import com.jetsynthesys.rightlife.ui.new_design.OnboardingQuestionnaireActivity
import com.jetsynthesys.rightlife.ui.profile_new.ProfileSettingsActivity
import com.jetsynthesys.rightlife.ui.questionnaire.QuestionnaireEatRightActivity
import com.jetsynthesys.rightlife.ui.questionnaire.QuestionnaireThinkRightActivity
import com.jetsynthesys.rightlife.ui.scan_history.PastReportActivity
import com.jetsynthesys.rightlife.ui.utility.AppConstants
import com.jetsynthesys.rightlife.ui.utility.DateTimeUtils
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


class HomeDashboardActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityHomeDashboardBinding
    private var isAdd = true
    private var checklistComplete = true
    private var checkListCount = 0
    private lateinit var healthConnectClient: HealthConnectClient
    private val allReadPermissions = setOf(
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(SpeedRecord::class),
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class)
    )

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDashboardBinding.inflate(layoutInflater)
        setChildContentView(binding.root)

        // Set default fragment
        loadFragment(HomeFragment())

        //set report list dummy for demo
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Health No Data


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

        //handle bottom menu
        binding.includeChecklist.imgQuestionmarkChecklist.setOnClickListener {
            DialogUtils.showCheckListQuestionCommonDialog(this,"Why Checklist?")
        }
        binding.includeChecklist.rlChecklistWhyThisDialog.setOnClickListener {
            DialogUtils.showCheckListQuestionCommonDialog(this)
        }
        binding.rlViewPastReports.setOnClickListener {
            startActivity(Intent(this, PastReportActivity::class.java))
        }

        with(binding) {
            binding.includedhomebottomsheet.llJournal.setOnClickListener {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(
                        Intent(
                            this@HomeDashboardActivity, JournalListActivity::class.java
                        )
                    )
                }
            }
            binding.includedhomebottomsheet.llAffirmations.setOnClickListener {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(
                        Intent(
                            this@HomeDashboardActivity, TodaysAffirmationActivity::class.java
                        )
                    )
                }
            }
            binding.includedhomebottomsheet.llSleepsounds.setOnClickListener {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(
                        Intent(
                            this@HomeDashboardActivity, NewSleepSoundActivity::class.java
                        )
                    )
                }
            }
            binding.includedhomebottomsheet.llBreathwork.setOnClickListener {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(
                        Intent(
                            this@HomeDashboardActivity, BreathworkActivity::class.java
                        )
                    )
                }
            }
            binding.includedhomebottomsheet.llHealthCamQl.setOnClickListener {
                if (DashboardChecklistManager.facialScanStatus) {
                    startActivity(
                        Intent(
                            this@HomeDashboardActivity, NewHealthCamReportActivity::class.java
                        )
                    )
                } else {
                    startActivity(Intent(this@HomeDashboardActivity, HealthCamActivity::class.java))
                }
            }
            binding.includedhomebottomsheet.llMealplan.setOnClickListener {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(Intent(
                        this@HomeDashboardActivity, MainAIActivity::class.java
                    ).apply {
                        putExtra("ModuleName", "EatRight")
                        putExtra("BottomSeatName", "SelectMealTypeEat")
                    })
                }
            }

        }
        binding.includedhomebottomsheet.llFoodLog.setOnClickListener(this)
        binding.includedhomebottomsheet.llActivityLog.setOnClickListener(this)
        binding.includedhomebottomsheet.llMoodLog.setOnClickListener(this)
        binding.includedhomebottomsheet.llSleepLog.setOnClickListener(this)
        binding.includedhomebottomsheet.llWeightLog.setOnClickListener(this)
        binding.includedhomebottomsheet.llWaterLog.setOnClickListener(this)

        val todayDate = SimpleDateFormat(
            "EEEE, d MMMM", Locale.getDefault()
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
            finish()
        }

        // Set initial selection
        updateMenuSelection(R.id.menu_home)

        // Handling Subscribe to RightLife
        binding.trialExpiredLayout.btnSubscription.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }


        // Handle FAB click
        binding.fab.backgroundTintList = ContextCompat.getColorStateList(
            this, android.R.color.white
        )
        binding.fab.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
        val bottom_sheet = binding.includedhomebottomsheet.bottomSheet
        binding.fab.setOnClickListener { v ->

            if (binding.includedhomebottomsheet.bottomSheet.visibility == View.VISIBLE) {
                bottom_sheet.visibility = View.GONE
                binding.iconHome.setBackgroundResource(R.drawable.homeselected)
                binding.labelHome.setTextColor(resources.getColor(R.color.menuselected))
                val typeface = ResourcesCompat.getFont(this, R.font.dmsans_bold)
                binding.labelHome.typeface = typeface
            } else {
                bottom_sheet.visibility = View.VISIBLE
                //binding.iconHome.setBackgroundColor(Color.TRANSPARENT)
                //binding.labelHome.setTextColor(resources.getColor(R.color.txt_color_header))
                val typeface = ResourcesCompat.getFont(this, R.font.dmsans_regular)
                //binding.labelHome.setTypeface(typeface)
            }
            v.isSelected = !v.isSelected

            binding.fab.animate().rotationBy(180f).setDuration(60)
                .setInterpolator(DecelerateInterpolator()).withEndAction {
                    // Change icon after rotation
                    if (isAdd) {
                        binding.fab.setImageResource(R.drawable.icon_quicklink_plus_black) // Change to close icon
                        binding.fab.backgroundTintList = ContextCompat.getColorStateList(
                            this, R.color.rightlife
                        )
                        binding.fab.imageTintList = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.black
                            )
                        )
                    } else {
                        binding.fab.setImageResource(R.drawable.icon_quicklink_plus) // Change back to add icon
                        binding.fab.backgroundTintList = ContextCompat.getColorStateList(
                            this, R.color.white
                        )
                        binding.fab.imageTintList = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.rightlife
                            )
                        )
                    }
                    isAdd = !isAdd // Toggle the state
                }.start()

        }

        // Api calls
        getUserDetails("")

        getAiDashboard("")

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
            if (ifFreeTrailOrPaymentAvailable()) {
                startActivity(Intent(this, QuestionnaireEatRightActivity::class.java))
            }
        }
        binding.includeChecklist.rlChecklistSleepright.setOnClickListener {
            if (ifFreeTrailOrPaymentAvailable()) {
                startActivity(Intent(this, QuestionnaireThinkRightActivity::class.java))
            }
        }
        binding.includeChecklist.rlChecklistSynchealth.setOnClickListener {
            val availabilityStatus = HealthConnectClient.getSdkStatus(this)
            if (availabilityStatus == HealthConnectClient.SDK_AVAILABLE) {
                healthConnectClient = HealthConnectClient.getOrCreate(this)
                lifecycleScope.launch {
                    requestPermissionsAndReadAllData()

                }
            } else {
                installHealthConnect(this)
            }
        }
        binding.includeChecklist.rlChecklistProfile.setOnClickListener {
            if (ifFreeTrailOrPaymentAvailable()) {
                val intent = Intent(this, OnboardingQuestionnaireActivity::class.java)
                intent.putExtra("forProfileChecklist", true)
                startActivity(intent)
            }
        }
        binding.includeChecklist.rlChecklistSnapmeal.setOnClickListener {
            startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                putExtra("ModuleName", "EatRight")
                putExtra("BottomSeatName", "SnapMealTypeEat")
            })
        }
        binding.includeChecklist.rlChecklistFacescan.setOnClickListener {
            if (DashboardChecklistManager.facialScanStatus) {
                startActivity(
                    Intent(
                        this@HomeDashboardActivity, NewHealthCamReportActivity::class.java
                    )
                )
            } else {
                startActivity(Intent(this@HomeDashboardActivity, HealthCamActivity::class.java))
            }
        }
        binding.profileImage.setOnClickListener {
            startActivity(Intent(this@HomeDashboardActivity, ProfileSettingsActivity::class.java))
        }

        binding.cardThinkrightMain.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "ThinkRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }
        binding.cardEatrightMain.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "EatRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }

        binding.cardMoverightMain.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "MoveRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }
        binding.cardSleeprightMain.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "SleepRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }

        // for no data card
        binding.cardThinkrightMainNodata.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "ThinkRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }
        binding.cardEatrightMainNodata.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "EatRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }

        binding.cardMoverightMainNodata.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "MoveRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }
        binding.cardSleeprightMainNodata.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "SleepRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }



        binding.cardEatright.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "EatRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }
        binding.cardSleepright.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "SleepRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }
        binding.cardThinkright.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "ThinkRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }
        binding.cardMoveright.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(this@HomeDashboardActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "MoveRight")
                    putExtra("BottomSeatName", "Not")
                })
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getDashboardChecklist("")
        getDashboardChecklistStatus()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
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
        // Make the API call
        val call = apiService.getUserDetais(sharedPreferenceManager.accessToken)
        call.enqueue(object : Callback<JsonElement?> {
            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                if (response.isSuccessful && response.body() != null) {
                    val gson = Gson()
                    val jsonResponse = gson.toJson(response.body())

                    val ResponseObj = gson.fromJson(
                        jsonResponse, UserProfileResponse::class.java
                    )
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
                    val tvGreetingText = findViewById<TextView>(R.id.greetingText)
                    tvGreetingText.text = "Good " + DateTimeUtils.getWishingMessage() + " ,"

                    val countDown = getCountDownDays(ResponseObj.userdata.createdAt)
                    if (countDown <= 7) {
                        binding.tvCountDown.text = "${countDown + 1}/7"
                        binding.llCountDown.visibility = View.VISIBLE
                        binding.trialExpiredLayout.trialExpiredLayout.visibility = View.GONE
                    } else {
                        binding.llCountDown.visibility = View.GONE
                        binding.trialExpiredLayout.trialExpiredLayout.visibility = View.VISIBLE
                    }

                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                handleNoInternetView(t)
            }
        })
    }

    private fun getCountDownDays(pastTimestamp: Long): Int {
        val currentTimestamp = System.currentTimeMillis()

        val diffInMillis = currentTimestamp - pastTimestamp
        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        return diffInDays.toInt()
    }


    // get AI Dashboard Data
    private fun getAiDashboard(s: String) {
        // Make the API call
        val call = apiService.getAiDashboard(sharedPreferenceManager.accessToken)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    val promotionResponse2 = response.body()!!.string()

                    val gson = Gson()

                    val aiDashboardResponseMain = gson.fromJson(
                        promotionResponse2, AiDashboardResponseMain::class.java
                    )
                    handleSelectedModule(aiDashboardResponseMain)
                    handleDescoverList(aiDashboardResponseMain)
                    binding.recyclerView.adapter = HeartRateAdapter(
                        aiDashboardResponseMain.data?.facialScan, this@HomeDashboardActivity
                    )
                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                handleNoInternetView(t)
            }
        })
    }

    private fun extractNumericValues(input: String): Pair<String, String> {
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

    private fun setIfNotNullOrBlank(textView: TextView, value: String?) {
        if (!value.isNullOrBlank()) {
            textView.text = value
        }
    }

    private fun handleDescoverList(aiDashboardResponseMain: AiDashboardResponseMain?) {
        if (aiDashboardResponseMain?.data?.discoverData != null) {
            setHealthNoDataCardAdapter(aiDashboardResponseMain.data.discoverData)
        }
    }

    private fun handleSelectedModule(aiDashboardResponseMain: AiDashboardResponseMain?) {
        val modules = aiDashboardResponseMain?.data?.updatedModules

        if (modules.isNullOrEmpty()) {
            binding.llNodataMain.visibility = View.VISIBLE
            var moduleId = SharedPreferenceManager.getInstance(this).selectedOnboardingModule
            if (moduleId.isEmpty()) {
                moduleId = "EAT_RIGHT"
            }
            when (moduleId) {
                "MOVE_RIGHT" -> {
                    // MOVE_RIGHT logic here
                    binding.cardMoverightMainNodata.visibility = View.VISIBLE
                    binding.cardMoveright.visibility = View.GONE
                }

                "THINK_RIGHT" -> {
                    // THINK_RIGHT logic here
                    binding.cardThinkrightMainNodata.visibility = View.VISIBLE
                    binding.cardThinkright.visibility = View.GONE
                }

                "EAT_RIGHT" -> {
                    // EAT_RIGHT logic here
                    binding.cardEatrightMainNodata.visibility = View.VISIBLE
                    binding.cardEatright.visibility = View.GONE
                }

                "SLEEP_RIGHT" -> {
                    // SLEEP_RIGHT logic here
                    binding.cardSleeprightMainNodata.visibility = View.VISIBLE
                    binding.cardSleepright.visibility = View.GONE
                }

                else -> {
                    // Default case logic here
                }
            }

        } else {
            binding.llNodataMain.visibility = View.GONE


            //for (module in aiDashboardResponseMain?.data?.updatedModules!!) {
            aiDashboardResponseMain.data.updatedModules.forEach { module ->
                val moduleId = module.moduleId
                val isSelected = module.isSelectedModule


                when (moduleId) {
                    "MOVE_RIGHT" -> {

                        if (isSelected == true) {
                            binding.cardMoverightMain.visibility = View.VISIBLE
                            binding.cardMoveright.visibility = View.GONE
                        }
                        //set data on card once response works
                        binding.tvCaloryValue.text = module.calorieBalance.toString()
                        binding.tvCaloryIntake.text = module.intake.toString()
                        binding.tvCaloryBurn.text = module.burned.toString()

                        setIfNotNullOrBlank(
                            binding.tvModuleValueEatright,
                            module.calories?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueSleepright, module.sleepDuration?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueThinkright, module.mindfulTime?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueMoveright, module.activeBurn?.toString()
                        )
                    }

                    "THINK_RIGHT" -> {
                        if (isSelected == true) {
                            binding.cardThinkrightMain.visibility = View.VISIBLE
                            binding.cardThinkright.visibility = View.GONE
                        }
                        binding.tvMinutesTextValue.text = module.mindfulnessMinutes
                        binding.tvDaysTextValue.text = module.wellnessDays

                        setIfNotNullOrBlank(
                            binding.tvModuleValueEatright,
                            module.calories?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueSleepright, module.sleepDuration?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueThinkright, module.mindfulTime?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueMoveright, module.activeBurn?.toString()
                        )
                    }

                    "EAT_RIGHT" -> {
                        if (isSelected == true) {
                            binding.cardEatrightMain.visibility = View.VISIBLE
                            binding.cardEatright.visibility = View.GONE
                        }

                        val (proteinValue, proteinTotal) = extractNumericValues(module.protein.toString())
                        val (carbsValue, carbsTotal) = extractNumericValues(module.carbs.toString())
                        val (fatsValue, fatsTotal) = extractNumericValues(module.fats.toString())

                        binding.tvSubtractionCalValue.text = proteinValue
                        binding.tvSubtractionCalUnit.text = "/" + proteinTotal + " g"
                        binding.carbsProgressBar.max = proteinTotal.toDouble().toInt()
                        binding.carbsProgressBar.progress = proteinValue.toDouble().toInt()


                        binding.tvSubtractionCarbsValue.text = carbsValue
                        binding.tvSubtractionCarbsUnit.text = "/" + carbsTotal + " g"
                        binding.protienProgressBar.max = carbsTotal.toDouble().toInt()
                        binding.protienProgressBar.progress = carbsValue.toDouble().toInt()

                        binding.tvSubtractionFatsValue.text = fatsValue
                        binding.tvSubtractionFatsUnit.text = "/" + fatsTotal + " g"
                        binding.fatsProgressBar.max = fatsTotal.toDouble().toInt()
                        binding.fatsProgressBar.progress = fatsValue.toDouble().toInt()


                        binding.halfCurveProgressBar.setProgress(0f)
                        binding.halfCurveProgressBar.setValues(0,0)
                        // value is wrong for eatright progress let backend correct then uncomment below
                        /*val (curent, max) = extractNumericValues(module.calories.toString())
                            binding.halfCurveProgressBar.setValues(curent.toInt(), max.toInt())
                            val percentage = calculatePercentage(curent.toInt(), max.toInt())
                            binding.halfCurveProgressBar.setProgress(percentage.toFloat())*/

                        setIfNotNullOrBlank(
                            binding.tvModuleValueEatright,
                            module.calories?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueSleepright, module.sleepDuration?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueThinkright, module.mindfulTime?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueMoveright, module.activeBurn?.toString()
                        )
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
                            SleepSegmentModel(
                                0.001f, 0.100f, resources.getColor(R.color.blue_bar), 110f
                            ), SleepSegmentModel(
                                0.101f, 0.150f, resources.getColor(R.color.blue_bar), 110f
                            ), SleepSegmentModel(
                                0.151f, 0.300f, resources.getColor(R.color.blue_bar), 110f
                            ), SleepSegmentModel(
                                0.301f, 0.400f, resources.getColor(R.color.blue_bar), 110f
                            ), SleepSegmentModel(
                                0.401f, 0.450f, resources.getColor(R.color.blue_bar), 110f
                            ), SleepSegmentModel(
                                0.451f, 0.550f, resources.getColor(R.color.sky_blue_bar), 110f
                            ), SleepSegmentModel(
                                0.551f, 0.660f, resources.getColor(R.color.sky_blue_bar), 110f
                            ), SleepSegmentModel(
                                0.661f, 0.690f, resources.getColor(R.color.sky_blue_bar), 110f
                            ), SleepSegmentModel(
                                0.691f, 0.750f, resources.getColor(R.color.deep_purple_bar), 110f
                            ), SleepSegmentModel(
                                0.751f, 0.860f, resources.getColor(R.color.deep_purple_bar), 110f
                            ), SleepSegmentModel(
                                0.861f, 0.990f, resources.getColor(R.color.red_orange_bar), 110f
                            )
                        )

                        binding.sleepStagesView.setSleepData(sleepData)

                        setIfNotNullOrBlank(
                            binding.tvModuleValueEatright,
                            module.calories?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueSleepright, module.sleepDuration?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueThinkright, module.mindfulTime?.toString()
                        )
                        setIfNotNullOrBlank(
                            binding.tvModuleValueMoveright, module.activeBurn?.toString()
                        )

                        aiDashboardResponseMain.data.updatedModules

                        val sleepModule =
                            aiDashboardResponseMain.data.updatedModules.find { it.moduleId == "SLEEP_RIGHT" }
                        sleepModule?.let {
                            setStageGraphFromSleepRightModule(
                                rem = it.rem ?: "0min",
                                core = it.core ?: "0min",
                                deep = it.deep ?: "0min",
                                awake = it.awake ?: "0min"
                            )
                        }
                    }

                    else -> {
                        binding.cardMoverightMain.visibility = View.VISIBLE
                        binding.cardMoveright.visibility = View.GONE
                    }
                }


            }
        }
    }


    private fun parseSleepDuration(durationStr: String): Float {
        val hourRegex = Regex("(\\d+)h")
        val minRegex = Regex("(\\d+)min")

        val hours = hourRegex.find(durationStr)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
        val minutes = minRegex.find(durationStr)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f

        return hours * 60 + minutes
    }


    private fun setStageGraphFromSleepRightModule(
        rem: String, core: String, deep: String, awake: String
    ) {
        val sleepData: ArrayList<SleepSegmentModel> = arrayListOf()
        val durations = mapOf(
            "REM" to parseSleepDuration(rem),
            "Core" to parseSleepDuration(core),
            "Deep" to parseSleepDuration(deep),
            "Awake" to parseSleepDuration(awake)
        )

        val totalDuration = durations.values.sum()
        var currentPosition = 0f

        durations.forEach { (stage, duration) ->
            val start = currentPosition / totalDuration
            val end = (currentPosition + duration) / totalDuration
            val color = when (stage) {
                "REM" -> Color.parseColor("#63D4FE")
                "Deep" -> Color.parseColor("#5E5CE6")
                "Core" -> Color.parseColor("#0B84FF")
                "Awake" -> Color.parseColor("#FF6650")
                else -> Color.GRAY
            }
            sleepData.add(SleepSegmentModel(start, end, color, 110f))
            currentPosition += duration
        }

        binding.sleepStagesView.setSleepData(sleepData)//sleepStagesView.setSleepData(sleepData)
    }


    //getDashboardChecklist
    private fun getDashboardChecklist(s: String) {
        // Make the API call
        val call = apiService.getDashboardChecklist(sharedPreferenceManager.accessToken)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    val promotionResponse2 = response.body()!!.string()
                    val gson = Gson()
                    val checklistResponse = gson.fromJson(
                        promotionResponse2, ChecklistResponse::class.java
                    )
                    handleChecklistResponse(checklistResponse)
                    checkListCount = 0
                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                handleNoInternetView(t) // Print the full stack trace for more details
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
            binding.includeChecklist.rlChecklistSnapmeal,
            false
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
            binding.includeChecklist.rlChecklistFacescan,
            false
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
        binding.includeChecklist.tvChecklistNumber.text = "$checkListCount of 6 tasks completed"
        // Chceklist completion logic
        if (DashboardChecklistManager.checklistStatus) {
            binding.llDashboardMainData.visibility = View.VISIBLE
            binding.includeChecklist.llLayoutChecklist.visibility = View.GONE
        } else {
            binding.llDashboardMainData.visibility = View.GONE
            binding.includeChecklist.llLayoutChecklist.visibility = View.VISIBLE
        }
    }

    private fun setStatusOfChecklist(
        profile: String,
        imageView: ImageView,
        relativeLayout: RelativeLayout,
        disableclick: Boolean = true,
    ) {
        when (profile) {
            "INITIAL" -> {
                imageView.setImageResource(R.drawable.ic_checklist_tick_bg)
                checklistComplete = false
            }

            "INPROGRESS" -> {
                imageView.setImageResource(R.drawable.ic_checklist_pending)
                checklistComplete = false
            }

            "COMPLETED" -> {
                imageView.setImageResource(R.drawable.ic_checklist_complete)
                if (profile.equals("COMPLETED") && profile.equals("COMPLETED")) {

                }
                if (disableclick) {
                    relativeLayout.setOnClickListener(null)
                }
                checkListCount++
            }

            else -> {
                imageView.setImageResource(R.drawable.ic_checklist_tick_bg)
                checklistComplete = false
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_food_log -> {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(Intent(
                        this@HomeDashboardActivity, MainAIActivity::class.java
                    ).apply {
                        putExtra("ModuleName", "EatRight")
                        putExtra("BottomSeatName", "MealLogTypeEat")
                    })
                }
            }

            R.id.ll_activity_log -> {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(Intent(
                        this@HomeDashboardActivity, MainAIActivity::class.java
                    ).apply {
                        putExtra("ModuleName", "MoveRight")
                        putExtra("BottomSeatName", "SearchActivityLogMove")
                    })
                }
            }

            R.id.ll_mood_log -> {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(Intent(
                        this@HomeDashboardActivity, MainAIActivity::class.java
                    ).apply {
                        putExtra("ModuleName", "ThinkRight")
                        putExtra("BottomSeatName", "RecordEmotionMoodTracThink")
                    })
                }
            }


            R.id.ll_sleep_log -> {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(Intent(
                        this@HomeDashboardActivity, MainAIActivity::class.java
                    ).apply {
                        putExtra("ModuleName", "SleepRight")
                        putExtra("BottomSeatName", "LogLastNightSleep")
                    })
                }
            }

            R.id.ll_weight_log -> {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(Intent(
                        this@HomeDashboardActivity, MainAIActivity::class.java
                    ).apply {
                        putExtra("ModuleName", "EatRight")
                        putExtra("BottomSeatName", "LogWeightEat")
                    })
                }
            }

            R.id.ll_water_log -> {
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(Intent(
                        this@HomeDashboardActivity, MainAIActivity::class.java
                    ).apply {
                        putExtra("ModuleName", "EatRight")
                        putExtra("BottomSeatName", "LogWaterIntakeEat")
                    })
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private suspend fun requestPermissionsAndReadAllData() {
        try {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (allReadPermissions.all { it in granted }) {
//                fetchAllHealthData()
                CommonAPICall.updateChecklistStatus(
                    this, "sync_health_data", AppConstants.CHECKLIST_COMPLETED
                )
            } else {
                requestPermissionsLauncher.launch(allReadPermissions.toTypedArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error checking permissions: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.all { it }) {
                lifecycleScope.launch {
                    // fetchAllHealthData()
                }
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun isHealthConnectAvailable(context: Context): Boolean {
        val packageManager = context.packageManager
        return try {
            packageManager.getPackageInfo("com.google.android.apps.healthdata", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun setHealthNoDataCardAdapter(discoverData: List<DiscoverDataItem>?) {

        val adapter = HealthCardAdapter(discoverData)
        binding.healthCardRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.healthCardRecyclerView.adapter = adapter
    }

    private fun installHealthConnect(context: Context) {
        val uri =
            "https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    // get dashbord checklist status complete or not
    private fun getDashboardChecklistStatus() {
        apiService.getdashboardChecklistStatus(SharedPreferenceManager.getInstance(this).accessToken)
            .enqueue(object : Callback<DashboardChecklistResponse> {
                override fun onResponse(
                    call: Call<DashboardChecklistResponse>,
                    response: Response<DashboardChecklistResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.data?.let {
                            DashboardChecklistManager.updateFrom(it)

                        }
                        if (DashboardChecklistManager.isDataLoaded) {
                            // Chceklist completion logic
                            if (DashboardChecklistManager.checklistStatus) {
                                binding.llDashboardMainData.visibility = View.VISIBLE
                                binding.includeChecklist.llLayoutChecklist.visibility = View.GONE
                                binding.llDiscoverLayout.visibility = View.GONE
                            } else {
                                binding.llDashboardMainData.visibility = View.GONE
                                binding.includeChecklist.llLayoutChecklist.visibility = View.VISIBLE
                                binding.llDiscoverLayout.visibility = View.VISIBLE
                            }
                        } else {
                            Toast.makeText(
                                this@HomeDashboardActivity,
                                "Server Error: " + response.code(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<DashboardChecklistResponse>, t: Throwable) {
                    handleNoInternetView(t)
                }

            })
    }

    private fun checkTrailEndedAndShowDialog(): Boolean {
        return if (!DashboardChecklistManager.paymentStatus) {
            showTrailEndedBottomSheet()
            false // Return false if condition is true and dialog is shown
        } else {
            if (!DashboardChecklistManager.checklistStatus) {
                DialogUtils.showCheckListQuestionCommonDialog(this)
                false
            } else {
                true // Return true if condition is false
            }
        }
        return true
    }

    private fun ifFreeTrailOrPaymentAvailable(): Boolean {
        return return if (!DashboardChecklistManager.paymentStatus) {
            showTrailEndedBottomSheet()
            false // Return false if condition is true and dialog is shown
        } else {
            true
        }
        return true
    }

    private fun showTrailEndedBottomSheet() {
        // Create and configure BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(this)

        // Inflate the BottomSheet layout
        val dialogBinding = BottomsheetTrialEndedBinding.inflate(layoutInflater)
        val bottomSheetView = dialogBinding.root

        bottomSheetDialog.setContentView(bottomSheetView)


        bottomSheetDialog.setContentView(bottomSheetView)

        // Set up the animation
        val bottomSheetLayout = bottomSheetView.findViewById<LinearLayout>(R.id.design_bottom_sheet)
        if (bottomSheetLayout != null) {
            val slideUpAnimation: Animation =
                AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_slide_up)
            bottomSheetLayout.animation = slideUpAnimation
        }

        dialogBinding.ivDialogClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        dialogBinding.btnExplorePlan.setOnClickListener {
            bottomSheetDialog.dismiss()
            //finish()
        }

        bottomSheetDialog.show()
    }
}