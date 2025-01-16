package com.example.rlapp.ui.new_design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

class ImageSliderAdapter(
    private val context: Context,
    private val images: List<Int>,
    private val heading: List<String>,
    private val description: List<String>
) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_image_slidder, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
        holder.tvHeader.text = heading[position]
        holder.tvDesc.text = description[position]
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image_slider)
        var tvHeader: TextView = itemView.findViewById(R.id.tv_header)
        var tvDesc: TextView = itemView.findViewById(R.id.tv_description)
    }
}
