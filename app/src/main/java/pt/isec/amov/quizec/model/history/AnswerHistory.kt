package pt.isec.amov.quizec.model.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pt.isec.amov.quizec.model.question.Answer

@Serializable
data class AnswerHistory (
    val content: String,
    @SerialName("image_url")
    val image: String?,
    val correctAnswer: Answer,
    val userAnswer: Answer,
    val score : Double
) {
    override fun toString(): String {
        return "\nTitle='$content'\nimage=$image\ncorrectAnswer=$correctAnswer\nuserAnswer=$userAnswer\nscore=$score"
    }
}