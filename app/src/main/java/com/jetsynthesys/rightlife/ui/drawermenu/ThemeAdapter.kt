package com.jetsynthesys.rightlife.ui.drawermenu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R

class ThemeAdapter(
    val context: Context,
    private val themes: ArrayList<ThemeItem>,
    val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>() {

    private var selectedItemPos = -1
    private var lastItemSelectedPos = -1

    fun interface OnItemClickListener {
        fun onItemClick(themeItem: ThemeItem)
    }

    class ThemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvSystem)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.ll_system_mode)
        val radio: RadioButton = itemView.findViewById(R.id.radioSystem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_theme, parent, false)
        return ThemeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return themes.size
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val themeItem = themes[position]
        holder.textView.text = themeItem.value

        val bgDrawable =
            AppCompatResources.getDrawable(context, R.drawable.bg_rightlife_border)

        val unwrappedDrawable =
            AppCompatResources.getDrawable(
                context,
                R.drawable.rounded_corder_border_gray_radius_small
            )
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(
            wrappedDrawable,
            ContextCompat.getColor(
                context,
                R.color.rightlife
            )
        )

        if (themeItem.isSelected)
            selectedItemPos = position

        holder.linearLayout.background =
            if (position == selectedItemPos) wrappedDrawable else bgDrawable

        holder.radio.isChecked = position == selectedItemPos

        holder.itemView.setOnClickListener {
            for (theme in themes){
                theme.isSelected = false
            }
            themeItem.isSelected = !themeItem.isSelected
            selectedItemPos = holder.adapterPosition
            lastItemSelectedPos = selectedItemPos
            notifyDataSetChanged()
            onItemClickListener.onItemClick(themeItem)
        }

    }
}