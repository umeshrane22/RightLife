package com.jetsynthesys.rightlife.ai_package.ui.eatright.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R

class LogWeightRulerAdapter(private val values: List<Float>, private val listener: (Any) -> Unit) :
    RecyclerView.Adapter<LogWeightRulerAdapter.RulerViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulerViewHolder {
            // Inflate the layout item for each ruler number
            val itemView: View =
                LayoutInflater.from(parent.context).inflate(R.layout.log_weight_ruler_item, parent, false)
            return RulerViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RulerViewHolder, position: Int) {
            val number = values[position]

            // Set the number text
            holder.numberText.text = number.toString()

            // Customize the line height
            if (number % 1 == 0f) { // Whole numbers get big lines
                holder.ruler_line_small.visibility = View.GONE
                holder.rulerLine.visibility = View.VISIBLE
                holder.numberText.visibility = View.VISIBLE
            } else {
                // Small line for decimal numbers
                holder.ruler_line_small.visibility = View.VISIBLE
                holder.rulerLine.visibility = View.GONE
                holder.numberText.visibility = View.GONE
            }

            // Set click listener to show number
            // Set click listener to show number
            holder.itemView.setOnClickListener {
                listener(number) // Use the lambda function to handle the selected number
            }
        }

        override fun getItemCount(): Int {
            return values.size
        }

        interface OnNumberSelectedListener {
            fun onNumberSelected(number: Float)
        }

        class RulerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var numberText: TextView = itemView.findViewById<TextView>(R.id.number_text)
            var rulerLine: View = itemView.findViewById<View>(R.id.ruler_line)
            var ruler_line_small: View = itemView.findViewById<View>(R.id.ruler_line_small)
        }
    }


