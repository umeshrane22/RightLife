package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jetsynthesys.rightlife.ai_package.model.MealDetails
import com.jetsynthesys.rightlife.ai_package.model.response.SearchResultItem
import com.jetsynthesys.rightlife.ai_package.model.response.SnapRecipeData
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnapDishLocalListModel(
    @SerializedName("data")
    var data: ArrayList<SearchResultItem>
): Parcelable
