package com.jetsynthesys.rightlife.ui.questionnaire.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.RowSocialInteractionsBinding
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.SocialInteraction

class SocialInteractionAdapter(
    private val items: List<SocialInteraction>,
    private val onItemClick: (SocialInteraction) -> Unit
) : RecyclerView.Adapter<SocialInteractionAdapter.ViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: RowSocialInteractionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SocialInteraction, isSelected: Boolean) {
            binding.tvTitle.text = item.title
            binding.ivIcon.setImageResource(item.iconRes)
            if (isSelected) {
                binding.root.setBackgroundResource(R.drawable.bg_item_selected_think_right)
            } else {
                binding.root.setBackgroundResource(R.drawable.bg_food_item)
            }
            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowSocialInteractionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = items.size
}
