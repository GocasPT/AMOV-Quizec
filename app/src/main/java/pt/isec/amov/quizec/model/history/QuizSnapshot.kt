package pt.isec.amov.quizec.model.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizSnapshot (
    val title: String,
    @SerialName("image_url")
    val image: String?,
    val owner: String
)