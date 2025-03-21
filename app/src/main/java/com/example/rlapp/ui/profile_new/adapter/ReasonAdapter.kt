package com.example.rlapp.ui.profile_new.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.databinding.RowReasonsDeleteBinding

class ReasonAdapter(private val reasons: List<String>) :
    RecyclerView.Adapter<ReasonAdapter.ReasonViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

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
        holder.binding.radioButton.isChecked = selectedItems.contains(position)

        // OnClick Listener
        holder.binding.root.setOnClickListener {
            if (selectedItems.contains(position)) {
                selectedItems.remove(position)
            } else {
                selectedItems.add(position)
            }
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = reasons.size

    fun getSelectedReasons(): List<String> {
        return selectedItems.map { reasons[it] }
    }
}
