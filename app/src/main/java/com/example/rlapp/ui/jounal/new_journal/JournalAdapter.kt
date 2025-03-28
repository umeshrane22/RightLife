package com.example.rlapp.ui.jounal.new_journal

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rlapp.RetrofitData.ApiClient
import com.example.rlapp.databinding.RowJournalNewBinding
import com.example.rlapp.ui.utility.Utils

class JournalAdapter(
    private val items: List<JournalItem>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    inner class JournalViewHolder(val binding: RowJournalNewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val binding =
            RowJournalNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JournalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            titleText.text = item.title
            descText.text = item.desc

            /***
             * 000000 is replaced with actual color when get it from API
             */
            titleText.setTextColor(Utils.getColorFromColorCode("000000"))
            descText.setTextColor(Utils.getColorFromColorCode("000000"))

            //imageViewAdd.imageTintList = Utils.getColorStateListFromColorCode("FD6967")

            //container.backgroundTintList = Utils.getColorStateListFromColorCode(item.color)

            Glide.with(imageView.context)
                .load(ApiClient.CDN_URL_QA + item.image)
                .into(imageView)

            root.setOnClickListener{
                onItemClickListener.onClick(item)
            }
        }
    }

    fun interface OnItemClickListener {
        fun onClick(journalItem: JournalItem)
    }

    override fun getItemCount(): Int = items.size
}
