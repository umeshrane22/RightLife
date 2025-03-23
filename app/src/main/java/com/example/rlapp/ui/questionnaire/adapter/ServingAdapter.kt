package com.example.rlapp.ui.questionnaire.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.databinding.ItemServingBinding
import com.example.rlapp.ui.questionnaire.pojo.ServingItem

class ServingAdapter(
    private val list: ArrayList<ServingItem>,
    private val onPlusMinusClick: (ServingItem) -> Unit
) : RecyclerView.Adapter<ServingAdapter.ServingViewHolder>() {

    inner class ServingViewHolder(val binding: ItemServingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServingViewHolder {
        val binding = ItemServingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServingViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            imageIcon.setImageResource(item.iconRes)
            textTitle.text = item.title
            textServing.text = item.servingText
            textCount.text = item.count.toString()

            btnPlus.setOnClickListener {
                item.count++
                textCount.text = item.count.toString()
                onPlusMinusClick(item)
            }

            btnMinus.setOnClickListener {
                if (item.count > 0) {
                    item.count--
                    textCount.text = item.count.toString()
                    onPlusMinusClick(item)
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}
