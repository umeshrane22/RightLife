package com.example.rlapp.ai_package.ui.thinkright.adapter

import Phq9Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

class Phq9Adapter(private val items: List<Phq9Item>) :
    RecyclerView.Adapter<Phq9Adapter.Phq9ViewHolder>() {
    class Phq9ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val scoreTextView: TextView = view.findViewById(R.id.tvScore)
        val categoryTextView: TextView = view.findViewById(R.id.tvScoreCategory)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Phq9ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_mind_audit_phq9, parent, false)
        return Phq9ViewHolder(view)
    }
    override fun onBindViewHolder(holder: Phq9ViewHolder, position: Int) {
        val item = items[position]
        holder.scoreTextView.text = item.score.toString()
        holder.categoryTextView.text = item.category
    }
    override fun getItemCount(): Int = items.size
}