package com.example.rlapp.ui.sdkpackage

import ai.nuralogix.anurasdk.camera.CameraCapability
import ai.nuralogix.anurasdk.camera.CameraInfo
import ai.nuralogix.anurasdk.core.entity.MeasurementQuestionnaire
import ai.nuralogix.anurasdk.error.AnuraError
import ai.nuralogix.anurasdk.utils.AnuLogUtil
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.rlapp.BuildConfig
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.ui.healthcam.HealthCamFacialScanRequest
import com.example.rlapp.ui.healthcam.ReportData
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.exitProcess

class HealthCamRecorderActivity : AppCompatActivity() {

    private val exampleStartViewModel: StartViewModel by viewModels { StartViewModel.Factory }

    private var faceIndex = 0

    companion object {
        const val TAG = "ANURA_MainActivity"
        const val MY_PERMISSIONS_REQUEST = 1
        const val ANURA_REQUEST = 100

        var DFX_REST_URL = BuildConfig.DFX_REST_URL
        var DFX_WS_URL = BuildConfig.DFX_WS_URL
        var LICENSE_KEY = BuildConfig.DFX_LICENSE_KEY
        var STUDY_ID = BuildConfig.DFX_STUDY_ID

        var USER_PROFILE_HEIGHT = "162"
        var USER_PROFILE_WEIGHT = "62"
        var USER_PROFILE_AGE = "36"
        var USER_PROFILE_GENDER = "male"
        var PARTNER_ID = "AndroidTest"
        var REPORT_ID = "6777ff3f24e05ea22043ebf9"
        var measurementQuestionnaire = MeasurementQuestionnaire()

        const val REST_SERVER_KEY = "rest_server_key"
        const val WS_SERVER_KEY = "ws_server_key"
        const val STUDY_ID_KEY = "study_id_key"
        const val PARTNER_ID_KEY = "partner_id_key"
        const val USER_PROFILE_HEIGHT_KEY = "user_profile_height_key"
        const val USER_PROFILE_WEIGHT_KEY = "user_profile_weight_key"
        const val USER_PROFILE_GENDER_KEY = "user_profile_gender_key"
        const val USER_PROFILE_AGE_KEY = "user_profile_age_key"

        const val PREF_NAME = "nura_sample_config"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*REPORT_ID = intent.getStringExtra("reportID").toString()
        USER_PROFILE_HEIGHT = intent.getStringExtra("USER_PROFILE_HEIGHT").toString()
        USER_PROFILE_WEIGHT = intent.getStringExtra("USER_PROFILE_WEIGHT").toString()
        USER_PROFILE_AGE = intent.getStringExtra("USER_PROFILE_AGE").toString()
        USER_PROFILE_GENDER = intent.getStringExtra("USER_PROFILE_GENDER").toString()*/


        exampleStartViewModel.readyToMeasure.observe(this) { handleTokenVerified(it) }
        exampleStartViewModel.error.observe(this) { handleError(it) }

        initialize()
        if (requestPermission()) {
            registerScan()
        }

    }


    private fun handleTokenVerified(isTokenVerified: Boolean) {
        Log.d(TAG, "=============>handleTokenVerified")
        if (isTokenVerified) {
            startScan()
        }
    }


    private fun startScan() {
        saveConfig()
        val anuraSupportedCameras: List<CameraInfo> =
            CameraCapability.createCameraCapabilityInstance(baseContext).getAnuraSupportedCameras(
                CameraInfo.CAMERA_CHECK_MIN_PIXEL_FLAG
            )
        if (anuraSupportedCameras.isEmpty()) {
            Toast.makeText(baseContext, "Camera does not support", Toast.LENGTH_LONG).show()
            return
        }
        faceIndex = 2
        if (USER_PROFILE_HEIGHT.isNotBlank()
            || USER_PROFILE_WEIGHT.isNotBlank()
            || USER_PROFILE_AGE.isNotBlank()
            || USER_PROFILE_GENDER.isNotBlank()
        ) {
            validateInputs(measurementQuestionnaire)
        } else {
            launchRLAnuraMeasurementActivity()
        }
    }

