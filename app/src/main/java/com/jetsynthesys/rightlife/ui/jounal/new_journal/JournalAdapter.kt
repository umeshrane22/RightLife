package com.jetsynthesys.rightlife.ui.jounal.new_journal

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.RowJournalNewBinding
import com.jetsynthesys.rightlife.ui.showBalloonWithDim

class JournalAdapter(
    private val context: Context,
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

        if (position == 0) {
            context.showBalloonWithDim(
                holder.binding.imageViewAdd,
                "Tap to add this journaling type to your toolkit.",
                "JournalAdapter",
                xOff = -200
            )
        }

        val item = items[position]
        with(holder.binding) {
            titleText.text = item.title
            descText.text = item.desc

            //imageViewAdd.imageTintList = Utils.getColorStateListFromColorCode("FD6967")
            imageViewAdd.setImageResource(
                if (item.isAddedToToolKit) R.drawable.greentick else R.drawable.add_journal
            )

            val colorStateList: ColorStateList
            when (item.title) {
                "Free Form" -> {
                    colorStateList = ColorStateList.valueOf(Color.parseColor("#F7E6B7"))
                    imageView.setImageResource(R.drawable.ic_freeform_journal)
                }

                "Bullet" -> {
                    colorStateList = ColorStateList.valueOf(Color.parseColor("#FDD3D2"))
                    imageView.setImageResource(R.drawable.ic_bullet_journal)
                }

                "Gratitude" -> {
                    colorStateList = ColorStateList.valueOf(Color.parseColor("#A6E0CE"))
                    imageView.setImageResource(R.drawable.ic_gratitude_journal)
                }

                else -> {
                    colorStateList = ColorStateList.valueOf(Color.parseColor("#E6F0FE"))
                    imageView.setImageResource(R.drawable.ic_grief_journal)
                }
            }

            container.backgroundTintList = colorStateList

            /*Glide.with(imageView.context)
                .load(ApiClient.CDN_URL_QA + item.image)
                .into(imageView)*/

            root.setOnClickListener {
                onItemClickListener.onClick(item)
            }
            imageViewAdd.setOnClickListener {
                onItemClickListener.onAddToolTip(item)
                item.isAddedToToolKit = !item.isAddedToToolKit
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(journalItem: JournalItem)
        fun onAddToolTip(journalItem: JournalItem)
    }


    override fun getItemCount(): Int = items.size
}
