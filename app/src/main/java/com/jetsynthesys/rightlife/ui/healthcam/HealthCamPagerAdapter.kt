package com.jetsynthesys.rightlife.ui.healthcam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HealthCamPagerAdapter // Constructor to accept an array of layouts (resources IDs)
    (private val layouts: IntArray) : RecyclerView.Adapter<HealthCamPagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for the current page
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layouts[viewType], parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Nothing to bind since the layout is static, but you can configure your view here if needed
    }

    override fun getItemCount(): Int {
        return layouts.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // ViewHolder class
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
