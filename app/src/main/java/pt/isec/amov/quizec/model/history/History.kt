package pt.isec.amov.quizec.model.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class History (
    val id: Int? = null,
    @SerialName("user_id")
    val userId: String,
    val quiz: QuizSnapshot,
    val answers: List<AnswerHistory>,
    val score: Int, //0-20
    val date: String
)