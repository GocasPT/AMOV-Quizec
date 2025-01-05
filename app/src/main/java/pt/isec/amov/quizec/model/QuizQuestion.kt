package pt.isec.amov.quizec.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    val quiz_id: Int,
    val question_id: Int
)