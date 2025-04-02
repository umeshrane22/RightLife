package com.example.rlapp.ai_package.ui.thinkright.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

class MindAuditCarouselAdapter(private val images: List<Int>) :
    RecyclerView.Adapter<MindAuditCarouselAdapter.MindAuditViewHolder>() {

    class MindAuditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MindAuditViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carousel_mind_audit, parent, false)
        return MindAuditViewHolder(view)
    }

    override fun onBindViewHolder(holder: MindAuditViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int = images.size
}
