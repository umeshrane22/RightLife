package com.jetsynthesys.rightlife.ui.new_design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.ui.new_design.pojo.ModuleService
import com.jetsynthesys.rightlife.ui.utility.Utils

class OnBoardingModuleListAdapter(
    private val context: Context,
    private val onItemClickListener: OnItemClickListener,
    private val moduleServices: List<ModuleService>,
    private var selectedPosition: Int = -1

    ) : RecyclerView.Adapter<OnBoardingModuleListAdapter.OnBoardingModuleViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingModuleViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_onboarding_module, parent, false)
        return OnBoardingModuleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moduleServices.size
    }

    override fun onBindViewHolder(holder: OnBoardingModuleViewHolder, position: Int) {
        val moduleService = moduleServices[position]
        holder.tvHeader.text = moduleService.moduleName
        holder.tvDescription.text = moduleService.moduleTitle

        holder.tvHeader.setTextColor(Utils.getModuleDarkColor(context, moduleService.moduleName))
        holder.tvDescription.setTextColor(Utils.getModuleDarkColor(context, moduleService.moduleName))

        Glide.with(context).load(ApiClient.CDN_URL_QA + moduleService.moduleThumbnail)
            .placeholder(R.drawable.think_right)
            /*.error(R.drawable.avatar)*/
            .into(holder.imageView)

        val bgDrawable = AppCompatResources.getDrawable(context, R.drawable.bg_gray_border)

        val unwrappedDrawable =
            AppCompatResources.getDrawable(context, R.drawable.rounded_corder_border_gray)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(
            wrappedDrawable,
            Utils.getModuleColor(context, moduleService.moduleName)
        )

        holder.rlOnBoardingModule.background =
            if (selectedPosition == position) wrappedDrawable else bgDrawable

       /* holder.rlOnBoardingModule.background =
            if (moduleService.isSelected) wrappedDrawable else bgDrawable*/


        holder.itemView.setOnClickListener {

            selectedPosition = position

            onItemClickListener.onItemClick(service = moduleService)
            moduleService.isSelected = !moduleService.isSelected
            notifyDataSetChanged()
        }

    }

    fun interface OnItemClickListener {
        fun onItemClick(service: ModuleService)
    }

    fun setSelectedPosition(position: Int){
        selectedPosition = position
        notifyDataSetChanged()
    }

    class OnBoardingModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.img_onboarding_module)
        var tvHeader: TextView = itemView.findViewById(R.id.tv_onboarding_module_header)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_onboarding_module_desc)
        var rlOnBoardingModule: RelativeLayout = itemView.findViewById(R.id.rl_onboarding_module)
    }
}