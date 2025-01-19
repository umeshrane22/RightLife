package com.example.rlapp.ui.new_design

import com.example.rlapp.R



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RulerAdapterVertical(
    private val numbers: List<Float>, // Updated to handle float values
    private val listener: (Any) -> Unit
) : RecyclerView.Adapter<RulerAdapterVertical.RulerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulerViewHolder {
        // Inflate the layout item for each ruler number
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ruler_item_vertical, parent, false)
        return RulerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RulerViewHolder, position: Int) {
        val number = numbers[position]

        // Set the number text
        holder.numberText.text = number.toString()

        // Customize the line height
        if (number % 1 == 0f) { // Whole numbers get big lines
            holder.rulerLineSmall.visibility = View.GONE
            holder.rulerLine.visibility = View.VISIBLE
            holder.numberText.visibility = View.VISIBLE
        } else {
            // Small line for decimal numbers
            holder.rulerLineSmall.visibility = View.VISIBLE
            holder.rulerLine.visibility = View.GONE
            holder.numberText.visibility = View.GONE
        }

        // Set click listener to show number
        holder.itemView.setOnClickListener {
            listener(number) // Use the lambda function to handle the selected number
        }
    }

    override fun getItemCount(): Int = numbers.size

    interface OnNumberSelectedListener {
        fun onNumberSelected(number: Float)
    }

    class RulerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numberText: TextView = itemView.findViewById(R.id.number_text)
        val rulerLine: View = itemView.findViewById(R.id.ruler_line)
        val rulerLineSmall: View = itemView.findViewById(R.id.ruler_line_small)
    }
}
