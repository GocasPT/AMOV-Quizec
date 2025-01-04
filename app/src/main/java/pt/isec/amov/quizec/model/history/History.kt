package pt.isec.amov.quizec.model.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class History (
    val id: Int? = null,
    @SerialName("user_id")
    val userId: String,
    val quiz: QuizSnapshot,
    val answers: List<AnswerHistory>
    //val score: Int TODO
    //val date: Date TODO
) {
    override fun toString(): String {
        return "History:\nuserId='$userId'\nquiz=$quiz\nanswers=\n${answers.joinToString("\n")}"
    }
}