package com.jetsynthesys.rightlife.ui.contentdetailvideo.model

data class SeriesResponse(
    val success: Boolean,
    val statusCode: Int,
    val data: SeriesData
)

data class SeriesData(
    val _id: String,
    val contentType: String,
    val moduleId: String,
    val categoryId: String,
    val subCategories: List<String>,
    val artist: List<Artist>,
    val title: String,
    val thumbnail: Thumbnail,
    val desc: String,
    val tags: List<Tag>,
    val episodeCount: Int,
    val isPromoted: Boolean,
    val isDeleted: Boolean,
    val seriesType: String,
    val meta: Meta,
    val viewCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val shareUrl: String,
    val promotedAt: String,
    val pricing: String,
    val isActive: Boolean,
    val order: Int,
    val article: List<Any>, // or define Article class if needed
    val categoryName: String,
    val isFavourited: Boolean,
    val episodes: ArrayList<Episode>,
    val recommended: Recommended
)


data class Artist(
    val _id: String,
    val firstName: String,
    val lastName: String? = null,
    val profilePicture: String
)

data class Thumbnail(
    val url: String,
    val title: String
)

data class Meta(
    val duration: Int,
    val size: String,
    val sizeBytes: Long
)

data class Tag(
    val name: String,
    val _id: String
)

data class Episode(
    val _id: String,
    val contentId: String,
    val type: String,
    val title: String,
    val episodeNumber: Int,
    val desc: String,
    val tags: List<Tag>,
    val artist: List<Artist>,
    val thumbnail: Thumbnail,
    val pricingTier: String,
    val youtubeUrl: String,
    val shareUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val viewCount: Int,
    val meta: Meta,
    val isWatched: Boolean,
    var isFavourited: Boolean
)

data class Recommended(
    val count: Int,
    val recommendedList: List<RecommendedListItem>
)

data class RecommendedListItem(
    val _id: String,
    val contentType: String,
    val moduleId: String,
    val categoryId: String,
    val title: String,
    val thumbnail: Thumbnail,
    val desc: String,
    val episodeCount: Int,
    val isPromoted: Boolean,
    val seriesType: String,
    val createdAt: String,
    val updatedAt: String,
    val promotedAt: String,
    val subCategories: List<SubCategory>,
    val meta: Meta,
    val artist: List<Artist>,
    val shareUrl: String,
    val pricing: String,
    val viewCount: Int,
    val isActive: Boolean,
    val order: Int,
    val moduleName: String,
    val categoryName: String,
    val isWatched: Boolean,
    val isAffirmated: Boolean,
    val isFavourited: Boolean,
    val isAlarm: Boolean
)

data class SubCategory(
    val name: String,
    val _id: String
)
