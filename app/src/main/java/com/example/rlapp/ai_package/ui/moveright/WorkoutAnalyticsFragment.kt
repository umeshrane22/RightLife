package com.example.rlapp.ai_package.ui.moveright

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.rlapp.R
import com.example.rlapp.ai_package.base.BaseFragment
import com.example.rlapp.ai_package.model.CardItem
import com.example.rlapp.ai_package.ui.home.HomeBottomTabFragment
import com.example.rlapp.ai_package.ui.moveright.customProgressBar.CardioStrippedProgressBar
import com.example.rlapp.ai_package.ui.moveright.customProgressBar.FatBurnStrippedProgressBar
import com.example.rlapp.ai_package.ui.moveright.customProgressBar.LightStrippedprogressBar
import com.example.rlapp.ai_package.ui.moveright.customProgressBar.StripedProgressBar
import com.example.rlapp.databinding.FragmentWorkoutAnalyticsBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class HRDataPoint(val time: Long, val bpm: Int)

class WorkoutAnalyticsFragment : BaseFragment<FragmentWorkoutAnalyticsBinding>() {
    private lateinit var heartRateGraph: CustomHeartRateGraph
    private lateinit var customProgressBar: StripedProgressBar
    private lateinit var transparentOverlay: View
    private lateinit var customProgressBarCardio: CardioStrippedProgressBar
    private lateinit var transparentOverlayCardio: View
    private lateinit var customProgressBarLight: LightStrippedprogressBar
    private lateinit var transparentOverlayLight: View
    private lateinit var customProgressBarFatBurn: FatBurnStrippedProgressBar
    private lateinit var transparentOverlayFatBurn: View
    private var cardItem: CardItem? = null

    private val dataPoints = mutableListOf<HRDataPoint>()
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWorkoutAnalyticsBinding
        get() = FragmentWorkoutAnalyticsBinding::inflate

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            cardItem = it.getSerializable("cardItem") as? CardItem
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        heartRateGraph = view.findViewById(R.id.heartRateGraph)
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

        // Back press handling
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToFragment(HomeBottomTabFragment(), "LandingFragment")
            }
        })

        // Retrieve the CardItem from the Bundle
        cardItem = arguments?.getSerializable("cardItem") as? CardItem

        // Set the CardItem data to the UI
        cardItem?.let { item ->
            // Set the title
            view.findViewById<TextView>(R.id.functional_strength_text).text = item.title

            // Set the timeline (start and end times)
            val startTime = item.heartRateData.firstOrNull()?.date?.let { formatTime(it) } ?: "N/A"
            val endTime = item.heartRateData.lastOrNull()?.date?.let { formatTime(it) } ?: "N/A"
            view.findViewById<TextView>(R.id.timeline_text).text = "$startTime to $endTime"

            // Set the duration, calories burned, and average heart rate
            view.findViewById<TextView>(R.id.duration_text).text = item.duration
            view.findViewById<TextView>(R.id.calories_text).text = item.caloriesBurned
            view.findViewById<TextView>(R.id.avg_heart_rate_text_value).text = item.avgHeartRate

            // Convert heartRateData to HRDataPoint and set it on the graph
            dataPoints.clear()
            item.heartRateData.forEach { heartRateData ->
                val time = parseTime(heartRateData.date)
                val bpm = heartRateData.heartRate.toInt()
                dataPoints.add(HRDataPoint(time, bpm))
            }
            heartRateGraph.setData(dataPoints)
        } ?: run {
            // Handle the case where no CardItem is provided
            view.findViewById<TextView>(R.id.functional_strength_text).text = "No Data"
        }
    }

    // Helper function to format the timestamp to a time string (e.g., "6:30 am")
    private fun formatTime(timestamp: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = sdf.parse(timestamp)
            timeFormat.format(date).lowercase()
        } catch (e: Exception) {
            "N/A"
        }
    }

    // Helper function to parse the timestamp to a Long (milliseconds)
    private fun parseTime(timestamp: String): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            sdf.parse(timestamp)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    private fun setProgressLineBarProgress(progressLineBar: View, progress: Int) {
        val progressPercentage = progress.coerceIn(0, 100)
        val totalWidth = progressLineBar.width
        val pinkWidth = (progressPercentage / 100.0 * totalWidth).toInt()
        val pinkDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.parseColor("#FFC0CB"))
            cornerRadius = 0f
        }
        val whiteGreyDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.WHITE)
            setStroke(2, Color.GRAY)
            cornerRadius = 0f
        }
        val layerDrawable = LayerDrawable(arrayOf(pinkDrawable, whiteGreyDrawable))
        layerDrawable.setLayerInset(0, 0, 0, totalWidth - pinkWidth, 0)
        layerDrawable.setLayerInset(1, pinkWidth, 0, 0, 0)
        progressLineBar.background = layerDrawable
    }
}