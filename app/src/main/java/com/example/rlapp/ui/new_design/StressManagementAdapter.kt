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
import com.example.rlapp.ui.new_design.pojo.StressManagement
import com.example.rlapp.ui.utility.SharedPreferenceManager
import com.example.rlapp.ui.utility.Utils

class StressManagementAdapter(
    private val context: Context,
    private val stressManagementList: ArrayList<StressManagement>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<StressManagementAdapter.StressManagementViewHolder>() {
    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StressManagementViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_stress_management, parent, false)
        return StressManagementViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stressManagementList.size
    }

    override fun onBindViewHolder(holder: StressManagementViewHolder, position: Int) {
        holder.tvHeader.text = stressManagementList[position].header
        holder.tvDescription.text = stressManagementList[position].description
        holder.imageView.setImageResource(stressManagementList[position].imageResource)

        val bgDrawable = AppCompatResources.getDrawable(context, R.drawable.bg_gray_border_transperent)

        val unwrappedDrawable =
            AppCompatResources.getDrawable(context, R.drawable.rounded_corder_border_gray)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(
            wrappedDrawable,
            Utils.getModuleColor(context, SharedPreferenceManager.getInstance(context).selectedOnboardingModule)
        )

        holder.llStressManagement.background =
            if (selectedPosition == position) wrappedDrawable else bgDrawable

        holder.itemView.setOnClickListener {

            selectedPosition = position

            onItemClickListener.onItemClick(stressManagementList[position])
            stressManagementList[position].isSelected = !stressManagementList[position].isSelected
            notifyDataSetChanged()
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(stressManagement: StressManagement)
    }

    class StressManagementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image_row_stress_management)
        var tvHeader: TextView = itemView.findViewById(R.id.tv_row_stress_management)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_row_desc_stress_management)
        var llStressManagement: LinearLayout = itemView.findViewById(R.id.ll_stress_management)
    }
}