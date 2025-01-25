package com.example.rlapp.ui.new_design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ui.new_design.pojo.BodyFat

class BodyFatAdapter(
    private val context: Context,
    private val bodyFatLists: ArrayList<BodyFat>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<BodyFatAdapter.BodyFatViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyFatViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_body_fat, parent, false)
        return BodyFatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bodyFatLists.size
    }

    override fun onBindViewHolder(holder: BodyFatViewHolder, position: Int) {
        holder.tvHeader.text = bodyFatLists[position].bodyFatNumber
        holder.imageView.setImageResource(bodyFatLists[position].image)

        val bgDrawable = AppCompatResources.getDrawable(context, R.drawable.bg_edittext_gray)

        val unwrappedDrawable =
            AppCompatResources.getDrawable(context, R.drawable.rounded_corder_border_gray)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(
            wrappedDrawable,
            ContextCompat.getColor(context, R.color.color_green)
        )

        holder.llBodyFat.background =
            if (selectedPosition == position) wrappedDrawable else bgDrawable

        holder.itemView.setOnClickListener {

            selectedPosition = position

            onItemClickListener.onItemClick(bodyFat = bodyFatLists[position])
            bodyFatLists[position].isSelected = !bodyFatLists[position].isSelected
            notifyDataSetChanged()
        }
    }

    fun clearSelection() {
        for (i in 0 until bodyFatLists.size) {
            bodyFatLists[i].isSelected = false
        }
        selectedPosition = -1
        notifyDataSetChanged()
    }

    fun setSelected(position: Int) {
        bodyFatLists[position].isSelected = true
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun interface OnItemClickListener {
        fun onItemClick(bodyFat: BodyFat)
    }

    class BodyFatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image_row_body_fat)
        var tvHeader: TextView = itemView.findViewById(R.id.tv_row_body_fat)
        var llBodyFat: LinearLayout = itemView.findViewById(R.id.ll_body_fat)
    }
}