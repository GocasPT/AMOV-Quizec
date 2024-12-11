package pt.isec.amov.quizec.model.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Answer {
    abstract val type: QuestionType

    //P01
    @Serializable
    data class TrueFalse(val rightAnswer: Boolean) : Answer() {
        @SerialName("TrueFalse")
        override val type: QuestionType = QuestionType.TRUE_FALSE
        override fun toString(): String {
            return if (rightAnswer) "True" else "False"
        }
    }

    //P02
    @Serializable
    data class SingleChoice(val answers: Set<Pair<Boolean, String>>) : Answer() {
        override val type: QuestionType = QuestionType.SINGLE_CHOICE
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }

    //P03
    @Serializable
    data class MultipleChoice(val answers: Set<Pair<Boolean, String>>) : Answer() {
        override val type: QuestionType = QuestionType.MULTIPLE_CHOICE
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }

    //P04
    @Serializable
    data class Matching(val pairs: Set<Pair<String, String>>) : Answer() {
        override val type: QuestionType = QuestionType.MATCHING
        override fun toString(): String {
            return pairs.joinToString(", ") { "${it.first} -> ${it.second}" }
        }
    }

    //P05
    @Serializable
    data class Ordering(val order: List<String>) : Answer() {
        override val type: QuestionType = QuestionType.ORDERING
        override fun toString(): String {
            return order.joinToString(", ")
        }
    }

    //P06
    @Serializable
    data class Drag(val answers: Set<Pair<Int, String>>) : Answer() {
        override val type: QuestionType = QuestionType.DRAG
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }

    //P07 - not implemented

    //P08
    @Serializable
    data class FillBlank(val answers: Set<Pair<Int, String>>) : Answer() {
        override val type: QuestionType = QuestionType.FILL_BLANK
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }
}