package pt.isec.amov.quizec.model.quiz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pt.isec.amov.quizec.model.question.Question

@Serializable
data class Quiz(
    var id: Int? = null,
    val title: String,
    @SerialName("image_url")
    val image: String?,
    val owner: String,
    var questions: List<Question>? = null,
)