package com.example.rlapp.ui.new_design


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

class RulerAdapterVertical(
    private val numbers: List<Float>, // Updated to handle float values
    private val listener: (Any) -> Unit
) : RecyclerView.Adapter<RulerAdapterVertical.RulerViewHolder>() {

    private var type = "cms"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulerViewHolder {
        // Inflate the layout item for each ruler number
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.ruler_item_vertical, parent, false)
        return RulerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RulerViewHolder, position: Int) {
        val number = numbers[position]

        // Set the number text
        holder.numberText.text = number.toInt().toString()

        // Customize the line height
        if (type == "cms") {
            if (number % 5 == 0f) { // Whole numbers get big lines
                holder.rulerLineSmall.visibility = View.GONE
                holder.rulerLine.visibility = View.VISIBLE
                holder.numberText.visibility = View.VISIBLE
            } else {
                // Small line for decimal numbers
                holder.rulerLineSmall.visibility = View.VISIBLE
                holder.rulerLine.visibility = View.GONE
                holder.numberText.visibility = View.GONE
            }
        } else {
            if (number % 12 == 0f) {
                holder.rulerLineSmall.visibility = View.GONE
                holder.rulerLine.visibility = View.VISIBLE
                holder.numberText.visibility = View.VISIBLE
                holder.numberText.text = (number/12).toInt().toString()
            } else {
                holder.rulerLineSmall.visibility = View.VISIBLE
                holder.rulerLine.visibility = View.GONE
                holder.numberText.visibility = View.GONE
            }
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

    fun setType(type: String) {
        this.type = type
    }
}
