package com.jetsynthesys.rightlife.ui.NewSleepSounds.bottomplaylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.databinding.ItemPlaylistBinding
import com.jetsynthesys.rightlife.ui.NewSleepSounds.newsleepmodel.Service

class PlaylistAdapter(
    private val list: ArrayList<Service>,

    private var currentIndex: Int,

    private val onItemClick: (Int) -> Unit,

    private val onItemRemoved: (Int) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(service: Service, position: Int) {
            binding.songTitle.text = service.title
            binding.songSubtitle.text = service.desc ?: ""
            Glide.with(binding.root.context)
                .load(ApiClient.CDN_URL_QA + service.image)
                .placeholder(R.drawable.logo_rightlife)
                .into(binding.songImage)

            if (position == currentIndex) {
                binding.playingIcon.visibility = View.VISIBLE
            } else {
                binding.playingIcon.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(list[position], position)
    }
    fun removeItem(position: Int) {

        /*list.removeAt(position)

        notifyItemRemoved(position)

        notifyItemRangeChanged(position, list.size)*/

        onItemRemoved(position)

    }

}
