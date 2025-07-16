package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.base.BaseFragment
import com.jetsynthesys.rightlife.ai_package.model.CardItem
import com.jetsynthesys.rightlife.ai_package.ui.home.HomeBottomTabFragment
import com.jetsynthesys.rightlife.ai_package.ui.moveright.customProgressBar.CardioStrippedProgressBar
import com.jetsynthesys.rightlife.ai_package.ui.moveright.customProgressBar.FatBurnStrippedProgressBar
import com.jetsynthesys.rightlife.ai_package.ui.moveright.customProgressBar.LightStrippedprogressBar
import com.jetsynthesys.rightlife.ai_package.ui.moveright.customProgressBar.StripedProgressBar
import com.jetsynthesys.rightlife.databinding.FragmentWorkoutAnalyticsBinding
import java.text.SimpleDateFormat
import java.util.Locale

data class HRDataPoint(val time: Long, val bpm: Int)

class WorkoutAnalyticsFragment : BaseFragment<FragmentWorkoutAnalyticsBinding>() {

    private lateinit var heartRateGraph: CustomHeartRateGraph
    private lateinit var customProgressBar: StripedProgressBar
    private lateinit var transparentOverlay: View
    private lateinit var workOutsAnalyticsBackButton: ImageView
    private lateinit var customProgressBarCardio: CardioStrippedProgressBar
    private lateinit var transparentOverlayCardio: View
    private lateinit var customProgressBarLight: LightStrippedprogressBar
    private lateinit var transparentOverlayLight: View
    private lateinit var customProgressBarFatBurn: FatBurnStrippedProgressBar
    private lateinit var transparentOverlayFatBurn: View
    private lateinit var light_percentage_value: TextView
    private lateinit var yourHeartRateZone : ImageView
    private lateinit var light_time_value: TextView
    private lateinit var fat_burn_time_value: TextView
    private lateinit var fat_burn_percentage_value: TextView
    private lateinit var cardio_text_time_value: TextView
    private lateinit var cardio_text_percentage_value: TextView
    private lateinit var peak_text_time_value: TextView
    private lateinit var light_text_percentage: TextView
    private var cardItem: CardItem? = null
    private var loadingOverlay: FrameLayout? = null

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
        workOutsAnalyticsBackButton = view.findViewById(R.id.back_button)
        light_percentage_value = view.findViewById(R.id.light_percentage_value)
        light_time_value = view.findViewById(R.id.light_time_value)
        fat_burn_time_value = view.findViewById(R.id.fat_burn_time_value)
        fat_burn_percentage_value = view.findViewById(R.id.fat_burn_percentage_value)
        cardio_text_time_value = view.findViewById(R.id.cardio_text_time_value)
        cardio_text_percentage_value = view.findViewById(R.id.cardio_text_percentage_value)
        peak_text_time_value = view.findViewById(R.id.peak_text_time_value)
        light_text_percentage = view.findViewById(R.id.light_text_percentage)
        yourHeartRateZone = view.findViewById(R.id.yourHeartRateZone)

        workOutsAnalyticsBackButton.setOnClickListener {
            val fragment = HomeBottomTabFragment()
            val args = Bundle().apply {
                putString("ModuleName", "MoveRight")
            }
            fragment.arguments = args
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, fragment, "SearchWorkoutFragment")
                addToBackStack(null)
                commit()
            }
        }

        // Back press handling
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = HomeBottomTabFragment()
                val args = Bundle().apply {
                    putString("ModuleName", "MoveRight")
                }
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, fragment, "SearchWorkoutFragment")
                    addToBackStack(null)
                    commit()
                }
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

            // Set heart rate zone percentages and minutes
            light_text_percentage.text = "${item.heartRateZonePercentages.lightZone}%"
            light_percentage_value.text = "${item.heartRateZonePercentages.fatBurnZone}%"
            fat_burn_percentage_value.text = "${item.heartRateZonePercentages.cardioZone}%"
            cardio_text_percentage_value.text = "${item.heartRateZonePercentages.peakZone}%"
            peak_text_time_value.text = "${item.heartRateZoneMinutes.peakZone}min"
            cardio_text_time_value.text = "${item.heartRateZoneMinutes.cardioZone}min"
            fat_burn_time_value.text = "${item.heartRateZoneMinutes.fatBurnZone}min"
            light_time_value.text = "${item.heartRateZoneMinutes.lightZone}min"

            // Set progress bar percentages and overlay widths from heartRateZonePercentages
            customProgressBar.post {
                customProgressBar.progress = item.heartRateZonePercentages.peakZone
                val progressBarWidth = customProgressBar.width
                val filledWidth = (progressBarWidth * (item.heartRateZonePercentages.peakZone / 100.0)).toInt()
                val layoutParams = transparentOverlay.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.width = filledWidth
                layoutParams.startToStart = customProgressBar.id
                layoutParams.marginStart = 0
                transparentOverlay.layoutParams = layoutParams
            }

            customProgressBarLight.post {
                customProgressBarLight.progress = item.heartRateZonePercentages.lightZone
                val progressBarWidth = customProgressBarLight.width
                val filledWidth = (progressBarWidth * (item.heartRateZonePercentages.lightZone / 100.0)).toInt()
                val layoutParams = transparentOverlayLight.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.width = filledWidth
                layoutParams.startToStart = customProgressBarLight.id
                layoutParams.marginStart = 0
                transparentOverlayLight.layoutParams = layoutParams
            }

            customProgressBarFatBurn.post {
                customProgressBarFatBurn.progress = item.heartRateZonePercentages.fatBurnZone
                val progressBarWidth = customProgressBarFatBurn.width
                val filledWidth = (progressBarWidth * (item.heartRateZonePercentages.fatBurnZone / 100.0)).toInt()
                val layoutParams = transparentOverlayFatBurn.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.width = filledWidth
                layoutParams.startToStart = customProgressBarFatBurn.id
                layoutParams.marginStart = 0
                transparentOverlayFatBurn.layoutParams = layoutParams
            }

            customProgressBarCardio.post {
                customProgressBarCardio.progress = item.heartRateZonePercentages.cardioZone
                val progressBarWidth = customProgressBarCardio.width
                val filledWidth = (progressBarWidth * (item.heartRateZonePercentages.cardioZone / 100.0)).toInt()
                val layoutParams = transparentOverlayCardio.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.width = filledWidth
                layoutParams.startToStart = customProgressBarCardio.id
                layoutParams.marginStart = 0
                transparentOverlayCardio.layoutParams = layoutParams
            }

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
            Toast.makeText(requireContext(), "No workout data available", Toast.LENGTH_SHORT).show()
        }

        yourHeartRateZone.setOnClickListener {
            val yourHeartRateZonesInfoBottomSheet = YourHeartRateZonesInfoBottomSheet()
            yourHeartRateZonesInfoBottomSheet.isCancelable = true
            parentFragment.let { yourHeartRateZonesInfoBottomSheet.show(childFragmentManager, "YourHeartRateZonesInfoBottomSheet") }
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

    private fun showLoader(view: View) {
        loadingOverlay = view.findViewById(R.id.loading_overlay)
        loadingOverlay?.visibility = View.VISIBLE
    }

    private fun dismissLoader(view: View) {
        loadingOverlay = view.findViewById(R.id.loading_overlay)
        loadingOverlay?.visibility = View.GONE
    }
}