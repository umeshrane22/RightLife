package com.example.rlapp.ai_package.ui.thinkright.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ai_package.model.ToolsData
import com.example.rlapp.ai_package.ui.eatright.model.MyMealModel

class ToolsAdapter(private val context: Context, private var dataLists: ArrayList<ToolsData>?,
                   val onToolItem: (ToolsData, Int, Boolean) -> Unit)
    : RecyclerView.Adapter<ToolsAdapter.ViewHolder>() {

    val imageList = arrayListOf(
        R.drawable.breathwork_icon,    // Replace with actual drawable names
        R.drawable.journaling_ink_icon,
        R.drawable.quote_grey_icon,
        R.drawable.meditate_icon
    )

    val colorList = arrayListOf(
        R.color.blue_bar,
        R.color.thinkright_text_color,
        R.color.ql_affirmation_color,
        R.color.meditate_text_color
    )
    private var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tools_card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToolsAdapter.ViewHolder, position: Int) {
        imageList.getOrNull(position)?.let { holder.itemView.findViewById<ImageView>(R.id.iconImageView).setImageResource(it) }
        holder.itemView.findViewById<TextView>(R.id.titleTextView).setText(dataLists?.getOrNull(position)?.moduleName)
        colorList.getOrNull(position)?.let { holder.itemView.findViewById<TextView>(R.id.titleTextView).setTextColor(it) }
        holder.itemView.findViewById<TextView>(R.id.subtitleTextView).setText(dataLists?.getOrNull(position)?.subtitle)


        holder.itemView.findViewById<CardView>(R.id.mainLayout).setOnClickListener {
            dataLists?.getOrNull(position)?.let { it1 -> onToolItem(it1, position, true) }
        }
    }

    override fun getItemCount(): Int {
        return dataLists?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}