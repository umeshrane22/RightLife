package com.jetsynthesys.rightlife.ui

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.apimodel.morelikecontent.Like
import com.jetsynthesys.rightlife.databinding.RowYouMayAlsoLikeBinding
import com.jetsynthesys.rightlife.ui.Articles.ArticlesDetailActivity
import com.jetsynthesys.rightlife.ui.contentdetailvideo.ContentDetailsActivity
import com.jetsynthesys.rightlife.ui.contentdetailvideo.SeriesListActivity
import com.jetsynthesys.rightlife.ui.utility.Utils

class YouMayAlsoLikeAdapter(
    private val context: Context,
    private val contentList: List<Like>? = null
) :
    RecyclerView.Adapter<YouMayAlsoLikeAdapter.YouMayAlsoLikeViewHolder>() {

    inner class YouMayAlsoLikeViewHolder(val binding: RowYouMayAlsoLikeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouMayAlsoLikeViewHolder {
        val binding =
            RowYouMayAlsoLikeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YouMayAlsoLikeViewHolder(binding)
    }

    override fun getItemCount(): Int = contentList!!.size

    override fun onBindViewHolder(holder: YouMayAlsoLikeViewHolder, position: Int) {
        val item = contentList?.get(position)
        with(holder.binding) {
            if (item?.thumbnail?.url != null && item.thumbnail?.url?.isNotEmpty() == true) {
                Glide.with(context)
                    .load(ApiClient.CDN_URL_QA + item.thumbnail.url)
                    .transform(RoundedCorners(24))
                    .into(itemImage)
            }

            tvModuleName.text = Utils.getModuleText(item?.moduleName)
            val color = Utils.getModuleColor(context, item?.moduleId)
            imageModuleTag.imageTintList = ColorStateList.valueOf(color)
            tvCategory.text = item?.categoryName
            tvHeader.text = item?.title
            tvName.text =
                if (item?.artist != null && item.artist?.size!! > 0) item.artist?.get(0)?.firstName + " " + item.artist?.get(
                    0
                )?.lastName else ""

            //Module Name
            val moduleId = item?.moduleId
            if (moduleId.equals("EAT_RIGHT", ignoreCase = true)) {
                imgModule.setImageResource(R.drawable.ic_db_eatright)
            } else if (moduleId.equals("THINK_RIGHT", ignoreCase = true)) {
                imgModule.setImageResource(R.drawable.ic_db_thinkright)
            } else if (moduleId.equals("SLEEP_RIGHT", ignoreCase = true)) {
                imgModule.setImageResource(R.drawable.ic_db_sleepright)
            } else if (moduleId.equals("MOVE_RIGHT", ignoreCase = true)) {
                imgModule.setImageResource(R.drawable.ic_db_moveright)
            }

            if (item?.contentType.equals("TEXT", ignoreCase = true)) {
                imageContentType.setImageResource(R.drawable.ic_text_content)
            } else if (item?.contentType.equals("VIDEO", ignoreCase = true)) {
                imageContentType.setImageResource(R.drawable.ic_video_content)
            } else if (item?.contentType.equals("SERIES", ignoreCase = true)) {
                imageContentType.setImageResource(R.drawable.ic_series_content)
            } else {
                imageContentType.setImageResource(R.drawable.ic_audio_content)
            }
        }

        holder.itemView.setOnClickListener {
            if (item?.contentType.equals("TEXT", ignoreCase = true)) {
                context.startActivity(Intent(context, ArticlesDetailActivity::class.java).apply {
                    putExtra("contentId", item?.id)
                })
            } else if (item?.contentType
                    .equals("VIDEO", ignoreCase = true) || item?.contentType
                    .equals("AUDIO", ignoreCase = true)
            ) {
                context.startActivity(
                    Intent(
                        holder.itemView.context,
                        ContentDetailsActivity::class.java
                    ).apply {
                        putExtra("contentId", item?.id)
                    })
            } else if (item?.contentType.equals("SERIES", ignoreCase = true)) {
                context.startActivity(Intent(context, SeriesListActivity::class.java).apply {
                    putExtra("contentId", item?.id)
                })
            }
        }
    }
}