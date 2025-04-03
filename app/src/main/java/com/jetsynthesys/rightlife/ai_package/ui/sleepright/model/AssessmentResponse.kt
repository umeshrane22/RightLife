package com.jetsynthesys.rightlife.ai_package.ui.sleepright.model

data class AssessmentResponse(
    val result: List<AssessmentResult>,
    val recommendations: List<Recommendation>
)

data class AssessmentResult(
    val _id: String,
    val userId: String,
    val emotionalState: List<String>,
    val assessmentsTaken: List<AssessmentTaken>,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class AssessmentTaken(
    val assessment: String,
    val interpretations: Map<String, Interpretation>,
    val _id: String
)

data class Interpretation(
    val level: String,
    val score: String
)

data class Recommendation(
    val _id: String,
    val contentType: String,
    val url: String? = null,
    val previewUrl: String? = null,
    val moduleId: String,
    val categoryId: String,
    val subCategories: List<SubCategory>,
    val artist: List<Artist>,
    val title: String,
    val pricing: String,
    val thumbnail: Thumbnail,
    val desc: String,
    val episodeCount: Int,
    val isPromoted: Boolean,
    val youtubeUrl: String? = null,
    val seriesType: String,
    val meta: Meta,
    val viewCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val shareUrl: String,
    val promotedAt: String? = null,
    val moduleName: String,
    val categoryName: String,
    val tagsAsStrings: List<Tag>,
    val isWatched: Boolean,
    val isAffirmated: Boolean,
    val isFavourited: Boolean,
    val isAlarm: Boolean
)

data class SubCategory(
    val name: String,
    val _id: String
)

data class Artist(
    val firstName: String,
    val lastName: String,
    val profilePicture: String,
    val _id: String
)

data class Thumbnail(
    val url: String,
    val title: String
)

data class Meta(
    val duration: Int,
    val size: String,
    val sizeBytes: Int
)

data class Tag(
    val _id: String,
    val __v: Int,
    val appId: String,
    val createdAt: Long,
    val default: Boolean,
    val isDeleted: Boolean,
    val name: String,
    val type: String,
    val updatedAt: Long
)