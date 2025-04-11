package com.jetsynthesys.rightlife.apimodel.newquestionrequestfacescan

class HealthQuestionnaireRequest(
    val questionId: String,
    val answers: List<AnswerFaceScan>
)

class Answer(
    val question: String,
    val answer: Any
)
