package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class ThinkQuoteData(

    @SerializedName("_id"        )
    var Id         : String?,
    @SerializedName("quote"      )
    var quote      : String?,
    @SerializedName("authorName" )
    var authorName : String? ,
    @SerializedName("moduleName" )
    var moduleName : String? ,
    @SerializedName("createdAt"  )
    var createdAt  : String? ,
    @SerializedName("updatedAt"  )
    var updatedAt  : String? ,
    @SerializedName("__v"        )
    var _v         : Int?
)
