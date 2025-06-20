package com.jetsynthesys.rightlife.ui.questionnaire.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.databinding.RowPhysicalActivityDialogBinding
import com.jetsynthesys.rightlife.ui.questionnaire.pojo.ServingItem

class PhysicalActivityDialogAdapter(
    private val list: ArrayList<ServingItem>,
    private val limit: Int,
    private val onPlusMinusClick: (ServingItem, count: Int) -> Unit
) : RecyclerView.Adapter<PhysicalActivityDialogAdapter.ServingViewHolder>() {

    inner class ServingViewHolder(val binding: RowPhysicalActivityDialogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServingViewHolder {
        val binding = RowPhysicalActivityDialogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ServingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServingViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            textTitle1.text = item.title
            textCount1.text = item.count.toString()

            btnPlus1.setOnClickListener {
                if (item.count < limit) {
                    item.count++
                    textCount1.text = item.count.toString()
                    onPlusMinusClick(item, 1)
                }
            }

            btnMinus1.setOnClickListener {
                if (item.count > 0) {
                    item.count--
                    textCount1.text = item.count.toString()
                    onPlusMinusClick(item, -1)
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}
