package pt.isec.amov.quizec.model.quiz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pt.isec.amov.quizec.model.question.Question

@Serializable
data class Quiz(
    val id: Int,
    val title: String,
    @SerialName("image_url")
    val image: String?,
    val owner: String,
    var questions: List<Question>? = null,
    //val isActive: Boolean,
    //val maxTime: Long?,
    //val locationRestricted: Boolean,
    //val creatorLocation: GeoPoint?,
    //val immediateResults: Boolean
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