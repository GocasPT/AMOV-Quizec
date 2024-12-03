package pt.isec.amov.quizec.model.question

sealed class Answer {
    //P01
    data class TrueFalse(val rightAnswer: Boolean) : Answer() {
        override fun toString(): String {
            return if (rightAnswer) "True" else "False"
        }
    }
    //P02
    data class SingleChoice(val answers: Set<String>, val rightAnswer: String) : Answer() {
        override fun toString(): String {
            return rightAnswer
        }
    }
    //P03
    data class MultipleChoice(val answers: Set<String>, val rightAnswers: Set<String>) : Answer() {
        override fun toString(): String {
            return rightAnswers.joinToString(", ")
        }
    }
    //P04
    data class Matching(val pairs: Set<Pair<String, String>>) : Answer() {
        override fun toString(): String {
            return pairs.joinToString(", ") { "${it.first} -> ${it.second}" }
        }
    }
    //P05
    data class Ordering(val order: List<String>) : Answer() {
        override fun toString(): String {
            return order.joinToString(", ")
        }
    }
    //P06
    data class Drag(val answers: Set<Pair<Int, String>>) : Answer() {
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }
    //P08
    data class FillBlank(val answers: Set<Pair<Int, String>>) : Answer() {
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }
}