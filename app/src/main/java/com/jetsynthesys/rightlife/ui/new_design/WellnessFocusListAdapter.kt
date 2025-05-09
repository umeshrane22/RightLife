package com.jetsynthesys.rightlife.ui.new_design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.ui.new_design.pojo.ModuleTopic
import com.jetsynthesys.rightlife.ui.utility.Utils

class WellnessFocusListAdapter(
    private val context: Context,
    private val wellnessFocusList: ArrayList<ModuleTopic>,
    private val onItemClickListener: OnItemClickListener,
    private val module: String
) : RecyclerView.Adapter<WellnessFocusListAdapter.WellnessFocusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WellnessFocusViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_wellness_focus_list_new_design, parent, false)
        return WellnessFocusViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wellnessFocusList.size
    }

    override fun onBindViewHolder(holder: WellnessFocusViewHolder, position: Int) {
        val wellnessFocus = wellnessFocusList[position]
        //holder.imageView.setImageResource(wellnessFocus.imageResource)

        Glide.with(context).load(ApiClient.CDN_URL_QA + wellnessFocus.moduleThumbnail)
            .placeholder(R.drawable.think_right)
            .error(R.drawable.think_right)
            .into(holder.imageView)

        holder.tvHeader.text = wellnessFocus.moduleTopic


        val bgDrawable =
            AppCompatResources.getDrawable(context, R.drawable.bg_gray_border_radius_small)

        val unwrappedDrawable =
            AppCompatResources.getDrawable(context, R.drawable.rounded_corder_border_gray_radius_small)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(
            wrappedDrawable,
            Utils.getModuleColor(context, module)
        )

        holder.llWellnessFocus.background =
            if (wellnessFocus.isSelected) wrappedDrawable else bgDrawable

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(wellnessFocus)
            wellnessFocus.isSelected = !wellnessFocus.isSelected
            notifyDataSetChanged()
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(wellnessFocus: ModuleTopic)
    }

    class WellnessFocusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image_row_wellness_focus)
        var tvHeader: TextView = itemView.findViewById(R.id.tv_row_wellness_focus)
        var llWellnessFocus: LinearLayout = itemView.findViewById(R.id.ll_wellness_focus)
    }
}