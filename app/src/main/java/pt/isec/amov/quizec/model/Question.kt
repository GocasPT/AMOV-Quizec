package pt.isec.amov.quizec.model

data class Question (
    //val id: String,
    //val creatorId: String,
    val title: String,
    val type: QuestionType,
    val image: String?,
    val options: List<QuestionOption>
)