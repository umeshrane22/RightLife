package com.jetsynthesys.rightlife.ui.questionnaire.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.ItemRelaxAndWindBinding
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.StressReason

class BeforeGoingToBedAdapter(
    private val reasons: List<StressReason>,
    private val type: String = "ThinkRight",
    private val onSelectionChanged: (StressReason) -> Unit
) : RecyclerView.Adapter<BeforeGoingToBedAdapter.ReasonViewHolder>() {
    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ReasonViewHolder(private val binding: ItemRelaxAndWindBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reason: StressReason, position: Int) {
            binding.tvTitle.text = reason.title
            binding.ivIcon.setImageResource(reason.iconRes)

            if (type == "ThinkRight") {
                val background = if (selectedPosition == position)
                    R.drawable.bg_item_selected_think_right
                else
                    R.drawable.bg_food_item
                binding.root.setBackgroundResource(background)
            }

            if (type == "SleepRight") {
                val background = if (selectedPosition == position)
                    R.drawable.bg_item_selected_sleep_right
                else
                    R.drawable.bg_food_item
                binding.root.setBackgroundResource(background)
            }

            binding.root.setOnClickListener {

                if (position != RecyclerView.NO_POSITION && position < itemCount) {
                    val previousPosition = selectedPosition
                    selectedPosition = position
                    // Notify changes for the previous and current positions
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                    onSelectionChanged(reason)
                    reason.isSelected = !reason.isSelected
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReasonViewHolder {
        val binding =
            ItemRelaxAndWindBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReasonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReasonViewHolder, position: Int) {
        holder.bind(reasons[position], position)
    }

    override fun getItemCount(): Int = reasons.size
}
