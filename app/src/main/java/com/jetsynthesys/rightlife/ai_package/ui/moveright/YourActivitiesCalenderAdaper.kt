package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.model.ActivityModel
import com.jetsynthesys.rightlife.ai_package.ui.moveright.DeleteWorkoutBottomSheet

class YourActivitiesCalenderAdaper(private val context: Context, private var dataLists: ArrayList<ActivityModel>,
                                   private var clickPos: Int,
                                   private var workoutData: ActivityModel?,
                                   private var isClickView: Boolean,
                                   val onWorkoutItemClick: (ActivityModel, Int, Boolean) -> Unit,
                                   private val onCirclePlusClick: (ActivityModel, Int) -> Unit
) : RecyclerView.Adapter<YourActivitiesCalenderAdaper.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_your_workout_calender_ai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataLists[position]

        holder.mealTitle.text = item.workoutType
        if (item.isSynced){
            holder.edit.visibility = View.GONE
            holder.delete.visibility = View.GONE
            holder.bpmUnit.visibility = View.VISIBLE
            holder.wearable.visibility = View.VISIBLE
            holder.bpmUnit.text = "bpm"
            holder.subtractionValue.text = item.averageHeartRate.toInt().toString()
            holder.subtraction.setImageResource(R.drawable.avg_heart_rate)
        }else{
            holder.edit.visibility = View.GONE
            holder.delete.visibility = View.GONE
            holder.wearable.visibility = View.GONE
            holder.bpmUnit.visibility = View.GONE
            holder.subtractionValue.text = item.intensity
            holder.subtraction.setImageResource(R.drawable.intensity_meter)
        }
        val formattedTime = formatTimeString(item.duration!!)
        holder.duration.text = formattedTime
        val formattedCalories = item.caloriesBurned!!.substringBefore(".")
        holder.calValue.text = formattedCalories
        holder.calUnit.visibility = View.VISIBLE
        holder.calUnit.text = "cal"
        holder.mealName.visibility = View.GONE

        holder.delete.setOnClickListener {
            val bottomSheet = DeleteWorkoutBottomSheet.newInstance(
                calorieId = item.id!!,
                userId = item.userId!! // Replace with dynamic userId if available
            )

            bottomSheet.setOnDeleteSuccessListener {
                dataLists.removeAt(position)
                notifyDataSetChanged()
            }

            bottomSheet.show((context as AppCompatActivity).supportFragmentManager, "EditWorkoutBottomSheet")
        }

        holder.edit.setOnClickListener {
            onCirclePlusClick(item, position)
        }

        Glide.with(context)
            .load(item.icon) // <-- your image URL string
            .into(holder.main_heading_icon)
        // Set up item click listener
        /* holder.itemView.setOnClickListener {
             onWorkoutItemClick(item, position, true)
         }*/
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealTitle: TextView = itemView.findViewById(R.id.tv_meal_title)
        val wearable: ImageView = itemView.findViewById(R.id.image_delete)
        val delete: ImageView = itemView.findViewById(R.id.image_edit)
        val edit: ImageView = itemView.findViewById(R.id.image_circle_plus)
        val mealName: TextView = itemView.findViewById(R.id.tv_meal_name)
        val serve: ImageView = itemView.findViewById(R.id.image_serve)
        val serves: TextView = itemView.findViewById(R.id.tv_serves)
        val duration: TextView = itemView.findViewById(R.id.tv_serves_count)
        val cal: ImageView = itemView.findViewById(R.id.image_cal)
        val calValue: TextView = itemView.findViewById(R.id.tv_cal_value)
        val calUnit: TextView = itemView.findViewById(R.id.tv_cal_unit)
        val main_heading_icon: ImageView = itemView.findViewById(R.id.main_heading_icon)
        val subtraction: ImageView = itemView.findViewById(R.id.image_subtraction)
        val subtractionValue: TextView = itemView.findViewById(R.id.tv_subtraction_value)
        val bpmUnit: TextView = itemView.findViewById(R.id.tv_subtraction_unit)
        val baguette: ImageView = itemView.findViewById(R.id.image_baguette)
        val baguetteValue: TextView = itemView.findViewById(R.id.tv_baguette_value)
        val baguetteUnit: TextView = itemView.findViewById(R.id.tv_baguette_unit)
        val dewpoint: ImageView = itemView.findViewById(R.id.image_dewpoint)
        val dewpointValue: TextView = itemView.findViewById(R.id.tv_dewpoint_value)
        val dewpointUnit: TextView = itemView.findViewById(R.id.tv_dewpoint_unit)
    }

    fun addAll(items: ArrayList<ActivityModel>?, pos: Int, workoutItem: ActivityModel?, isClick: Boolean) {
        dataLists.clear()
        if (items != null) {
            dataLists = items
            clickPos = pos
            workoutData = workoutItem
            isClickView = isClick
        }
        notifyDataSetChanged()
    }
    private fun formatCalorieString(calorieString: String): SpannableStringBuilder {
        // Step 1: Parse the input string (e.g., "488 kcal" -> 488)
        val calories = calorieString.replace(" kcal", "").toIntOrNull() ?: 0
        val formattedText = "$calories cal"
        // Step 2: Create SpannableStringBuilder for styling
        val spannable = SpannableStringBuilder(formattedText)
        // Step 3: Apply bold and 18sp to the number
        val numberLength = calories.toString().length
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            numberLength,
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            AbsoluteSizeSpan(18, true), // 18sp
            0,
            numberLength,
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        // Step 4: Apply normal and 12sp to "kcal"
        spannable.setSpan(
            AbsoluteSizeSpan(12, true), // 12sp
            numberLength,
            formattedText.length, // " kcal"
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    private fun formatTimeString(timeString: String): SpannableStringBuilder {
        // Step 1: Parse the input string (e.g., "2 hr 01 mins")
        var hours = 0
        var minutes = 0
        val parts = timeString.split(" ")
        when (parts.size) {
            2 -> { // Format: "61 mins"
                minutes = parts[0].toIntOrNull() ?: 0
            }
            4 -> { // Format: "2 hr 01 mins"
                hours = parts[0].toIntOrNull() ?: 0
                minutes = parts[2].toIntOrNull() ?: 0
            }
        }

        // Step 2: Build the formatted string (e.g., "2 hr 1 mins")
        val formattedText = buildString {
            if (hours > 0) {
                append("$hours hr ")
            }
            if (minutes > 0 || hours == 0) { // Show minutes even if 0 when hours is 0
                append("$minutes mins")
            }
        }.trim()

        // Step 3: Create SpannableStringBuilder for styling
        val spannable = SpannableStringBuilder(formattedText)

        // Apply bold and 18sp to numbers
        var currentIndex = 0
        if (hours > 0) {
            // Style the hours number
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                hours.toString().length,
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                AbsoluteSizeSpan(18, true), // 18sp
                0,
                hours.toString().length,
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Style the "hr" unit (normal, 12sp, no bold)
            spannable.setSpan(
                AbsoluteSizeSpan(12, true), // 12sp
                hours.toString().length,
                hours.toString().length + 3, // " hr "
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            currentIndex = hours.toString().length + 4 // Move index past "X hr "
        }

        // Style the minutes number
        if (minutes > 0 || hours == 0) {
            val minutesStart = currentIndex
            val minutesEnd = minutesStart + minutes.toString().length
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                minutesStart,
                minutesEnd,
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                AbsoluteSizeSpan(18, true), // 18sp
                minutesStart,
                minutesEnd,
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Style the "mins" unit (normal, 12sp, no bold)
            spannable.setSpan(
                AbsoluteSizeSpan(12, true), // 12sp
                minutesEnd,
                formattedText.length, // " mins"
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannable
    }
}