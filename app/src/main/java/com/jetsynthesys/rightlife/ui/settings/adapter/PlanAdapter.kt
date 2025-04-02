package com.jetsynthesys.rightlife.ui.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.databinding.RowPlanBinding
import com.jetsynthesys.rightlife.ui.settings.pojo.Plan

class PlanAdapter(
    private val plans: List<Plan>,
    private val onPlanSelected: (Plan) -> Unit
) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    private var selectedPosition = -1

    inner class PlanViewHolder(private val binding: RowPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: Plan, position: Int) {
            binding.planName.text = plan.name
            binding.planDescription.text = plan.description
            binding.radioButton.isChecked = position == selectedPosition

            binding.root.setOnClickListener {
                selectedPosition = adapterPosition
                onPlanSelected(plan)
                notifyDataSetChanged()
            }

            binding.radioButton.setOnClickListener {
                selectedPosition = adapterPosition
                onPlanSelected(plan)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = RowPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(plans[position], position)
    }

    override fun getItemCount(): Int = plans.size
}
