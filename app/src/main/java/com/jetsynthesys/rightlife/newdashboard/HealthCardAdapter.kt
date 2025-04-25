package com.jetsynthesys.rightlife.newdashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.ItemHealthCardBinding
import com.jetsynthesys.rightlife.newdashboard.model.DashboardChecklistManager
import com.jetsynthesys.rightlife.newdashboard.model.DiscoverDataItem
import com.jetsynthesys.rightlife.ui.healthcam.HealthCamActivity
import com.jetsynthesys.rightlife.ui.healthcam.NewHealthCamReportActivity

class HealthCardAdapter(private val cardList: List<DiscoverDataItem>?) :
    RecyclerView.Adapter<HealthCardAdapter.HealthCardViewHolder>() {

    inner class HealthCardViewHolder(val binding: ItemHealthCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthCardViewHolder {
        val binding =
            ItemHealthCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HealthCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HealthCardViewHolder, position: Int) {
        val item = cardList?.get(position)
        with(holder.binding) {
            if (item != null) {
                cardTitle.text = item.parameter
            }
            if (item != null) {
                tvDescription.text = item.def
            }

            val iconResReport = getReportIconByType(item?.key.toString())
            imageCard.setImageResource(iconResReport)

            /*Glide.with(root.context)
                .load(item.imageCard)
                .into(imageCard)

            Glide.with(root.context)
                .load(item.imageGraph)
                .into(imageGraph)*/

            btnDiscover.setOnClickListener {
                val context = holder.itemView.context
                val intent = if (DashboardChecklistManager.facialScanStatus) {
                    Intent(context, NewHealthCamReportActivity::class.java)
                } else {
                    Intent(context, HealthCamActivity::class.java)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = cardList!!.size

    private fun getReportIconByType(type: String): Int {
        return when (type) {
            "BMI_CALC" -> R.drawable.ic_db_report_bmi
            "BP_RPP" -> R.drawable.ic_db_report_cardiak_workload
            "BP_SYSTOLIC" -> R.drawable.ic_db_report_bloodpressure
            "BP_CVD" -> R.drawable.ic_db_report_cvdrisk
            "MSI" -> R.drawable.ic_db_report_stresslevel
            "BR_BPM" -> R.drawable.ic_db_report_respiratory_rate
            "HRV_SDNN" -> R.drawable.ic_db_report_heart_variability
            else -> R.drawable.ic_db_report_heart_rate
        }
    }

}
