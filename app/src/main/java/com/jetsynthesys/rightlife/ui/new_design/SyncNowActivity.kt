package com.jetsynthesys.rightlife.ui.new_design

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.lifecycle.lifecycleScope
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.BaseActivity
import com.jetsynthesys.rightlife.ui.CommonAPICall
import com.jetsynthesys.rightlife.ui.new_design.pojo.LoggedInUser
import com.jetsynthesys.rightlife.ui.utility.AppConstants
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import kotlinx.coroutines.launch

class SyncNowActivity : BaseActivity() {

    private lateinit var healthConnectClient: HealthConnectClient
    private val allReadPermissions = setOf(
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(SpeedRecord::class),
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class),
        HealthPermission.getReadPermission(OxygenSaturationRecord::class),
        HealthPermission.getReadPermission(RespiratoryRateRecord::class),
        HealthPermission.getReadPermission(BodyFatRecord::class),
        HealthPermission.getReadPermission(RestingHeartRateRecord::class),
        HealthPermission.getReadPermission(BasalMetabolicRateRecord::class),
        HealthPermission.getReadPermission(HeartRateVariabilityRmssdRecord::class),
        HealthPermission.getReadPermission(BloodPressureRecord::class)
    )

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChildContentView(R.layout.activity_sync_now)

        var header = intent.getStringExtra("WellnessFocus")
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        if (header.isNullOrEmpty()){
            header = sharedPreferenceManager.selectedWellnessFocus
        }

        val btnSyncNow = findViewById<LinearLayout>(R.id.btn_sync_now)
        val btnSkipForNOw = findViewById<Button>(R.id.btn_skip_for_now)

        btnSyncNow.setOnClickListener {
            val availabilityStatus = HealthConnectClient.getSdkStatus(this)
            if (availabilityStatus == HealthConnectClient.SDK_AVAILABLE) {
                healthConnectClient = HealthConnectClient.getOrCreate(this)
                lifecycleScope.launch {
                    requestPermissionsAndReadAllData(sharedPreferenceManager, header)
                }
            } else {
                installHealthConnect(this)
                //  Toast.makeText(this, "Please install or update Health Connect from the Play Store.", Toast.LENGTH_LONG).show()
            }
        }
        btnSkipForNOw.setOnClickListener {
            startNextActivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private suspend fun requestPermissionsAndReadAllData(
        sharedPreferenceManager: SharedPreferenceManager,
        header: String?
    ) {
        try {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (allReadPermissions.all { it in granted }) {
                startNextActivity()
            } else {
                requestPermissionsLauncher.launch(allReadPermissions.toTypedArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error checking permissions: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var header = intent.getStringExtra("WellnessFocus")
            val sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
            if (header.isNullOrEmpty()){
                header = sharedPreferenceManager.selectedWellnessFocus
            }
            if (permissions.values.all { it }) {
                lifecycleScope.launch {
                    // fetchAllHealthData()
                    CommonAPICall.updateChecklistStatus(this@SyncNowActivity, "sync_health_data", AppConstants.CHECKLIST_COMPLETED)
                }
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    // fetchAllHealthData()
                    CommonAPICall.updateChecklistStatus(this@SyncNowActivity, "sync_health_data", AppConstants.CHECKLIST_COMPLETED)
                }
            }
            startNextActivity()
        }

    private fun installHealthConnect(context: Context) {
        val uri =
            "https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun startNextActivity(){
        val loggedInUsers = sharedPreferenceManager.loggedUserList

        var loggedInUser: LoggedInUser? = null
        val iterator = loggedInUsers.iterator()
        while (iterator.hasNext()) {
            val user = iterator.next()
            if (sharedPreferenceManager.email == user.email) {
                iterator.remove() // Safe removal
                user.isOnboardingComplete = true
                loggedInUser = user
            }
        }

        if (loggedInUser != null){
            loggedInUsers.add(loggedInUser)
            sharedPreferenceManager.setLoggedInUsers(loggedInUsers)
        }
        sharedPreferenceManager.loggedUserList
        sharedPreferenceManager.clearOnboardingData()
        sharedPreferenceManager.syncNow = true
        startActivity(Intent(this, FreeTrialServiceActivity::class.java))
        finish()
    }
}