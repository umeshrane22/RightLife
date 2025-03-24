package com.example.rlapp.ui.questionnaire.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.databinding.ItemScheduleOptionBinding
import com.example.rlapp.ui.questionnaire.pojo.ScheduleOption

class ScheduleOptionAdapter(
    private val options: List<ScheduleOption>,
    private val type: String = "EatRight",
    private val onItemClick: (ScheduleOption) -> Unit
) : RecyclerView.Adapter<ScheduleOptionAdapter.ScheduleOptionViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ScheduleOptionViewHolder(val binding: ItemScheduleOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: ScheduleOption, isSelected: Boolean) {
            binding.tvTitle.text = option.title
            binding.tvDescription.text = option.description
            binding.ivScheduleImage.setImageResource(option.imageResId)

            if (isSelected) {
                when(type){
                    "EatRight" ->
                        binding.scheduleItemRoot.setBackgroundResource(R.drawable.bg_food_item_selected)
                    "MoveRight" ->
                        binding.scheduleItemRoot.setBackgroundResource(R.drawable.bg_move_right_selected)
                    "SleepRight" ->
                        binding.scheduleItemRoot.setBackgroundResource(R.drawable.bg_item_selected_sleep_right)
                    "ThinkRight" ->
                        binding.scheduleItemRoot.setBackgroundResource(R.drawable.bg_item_selected_think_right)
                }

            } else {
                binding.scheduleItemRoot.setBackgroundResource(R.drawable.bg_food_item)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleOptionViewHolder {
        val binding =
            ItemScheduleOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleOptionViewHolder, position: Int) {
        holder.bind(options[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = options.size
}
