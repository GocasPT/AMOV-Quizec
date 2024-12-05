package pt.isec.amov.quizec.model.question

sealed class Answer {
    abstract val type: QuestionType

    //P01
    data class TrueFalse(val rightAnswer: Boolean) : Answer() {
        override val type: QuestionType = QuestionType.TRUE_FALSE
        override fun toString(): String {
            return if (rightAnswer) "True" else "False"
        }
    }
    //P02
    data class SingleChoice(val answers: Set<Pair<Boolean, String>>) : Answer() {
        override val type: QuestionType = QuestionType.SINGLE_CHOICE
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }
    //P03
    data class MultipleChoice(val answers: Set<Pair<Boolean, String>>) : Answer() {
        override val type: QuestionType = QuestionType.MULTIPLE_CHOICE
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }
    //P04
    data class Matching(val pairs: Set<Pair<String, String>>) : Answer() {
        override val type: QuestionType = QuestionType.MATCHING
        override fun toString(): String {
            return pairs.joinToString(", ") { "${it.first} -> ${it.second}" }
        }
    }
    //P05
    data class Ordering(val order: List<String>) : Answer() {
        override val type: QuestionType = QuestionType.ORDERING
        override fun toString(): String {
            return order.joinToString(", ")
        }
    }
    //P06
    data class Drag(val answers: Set<Pair<Int, String>>) : Answer() {
        override val type: QuestionType = QuestionType.DRAG
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }
    //P07 - not implemented
    //P08
    data class FillBlank(val answers: Set<Pair<Int, String>>) : Answer() {
        override val type: QuestionType = QuestionType.FILL_BLANK
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }
}