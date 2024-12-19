package pt.isec.amov.quizec.model.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Answer {
    //The 'type' field is used in kotlinx.serialization to determine the type of the object
    abstract val answerType: QuestionType

    //P01
    @Serializable
    @SerialName("TrueFalse")
    data class TrueFalse(val rightAnswer: Boolean) : Answer() {
        override val answerType: QuestionType = QuestionType.TRUE_FALSE
        override fun toString(): String {
            return if (rightAnswer) "True" else "False"
        }
    }

    //P02
    @Serializable
    @SerialName("SingleChoice")
    data class SingleChoice(val answers: Set<Pair<Boolean, String>>) : Answer() {
        override val answerType: QuestionType = QuestionType.SINGLE_CHOICE
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }

    //P03
    @Serializable
    @SerialName("MultipleChoice")
    data class MultipleChoice(val answers: Set<Pair<Boolean, String>>) : Answer() {
        override val answerType: QuestionType = QuestionType.MULTIPLE_CHOICE
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }

    //P04
    @Serializable
    @SerialName("Matching")
    data class Matching(val pairs: Set<Pair<String, String>>) : Answer() {
        override val answerType: QuestionType = QuestionType.MATCHING
        override fun toString(): String {
            return pairs.joinToString(", ") { "${it.first} -> ${it.second}" }
        }
    }

    //P05
    @Serializable
    @SerialName("Ordering")
    data class Ordering(val order: List<String>) : Answer() {
        override val answerType: QuestionType = QuestionType.ORDERING
        override fun toString(): String {
            return order.joinToString(", ")
        }
    }

    //P06
    @Serializable
    @SerialName("Drag")
    data class Drag(val answers: Set<Pair<Int, String>>) : Answer() {
        override val answerType: QuestionType = QuestionType.DRAG
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }

    //P07 - not implemented

    //P08
    @Serializable
    @SerialName("FillBlank")
    data class FillBlank(val answers: Set<Pair<Int, String>>) : Answer() {
        override val answerType: QuestionType = QuestionType.FILL_BLANK
        override fun toString(): String {
            return answers.joinToString(", ") { it.second }
        }
    }
}