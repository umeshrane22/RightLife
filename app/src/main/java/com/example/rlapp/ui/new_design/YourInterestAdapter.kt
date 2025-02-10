package com.example.rlapp.ui.new_design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R
import com.example.rlapp.ui.new_design.pojo.InterestDataList
import com.example.rlapp.ui.utility.Utils

class YourInterestAdapter(
    private val context: Context,
    private val interestList: ArrayList<InterestDataList>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<YourInterestAdapter.YourInterestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourInterestViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_your_interest, parent, false)
        return YourInterestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return interestList.size
    }

    override fun onBindViewHolder(holder: YourInterestViewHolder, position: Int) {
        val interest = interestList[position]
        holder.textView.text = interest.topic


        val bgDrawable = AppCompatResources.getDrawable(context, R.drawable.bg_gray_border_radius_small)

        val unwrappedDrawable =
            AppCompatResources.getDrawable(context, R.drawable.rounded_corder_border_gray_radius_small)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(
            wrappedDrawable,
            Utils.getModuleColor(context, interest.moduleName)
        )

        holder.linearLayout.background = if (interest.isSelected) wrappedDrawable else bgDrawable

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(interest)
            interest.isSelected = !interest.isSelected
            notifyItemChanged(position)
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(interest: InterestDataList)
    }

    class YourInterestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_row_your_interest)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.ll_your_interest)
    }
}