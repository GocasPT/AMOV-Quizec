package pt.isec.amov.quizec.model.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int? = null,
    val content: String,
    @SerialName("image_url")
    val image: String?,
    val answers: Answer,
    @SerialName("user_id")
    val user: String
)