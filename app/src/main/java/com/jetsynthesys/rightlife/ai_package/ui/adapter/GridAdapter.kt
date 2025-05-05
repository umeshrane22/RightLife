package com.jetsynthesys.rightlife.ai_package.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.model.GridItem
import com.jetsynthesys.rightlife.ai_package.ui.moveright.graphs.BarGraphView
import com.jetsynthesys.rightlife.ai_package.ui.moveright.graphs.LineGraphView

class GridAdapter(
    private var items: List<GridItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val textView: TextView = itemView.findViewById(R.id.item_text)
        val no_data_cardview: TextView = itemView.findViewById(R.id.no_data_cardview)
        val additionalTextView: TextView = itemView.findViewById(R.id.unit_rating)
        val fourthTextView: TextView = itemView.findViewById(R.id.rating)
        val lineGraphView: LineGraphView = itemView.findViewById(R.id.line_graph)
        val barGraphView: BarGraphView = itemView.findViewById(R.id.BarGraphView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_layout_ai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.name
        holder.imageView.setImageResource(item.imageRes)
        holder.additionalTextView.text = item.additionalInfo
        holder.fourthTextView.text = item.fourthParameter

        // Check if dataPoints is empty or all values are zero for this specific item
        if (item.dataPoints.isEmpty() || item.dataPoints.all { it == 0f }) {
            holder.barGraphView.visibility = View.GONE
            holder.lineGraphView.visibility = View.GONE
            holder.no_data_cardview.visibility = View.VISIBLE
        } else {
            holder.no_data_cardview.visibility = View.GONE
            if (item.name == "Burn") {
                holder.barGraphView.visibility = View.VISIBLE
                holder.lineGraphView.visibility = View.GONE
                holder.barGraphView.setDataPoints(item.dataPoints)
            } else {
                holder.lineGraphView.visibility = View.VISIBLE
                holder.barGraphView.visibility = View.GONE
                holder.lineGraphView.setDataPoints(item.dataPoints)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(item.name)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<GridItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}