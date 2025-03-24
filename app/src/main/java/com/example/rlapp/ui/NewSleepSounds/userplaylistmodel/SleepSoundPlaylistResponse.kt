package com.example.rlapp.ui.NewSleepSounds.userplaylistmodel

import com.example.rlapp.ui.NewSleepSounds.newsleepmodel.Meta
import com.example.rlapp.ui.NewSleepSounds.newsleepmodel.Service
import com.example.rlapp.ui.NewSleepSounds.newsleepmodel.Tag
import java.io.Serializable

data class SleepSoundPlaylistResponse(
    val success: Boolean,
    val statusCode: Int,
    val data: List<Service>
): Serializable

/*data class Service(
    val _id: String,
    val catagoryId: String,
    val title: String,
    val url: String,
    val desc: String,
    val image: String,
    val tags: ArrayList<Tag>,
    val meta: Meta,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
): Serializable*/
/*data class SleepSoundPlaylistItem(
    val _id: String,
    val catagoryId: String,
    val title: String,
    val url: String,
    val desc: String,
    val image: String,
    val tags: List<Tag>,
    val meta: Meta
): Serializable*/

data class Tag(
    val name: String,
    val _id: String
) : Serializable

data class Meta(
    val duration: Int,
    val size: String,
    val sizeBytes: Int
): Serializable
