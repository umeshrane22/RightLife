package com.jetsynthesys.rightlife

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

fun Context.showCustomToast(message: String) {
    val inflater = LayoutInflater.from(this)
    val layout = inflater.inflate(R.layout.custom_toast_new, null)

    val toastText = layout.findViewById<TextView>(R.id.tvMessage)
    toastText.text = message

    val ivIcon = layout.findViewById<ImageView>(R.id.ivIcon)
    ivIcon.setImageResource(R.drawable.close_journal)

    val toast = Toast(this)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    toast.show()
}