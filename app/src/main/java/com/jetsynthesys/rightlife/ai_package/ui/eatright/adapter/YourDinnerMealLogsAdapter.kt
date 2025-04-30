package com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.model.response.MergedLogsMealItem
import com.jetsynthesys.rightlife.ai_package.model.response.RegularRecipeEntry
import com.jetsynthesys.rightlife.ai_package.model.response.SnapMeal


class YourDinnerMealLogsAdapter(val context: Context, private var dataLists: ArrayList<MergedLogsMealItem>,
                                private var clickPos: Int, private var regularRecipeEntry : RegularRecipeEntry?,
                                private var snapMealData : SnapMeal?, private var isClickView : Boolean,
                                val onDinnerRegularRecipeDeleteItem: (RegularRecipeEntry, Int, Boolean) -> Unit,
                                val onDinnerRegularRecipeEditItem: (RegularRecipeEntry, Int, Boolean) -> Unit,
                                val onDinnerSnapMealDeleteItem: (SnapMeal, Int, Boolean) -> Unit,
                                val onDinnerSnapMealEditItem: (SnapMeal, Int, Boolean) -> Unit, val isLanding : Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedItem = -1

    companion object {
        private const val TYPE_REGULAR_RECIPE = 0
        private const val TYPE_SNAP_MEAL = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataLists[position]) {
            is MergedLogsMealItem.RegularRecipeList -> TYPE_REGULAR_RECIPE
            is MergedLogsMealItem.SnapMealList -> TYPE_SNAP_MEAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_REGULAR_RECIPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_breakfast_meal_logs_ai, parent, false)
                RegularRecipeViewHolder(view)
            }
            TYPE_SNAP_MEAL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_breakfast_meal_logs_ai, parent, false)
                SnapMealViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = dataLists[position]) {
            is MergedLogsMealItem.RegularRecipeList -> (holder as RegularRecipeViewHolder).bind(item.entry)
            is MergedLogsMealItem.SnapMealList -> (holder as SnapMealViewHolder).bind(item.meal)
        }
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

    inner class RegularRecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data : RegularRecipeEntry) {
            val mealTimeTv : TextView = itemView.findViewById(R.id.tv_eat_time)
            val delete: ImageView = itemView.findViewById(R.id.image_delete)
            val edit: ImageView = itemView.findViewById(R.id.image_edit)
            val imageUpDown : ImageView = itemView.findViewById(R.id.imageUpDown)
            val mealImage : ImageView = itemView.findViewById(R.id.image_meal)
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
            val layoutVegNonveg : LinearLayoutCompat = itemView.findViewById(R.id.layout_veg_nonveg)
            val layoutEatTime : LinearLayoutCompat = itemView.findViewById(R.id.layout_eat_time)
            val servesLayout : LinearLayoutCompat = itemView.findViewById(R.id.servesLayout)

            if (isLanding){
                delete.visibility = View.GONE
                edit.visibility = View.GONE
                layoutEatTime.visibility = View.GONE
                layoutVegNonveg.visibility = View.GONE
                servesLayout.visibility = View.GONE
            }else {
                delete.visibility = View.VISIBLE
                edit.visibility = View.VISIBLE
                layoutEatTime.visibility = View.VISIBLE
                layoutVegNonveg.visibility = View.VISIBLE
                servesLayout.visibility = View.VISIBLE
            }


            mealName.text = data.receipe.recipe_name
            servesCount.text = data.receipe.servings.toString()
            val mealTime = data.receipe.serving_weight
            mealTimeTv.text = mealTime.toInt().toString()
            calValue.text = data.receipe.calories.toInt().toString()
            subtractionValue.text = data.receipe.carbs.toInt().toString()
            baguetteValue.text = data.receipe.protein.toInt().toString()
            dewpointValue.text = data.receipe.fat.toInt().toString()
            val imageUrl = getDriveImageUrl(data.receipe.photo_url)
            Glide.with(this.itemView)
                .load(imageUrl)
                .placeholder(R.drawable.ic_dinner)
                .error(R.drawable.ic_dinner)
                .into(mealImage)

            delete.setOnClickListener {
                onDinnerRegularRecipeDeleteItem(data, bindingAdapterPosition, true)
            }

            edit.setOnClickListener {
                onDinnerRegularRecipeEditItem(data, bindingAdapterPosition, true)
            }
        }

        fun getDriveImageUrl(originalUrl: String): String? {
            val regex = Regex("(?<=/d/)(.*?)(?=/|$)")
            val matchResult = regex.find(originalUrl)
            val fileId = matchResult?.value
            return if (!fileId.isNullOrEmpty()) {
                "https://drive.google.com/uc?export=view&id=$fileId"
            } else {
                null
            }
        }
    }

    inner class SnapMealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: SnapMeal) {
            val mealTimeTv : TextView = itemView.findViewById(R.id.tv_eat_time)
            val delete: ImageView = itemView.findViewById(R.id.image_delete)
            val edit: ImageView = itemView.findViewById(R.id.image_edit)
            val imageUpDown : ImageView = itemView.findViewById(R.id.imageUpDown)
            val mealImage : ImageView = itemView.findViewById(R.id.image_meal)
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
            val layoutVegNonveg : LinearLayoutCompat = itemView.findViewById(R.id.layout_veg_nonveg)
            val layoutEatTime : LinearLayoutCompat = itemView.findViewById(R.id.layout_eat_time)
            val servesLayout : LinearLayoutCompat = itemView.findViewById(R.id.servesLayout)

            if (isLanding){
                delete.visibility = View.GONE
                edit.visibility = View.GONE
                layoutEatTime.visibility = View.GONE
                layoutVegNonveg.visibility = View.GONE
                servesLayout.visibility = View.GONE
            }else {
                delete.visibility = View.VISIBLE
                edit.visibility = View.VISIBLE
                layoutEatTime.visibility = View.VISIBLE
                layoutVegNonveg.visibility = View.VISIBLE
                servesLayout.visibility = View.VISIBLE
            }

            mealName.text = data.name
            servesCount.text = "1"
            val mealTime = ""
            mealTimeTv.text = ""//mealTime.toInt().toString()
            calValue.text = data.calories_kcal?.toInt().toString()
            subtractionValue.text = data.carb_g?.toInt().toString()
            baguetteValue.text = data.protein_g?.toInt().toString()
            dewpointValue.text = data.fat_g?.toInt().toString()
            val imageUrl = ""//getDriveImageUrl(data.photo_url)
            Glide.with(this.itemView)
                .load(imageUrl)
                .placeholder(R.drawable.ic_dinner)
                .error(R.drawable.ic_dinner)
                .into(mealImage)

            delete.setOnClickListener {
                onDinnerSnapMealDeleteItem(data, bindingAdapterPosition, true)
            }

            edit.setOnClickListener {
                onDinnerSnapMealEditItem(data, bindingAdapterPosition, true)
            }
        }

        fun getDriveImageUrl(originalUrl: String): String? {
            val regex = Regex("(?<=/d/)(.*?)(?=/|$)")
            val matchResult = regex.find(originalUrl)
            val fileId = matchResult?.value
            return if (!fileId.isNullOrEmpty()) {
                "https://drive.google.com/uc?export=view&id=$fileId"
            } else {
                null
            }
        }
    }

    fun addAll(item : ArrayList<MergedLogsMealItem>?, pos: Int, mealLogItem : RegularRecipeEntry?, snapMeal : SnapMeal?,
               isClick : Boolean) {
        dataLists.clear()
        if (item != null) {
            dataLists = item
            clickPos = pos
            regularRecipeEntry = mealLogItem
            snapMealData = snapMeal
            isClickView = isClick
        }
        notifyDataSetChanged()
    }
}