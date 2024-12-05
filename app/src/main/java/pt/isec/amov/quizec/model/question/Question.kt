package pt.isec.amov.quizec.model.question

data class Question (
    val id: Int,
    val content: String,
    val image: String?,
    val answers: Answer
)