package com.jetsynthesys.rightlife.ui.jounal.new_journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R

class PromptAdapter(
    private val prompts: ArrayList<Question>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<PromptAdapter.PromptViewHolder>() {

    inner class PromptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPrompt: TextView = itemView.findViewById(R.id.tvPrompt)
        val ivRefresh: ImageView = itemView.findViewById(R.id.ivRefresh)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromptViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_journal_prompt, parent, false)
        return PromptViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromptViewHolder, position: Int) {
        val question = prompts[position]
        holder.tvPrompt.text = question.question
        holder.ivRefresh.setOnClickListener {
            onItemClickListener.onSwapClick(question, position)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(question)
        }
    }

    override fun getItemCount() = prompts.size

    interface OnItemClickListener {
        fun onItemClick(question: Question)
        fun onSwapClick(question: Question, position: Int)
    }
}
