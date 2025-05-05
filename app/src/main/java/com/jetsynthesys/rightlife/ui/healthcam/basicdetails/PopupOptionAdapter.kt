package com.jetsynthesys.rightlife.ui.healthcam.basicdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.Option

class PopupOptionAdapter(
    private val options: List<Option>,
    private val onClick: (Option) -> Unit
) : RecyclerView.Adapter<PopupOptionAdapter.OptionViewHolder>() {

    inner class OptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvOption: TextView = view.findViewById(R.id.tvOption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_popup_option, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val option = options[position]
        holder.tvOption.text = option.optionText
        holder.tvOption.setOnClickListener {
            onClick(option)
        }
    }

    override fun getItemCount(): Int = options.size
}
