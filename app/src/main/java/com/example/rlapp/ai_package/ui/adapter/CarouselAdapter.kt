package com.example.rlapp.ai_package.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ai_package.model.CardItem
import com.example.rlapp.ai_package.ui.moveright.graphs.LineGraphViewWorkout
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
        private val avgHeartRateText: TextView = itemView.findViewById(R.id.avg_heart_rate_text_value)
        private val lineGraph: LineGraphViewWorkout = itemView.findViewById(R.id.line_graph_workout)
        private val noDataIcon: ImageView = itemView.findViewById(R.id.no_data_workout_icon)
        private val noDataText: TextView = itemView.findViewById(R.id.no_data_text_workout)
        private val leftTimeLabel: TextView = itemView.findViewById(R.id.left_time_label)
        private val rightTimeLabel: TextView = itemView.findViewById(R.id.right_time_label)

        fun bind(item: CardItem, position: Int) {
            cardTitle.text = item.title
            durationText.text = item.duration
            caloriesText.text = item.caloriesBurned
            avgHeartRateText.text = item.avgHeartRate
            if (item.heartRateData.isNotEmpty()) {
                lineGraph.visibility = View.VISIBLE
                itemView.findViewById<View>(R.id.timeline_layout).visibility = View.VISIBLE
                noDataIcon.visibility = View.GONE
                noDataText.visibility = View.GONE
                val heartRates = item.heartRateData.map { it.heartRate.toFloat() }
                lineGraph.updateData(heartRates)
                leftTimeLabel.text = item.heartRateData.firstOrNull()?.date?.let { formatTime(it) } ?: "N/A"
                rightTimeLabel.text = item.heartRateData.lastOrNull()?.date?.let { formatTime(it) } ?: "N/A"
            } else {
                lineGraph.visibility = View.GONE
                itemView.findViewById<View>(R.id.timeline_layout).visibility = View.GONE
                noDataIcon.visibility = View.VISIBLE
                noDataText.visibility = View.VISIBLE
            }

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