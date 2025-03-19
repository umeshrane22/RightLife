package com.example.rlapp.ui.breathwork

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.databinding.ItemBreathworkBinding

class BreathworkAdapter(
    private val items: List<BreathworkPattern>,
    private val onItemClick: (BreathworkPattern) -> Unit
) : RecyclerView.Adapter<BreathworkAdapter.BreathworkViewHolder>() {

    inner class BreathworkViewHolder(val binding: ItemBreathworkBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreathworkViewHolder {
        val binding = ItemBreathworkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BreathworkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BreathworkViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            titleTextView.text = item.title
            descriptionTextView.text = item.description
            imageView.setImageResource(item.imageResId)

            plusButton.setOnClickListener {
                onItemClick(item)
            }

            cardView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount() = items.size
}
