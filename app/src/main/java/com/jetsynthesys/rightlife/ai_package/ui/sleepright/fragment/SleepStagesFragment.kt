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
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.ai_package.ui.sleepright.customprogressbar.AwakeProgressBar
import com.jetsynthesys.rightlife.ai_package.ui.sleepright.customprogressbar.CoreProgressBar
import com.jetsynthesys.rightlife.ai_package.ui.sleepright.customprogressbar.DeepprogressBar
import com.jetsynthesys.rightlife.ai_package.ui.sleepright.customprogressbar.RemProgressBar
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.max

class SleepStagesFragment : BaseFragment<FragmentSleepStagesBinding>() {

    private lateinit var customProgressBar: AwakeProgressBar
    private lateinit var transparentOverlay: View
    private lateinit var customProgressBarCardio: RemProgressBar
    private lateinit var transparentOverlayCardio: View
    private lateinit var customProgressBarLight: DeepprogressBar
    private lateinit var transparentOverlayLight: View
    private lateinit var customProgressBarFatBurn: CoreProgressBar
    private lateinit var transparentOverlayFatBurn: View
    private lateinit var sleepChart: SleepChartView
    private lateinit var progressDialog: ProgressDialog
    val remData: ArrayList<Float> = arrayListOf()
    val awakeData: ArrayList<Float> = arrayListOf()
    val coreData: ArrayList<Float> = arrayListOf()
    val deepData: ArrayList<Float> = arrayListOf()
    val formatters = DateTimeFormatter.ISO_DATE_TIME

    private lateinit var sleepStageResponse: SleepStageResponse

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSleepStagesBinding
        get() = FragmentSleepStagesBinding::inflate
    var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sleepChart = view.findViewById<SleepChartView>(R.id.sleepChart)

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Loading")
        progressDialog.setCancelable(false)

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
            val layoutParams =
                transparentOverlayCardio.layoutParams as ConstraintLayout.LayoutParams
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
            val layoutParams =
                transparentOverlayFatBurn.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.width = overlayWidth
            transparentOverlayFatBurn.layoutParams = layoutParams
        }

        val backBtn = view.findViewById<ImageView>(R.id.img_back)

        backBtn.setOnClickListener {
            navigateToFragment(HomeBottomTabFragment(), "HomeBottomTabFragment")
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateToFragment(HomeBottomTabFragment(), "HomeBottomTabFragment")
                }
            })
    }

    private fun fetchSleepData() {
        progressDialog.show()
        val userid = SharedPreferenceManager.getInstance(requireActivity()).userId
            ?: "68010b615a508d0cfd6ac9ca"
        val date = "2025-03-18"
        val source = "apple"
        val call = ApiClient.apiServiceFastApi.fetchSleepStage(userid, source, date)
        call.enqueue(object : Callback<SleepStageResponse> {
            override fun onResponse(
                call: Call<SleepStageResponse>,
                response: Response<SleepStageResponse>
            ) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    sleepStageResponse = response.body()!!
                    if (sleepStageResponse.sleepStageAllData?.sleepStageData != null) {
                        sleepChart.setSleepData(sleepStageResponse.sleepStageAllData?.sleepStageData!!)
                    }
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
    private val sleepSegments = mutableListOf<Segment>()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    private data class Segment(
        val position: Position,
        val widthFraction: Float,
        val color: Int
    )

    fun setSleepData(data: List<SleepStageData>) {
        sleepSegments.clear()

        if (data.isEmpty()) return

        // Parse timestamps and calculate total duration
        val parsedData = data.mapNotNull {
            try {
                val start = dateFormat.parse(it.startDatetime)?.time ?: return@mapNotNull null
                val end = dateFormat.parse(it.endDatetime)?.time ?: return@mapNotNull null
                val durationMinutes = TimeUnit.MILLISECONDS.toMinutes(end - start).toInt()
                Triple(it.recordType, durationMinutes, getPositionForRecordType(it.recordType!!))
            } catch (e: Exception) {
                null
            }
        }

        val totalMinutes = parsedData.sumOf { it.second }

        parsedData.forEach { (recordType, duration, position) ->
            val fraction = duration.toFloat() / max(totalMinutes, 1)
            val color = getColorForRecordType(recordType!!)
            sleepSegments.add(Segment(position, fraction, color))
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        val barHeight = h * 0.15f
        val cornerRadius = barHeight / 2
        var currentX = 0f

        sleepSegments.forEach { segment ->
            paint.color = segment.color

            val top = when (segment.position) {
                Position.UPPER -> h * 0.05f
                Position.MIDDLE1 -> h * 0.25f
                Position.MIDDLE2 -> h * 0.5f
                Position.LOWER -> h * 0.75f
            }
            val bottom = top + barHeight
            val right = currentX + (segment.widthFraction * w)

            canvas.drawRoundRect(RectF(currentX, top, right, bottom), cornerRadius, cornerRadius, paint)
            currentX = right
        }
    }

    private fun getPositionForRecordType(type: String): Position {
        return when (type) {
            "Awake" -> Position.UPPER
            "Asleep" -> Position.MIDDLE1
            "In Bed" -> Position.MIDDLE2
            "Light Sleep" -> Position.LOWER
            else -> error("Unknown record type: $type")
        }
    }

    private fun getColorForRecordType(type: String): Int {
        return when (type) {
            "Awake" -> Color.RED
            "Asleep" -> Color.CYAN
            "In Bed" -> Color.BLUE
            "Light Sleep" -> Color.DKGRAY
            else -> error("Unknown record type: $type")
        }
    }
}

enum class Position {
    UPPER, MIDDLE1, MIDDLE2, LOWER
}

