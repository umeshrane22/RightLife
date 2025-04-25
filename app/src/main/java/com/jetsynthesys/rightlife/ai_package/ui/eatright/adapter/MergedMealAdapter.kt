//package com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter
//
//class MergedMealAdapter(private val items: List<MergedMealItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    companion object {
//        const val TYPE_SNAP_MEAL = 0
//        const val TYPE_SAVED_MEAL = 1
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return when (items[position]) {
//            is MergedMealItem.SnapMeal -> TYPE_SNAP_MEAL
//            is MergedMealItem.SavedMeal -> TYPE_SAVED_MEAL
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            TYPE_SNAP_MEAL -> {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_snap_meal, parent, false)
//                SnapMealViewHolder(view)
//            }
//            TYPE_SAVED_MEAL -> {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_meal, parent, false)
//                SavedMealViewHolder(view)
//            }
//            else -> throw IllegalArgumentException("Unknown view type")
//        }
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (val item = items[position]) {
//            is MergedMealItem.SnapMeal -> (holder as SnapMealViewHolder).bind(item.data)
//            is MergedMealItem.SavedMeal -> (holder as SavedMealViewHolder).bind(item.data)
//        }
//    }
//
//    inner class SnapMealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(item: SnapMealDetail) {
//            // Bind your SnapMealDetail data here
//            itemView.findViewById<TextView>(R.id.mealName).text = item.meal_name
//        }
//    }
//
//    inner class SavedMealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(item: MealDetail) {
//            // Bind your MealDetail data here
//            itemView.findViewById<TextView>(R.id.mealName).text = item.meal_name
//        }
//    }
//}
