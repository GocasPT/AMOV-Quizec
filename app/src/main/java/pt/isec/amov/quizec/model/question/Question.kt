package pt.isec.amov.quizec.model.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    val content: String,
    @SerialName("image_url")
    val image: String?,
    val answers: Answer
)