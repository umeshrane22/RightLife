package com.jetsynthesys.rightlife.ai_package.model

data class AddEmojiRequest(
    var title : String?,
    var questionId : String?,
    var answer : String?,
    var emotion : String?,
    var tags : ArrayList<String>?,
)
