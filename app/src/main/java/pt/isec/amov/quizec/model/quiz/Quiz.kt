package pt.isec.amov.quizec.model.quiz

import pt.isec.amov.quizec.model.question.Question

data class Quiz(
    //val id: String,
    //val creatorId: String,
    val title: String,
    val image: String?,
    val questions: List<Question>,
    val isActive: Boolean,
    val maxTime: Long?,
    val locationRestricted: Boolean,
    //val creatorLocation: GeoPoint?,
    val immediateResults: Boolean
)

//TODO: check this later
/*data class QuestionnaireResponse {
    //val id: String
    //val questionnaireId: String
    //val userId: String
    val timestamp: Long
    val responses: Map<String, Any>
    val isAnonymous: Boolean
}

data class QuestionnaireResults {
    //val questionnaireId: String
    val aggregatedResponses: Map<String, Map<String, Int>>
    val totalResponses: Int
}*/