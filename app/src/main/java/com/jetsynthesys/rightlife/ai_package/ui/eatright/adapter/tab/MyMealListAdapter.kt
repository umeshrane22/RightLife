package com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter.tab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.model.response.MealDetails
import com.jetsynthesys.rightlife.ai_package.model.response.MergedMealItem
import com.jetsynthesys.rightlife.ai_package.model.response.SnapMealDetail

class MyMealListAdapter(private val context: Context, private var dataLists: ArrayList<MergedMealItem>,
                        private var clickPos: Int, private var mealDetails : MealDetails?,
                        private var snapMealDetail : SnapMealDetail?,
                        private var isClickView : Boolean, val onDeleteMealItem: (MealDetails, Int, Boolean) -> Unit,
                        val onAddMealLogItem: (MealDetails, Int, Boolean) -> Unit,
                        val onEditMealLogItem: (MealDetails, Int, Boolean) -> Unit,
                        val onDeleteSnapMealItem: (SnapMealDetail, Int, Boolean) -> Unit,
                        val onAddSnapMealLogItem: (SnapMealDetail, Int, Boolean) -> Unit,
                        val onEditSnapMealLogItem: (SnapMealDetail, Int, Boolean) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedItem = -1

    companion object {
        private const val TYPE_SNAP_MEAL = 0
        private const val TYPE_SAVED_MEAL = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataLists[position]) {
            is MergedMealItem.SnapMeal -> TYPE_SNAP_MEAL
            is MergedMealItem.SavedMeal -> TYPE_SAVED_MEAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MyMealListAdapter.TYPE_SNAP_MEAL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_meal_ai, parent, false)
                SnapMealViewHolder(view)
            }
            MyMealListAdapter.TYPE_SAVED_MEAL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_meal_ai, parent, false)
                SavedMealViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = dataLists[position]) {
            is MergedMealItem.SnapMeal -> (holder as SnapMealViewHolder).bind(item.data)
            is MergedMealItem.SavedMeal -> (holder as SavedMealViewHolder).bind(item.data)
        }

//        holder.mealTitle.text = item
//        holder.mealName.text = item.dishes.name
//        holder.servesCount.text = item.dishes.numOfServings.toString()
//        holder.calValue.text = item.dishes.calcium.toInt().toString()
//        holder.subtractionValue.text = item.dishes.protein.toInt().toString()
//        holder.baguetteValue.text = item.dishes.carbs.toInt().toString()
//        holder.dewpointValue.text = item.dishes.fats.toInt().toString()
//        if (item.status == true) {
//            holder.mealDay.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//            holder.mealDate.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//            holder.circleStatus.setImageResource(R.drawable.circle_check)
//            if (mealLogListData != null){
//                if (clickPos == position && mealLogListData == item && isClickView == true){
//                    holder.layoutMain.setBackgroundResource(R.drawable.green_meal_date_bg)
//                }else{
//                    holder.layoutMain.setBackgroundResource(R.drawable.white_meal_date_bg)
//                }
//            }
//        }else{
//            holder.mealDay.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//            holder.mealDate.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//            holder.circleStatus.setImageResource(R.drawable.circle_uncheck)
//            if (mealLogListData != null){
//                if (clickPos == position && mealLogListData == item && isClickView == true){
//                    holder.layoutMain.setBackgroundResource(R.drawable.green_meal_date_bg)
//                }else{
//                    holder.layoutMain.setBackgroundResource(R.drawable.white_meal_date_bg)
//                }
//            }
   //     }
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

    inner class SnapMealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SnapMealDetail) {
            itemView.findViewById<TextView>(R.id.tv_meal_name).text = item.meal_name
            val mealTitle: TextView = itemView.findViewById(R.id.tv_meal_title)
            val delete: ImageView = itemView.findViewById(R.id.image_delete)
            val edit: ImageView = itemView.findViewById(R.id.image_edit)
            val circlePlus : ImageView = itemView.findViewById(R.id.image_circle_plus)
            val servesCount: TextView = itemView.findViewById(R.id.tv_serves_count)
            val calValue: TextView = itemView.findViewById(R.id.tv_cal_value)
            val subtractionValue: TextView = itemView.findViewById(R.id.tv_subtraction_value)
            val baguetteValue: TextView = itemView.findViewById(R.id.tv_baguette_value)
            val dewpointValue: TextView = itemView.findViewById(R.id.tv_dewpoint_value)

            mealTitle.text = item.meal_type
            servesCount.text = item.total_servings.toString()
            calValue.text = item.total_calories.toInt().toString()
            subtractionValue.text = item.total_protein.toInt().toString()
            baguetteValue.text = item.total_carbs.toInt().toString()
            dewpointValue.text = item.total_fat.toInt().toString()

            delete.setOnClickListener {
                onDeleteSnapMealItem(item, bindingAdapterPosition, true)
            }

            circlePlus.setOnClickListener {
                onAddSnapMealLogItem(item, bindingAdapterPosition, true)
            }

            edit.setOnClickListener {
                onEditSnapMealLogItem(item, bindingAdapterPosition, true)
            }
        }
    }

    inner class SavedMealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MealDetails) {
            itemView.findViewById<TextView>(R.id.tv_meal_name).text = item.meal_name
            val mealTitle: TextView = itemView.findViewById(R.id.tv_meal_title)
            val delete: ImageView = itemView.findViewById(R.id.image_delete)
            val edit: ImageView = itemView.findViewById(R.id.image_edit)
            val circlePlus: ImageView = itemView.findViewById(R.id.image_circle_plus)
            val servesCount: TextView = itemView.findViewById(R.id.tv_serves_count)
            val calValue: TextView = itemView.findViewById(R.id.tv_cal_value)
            val subtractionValue: TextView = itemView.findViewById(R.id.tv_subtraction_value)
            val baguetteValue: TextView = itemView.findViewById(R.id.tv_baguette_value)
            val dewpointValue: TextView = itemView.findViewById(R.id.tv_dewpoint_value)

            mealTitle.text = item.meal_type
            servesCount.text = item.total_servings.toString()
            calValue.text = item.total_calories.toInt().toString()
            subtractionValue.text = item.total_protein.toInt().toString()
            baguetteValue.text = item.total_carbs.toInt().toString()
            dewpointValue.text = item.total_fat.toInt().toString()

            delete.setOnClickListener {
                onDeleteMealItem(item, bindingAdapterPosition, true)
            }

            circlePlus.setOnClickListener {
                onAddMealLogItem(item, bindingAdapterPosition, true)
            }

            circlePlus.setOnClickListener {
                onAddMealLogItem(item, bindingAdapterPosition, true)
            }

            edit.setOnClickListener {
                onEditMealLogItem(item, bindingAdapterPosition, true)
            }
        }
    }

