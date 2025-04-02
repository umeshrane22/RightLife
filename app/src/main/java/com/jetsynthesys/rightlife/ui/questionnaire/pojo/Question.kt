package com.jetsynthesys.rightlife.ui.questionnaire.pojo

import java.io.Serializable

data class Question(
    val questionId: String,
    val questionText: String,
    val type: String
) : Serializable
