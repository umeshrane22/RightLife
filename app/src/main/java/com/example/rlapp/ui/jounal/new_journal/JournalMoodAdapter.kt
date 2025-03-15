package com.example.rlapp.ui.jounal.new_journal

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

class JournalMoodAdapter(
    private val moods: List<Mood>,
    private val onMoodSelected: (Mood) -> Unit
) : RecyclerView.Adapter<JournalMoodAdapter.MoodViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class MoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val moodIcon: ImageView = itemView.findViewById(R.id.moodIcon)
        private val moodLabel: TextView = itemView.findViewById(R.id.moodLabel)
        private val iconWrapper: FrameLayout = itemView.findViewById(R.id.iconWrapper)

        fun bind(mood: Mood, isSelected: Boolean) {
            moodIcon.setImageResource(mood.iconResId)
            moodLabel.text = mood.name

            // Apply selected state
            iconWrapper.isSelected = isSelected
            moodLabel.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
            moodIcon.alpha = if (isSelected) 1.0f else 0.5f
            // Remove scaleType on selection
            moodIcon.scaleType = if (isSelected) ImageView.ScaleType.FIT_CENTER else ImageView.ScaleType.CENTER_INSIDE

            // Animate selection
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition

                if (previousPosition != RecyclerView.NO_POSITION) notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onMoodSelected(mood)

                // Simple scale animation
                iconWrapper.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(100)
                    .withEndAction {
                        iconWrapper.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100)
                            .start()
                    }.start()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_journal_mood, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        holder.bind(moods[position], isSelected)
    }

    override fun getItemCount() = moods.size
}
