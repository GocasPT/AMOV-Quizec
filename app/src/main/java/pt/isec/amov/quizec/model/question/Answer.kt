package pt.isec.amov.quizec.model.question

sealed class Answer {
    data class TrueFalse(val rightAnswer: Boolean) : Answer() {
        override fun toString(): String {
            return if (rightAnswer) "True" else "False"
        }
    }
    data class SingleChoice(val answers: Set<String>, val rightAnswer: String) : Answer() {
        override fun toString(): String {
            return rightAnswer
        }
    }
}