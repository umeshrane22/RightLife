package com.jetsynthesys.rightlife.ai_package.ui.thinkright.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.ai_package.model.ContentList

class RecommendationAdapter(val context: Context,private val items: ArrayList<ContentList>) :
    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    inner class RecommendationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleText)
        val tag: TextView = view.findViewById(R.id.topicTag2)
        val author: TextView = view.findViewById(R.id.authorText)
        val image: ImageView = view.findViewById(R.id.thumbnailImage)
        val overlay: ImageView = view.findViewById(R.id.overlayIcon)
        val viewLine: View = view.findViewById(R.id.view_line)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recomendation, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.desc
        holder.tag.text = "${item.subCategories.getOrNull(0)?.name}"
       holder.author.text = "${item.artist?.getOrNull(0)?.firstName+" "+item.artist?.getOrNull(0)?.lastName}   |   ${item.viewCount}"
        Glide.with(context)
            .load( "https://d1sacaybzizpm5.cloudfront.net/"+item.thumbnail?.url)
            .placeholder(R.drawable.ic_galory)
            .into(holder.image)
       when(item.contentType) {
           "AUDIO" ->{
               Glide.with(context)
                   .load( R.drawable.music_mini_icon)
                   .placeholder(R.drawable.music_mini_icon)
                   .into(holder.overlay)
           }
           "SERIES" ->{
               Glide.with(context)
                   .load( R.drawable.book_mini_icon)
                   .placeholder(R.drawable.book_mini_icon)
                   .into(holder.overlay)
           }
           "VIDEO"  ->{
               Glide.with(context)
                   .load( R.drawable.play_mini_icon)
                   .placeholder(R.drawable.play_mini_icon)
                   .into(holder.overlay)
           }
           "YOUTUBE"  ->{
               Glide.with(context)
                   .load( R.drawable.video_mini_icon)
                   .placeholder(R.drawable.video_mini_icon)
                   .into(holder.overlay)
           }
       }
        if (position == items.size -1){
            holder.viewLine.visibility = View.GONE
        }else{
            holder.viewLine.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = items.size
}