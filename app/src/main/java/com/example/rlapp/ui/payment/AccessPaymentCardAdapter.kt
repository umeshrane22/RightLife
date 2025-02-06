package com.example.rlapp.ui.payment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rlapp.R

class AccessPaymentCardAdapter(
    private val context: Context,
    private val paymentCardList: ArrayList<PaymentCardList>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<AccessPaymentCardAdapter.PaymentCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentCardViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_payment_card, parent, false)
        return PaymentCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return paymentCardList.size
    }

    override fun onBindViewHolder(holder: PaymentCardViewHolder, position: Int) {
        val paymentCard = paymentCardList[position]
        val cardColor = ColorStateList.valueOf(Color.parseColor(paymentCard.card.colorCode.card))
        val itemColor = ColorStateList.valueOf(Color.parseColor(paymentCard.card.colorCode.item))
        holder.llPaymentCard.backgroundTintList = cardColor
        holder.rlPaymentCard.backgroundTintList = cardColor
        holder.tvOfferNumbers.backgroundTintList = itemColor

        holder.tvCardName.text = paymentCard.card.title
        holder.tvCardType.text = paymentCard.card.subTitle
        holder.tvCardDesc.text = paymentCard.card.desc
        holder.tvPrice.text = "\u20B9 " + paymentCard.price.inr
        holder.tvOfferNumbers.text = "${paymentCard.card.items.size} Offers Available"

        holder.tvOfferNumbers.setOnClickListener {
            onItemClickListener.onOfferClick(paymentCard.card)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(paymentCard)
        }

        holder.tvBuy.setOnClickListener {
            onItemClickListener.onBuyClick(paymentCard.card)
        }

    }

    interface OnItemClickListener {
        fun onItemClick(paymentCard: PaymentCardList)
        fun onBuyClick(paymentCard: PaymentCard)
        fun onOfferClick(paymentCard: PaymentCard)
    }

    class PaymentCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llPaymentCard = itemView.findViewById<LinearLayout>(R.id.ll_payment_card)
        val tvOffer = itemView.findViewById<TextView>(R.id.tv_offer)
        val rlPaymentCard = itemView.findViewById<RelativeLayout>(R.id.rl_payment_card)
        val cornerImage = itemView.findViewById<ImageView>(R.id.image_corner)
        val tvCardName = itemView.findViewById<TextView>(R.id.tv_cardtype2)
        val tvCardType = itemView.findViewById<TextView>(R.id.tv_cardtype3)
        val tvPrice = itemView.findViewById<TextView>(R.id.tv_cardvalue2)
        val tvCardDesc = itemView.findViewById<TextView>(R.id.tv_cardcontent2)
        val tvOfferNumbers = itemView.findViewById<TextView>(R.id.tv_numberofoffers2)
        val tvBuy = itemView.findViewById<TextView>(R.id.btn_howitworks2)
    }
}