package com.example.rlapp.ui.sdkpackage


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.rlapp.R
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.RetrofitData.ApiService
import com.example.rlapp.apimodel.UserAuditAnswer.UserAnswerRequest
import com.example.rlapp.ui.payment.AccessPaymentActivity
import com.example.rlapp.ui.utility.SharedPreferenceConstants
import com.example.rlapp.ui.voicescan.VoiceScanCheckInRequest
import com.example.rlapp.ui.voicescan.VoiceScanSubmitResponse
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.sondeservices.common.HealthCheckType
import com.sondeservices.edge.inference.InferenceCallback
import com.sondeservices.edge.init.SondeEdgeSdk
import com.sondeservices.edge.ml.model.Gender
import com.sondeservices.edge.ml.model.MetaData
import com.sondeservices.edge.ml.model.Score
import com.sondeservices.edge.ml.model.VFFinalScore
import com.sondeservices.edge.recorder.SondeAudioRecorder
import com.sondeservices.edge.recorder.listeners.TimerRecordingListener
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoiceRecorderActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE = 101
    }


    private lateinit var sondeAudioRecorder: SondeAudioRecorder
    private var answerId: String? = null


    private val timerRecordingListener = object : TimerRecordingListener {
        override fun onRecorderInitialized() {
            Log.d("AAAA", "onRecorderInitialized")
        }

        override fun onRecordingStarted() {
            //callback when recording started
            Log.d("AAAA", "onRecordingStarted")
        }

        override fun onError(throwable: Throwable) {
            // get callback for all type of errors
            Log.d("AAAA", "onError")
        }

        override fun onTick(millisUntilFinished: Long) {
            // callback for remaining time for recording
            Log.d("AAAA", "onTick $millisUntilFinished")
        }

        override fun onRecordingFinish(filePath: String) {
            Log.d("AAAA", "onRecordingFinish filepath $filePath")
            //startNewActivity(filePath)
            registerVoiceScan(filePath)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start_recording)


        sondeAudioRecorder = SondeAudioRecorder(baseContext)

        sondeAudioRecorder.beginRecording(3000, timerRecordingListener)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)

            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        }
        answerId = intent.getStringExtra("answerId")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()
            && grantResults[0] != PackageManager.PERMISSION_GRANTED
        ) {
            throw RuntimeException("Record Audio permission is required to start recording.")
        } else {
            sondeAudioRecorder.beginRecording(
                300000, timerRecordingListener
            )
        }
    }

    // Check if permissions are granted
    private fun checkPermissions(): Boolean {
        val recordPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        val writePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return recordPermission == PackageManager.PERMISSION_GRANTED &&
                writePermission == PackageManager.PERMISSION_GRANTED
    }

    // Request permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }


    private fun registerVoiceScan(filePath: String) {
        SondeEdgeSdk.createInferenceEngine(
            this,
            MetaData(Gender.MALE)
        )
        SondeEdgeSdk.inferScore(filePath, HealthCheckType.MENTAL_FITNESS,
            object : InferenceCallback {
                override fun onScore(score: Score) {
                    Log.d("AAAA", "SCORE RECEIVED")
                    val vfFinalScore: VFFinalScore = score as VFFinalScore
                    var arrayList: ArrayList<Pair<String, String>> = arrayListOf()
                    arrayList.add(Pair("Score", vfFinalScore.getValue().toString()))
                    for (index in 0 until vfFinalScore.getVFScores().size) {
                        arrayList.add(
                            Pair(
                                vfFinalScore.getVFScores()[index].getName().toString(),
                                vfFinalScore.getVFScores()[index].getRawValue().toString()
                            )
                        )
                    }
                    Log.d("AAAA", "Arraylist = $arrayList")
                    //_result?.success(arrayList.toMap());
                    val voiceScanCheckInRequest = VoiceScanCheckInRequest()
                    voiceScanCheckInRequest.score = vfFinalScore.getValue()
                    voiceScanCheckInRequest.answerId = answerId
                    voiceScanCheckInRequest.reportList = arrayList
                    submitAnswerRequest(voiceScanCheckInRequest)
                }

                override fun onError(throwable: Throwable) {
                    Log.d("throwable", "$throwable")
                }
            })
    }

    fun submitAnswerRequest(requestAnswer: VoiceScanCheckInRequest?) {
        val sharedPreferences =
            getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null)

        Log.d("Access Token", "Token: $accessToken")

        val apiService = ApiClient.getClient().create(ApiService::class.java)

        // Create a request body (replace with actual email and phone number)
        //SubmitLoginOtpRequest request = new SubmitLoginOtpRequest("+91"+mobileNumber,OTP,"ABC123","Asus ROG 6","hp","ABC123");

        // Make the API call
        val call = apiService.voiceScanCheckInCreate(accessToken, requestAnswer)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    //LoginResponseMobile loginResponse = response.body();
                    val voicecheckinresposne = response.body()!!.string()
                    Log.d("API Response", "Success: " + response.body().toString())
                    Log.d("API Response 2", "Success: " + response.body().toString())

                    val gson = Gson()
                    val jsonResponse = gson.toJson(response.body()!!.string())
                    Log.d("API Response body", "Success: $voicecheckinresposne")


                } else {
                    try {
                        if (response.errorBody() != null) {
                            val errorMessage = response.errorBody()!!.string()
                            println("Request failed with error: $errorMessage")
                            Log.d("API Response 2", "Success: $errorMessage")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    Toast.makeText(
                        this@VoiceRecorderActivity,
                        "Server Error: " + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(
                    this@VoiceRecorderActivity,
                    "Network Error: " + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}