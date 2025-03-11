package com.example.rlapp.ui.affirmation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ui.affirmation.pojo.WatchedAffirmationPlaylistData

class WeekDayAdapter(
    private val days: ArrayList<WatchedAffirmationPlaylistData>
) : RecyclerView.Adapter<WeekDayAdapter.DayViewHolder>() {

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val circle: ImageView = view.findViewById(R.id.dayCircle)
        val label: TextView = view.findViewById(R.id.dayLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_circle, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.label.text = days[position].day?.first().toString()

        if (days[position].duration!! > 0) {
            holder.circle.setImageResource(R.drawable.circle_selected)
        } else {
            holder.circle.setImageResource(R.drawable.circle_unselected)
        }
    }

    override fun getItemCount() = days.size
}