    private fun saveConfig() {
        val pref = getSharedPreferences(PREF_NAME, 0)
        val editor = pref.edit()
        editor.putString(REST_SERVER_KEY, DFX_REST_URL)
        editor.putString(WS_SERVER_KEY, DFX_WS_URL)
        editor.putString(LICENSE_KEY, LICENSE_KEY)
        editor.putString(STUDY_ID_KEY, STUDY_ID)
        editor.putString(USER_PROFILE_HEIGHT_KEY, USER_PROFILE_HEIGHT)
        editor.putString(USER_PROFILE_WEIGHT_KEY, USER_PROFILE_WEIGHT)
        editor.putString(USER_PROFILE_AGE_KEY, USER_PROFILE_AGE)
        editor.putString(USER_PROFILE_GENDER_KEY, USER_PROFILE_GENDER)
        editor.putString(PARTNER_ID_KEY, PARTNER_ID)
        editor.commit()
    }

    /**
     * Generic method to handle errors. Your application is responsible for gracefully handling
     * errors and explaining what's happening to your end users
     */
    private fun handleError(errorMsg: String?) {
        Toast.makeText(this, "Error:$errorMsg", Toast.LENGTH_SHORT).show()
//        returnErrorResult("Error:$errorMsg")
    }


    private fun initialize() {
        SharedPreferencesHelper.initialize(application)
        AnuLogUtil.setShowLog(BuildConfig.DEBUG)
    }

