package com.jetsynthesys.rightlife.ai_package.ui.sleepright.adapter

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
import com.jetsynthesys.rightlife.ai_package.ui.thinkright.adapter.RecommendationAdapter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RecommendedAdapterSleep(val context: Context, private val items: ArrayList<ContentList>) :
    RecyclerView.Adapter<RecommendedAdapterSleep.RecommendationViewHolder>() {

    inner class RecommendationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleText)
        val tag: TextView = view.findViewById(R.id.topicTag2)
        val tag1: TextView = view.findViewById(R.id.topicTag)
        val author: TextView = view.findViewById(R.id.authorText)
        val image: ImageView = view.findViewById(R.id.thumbnailImage)
        val moduleNameImage: ImageView = view.findViewById(R.id.moduleNameImage)
        val image_recommended: ImageView = view.findViewById(R.id.image_recommended)
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
        holder.title.text = item.title
        holder.tag.text = "${item.subCategories.getOrNull(0)?.name}"
        holder.tag1.text = "${item.moduleName}"
        val inputTime = item.createdAt.toString()
        val formattedTime = convertUtcTo12HourFormat(inputTime)
        val artist = item.artist.getOrNull(0)
        val hasValidName = !artist?.firstName.isNullOrBlank() && !artist?.lastName.isNullOrBlank()
        val artistName = if (hasValidName) "${artist?.firstName} ${artist?.lastName}" else ""
        //holder.author.text = if (hasValidName) "$artistName   |   $formattedTime" else formattedTime
        holder.author.text = "${item.categoryName}  |  ${item.createdAt}"
        Glide.with(context)
            .load( "https://d1sacaybzizpm5.cloudfront.net/"+item.thumbnail?.url)
            .placeholder(R.drawable.ic_galory)
            .into(holder.image)
        Glide.with(context)
            .load( "")
            .placeholder(R.drawable.ic_db_sleepright)
            .into(holder.moduleNameImage)
        Glide.with(context)
            .load( "")
            .placeholder(R.drawable.sleep_tag)
            .into(holder.image_recommended)
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
    fun convertUtcTo12HourFormat(input: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .withZone(ZoneId.of("UTC")) // Input is in UTC
        val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a")
            .withZone(ZoneId.systemDefault()) // Convert to device local time
        val instant = Instant.parse(input)
        return outputFormatter.format(instant)
    }

    override fun getItemCount(): Int = items.size
}