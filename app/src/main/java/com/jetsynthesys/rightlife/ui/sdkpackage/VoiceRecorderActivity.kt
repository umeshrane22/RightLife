package com.jetsynthesys.rightlife.ui.sdkpackage


import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants
import com.jetsynthesys.rightlife.ui.voicescan.VoiceScanCheckInRequest
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
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VoiceRecorderActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var tvPercentage: TextView
    private lateinit var tvYourTopic: TextView

    companion object {
        const val PERMISSION_REQUEST_CODE = 101
        const val TIME_DURATION_RECORDING = 30000L
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

            val progress =
                ((TIME_DURATION_RECORDING - millisUntilFinished).toFloat() / TIME_DURATION_RECORDING.toFloat()) * 100
            Log.d("AAAA", "progress $progress")
            progressBar.progress = progress.toInt() + 1

            // Update the text with the progress
            val secondsRemaining = millisUntilFinished / 1000
            tvPercentage.text = "$secondsRemaining"
        }

        override fun onRecordingFinish(filePath: String) {
            Log.d("AAAA", "onRecordingFinish filepath $filePath")
            registerVoiceScan(filePath)
            showDialogAfterSubmit(
                R.drawable.layer_1,
                "Improve your mood and lower your anxiety risk.\n Keep your body hydrated!"
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start_recording)

        tvPercentage = findViewById(R.id.percentageText)
        progressBar = findViewById(R.id.progressBar)
        tvYourTopic = findViewById(R.id.tv_selected_topic)

        val yourTopic = intent.getStringExtra("description")
        tvYourTopic.text = yourTopic

        answerId = intent.getStringExtra("answerId")


        sondeAudioRecorder = SondeAudioRecorder(baseContext)

        sondeAudioRecorder.beginRecording(TIME_DURATION_RECORDING, timerRecordingListener)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)

            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        }

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
                TIME_DURATION_RECORDING, timerRecordingListener
            )
        }
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

    private fun showDialogAfterSubmit(
        imageResource: Int,
        text: String,
        isContinue: Boolean = true
    ) {
        val dialog = Dialog(this@VoiceRecorderActivity, android.R.style.Theme_Light)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_after_voicescan)

        val image: ImageView = dialog.findViewById(R.id.image_view)
        val textView: TextView = dialog.findViewById(R.id.tv_selected_topic)

        image.setImageResource(imageResource)
        textView.text = text

        Handler().postDelayed({
            dialog.dismiss()
            if (isContinue)
                showDialogAfterSubmit(
                    R.drawable.layer_1_1, "We respect your privacy. \n" +
                            "Your data is stored securely.", false
                )
        }, 3000)

        dialog.show()
    }

    fun submitAnswerRequest(requestAnswer: VoiceScanCheckInRequest?) {
        val sharedPreferences =
            getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null)

        Log.d("Access Token", "Token: $accessToken")

        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val call = apiService.voiceScanCheckInCreate(accessToken, requestAnswer)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful && response.body() != null) {
                    val voicecheckinresposne = response.body()!!.string()
                    Log.d("API Response", "Success: " + response.body().toString())

                    val jsonObject = JSONObject(voicecheckinresposne)
                    val checkInId = jsonObject.getString("checkInId")
                    val message = jsonObject.getString("successMessage")
                    Toast.makeText(
                        this@VoiceRecorderActivity,
                        "Response : $message",
                        Toast.LENGTH_SHORT
                    ).show()
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

    override fun onDestroy() {
        super.onDestroy()
        sondeAudioRecorder.interruptAndDiscardRecording()
    }

}