package com.jetsynthesys.rightlife.ui.profile_new.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.databinding.RowReasonsDeleteBinding

class DeleteReasonAdapter(private val reasons: List<String>) :
    RecyclerView.Adapter<DeleteReasonAdapter.ReasonViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ReasonViewHolder(val binding: RowReasonsDeleteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReasonViewHolder {
        val binding =
            RowReasonsDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReasonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReasonViewHolder, position: Int) {
        val reason = reasons[position]
        holder.binding.reasonText.text = reason
        holder.binding.radioButton.isChecked = (position == selectedPosition)

        holder.binding.root.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int = reasons.size

    fun getSelectedReason(): String? {
        return if (selectedPosition != RecyclerView.NO_POSITION) reasons[selectedPosition] else null
    }
}

