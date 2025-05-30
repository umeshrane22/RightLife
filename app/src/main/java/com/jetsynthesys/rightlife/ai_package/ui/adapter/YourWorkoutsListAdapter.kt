package com.jetsynthesys.rightlife.ai_package.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.ui.moveright.DeleteWorkoutBottomSheet
import com.jetsynthesys.rightlife.ai_package.ui.moveright.WorkoutModel

class YourWorkoutsListAdapter(
    private val context: Context,
    private var dataLists: ArrayList<WorkoutModel>,
    private var clickPos: Int,
    private var workoutData: WorkoutModel?,
    private var isClickView: Boolean,
    val onWorkoutItemClick: (WorkoutModel, Int, Boolean) -> Unit
) : RecyclerView.Adapter<YourWorkoutsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_your_workouts_ai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataLists[position]

        // Bind data to views
        holder.mealTitle.text = item.workoutType
        holder.servesCount.text = item.duration
        holder.calValue.text = item.caloriesBurned
        holder.subtractionValue.text = item.intensity

        // Hide unused views
        holder.mealName.visibility = View.GONE
        holder.delete.visibility = View.GONE
        holder.circlePlus.visibility = View.VISIBLE

        // Set up edit button click listener
        holder.edit.setOnClickListener {
            val bottomSheet = DeleteWorkoutBottomSheet()
            bottomSheet.show((context as AppCompatActivity).supportFragmentManager, "EditWorkoutBottomSheet")
        }

        // Set up item click listener
        holder.itemView.setOnClickListener {
            onWorkoutItemClick(item, position, true)
        }
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealTitle: TextView = itemView.findViewById(R.id.tv_meal_title)
        val delete: ImageView = itemView.findViewById(R.id.image_delete)
        val edit: ImageView = itemView.findViewById(R.id.image_edit)
        val circlePlus: ImageView = itemView.findViewById(R.id.image_circle_plus)
        val mealName: TextView = itemView.findViewById(R.id.tv_meal_name)
        val serve: ImageView = itemView.findViewById(R.id.image_serve)
        val serves: TextView = itemView.findViewById(R.id.tv_serves)
        val servesCount: TextView = itemView.findViewById(R.id.tv_serves_count)
        val cal: ImageView = itemView.findViewById(R.id.image_cal)
        val calValue: TextView = itemView.findViewById(R.id.tv_cal_value)
        val calUnit: TextView = itemView.findViewById(R.id.tv_cal_unit)
        val subtraction: ImageView = itemView.findViewById(R.id.image_subtraction)
        val subtractionValue: TextView = itemView.findViewById(R.id.tv_subtraction_value)
        val subtractionUnit: TextView = itemView.findViewById(R.id.tv_subtraction_unit)
        val baguette: ImageView = itemView.findViewById(R.id.image_baguette)
        val baguetteValue: TextView = itemView.findViewById(R.id.tv_baguette_value)
        val baguetteUnit: TextView = itemView.findViewById(R.id.tv_baguette_unit)
        val dewpoint: ImageView = itemView.findViewById(R.id.image_dewpoint)
        val dewpointValue: TextView = itemView.findViewById(R.id.tv_dewpoint_value)
        val dewpointUnit: TextView = itemView.findViewById(R.id.tv_dewpoint_unit)
    }

    fun addAll(items: ArrayList<WorkoutModel>?, pos: Int, workoutItem: WorkoutModel?, isClick: Boolean) {
        dataLists.clear()
        if (items != null) {
            dataLists = items
            clickPos = pos
            workoutData = workoutItem
            isClickView = isClick
        }
        notifyDataSetChanged()
    }
}