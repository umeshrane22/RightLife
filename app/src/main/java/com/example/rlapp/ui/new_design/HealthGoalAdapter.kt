package com.example.rlapp.ui.new_design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ui.new_design.pojo.HealthGoal

class HealthGoalAdapter(
    private val context: Context,
    private val healthGoalList: ArrayList<HealthGoal>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<HealthGoalAdapter.HealthGoalViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthGoalViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_health_goals, parent, false)
        return HealthGoalViewHolder(view)
    }

    override fun getItemCount(): Int {
        return healthGoalList.size
    }

    override fun onBindViewHolder(holder: HealthGoalViewHolder, position: Int) {
        holder.tvHeader.text = healthGoalList[position].header


        val colorStateListSelected = ContextCompat.getColorStateList(context, R.color.white)
        val colorStateList = ContextCompat.getColorStateList(context, R.color.color_green)


        val bgDrawable = AppCompatResources.getDrawable(context, R.drawable.bg_gray_border)
        val unwrappedDrawable =
            AppCompatResources.getDrawable(context, R.drawable.rounded_corder_border_gray)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(
            wrappedDrawable,
            ContextCompat.getColor(context, R.color.color_green)
        )

        holder.llHealthGoals.background =
            if (selectedPosition == position) wrappedDrawable else bgDrawable

        if (selectedPosition == position) {
            holder.imageView.setColorFilter(
                ContextCompat.getColor(context, R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            holder.tvHeader.setTextColor(context.getColor(R.color.white))
        }
        else {
            holder.imageView.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.txt_color_header
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
            holder.tvHeader.setTextColor(context.getColor(R.color.txt_color_header))
        }


        holder.itemView.setOnClickListener {

            selectedPosition = position

            onItemClickListener.onItemClick(healthGoalList[position])
            healthGoalList[position].isSelected = !healthGoalList[position].isSelected
            notifyDataSetChanged()
        }

    }

    fun interface OnItemClickListener {
        fun onItemClick(healthGoal: HealthGoal)
    }

    class HealthGoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image_health_goals)
        var tvHeader: TextView = itemView.findViewById(R.id.tv_health_goals)
        var llHealthGoals: LinearLayout = itemView.findViewById(R.id.ll_health_goals)
    }
}