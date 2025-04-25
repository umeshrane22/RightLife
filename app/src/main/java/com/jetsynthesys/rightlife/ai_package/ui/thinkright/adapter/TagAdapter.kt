package com.jetsynthesys.rightlife.ai_package.ui.thinkright.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R

class TagAdapter(private val tags: List<String>) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    inner class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tagText: TextView = view.findViewById(R.id.tagText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.tagText.text = tags[position]
    }

    override fun getItemCount(): Int = tags.size
}