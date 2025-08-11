package com.jetsynthesys.rightlife.newdashboard

import android.app.ComponentCaller
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryPurchasesParams
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.BuildConfig
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.ai_package.ui.MainAIActivity
import com.jetsynthesys.rightlife.apimodel.userdata.UserProfileResponse
import com.jetsynthesys.rightlife.databinding.ActivityHomeNewBinding
import com.jetsynthesys.rightlife.databinding.BottomsheetTrialEndedBinding
import com.jetsynthesys.rightlife.databinding.DialogForceUpdateBinding
import com.jetsynthesys.rightlife.databinding.DialogSwitchAccountBinding
import com.jetsynthesys.rightlife.newdashboard.model.DashboardChecklistManager
import com.jetsynthesys.rightlife.subscriptions.SubscriptionPlanListActivity
import com.jetsynthesys.rightlife.subscriptions.pojo.PaymentSuccessRequest
import com.jetsynthesys.rightlife.subscriptions.pojo.PaymentSuccessResponse
import com.jetsynthesys.rightlife.subscriptions.pojo.SdkDetail
import com.jetsynthesys.rightlife.ui.DialogUtils
import com.jetsynthesys.rightlife.ui.NewSleepSounds.NewSleepSoundActivity
import com.jetsynthesys.rightlife.ui.affirmation.TodaysAffirmationActivity
import com.jetsynthesys.rightlife.ui.aireport.AIReportWebViewActivity
import com.jetsynthesys.rightlife.ui.breathwork.BreathworkActivity
import com.jetsynthesys.rightlife.ui.healthcam.HealthCamActivity
import com.jetsynthesys.rightlife.ui.healthcam.NewHealthCamReportActivity
import com.jetsynthesys.rightlife.ui.jounal.new_journal.JournalListActivity
import com.jetsynthesys.rightlife.ui.profile_new.ProfileSettingsActivity
import com.jetsynthesys.rightlife.ui.utility.AnalyticsEvent
import com.jetsynthesys.rightlife.ui.utility.AnalyticsLogger
import com.jetsynthesys.rightlife.ui.utility.DateTimeUtils
import com.jetsynthesys.rightlife.ui.utility.NetworkUtils
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class HomeNewActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeNewBinding
    private var isAdd = true
    var isTrialExpired = false
    private var isCountDownVisible = false
    var isHealthCamFree = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeNewBinding.inflate(layoutInflater)
        setChildContentView(binding.root)

        // Load default fragment only on first launch
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeDashboardFragment())
                .commit()
            updateMenuSelection(R.id.menu_home)
        } else {
            // 🟢 Restore menu highlight based on current fragment
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            when (currentFragment) {
                is HomeExploreFragment -> updateMenuSelection(R.id.menu_explore)
                is HomeDashboardFragment -> updateMenuSelection(R.id.menu_home)
            }
        }

        binding.scrollView.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.swipeRefreshLayout.isEnabled = scrollY <= 5
        }

        onBackPressedDispatcher.addCallback {
            if (binding.includedhomebottomsheet.bottomSheet.visibility == View.VISIBLE) {
                binding.includedhomebottomsheet.bottomSheet.visibility = View.GONE
                val currentFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                when (currentFragment) {
                    is HomeExploreFragment -> updateMenuSelection(R.id.menu_explore)
                    is HomeDashboardFragment -> updateMenuSelection(R.id.menu_home)
                }

                binding.fab.setImageResource(R.drawable.icon_quicklink_plus) // Change back to add icon
                binding.fab.backgroundTintList = ContextCompat.getColorStateList(
                    this@HomeNewActivity, R.color.white
                )
                binding.fab.imageTintList = ColorStateList.valueOf(
                    resources.getColor(
                        R.color.rightlife
                    )
                )
            isAdd = !isAdd // Toggle the state

            } else {
                val currentFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                when (currentFragment) {
                    is HomeExploreFragment -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, HomeDashboardFragment())
                            .commit()
                        updateMenuSelection(R.id.menu_home)
                    }

                    is HomeDashboardFragment -> finish()
                }
            }
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
                val currentFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                when (currentFragment) {
                    is HomeExploreFragment -> updateMenuSelection(R.id.menu_explore)
                    is HomeDashboardFragment -> updateMenuSelection(R.id.menu_home)
                }
            } else {
                bottom_sheet.visibility = View.VISIBLE
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

        binding.profileImage.setOnClickListener {
            startActivity(Intent(this, ProfileSettingsActivity::class.java))
        }

        // Handle menu item clicks
        binding.menuHome.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeDashboardFragment())
                .commit()
            updateMenuSelection(R.id.menu_home)
        }

        binding.menuExplore.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeExploreFragment())
                .commit()
            updateMenuSelection(R.id.menu_explore)
        }

        with(binding) {
            includedhomebottomsheet.llJournal.setOnClickListener {
                AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.EOS_JOURNALING_CLICK)
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(
                        Intent(
                            this@HomeNewActivity, JournalListActivity::class.java
                        )
                    )
                }
            }
            includedhomebottomsheet.llAffirmations.setOnClickListener {
                AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.EOS_AFFIRMATION_CLICK)
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(
                        Intent(
                            this@HomeNewActivity, TodaysAffirmationActivity::class.java
                        )
                    )
                }
            }
            includedhomebottomsheet.llSleepsounds.setOnClickListener {
                AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.EOS_SLEEP_SOUNDS)
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(
                        Intent(
                            this@HomeNewActivity, NewSleepSoundActivity::class.java
                        )
                    )
                }
            }
            includedhomebottomsheet.llBreathwork.setOnClickListener {
                AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.EOS_BREATH_WORK_CLICK)
                if (checkTrailEndedAndShowDialog()) {
                    startActivity(
                        Intent(
                            this@HomeNewActivity, BreathworkActivity::class.java
                        )
                    )
                }
            }
            includedhomebottomsheet.llHealthCamQl.setOnClickListener {
                AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.EOS_FACE_SCAN_CLICK)
                if (DashboardChecklistManager.facialScanStatus) {
                    startActivity(
                        Intent(
                            this@HomeNewActivity, NewHealthCamReportActivity::class.java
                        )
                    )
                } else {
                    startActivity(Intent(this@HomeNewActivity, HealthCamActivity::class.java))
                }
            }
            includedhomebottomsheet.llMealplan.setOnClickListener {
                AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.EOS_SNAP_MEAL_CLICK)
                startActivity(Intent(this@HomeNewActivity, MainAIActivity::class.java).apply {
                    putExtra("ModuleName", "EatRight")
                    putExtra("BottomSeatName", "SnapMealTypeEat")
                    if (sharedPreferenceManager.snapMealId.isNotEmpty()) {
                        intent.putExtra(
                            "snapMealId",
                            sharedPreferenceManager.snapMealId
                        ) // make sure snapMealId is declared and initialized
                    }
                })
            }

        }

        binding.includedhomebottomsheet.llFoodLog.setOnClickListener {
            AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.LYA_FOOD_LOG_CLICK)
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(
                    this@HomeNewActivity, MainAIActivity::class.java
                ).apply {
                    putExtra("ModuleName", "EatRight")
                    putExtra("BottomSeatName", "MealLogTypeEat")
                })
            }
        }
        binding.includedhomebottomsheet.llActivityLog.setOnClickListener {
            AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.LYA_ACTIVITY_LOG_CLICK)
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(
                    this@HomeNewActivity, MainAIActivity::class.java
                ).apply {
                    putExtra("ModuleName", "MoveRight")
                    putExtra("BottomSeatName", "SearchActivityLogMove")
                })
            }
        }
        binding.includedhomebottomsheet.llMoodLog.setOnClickListener {
            if (checkTrailEndedAndShowDialog()) {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
            }
        }
        binding.includedhomebottomsheet.llSleepLog.setOnClickListener {
            AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.LYA_SLEEP_LOG_CLICK)
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(
                    this@HomeNewActivity, MainAIActivity::class.java
                ).apply {
                    putExtra("ModuleName", "SleepRight")
                    putExtra("BottomSeatName", "LogLastNightSleep")
                })
            }
        }
        binding.includedhomebottomsheet.llWeightLog.setOnClickListener {
            AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.LYA_WEIGHT_LOG_CLICK)
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(
                    this@HomeNewActivity, MainAIActivity::class.java
                ).apply {
                    putExtra("ModuleName", "EatRight")
                    putExtra("BottomSeatName", "LogWeightEat")
                })
            }
        }
        binding.includedhomebottomsheet.llWaterLog.setOnClickListener {
            AnalyticsLogger.logEvent(this@HomeNewActivity, AnalyticsEvent.LYA_WATER_LOG_CLICK)
            if (checkTrailEndedAndShowDialog()) {
                startActivity(Intent(
                    this@HomeNewActivity, MainAIActivity::class.java
                ).apply {
                    putExtra("ModuleName", "EatRight")
                    putExtra("BottomSeatName", "LogWaterIntakeEat")
                })
            }
        }

        // Open AI Report WebView on click   // Also logic to hide this button if Report is not generated pending
        binding.rightLifeReportCard.setOnClickListener {
            var dynamicReportId = "" // This Is User ID
            dynamicReportId = SharedPreferenceManager.getInstance(applicationContext).userId
            if (dynamicReportId.isEmpty()) {
                // Some error handling if the ID is not available
            }else{
                val intent = Intent(this, AIReportWebViewActivity::class.java).apply {
                    // Put the dynamic ID as an extra
                    putExtra(AIReportWebViewActivity.EXTRA_REPORT_ID, dynamicReportId)
                }
                startActivity(intent)
            }
        }

        // Handling Subscribe to RightLife
        binding.trialExpiredLayout.btnSubscription.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SubscriptionPlanListActivity::class.java
                ).apply {
                    putExtra("SUBSCRIPTION_TYPE", "SUBSCRIPTION_PLAN")
                })
        }
    }

    override fun onResume() {
        super.onResume()
        checkForUpdate()
        getUserDetails()
        initBillingAndRecover()
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        if (intent.getBooleanExtra("start_journal", false)) {
            startActivity(Intent(this, JournalListActivity::class.java))
        } else if (intent.getBooleanExtra("start_profile", false)) {
            startActivity(Intent(this, ProfileSettingsActivity::class.java))
        }
    }


    // get user details
    fun getUserDetails() {
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

                    SharedPreferenceManager.getInstance(applicationContext).
                    setAIReportGeneratedView(ResponseObj.reportView)

                    if (ResponseObj.userdata.profilePicture != null) {
                        Glide.with(this@HomeNewActivity)
                            .load(ApiClient.CDN_URL_QA + ResponseObj.userdata.profilePicture)
                            .placeholder(R.drawable.rl_profile).error(R.drawable.rl_profile)
                            .into(binding.profileImage)
                    }
                    binding.userName.text = ResponseObj.userdata.firstName
                    val tvGreetingText = findViewById<TextView>(R.id.greetingText)
                    tvGreetingText.text = "Good " + DateTimeUtils.getWishingMessage() + " ,"

                    val countDown = getCountDownDays(ResponseObj.userdata.createdAt)
                    if (countDown < 7) {
                        binding.tvCountDown.text = "${countDown + 1}/7"
                        binding.llCountDown.visibility = View.VISIBLE
                        binding.trialExpiredLayout.trialExpiredLayout.visibility = View.GONE
                        isTrialExpired = false
                        isCountDownVisible = true
                    } else {
                        binding.llCountDown.visibility = View.GONE
                        isCountDownVisible = false
                        if (!DashboardChecklistManager.paymentStatus) {
                            binding.trialExpiredLayout.trialExpiredLayout.visibility = View.VISIBLE
                            isTrialExpired = true
                        }else{
                            binding.trialExpiredLayout.trialExpiredLayout.visibility = View.GONE
                            isTrialExpired = false
                        }
                    }

                    if (ResponseObj.isReportGenerated && !ResponseObj.reportView) {
                        binding.rightLifeReportCard.visibility = View.VISIBLE
                    } else {
                        binding.rightLifeReportCard.visibility = View.GONE
                    }


                    /*if (!ResponseObj.reportView){
                        binding.rightLifeReportCard.visibility = View.VISIBLE
                    } else {
                        binding.rightLifeReportCard.visibility = View.GONE
                    }*/
                    /*if (ResponseObj.isFacialReport != null && ResponseObj.isFacialReport) {
                        showSwitchAccountDialog(this@HomeNewActivity,"","")
                    } else {

                    }*/

                    // Find HealthCam service and check if it's free
                    var isHealthCamFree = ResponseObj?.homeServices
                        ?.find { it.title == "HealthCam" }
                        ?.isFree ?: false
                    Log.d("isHealthCamFree", isHealthCamFree.toString())
                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                if (!DashboardChecklistManager.paymentStatus) {
                    binding.trialExpiredLayout.trialExpiredLayout.visibility = View.VISIBLE
                    isTrialExpired = true
                }else{
                    binding.trialExpiredLayout.trialExpiredLayout.visibility = View.GONE
                    isTrialExpired = false
                }


            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                handleNoInternetView(t)
            }
        })
    }

    private fun checkForUpdate() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(
            mapOf(
                "force_update_current_version" to "1.0.0",
                "isForceUpdate" to false,
                "force_update_build_number" to "261"
            )
        )

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val latestVersion = remoteConfig.getString("force_update_current_version")
                val isForceUpdate = remoteConfig.getBoolean("isForceUpdate")
                val forceUpdateBuildNumber = remoteConfig.getString("force_update_build_number")

                val currentVersion = BuildConfig.VERSION_NAME

                if (isForceUpdate && isVersionOutdated(currentVersion, latestVersion)) {
                    showForceUpdateDialog()
                }
            }
        }
    }

    private fun isVersionOutdated(current: String, latest: String): Boolean {
        return current != latest // or use a version comparator for more complex rules
    }

    private fun getCountDownDays(pastTimestamp: Long): Int {
        val currentTimestamp = System.currentTimeMillis()

        val diffInMillis = currentTimestamp - pastTimestamp
        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        return diffInDays.toInt()
    }

    private fun showForceUpdateDialog() {

        // Create the dialog
        val dialog = Dialog(this)
        val binding = DialogForceUpdateBinding.inflate(layoutInflater)

        dialog.setContentView(binding.root)
        dialog.setCancelable(false) // Prevent back press
        dialog.setCanceledOnTouchOutside(false) // Prevent outside tap
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val window = dialog.window
        window?.let {
            val layoutParams = it.attributes
            layoutParams.dimAmount = 0.7f
            it.attributes = layoutParams
        }

        binding.btnUpdate.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${packageName}")
                )
            )
            finish()
        }

        dialog.show()

    }

    private fun updateMenuSelection(selectedMenuId: Int) {
        // Reset both to unselected
        binding.iconHome.setImageResource(R.drawable.new_home_unselected_svg)
        binding.labelHome.setTextColor(ContextCompat.getColor(this, R.color.gray))
        binding.labelHome.setTypeface(null, Typeface.NORMAL)

        binding.iconExplore.setImageResource(R.drawable.new_explore_unselected_svg)
        binding.labelExplore.setTextColor(ContextCompat.getColor(this, R.color.gray))
        binding.labelExplore.setTypeface(null, Typeface.NORMAL)

        // Now highlight the selected one
        when (selectedMenuId) {
            R.id.menu_home -> {
                binding.iconHome.setImageResource(R.drawable.new_home_selected_svg)
                binding.labelHome.setTextColor(ContextCompat.getColor(this, R.color.rightlife))
                binding.labelHome.setTypeface(null, Typeface.BOLD)
            }

            R.id.menu_explore -> {
                binding.iconExplore.setImageResource(R.drawable.new_explore_selected_svg)
                binding.labelExplore.setTextColor(ContextCompat.getColor(this, R.color.rightlife))
                binding.labelExplore.setTypeface(null, Typeface.BOLD)
            }
        }
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
            if (NetworkUtils.isInternetAvailable(this)) {
                startActivity(Intent(this, SubscriptionPlanListActivity::class.java).apply {
                    putExtra("SUBSCRIPTION_TYPE", "SUBSCRIPTION_PLAN")
                })
            }else {
                showInternetError()
            }
            //finish()
        }

        bottomSheetDialog.show()
    }

    fun showHeader(show: Boolean) {
        binding.llCountDown.visibility = if (show && isCountDownVisible) View.VISIBLE else View.GONE
    }
    private fun showInternetError() {
        Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
    }

    fun showSubsribeLayout(show: Boolean) {
        if (!DashboardChecklistManager.paymentStatus) {
            binding.trialExpiredLayout.trialExpiredLayout.visibility = View.VISIBLE
            isTrialExpired = true
        }else{
            binding.trialExpiredLayout.trialExpiredLayout.visibility = View.GONE
            isTrialExpired = false
        }
    }

    private lateinit var billingClient: BillingClient



    private fun initBillingAndRecover() {

        billingClient = BillingClient.newBuilder(this)

            .setListener { billingResult, purchases ->

                // Optional: React to new purchases here if needed

            }.enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()

            )

            .build()



        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingSetupFinished(billingResult: BillingResult) {

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                    // Query in-app purchases

                    recoverInAppPurchases()

                    // Query subscriptions

                    recoverSubscriptions()

                }

            }



            override fun onBillingServiceDisconnected() {

                // Retry connection if needed

            }

        })

    }



    private val TAG = "BillingRecovery"

    private fun recoverInAppPurchases() {
        Log.d(TAG, "Starting recoverInAppPurchases()...")

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            Log.d(TAG, "INAPP queryPurchasesAsync() result: code=${billingResult.responseCode}, purchasesCount=${purchases.size}")


            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in purchases) {
                    Log.d(TAG, "Found INAPP purchase: token=${purchase.purchaseToken}, state=${purchase.purchaseState}, acknowledged=${purchase.isAcknowledged}")

                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        Log.d(TAG, "Processing INAPP purchase: ${purchase.purchaseToken}")
                        consumeIfNeeded(purchase)
                    }
                }
            } else {
                Log.d(TAG, "INAPP purchase query failed: ${billingResult.debugMessage}")
            }
        }
    }

    private fun recoverSubscriptions() {
        Log.d(TAG, "Starting recoverSubscriptions()...")

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            Log.d(TAG, "SUBS queryPurchasesAsync() result: code=${billingResult.responseCode}, purchasesCount=${purchases.size}")

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in purchases) {
                    Log.d(TAG, "Found SUBS purchase: token=${purchase.purchaseToken}, state=${purchase.purchaseState}, acknowledged=${purchase.isAcknowledged}")

                    if (!purchase.isAcknowledged && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        Log.d(TAG, "Acknowledging subscription: ${purchase.purchaseToken}")
                        acknowledgeSubscription(purchase)
                    } else if (purchase.isAcknowledged) {
                        Log.d(TAG, "Subscription already acknowledged: ${purchase.purchaseToken} — consider updating backend/UI")
                        updateBackendForPurchase(purchase)
                    }
                }
            } else {
                Log.d(TAG, "SUBS purchase query failed: ${billingResult.debugMessage}")
            }
        }
    }

    private fun consumeIfNeeded(purchase: Purchase) {
        Log.d(TAG, "consumeIfNeeded() called for token=${purchase.purchaseToken}")

        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.consumeAsync(consumeParams) { result, _ ->
            Log.d(TAG, "consumeAsync() result: code=${result.responseCode} for token=${purchase.purchaseToken}")

            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Purchase consumed successfully: ${purchase.purchaseToken}")
                updateBackendForPurchase(purchase)
            } else {
                Log.d(TAG, "Failed to consume purchase: ${result.debugMessage}")
            }
        }
    }

    private fun acknowledgeSubscription(purchase: Purchase) {
        Log.d(TAG, "acknowledgeSubscription() called for token=${purchase.purchaseToken}")

        val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.acknowledgePurchase(acknowledgeParams) { result ->
            Log.d(TAG, "acknowledgePurchase() result: code=${result.responseCode} for token=${purchase.purchaseToken}")

            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Subscription acknowledged successfully: ${purchase.purchaseToken}")
                updateBackendForPurchase(purchase)
            } else {
                Log.d(TAG, "Failed to acknowledge subscription: ${result.debugMessage}")
            }
        }
    }

    private fun updateBackendForPurchase(purchase: Purchase) {
        Log.d(TAG, "Updating backend for purchase: token=${purchase.purchaseToken}, products=${purchase.products}")

        val paymentSuccessRequest = PaymentSuccessRequest().apply {
            planId = purchase.products.firstOrNull() ?: ""
            planName = purchase.products.firstOrNull() ?: ""
            paymentGateway = "googlePlay"
            orderId = purchase.orderId ?: ""
            environment = "payment"
            notifyType = "SDK"
            couponId = ""
            obfuscatedExternalAccountId = ""
            price = purchase.products.firstOrNull() ?: ""

            sdkDetail = SdkDetail().apply {
                price = ""
                orderId = purchase.orderId ?: ""
                title = ""
                environment = "payment"
                description = ""
                currencyCode = "INR"
                currencySymbol = "₹"
            }
        }

        saveSubscriptionSuccess(paymentSuccessRequest)
    }

    private fun saveSubscriptionSuccess(paymentSuccessRequest: PaymentSuccessRequest) {
        Log.d(TAG, "Calling saveSubscriptionSuccess() with planId=${paymentSuccessRequest.planId}, orderId=${paymentSuccessRequest.orderId}")

        val call = apiService.savePaymentSuccess(
            sharedPreferenceManager.accessToken,
            paymentSuccessRequest
        )
        call.enqueue(object : Callback<PaymentSuccessResponse> {
            override fun onResponse(
                call: Call<PaymentSuccessResponse>,
                response: Response<PaymentSuccessResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG, "savePaymentSuccess() success: ${response.body()}")
                } else {
                    Log.d(TAG, "savePaymentSuccess() failed: code=${response.code()}, message=${response.message()}")
                }
            }

            override fun onFailure(call: Call<PaymentSuccessResponse>, t: Throwable) {
                Log.d(TAG, "savePaymentSuccess() failed due to network or server error: ${t.localizedMessage}")
                handleNoInternetView(t)
            }
        })
    }


    fun showSwitchAccountDialog(context: Context, header: String, htmlText: String) {
        val dialog = Dialog(context)
        val binding = DialogSwitchAccountBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        // Set the dim amount
        val layoutParams = window!!.attributes
        layoutParams.dimAmount = 0.7f // Adjust the dim amount (0.0 - 1.0)
        window.attributes = layoutParams
        binding.btnSwitchAccount.setText("Cancel")
        binding.btnSwitchAccount.visibility = View.GONE
        binding.tvTitle.text = "Free Service Already Used on This Device"
        binding.tvDescription.text = "Looks like this device has already claimed a free service under another account. To continue, log out and switch to a new device."

        // Handle close button click
        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnSwitchAccount.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