//     inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//         val mealTitle: TextView = itemView.findViewById(R.id.tv_meal_title)
//         val delete: ImageView = itemView.findViewById(R.id.image_delete)
//         val edit: ImageView = itemView.findViewById(R.id.image_edit)
//         val circlePlus : ImageView = itemView.findViewById(R.id.image_circle_plus)
//         val mealName: TextView = itemView.findViewById(R.id.tv_meal_name)
//         val serve: ImageView = itemView.findViewById(R.id.image_serve)
//         val serves: TextView = itemView.findViewById(R.id.tv_serves)
//         val servesCount: TextView = itemView.findViewById(R.id.tv_serves_count)
//         val cal: ImageView = itemView.findViewById(R.id.image_cal)
//         val calValue: TextView = itemView.findViewById(R.id.tv_cal_value)
//         val calUnit: TextView = itemView.findViewById(R.id.tv_cal_unit)
//         val subtraction: ImageView = itemView.findViewById(R.id.image_subtraction)
//         val subtractionValue: TextView = itemView.findViewById(R.id.tv_subtraction_value)
//         val subtractionUnit: TextView = itemView.findViewById(R.id.tv_subtraction_unit)
//         val baguette: ImageView = itemView.findViewById(R.id.image_baguette)
//         val baguetteValue: TextView = itemView.findViewById(R.id.tv_baguette_value)
//         val baguetteUnit: TextView = itemView.findViewById(R.id.tv_baguette_unit)
//         val dewpoint: ImageView = itemView.findViewById(R.id.image_dewpoint)
//         val dewpointValue: TextView = itemView.findViewById(R.id.tv_dewpoint_value)
//         val dewpointUnit: TextView = itemView.findViewById(R.id.tv_dewpoint_unit)
//     }

    fun addAll(item : ArrayList<MergedMealItem>?, pos: Int, mealLogItem : MealDetails?, snapMealLogItem : SnapMealDetail?,
               isClick : Boolean) {
        dataLists.clear()
        if (item != null) {
            dataLists = item
            clickPos = pos
            mealDetails = mealLogItem
            snapMealDetail = snapMealLogItem
            isClickView = isClick
        }
        notifyDataSetChanged()
    }
}