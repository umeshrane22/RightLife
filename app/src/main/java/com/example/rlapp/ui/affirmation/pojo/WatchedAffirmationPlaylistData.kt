package com.example.rlapp.ui.affirmation.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WatchedAffirmationPlaylistData {
    @SerializedName("day")
    @Expose
    var day: String? = null

    @SerializedName("totalSession")
    @Expose
    var totalSession: Int? = null

    @SerializedName("duration")
    @Expose
    var duration: Int? = null

    @SerializedName("readAffirmation")
    @Expose
    var readAffirmation: Int? = null
}