    private fun requestPermission(): Boolean {
        val permissionList = ArrayList<String>()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.CAMERA)
        }
        val permissionStrings = arrayOfNulls<String>(permissionList.size)
        permissionList.toArray(permissionStrings)
        return if (permissionList.size > 0) {
            ActivityCompat.requestPermissions(this, permissionStrings, MY_PERMISSIONS_REQUEST)
            false
        } else {
            AnuLogUtil.d(TAG, "have all the required permissions")
            true
        }
    }

    private fun registerScan() {
        /**
         * Check if the application has been configured with a DeepAffex License Key and Study ID
         */
        if (checkEmbeddedDeepAffexLicenseAndStudyID()) {
            lifecycleScope.launch {
                /**
                 * Before launching [RLAnuraMeasurementActivity], we need to ensure that the
                 * application has a valid DeepAffex Cloud access token. The application also needs
                 * to ensure it has the latest study configuration binary that's required to
                 * initialize DeepAffex Extraction Library
                 */
                exampleStartViewModel.verifyDeepAffexTokenAndStudyFile()
            }
        } else {
            /**
             * If either the DeepAffex License Key or Study ID are not configured, show an error
             * dialog box and exit the app
             */
            showExitAppDialog(
                "App Configuration Error",
                "Your DFX_LICENSE_KEY and DFX_STUDY_ID are not set in server.properties"
            )
        }
    }

    private fun checkEmbeddedDeepAffexLicenseAndStudyID(): Boolean {
        return !(BuildConfig.DFX_LICENSE_KEY.isEmpty() || BuildConfig.DFX_STUDY_ID.isEmpty())
    }

    private fun showExitAppDialog(title: String, msg: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton("Exit")
            { _, _ -> exitProcess(0) }
            .setCancelable(false)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && requestCode == MY_PERMISSIONS_REQUEST
        ) {
            registerScan()
        } else {
//             returnErrorResult("")
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    /**
     * Use the [MeasurementQuestionnaire] to validate and persist the user
     *  profile information
     */
    private fun validateInputs(
        measurementQuestionnaire: MeasurementQuestionnaire
    ) {
        var invalidInputs = ""

        if (USER_PROFILE_HEIGHT.isBlank()
            || (USER_PROFILE_HEIGHT.isNotBlank() && measurementQuestionnaire.setHeightInCm(
                USER_PROFILE_HEIGHT.toInt()
            ) == AnuraError.Core.INVALID_INPUT)
        ) {
            invalidInputs = invalidInputs.plus("|Height|")
        }
        if (USER_PROFILE_WEIGHT.isBlank() || (USER_PROFILE_WEIGHT.isNotBlank() && measurementQuestionnaire.setWeightInKg(
                USER_PROFILE_WEIGHT.toDouble().toInt()
            ) == AnuraError.Core.INVALID_INPUT)
        ) {
            invalidInputs = invalidInputs.plus("|Weight|")
        }
        if (USER_PROFILE_AGE.isBlank() || (USER_PROFILE_AGE.isNotBlank() && measurementQuestionnaire.setAge(
                USER_PROFILE_AGE.toInt()
            ) == AnuraError.Core.INVALID_INPUT)
        ) {
            invalidInputs = invalidInputs.plus("|Age|")
        }
        if (measurementQuestionnaire.setSexAssignedAtBirth(
                USER_PROFILE_GENDER
            ) == AnuraError.Core.INVALID_INPUT
        ) {
            invalidInputs = invalidInputs.plus("|Sex At Birth|")
        }

        if (invalidInputs.isNotBlank()) {
            showInvalidInputErrorToast(invalidInputs)
        } else {
            launchRLAnuraMeasurementActivity()
        }
    }

    private fun launchRLAnuraMeasurementActivity() {
        Log.d(
            TAG,
            "Start RLAnuraMeasurementActivity with UserInfo: " +
                    measurementQuestionnaire.toString() +
                    "partnerID=$PARTNER_ID"
        )
        val intent = Intent(this@HealthCamRecorderActivity, RlAnuraMeasurementActivity::class.java)
        startActivityForResult(intent, ANURA_REQUEST)
    }

    private fun showInvalidInputErrorToast(inputWithError: String) {
        Toast.makeText(this, "Invalid Inputs: $inputWithError", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == ANURA_REQUEST) {
            Log.d(TAG, "results222=${data?.getStringExtra("result")}")
            /*val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data?.getParcelableExtra(
                    "result",
                    MeasurementResults::class.java
                )
            } else {
                data?.getParcelableExtra("result")
            }*/

            val result = data?.getStringExtra("result")
            val jsonObject = JSONObject(result)

            val healthCamFacialScanRequest = HealthCamFacialScanRequest()
            healthCamFacialScanRequest.status = "SUCCESS"
            healthCamFacialScanRequest.reportId = REPORT_ID
            /*val reportData = ReportData()

            if (jsonObject.has("bmi") && !jsonObject.isNull("bmi"))
                reportData.bmi = jsonObject.getDouble("bmi")
            if (jsonObject.has("snr") && !jsonObject.isNull("snr"))
                reportData.snr = jsonObject.getDouble("snr")
            if (jsonObject.has("msi") && !jsonObject.isNull("msi"))
                reportData.msi = jsonObject.getDouble("msi")
            if (jsonObject.has("systolic") && !jsonObject.isNull("systolic"))
                reportData.systolic = jsonObject.getDouble("systolic")
            if (jsonObject.has("diastolic") && !jsonObject.isNull("diastolic"))
                reportData.diastolic = jsonObject.getDouble("diastolic")
            if (jsonObject.has("AGE") && !jsonObject.isNull("AGE"))
                reportData.age = jsonObject.getDouble("AGE")
            if (jsonObject.has("healthScore") && !jsonObject.isNull("healthScore"))
                reportData.healthScore1 = jsonObject.getDouble("healthScore")
            if (jsonObject.has("heartRateVariability") && !jsonObject.isNull("heartRateVariability"))
                reportData.heartRateVariability = jsonObject.getDouble("heartRateVariability")
            if (jsonObject.has("absi") && !jsonObject.isNull("absi"))
                reportData.absi = jsonObject.getDouble("absi")
            if (jsonObject.has("cvdRisk") && !jsonObject.isNull("cvdRisk"))
                reportData.cvdRisk = jsonObject.getDouble("cvdRisk")
            if (jsonObject.has("strokeRisk") && !jsonObject.isNull("strokeRisk"))
                reportData.strokeRisk = jsonObject.getDouble("strokeRisk")
            if (jsonObject.has("heartAttackRisk") && !jsonObject.isNull("heartAttackRisk"))
                reportData.heartAttackRisk = jsonObject.getDouble("heartAttackRisk")
            if (jsonObject.has("HDLTC_RISK_PROB") && !jsonObject.isNull("HDLTC_RISK_PROB"))
                reportData.hdltcRiskProb = jsonObject.getDouble("HDLTC_RISK_PROB")
            if (jsonObject.has("HPT_RISK_PROB") && !jsonObject.isNull("HPT_RISK_PROB"))
                reportData.hptRiskProb = jsonObject.getDouble("HPT_RISK_PROB")
            if (jsonObject.has("TG_RISK_PROB") && !jsonObject.isNull("TG_RISK_PROB"))
                reportData.tgRiskProb = jsonObject.getDouble("TG_RISK_PROB")
            if (jsonObject.has("VITAL_SCORE") && !jsonObject.isNull("VITAL_SCORE"))
                reportData.vitalScore = jsonObject.getDouble("VITAL_SCORE")
            if (jsonObject.has("waistToHeight") && !jsonObject.isNull("waistToHeight"))
                reportData.waistToHeight = jsonObject.getDouble("waistToHeight")
            if (jsonObject.has("WAIST_TO_HEIGHT") && !jsonObject.isNull("WAIST_TO_HEIGHT"))
                reportData.waistToHeight1 = jsonObject.getDouble("WAIST_TO_HEIGHT")
            if (jsonObject.has("BP_RPP") && !jsonObject.isNull("BP_RPP"))
                reportData.bpRpp = jsonObject.getDouble("BP_RPP")
            if (jsonObject.has("HRV_SDNN") && !jsonObject.isNull("HRV_SDNN"))
                reportData.hrvSdnn = jsonObject.getDouble("HRV_SDNN")
            if (jsonObject.has("BP_HEART_ATTACK") && !jsonObject.isNull("BP_HEART_ATTACK"))
                reportData.bpHeartAttack = jsonObject.getDouble("BP_HEART_ATTACK")
            if (jsonObject.has("WEIGHT") && !jsonObject.isNull("WEIGHT"))
                reportData.weight = jsonObject.getDouble("WEIGHT")
            if (jsonObject.has("irregularHeartBeats") && !jsonObject.isNull("irregularHeartBeats"))
                reportData.irregularHeartBeats = jsonObject.getDouble("irregularHeartBeats")
            if (jsonObject.has("PHYSICAL_SCORE") && !jsonObject.isNull("PHYSICAL_SCORE"))
                reportData.physioScore = jsonObject.getDouble("PHYSICAL_SCORE")
            if (jsonObject.has("ABSI") && !jsonObject.isNull("ABSI"))
                reportData.absi = jsonObject.getDouble("ABSI")
            if (jsonObject.has("BP_DIASTOLIC") && !jsonObject.isNull("BP_DIASTOLIC"))
                reportData.bpDiastolic = jsonObject.getDouble("BP_DIASTOLIC")
            if (jsonObject.has("BP_CVD") && !jsonObject.isNull("BP_CVD"))
                reportData.bpCvd = jsonObject.getDouble("BP_CVD")
            if (jsonObject.has("IHB_COUNT") && !jsonObject.isNull("IHB_COUNT"))
                reportData.ihbCount = jsonObject.getDouble("IHB_COUNT")
            if (jsonObject.has("HEALTH_SCORE") && !jsonObject.isNull("HEALTH_SCORE"))
                reportData.healtH_SCORE = jsonObject.getDouble("HEALTH_SCORE")
            if (jsonObject.has("BP_HEART_ATTACK") && !jsonObject.isNull("MENTAL_SCORE"))
                reportData.mentalScore = jsonObject.getDouble("MENTAL_SCORE")
            if (jsonObject.has("BP_STROKE") && !jsonObject.isNull("BP_STROKE"))
                reportData.bpStroke = jsonObject.getDouble("BP_STROKE")
            if (jsonObject.has("BMI_CALC") && !jsonObject.isNull("BMI_CALC"))
                reportData.bmiCalc = jsonObject.getDouble("BMI_CALC")
            if (jsonObject.has("BP_TAU") && !jsonObject.isNull("BP_TAU"))
                reportData.bpTau = jsonObject.getDouble("BP_TAU")
            if (jsonObject.has("BP_SYSTOLIC") && !jsonObject.isNull("BP_SYSTOLIC"))
                reportData.bpSystolic = jsonObject.getDouble("BP_SYSTOLIC")
            if (jsonObject.has("HEIGHT") && !jsonObject.isNull("HEIGHT"))
                reportData.height = jsonObject.getDouble("HEIGHT")
            if (jsonObject.has("MSI") && !jsonObject.isNull("MSI"))
                reportData.msi1 = jsonObject.getDouble("MSI")*/

            val  reportData1 = data?.getSerializableExtra("resultObject") as ReportData
            healthCamFacialScanRequest.reportData = reportData1

            submitReport(healthCamFacialScanRequest)

            Log.d("AAAA", result.toString())
        } else {
            Log.d(TAG, "results= error")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun submitReport(healthCamFacialScanRequest: HealthCamFacialScanRequest) {
        val accessToken = SharedPreferenceManager.getInstance(this).accessToken
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val gson = Gson()
        val json = gson.toJson(healthCamFacialScanRequest)

        val call = apiService.submitHealthCamReport(accessToken, healthCamFacialScanRequest)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    val gson = Gson()
                    val jsonResponse = gson.toJson(response.body()?.string())
                    Log.d("AAAA", jsonResponse)

                } else {
                    try {
                        if (response.errorBody() != null) {
                            val errorMessage = response.errorBody()!!.string()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    Toast.makeText(
                        this@HealthCamRecorderActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(
                    this@HealthCamRecorderActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}