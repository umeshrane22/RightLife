package com.example.rlapp.ui.affirmation.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WatchAffirmationPlaylistRequest {
    @SerializedName("duration")
    @Expose
    var duration: Int? = null

    @SerializedName("totalSession")
    @Expose
    var totalSession: Int? = null

    @SerializedName("readAffirmation")
    @Expose
    var readAffirmation: Int? = null
}