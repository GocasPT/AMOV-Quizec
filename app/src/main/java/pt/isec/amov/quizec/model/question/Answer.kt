package pt.isec.amov.quizec.model.question

sealed class Answer {
    data class TrueFalse(val answer: Boolean) : Answer() {
        override fun toString(): String {
            return if (answer) "True" else "False"
        }
    }
}