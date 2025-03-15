package com.example.rlapp.ui.jounal.new_journal

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.rlapp.R

class PopupMenuAdapter(private val context: Context, private val menuItems: List<MenuItemData>,
private val onItemClickListener: OnItemClickListener) : BaseAdapter() {

    override fun getCount(): Int = menuItems.size
    override fun getItem(position: Int): Any = menuItems[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.popup_menu_item, parent, false)
        
        val menuText = view.findViewById<TextView>(R.id.menuText)
        val menuIcon = view.findViewById<ImageView>(R.id.menuIcon)

        val item = menuItems[position]
        menuText.text = item.title
        menuText.setTextColor(item.color)
        menuIcon.setImageResource(item.iconRes)

        view.setOnClickListener{
            onItemClickListener.onItemClick(item)
        }

        return view
    }

    fun interface OnItemClickListener{
        fun onItemClick(menuItem: MenuItemData)
    }
}

data class MenuItemData(val title: String, val iconRes: Int, val color: Int)
