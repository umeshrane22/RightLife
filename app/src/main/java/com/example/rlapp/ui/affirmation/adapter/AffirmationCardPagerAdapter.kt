package com.example.rlapp.ui.affirmation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.rlapp.R
import com.example.rlapp.ui.affirmation.pojo.AffirmationSelectedCategoryData
import com.example.rlapp.ui.utility.Utils
import com.zhpan.indicator.IndicatorView

class AffirmationCardPagerAdapter(
    private val items: ArrayList<AffirmationSelectedCategoryData>,
    private val context: Context,
    val viewPager: ViewPager
) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(R.layout.row_affirmation_card, container, false)

        val item = items[position]

        // Set up views
        val imageAffirmationCard: ImageView = view.findViewById(R.id.imageAffirmationCard)
        val cardViewAffirmation: CardView = view.findViewById(R.id.cardViewAffirmation)
        val tvAffirmationTitle: TextView = view.findViewById(R.id.tvAffirmationTitle)
        val tvAffirmationAuthor: TextView = view.findViewById(R.id.tvAffirmationAuthor)
        val pageCount: TextView = view.findViewById(R.id.pageCount)
        val indicatorView : IndicatorView = view.findViewById(R.id.indicator_view)

        /*GlideApp.with(context)
            .load(ApiClient.CDN_URL_QA + item.image)
            .placeholder(R.drawable.image_affirmation_card)
            .into(imageAffirmationCard)*/

        imageAffirmationCard.imageTintList = Utils.getColorStateListFromColorCode(item.textColor)
        cardViewAffirmation.backgroundTintList = Utils.getColorStateListFromColorCode(item.bgColor)

        tvAffirmationTitle.text = item.title
        tvAffirmationAuthor.text = "${item.artist?.firstName} ${item.artist?.firstName}"
        tvAffirmationAuthor.setTextColor(Utils.getColorFromColorCode(item.textColor))

        // Load image using Glide
        //Glide.with(context).load(item).into(image)

        indicatorView.setupWithViewPager(viewPager = viewPager)
        pageCount.text = "${position + 1}/${items.size}"

        container.addView(view)
        return view
    }

    override fun getCount() = items.size

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}