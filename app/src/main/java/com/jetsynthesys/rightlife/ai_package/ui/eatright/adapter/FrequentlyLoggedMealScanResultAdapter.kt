package com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.model.NutritionData
import com.jetsynthesys.rightlife.ai_package.model.NutritionDetails

class FrequentlyLoggedMealScanResultAdapter(private val context: Context, private var dataLists: ArrayList<NutritionData>,
                                            private var clickPos: Int, private var mealLogListData : NutritionData?,
                                            private var isClickView : Boolean, val onFrequentlyLoggedItem: (NutritionDetails, Int, Boolean) -> Unit,) :
    RecyclerView.Adapter<FrequentlyLoggedMealScanResultAdapter.ViewHolder>() {

    private var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal_scan_results_ai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataLists[position]

        holder.mealTitle.text = item.name
//        Glide.with(context)
//            .load(item.url)
//            .placeholder(R.drawable.ic_breakfast)
//            .error(R.drawable.ic_breakfast)
//            .into(holder.imageMeal)
        // holder.mealName.text = item.mealName
        if (item.selected_portion_nutrition != null){
            holder.servesCount.text = "1"
            holder.calValue.text = item.selected_portion_nutrition.calories_kcal.toInt().toString()
            holder.proteinValue.text = item.selected_portion_nutrition.protein_g.toInt().toString()
            holder.cabsValue.text = item.selected_portion_nutrition.carb_g.toInt().toString()
            holder.fatsValue.text = item.selected_portion_nutrition.fat_g.toInt().toString()
        }else{
            holder.servesCount.text = "NA"
            holder.calValue.text = "NA"
            holder.proteinValue.text = "NA"
            holder.cabsValue.text = "NA"
            holder.fatsValue.text = "NA"
        }

       // if (item.isAddDish == true) {
//            holder.mealDay.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//            holder.mealDate.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
          //  holder.circlePlus.setImageResource(R.drawable.edit_green_icon)
//            if (mealLogListData != null){
//                if (clickPos == position && mealLogListData == item && isClickView == true){
//                    holder.layoutMain.setBackgroundResource(R.drawable.green_meal_date_bg)
//                }else{
//                    holder.layoutMain.setBackgroundResource(R.drawable.white_meal_date_bg)
//                }
//            }
      //  }else{
//            holder.mealDay.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//            holder.mealDate.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
        //    holder.circlePlus.setImageResource(R.drawable.edit_green_icon)
//            if (mealLogListData != null){
//                if (clickPos == position && mealLogListData == item && isClickView == true){
//                    holder.layoutMain.setBackgroundResource(R.drawable.green_meal_date_bg)
//                }else{
//                    holder.layoutMain.setBackgroundResource(R.drawable.white_meal_date_bg)
//                }
//            }
   //     }

        holder.circlePlus.setOnClickListener {
            onFrequentlyLoggedItem(item.selected_portion_nutrition, position, true)
        }

//        holder.layoutMain.setOnClickListener {
//            onMealLogDateItem(item, position, true)
//        }
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageMeal : ImageView = itemView.findViewById(R.id.image_meal)
        val mealTitle: TextView = itemView.findViewById(R.id.tv_meal_title)
        val delete: ImageView = itemView.findViewById(R.id.image_delete)
        val edit: ImageView = itemView.findViewById(R.id.image_edit)
        val circlePlus : ImageView = itemView.findViewById(R.id.image_circle_plus)
        val mealName: TextView = itemView.findViewById(R.id.tv_meal_name)
        val serve: ImageView = itemView.findViewById(R.id.image_serve)
        val serves: TextView = itemView.findViewById(R.id.tv_serves)
        val servesCount: TextView = itemView.findViewById(R.id.tv_serves_count)
        val cal: ImageView = itemView.findViewById(R.id.image_cal)
        val calValue: TextView = itemView.findViewById(R.id.tv_cal_value)
        val calUnit: TextView = itemView.findViewById(R.id.tv_cal_unit)
        val subtraction: ImageView = itemView.findViewById(R.id.image_subtraction)
        val proteinValue: TextView = itemView.findViewById(R.id.tv_protein_value)
        val subtractionUnit: TextView = itemView.findViewById(R.id.tv_subtraction_unit)
        val baguette: ImageView = itemView.findViewById(R.id.image_baguette)
        val cabsValue: TextView = itemView.findViewById(R.id.tv_cabs_value)
        val baguetteUnit: TextView = itemView.findViewById(R.id.tv_baguette_unit)
        val dewpoint: ImageView = itemView.findViewById(R.id.image_dewpoint)
        val fatsValue: TextView = itemView.findViewById(R.id.tv_fats_value)
        val dewpointUnit: TextView = itemView.findViewById(R.id.tv_dewpoint_unit)
    }

    fun addAll(item : ArrayList<NutritionData>?, pos: Int, mealLogItem : NutritionData?, isClick : Boolean) {
        dataLists.clear()
        if (item != null) {
            dataLists = item
            clickPos = pos
            mealLogListData = mealLogItem
            isClickView = isClick
        }
        notifyDataSetChanged()
    }
}