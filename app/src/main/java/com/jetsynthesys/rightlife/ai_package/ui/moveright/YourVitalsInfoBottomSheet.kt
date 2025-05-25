package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.R.color.transparent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.jetsynthesys.rightlife.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class YourVitalsInfoBottomSheet : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_meal_summary_info_bottom_sheet, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog = BottomSheetDialog(requireContext(), R.style.LoggedBottomSheetDialogTheme)
        dialog.setContentView(R.layout.fragment_frequently_logged)
        dialog.window?.setBackgroundDrawableResource(transparent)
        val title = view.findViewById<TextView>(R.id.title)
        val close = view.findViewById<ImageView>(R.id.close)
        val summary = view.findViewById<TextView>(R.id.tvMealSummary)
        val image = view.findViewById<ImageView>(R.id.image)

        image.setImageResource(R.drawable.active_heart_rate_zone)
        title.text = "Your Vitals"

        close.setOnClickListener {
            dismiss()
        }
        val htmlContent = """
        <h2>Your Vitals - A Summary?</h2>
        <p><b>Your Vitals</b> provides a quick snapshot of key heart and activity metrics to help you track your daily health. This section includes core health indicators, whether you use a wearable device or not. Each metric gives you insights into how your body is responding to movement, recovery, and overall wellness.</p>

<h3>Active Heart Rate Zones</h3>
<p><i>Available for both wearable and non-wearable users</i></p>
<p>Heart Rate Zones help you understand how much effort your body is putting into physical activity. They range from Light (low effort) to Peak (maximum intensity) and are personalized to your fitness level.</p>

<p><b>How Heart Rate Zones Are Calculated</b><br>
RightLife uses an advanced formula based on the <b>Karvonen Method</b>, which factors in your Max Heart Rate (MHR) and Resting Heart Rate (RHR) to create customized zones.</p>

<ul>
  <li><b>Wearable Users:</b> Your heart rate zones are updated daily using real-time heart rate data from your device.</li>
  <li><b>Non-Wearable Users:</b> Your heart rate zones are updated weekly based on your activity level, which RightLife tracks and calculates as you log workouts.</li>
</ul>

<h3>Resting Heart Rate (RHR) & Average Heart Rate</h3>
<p><i>Wearable users only</i></p>
<ul>
  <li><b>RHR:</b> The number of times your heart beats per minute while at rest. A lower RHR typically indicates better cardiovascular fitness.</li>
  <li><b>Average Daily HR:</b> Tracks how your heart rate fluctuates throughout the day to provide insights into your overall health and activity levels.</li>
</ul>

<h3>Heart Rate Variability (HRV)</h3>
<p><i>Synced from wearable OR measured through a facial scan for non-wearable users</i></p>
<p>HRV measures the small variations in time between heartbeats. A higher HRV usually indicates better recovery and stress resilience.</p>

<ul>
  <li><b>Wearable Users:</b> Automatically synced from your device.</li>
  <li><b>Non-Wearable Users:</b> Measured using our facial scan technology.</li>
</ul>

<h3>Active Burn</h3>
<p><i>Synced from wearable OR estimated using calorie calculations for non-wearable users</i></p>
<p>Active Burn tracks the calories you burn through movement and exercise.</p>

<ul>
  <li><b>Wearable Users:</b> Automatically calculated based on heart rate and movement data.</li>
  <li><b>Non-Wearable Users:</b> Estimated using MET (Metabolic Equivalent of Task) values based on activity intensity.</li>
</ul>

<p>Each of these cards provides deeper insights when tapped, helping you understand trends and make informed decisions about your health. Whether you're using a wearable or not, <b>Your Vitals</b> ensures you stay connected to your body’s key metrics.</p>

    """.trimIndent()
        summary.text = Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY)
    }

    companion object {
        const val TAG = "LoggedBottomSheet"
        @JvmStatic
        fun newInstance() = YourVitalsInfoBottomSheet().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
}

