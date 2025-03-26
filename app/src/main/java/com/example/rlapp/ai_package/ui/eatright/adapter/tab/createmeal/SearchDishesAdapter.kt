package com.example.rlapp.ai_package.ui.eatright.adapter.tab.createmeal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

import com.example.rlapp.ai_package.ui.eatright.model.MyMealModel

class SearchDishesAdapter(private val context: Context, private var dataLists: ArrayList<MyMealModel>,
                          private var clickPos: Int, private var mealLogListData : MyMealModel?,
                          private var isClickView : Boolean, val onSearchDishItem: (MyMealModel, Int, Boolean) -> Unit) :
    RecyclerView.Adapter<SearchDishesAdapter.ViewHolder>() {

    private var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish_search_ai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataLists[position]

        holder.dishName.text = item.mealName
//        if (item.status == true) {
//            holder.circleStatus.setImageResource(R.drawable.circle_check)
//            if (mealLogListData != null){
//                if (clickPos == position && mealLogListData == item && isClickView == true){
//                    holder.layoutMain.setBackgroundResource(R.drawable.dark_green_meal_bg)
//                    holder.mealDay.setTextColor(ContextCompat.getColor(context,R.color.white))
//                    holder.mealDate.setTextColor(ContextCompat.getColor(context,R.color.white))
//                }else{
//                    holder.layoutMain.setBackgroundResource(R.drawable.white_meal_date_bg)
//                    holder.mealDay.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//                    holder.mealDate.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//                }
//            }
//        }else{
//            holder.mealDay.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//            holder.mealDate.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//            holder.circleStatus.setImageResource(R.drawable.circle_uncheck)
//            if (mealLogListData != null){
//                if (clickPos == position && mealLogListData == item && isClickView == true){
//                    holder.layoutMain.setBackgroundResource(R.drawable.dark_green_meal_bg)
//                    holder.mealDay.setTextColor(ContextCompat.getColor(context,R.color.white))
//                    holder.mealDate.setTextColor(ContextCompat.getColor(context,R.color.white))
//                }else{
//                    holder.layoutMain.setBackgroundResource(R.drawable.white_meal_date_bg)
//                    holder.mealDay.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//                    holder.mealDate.setTextColor(ContextCompat.getColor(context,R.color.black_no_meals))
//                }
//            }
//        }

        holder.layoutMain.setOnClickListener {
            onSearchDishItem(item, position, true)
        }
    }

    override fun getItemCount(): Int {
        return dataLists.size
    }

     inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         val dishName: TextView = itemView.findViewById(R.id.tv_dish_name)
         val rightArrow: ImageView = itemView.findViewById(R.id.image_right_arrow)
         val dishImage: ImageView = itemView.findViewById(R.id.image_dish)
         val layoutMain : LinearLayout = itemView.findViewById(R.id.lyt_meal_item)
     }

    fun addAll(item : ArrayList<MyMealModel>?, pos: Int, mealLogItem : MyMealModel?, isClick : Boolean) {
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