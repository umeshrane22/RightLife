package com.example.rlapp.ui.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.databinding.RowSettingsBinding
import com.example.rlapp.ui.settings.pojo.FAQDetails

class FAQNewAdapter(
    private val items: MutableList<FAQDetails>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<FAQNewAdapter.FAQViewHolder>() {

    inner class FAQViewHolder(val binding: RowSettingsBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClick(faqData: FAQDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val binding = RowSettingsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FAQViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val faqDetails = items[position]
        with(holder.binding) {
            settingTitle.text = faqDetails.question
        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(faqDetails)
        }
    }
}