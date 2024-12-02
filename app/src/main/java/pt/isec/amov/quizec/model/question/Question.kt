package pt.isec.amov.quizec.model.question

data class Question (
    val content: String,
    val type: QuestionType,
    val image: String?,
    val answers: List<Answer>
)