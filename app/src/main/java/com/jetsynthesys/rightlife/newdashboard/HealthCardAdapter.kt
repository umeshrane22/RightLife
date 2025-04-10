package com.jetsynthesys.rightlife.newdashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.databinding.ItemHealthCardBinding
import com.jetsynthesys.rightlife.newdashboard.model.HealthCard

class HealthCardAdapter(private val cardList: ArrayList<HealthCard>) :
    RecyclerView.Adapter<HealthCardAdapter.HealthCardViewHolder>() {

    inner class HealthCardViewHolder(val binding: ItemHealthCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthCardViewHolder {
        val binding =
            ItemHealthCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HealthCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HealthCardViewHolder, position: Int) {
        val item = cardList[position]
        with(holder.binding) {
            cardTitle.text = item.cardTitle
            tvDescription.text = item.description

            /*Glide.with(root.context)
                .load(item.imageCard)
                .into(imageCard)

            Glide.with(root.context)
                .load(item.imageGraph)
                .into(imageGraph)*/

            btnDiscover.setOnClickListener {
                // Handle click
            }
        }
    }

    override fun getItemCount(): Int = cardList.size
}
