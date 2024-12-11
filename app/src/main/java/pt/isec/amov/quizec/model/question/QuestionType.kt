package pt.isec.amov.quizec.model.question

enum class QuestionType(val displayName: String) {
    TRUE_FALSE("True or False"),
    SINGLE_CHOICE("Single Choice"),
    MULTIPLE_CHOICE("Multiple Choice"),
    MATCHING("Matching"),
    ORDERING("Ordering"),
    DRAG("Drag"),
    //ASSOCIATION("Association"),
    FILL_BLANK("Fill in the blank"),
}