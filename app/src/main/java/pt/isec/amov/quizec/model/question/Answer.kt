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
    data class MultipleChoice(val answers: Set<String>, val rightAnswers: Set<String>) : Answer() {
        override fun toString(): String {
            return rightAnswers.joinToString(", ")
        }
    }
    data class Matching(val pairs: Set<Pair<String, String>>) : Answer() {
        override fun toString(): String {
            return pairs.joinToString(", ") { "${it.first} -> ${it.second}" }
        }
    }
    data class Ordering(val order: Set<String>) : Answer() {
        override fun toString(): String {
            return order.joinToString(", ")
        }
    }
}