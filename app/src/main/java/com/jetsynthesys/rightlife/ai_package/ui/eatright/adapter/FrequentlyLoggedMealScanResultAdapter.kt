package com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.model.NutritionData
import com.jetsynthesys.rightlife.ai_package.model.NutritionDetails
import com.jetsynthesys.rightlife.ai_package.model.response.SnapRecipeData

class FrequentlyLoggedMealScanResultAdapter(private val context: Context, private var dataLists: ArrayList<SnapRecipeData>,
                                            private var clickPos: Int, private var mealLogListData : SnapRecipeData?,
                                            private var isClickView : Boolean, val onMenuEditItem: (SnapRecipeData, Int, Boolean) -> Unit,
                                            val onMenuDeleteItem: (SnapRecipeData, Int, Boolean) -> Unit) :
    RecyclerView.Adapter<FrequentlyLoggedMealScanResultAdapter.ViewHolder>() {

    private var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal_scan_results_ai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataLists[position]

       // holder.mealQuantityTv.text = item.name
        holder.mealName.text = item.recipe_name
//        Glide.with(context)
//            .load(item.url)
//            .placeholder(R.drawable.ic_breakfast)
//            .error(R.drawable.ic_breakfast)
//            .into(holder.imageMeal)
        // holder.mealName.text = item.mealName
        if (item != null){
         //   holder.servesCount.text = "1"
            holder.calValue.text = item.calories.toInt().toString()
            holder.proteinValue.text = item.protein.toInt().toString()
            holder.cabsValue.text = item.carbs.toInt().toString()
            holder.fatsValue.text = item.fat.toInt().toString()
        }else{
          //  holder.servesCount.text = "NA"
            holder.calValue.text = "NA"
            holder.proteinValue.text = "NA"
            holder.cabsValue.text = "NA"
            holder.fatsValue.text = "NA"
        }

        holder.editDeleteMenu.setOnClickListener { view ->
            val popupMenu = PopupMenu(view.context, holder.editDeleteMenu)
            popupMenu.menuInflater.inflate(R.menu.sanp_meal_popup_menu, popupMenu.menu)
            // 🔽 Enable icons using reflection
            try {
                val fields = popupMenu.javaClass.declaredFields
                for (field in fields) {
                    if (field.name == "mPopup") {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popupMenu)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons =
                            classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_edit -> {
                    onMenuEditItem(item, position, true)
                    true
                }
                R.id.action_delete -> {
                    onMenuDeleteItem(item, position, true)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

//        holder.editDeleteMenu.setOnClickListener {
//            val popup = PopupMenu(context, holder.editDeleteMenu)
//            popup.inflate(R.menu.sanp_meal_popup_menu)
//            popup.setOnMenuItemClickListener {
//                when (it.itemId) {
//                    R.id.action_edit -> {
//                        onFrequentlyLoggedItem(item.selected_portion_nutrition, position, true)
//                        true
//                    }
//                    R.id.action_delete -> {
//                        onFrequentlyLoggedItem(item.selected_portion_nutrition, position, true)
//                        true
//                    }
//                    else -> false
//                }
//            }
//            popup.show()
//        }

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

//        holder.editDeleteMenu.setOnClickListener {
//            onFrequentlyLoggedItem(item.selected_portion_nutrition, position, true)
//        }

//        holder.layoutMain.setOnClickListener {
//            onMealLogDateItem(item, position, true)
//        }
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageMeal : ImageView = itemView.findViewById(R.id.image_meal)
        val mealQuantityTv: TextView = itemView.findViewById(R.id.mealQuantityTv)
    //    val delete: ImageView = itemView.findViewById(R.id.image_delete)
     //   val edit: ImageView = itemView.findViewById(R.id.image_edit)
        val editDeleteMenu : ImageView = itemView.findViewById(R.id.editDeleteMenu)
        val mealName: TextView = itemView.findViewById(R.id.tv_meal_name)
     //   val serve: ImageView = itemView.findViewById(R.id.image_serve)
 //       val serves: TextView = itemView.findViewById(R.id.tv_serves)
 //       val servesCount: TextView = itemView.findViewById(R.id.tv_serves_count)
     //   val cal: ImageView = itemView.findViewById(R.id.image_cal)
        val calValue: TextView = itemView.findViewById(R.id.tv_cal_value)
        val calUnit: TextView = itemView.findViewById(R.id.tv_cal_unit)
     //   val subtraction: ImageView = itemView.findViewById(R.id.image_subtraction)
        val proteinValue: TextView = itemView.findViewById(R.id.tv_protein_value)
        val subtractionUnit: TextView = itemView.findViewById(R.id.tv_subtraction_unit)
      //  val baguette: ImageView = itemView.findViewById(R.id.image_baguette)
        val cabsValue: TextView = itemView.findViewById(R.id.tv_cabs_value)
        val baguetteUnit: TextView = itemView.findViewById(R.id.tv_baguette_unit)
     //   val dewpoint: ImageView = itemView.findViewById(R.id.image_dewpoint)
        val fatsValue: TextView = itemView.findViewById(R.id.tv_fats_value)
        val dewpointUnit: TextView = itemView.findViewById(R.id.tv_dewpoint_unit)
    }

    fun addAll(item : ArrayList<SnapRecipeData>?, pos: Int, mealLogItem : SnapRecipeData?, isClick : Boolean) {
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