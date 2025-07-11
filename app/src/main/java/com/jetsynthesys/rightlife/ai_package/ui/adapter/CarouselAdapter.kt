package com.jetsynthesys.rightlife.ai_package.ui.adapter

import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.model.CardItem
import com.jetsynthesys.rightlife.ai_package.ui.moveright.graphs.LineGraphViewWorkout
import java.text.SimpleDateFormat
import java.util.Locale

class CarouselAdapter(
    private val cardItems: List<CardItem>,
    private val onGraphClick: (CardItem, Int) -> Unit
) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_view_ai, parent, false)
        return CarouselViewHolder(view, onGraphClick)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = cardItems[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return cardItems.size
    }

    class CarouselViewHolder(
        itemView: View,
        private val onGraphClick: (CardItem, Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val cardTitle: TextView = itemView.findViewById(R.id.functional_strength_heading)
        private val durationText: TextView = itemView.findViewById(R.id.duration_text)
        private val caloriesText: TextView = itemView.findViewById(R.id.calories_text)
        private val workout_function_icon: ImageView = itemView.findViewById(R.id.workout_function_icon)
        private val avgHeartRateText: TextView = itemView.findViewById(R.id.avg_heart_rate_text_value)
        private val lineGraph: LineGraphViewWorkout = itemView.findViewById(R.id.line_graph_workout)
        private val noDataIcon: ImageView = itemView.findViewById(R.id.no_data_workout_icon)
        private val noDataText: TextView = itemView.findViewById(R.id.no_data_text_workout)
        private val noDataTextWorkoutLoggedManually: TextView = itemView.findViewById(R.id.no_data_text_workout_lpgged_manually)
        private val leftTimeLabel: TextView = itemView.findViewById(R.id.left_time_label)
        private val rightTimeLabel: TextView = itemView.findViewById(R.id.right_time_label)
        private val sourceIcon: ImageView = itemView.findViewById(R.id.sourceIcon)
        private val timeline_line1: View = itemView.findViewById(R.id.timeline_line1)
        private val timeline_line: View = itemView.findViewById(R.id.timeline_line)

        fun bind(item: CardItem, position: Int) {
            cardTitle.text = item.title
            //durationText.text = item.duration
            val durationStr = item.duration
            val spannable = SpannableString(durationStr)
            val hrIndex = durationStr.indexOf("hr")
            val minsIndex = durationStr.indexOf("mins")
            if (hrIndex != -1) {
                spannable.setSpan(AbsoluteSizeSpan(10, true), hrIndex, hrIndex + 2, 0) // "hr" 10sp
            }
            if (minsIndex != -1) {
                spannable.setSpan(AbsoluteSizeSpan(10, true), minsIndex, minsIndex + 4, 0) // "mins" 10sp
            }
            // Rest of the text (numbers) will remain 19sp (default or set in XML)
            durationText.text = spannable

           // caloriesText.text = item.caloriesBurned
            val caloriesStr =  item.caloriesBurned// e.g., "305 cal"
            val caloriesSpannable = SpannableString(caloriesStr)
            val calIndex = caloriesStr.indexOf("cal")
            if (calIndex != -1) {
                caloriesSpannable.setSpan(AbsoluteSizeSpan(10, true), calIndex, calIndex + 3, 0) // "cal" 10sp
            }
            caloriesText.text = caloriesSpannable
            avgHeartRateText.text = item.avgHeartRate
            if(item.isSynced){
                sourceIcon.visibility = View.VISIBLE
            }else{
                sourceIcon.visibility = View.GONE
            }
            if (item.heartRateData.isNotEmpty()) {
                lineGraph.visibility = View.VISIBLE
                itemView.findViewById<View>(R.id.timeline_layout).visibility = View.VISIBLE
                itemView.findViewById<View>(R.id.timeline_layout_nodata).visibility = View.GONE
                itemView.findViewById<CardView>(R.id.avg_heartrate_Layout).visibility = View.VISIBLE
                noDataIcon.visibility = View.GONE
                noDataText.visibility = View.GONE
                noDataTextWorkoutLoggedManually.visibility = View.GONE
                val heartRates = item.heartRateData.map { it.heartRate.toFloat() }
                lineGraph.updateData(heartRates)
                leftTimeLabel.text = item.heartRateData.firstOrNull()?.date?.let { formatTime(it) } ?: "N/A"
                rightTimeLabel.text = item.heartRateData.lastOrNull()?.date?.let { formatTime(it) } ?: "N/A"
                val displayMetrics = itemView.context.resources.displayMetrics
                val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
                val layoutParams = timeline_line.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.width = if (screenWidthDp < 600) {
                    itemView.context.resources.getDimensionPixelSize(R.dimen.timeline_width_small) // 50dp (reused dimen)
                } else {
                    itemView.context.resources.getDimensionPixelSize(R.dimen.timeline_width_large) // 56dp (reused dimen)
                }
                timeline_line.layoutParams = layoutParams
            } else {
                lineGraph.visibility = View.GONE
                itemView.findViewById<View>(R.id.timeline_layout).visibility = View.GONE
                itemView.findViewById<View>(R.id.timeline_layout_nodata).visibility = View.VISIBLE
                itemView.findViewById<CardView>(R.id.avg_heartrate_Layout).visibility = View.GONE

                noDataIcon.visibility = View.VISIBLE
                noDataText.visibility = View.VISIBLE
                noDataTextWorkoutLoggedManually.visibility = View.VISIBLE
                val displayMetrics = itemView.context.resources.displayMetrics
                val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
                val layoutParams = timeline_line1.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.width = if (screenWidthDp < 600) {
                    itemView.context.resources.getDimensionPixelSize(R.dimen.timeline_width_small) // 50dp (reused dimen)
                } else {
                    itemView.context.resources.getDimensionPixelSize(R.dimen.timeline_width_large) // 56dp (reused dimen)
                }
                timeline_line1.layoutParams = layoutParams
            }
            Glide.with(itemView.context)
                .load(item.icon)
                .placeholder(R.drawable.workout_function_icon)
                .into(workout_function_icon)

            lineGraph.setOnClickListener {
                onGraphClick(item, position)
            }
        }

        // Helper function to format the timestamp to a time string (e.g., "7:00 am")
        private fun formatTime(timestamp: String): String {
            return try {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = sdf.parse(timestamp)
                val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                timeFormat.format(date).lowercase()
            } catch (e: Exception) {
                "N/A"
            }
        }
    }
}