package com.example.rlapp.ai_package.ui.eatright.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

import com.example.rlapp.ai_package.ui.eatright.model.WeekData
import kotlin.math.abs

class WeekAdapter(private val weekList: List<WeekData>) :
    RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_week_ai, parent, false)
        return WeekViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val weekData = weekList[position]
        holder.bind(weekData)
    }

    override fun getItemCount(): Int = weekList.size

    class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtCalorieStatus: TextView = itemView.findViewById(R.id.txtCalorieStatus)
        private val layoutBackground: LinearLayout = itemView.findViewById(R.id.layoutBackground)

        fun bind(weekData: WeekData) {
            txtCalorieStatus.text = if (weekData.calories > 0) "Surplus ${weekData.calories} kCal"
            else "Deficit ${abs(weekData.calories)}"

            layoutBackground.setBackgroundColor(
                if (weekData.calories > 0) Color.GREEN else Color.RED
            )
        }
    }
}
