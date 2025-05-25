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

class YourHeartRateZonesInfoBottomSheet : BottomSheetDialogFragment() {

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

        image.setImageResource(R.drawable.your_heart_hate_zone)
        title.text = "Your Heart Rate Zones"

        close.setOnClickListener {
            dismiss()
        }
        val htmlContent = """
        <h2>How does RightLife calculate your Heart Rate Zones?</h2>
        <h2>Why Heart Rate Zones Matter</h2>
<p>Heart rate (HR) zones are essential for optimizing workouts, ensuring that you’re training at the right intensity to achieve your fitness goals. However, many fitness apps and wearables rely on outdated formulas that fail to account for individual differences.</p>

<p><b>RightLife</b> takes a science-backed approach, using the <b>Karvonen Method</b>, a more accurate formula that personalizes HR zones based on both Maximum Heart Rate (MHR) and Resting Heart Rate (RHR). This means your HR zones adjust dynamically as your fitness improves, ensuring more precise and effective training.</p>

<h3>How RightLife Calculates Your HR Zones</h3>
<p>Unlike many wearables that use the generic <i>[220 - age]</i> formula, RightLife incorporates your resting HR, ensuring HR zones are tailored to you. Here’s how we do it:</p>

<ul>
  <li><b>Determine Your Maximum Heart Rate (MHR):</b><br>
  We use the scientifically-backed formula: <i>MHR = 208 - (0.7 × age)</i>.<br>
  If we have recent workout data, we compare it to your highest recorded HR and use the greater value.</li>

  <li><b>Find Your Resting Heart Rate (RHR):</b><br>
  If you track sleep with a wearable, we use your lowest recorded HR during sleep.<br>
  If not, we use your activity factor and demographic information to customize a Resting Heart Rate value. 
  <i>[Note: This unfortunately does not take into account days when your RHR shoots up, such as after consuming alcohol. We’re working on making this happen 🙂]</i></li>

  <li><b>Calculate Heart Rate Reserve (HRR):</b><br>
  <i>HRR = MHR - RHR</i><br>
  This measures your functional heart rate capacity and ensures more personalized HR zones.</li>

  <li><b>Break Down the HR Zones:</b>
    <ul>
      <li><b>Light Zone (Warm-Up & Recovery):</b> Ideal for warm-ups, cool-downs, and active recovery.</li>
      <li><b>Fat Burn Zone:</b> Best for steady-state cardio and weight loss.</li>
      <li><b>Cardio Zone:</b> Improves endurance, heart health, and aerobic fitness.</li>
      <li><b>Peak Zone (High-Intensity Training):</b> Used for interval training and pushing performance limits.</li>
    </ul>
  </li>
</ul>

<h3>How RightLife Stands Out</h3>
<p>Many fitness apps fail to personalize HR zones, leading to inaccurate intensity recommendations. RightLife improves on industry standards in the following ways:</p>

<ul>
  <li><b>Real-Time Adaptation:</b> Our HR zones adjust as your type of workout, overall fitness, sleep and stress change, making them more accurate over time.</li>
  <li><b>Better Workout Guidance:</b> Many users don’t fully understand HR zones, and because of that, struggle with how HR Zones can be used to improve their fitness. We cut right through that barrier with easy to understand zoning.</li>
  <li><b>Scientifically Validated Approach:</b> Our use of the Karvonen formula is supported by extensive research and excels in comparative studies with other methods.</li>
</ul>

<p><i>See “How does RL track HR Zones better than Polar and Garmin”</i></p>

<h3>Maximize Your Training with RightLife</h3>
<p>Your heart rate zones should work for you, not against you. With RightLife, you get an adaptive, data-driven approach that ensures every workout is optimized for efficiency, whether you're aiming for fat loss, endurance, or peak performance. Start training smarter with RightLife’s personalized HR zones.</p>
    """.trimIndent()
        summary.text = Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY)
    }

    companion object {
        const val TAG = "LoggedBottomSheet"
        @JvmStatic
        fun newInstance() = YourHeartRateZonesInfoBottomSheet().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
}

