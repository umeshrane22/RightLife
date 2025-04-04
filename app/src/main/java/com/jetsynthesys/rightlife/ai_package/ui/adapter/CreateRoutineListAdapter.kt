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
import com.jetsynthesys.rightlife.ai_package.model.Workout
import com.jetsynthesys.rightlife.ai_package.ui.eatright.model.MyMealModel
import com.jetsynthesys.rightlife.ai_package.ui.moveright.DeleteWorkoutBottomSheet

class CreateRoutineListAdapter(
    private val context: Context,
    private var dataLists: ArrayList<Workout>,
    private var clickPos: Int,
    private var selectedWorkout: Workout?,
    private var isClickView: Boolean,
    val onWorkoutItemClick: (Workout, Int, Boolean) -> Unit
) : RecyclerView.Adapter<CreateRoutineListAdapter.ViewHolder>() {

    private var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_create_routine_ai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = dataLists[position]

        // Populate the UI with workout data
        holder.mealTitle.text = workout.recordType // e.g., "Running"
        holder.mealName.text = "Workout" // You can customize this (e.g., workout name if available)
        holder.serves.text = "${workout.durationInt / 60}" // Hours part
        holder.servesCount.text = "${workout.durationInt % 60} ${workout.durationUnit}" // Minutes part, e.g., "30 min"
        holder.calValue.text = workout.caloriesBurned // e.g., "250"
        holder.calUnit.text = workout.caloriesUnit // e.g., "kcal"

        // Calculate average heart rate for intensity (Low Intensity, etc.)
        val avgHeartRate = if (workout.heartRateData.isNotEmpty()) {
            workout.heartRateData.map { it.heartRate }.average().toInt()
        } else {
            0
        }
        holder.subtractionValue.text = when {
            avgHeartRate < 128 -> "Low Intensity"
            avgHeartRate < 139 -> "Fat Burn"
            avgHeartRate < 161 -> "Cardio"
            else -> "Peak"
        }

        // Distance (using baguette field, since it's not used for workouts)
        holder.baguette.visibility = View.VISIBLE
        holder.baguetteValue.visibility = View.VISIBLE
        holder.baguetteUnit.visibility = View.VISIBLE
        holder.baguetteValue.text = workout.distance // e.g., "4"
        holder.baguetteUnit.text = workout.distanceUnit // e.g., "km"

        // Hide dewpoint fields (not relevant for workouts)
        holder.dewpoint.visibility = View.GONE
        holder.dewpointValue.visibility = View.GONE
        holder.dewpointUnit.visibility = View.GONE

        // Edit button to show the bottom sheet
        holder.edit.setOnClickListener {
            val bottomSheet = DeleteWorkoutBottomSheet()
            bottomSheet.show((context as AppCompatActivity).supportFragmentManager, "EditWorkoutBottomSheet")
        }

        // Handle click on the item
        holder.itemView.setOnClickListener {
            onWorkoutItemClick(workout, position, true)
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

    fun addAll(items: ArrayList<Workout>?, pos: Int, selectedItem: Workout?, isClick: Boolean) {
        dataLists.clear()
        if (items != null) {
            dataLists = items
            clickPos = pos
            selectedWorkout = selectedItem
            isClickView = isClick
        }
        notifyDataSetChanged()
    }
}