package com.example.rlapp.ui.NewSleepSounds.userplaylistmodel

import com.example.rlapp.ui.NewSleepSounds.newsleepmodel.Service
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewReleaseData {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("services")
    @Expose
    var services: List<Service>? = null
}