package com.example.rlapp.ui.breathwork.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BreathingData : Serializable {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("subTitle")
    @Expose
    var subTitle: String? = null

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null

    @SerializedName("breathInhaleTime")
    @Expose
    var breathInhaleTime: String? = null

    @SerializedName("breathExhaleTime")
    @Expose
    var breathExhaleTime: String? = null

    @SerializedName("breathHoldTime")
    @Expose
    var breathHoldTime: String? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null

    @SerializedName("totalTime")
    @Expose
    var totalTime: String? = null

    var isAddedToToolKit: Boolean = false
}