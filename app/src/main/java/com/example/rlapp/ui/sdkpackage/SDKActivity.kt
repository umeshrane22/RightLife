package com.example.rlapp.ui.sdkpackage

import ai.nuralogix.anurasdk.camera.CameraCapability
import ai.nuralogix.anurasdk.camera.CameraInfo
import ai.nuralogix.anurasdk.core.entity.MeasurementQuestionnaire
import ai.nuralogix.anurasdk.core.result.MeasurementResults
import ai.nuralogix.anurasdk.error.AnuraError
import ai.nuralogix.anurasdk.utils.AnuLogUtil
import android.Manifest
import android.app.Activity
import android.content.Context
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sondeservices.common.HealthCheckType
import com.sondeservices.edge.inference.InferenceCallback
import com.sondeservices.edge.init.SondeEdgeSdk
import com.sondeservices.edge.ml.model.Gender
import com.sondeservices.edge.ml.model.MetaData
import com.sondeservices.edge.ml.model.Score
import com.sondeservices.edge.ml.model.VFFinalScore
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import kotlin.system.exitProcess

class SDKActivity : AppCompatActivity() {

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




        exampleStartViewModel.readyToMeasure.observe(this) { handleTokenVerified(it) }
        exampleStartViewModel.error.observe(this) { handleError(it) }

        initialize()
        if (requestPermission()) {
            //registerScan()

            val file = intent.getStringExtra("FILEPATH")
            registerVoiceScan(file!!)

        }

    }




    private fun handleTokenVerified(isTokenVerified: Boolean) {
        Log.d(TAG, "=============>handleTokenVerified")
        if (isTokenVerified) {
           // registerVoiceScan(filePath = )
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
        val intent = Intent(this@SDKActivity, RlAnuraMeasurementActivity::class.java)
        startActivityForResult(intent, ANURA_REQUEST)
    }

    private fun showInvalidInputErrorToast(inputWithError: String) {
        Toast.makeText(this, "Invalid Inputs: $inputWithError", Toast.LENGTH_SHORT).show()
//        returnErrorResult("Invalid Inputs: $inputWithError")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == ANURA_REQUEST) {
            Log.d(TAG, "results222=${data?.getStringExtra("result")}")
            val jsonObject = data?.getStringExtra("result")?.let { JSONObject(it) }

            var bmi = 0.0
            var snr = 0.0
            bmi = jsonObject?.getDouble("bmi")!!
            snr = jsonObject.getDouble("snr")
            Log.d(TAG,"result bmi = $snr")
        } else {
            Log.d(TAG, "results= error")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    // Voice scan
    private fun registerVoiceScan(filePath : String){
        SondeEdgeSdk.createInferenceEngine(
            this,
            MetaData(Gender.MALE)
        )
        SondeEdgeSdk.inferScore(filePath, HealthCheckType.MENTAL_FITNESS,
            object : InferenceCallback {
                override fun onScore(score: Score) {
                    Log.d("AAAA","SCORE RECEIVED")
                    val vfFinalScore: VFFinalScore = score as VFFinalScore
                    var arrayList: ArrayList<Pair<String,String>> = arrayListOf()
                    arrayList.add(Pair("Score",vfFinalScore.getValue().toString()))
                    for(index in 0 until vfFinalScore.getVFScores().size){
                        arrayList.add( Pair(vfFinalScore.getVFScores()[index].getName().toString(),vfFinalScore.getVFScores()[index].getRawValue().toString()))
                    }
                    Log.d("AAAA", "Arraylist = $arrayList")
                    //_result?.success(arrayList.toMap());
                }
                override fun onError(throwable: Throwable) {
                    Log.d("throwable", "$throwable")
                }
            })
    }

    fun createEmptyFile(context: Context, fileName: String): String {
        // Create an empty file in the app's internal storage
        val file = File(context.filesDir, fileName)
        if (!file.exists()) {
            file.createNewFile() // Create the empty file
        }
        return file.absolutePath // Return the file's path
    }
}