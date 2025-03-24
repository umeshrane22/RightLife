package com.example.rlapp.ui.questionnaire.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.databinding.ItemMealOptionBinding
import com.example.rlapp.ui.questionnaire.pojo.MealOption

class MealOptionAdapter(
    private val options: List<MealOption>,
    private val onItemClick: (MealOption) -> Unit
) : RecyclerView.Adapter<MealOptionAdapter.MealOptionViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class MealOptionViewHolder(val binding: ItemMealOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: MealOption, isSelected: Boolean) {
            binding.tvMealTitle.text = option.title
            binding.ivMealImage.setImageResource(option.imageResId)

            if (isSelected) {
                binding.mealItemRoot.setBackgroundResource(R.drawable.bg_food_item_selected)
            } else {
                binding.mealItemRoot.setBackgroundResource(R.drawable.bg_food_item)
            }

            binding.root.setOnClickListener {
                val prevPos = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(prevPos)
                notifyItemChanged(selectedPosition)
                onItemClick(option)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealOptionViewHolder {
        val binding = ItemMealOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealOptionViewHolder, position: Int) {
        holder.bind(options[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = options.size
}
