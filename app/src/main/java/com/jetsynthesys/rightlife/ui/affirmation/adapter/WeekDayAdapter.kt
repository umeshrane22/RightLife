package com.jetsynthesys.rightlife.ui.affirmation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ui.affirmation.pojo.WatchedAffirmationPlaylistData

class WeekDayAdapter(
    private val days: ArrayList<WatchedAffirmationPlaylistData>
) : RecyclerView.Adapter<WeekDayAdapter.DayViewHolder>() {

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val circle: ImageView = view.findViewById(R.id.dayCircle)
        val label: TextView = view.findViewById(R.id.dayLabel)
        val smallDot: ImageView = view.findViewById(R.id.smallDot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_circle, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.label.text = days[position].day?.first().toString()

        if (days[position].duration!! > 0) {
            holder.circle.setImageResource(R.drawable.correct_green)
            holder.smallDot.visibility = View.VISIBLE
        } else {
            holder.circle.setImageResource(R.drawable.circle_unselected)
            holder.smallDot.visibility = View.GONE
        }
    }

    override fun getItemCount() = days.size
}
