package com.jetsynthesys.rightlife.ai_package.ui.sleepright.fragment

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.data.repository.ApiClient
import com.jetsynthesys.rightlife.ai_package.model.SleepStageData
import com.jetsynthesys.rightlife.ai_package.model.SleepStageResponse
import com.jetsynthesys.rightlife.databinding.FragmentSleepStagesBinding
import com.google.android.material.snackbar.Snackbar
import com.jetsynthesys.rightlife.ai_package.ui.sleepright.customprogressbar.AwakeProgressBar
import com.jetsynthesys.rightlife.ai_package.ui.sleepright.customprogressbar.CoreProgressBar
import com.jetsynthesys.rightlife.ai_package.ui.sleepright.customprogressbar.DeepprogressBar
import com.jetsynthesys.rightlife.ai_package.ui.sleepright.customprogressbar.RemProgressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SleepStagesFragment : BaseFragment<FragmentSleepStagesBinding>() {

    private lateinit var customProgressBar: AwakeProgressBar
    private lateinit var transparentOverlay: View
    private lateinit var customProgressBarCardio: RemProgressBar
    private lateinit var transparentOverlayCardio: View
    private lateinit var customProgressBarLight: DeepprogressBar
    private lateinit var transparentOverlayLight: View
    private lateinit var customProgressBarFatBurn: CoreProgressBar
    private lateinit var transparentOverlayFatBurn: View
    private lateinit var progressDialog: ProgressDialog
    val remData : ArrayList<Float> = arrayListOf()
    val awakeData : ArrayList<Float> = arrayListOf()
    val coreData : ArrayList<Float> = arrayListOf()
    val deepData : ArrayList<Float> = arrayListOf()
    val formatters = DateTimeFormatter.ISO_DATE_TIME

    private lateinit var sleepStageResponse: SleepStageResponse

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSleepStagesBinding
        get() = FragmentSleepStagesBinding::inflate
    var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sleepChart = view.findViewById<SleepChartView>(R.id.sleepChart)
        val sleepData = listOf(
            SleepSegment(0.001f, resources.getColor(R.color.red_orange_bar), 110f,Position.UPPER),
            SleepSegment(0.101f, resources.getColor(R.color.blue_bar), 110f,Position.MIDDLE2),
            SleepSegment(0.151f, resources.getColor(R.color.purple_bar), 110f,Position.LOWER),
            SleepSegment(0.301f, resources.getColor(R.color.light_blue_bar), 110f,Position.MIDDLE1),
            SleepSegment(0.401f, resources.getColor(R.color.purple_bar), 110f,Position.LOWER),
            SleepSegment(0.451f, resources.getColor(R.color.blue_bar), 110f,Position.MIDDLE2),
            SleepSegment(0.551f, resources.getColor(R.color.light_blue_bar), 110f,Position.MIDDLE1),
            SleepSegment(0.661f, resources.getColor(R.color.red_orange_bar), 110f,Position.UPPER),
            SleepSegment(0.691f, resources.getColor(R.color.blue_bar), 110f,Position.MIDDLE2),
            SleepSegment(0.701f, resources.getColor(R.color.purple_bar), 110f,Position.LOWER),
            SleepSegment(0.861f, resources.getColor(R.color.light_blue_bar), 110f,Position.MIDDLE1)
        )
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)


        sleepChart.setSleepData(sleepData)
        fetchSleepData()

        customProgressBar = view.findViewById(R.id.customProgressBar)
        transparentOverlay = view.findViewById(R.id.transparentOverlay)
        customProgressBarCardio = view.findViewById(R.id.customProgressBarCardio)
        transparentOverlayCardio = view.findViewById(R.id.transparentOverlayCardio)
        customProgressBarLight = view.findViewById(R.id.customProgressBarLight)
        transparentOverlayLight = view.findViewById(R.id.transparentOverlayLight)
        customProgressBarFatBurn = view.findViewById(R.id.customProgressBarFatBurn)
        transparentOverlayFatBurn = view.findViewById(R.id.transparentOverlayFatBurn)

        // Setup progress bars
        customProgressBar.post {
            val progressBarWidth = customProgressBar.width
            val overlayWidth = (progressBarWidth * 0.5).toInt()
            val layoutParams = transparentOverlay.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.width = overlayWidth
            transparentOverlay.layoutParams = layoutParams
        }

        customProgressBarCardio.post {
            val progressBarWidth = customProgressBarCardio.width
            val overlayWidth = (progressBarWidth * 0.5).toInt()
            val layoutParams = transparentOverlayCardio.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.width = overlayWidth
            transparentOverlayCardio.layoutParams = layoutParams
        }

        customProgressBarLight.post {
            val progressBarWidth = customProgressBarLight.width
            val overlayWidth = (progressBarWidth * 0.5).toInt()
            val layoutParams = transparentOverlayLight.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.width = overlayWidth
            transparentOverlayLight.layoutParams = layoutParams
        }

        customProgressBarFatBurn.post {
            val progressBarWidth = customProgressBarFatBurn.width
            val overlayWidth = (progressBarWidth * 0.5).toInt()
            val layoutParams = transparentOverlayFatBurn.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.width = overlayWidth
            transparentOverlayFatBurn.layoutParams = layoutParams
        }

        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(SleepRightLandingFragment(), "SleepRightLandingFragment")
            }
        })
    }

    private fun fetchSleepData() {
        progressDialog.show()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkIjoiNjdhNWZhZTkxOTc5OTI1MTFlNzFiMWM4Iiwicm9sZSI6InVzZXIiLCJjdXJyZW5jeVR5cGUiOiJJTlIiLCJmaXJzdE5hbWUiOiJBZGl0eWEiLCJsYXN0TmFtZSI6IlR5YWdpIiwiZGV2aWNlSWQiOiJCNkRCMTJBMy04Qjc3LTRDQzEtOEU1NC0yMTVGQ0U0RDY5QjQiLCJtYXhEZXZpY2VSZWFjaGVkIjpmYWxzZSwidHlwZSI6ImFjY2Vzcy10b2tlbiJ9LCJpYXQiOjE3MzkxNzE2NjgsImV4cCI6MTc1NDg5NjQ2OH0.koJ5V-vpGSY1Irg3sUurARHBa3fArZ5Ak66SkQzkrxM"
        val userId = "user_test_1"
        val date = "2025-03-18"
        val source = "apple"
        val call = ApiClient.apiServiceFastApi.fetchSleepStage(userId, source, date)
        call.enqueue(object : Callback<SleepStageResponse> {
            override fun onResponse(call: Call<SleepStageResponse>, response: Response<SleepStageResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    sleepStageResponse = response.body()!!
                    setSleepRightStageData(sleepStageResponse)
                } else {
                    Log.e("Error", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
            override fun onFailure(call: Call<SleepStageResponse>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        })
    }

    private fun setSleepRightStageData(sleepStageResponse: SleepStageResponse) {
        val sleepStageData : ArrayList<SleepStageData> = arrayListOf()
        if (sleepStageResponse?.sleepStageData != null) {
            for (i in 0 until sleepStageResponse.sleepStageData.size) {
                sleepStageResponse.sleepStageData.getOrNull(i)?.let {
                    sleepStageData.add(it)
                }
                if (sleepStageResponse.sleepStageData.getOrNull(i)?.entryValue == "REM"){
                    val startDateTime = LocalDateTime.parse(sleepStageResponse.sleepStageData.getOrNull(i)?.startDatetime, formatters)
                    val endDateTime = LocalDateTime.parse(sleepStageResponse.sleepStageData.getOrNull(i)?.endDatetime, formatters)
                    val duration = Duration.between(startDateTime, endDateTime)
                    remData.add(duration.toHours().toFloat())
                }
                if (sleepStageResponse.sleepStageData.getOrNull(i)?.entryValue == "Deep"){
                    val startDateTime = LocalDateTime.parse(sleepStageResponse.sleepStageData.getOrNull(i)?.startDatetime, formatters)
                    val endDateTime = LocalDateTime.parse(sleepStageResponse.sleepStageData.getOrNull(i)?.endDatetime, formatters)
                    val duration = Duration.between(startDateTime, endDateTime)
                    deepData.add(duration.toHours().toFloat())
                }
                if (sleepStageResponse.sleepStageData.getOrNull(i)?.entryValue == "Core"){
                    val startDateTime = LocalDateTime.parse(sleepStageResponse.sleepStageData.getOrNull(i)?.startDatetime, formatters)
                    val endDateTime = LocalDateTime.parse(sleepStageResponse.sleepStageData.getOrNull(i)?.endDatetime, formatters)
                    val duration = Duration.between(startDateTime, endDateTime)
                    coreData.add(duration.toHours().toFloat())

                }
                if (sleepStageResponse.sleepStageData.getOrNull(i)?.entryValue == "Awake"){
                    val startDateTime = LocalDateTime.parse(sleepStageResponse.sleepStageData.getOrNull(i)?.startDatetime, formatters)
                    val endDateTime = LocalDateTime.parse(sleepStageResponse.sleepStageData.getOrNull(i)?.endDatetime, formatters)
                    val duration = Duration.between(startDateTime, endDateTime)
                    awakeData.add(duration.toHours().toFloat())

                }
            }
        }

    }

    private fun calculateTimeDifference(){



    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }
}


class SleepChartView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        textSize = 32f
        textAlign = Paint.Align.CENTER
    }
    private val sleepData = mutableListOf<SleepSegment>()

    fun setSleepData(data: List<SleepSegment>) {
        sleepData.clear()
        sleepData.addAll(data)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        val middle1Y = h * 0.45f
        val middle2Y = h * 0.65f
        val upperY = h * 0.3f
        val lowerY = h * 0.8f
        val barHeight = h * 0.15f
        val cornerRadius = barHeight / 5f

        var currentX = 0f  // Start drawing from the leftmost side

        for (segment in sleepData) {
            paint.color = segment.color

            val segmentWidth = segment.width * w
            val left = currentX
            val right = left + segmentWidth

            val top = when (segment.position) {
                Position.UPPER -> upperY - barHeight
                Position.MIDDLE1 -> middle1Y - barHeight / 2
                Position.MIDDLE2 -> middle2Y - barHeight / 2
                Position.LOWER -> lowerY
            }
            val bottom = top + barHeight

            canvas.drawRoundRect(RectF(left, top, right, bottom), cornerRadius, cornerRadius, paint)

            currentX += segmentWidth // Move to next segment's start position
        }
    }
}
data class SleepSegment(val width:Float, val color: Int,val height: Float, val position: Position)
enum class Position { UPPER, MIDDLE1, MIDDLE2, LOWER }