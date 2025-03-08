package com.example.rlapp.ui.affirmation.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreateAffirmationPlaylistRequest {
    @SerializedName("list")
    @Expose
    var list: ArrayList<String>? = ArrayList()
}